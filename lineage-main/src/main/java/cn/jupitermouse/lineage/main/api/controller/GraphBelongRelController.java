package cn.jupitermouse.lineage.main.api.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.jupitermouse.lineage.graph.model.FieldEntity;
import cn.jupitermouse.lineage.graph.model.TableEntity;
import cn.jupitermouse.lineage.graph.service.OfRelService;
import cn.jupitermouse.lineage.main.api.dto.NodeRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 属于关系
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@RestController
@RequestMapping("/rel/belong/")
@Api(tags = "属于关系", value = "属于关系")
public class GraphBelongRelController {

    private final OfRelService ofRelService;

    public GraphBelongRelController(OfRelService ofRelService) {
        this.ofRelService = ofRelService;
    }

    @ApiOperation(value = "field->table属于关系", notes = "field->table属于关系")
    @PostMapping(value = "/field")
    public ResponseEntity<Void> saveFieldBelongTable(NodeRequestDTO dto) {
        ofRelService.createFieldOfTableRel(Collections.singletonList(dto.getFieldDTO()), dto.getTableDTO());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "fields->table属于关系", notes = "field->table属于关系")
    @GetMapping("/fields")
    public ResponseEntity<Void> saveFieldBelongTable(List<FieldEntity> fields, TableEntity table) {
        ofRelService.createFieldOfTableRel(fields, table);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
