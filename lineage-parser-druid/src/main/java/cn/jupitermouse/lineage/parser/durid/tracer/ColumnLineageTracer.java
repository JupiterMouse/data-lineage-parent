package cn.jupitermouse.lineage.parser.durid.tracer;

import java.util.*;

import cn.jupitermouse.lineage.parser.exception.ParserException;
import cn.jupitermouse.lineage.parser.model.ColumnNode;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 字段血缘生成
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Slf4j
public class ColumnLineageTracer {

    private final Map<Long, List<TreeNode<TableNode>>> recentTreeNodeMap = new HashMap<>();

    /**
     * 构建字段来源的字段血缘
     *
     * @param currentColumnNode 当前的字段节点
     * @param tableNode         表节点
     */
    public void traceColumnLineageTree(String dbType, TreeNode<ColumnNode> currentColumnNode,
            TreeNode<TableNode> tableNode) {
        // 当前字段向下检索列的来源, 后面需定位当前列所在的节点
        ColumnNode currentColumn = currentColumnNode.getValue();
        // 根据AST构造关系，这里来源表最多一层，所以判断来源是否有值，如果有值，那么以来源字段构建检索
        if (CollectionUtils.isNotEmpty(currentColumn.getSourceColumns())) {
            // 来源字段
            List<ColumnNode> sourceColumnList = currentColumn.getSourceColumns();
            // 遍历存入能够直接取到的字段
            sourceColumnList.forEach(column -> {
                TreeNode<ColumnNode> middleColumnNode = new TreeNode<>();
                currentColumnNode.addChild(middleColumnNode);
                middleColumnNode.setValue(column);
                // 依旧以当前的表节点去向下检索来源字段
                this.traceColumnLineageTree(dbType, middleColumnNode, tableNode);
            });
            // 来源字段不为空，提前结束
            return;
        }
        // 字段肯定来源于下一级的节点去寻找, 构建离当前节点最近的别名节点
        List<TreeNode<TableNode>> nearestTableNodeList = this.nearestTableNodes(tableNode);
        // 当前字段的定位表名
        String scanTableName = currentColumnNode.getValue().getTableName();
        // 字段名称为空修复
        if (StringUtils.isEmpty(scanTableName)) {
            scanTableName = this.repairMissingTableName(currentColumnNode.getValue(), dbType);
        }
        // 先遍历来源的表节点列表
        for (TreeNode<TableNode> currentRecentlyTableNode : nearestTableNodeList) {
            TableNode lineageTable = currentRecentlyTableNode.getValue();
            //  如果是叶子节点，直接返回表名作为别名
            String alias = Optional.ofNullable(lineageTable.getAlias()).orElse(lineageTable.getName());
            if (!scanTableName.equals(alias)) {
                // 下一次循环
                continue;
            }
            if (currentRecentlyTableNode.isLeaf()) {
                TreeNode<ColumnNode> endColumnNode = new TreeNode<>();
                endColumnNode.setValue(ColumnNode.builder()
                        .name(currentColumnNode.getValue().getName())
                        .tableName(scanTableName)
                        .owner(lineageTable)
                        // 记录节点ID
                        .tableTreeNodeId(currentRecentlyTableNode.getId().get())
                        .build());
                currentColumnNode.addChild(endColumnNode);
                return;
                // 1. 终止
            }
            // 定位的列名 先取别名，别名取不到取表名
            String scanColumnName = Optional.ofNullable(currentColumnNode.getValue().getAlias())
                    .orElse(currentColumnNode.getValue().getName());
            // 获取当前中间节点的字段名
            List<ColumnNode> columns = currentRecentlyTableNode.getValue().getColumns();
            // 设置节点所有表为当前
            for (ColumnNode column : columns) {
                String name = Optional.ofNullable(column.getAlias()).orElse(column.getName());
                // 如果相等 构建关系
                if (scanColumnName.equals(name)) {
                    TreeNode<ColumnNode> midColumnTree = new TreeNode<>();
                    currentColumnNode.addChild(midColumnTree);
                    midColumnTree.setValue(column);
                    // 继续向下遍历
                    this.traceColumnLineageTree(dbType, midColumnTree, currentRecentlyTableNode);
                    return;
                }
            }
        }
        // for 循环完之后还是找不到，判断长度是否为1并且为leaf节点
        this.possibleColumnSource(currentColumnNode, nearestTableNodeList);
    }

    private void possibleColumnSource(TreeNode<ColumnNode> currentColumnNode,
            List<TreeNode<TableNode>> nearestTableNodeList) {
        if (!CollectionUtils.isEmpty(nearestTableNodeList) && nearestTableNodeList.size() == 1
                && nearestTableNodeList.get(0).isLeaf()) {
            // 2. 终止
            TreeNode<ColumnNode> endColumnNode = new TreeNode<>();
            currentColumnNode.addChild(endColumnNode);
            endColumnNode
                    .setValue(ColumnNode.builder()
                            // 最后是取真实字段名
                            .name(currentColumnNode.getValue().getName())
                            .tableName(nearestTableNodeList.get(0).getValue().getName())
                            .owner(nearestTableNodeList.get(0).getValue())
                            .build()
                    );
        } else {
            // 兜底：记录找不到的信息
            log.warn("columnNodeTree:{} not ended", currentColumnNode.getId());
        }
    }

    /**
     * 有效节点判断
     *
     * @param node 当前节点
     * @return nearestTableNodeList List<TreeNode<TableNode>>
     */
    @SuppressWarnings("unsed")
    private boolean validNode(TreeNode<TableNode> node) {
        return node.getValue().getAlias() != null || node.getValue().getIsVirtualTemp() == null;
    }

    /**
     * 查找表血缘树最近的节点 TODO
     *
     * @param currentNode 当前的🌲 节点
     * @return List<TreeNode < TableNode>>
     */
    public List<TreeNode<TableNode>> nearestTableNodes(TreeNode<TableNode> currentNode) {
        List<TreeNode<TableNode>> hitTreeNodeList = recentTreeNodeMap.get(currentNode.getId().get());
        if (CollectionUtils.isNotEmpty(hitTreeNodeList)) {
            return hitTreeNodeList;
        }
        List<TreeNode<TableNode>> nearestTableNodeList = new ArrayList<>();
        this.nearestTableNodes(currentNode, nearestTableNodeList);
        // 放入缓存
        recentTreeNodeMap.put(currentNode.getId().get(), nearestTableNodeList);
        return nearestTableNodeList;
    }

    /**
     * 查询离当前节点最近的节点
     *
     * @param currentNode          当前节点
     * @param nearestTableNodeList 存储当前的最近节点
     */
    public void nearestTableNodes(TreeNode<TableNode> currentNode, List<TreeNode<TableNode>> nearestTableNodeList) {
        // 找完所有的节点都没有找到，那么从查询的中断节点里面去寻找，如果别名为空 找下一个节点，如果匹配到别名就停止并返回
        if (currentNode.isLeaf()) {
            nearestTableNodeList.add(currentNode);
            return;
        }
        // 如果找不到就找子节点
        currentNode.getChildList().forEach(node -> {
            // 子节点，找到就结束
            if (node.getValue().getAlias() != null || node.getValue().getIsVirtualTemp() == null) {
                nearestTableNodeList.add(node);
                // 当本身是别名节点时不往下走
                return;
            }
            // 找不到继续向下
            this.nearestTableNodes(node, nearestTableNodeList);
        });
    }

    /**
     * 修复缺失的表名
     *
     * @param columnNode columnNode
     * @param dbType     dbType
     * @return 表名
     */
    public String repairMissingTableName(ColumnNode columnNode, String dbType) {
        if (StringUtils.isEmpty(columnNode.getExpression())) {
            throw new ParserException("repair missing table, expression can't null.");
        }
        SQLStatement stmt = SQLUtils.parseSingleStatement(columnNode.getExpression(), dbType);
        PGSchemaStatVisitor pgSchemaStatVisitor = new PGSchemaStatVisitor();
        stmt.accept(pgSchemaStatVisitor);
        return pgSchemaStatVisitor.getTables().keySet().stream().findFirst()
                .orElseThrow(() -> new ParserException("repair missing table failed,column expression[%s].",
                        columnNode.getExpression()))
                .getName();

    }

}
