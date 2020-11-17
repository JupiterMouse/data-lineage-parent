package cn.site.jupitermouse.lineage.graph.domain.model;

import java.util.Optional;

import cn.site.jupitermouse.lineage.common.util.StringPool;
import cn.site.jupitermouse.lineage.graph.contants.NeoConstant;
import cn.site.jupitermouse.lineage.graph.domain.NodeQualifiedName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * <p>
 * Schema Node, if database contain catalog,use database = catalog.schema
 * 存储字段：schemaNode
 * </p>
 *
 * @author JupiterMouse 2020/09/28
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@Data
@NodeEntity(NeoConstant.Type.NODE_SCHEMA)
public class SchemaNode extends BaseNodeEntity {

    //===============================================================================
    //  Other information fields
    //===============================================================================

    /**
     * 创建database的sql
     */
    private String sql;

    public SchemaNode(String platformName, String clusterName, String catalogName, String originSchemaName) {
        Optional.ofNullable(platformName).ifPresent(this::setPlatformName);
        Optional.ofNullable(clusterName).ifPresent(this::setClusterName);
        Optional.ofNullable(catalogName).ifPresent(this::setCatalogName);
        Optional.ofNullable(originSchemaName).ifPresent(this::setOriginSchemaName);
        if (StringUtils.isEmpty(catalogName)) {
            this.setSchemaName(originSchemaName);
        } else {
            this.setSchemaName(this.getCatalogName() + StringPool.DOT + this.getOriginSchemaName());
        }
        String pk = NodeQualifiedName.ofSchema(this.getPlatformName(), this.getClusterName(), this.getSchemaName()).toString();
        this.setPk(pk);
        // displayName
        this.setName(this.getSchemaName());
    }

    public SchemaNode(String platformName, String clusterName, String schemaName) {
        this(platformName, clusterName, null, schemaName);
    }

    public SchemaNode(String schemaName) {
        this(null, null, schemaName);
    }

}
