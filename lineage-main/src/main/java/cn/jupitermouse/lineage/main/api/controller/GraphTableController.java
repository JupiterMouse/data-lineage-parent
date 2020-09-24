package cn.jupitermouse.lineage.main.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cn.jupitermouse.lineage.graph.repository.TableGraphRepository;
import cn.jupitermouse.lineage.graph.service.LineageElementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 数据血缘表操作
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
@RestController
@RequestMapping("/table")
@Api(tags = "表", value = "表血缘")
@Slf4j
public class GraphTableController {

    private final LineageElementService lineageElementService;
    private final TableGraphRepository tableRepository;

    public GraphTableController(LineageElementService lineageElementService, TableGraphRepository tableRepository) {
        this.lineageElementService = lineageElementService;
        this.tableRepository = tableRepository;
    }

    @ApiOperation(value = "生成表血缘", notes = "生成表血缘")
    @PostMapping
    public ResponseEntity<Void> saveTableLineage(String cluster, String catalog, String schema, @RequestBody String sql,
            String dbType) {
        lineageElementService.ingestTableLineage(cluster, catalog, schema, dbType, sql);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ApiOperation(value = "清空图数据库", notes = "清空图数据库")
    public ResponseEntity<Void> cleanTableLineage() {
        tableRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
