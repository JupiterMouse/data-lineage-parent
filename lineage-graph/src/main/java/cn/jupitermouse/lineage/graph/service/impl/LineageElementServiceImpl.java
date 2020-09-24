//package cn.jupitermouse.lineage.graph.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import cn.jupitermouse.lineage.common.message.SqlRequestDTO;
//import cn.jupitermouse.lineage.graph.constats.NeoConstant;
//import cn.jupitermouse.lineage.graph.entity.ColumnEntity;
//import cn.jupitermouse.lineage.graph.entity.TableEntity;
//import cn.jupitermouse.lineage.graph.repository.ColumnRepository;
//import cn.jupitermouse.lineage.graph.repository.TableGraphRepository;
//import cn.jupitermouse.lineage.graph.service.LineageElementService;
//import cn.jupitermouse.lineage.parser.durid.analyse.LineageAnalyzer;
//import cn.jupitermouse.lineage.parser.durid.dto.LineageColumnDTO;
//import cn.jupitermouse.lineage.parser.model.ColumnNode;
//import cn.jupitermouse.lineage.parser.model.TableNode;
//import cn.jupitermouse.lineage.parser.model.TreeNode;
//
//import static cn.jupitermouse.lineage.graph.constats.NeoConstant.*;
//
///**
// * <p>
// * 血缘关系写入图数据库
// * </p>
// *
// * @author JupiterMouse 2020/09/09
// * @since 1.0
// */
//@Service
//public class LineageElementServiceImpl implements LineageElementService {
//
//    private final TableGraphRepository tableRepository;
//
//    private final ColumnRepository columnRepository;
//
//    public LineageElementServiceImpl(TableGraphRepository tableRepository, ColumnRepository columnRepository) {
//        this.tableRepository = tableRepository;
//        this.columnRepository = columnRepository;
//    }
//
//    @Override
//    public void ingestTableLineage(String dbType, String sql) {
//        LineageAnalyzer lineageAnalyzer = new LineageAnalyzer();
//        TreeNode<TableNode> root = lineageAnalyzer
//                .getLineageTree(SqlRequestDTO.builder().dbType(dbType).sql(sql).build());
//        List<TableNode> sourceTableNodeList = new ArrayList<>();
//        lineageAnalyzer.traverseTableLineageTree(root, sourceTableNodeList);
//        // 构建写入图的对象
//        List<TableEntity> fromTableEntityList = sourceTableNodeList.stream().map(this::transform2TableEntity)
//                .collect(Collectors.toList());
//        TableEntity rootTableEntity = this.transform2TableEntity(root.getValue());
//        rootTableEntity.setFromTables(fromTableEntityList);
//        tableRepository.save(rootTableEntity);
//    }
//
//    @Override
//    public void ingestColumnLineage(String dbType, String sql) {
//        LineageAnalyzer lineageAnalyzer = new LineageAnalyzer();
//        List<LineageColumnDTO> lineageColumnDTOList = lineageAnalyzer
//                .getColumnLineage(SqlRequestDTO.builder().dbType(dbType).sql(sql).build());
//        List<ColumnEntity> resultColumnEntityList = new ArrayList<>();
//        for (LineageColumnDTO lineageColumnDTO : lineageColumnDTOList) {
//            ColumnEntity rootColumnEntity = this.transform2ColumnEntity(lineageColumnDTO.getColumn());
//            List<ColumnEntity> sourceColumnEntityList = lineageColumnDTO.getSourceColumnList().stream()
//                    .map(this::transform2ColumnEntity).collect(Collectors.toList());
//            rootColumnEntity.setColumnSource(sourceColumnEntityList);
//            resultColumnEntityList.add(rootColumnEntity);
//        }
//        columnRepository.saveAll(resultColumnEntityList);
//    }
//
//    @Override
//    public void ingestTableLineage(String cluster, String catalog, String schema, String dbType, String sql) {
//        LineageAnalyzer lineageAnalyzer = new LineageAnalyzer();
//        TreeNode<TableNode> root = lineageAnalyzer
//                .getLineageTree(SqlRequestDTO.builder().dbType(dbType).sql(sql).build());
//
//        List<TableNode> sourceTableNodeList = new ArrayList<>();
//        lineageAnalyzer.traverseTableLineageTree(root, sourceTableNodeList);
//        // 请求刷新 neo4j 的table  schema
//
//        // 覆盖为空的 catalog，schema，schema
//
//
//
//
//        // 构建写入图的对象
//        List<TableEntity> fromTableEntityList = sourceTableNodeList.stream().map(this::transform2TableEntity)
//                .collect(Collectors.toList());
//        TableEntity rootTableEntity = this.transform2TableEntity(root.getValue());
//        rootTableEntity.setFromTables(fromTableEntityList);
//        tableRepository.save(rootTableEntity);
//    }
//
//    private TableEntity transform2TableEntity(TableNode table) {
//        return TableEntity.builder()
//                .id(this.getTableIndex(table))
//                .name(table.getName())
//                .build();
//    }
//
//    private ColumnEntity transform2ColumnEntity(ColumnNode column) {
//        return ColumnEntity.builder()
//                .id(this.getColumnIndex(column))
//                .name(column.getName())
//                .tableName(column.getOwner().getName())
//                .build();
//
//    }
//
//    private String getTableIndex(TableNode table) {
//        return String
//                .format(FT_TABLE_INDEX, DEFAULT_BUSINESS_CODE, DEFAULT_DB_NAME, DEFAULT_DB_SCHEMA, table.getName());
//    }
//
//    private String getColumnIndex(ColumnNode column) {
//        return String.format(NeoConstant.FT_COLUMN_INDEX, DEFAULT_BUSINESS_CODE, DEFAULT_DB_NAME, DEFAULT_DB_SCHEMA,
//                column.getOwner().getName(), column.getName());
//    }
//
//
//}
