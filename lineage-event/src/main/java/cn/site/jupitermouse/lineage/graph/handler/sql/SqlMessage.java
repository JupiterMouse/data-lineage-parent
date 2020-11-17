package cn.site.jupitermouse.lineage.graph.handler.sql;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * SQL对象承载
 * </p>
 *
 * @author JupiterMouse 2020/10/09
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SqlMessage {

    private String sourceType;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * platformName
     */
    private String platformName;

    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 数据源Code
     */
    private String datasourceCode;

    /**
     * clusterName
     */
    private String clusterName;

    /**
     * catalog
     */
    private String catalogName;
    /**
     * schema
     */
    private String schemaName;
    /**
     * 表名
     */
    private String tableName;

    /**
     * sql
     */
    private String sql;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * 任务参数
     */
    Map<String, String> job;
}
