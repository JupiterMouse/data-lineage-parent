package cn.site.jupitermouse.lineage.graph.domain.model;

import java.util.Optional;

import cn.site.jupitermouse.lineage.common.util.StringPool;
import cn.site.jupitermouse.lineage.graph.contants.NeoConstant;
import cn.site.jupitermouse.lineage.graph.domain.NodeQualifiedName;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * <p>
 * Node Field
 * </p>
 *
 * @author isaac 2020/09/28
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@NodeEntity(NeoConstant.Type.NODE_FIELD)
public class FieldNode extends BaseNodeEntity {

    private String tableName;
    private String fieldName;
    private String fieldType;

    //===============================================================================
    //  Other information fields
    //===============================================================================

    private String remark;

    public FieldNode(String platformName, String clusterName,
                     String catalogName, String originSchemaName,
                     String tableName, String fieldName) {
        Optional.ofNullable(platformName).ifPresent(this::setPlatformName);
        Optional.ofNullable(clusterName).ifPresent(this::setClusterName);
        Optional.ofNullable(catalogName).ifPresent(this::setCatalogName);
        Optional.ofNullable(originSchemaName).ifPresent(this::setOriginSchemaName);
        if (StringUtils.isNotEmpty(this.getCatalogName())) {
            this.setSchemaName(this.getCatalogName() + StringPool.DOT + this.getOriginSchemaName());
        } else {
            this.setSchemaName(originSchemaName);
        }
        this.setTableName(tableName);
        this.setFieldName(fieldName);
        String pk = NodeQualifiedName.ofField(this.getPlatformName(), this.getClusterName(), this.getSchemaName(),
                this.getTableName(), this.getFieldName()).toString();
        this.setPk(pk);
    }
}
