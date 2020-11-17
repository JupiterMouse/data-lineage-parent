package cn.site.jupitermouse.lineage.graph.domain.repository;

import cn.site.jupitermouse.lineage.graph.domain.model.PlatformNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 平台关系
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@Repository
public interface PlatformRepository extends Neo4jRepository<PlatformNode, String> {

    /**
     * CLUSTER_FROM_PLATFORM Merge: if not exists create,otherwise,update it
     */
    @Query("MATCH (p:Platform),(c:Cluster) " +
            "WHERE p.platformName = c.platformName  " +
            "MERGE (c)-[r:CLUSTER_FROM_PLATFORM]->(p)")
    void mergeRelWithCluster();
}
