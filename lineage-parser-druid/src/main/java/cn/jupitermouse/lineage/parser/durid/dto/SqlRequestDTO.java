package cn.jupitermouse.lineage.parser.durid.dto;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * SqlRequestDTO
 * </p>
 *
 * @author JupiterMouse 2020/09/15
 * @since 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlRequestDTO {

    private @NonNull
    String dbType;
    private String sql;
    private String tenantId;
    private String datasourceCode;
    private String databaseName;
    private String schemaName;
}
