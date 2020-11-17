package cn.site.jupitermouse.lineage.parser.druid.process.tablesource;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
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
