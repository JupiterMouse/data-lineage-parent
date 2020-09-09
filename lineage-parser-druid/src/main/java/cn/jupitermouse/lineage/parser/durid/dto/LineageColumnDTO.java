package cn.jupitermouse.lineage.parser.durid.dto;

import java.util.List;

import cn.jupitermouse.lineage.parser.model.ColumnNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 血缘Column DTO ColumnNode 中 表不能为空
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineageColumnDTO {

    private ColumnNode column;

    private List<ColumnNode> sourceColumnList;
}
