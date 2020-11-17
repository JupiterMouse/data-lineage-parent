package cn.site.jupitermouse.lineage.parser.druid.analyse.handler;

import java.util.ArrayList;
import java.util.List;

import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import cn.site.jupitermouse.lineage.parser.druid.tracer.ColumnLineageTracer;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlResponseContext;
import cn.site.jupitermouse.lineage.parser.druid.constant.PriorityConstants;
import cn.site.jupitermouse.lineage.parser.druid.exception.ParserException;
import cn.site.jupitermouse.lineage.parser.druid.model.ColumnNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.tracer.ColumnLineageTracerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 字段血缘解析
 * 顺序为最后执行字段解析
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
@Order(PriorityConstants.LITTLE_LOW)
@Component
public class LineageColumnHandler implements IHandler {

    @Override
    public void handleRequest(SqlRequestContext request, SqlResponseContext response) {
        handleColumnRelation(request, response);
    }

    private void handleColumnRelation(SqlRequestContext sqlContext, SqlResponseContext response) {
        TreeNode<TableNode> lineageTableTree = response.getLineageTableTree();
        TreeNode<TableNode> firstHaveColumnTableNode = this.findFirstHaveColumnTableNode(lineageTableTree);
        List<ColumnNode> rootColumns = firstHaveColumnTableNode.getValue().getColumns();
        if (CollectionUtils.isEmpty(rootColumns)) {
            throw new ParserException("node.not.found.effective");
        }
        ColumnLineageTracer columnLineageTracer = ColumnLineageTracerFactory.getDefaultTracer();
        // 获取到字段血缘树
        List<TreeNode<ColumnNode>> lineageColumnTreeList = new ArrayList<>();
        rootColumns.stream().map(TreeNode::of).forEach(nodeTreeNode -> {
            lineageColumnTreeList.add(nodeTreeNode);
            columnLineageTracer.traceColumnLineageTree(
                    sqlContext.getDbType(),
                    nodeTreeNode,
                    firstHaveColumnTableNode);
        });
        // save
        response.setLineageColumnTreeList(lineageColumnTreeList);
    }

    /**
     * 找到第一个有字段的节点
     *
     * @param root TableNode
     * @return TreeNode<TableNode>
     */
    private TreeNode<TableNode> findFirstHaveColumnTableNode(TreeNode<TableNode> root) {
        if (!CollectionUtils.isEmpty(root.getValue().getColumns())) {
            return root;
        }
        if (CollectionUtils.isEmpty(root.getChildList()) || root.getChildList().size() != 1) {
            throw new ParserException("node.found.more");
        }
        // 第一个有字段的节点，其父级仅有一个子元素
        return root.getChildList().get(0);
    }

}
