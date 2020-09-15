package cn.jupitermouse.lineage.parser.durid.process.tablesource;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;

/**
 * <p>
 * TableSource 处理
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
public interface TableSourceProcessor {

    /**
     * TableSource 的处理
     *
     * @param dbType         数据库类型
     * @param sequence       序列
     * @param parent         父节点
     * @param sqlTableSource SQLTableSource 子类
     */
    void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> parent, SQLTableSource sqlTableSource);
}
