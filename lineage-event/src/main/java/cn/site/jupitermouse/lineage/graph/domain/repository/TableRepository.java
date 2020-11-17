package cn.site.jupitermouse.lineage.graph.domain.repository;

import cn.site.jupitermouse.lineage.graph.domain.model.TableNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
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
public interface TableRepository extends Neo4jRepository<TableNode, String> {

    /**
     * FIELD_FROM_TABLE Merge: if not exists create,otherwise,update it
     */
    @Query("MATCH (field:Field),(table:Table) " +
            "WHERE field.platformName = table.platformName " +
            "AND field.clusterName = table.clusterName " +
            "AND field.schemaName = table.schemaName " +
            "AND field.tableName = table.tableName " +
            "MERGE (field)-[:FIELD_FROM_TABLE]->(table)")
    void mergeRelWithField();

}
