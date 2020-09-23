package cn.jupitermouse.lineage.graph.model.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import cn.jupitermouse.lineage.graph.model.OfRelationship;

/**
 * <p>
 * 关系 BeLongRelationship
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public interface OfRelationshipRepository extends Neo4jRepository<OfRelationship, String> {

}
