package cn.jupitermouse.lineage.metadata.ingest.api.controller.v1;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.jupitermouse.lineage.metadata.ingest.service.ModelIngestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 模型摄取服务
 * </p>
 *
 * @author JupiterMouse 2020/09/23
 * @since 1.0
 */
@RestController("modelIngestController.v1")
@RequestMapping("/metadata/ingest/v1/")
@Api(tags = "创建实体关系", value = "创建实体关系")
public class ModelIngestController {

    private final ModelIngestService modelIngestService;

    public ModelIngestController(ModelIngestService modelIngestService) {
        this.modelIngestService = modelIngestService;
    }

    @ApiOperation(value = "schema实体及实体下table建立")
    @GetMapping("/schema")
    public ResponseEntity<Void> schemas(String cluster, String catalog, String schemaPattern) {
        modelIngestService.schemaInfoIngest(cluster, catalog, schemaPattern);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "table实体及实体下field建立")
    @GetMapping("/table")
    public ResponseEntity<Void> tables(String cluster, String catalog, String schema, String tablePattern) {
        modelIngestService.tableInfoIngest(cluster, catalog, schema, tablePattern);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
