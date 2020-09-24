package cn.jupitermouse.lineage.graph.domain;

import org.springframework.data.neo4j.annotation.QueryResult;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/24
 * @since 1.0
 */
@Data
@QueryResult
public class NodeRelResult {

    private String startGraphId;

    private String endGraphId;

    private Long relId;
}
