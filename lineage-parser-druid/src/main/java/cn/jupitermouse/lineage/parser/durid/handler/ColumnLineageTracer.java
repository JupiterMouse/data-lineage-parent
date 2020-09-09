package cn.jupitermouse.lineage.parser.durid.handler;

import java.util.*;

import org.springframework.util.CollectionUtils;

import cn.jupitermouse.lineage.parser.model.ColumnNode;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;

/**
 * <p>
 * 字段血缘生成
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public class ColumnLineageTracer {

    /**
     * 构建字段来源的字段血缘
     *
     * @param currentColumnNode 当前的字段节点
     * @param tableNode         表节点
     */
    public void traceabilityFieldSource(TreeNode<ColumnNode> currentColumnNode, TreeNode<TableNode> tableNode,
            String dbType) {
        // 当前字段向下检索列的来源, 后面需定位当前列所在的节点
        ColumnNode currentColumn = currentColumnNode.getValue();
        // 根据AST构造关系，这里来源表最多一层，所以判断来源是否有值，如果有值，那么以来源字段构建检索
        if (!CollectionUtils.isEmpty(currentColumn.getSourceColumns())) {
            // 来源字段
            List<ColumnNode> sourceColumnList = currentColumn.getSourceColumns();
            // 遍历存入能够直接取到的字段
            sourceColumnList.forEach(column -> {
                TreeNode<ColumnNode> middleColumnNode = new TreeNode<>();
                currentColumnNode.addChild(middleColumnNode);
                middleColumnNode.setValue(column);
                // 依旧以当前的表节点去向下检索来源字段
                this.traceabilityFieldSource(middleColumnNode, tableNode, dbType);
            });
        } else {
            // 当前字段的定位表名
            String scanTableName = currentColumnNode.getValue().getTableName();
            // 如果表名为空找到最可能的表名：
            if (scanTableName == null) {
                // 如果scanTableName为null，当前SQL的形式 类似如： select a,b from table1;
                // todo 考虑在构建时就将字段为空的表名构建好
                SQLStatement stmt = SQLUtils.parseSingleStatement(currentColumnNode.getValue().getExpression(), dbType);
                PGSchemaStatVisitor pgSchemaStatVisitor = new PGSchemaStatVisitor();
                stmt.accept(pgSchemaStatVisitor);
                scanTableName = pgSchemaStatVisitor.getTables().keySet().stream().findFirst()
                        .orElseThrow(NullPointerException::new)
                        .getName();
            }

            List<TreeNode<TableNode>> nearestTableNodeList = new ArrayList<>();
            // 构建离当前节点最近的别名节点
            this.nearestTableNodes(tableNode, nearestTableNodeList);

            // 深度遍历
            for (TreeNode<TableNode> currentRecentlyTableNode : nearestTableNodeList) {
                TableNode lineageTable = currentRecentlyTableNode.getValue();
                //  如果是叶子节点，直接返回表名作为别名
                String alias = Optional.ofNullable(lineageTable.getAlias()).orElse(lineageTable.getName());
                if (scanTableName.equals(alias)) {
                    if (currentRecentlyTableNode.isLeaf()) {
                        TreeNode<ColumnNode> endColumnNode = new TreeNode<>();
                        currentColumnNode.addChild(endColumnNode);
                        endColumnNode
                                .setValue(ColumnNode.builder()
                                        .name(currentColumnNode.getValue().getName())
                                        .tableName(scanTableName)
                                        .owner(lineageTable)
                                        .build()
                                );
                        return;
                        // 1. 终止
                    } else {
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
                                TreeNode<ColumnNode> midColumnNode = new TreeNode<>();
                                currentColumnNode.addChild(midColumnNode);
                                midColumnNode.setValue(column);
                                // 继续向下遍历
                                this.traceabilityFieldSource(midColumnNode, currentRecentlyTableNode, dbType);
                                return;
                            }
                        }
                    }
                } else {
                    // 当前遍历的节点找不到，就继续向下搜索
                    if (currentRecentlyTableNode.isLeaf()) {
                        // 结束此次循环
                        continue;
                    }
                    this.traceabilityFieldSource(currentColumnNode, currentRecentlyTableNode, dbType);
                }
            }
            // for 循环完之后还是找不到，判断长度是否为1并且为leaf节点
            if (!CollectionUtils.isEmpty(nearestTableNodeList) && nearestTableNodeList.size() == 1 && nearestTableNodeList.get(0).isLeaf()){
                // 2. 终止
                ColumnNode column = currentColumnNode.getValue();
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
            }
        }

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

}
