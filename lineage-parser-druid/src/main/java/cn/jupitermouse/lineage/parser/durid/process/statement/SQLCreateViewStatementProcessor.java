package cn.jupitermouse.lineage.parser.durid.process.statement;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;

/**
 * <p>
 * SQLCreateViewStatement 处理
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@SQLObjectType(clazz = SQLCreateViewStatement.class)
public class SQLCreateViewStatementProcessor extends AbstractStatementProcessor {

    @Override
    public void doProcess(String dbType, AtomicInteger sequence, TreeNode<TableNode> root, SQLStatement statement) {
        SQLCreateViewStatement createViewStatement = (SQLCreateViewStatement) statement;
        SQLExprTableSource sqlExprTableSource = createViewStatement.getTableSource();
        // 构建根表
        this.constructRootNode(dbType, root, statement, sqlExprTableSource);
        // 获取SQLSelectQuery
        SQLSelectQuery sqlSelectQuery = createViewStatement.getSubQuery().getQuery();
        ProcessorRegister.getSQLSelectQueryProcessor(sqlSelectQuery.getClass())
                .process(dbType, sequence, root, sqlSelectQuery);
    }

}
