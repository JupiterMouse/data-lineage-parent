package cn.jupitermouse.lineage.graph.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.jupitermouse.lineage.common.message.SqlRequestDTO;
import cn.jupitermouse.lineage.graph.model.FieldEntity;
import cn.jupitermouse.lineage.graph.model.TableEntity;
import cn.jupitermouse.lineage.graph.model.repository.FieldRepository;
import cn.jupitermouse.lineage.graph.model.repository.TableRepository;
import cn.jupitermouse.lineage.graph.service.LineageElementService;
import cn.jupitermouse.lineage.parser.durid.analyse.LineageAnalyzer;
import cn.jupitermouse.lineage.parser.durid.dto.LineageColumnDTO;
import cn.jupitermouse.lineage.parser.model.ColumnNode;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 血缘关系写入图数据库
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Service
@Slf4j
public class LineageElementServiceImpl2 implements LineageElementService {

    private final TableRepository tableRepository;
    private final FieldRepository fieldRepository;

    public LineageElementServiceImpl2(TableRepository tableRepository,
            FieldRepository fieldRepository) {
        this.tableRepository = tableRepository;
        this.fieldRepository = fieldRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ingestTableLineage(String cluster, String catalog, String schema, String dbType, String sql) {
        LineageAnalyzer lineageAnalyzer = new LineageAnalyzer();
        TreeNode<TableNode> root = lineageAnalyzer
                .getLineageTree(SqlRequestDTO.builder().dbType(dbType).sql(sql).build());

        List<TableNode> sourceTableNodeList = new ArrayList<>();
        lineageAnalyzer.traverseTableLineageTree(root, sourceTableNodeList);
        // TODO 同步请求刷新 neo4j 的table  schema
        log.debug("request refresh neo4j entity ...");
        // 覆盖为空的 catalog，schema，schema
        final List<TableEntity> tableEntityList = sourceTableNodeList.stream()
                .map(tableNode -> this.transform2TableEntity(cluster, catalog, schema, tableNode))
                .collect(Collectors.toList());
        TableEntity rootTableEntity = this.transform2TableEntity(cluster, catalog, schema, root.getValue());
        rootTableEntity.setFromTableList(tableEntityList);
        // 构建图之间的关系，只建立关系，维持基本属性即可
        tableRepository.save(rootTableEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ingestColumnLineage(String cluster, String catalog, String schema, String dbType, String sql) {
        LineageAnalyzer lineageAnalyzer = new LineageAnalyzer();
        List<LineageColumnDTO> lineageColumnDTOList = lineageAnalyzer
                .getColumnLineage(SqlRequestDTO.builder().dbType(dbType).sql(sql).build());

        // TODO 同步请求刷新 neo4j 的table  schema
        log.debug("request refresh neo4j entity ...");

        List<FieldEntity> resultColumnEntityList = new ArrayList<>();
        for (LineageColumnDTO lineageColumnDTO : lineageColumnDTOList) {
            FieldEntity rootColumnEntity = this
                    .transform2ColumnEntity(cluster, catalog, schema, lineageColumnDTO.getColumn());
            List<FieldEntity> sourceColumnEntityList = lineageColumnDTO.getSourceColumnList().stream()
                    .map(fieldNode -> this.transform2ColumnEntity(cluster, catalog, schema, fieldNode))
                    .collect(Collectors.toList());
            rootColumnEntity.setFromFieldList(sourceColumnEntityList);
            resultColumnEntityList.add(rootColumnEntity);
        }
        fieldRepository.saveAll(resultColumnEntityList);
    }

    private TableEntity transform2TableEntity(String clusterName, String databaseName, String schema, TableNode table) {
        return new TableEntity(clusterName, databaseName,
                Optional.ofNullable(table.getSchemaName()).orElse(schema), table.getName());
    }

    private FieldEntity transform2ColumnEntity(String clusterName, String databaseName, String schema,
            ColumnNode columnNode) {
        return new FieldEntity(clusterName, databaseName,
                Optional.ofNullable(columnNode.getOwner().getSchemaName()).orElse(schema),
                Optional.ofNullable(columnNode.getOwner().getName()).orElse(columnNode.getTableName()),
                columnNode.getName());
    }
}
