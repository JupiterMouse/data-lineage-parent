package cn.site.jupitermouse.lineage.parser.druid.util;

import java.util.ArrayList;
import java.util.List;

import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;

/**
 * <p>
 * 树结构相关操作工具类
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public class TreeNodeUtil {
    private TreeNodeUtil() {
    }

    /**
     * 返货所有叶子节点
     *
     * @param root Tree
     * @param <T>  节点类型
     * @return List<T>
     */
    public static <T> List<T> searchTreeLeafNodeList(TreeNode<T> root) {
        List<T> list = new ArrayList<>();
        traverseNodeLineageTree(root, list);
        return list;
    }

    /**
     * 返回叶子列表
     *
     * @param root           TreeNode<TableNode>
     * @param sourceNodeList List<TableNode>
     */
    private static <T> void traverseNodeLineageTree(TreeNode<T> root, List<T> sourceNodeList) {
        if (root.isLeaf()) {
            sourceNodeList.add(root.getValue());
        } else {
            root.getChildList().forEach(node -> traverseNodeLineageTree(node, sourceNodeList));
        }
    }
}
