package cn.jupitermouse.lineage.graph.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cn.jupitermouse.lineage.graph.model.BaseNodeEntity;
import cn.jupitermouse.lineage.graph.model.FieldEntity;
import cn.jupitermouse.lineage.graph.model.FromRelationship;
import cn.jupitermouse.lineage.graph.model.TableEntity;
import cn.jupitermouse.lineage.graph.model.repository.FromRelationshipRepository;
import cn.jupitermouse.lineage.graph.service.FromRelService;

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

    private final FromRelationshipRepository fromRepository;

    public FromRelServiceImpl(FromRelationshipRepository fromRepository) {
        this.fromRepository = fromRepository;
    }

    @Override
    public void createNodeFromRel(BaseNodeEntity start, List<BaseNodeEntity> ends) {

    }

    @Override
    public void createTableFromTables(TableEntity table, List<TableEntity> sourceTables) {
        List<FromRelationship> list = sourceTables.stream()
                .map(sourceTable -> FromRelationship.builder().start(table).end(sourceTable).build())
                .collect(Collectors.toList());
        fromRepository.saveAll(list);
    }

    @Override
    public void createFieldFromFields(FieldEntity field, List<FieldEntity> sourceFields) {
        List<FromRelationship> list = sourceFields.stream()
                .map(sourceField -> FromRelationship.builder().start(field).end(sourceField).build())
                .collect(Collectors.toList());
        fromRepository.saveAll(list);
    }
}
