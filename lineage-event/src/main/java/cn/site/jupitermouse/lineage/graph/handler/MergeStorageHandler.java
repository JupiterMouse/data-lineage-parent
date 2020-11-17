package cn.site.jupitermouse.lineage.graph.handler;

import java.util.List;

import cn.site.jupitermouse.lineage.graph.domain.model.ProcessNode;
import cn.site.jupitermouse.lineage.graph.domain.repository.*;
import cn.site.jupitermouse.lineage.graph.domain.service.RelationshipService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 合并写入
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */

/**
 * <p>
 * 从处理上下文中，取出元数据进行存储。
 * merge的方式，存在则更新。不存在则新增。
 * </p>
 *
 * @author JupiterMouse 2020/10/09
 * @since 1.0
 */
@Component
public class MergeStorageHandler implements BaseStorageHandler {

    private final SchemaRepository schemaRepository;
    private final TableRepository tableRepository;
    private final FieldRepository fieldRepository;
    private final ProcessRepository processRepository;
    private final ClusterRepository clusterRepository;
    private final PlatformRepository platformRepository;
    private final RelationshipService relationshipService;

    public MergeStorageHandler(SchemaRepository schemaRepository, TableRepository tableRepository,
                               FieldRepository fieldRepository, ProcessRepository processRepository,
                               ClusterRepository clusterRepository, PlatformRepository platformRepository,
                               RelationshipService relationshipService) {
        this.schemaRepository = schemaRepository;
        this.tableRepository = tableRepository;
        this.fieldRepository = fieldRepository;
        this.processRepository = processRepository;
        this.clusterRepository = clusterRepository;
        this.platformRepository = platformRepository;
        this.relationshipService = relationshipService;
    }

    @Override
    public void handle(LineageContext lineageMapping) {
        // 创建或更新信息
        createOrUpdateNode(lineageMapping);
        // 创建或更新节点关系
        createOrUpdateRelationShip(lineageMapping);
    }

    private void createOrUpdateRelationShip(LineageContext lineageMapping) {
        // platform clusters
        platformRepository.mergeRelWithCluster();
        // cluster
        clusterRepository.mergeRelWithSchema();
        // schema tables
        schemaRepository.mergeRelWithTable();
        // table fields
        tableRepository.mergeRelWithField();
        // relationship
        this.createOrUpdateProcessRelationship(lineageMapping);
    }

    private void createOrUpdateProcessRelationship(LineageContext lineageMapping) {
        List<ProcessNode> processNodeList = lineageMapping.getProcessNodeList();
        if (CollectionUtils.isEmpty(processNodeList)) {
            return;
        }
        processNodeList.forEach(processNode -> {
            // TABLE|FIELD（many） -(PROCESS_IN)> Process
            relationshipService.mergeRelProcessInputs(processNode.getSourceNodePkList(), processNode.getPk());
            processRepository.mergeRelProcessOutput(processNode.getPk(), processNode.getTargetNodePk());
        });
    }

    private void createOrUpdateNode(LineageContext lineageMapping) {
        // platform
        platformRepository.saveAll(lineageMapping.getPlatformNodeList());
        // cluster
        clusterRepository.saveAll(lineageMapping.getClusterNodeList());
        // schema
        schemaRepository.saveAll(lineageMapping.getSchemaNodeList());
        // table
        tableRepository.saveAll(lineageMapping.getTableNodeList());
        // field
        fieldRepository.saveAll(lineageMapping.getFieldNodeList());
        // process
        processRepository.saveAll(lineageMapping.getProcessNodeList());
    }
}
