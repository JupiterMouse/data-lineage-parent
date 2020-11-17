package cn.site.jupitermouse.lineage.graph.domain.repository;

import cn.site.jupitermouse.lineage.graph.domain.model.SchemaNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 表关系建立
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@Repository
public interface SchemaRepository extends Neo4jRepository<SchemaNode, String> {

    /**
     * TABLE_FROM_SCHEMA Merge: if not exists create,otherwise,update it
     */
    @Query("MATCH (s:Schema),(table:Table) " +
            "WHERE table.platformName = s.platformName  " +
            "AND table.clusterName = s.clusterName " +
            "AND table.schemaName = s.schemaName " +
            "MERGE (table)-[r:TABLE_FROM_SCHEMA]->(s)")
    void mergeRelWithTable();
}
