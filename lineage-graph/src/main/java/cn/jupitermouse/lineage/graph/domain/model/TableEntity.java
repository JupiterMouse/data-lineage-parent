package cn.jupitermouse.lineage.graph.domain.model;

import java.util.List;

import cn.jupitermouse.lineage.graph.infra.constats.NeoConstant;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;

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

    public TableEntity(String clusterName, String database, String schema, String table) {
        setClusterName(clusterName);
        this.database = database;
        this.schema = schema;
        this.table = table;
        setGraphId(this.generateId(clusterName));
    }

    public TableEntity(Long tenantId, String datasourceCode, String database, String schema, String table) {
        this.setTenantId(tenantId);
        this.database = database;
        this.schema = schema;
        this.table = table;
        setDatasourceCode(datasourceCode);
        setGraphId(this.generateId());
    }

    @Transient
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

    public void setTable(String table) {
        this.table = table;
        this.setName(this.table);
    }
}
