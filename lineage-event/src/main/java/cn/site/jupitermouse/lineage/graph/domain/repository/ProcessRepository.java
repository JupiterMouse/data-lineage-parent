package cn.site.jupitermouse.lineage.graph.domain.repository;


import cn.site.jupitermouse.lineage.graph.domain.model.ProcessNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 关系节点
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@Repository
public interface ProcessRepository extends Neo4jRepository<ProcessNode, String> {
    /**
     * PROCESS_OUTPUT Merge: if not exists create,otherwise,update it
     * process -> field|table
     *
     * @param processPk processPk
     * @param tablePk   tablePk
     */
    @Query("MATCH (t),(p:Process) " +
            "WHERE t.platformName = p.platformName  " +
            "AND t.clusterName = p.clusterName  " +
            "AND t.pk = $tablePk  " +
            "AND p.pk = $processPk  " +
            "MERGE (p)-[r:PROCESS_OUTPUT]->(t)")
    void mergeRelProcessOutput(@Param("processPk") String processPk, @Param("tablePk") String tablePk);

}
