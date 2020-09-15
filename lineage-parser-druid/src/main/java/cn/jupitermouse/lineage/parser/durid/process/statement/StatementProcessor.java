package cn.jupitermouse.lineage.parser.durid.process.statement;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.ast.SQLStatement;

/**
 * <p>
 * Statement
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public interface StatementProcessor {

    /**
     * SQLStatement 处理
     *
     * @param dbType    数据库类型
     * @param sequence  序列
     * @param root      当前表节点
     * @param statement SQLStatement
     */
    void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> root, SQLStatement statement);
}
