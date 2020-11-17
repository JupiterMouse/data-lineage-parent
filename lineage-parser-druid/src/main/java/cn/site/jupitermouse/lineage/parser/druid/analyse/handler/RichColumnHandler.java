package cn.site.jupitermouse.lineage.parser.druid.analyse.handler;

import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlResponseContext;
import cn.site.jupitermouse.lineage.parser.druid.model.ColumnNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.constant.PriorityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 填充信息
 * 1. 填充字段信息 setTableExpression(String)
 *
 * @author JupiterMouse 2020/10/15
 * @see ColumnNode#setTableExpression(String) ()
 * </p>
 * @since 1.0
 */
@Order(PriorityConstants.LITTLE_LOW - 10)
@Component
@Slf4j
public class RichColumnHandler implements IHandler {

    @Override
    public void handleRequest(SqlRequestContext request, SqlResponseContext response) {
        fillingTableExpression(response.getLineageTableTree());
    }

    /**
     * Tree<Table> 填充Column的TableExpression 字段
     *
     * @param root 当前表关系树节点
     */
    public void fillingTableExpression(TreeNode<TableNode> root) {
        root.getValue().getColumns()
                .forEach(columnNode -> columnNode.setTableExpression(root.getValue().getExpression()));
        if (root.isLeaf()) {
            return;
        }
        root.getChildList().forEach(this::fillingTableExpression);
    }

}
