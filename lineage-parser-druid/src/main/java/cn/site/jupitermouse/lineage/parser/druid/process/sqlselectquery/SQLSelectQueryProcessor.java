package cn.site.jupitermouse.lineage.parser.druid.process.sqlselectquery;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;

/**
 * <p>
 * SQLSelectQuery 处理
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public interface SQLSelectQueryProcessor {

    /**
     * SQLSelectQuery 处理
     *
     * @param dbType         数据库类型
     * @param sequence       节点主键
     * @param parent         传入的节点
     * @param sqlSelectQuery SQLSelectQuery子类
     */
    void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> parent, SQLSelectQuery sqlSelectQuery);

}
