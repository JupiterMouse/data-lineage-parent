package cn.jupitermouse.lineage.graph.model;

import cn.jupitermouse.lineage.graph.constats.NeoConstant;
import lombok.*;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * <p>
 * 字段
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity(label = NeoConstant.Graph.NODE_FIELD)
@Builder
public class FieldEntity extends BaseNodeEntity {

    /**
     * 数据库名
     */
    private String database;
    /**
     * schema 名
     */
    private String schema;
    /**
     * table名
     */
    private String table;
    /**
     * 字段名
     */
    private String field;

    /**
     * 是否为分区字段
     */
    private Integer partitionFlag;

    @Override
    protected String generateId() {
        this.valid();
        if (getTenantId() == null) {
            return QualifiedName.ofField(getClusterName(), this.database, this.schema, this.table, this.field)
                    .toString();
        }
        return QualifiedName.ofField(this.generateCluster(), this.database, this.schema, this.table, this.field)
                .toString();
    }

    @Override
    protected String generateId(String clusterName) {
        this.valid();
        return QualifiedName.ofField(clusterName, this.database, this.schema, this.table, this.field)
                .toString();
    }

    public void setGraphId() {
        this.setGraphId(this.generateId());
    }


}
