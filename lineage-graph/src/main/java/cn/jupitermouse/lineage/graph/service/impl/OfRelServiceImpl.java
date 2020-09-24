package cn.jupitermouse.lineage.graph.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cn.jupitermouse.lineage.graph.domain.model.*;
import cn.jupitermouse.lineage.graph.domain.repository.DatabaseRepository;
import cn.jupitermouse.lineage.graph.domain.repository.SchemaRepository;
import cn.jupitermouse.lineage.graph.domain.repository.TableRepository;
import cn.jupitermouse.lineage.graph.infra.constats.NeoConstant;
import cn.jupitermouse.lineage.graph.service.OfRelService;
import cn.jupitermouse.lineage.graph.service.RelationshipService;

/**
 * <p>
 * OfRelService
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@Service
public class OfRelServiceImpl implements OfRelService {

    private final TableRepository tableRepository;
    private final SchemaRepository schemaRepository;
    private final DatabaseRepository databaseRepository;
    private final RelationshipService relationshipService;


    public OfRelServiceImpl(
            TableRepository tableRepository,
            SchemaRepository schemaRepository,
            DatabaseRepository databaseRepository,
            RelationshipService relationshipService) {
        this.tableRepository = tableRepository;
        this.schemaRepository = schemaRepository;
        this.databaseRepository = databaseRepository;
        this.relationshipService = relationshipService;
    }

    @Override
    public void createNodeOfRelRel(List<BaseNodeEntity> starts, BaseNodeEntity end) {
    }

    @Override
    public void createSchemaOfDatabaseRel(List<SchemaEntity> schemas, DatabaseEntity database) {
        final List<String> endList = schemas.stream().map(SchemaEntity::getGraphId).collect(Collectors.toList());
        relationshipService.nodeRelNodes(NeoConstant.Graph.NODE_DATABASE,
                database.getGraphId(),
                NeoConstant.Graph.NODE_SCHEMA,
                endList,
                NeoConstant.Graph.REL_OF,
                Collections.emptyMap()
        );
    }

    @Override
    public void createTableOfDatabaseRel(List<TableEntity> tables, DatabaseEntity database) {
        final List<String> endList = tables.stream().map(TableEntity::getGraphId).collect(Collectors.toList());
        relationshipService.nodeRelNodes(NeoConstant.Graph.NODE_DATABASE,
                database.getGraphId(),
                NeoConstant.Graph.NODE_TABLE,
                endList,
                NeoConstant.Graph.REL_OF,
                Collections.emptyMap()
        );
    }

    @Override
    public void createTableOrSchemaOfDatabaseRel(DatabaseEntity database) {
        databaseRepository.save(database);
    }

    @Override
    public void createTableOrSchemaOfDatabaseRelList(List<DatabaseEntity> databaseEntityList) {
        databaseRepository.saveAll(databaseEntityList);
    }

    @Override
    public void createTableOfSchemaRel(List<TableEntity> tables, SchemaEntity schema) {
        final List<String> endList = tables.stream().map(TableEntity::getGraphId).collect(Collectors.toList());
        relationshipService.nodeRelNodes(NeoConstant.Graph.NODE_SCHEMA,
                schema.getGraphId(),
                NeoConstant.Graph.NODE_TABLE,
                endList,
                NeoConstant.Graph.REL_OF,
                Collections.emptyMap()
        );
    }

    @Override
    public void createTableOfSchemaRel(SchemaEntity schema) {
        schemaRepository.save(schema);
    }

    @Override
    public void createTableOfSchemaRelList(List<SchemaEntity> schemaEntityList) {
        schemaRepository.saveAll(schemaEntityList);
    }

    @Override
    public void createFieldOfTableRel(List<FieldEntity> fields, TableEntity table) {
        final List<String> endList = fields.stream().map(FieldEntity::getGraphId).collect(Collectors.toList());
        relationshipService.nodeRelNodes(NeoConstant.Graph.NODE_TABLE,
                table.getGraphId(),
                NeoConstant.Graph.NODE_FIELD,
                endList,
                NeoConstant.Graph.REL_OF,
                Collections.emptyMap()
        );
    }

    @Override
    public void createFieldOfTableRel(TableEntity table) {
        tableRepository.save(table);
    }

    @Override
    public void createFieldOfTableRelList(List<TableEntity> tableList) {
        tableRepository.saveAll(tableList);
    }
}
