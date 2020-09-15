package cn.jupitermouse.lineage.main.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * LineageRequestDTO
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "数据血缘请求参数", description = "Lineage Request")
public class LineageRequestDTO {

    @ApiModelProperty(value = "SQL", required = true)
    private String sql;

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dbType;
}
