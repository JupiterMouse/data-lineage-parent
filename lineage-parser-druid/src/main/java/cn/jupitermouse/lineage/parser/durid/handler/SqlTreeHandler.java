package cn.jupitermouse.lineage.parser.durid.handler;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;

/**
 * <p>
 * 将执行的SQL转换为（Table, Column）树结构
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public class SqlTreeHandler {

    /**
     * 构建血缘树入口
     * @param sql
     * @param dbType
     * @return
     */
    public TreeNode<TableNode> constructLineageTree(String sql, String dbType) {
        // 生成初始序列
        AtomicInteger sequence = new AtomicInteger();
        // 构建根节点血缘树
        TreeNode<TableNode> root = new TreeNode<>();
        // 解析SQL后生成的statement
        SQLStatement statement = SQLUtils.parseSingleStatement(sql, dbType);
        // 查询树
        SQLSelectQuery sqlSelectQuery = null;
        // 下面针对不同的SQL进行不同的实现
        if (statement instanceof SQLCreateViewStatement) {
            SQLCreateViewStatement createViewStatement = (SQLCreateViewStatement) statement;
            SQLExprTableSource sqlExprTableSource = createViewStatement.getTableSource();
            // 构建根表
            this.constructRootNode(root, statement, sqlExprTableSource);
            // 获取SQLSelectQuery
            sqlSelectQuery = createViewStatement.getSubQuery().getQuery();
        } else if (statement instanceof PGInsertStatement) {
            PGInsertStatement pgInsertStatement = (PGInsertStatement) statement;
            SQLExprTableSource sqlExprTableSource = pgInsertStatement.getTableSource();
            // 构建根表
            this.constructRootNode(root, statement, sqlExprTableSource);
            // 获取SQLSelectQuery
            sqlSelectQuery = pgInsertStatement.getQuery().getQuery();
        } else {
            throw new UnsupportedOperationException(statement.getClass().getName());
        }
        // todo 处理
        new SQLSelectQueryHandler().processSQLSelectQuery(sequence, root, sqlSelectQuery, dbType);
        return root;
    }

    private void constructRootNode(TreeNode<TableNode> root, SQLStatement statement,
            SQLExprTableSource sqlExprTableSource) {
        SQLExpr sqlExpr = sqlExprTableSource.getExpr();
        if (sqlExpr instanceof SQLPropertyExpr) {
            SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) sqlExpr;
            //获取schema
            String schemaName = ((SQLIdentifierExpr) sqlPropertyExpr
                    .getOwner())
                    .getName();
            //获取表
            String tableName = sqlPropertyExpr.getName();
            TableNode tableNode = TableNode.builder()
                    .schemaName(schemaName)
                    .name(tableName)
                    .build();
            root.setValue(tableNode);

            tableNode.setExpression(SQLUtils.toSQLString(statement));
            tableNode.setIsVirtualTemp(false);
        } else {
            throw new UnsupportedOperationException(statement.getClass().getName());
        }
    }

}
