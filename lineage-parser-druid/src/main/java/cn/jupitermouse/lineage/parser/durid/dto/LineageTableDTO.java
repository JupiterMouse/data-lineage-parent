package cn.jupitermouse.lineage.parser.durid.dto;

import java.util.List;

import cn.jupitermouse.lineage.parser.model.TableNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineageTableDTO {

    private TableNode root;

    private List<TableNode> sourceTableList;
}
