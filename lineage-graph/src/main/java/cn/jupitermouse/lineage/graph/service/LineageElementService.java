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
     * @param sql    执行的SQL
     * @param dbType 数据库
     */
    void ingestTableLineage(String dbType, String sql);

    /**
     * 输入字段血缘到图
     *
     * @param sql    执行的SQL
     * @param dbType 数据库
     */
    void ingestColumnLineage(String dbType, String sql);

}
