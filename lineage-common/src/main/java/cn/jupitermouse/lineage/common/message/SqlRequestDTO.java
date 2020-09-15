package cn.jupitermouse.lineage.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Sql 消息类型
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

    /**
     * 数据库类型
     */
    private String dbType;
    /**
     * sql
     */
    private String sql;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 数据源Code
     */
    private String datasourceCode;
    /**
     * 数据库或schema
     */
    private String database;
    /**
     * schema
     */
    private String schema;
    /**
     * 表名
     */
    private String table;

    /**
     * 创建时间
     */
    private Long createTime;
}
