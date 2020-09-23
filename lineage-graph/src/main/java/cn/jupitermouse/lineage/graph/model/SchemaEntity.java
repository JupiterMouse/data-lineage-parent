package cn.jupitermouse.lineage.graph.model;

import java.util.List;

import cn.jupitermouse.lineage.graph.constats.NeoConstant;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * <p>
 * schema
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NodeEntity(label = NeoConstant.Graph.NODE_SCHEMA)
public class SchemaEntity extends BaseNodeEntity {

    /**
     * database名
     */
    private String database;
    /**
     * schema名
     */
    private String schema;

    @Relationship(type = NeoConstant.Graph.REL_OF, direction = Relationship.INCOMING)
    private List<TableEntity> tableOfSchemaList;

    @Override
    protected boolean valid() {
        return StringUtils.isNotEmpty(database) && StringUtils.isNotEmpty(schema);
    }

    @Override
    protected String generateId() {
        this.valid();
        if (getTenantId() == null) {
            return QualifiedName.ofSchema(this.getClusterName(), this.database, this.schema).toString();
        }
        return QualifiedName.ofSchema(this.generateCluster(), this.database, this.schema).toString();
    }

    @Override
    protected String generateId(String clusterName) {
        this.valid();
        return QualifiedName.ofSchema(clusterName, this.database, this.schema).toString();
    }

    public void setGraphId() {
        this.setGraphId(this.generateId());
    }
}
