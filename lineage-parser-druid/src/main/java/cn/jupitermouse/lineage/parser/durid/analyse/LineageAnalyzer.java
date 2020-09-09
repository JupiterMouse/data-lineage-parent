package cn.jupitermouse.lineage.parser.durid.analyse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import cn.jupitermouse.lineage.parser.durid.dto.LineageColumnDTO;
import cn.jupitermouse.lineage.parser.durid.handler.ColumnLineageTracer;
import cn.jupitermouse.lineage.parser.durid.handler.SqlTreeHandler;
import cn.jupitermouse.lineage.parser.model.ColumnNode;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;

/**
 * <p>
 * 血缘分析主函数
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public class LineageAnalyzer {

    public TreeNode<TableNode> lineageTreeAnalyzer(String sql, String dbType) {
        return new SqlTreeHandler().constructLineageTree(sql, dbType);
    }

    /**
     * 原生的字段血缘树
     *
     * @param sql    SQL
     * @param dbType 数据库类型
     * @return List<TreeNode < ColumnNode>>
     */
    public List<TreeNode<ColumnNode>> originColumnLineageTreeAnalyzer(String sql, String dbType) {
        // 数据血缘SQL 解析生成字段级别血缘
        ColumnLineageTracer columnLineageTracer = new ColumnLineageTracer();
        // 生成血缘树
        TreeNode<TableNode> root = this.lineageTreeAnalyzer(sql, dbType);
        // 第一个有字段的节点的所有字段的来源
        TreeNode<TableNode> firstHaveColumnTableNode = this.findFirstHaveColumnTableNode(root);
        List<ColumnNode> lineageColumnList = firstHaveColumnTableNode.getValue().getColumns();
        List<TreeNode<ColumnNode>> lineageColumnTreeList = new ArrayList<>();

        lineageColumnList.forEach(lineageColumn -> {
            TreeNode<ColumnNode> node = new TreeNode<>();
            node.setValue(lineageColumn);
            lineageColumnTreeList.add(node);
            columnLineageTracer.traceabilityFieldSource(node, firstHaveColumnTableNode, dbType);
        });
        return lineageColumnTreeList;
    }

    /**
     * 封装的字段血缘 todo 考虑重建
     *
     * @param sql    sql
     * @param dbType dbType
     * @return List<LineageColumnDTO>
     */
    public List<LineageColumnDTO> columnLineageAnalyzer(String sql, String dbType) {
        // 数据血缘SQL 解析生成字段级别血缘
        ColumnLineageTracer columnLineageTracer = new ColumnLineageTracer();
        // 生成血缘树
        TreeNode<TableNode> root = this.lineageTreeAnalyzer(sql, dbType);
        // 第一个有字段的节点的所有字段的来源
        TreeNode<TableNode> firstHaveColumnTableNode = this.findFirstHaveColumnTableNode(root);
        List<ColumnNode> lineageColumnList = firstHaveColumnTableNode.getValue().getColumns();
        List<TreeNode<ColumnNode>> lineageColumnTreeList = new ArrayList<>();

        lineageColumnList.forEach(lineageColumn -> {
            TreeNode<ColumnNode> node = new TreeNode<>();
            node.setValue(lineageColumn);
            lineageColumnTreeList.add(node);
            columnLineageTracer.traceabilityFieldSource(node, firstHaveColumnTableNode, dbType);
        });

        // 处理 lineageColumnTree -> List<LineageColumnDTO>
        List<LineageColumnDTO> resultColumnDTOList = new ArrayList<>();
        for (TreeNode<ColumnNode> lineageColumnTree : lineageColumnTreeList) {
            ColumnNode columnNode = lineageColumnTree.getValue();
            ColumnNode target = new ColumnNode();
            BeanUtils.copyProperties(columnNode, target, "owner", "sourceColumns");
            // 设为首级表
            target.setOwner(root.getValue());
            LineageColumnDTO lineageColumnDTO = LineageColumnDTO.builder()
                    .column(target).build();
            List<ColumnNode> sourceTableList = new ArrayList<>();
            this.traverseTableLineageTree(lineageColumnTree, sourceTableList);
            lineageColumnDTO.setSourceColumnList(sourceTableList);
            resultColumnDTOList.add(lineageColumnDTO);
        }
        return resultColumnDTOList;
    }

    /**
     * 返回表血缘来自列表
     *
     * @param root            TreeNode<TableNode>
     * @param sourceTableList List<TableNode>
     */
    public <T> void traverseTableLineageTree(TreeNode<T> root, List<T> sourceTableList) {
        if (root.isLeaf()) {
            sourceTableList.add(root.getValue());
        } else {
            root.getChildList().forEach(node -> this.traverseTableLineageTree(node, sourceTableList));
        }
    }

    /**
     * 找到第一个有字段的表节点
     *
     * @param root root
     * @return TreeNode<TableNode>
     */
    private TreeNode<TableNode> findFirstHaveColumnTableNode(TreeNode<TableNode> root) {
        if (!CollectionUtils.isEmpty(root.getValue().getColumns())) {
            return root;
        }
        if (CollectionUtils.isEmpty(root.getChildList()) || root.getChildList().size() != 1) {
            throw new UnsupportedOperationException("findFirstColumnNode 不希望的异常");
        }
        // 第一个有字段的节点，其父级仅有一个子元素
        return root.getChildList().get(0);
    }

}
