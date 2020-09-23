package cn.jupitermouse.lineage.graph.model.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import cn.jupitermouse.lineage.graph.model.DatabaseEntity;

/**
 * <p>
 * 节点 DataBaseRepository
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public interface DatabaseRepository extends Neo4jRepository<DatabaseEntity, String> {

}
