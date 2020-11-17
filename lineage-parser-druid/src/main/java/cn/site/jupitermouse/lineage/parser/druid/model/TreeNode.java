package cn.site.jupitermouse.lineage.parser.druid.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 血缘解析时关系存储结构-树结构
 * 解析时，先将AST树转换为此结构
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public class TreeNode<T> {

    AtomicLong id = new AtomicLong(0);

    T value;

    TreeNode<T> parent;

    List<TreeNode<T>> childList;

    int height;

    int subtreeSize;

    public TreeNode() {
    }

    TreeNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TreeNode<T> getRoot() {
        TreeNode<T> current = this;
        while (current.parent != null) {
            current = current.parent;
        }
        return current;
    }

    public void initChildList() {
        if (childList == null) {
            childList = new ArrayList<>();
        }
    }

    public boolean isLeaf() {
        if (childList == null) {
            return true;
        }
        return childList.size() == 0;
    }

    public boolean isOneChildAndLeaf() {
        return childList != null && childList.size() == 1 && childList.get(0).isLeaf();
    }

    public void addChild(TreeNode<T> childNode) {
        initChildList();
        childNode.parent = this;
        childList.add(childNode);
        childNode.height = Optional.ofNullable(childNode.parent)
                .map(node -> node.height + 1)
                .orElse(0);
        this.subtreeSize++;
        childNode.id = new AtomicLong(Optional.ofNullable(childNode.parent)
                .map(node -> node.id.get() + 1)
                .orElse(0L));
    }

    public List<TreeNode<T>> getChildList() {
        return childList;
    }

    public AtomicLong getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getSubtreeSize() {
        return subtreeSize;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public static <T> TreeNode<T> of(T data) {
        TreeNode<T> treeNode = new TreeNode<>();
        treeNode.setValue(data);
        return treeNode;
    }

}


