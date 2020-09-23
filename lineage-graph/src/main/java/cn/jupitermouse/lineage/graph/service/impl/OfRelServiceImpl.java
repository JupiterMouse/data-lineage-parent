package cn.jupitermouse.lineage.graph.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cn.jupitermouse.lineage.graph.model.*;
import cn.jupitermouse.lineage.graph.model.repository.DatabaseRepository;
import cn.jupitermouse.lineage.graph.model.repository.OfRelationshipRepository;
import cn.jupitermouse.lineage.graph.model.repository.SchemaRepository;
import cn.jupitermouse.lineage.graph.model.repository.TableRepository;
import cn.jupitermouse.lineage.graph.service.OfRelService;

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

    private final OfRelationshipRepository beLongRepository;
    private final TableRepository tableRepository;
    private final SchemaRepository schemaRepository;
    private final DatabaseRepository databaseRepository;

    public OfRelServiceImpl(OfRelationshipRepository beLongRepository,
            TableRepository tableRepository,
            SchemaRepository schemaRepository,
            DatabaseRepository databaseRepository) {
        this.beLongRepository = beLongRepository;
        this.tableRepository = tableRepository;
        this.schemaRepository = schemaRepository;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public void createNodeOfRelRel(List<BaseNodeEntity> starts, BaseNodeEntity end) {

    }

    @Override
    public void createSchemaOfDatabaseRel(List<SchemaEntity> schemas, DatabaseEntity database) {
        List<OfRelationship> list = schemas.stream()
                .map(schema -> OfRelationship.builder().start(schema).end(database).build())
                .collect(Collectors.toList());
        beLongRepository.saveAll(list);
    }

    @Override
    public void createTableOfDatabaseRel(List<TableEntity> tables, DatabaseEntity database) {
        List<OfRelationship> list = tables.stream()
                .map(table -> OfRelationship.builder().start(table).end(database).build())
                .collect(Collectors.toList());
        beLongRepository.saveAll(list);
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
        List<OfRelationship> list = tables.stream()
                .map(table -> OfRelationship.builder().start(table).end(schema).build())
                .collect(Collectors.toList());
        beLongRepository.saveAll(list);
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
        List<OfRelationship> list = fields.stream()
                .map(field -> OfRelationship.builder().start(field).end(table).build())
                .collect(Collectors.toList());
        beLongRepository.saveAll(list);
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
