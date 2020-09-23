package cn.jupitermouse.lineage.main.app;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.jupitermouse.lineage.graph.model.FieldEntity;
import cn.jupitermouse.lineage.graph.model.TableEntity;
import cn.jupitermouse.lineage.graph.model.repository.FieldRepository;
import cn.jupitermouse.lineage.graph.service.OfRelService;
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
}
