package cn.site.jupitermouse.lineage.graph.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cn.site.jupitermouse.lineage.graph.domain.NodeQualifiedName;
import cn.site.jupitermouse.lineage.common.exception.ParserException;
import cn.site.jupitermouse.lineage.common.util.StringPool;
import cn.site.jupitermouse.lineage.graph.domain.model.*;
import cn.site.jupitermouse.lineage.graph.handler.sql.SqlMessage;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/09/24 13:51
 * @since 1.0.0
 */
public class LineageUtil {


    public static String genProcessNodePk(ProcessNode processNode) {
        // sourceNodePkList：x,y
        // targetNodePk: z
        // md5(targetNodePk + sourceNodePkList排序后使用下划线'_'连接)
        List<String> sourceNodePkList = processNode.getSourceNodePkList();
        if (CollectionUtils.isEmpty(sourceNodePkList)) {
            return DigestUtils.md5DigestAsHex(processNode.getTargetNodePk().getBytes());
        }
        sourceNodePkList.sort(Comparator.naturalOrder());
        String key = processNode.getTargetNodePk() + "_" + String.join("_", sourceNodePkList);
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }


    /**
     * 填充schema、cluster、name、id信息字段
     *
     * @param schemaNode schemaNode
     */
    public static void fillingSchemaNode(SchemaNode schemaNode) {
        // cluster
        LineageUtil.fillingSchema(schemaNode);
        // displayName
        schemaNode.setName(schemaNode.getSchemaName());
        // id
        schemaNode.setPk(NodeQualifiedName.ofSchema(
                schemaNode.getPlatformName(),
                schemaNode.getClusterName(),
                schemaNode.getSchemaName()).toString());
    }

    /**
     * 填充schema、cluster、name、id信息字段
     *
     * @param tableNode tableNode
     */
    public static void fillingTableNode(TableNode tableNode) {
        // cluster
        LineageUtil.fillingSchema(tableNode);
        // name
        tableNode.setName(tableNode.getOriginSchemaName() + StringPool.DOT + tableNode.getTableName());
        // id
        tableNode.setPk(NodeQualifiedName.ofTable(
                tableNode.getPlatformName(),
                tableNode.getClusterName(),
                tableNode.getSchemaName(),
                tableNode.getTableName()).toString()
        );
    }

    /**
     * 填充schema、cluster、name、id信息字段
     *
     * @param fieldNode fieldNode
     */
    public static void fillingFieldNode(FieldNode fieldNode) {
        // cluster
        LineageUtil.fillingSchema(fieldNode);
        // name
        fieldNode.setName(fieldNode.getFieldName());
        // id
        fieldNode.setPk(NodeQualifiedName.ofField(
                Optional.ofNullable(fieldNode.getPlatformName()).orElse("DEFAULT"),
                fieldNode.getClusterName(),
                fieldNode.getSchemaName(),
                fieldNode.getTableName(),
                fieldNode.getFieldName()).toString()
        );
    }

    /**
     * 填充schema信息字段
     *
     * @param entity entity
     */
    public static void fillingSchema(BaseNodeEntity entity) {
        LineageUtil.fillingCluster(entity);
        // schema
        if (!StringUtils.isEmpty(entity.getCatalogName())) {
            entity.setSchemaName(entity.getCatalogName() + StringPool.DOT + entity.getOriginSchemaName());
        } else {
            entity.setSchemaName(entity.getOriginSchemaName());
        }
    }

    /**
     * 填充cluster信息字段
     *
     * @param entity entity
     */
    public static void fillingCluster(BaseNodeEntity entity) {
        // cluster
        if (Objects.nonNull(entity.getTenantId()) && !StringUtils.isEmpty(entity.getDatasourceCode())) {
            entity.setClusterName(entity.getTenantId() + StringPool.UNDERSCORE + entity.getDatasourceCode());
        }
        if (StringUtils.isEmpty(entity.getClusterName())) {
            throw new ParserException("schema clusterName is null");
        }
    }

    public static String fillingCluster(SqlMessage sqlMessage) {
        // cluster
        if (Objects.nonNull(sqlMessage.getTenantId()) && !StringUtils.isEmpty(sqlMessage.getDatasourceCode())) {
            return sqlMessage.getTenantId() + StringPool.UNDERSCORE + sqlMessage.getDatasourceCode();
        }
        if (StringUtils.isEmpty(sqlMessage.getClusterName())) {
            throw new ParserException("schema clusterName is null");
        }
        return sqlMessage.getClusterName();
    }


    /**
     * 填充cluster信息字段
     *
     * @param source BaseNodeEntity FieldNode|TableNode
     * @param target ProcessNode
     */
    public static void fillingProcessNode(BaseNodeEntity source, ProcessNode target) {
        target.setPlatformName(source.getPlatformName());
        target.setClusterName(source.getClusterName());
        target.setTenantId(source.getTenantId());
        target.setDatasourceCode(source.getDatasourceCode());
    }
}
