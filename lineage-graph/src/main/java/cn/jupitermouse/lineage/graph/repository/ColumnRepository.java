package cn.jupitermouse.lineage.graph.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import cn.jupitermouse.lineage.graph.entity.ColumnEntity;


/**
 * <p>
 * ColumnRepository
 * </p>
 *
 * @author JupiterMouse 2020/9/9
 * @since 1.0
 */
public interface ColumnRepository extends Neo4jRepository<ColumnEntity, String> {

}
