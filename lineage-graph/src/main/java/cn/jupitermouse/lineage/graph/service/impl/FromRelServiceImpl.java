package cn.jupitermouse.lineage.graph.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cn.jupitermouse.lineage.graph.domain.model.BaseNodeEntity;
import cn.jupitermouse.lineage.graph.domain.model.FieldEntity;
import cn.jupitermouse.lineage.graph.domain.model.TableEntity;
import cn.jupitermouse.lineage.graph.infra.constats.NeoConstant;
import cn.jupitermouse.lineage.graph.service.FromRelService;
import cn.jupitermouse.lineage.graph.service.RelationshipService;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.SessionFactory;

/**
 * <p>
 * FromRelService
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@Service
public class FromRelServiceImpl implements FromRelService {

    private final SessionFactory sessionFactory;
    private final RelationshipService relationshipService;

    public FromRelServiceImpl(SessionFactory sessionFactory,
            RelationshipService relationshipService) {
        this.sessionFactory = sessionFactory;
        this.relationshipService = relationshipService;
    }

    /**
     * START n=node:node_auto_index(name='Neo'), t=node:node_auto_index(name='The Architect') CREATE UNIQUE
     * n-[r:SPEAKS_WITH]-t RETURN n AS Neo,r
     *
     * @param start 从节点列表  如List<Fields> -> Table
     * @param ends  目标节点 为大一级的节点
     */
    @Override
    public void createNodeFromRel(BaseNodeEntity start, List<BaseNodeEntity> ends) {
    }

    @Override
    public Result createNodeFromRel(String sql) {
        return sessionFactory.openSession().query(sql, Collections.emptyMap());
    }

    @Override
    public void createTableFromTables(TableEntity table, List<TableEntity> sourceTables) {
        final List<String> endList = sourceTables.stream().map(TableEntity::getGraphId).collect(Collectors.toList());
        relationshipService.nodeRelNodes(NeoConstant.Graph.NODE_TABLE,
                table.getGraphId(),
                NeoConstant.Graph.NODE_TABLE,
                endList,
                NeoConstant.Graph.REL_FROM,
                Collections.emptyMap()
        );
    }

    @Override
    public void createFieldFromFields(FieldEntity field, List<FieldEntity> sourceFields) {
        final List<String> endList = sourceFields.stream().map(FieldEntity::getGraphId).collect(Collectors.toList());
        relationshipService.nodeRelNodes(NeoConstant.Graph.NODE_FIELD,
                field.getGraphId(),
                NeoConstant.Graph.NODE_FIELD,
                endList,
                NeoConstant.Graph.REL_FROM,
                Collections.emptyMap()
        );
    }
}
