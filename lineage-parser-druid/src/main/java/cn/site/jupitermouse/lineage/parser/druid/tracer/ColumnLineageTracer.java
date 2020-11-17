package cn.site.jupitermouse.lineage.parser.druid.tracer;

import cn.site.jupitermouse.lineage.parser.druid.model.ColumnNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;

/**
 * <p>
 * 构建字段血缘
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public interface ColumnLineageTracer {

    /**
     * 构建血缘关系
     *
     * @param dbType            数据库类型
     * @param currentColumnNode 当前的Column
     * @param tableNode         表血缘树
     */
    void traceColumnLineageTree(String dbType, TreeNode<ColumnNode> currentColumnNode,
                                TreeNode<TableNode> tableNode);
}
