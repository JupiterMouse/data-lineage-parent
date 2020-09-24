package cn.jupitermouse.lineage.graph.model.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import cn.jupitermouse.lineage.graph.model.TableEntity;

/**
 * <p>
 * TableEntity 数据节点
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public interface TableRepository extends Neo4jRepository<TableEntity, String> {

}
