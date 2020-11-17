//package cn.jupitermouse.lineage.graph.metadata;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import javax.sql.DataSource;
//
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import cn.jupitermouse.lineage.graph.domain.model.FieldEntity;
//import cn.jupitermouse.lineage.graph.domain.model.SchemaEntity;
//import cn.jupitermouse.lineage.graph.domain.model.TableEntity;
//import cn.jupitermouse.lineage.graph.domain.repository.FieldRepository;
//import cn.jupitermouse.lineage.graph.domain.repository.SchemaRepository;
//import cn.jupitermouse.lineage.graph.domain.repository.TableRepository;
//import cn.jupitermouse.lineage.graph.domain.service.OfRelService;
//import cn.jupitermouse.lineage.metadata.ingest.infra.config.DataSourceHolder;
//import cn.jupitermouse.lineage.metadata.ingest.infra.util.MetaDataUtil;
//import cn.jupitermouse.lineage.metadata.ingest.service.ModelIngestService;
//
///**
// * <p>
// * 模型提取服务
// * </p>
// *
// * @author JupiterMouse 2020/09/23
// * @since 1.0
// */
//@Service
//public class ModelIngestServiceImpl implements ModelIngestService {
//
//    private final OfRelService ofRelService;
//    private final TableRepository tableRepository;
//    private final SchemaRepository schemaRepository;
//    private final FieldRepository fieldRepository;
//
//    public ModelIngestServiceImpl(OfRelService ofRelService,
//            TableRepository tableRepository,
//            SchemaRepository schemaRepository,
//            FieldRepository fieldRepository) {
//        this.ofRelService = ofRelService;
//        this.tableRepository = tableRepository;
//        this.schemaRepository = schemaRepository;
//        this.fieldRepository = fieldRepository;
//    }
//
//    @Override
//    public void schemaInfoIngest(String cluster, String catalog, String schemaPattern) {
//        final DataSource dataSource = DataSourceHolder.getDataSource(cluster);
//        // 获取catalog
//        if (StringUtils.isEmpty(catalog)) {
//            catalog = MetaDataUtil.getCatalog(dataSource);
//        }
//        if (StringUtils.isEmpty(schemaPattern)) {
//            schemaPattern = MetaDataUtil.getSchema(dataSource);
//        }
//        final List<Map<String, Object>> schemaMapList = MetaDataUtil.schemas(dataSource, catalog, schemaPattern);
//        if (CollectionUtils.isEmpty(schemaMapList)) {
//            return;
//        }
//        List<SchemaEntity> schemaEntityList = new ArrayList<>();
//        for (Map<String, Object> mapList : schemaMapList) {
//            SchemaEntity schemaEntity = this.mapConvertSchemaEntity(cluster, catalog, mapList);
//            String schema = schemaEntity.getSchema();
//            final List<Map<String, Object>> tableInfoList = MetaDataUtil.tableInfo(dataSource, catalog, schema, null);
//            String finalCatalog = catalog;
//            final List<TableEntity> tableEntities = tableInfoList.stream()
//                    .map(map -> mapConvertTableEntity(cluster, finalCatalog, schema, map))
//                    .collect(Collectors.toList());
//            schemaEntity.setTableOfSchemaList(tableEntities);
//            schemaEntityList.add(schemaEntity);
//        }
//        // 节点信息
//        ofRelService.createTableOfSchemaRelList(schemaEntityList);
//        schemaEntityList.forEach(schemaEntity -> tableRepository.saveAll(schemaEntity.getTableOfSchemaList()));
//        // 关系建立
//        schemaEntityList.forEach(schemaEntity -> this.ofRelService
//                .createTableOfSchemaRel(schemaEntity.getTableOfSchemaList(), schemaEntity));
//    }
//
//    @Override
//    public void tableInfoIngest(String cluster, String catalog, String schema, String tablePattern) {
//        final DataSource dataSource = DataSourceHolder.getDataSource(cluster);
//        if (StringUtils.isEmpty(catalog)) {
//            catalog = MetaDataUtil.getCatalog(dataSource);
//        }
//        if (StringUtils.isEmpty(schema)) {
//            schema = MetaDataUtil.getSchema(dataSource);
//        }
//        final List<Map<String, Object>> tableMapList = MetaDataUtil
//                .tableInfo(dataSource, catalog, schema, tablePattern);
//        // 构建table
//        if (CollectionUtils.isEmpty(tableMapList)) {
//            return;
//        }
//
//        List<TableEntity> tableEntityList = new ArrayList<>();
//        // 构建table 下field
//        for (Map<String, Object> mapList : tableMapList) {
//            // table
//            TableEntity tableEntity = this.mapConvertTableEntity(cluster, catalog, schema, mapList);
//            String tableName = tableEntity.getTable();
//            final List<Map<String, Object>> fieldInfoMap = MetaDataUtil
//                    .fieldInfo(dataSource, catalog, schema, tableName, null);
//            String finalCatalog1 = catalog;
//            String finalSchema = schema;
//            final List<FieldEntity> fieldEntityList = fieldInfoMap.stream()
//                    .map(m -> this.mapConvertFieldEntity(cluster, finalCatalog1, finalSchema, tableName, m))
//                    .collect(Collectors.toList());
//            tableEntity.setFieldOfTableList(fieldEntityList);
//            tableEntityList.add(tableEntity);
//        }
//        ofRelService.createFieldOfTableRelList(tableEntityList);
//        final List<FieldEntity> fieldEntities = tableEntityList.stream()
//                .flatMap(tableEntity -> tableEntity.getFieldOfTableList().stream())
//                .collect(Collectors.toList());
//        fieldRepository.saveAll(fieldEntities);
//        tableEntityList.forEach(
//                tableEntity -> this.ofRelService.createFieldOfTableRel(tableEntity.getFieldOfTableList(), tableEntity));
//
//    }
//
//    private SchemaEntity mapConvertSchemaEntity(String cluster, String catalog, Map<String, Object> map) {
//        final SchemaEntity schemaEntity = SchemaEntity.builder().build();
//        schemaEntity.setClusterName(cluster);
//        schemaEntity.setDatabase(catalog);
//        Optional.ofNullable(map.get("TABLE_SCHEM")).ifPresent(s -> {
//            schemaEntity.setSchema((String) s);
//        });
//        schemaEntity.setGraphId();
//        return schemaEntity;
//    }
//
//    private TableEntity mapConvertTableEntity(String cluster, String catalog, String schema, Map<String, Object> map) {
//        final TableEntity tableEntity = TableEntity.builder().build();
//        tableEntity.setClusterName(cluster);
//        tableEntity.setDatabase(catalog);
//        tableEntity.setSchema(schema);
//        tableEntity.setTable((String) map.get("TABLE_NAME"));
//
//        Map<String, String> attrs = new HashMap<>();
//        Optional.ofNullable(map.get("TABLE_TYPE")).ifPresent(v -> attrs.put("table_type", v.toString()));
//        Optional.ofNullable(map.get("REMARKS")).ifPresent(v -> attrs.put("remarks", v.toString()));
//        Optional.ofNullable(map.get("TABLE_TYPE")).ifPresent(v -> attrs.put("table_type", v.toString()));
//        tableEntity.setAttrs(attrs);
//        tableEntity.setGraphId();
//
//        Object tableType = map.get("TABLE_TYPE");
//
//        if ("VIEW".equals(tableType)) {
//            attrs.put("viewFlag", "1");
//        } else {
//            attrs.put("viewFlag", "2");
//        }
//        return tableEntity;
//    }
//
//    private FieldEntity mapConvertFieldEntity(String cluster, String catalog, String schema, String table,
//            Map<String, Object> map) {
//        final FieldEntity fieldEntity = FieldEntity.builder().build();
//        fieldEntity.setClusterName(cluster);
//        fieldEntity.setDatabase(catalog);
//        fieldEntity.setSchema(schema);
//        fieldEntity.setTable(table);
//        fieldEntity.setField((String) map.get("COLUMN_NAME"));
//
//        Map<String, String> attrs = new HashMap<>();
//
//        Optional.ofNullable(map.get("DATA_TYPE")).ifPresent(v -> attrs.put("data_type", v.toString()));
//        Optional.ofNullable(map.get("TYPE_NAME")).ifPresent(v -> attrs.put("type_name", v.toString()));
//        Optional.ofNullable(map.get("COLUMN_DEF")).ifPresent(v -> attrs.put("remark", v.toString()));
//
//        fieldEntity.setAttrs(attrs);
//        fieldEntity.setGraphId();
//
//        return fieldEntity;
//    }
//
//}
