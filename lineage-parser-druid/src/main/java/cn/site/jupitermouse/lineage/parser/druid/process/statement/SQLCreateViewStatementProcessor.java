package cn.site.jupitermouse.lineage.parser.druid.process.statement;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;

/**
 * <p>
 * SQLCreateViewStatement 处理
 * </p>
 * eg：
 * <p>
 * create view view_test as
 * select temp.a1,temp.a2 (
 * select a1,a2 from table1
 * ) temp
 * </p>
 *
 * @author JupiterMouse 2020/10/15
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
