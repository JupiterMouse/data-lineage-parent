package cn.jupitermouse.lineage.parser.durid.analyse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import cn.jupitermouse.lineage.parser.durid.dto.LineageColumnDTO;
import cn.jupitermouse.lineage.parser.durid.dto.SqlRequestDTO;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.durid.tracer.ColumnLineageTracer;
import cn.jupitermouse.lineage.parser.model.ColumnNode;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;

/**
 * <p>
 * 血缘分析
 * </p>
 *
 * @author JupiterMouse 2020/09/15
 * @since 1.0
 */
public class LineageAnalyzer {

    /**
     * 返回原始的血缘树
     *
     * @param dto SqlRequestDTO
     * @return TreeNode<TableNode>
     */
    public TreeNode<TableNode> getLineageTree(SqlRequestDTO dto) {
        // 构建血缘树
        // 生成初始序列
        AtomicInteger sequence = new AtomicInteger();
        // 构建根节点血缘树
        TreeNode<TableNode> root = new TreeNode<>();
        // 解析SQL后生成的statement
        SQLStatement statement = SQLUtils.parseSingleStatement(dto.getSql(), dto.getDbType());
        // 查询树
        ProcessorRegister.getStatementProcessor(statement.getClass())
                .process(dto.getDbType(), sequence, root, statement);
        // TODO 字段清洗 && 缺失字段补全 && 字段最可能来源的表
        return root;
    }

    /**
     * 返回原始的字段血缘树
     *
     * @param dto SqlRequestDTO
     * @return List<TreeNode < ColumnNode>>
     */
    public List<TreeNode<ColumnNode>> getColumnLineageTree(SqlRequestDTO dto) {
        // 生成血缘树
        TreeNode<TableNode> root = this.getLineageTree(dto);
        //
        TableNode rootValue = root.getValue();
        TableNode tempNode = new TableNode();
        BeanUtils.copyProperties(rootValue, tempNode, "columns");
        // 第一个有字段的节点的所有字段的来源
        TreeNode<TableNode> firstHaveColumnTableNode = this.findFirstHaveColumnTableNode(root);
        List<ColumnNode> lineageColumnList = firstHaveColumnTableNode.getValue().getColumns();
        List<TreeNode<ColumnNode>> lineageColumnTreeList = new ArrayList<>();
        // TODO REMOVE NEW
        ColumnLineageTracer columnLineageTracer = new ColumnLineageTracer();
        lineageColumnList.forEach(lineageColumn -> {
            TreeNode<ColumnNode> node = new TreeNode<>();
            node.setValue(lineageColumn);
            // TODO 暂时 第一个有字段的节点，表名更新为最顶级表名
            lineageColumn.setOwner(tempNode);
            lineageColumnTreeList.add(node);
            columnLineageTracer.traceColumnLineageTree(dto.getDbType(), node, firstHaveColumnTableNode);
        });
        return lineageColumnTreeList;
    }

    /**
     * 获取血缘字段的来源列表
     *
     * @param dto SqlRequestDTO
     * @return List<LineageColumnDTO>
     */
    public List<LineageColumnDTO> getColumnLineage(SqlRequestDTO dto) {
        List<TreeNode<ColumnNode>> lineageColumnTreeList = this.getColumnLineageTree(dto);
        // 处理 lineageColumnTree -> List<LineageColumnDTO>
        List<LineageColumnDTO> resultColumnDTOList = new ArrayList<>();
        for (TreeNode<ColumnNode> lineageColumnTree : lineageColumnTreeList) {
            ColumnNode columnNode = lineageColumnTree.getValue();
            ColumnNode target = new ColumnNode();
            BeanUtils.copyProperties(columnNode, target, "owner", "sourceColumns");
            // 设为首级表
            target.setOwner(columnNode.getOwner());
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
     * 获取血缘字段的来源列表
     *
     * @return List<LineageColumnDTO> lineageColumnTreeList
     */
    public List<LineageColumnDTO> getColumnLineage(List<TreeNode<ColumnNode>> lineageColumnTreeList) {
        List<LineageColumnDTO> resultColumnDTOList = new ArrayList<>();
        for (TreeNode<ColumnNode> lineageColumnTree : lineageColumnTreeList) {
            ColumnNode columnNode = lineageColumnTree.getValue();
            ColumnNode target = new ColumnNode();
            BeanUtils.copyProperties(columnNode, target, "owner", "sourceColumns");
            // 设为首级表
            target.setOwner(columnNode.getOwner());
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
     * 获取TreeNode中来源的节点
     *
     * @param dto SqlRequestDTO
     * @return TreeNode<TableNode>
     */
    public List<TableNode> getTableLineageList(SqlRequestDTO dto) {
        TreeNode<TableNode> lineageTree = this.getLineageTree(dto);
        List<TableNode> sourceTableNodeList = new ArrayList<>();
        this.traverseTableLineageTree(lineageTree, sourceTableNodeList);
        return sourceTableNodeList;
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
