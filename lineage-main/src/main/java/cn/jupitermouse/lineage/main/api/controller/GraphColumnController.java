package cn.jupitermouse.lineage.main.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cn.jupitermouse.lineage.graph.repository.ColumnRepository;
import cn.jupitermouse.lineage.graph.service.LineageElementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 数据血缘字段
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
@RestController
@RequestMapping("/column")
@Api(tags = "v1", value = "字段血缘")
public class GraphColumnController {

    private final LineageElementService lineageElementService;
    private final ColumnRepository columnRepository;

    public GraphColumnController(LineageElementService lineageElementService, ColumnRepository columnRepository) {
        this.lineageElementService = lineageElementService;
        this.columnRepository = columnRepository;
    }

    @ApiOperation(value = "生成字段血缘", notes = "生成字段血缘")
    @PostMapping
    public ResponseEntity<Void> saveColumnLineage(@RequestBody String sql, String dbType) {
        lineageElementService.ingestTableLineage(sql, dbType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ApiOperation(value = "清空图数据库", notes = "清空图数据库")
    public ResponseEntity<Void> cleanColumnLineage() {
        columnRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
