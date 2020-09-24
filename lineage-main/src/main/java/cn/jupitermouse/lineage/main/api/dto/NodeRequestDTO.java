package cn.jupitermouse.lineage.main.api.dto;

import cn.jupitermouse.lineage.graph.domain.model.FieldEntity;
import cn.jupitermouse.lineage.graph.domain.model.TableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeRequestDTO {

    private FieldEntity fieldDTO;
    private TableEntity tableDTO;
}
