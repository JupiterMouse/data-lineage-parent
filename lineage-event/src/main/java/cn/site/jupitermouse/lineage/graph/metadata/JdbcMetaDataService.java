package cn.site.jupitermouse.lineage.graph.metadata;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import cn.site.jupitermouse.lineage.graph.domain.model.FieldNode;
import cn.site.jupitermouse.lineage.graph.domain.model.SchemaNode;
import cn.site.jupitermouse.lineage.graph.domain.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@Component
public class JdbcMetaDataService implements MetaDataService {


    @Override
    public void querySchema(SchemaNode schemaNode) {
        DataSource dataSource = DataSourceHolder.getDataSource(schemaNode.getClusterName());
        List<Map<String, Object>> schemas = MetaDataUtil.schemas(
                dataSource,
                schemaNode.getCatalogName(),
                schemaNode.getOriginSchemaName()
        );
        // todo schema 补充内容
    }

    @Override
    public void queryTable(TableNode tableNode) {
        DataSource dataSource = DataSourceHolder.getDataSource(tableNode.getClusterName());
        List<Map<String, Object>> tables = MetaDataUtil.tableInfo(
                dataSource,
                tableNode.getCatalogName(),
                tableNode.getOriginSchemaName(),
                tableNode.getTableName()
        );
        // todo table 补充内容
    }

    @Override
    public void queryField(FieldNode fieldNode) {
        DataSource dataSource = DataSourceHolder.getDataSource(fieldNode.getClusterName());
        List<Map<String, Object>> fieldList = MetaDataUtil.fieldInfo(
                dataSource,
                fieldNode.getCatalogName(),
                fieldNode.getOriginSchemaName(),
                fieldNode.getTableName(),
                fieldNode.getFieldName()
        );
        // todo field 补充内容
    }

    @Override
    public List<FieldNode> queryField(SqlRequestContext request, String targetTableSchema, String targetTableName) {
        DataSource dataSource = DataSourceHolder.getDataSource(request.getClusterName());
        List<Map<String, Object>> fieldList = MetaDataUtil.fieldInfo(
                dataSource,
                request.getCatalogName(),
                targetTableSchema,
                targetTableName,
                null
        );
        List<FieldNode> fieldNodeList = fieldList.stream()
                .map(column -> {
                    String schema = null;
                    try (Connection connection = dataSource.getConnection()) {
                        schema = Optional.ofNullable(targetTableSchema)
                                .orElse(connection.getSchema());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FieldNode fieldNode = new FieldNode(
                            request.getPlatformName(),
                            request.getClusterName(),
                            request.getCatalogName(),
                            schema,
                            targetTableName,
                            (String) column.get("TABLE_NAME")
                    );
                    fieldNode.setTenantId(request.getTenantId());
                    fieldNode.setDatasourceCode(request.getDatasourceCode());
                    return fieldNode;
                }).collect(Collectors.toList());

        return fieldNodeList;
    }

    @Override
    public void queryTable(List<TableNode> tableNodeList) {
        tableNodeList.forEach(this::queryTable);
    }

    @Override
    public void querySchema(List<SchemaNode> schemaNodeList) {
        schemaNodeList.forEach(this::querySchema);
    }

    @Override
    public void queryField(List<FieldNode> fieldNodeList) {
        fieldNodeList.forEach(this::queryField);
    }

}
