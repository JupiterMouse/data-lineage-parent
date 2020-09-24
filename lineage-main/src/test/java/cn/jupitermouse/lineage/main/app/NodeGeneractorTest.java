package cn.jupitermouse.lineage.main.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.jupitermouse.lineage.graph.domain.model.FieldEntity;
import cn.jupitermouse.lineage.graph.domain.model.TableEntity;
import cn.jupitermouse.lineage.graph.domain.repository.FieldRepository;
import cn.jupitermouse.lineage.graph.domain.repository.FromRelationshipRepository;
import cn.jupitermouse.lineage.graph.infra.constats.NeoConstant;
import cn.jupitermouse.lineage.graph.service.FromRelService;
import cn.jupitermouse.lineage.graph.service.OfRelService;
import cn.jupitermouse.lineage.graph.service.RelationshipService;
import cn.jupitermouse.lineage.main.DataLineageApp;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@SpringBootTest(classes = DataLineageApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class NodeGeneractorTest {

    @Autowired
    private OfRelService ofRelService;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FromRelationshipRepository fromRelationshipRepository;

    @Autowired
    private FromRelService fromRelService;

    @Autowired
    private RelationshipService relationshipService;

    @Test
    public void testNode() {
        FieldEntity fieldEntity = FieldEntity.builder()
                .database("hdsp")
                .schema("hzero_iam")
                .table("iam_user")
                .field("id")
                .partitionFlag(0)
                .build();
        fieldEntity.setName("id");
        fieldEntity.setGraphId();

        Map<String, String> map = new HashMap<>();
        map.put("remark", null);
        map.put("remark2", "注释222222");
        fieldEntity.setAttrs(map);
        TableEntity tableEntity = TableEntity.builder()
                .database("hdsp")
                .schema("hzero_iam")
                .table("iam_user")
                .viewFlag(0)
                .build();
        tableEntity.setName("iam_user");
        tableEntity.setGraphId();
        ofRelService.createFieldOfTableRel(Collections.singletonList(fieldEntity), tableEntity);
        System.out.println();
    }


    @Test
    public void testForm() {
//        final FieldId start = new FieldId();
//        start.setGraphId("hand-hr-gp/hand/ods/ods_sap_et_orglist/vename");
//        final FieldId end = new FieldId();
//        end.setGraphId("hand-hr-gp/hand/ods/ods_sap_et_orglist/memail");
//
//        final FromRelationship fromRelationship = FromRelationship.builder()
//                .start(start)
//                .end(end)
//                .build();
//        fromRelationshipRepository.save(fromRelationship);
//        System.out.println();
    }


    @Test
    public void query() {
        fromRelService.createNodeFromRel(null, null);
        System.out.println();
    }

    @Test
    public void rel() {
        Map<String, String> map = new HashMap<>();
        map.put("a1", "a1");
        relationshipService.nodeRelNodes(NeoConstant.Graph.NODE_FIELD, "hand-hr-gp/hand/ods/ods_sap_et_orglist/vename",
                NeoConstant.Graph.NODE_FIELD, Arrays.asList("hand-hr-gp/hand/ods/ods_sap_et_orglist/memail", "hand-hr-gp/hand/ods/ods_sap_et_orglist/memail2"),
                NeoConstant.Graph.REL_FROM, map);
        System.out.println();
    }
}
