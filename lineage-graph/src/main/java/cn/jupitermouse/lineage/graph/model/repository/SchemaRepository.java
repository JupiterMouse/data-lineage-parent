package cn.jupitermouse.lineage.graph.model.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import cn.jupitermouse.lineage.graph.model.SchemaEntity;

/**
 * <p>
 * schema
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public interface SchemaRepository extends Neo4jRepository<SchemaEntity, String> {

}
