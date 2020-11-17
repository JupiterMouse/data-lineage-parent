package cn.site.jupitermouse.lineage.parser.druid.process.statement;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import com.alibaba.druid.sql.ast.SQLStatement;

/**
 * <p>
 * Statement 语句解析。
 * 判断属于哪种模式的语句后进行处理
 * eg:
 * PGInsertStatement: postgresql insert
 * SQLCreateViewStatement: create view as
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
