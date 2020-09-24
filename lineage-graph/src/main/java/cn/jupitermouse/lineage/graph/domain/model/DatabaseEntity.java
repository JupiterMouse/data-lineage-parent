package cn.jupitermouse.lineage.graph.domain.model;

import cn.jupitermouse.lineage.graph.infra.constats.NeoConstant;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * <p>
 * database = catalog
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
@NodeEntity(label = NeoConstant.Graph.NODE_DATABASE)
public class DatabaseEntity extends BaseNodeEntity {

    /**
     * 数据库名
     */
    private String database;

    @Override
    protected boolean valid() {
        return !StringUtils.isEmpty(database);
    }

    @Override
    protected String generateId() {
        return QualifiedName.ofCatalog(this.generateCluster(), this.database).toString();
    }

    @Override
    protected String generateId(String clusterName) {
        return QualifiedName.ofCatalog(clusterName, this.database).toString();
    }

    public void setGraphId() {
        this.setGraphId(this.generateId());
    }

}
