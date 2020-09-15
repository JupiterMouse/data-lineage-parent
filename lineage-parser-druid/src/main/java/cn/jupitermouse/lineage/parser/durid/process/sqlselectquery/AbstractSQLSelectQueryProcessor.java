package cn.jupitermouse.lineage.parser.durid.process.sqlselectquery;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;

/**
 * <p>
 * SQLSelectQuery
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public abstract class AbstractSQLSelectQueryProcessor implements SQLSelectQueryProcessor {


    @Override
    public void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> parent,
            SQLSelectQuery sqlSelectQuery) {

    }

    /**
     * SQLSelectStatement 考虑中 SQLCreateTableStatement 考虑中 SQLSubqueryTableSource V SQLObject Contain SQLSelect 时, 合并
     * SQLSubqueryTableSource 中的
     *
     * @param sqlObject sqlObject
     * @return alias
     */
    protected String getSubqueryTableSourceAlias(SQLObject sqlObject) {
        SQLObject parentObject = sqlObject.getParent().getParent();
        if (sqlObject.getParent() == null || parentObject == null) {
            return null;
        }
        if (parentObject instanceof SQLSubqueryTableSource) {
            SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource) parentObject;
            return sqlSubqueryTableSource.getAlias();
        } else if (parentObject instanceof SQLSelectStatement || parentObject instanceof SQLCreateTableStatement) {
            throw new UnsupportedOperationException(parentObject.getClass().getName());
        } else {
            return null;
        }
    }
}
