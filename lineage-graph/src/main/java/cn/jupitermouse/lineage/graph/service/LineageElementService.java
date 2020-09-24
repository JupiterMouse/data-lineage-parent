package cn.jupitermouse.lineage.graph.service;

/**
 * <p>
 * 血缘元素
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public interface LineageElementService {

    /**
     * 输入表血缘到图
     *
     * @param cluster cluster
     * @param catalog catalog
     * @param schema  schema
     * @param dbType  数据库类型
     * @param sql     执行的SQL
     */
    void ingestTableLineage(String cluster, String catalog, String schema, String dbType, String sql);

    /**
     * 字段血缘
     *
     * @param cluster cluster
     * @param catalog catalog
     * @param schema  schema
     * @param dbType  数据库类型
     * @param sql     执行的SQL
     */
    void ingestColumnLineage(String cluster, String catalog, String schema, String dbType, String sql);

}
