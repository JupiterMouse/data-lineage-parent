package cn.jupitermouse.lineage.graph.domain.model;

import cn.jupitermouse.lineage.graph.infra.constats.NeoConstant;
import lombok.*;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * <p>
 *  Cluster
 * </p>
 *
 * @author JupiterMouse 2020/09/24
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NodeEntity(label = NeoConstant.Graph.NODE_CLUSTER)
public class ClusterEntity {
}
