package cn.site.jupitermouse.lineage.graph.domain.repository;

import cn.site.jupitermouse.lineage.graph.domain.model.ClusterNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * 集群关系
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@Repository
public interface ClusterRepository extends Neo4jRepository<ClusterNode, String> {

    /**
     * SCHEMA_FROM_CLUSTER Merge: if not exists create,otherwise,update it
     */
    @Query("MATCH (c:Cluster),(s:Schema) " +
            "WHERE c.platformName = s.platformName  " +
            "AND c.clusterName = s.clusterName  " +
            "MERGE (s)-[r:SCHEMA_FROM_CLUSTER]->(c)")
    void mergeRelWithSchema();

}
