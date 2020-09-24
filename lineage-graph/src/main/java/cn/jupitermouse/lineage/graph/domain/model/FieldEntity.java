package cn.jupitermouse.lineage.graph.domain.model;

import java.util.List;

import cn.jupitermouse.lineage.graph.infra.constats.NeoConstant;
import lombok.*;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;

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

    @Transient
    private List<FieldEntity> fromFieldList;

    public FieldEntity(String clusterName, String database, String schema, String table, String field) {
        setClusterName(clusterName);
        this.database = database;
        this.schema = schema;
        this.table = table;
        this.field = field;
        setGraphId(this.generateId(clusterName));
    }

    public FieldEntity(Long tenantId, String datasourceCode, String database, String schema, String table,
            String field) {
        this.setTenantId(tenantId);
        this.database = database;
        this.schema = schema;
        this.table = table;
        this.field = field;
        setDatasourceCode(datasourceCode);
        setGraphId(this.generateId());
    }

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


    public void setField(String field) {
        this.field = field;
        this.setName(this.field);
    }
}
