package cn.jupitermouse.lineage.graph.domain.model;

import java.util.List;

import cn.jupitermouse.lineage.graph.infra.constats.NeoConstant;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * <p>
 * 节点间的来源 A -> B   tableA -> tableB  fieldA -> tableA
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@RelationshipEntity(type = NeoConstant.Graph.REL_FROM)
@Deprecated
public class FromRelationship extends BaseEntity {

    /**
     * 关系的开始端
     */
    @StartNode
    private BaseNodeEntity start;

//    /**
//     * 关系的结束端 多
//     */
//    @EndNode
//    private List<BaseId> ends;

    /**
     * 关系的结束端
     */
    @EndNode
    private BaseNodeEntity end;
}
