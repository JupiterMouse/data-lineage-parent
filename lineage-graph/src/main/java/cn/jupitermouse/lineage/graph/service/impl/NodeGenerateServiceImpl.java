package cn.jupitermouse.lineage.graph.service.impl;

import org.springframework.stereotype.Service;

import cn.jupitermouse.lineage.graph.model.*;
import cn.jupitermouse.lineage.graph.model.repository.DatabaseRepository;
import cn.jupitermouse.lineage.graph.model.repository.FieldRepository;
import cn.jupitermouse.lineage.graph.model.repository.SchemaRepository;
import cn.jupitermouse.lineage.graph.model.repository.TableRepository;
import cn.jupitermouse.lineage.graph.service.NodeService;

/**
 * <p>
 * 节点关系生成
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@Service
public class NodeGenerateServiceImpl implements NodeService {

    private final DatabaseRepository databaseRepository;
    private final SchemaRepository schemaRepository;
    private final TableRepository tableRepository;
    private final FieldRepository fieldRepository;

    public NodeGenerateServiceImpl(DatabaseRepository databaseRepository,
            SchemaRepository schemaRepository,
            TableRepository tableRepository,
            FieldRepository fieldRepository) {
        this.databaseRepository = databaseRepository;
        this.schemaRepository = schemaRepository;
        this.tableRepository = tableRepository;
        this.fieldRepository = fieldRepository;
    }

    @Override
    public void createNode(BaseNodeEntity node) {
        if (node instanceof DatabaseEntity) {
            databaseRepository.save((DatabaseEntity) node);
        } else if (node instanceof SchemaEntity) {
            schemaRepository.save((SchemaEntity) node);
        } else if (node instanceof TableEntity) {
            tableRepository.save((TableEntity) node);
        } else if (node instanceof FieldEntity) {
            fieldRepository.save((FieldEntity) node);
        } else {
            throw new UnsupportedOperationException(node.getClass().getName());
        }
    }

}
