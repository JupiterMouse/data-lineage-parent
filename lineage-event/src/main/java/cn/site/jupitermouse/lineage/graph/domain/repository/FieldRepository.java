package cn.site.jupitermouse.lineage.graph.domain.repository;

import cn.site.jupitermouse.lineage.graph.domain.model.FieldNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 字段关系
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@Repository
public interface FieldRepository extends Neo4jRepository<FieldNode, String> {


    @Query("MATCH (f:Field),(p:Process) " +
            "WHERE f.pk = $sourceFieldNodePk " +
            "and p.pk= $processNodePk " +
            "MERGE (f)-[r:PROCESS_INPUT]->(p)")
    void mergeRelWithProcess(@Param("sourceFieldNodePk") String sourceFieldNodePk,
                             @Param("processNodePk") String processNodePk);

}
