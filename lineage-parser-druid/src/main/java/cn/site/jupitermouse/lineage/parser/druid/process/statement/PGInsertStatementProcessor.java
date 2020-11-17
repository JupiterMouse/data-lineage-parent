package cn.site.jupitermouse.lineage.parser.druid.process.statement;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;

/**
 * <p>
 * PGInsertStatement 处理
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
@SQLObjectType(clazz = PGInsertStatement.class)
public class PGInsertStatementProcessor extends AbstractStatementProcessor {

    @Override
    public void doProcess(String dbType, AtomicInteger sequence, TreeNode<TableNode> root, SQLStatement statement) {
        PGInsertStatement pgInsertStatement = (PGInsertStatement) statement;
        SQLExprTableSource sqlExprTableSource = pgInsertStatement.getTableSource();
        // 构建根表
        this.constructRootNode(dbType, root, statement, sqlExprTableSource);
        // 获取SQLSelectQuery
        SQLSelectQuery sqlSelectQuery = pgInsertStatement.getQuery().getQuery();
        // 执行SQLSelectQuery 查询
        ProcessorRegister.getSQLSelectQueryProcessor(sqlSelectQuery.getClass())
                .process(dbType, sequence, root, sqlSelectQuery);
    }


}
