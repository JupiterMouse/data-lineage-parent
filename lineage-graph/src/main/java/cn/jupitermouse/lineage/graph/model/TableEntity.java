package cn.jupitermouse.lineage.graph.model;

import java.util.List;

import cn.jupitermouse.lineage.graph.constats.NeoConstant;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * <p>
 * Table
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NodeEntity(label = NeoConstant.Graph.NODE_TABLE)
public class TableEntity extends BaseNodeEntity {

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
     * 是否为视图 1 视图，0 为表
     */
    private Integer viewFlag;

    @Relationship(type = NeoConstant.Graph.REL_OF, direction = Relationship.INCOMING)
    private List<FieldEntity> fieldOfTableList;

    @Override
    protected boolean valid() {
        if (StringUtils.isEmpty(database) && StringUtils.isEmpty(schema)) {
            return false;
        }
        return StringUtils.isNotEmpty(table);
    }

    @Override
    protected String generateId() {
        this.valid();
        if (getTenantId() == null) {
            return QualifiedName.ofTable(getClusterName(), this.database, this.schema, this.table).toString();
        }
        return QualifiedName.ofTable(this.generateCluster(), this.database, this.schema, this.table).toString();
    }

    @Override
    protected String generateId(String clusterName) {
        this.valid();
        return QualifiedName.ofTable(clusterName, this.database, this.schema, this.table).toString();
    }


    public void setGraphId() {
        this.setGraphId(this.generateId());
    }
}
