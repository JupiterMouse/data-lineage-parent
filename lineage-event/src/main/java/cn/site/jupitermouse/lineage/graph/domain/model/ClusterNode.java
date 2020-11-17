package cn.site.jupitermouse.lineage.graph.domain.model;

import java.util.Optional;

import cn.site.jupitermouse.lineage.graph.contants.NeoConstant;
import cn.site.jupitermouse.lineage.graph.domain.NodeQualifiedName;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * <p>
 * Cluster Node
 * 基本字段使用存储字段
 * platformName
 * clusterName
 * tenantId
 * datasourceCode
 * </p>
 *
 * @author JupiterMouse 2020/09/28
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@NodeEntity(NeoConstant.Type.NODE_CLUSTER)
@NoArgsConstructor
public class ClusterNode extends BaseNodeEntity {

    public ClusterNode(String platformName, String clusterName) {
        this.setPlatformName(platformName);
        Optional.ofNullable(clusterName).ifPresent(this::setClusterName);
        // platform/cluster
        String pk = NodeQualifiedName.ofCluster(this.getPlatformName(), this.getClusterName()).toString();
        this.setPk(pk);
        // todo 考虑从clusterName 取出tenantId和clusterName
    }
}
