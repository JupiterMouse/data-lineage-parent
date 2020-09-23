package cn.jupitermouse.lineage.graph.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import cn.jupitermouse.lineage.graph.entity.TableEntity;


/**
 * <p>
 * TableRepository
 * </p>
 *
 * @author JupiterMouse 2020/9/9
 * @since 1.0
 */
public interface TableGraphRepository extends Neo4jRepository<TableEntity, String> {

}
