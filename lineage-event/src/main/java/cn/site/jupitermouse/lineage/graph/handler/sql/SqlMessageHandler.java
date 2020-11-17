package cn.site.jupitermouse.lineage.graph.handler.sql;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import cn.site.jupitermouse.lineage.graph.domain.model.*;
import cn.site.jupitermouse.lineage.common.exception.CommonException;
import cn.site.jupitermouse.lineage.common.util.JSON;
import cn.site.jupitermouse.lineage.graph.annotation.SourceHandler;
import cn.site.jupitermouse.lineage.graph.contants.HandlerConstant;
import cn.site.jupitermouse.lineage.graph.contants.NeoConstant;
import cn.site.jupitermouse.lineage.graph.handler.BaseMessageHandler;
import cn.site.jupitermouse.lineage.graph.handler.LineageContext;
import cn.site.jupitermouse.lineage.graph.metadata.MetaDataService;
import cn.site.jupitermouse.lineage.graph.util.LineageUtil;
import cn.site.jupitermouse.lineage.graph.util.SqlKafkaUtil;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlResponseContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.handler.DefaultHandlerChain;
import cn.site.jupitermouse.lineage.parser.druid.model.ColumnNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.util.TreeNodeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * <p>
 * SQL 解析
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@SourceHandler(NeoConstant.SourceType.SQL)
public class SqlMessageHandler implements BaseMessageHandler {

    private final DefaultHandlerChain defaultHandlerChain;
    private final MetaDataService metaDataService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SqlMessageHandler(DefaultHandlerChain defaultHandlerChain, MetaDataService metaDataService) {
        this.defaultHandlerChain = defaultHandlerChain;
        this.metaDataService = metaDataService;
    }

    @Override
    public LineageContext handle(ConsumerRecord<String, String> record) {
        SqlMessage sqlMessage = JSON.toObj(record.value(), SqlMessage.class);
        LineageContext context = new LineageContext();
        context.setSqlMessage(sqlMessage);
        // 处理
        List<SqlRequestContext> contextList = SqlKafkaUtil.convertSqlRequest(sqlMessage);
        // 解析SQL
        contextList.forEach(requestContext -> this.handleSql(requestContext, context));
        // 建立上层节点 Cluster、Platform、Schema
        createUpperLayerNode(context);
        // 从元数据更新节点信息，写入图
        updateNodeInfoMetadata(context);
        return context;
    }

    private void updateNodeInfoMetadata(LineageContext lineageMapping) {
        // schema
        metaDataService.querySchema(lineageMapping.getSchemaNodeList());
        // table
        metaDataService.queryTable(lineageMapping.getTableNodeList());
        // field
        metaDataService.queryField(lineageMapping.getFieldNodeList());
    }

    private void createUpperLayerNode(LineageContext context) {
        SqlMessage sqlMessage = context.getSqlMessage();

        // platform 一次SQL任务只会有一种PlatformNode
        PlatformNode platformNode = new PlatformNode(sqlMessage.getPlatformName());
        context.getPlatformNodeList().add(platformNode);

        // cluster 一次SQL任务只涉及一种数据源
        ClusterNode commonCluster = new ClusterNode(
                sqlMessage.getPlatformName(), sqlMessage.getClusterName()
        );
        commonCluster.setDatasourceCode(sqlMessage.getDatasourceCode());
        commonCluster.setTenantId(sqlMessage.getTenantId());
        context.getClusterNodeList().add(commonCluster);

        // schema 根据table 生成schema 节点信息
        List<SchemaNode> schemaNodeList = context.getTableNodeList()
                .stream()
                .filter(distinctByKey(BaseNodeEntity::getPlatformName))
                .filter(distinctByKey(BaseNodeEntity::getClusterName))
                .filter(distinctByKey(BaseNodeEntity::getSchemaName))
                .map(table -> {
                    SchemaNode distinctSchemaNode = new SchemaNode(
                            table.getPlatformName(), table.getClusterName(),
                            table.getCatalogName(), table.getOriginSchemaName()
                    );
                    distinctSchemaNode.setTenantId(table.getTenantId());
                    distinctSchemaNode.setDatasourceCode(table.getDatasourceCode());
                    return distinctSchemaNode;
                }).collect(Collectors.toList());
        context.getSchemaNodeList().addAll(schemaNodeList);
    }

    private void handleSql(SqlRequestContext request, LineageContext context) {
        SqlResponseContext response = new SqlResponseContext();
        defaultHandlerChain.handle(request, response);
        if (Objects.isNull(response.getLineageTableTree())) {
            return;
        }
        // 处理表关系
        handleTableNode(request, response, context);
        handleFieldNode(request, response, context);
    }

    private void handleFieldNode(SqlRequestContext request, SqlResponseContext response, LineageContext context) {
        // 获取字段关系树
        List<TreeNode<ColumnNode>> lineageColumnTreeList = response.getLineageColumnTreeList();
        if (CollectionUtils.isEmpty(lineageColumnTreeList)) {
            return;
        }
        lineageColumnTreeList.forEach(columnNodeTreeNode -> {
            ColumnNode target = columnNodeTreeNode.getValue();
            List<ColumnNode> leafColumnNodeList = TreeNodeUtil.searchTreeLeafNodeList(columnNodeTreeNode);
            // convert
            FieldNode targetFieldNode = buildFieldNodeNeo4j(request, target);
            List<FieldNode> sourceFieldNodeList = leafColumnNodeList.stream()
                    .map(fieldNode -> this.buildFieldNodeNeo4j(request, fieldNode))
                    .collect(Collectors.toList());
            // save
            context.getFieldNodeList().add(targetFieldNode);
            context.getFieldNodeList().addAll(sourceFieldNodeList);
            // 处理字段关系
            List<String> filedSourceNodePkList = sourceFieldNodeList
                    .stream()
                    .map(BaseNodeEntity::getPk)
                    .distinct()
                    .collect(Collectors.toList());
            // fieldProcessNode
            ProcessNode fieldProcessNode = new ProcessNode(
                    NeoConstant.ProcessType.FIELD_PROCESS,
                    filedSourceNodePkList,
                    targetFieldNode.getPk()
            );
            fieldProcessNode.setType(HandlerConstant.SOURCE_TYPE_SQL_PARSER);
            fieldProcessNode.setJob(JSON.toJson(context.getSqlMessage().getJob()));
            // 填充基本信息 base info
            LineageUtil.fillingProcessNode(targetFieldNode, fieldProcessNode);
            // extra info
            fieldProcessNode.getExtra().put("sql", request.getSql());
            // save
            context.getProcessNodeList().add(fieldProcessNode);
        });
    }

    private void handleTableNode(SqlRequestContext request, SqlResponseContext response, LineageContext context) {
        // 获取表关系树
        TreeNode<cn.site.jupitermouse.lineage.parser.druid.model.TableNode> lineageTableTree =
                response.getLineageTableTree();
        List<cn.site.jupitermouse.lineage.parser.druid.model.TableNode> leafTableNodeList = TreeNodeUtil.searchTreeLeafNodeList(lineageTableTree);
        cn.site.jupitermouse.lineage.parser.druid.model.TableNode rootTableNode = lineageTableTree.getRoot().getValue();
        // convert
        TableNode targetTableNode = buildTableNodeNeo4j(request, rootTableNode);
        List<TableNode> sourceTableNodeList = leafTableNodeList.stream()
                .map(tableNode -> this.buildTableNodeNeo4j(request, tableNode))
                .collect(Collectors.toList());
        context.getTableNodeList().add(targetTableNode);
        context.getTableNodeList().addAll(sourceTableNodeList);
        List<String> sourceNodePkList = sourceTableNodeList
                .stream()
                .map(BaseNodeEntity::getPk)
                .distinct()
                .collect(Collectors.toList());
        // processNode 处理表关系
        ProcessNode processNode = new ProcessNode(
                NeoConstant.ProcessType.TABLE_PROCESS,
                sourceNodePkList,
                targetTableNode.getPk()
        );
        processNode.setType(HandlerConstant.SOURCE_TYPE_SQL_PARSER);
        processNode.setJob(JSON.toJson(context.getSqlMessage().getJob()));
        // 补充信息
        // 填充 PlatformName、clusterName,tenantId,datasource
        LineageUtil.fillingProcessNode(targetTableNode, processNode);
        // 执行的SQL
        processNode.getExtra().put("sql", request.getSql());
        // save
        context.getProcessNodeList().add(processNode);
    }

    private FieldNode buildFieldNodeNeo4j(SqlRequestContext context, ColumnNode parserColumnNode) {
        cn.site.jupitermouse.lineage.parser.druid.model.TableNode tableNode = Optional
                .ofNullable(parserColumnNode.getOwner())
                .orElseThrow(() -> new CommonException("column.table.node.null"));

        String schemaName = Optional.ofNullable(tableNode.getSchemaName()).orElse(context.getSchemaName());
        FieldNode neo4jFieldNode = new FieldNode(
                context.getPlatformName(), context.getClusterName(),
                context.getCatalogName(), schemaName,
                tableNode.getName(), parserColumnNode.getName()
        );
        // tenantId
        Optional.ofNullable(context.getTenantId()).ifPresent(neo4jFieldNode::setTenantId);
        // datasourceCode
        Optional.ofNullable(context.getDatasourceCode()).ifPresent(neo4jFieldNode::setDatasourceCode);
        return neo4jFieldNode;
    }

    private TableNode buildTableNodeNeo4j(SqlRequestContext context,
                                          cn.site.jupitermouse.lineage.parser.druid.model.TableNode parserTableNode) {
        // origin schemaName
        String schemaName = Optional.ofNullable(parserTableNode.getSchemaName()).orElse(context.getSchemaName());
        return new TableNode(
                context.getPlatformName(), context.getClusterName(),
                context.getCatalogName(), schemaName,
                parserTableNode.getName()
        );
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return (t) -> seen.add(keyExtractor.apply(t));
    }
}
