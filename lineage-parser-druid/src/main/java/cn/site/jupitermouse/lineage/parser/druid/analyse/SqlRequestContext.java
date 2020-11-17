package cn.site.jupitermouse.lineage.parser.druid.analyse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * SqlRequestContext
 * </p>
 *
 * @author JupiterMouse 2020/09/15
 * @since 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlRequestContext {

    /**
     * sql 处理的数据库类型
     */
    private String dbType;
    /**
     * 单条 sql 语句
     */
    private String sql;
    /**
     * SQL 执行时的schema
     */
    private String schemaName;

    //===============================================================================
    //  redundancy fields
    //===============================================================================

    private String platformName;
    private String clusterName;
    private Long tenantId;
    private String datasourceCode;
    private String catalogName;
}
