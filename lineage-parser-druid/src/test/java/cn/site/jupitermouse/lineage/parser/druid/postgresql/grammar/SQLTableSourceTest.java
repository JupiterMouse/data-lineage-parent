package cn.site.jupitermouse.lineage.parser.druid.postgresql.grammar;

import cn.site.jupitermouse.lineage.parser.druid.process.tablesource.SQLExprTableSourceProcessor;
import cn.site.jupitermouse.lineage.parser.druid.process.tablesource.SQLUnionQueryTableSourceProcessor;
import cn.site.jupitermouse.lineage.parser.druid.postgresql.utils.SqlConstants;
import cn.site.jupitermouse.lineage.parser.druid.process.tablesource.SQLJoinTableSourceProcessor;
import cn.site.jupitermouse.lineage.parser.druid.process.tablesource.SQLSubqueryTableSourceProcessor;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * SQLTableSource
 *
 * @author JupiterMouse 2020/10/15
 * @see SQLTableSource
 * </p>
 * @since 1.0
 */
public class SQLTableSourceTest {

    /**
     * SQLJoinTableSource
     * 多表关联提取
     *
     * @see SQLJoinTableSourceProcessor
     */
    @Test
    public void testSQLJoinTableSource() {
        SQLStatement statement = SQLUtils.parseSingleStatement(
                SqlConstants.TABLE_SQL_JOIN_TABLE_SOURCE,
                JdbcConstants.POSTGRESQL);
        org.springframework.util.Assert.notNull(statement, "sqlExpr can't null");
        Assert.assertEquals("from is SQLJoinTableSource", SQLJoinTableSource.class,
                ((PGSelectQueryBlock) (((PGSelectStatement) statement).getSelect()).getQuery()).getFrom().getClass());

        SQLStatement statement2 = SQLUtils.parseSingleStatement(
                SqlConstants.TABLE_SQL_JOIN_TABLE_SOURCE2,
                JdbcConstants.POSTGRESQL);
        Assert.assertNotNull("sqlExpr can't null", statement2);
        Assert.assertEquals("from is SQLJoinTableSource", SQLJoinTableSource.class,
                ((PGSelectQueryBlock) (((PGSelectStatement) statement2).getSelect()).getQuery()).getFrom()
                        .getClass());
    }

    /**
     * SQLExprTableSource
     *
     * @see SQLExprTableSourceProcessor
     */
    @Test
    public void testSQLExprTableSource() {
        SQLStatement statement = SQLUtils.parseSingleStatement("select t1.a1 from table1 t1",
                JdbcConstants.POSTGRESQL);
        Assert.assertNotNull("sqlExpr can't null", statement);
        Assert.assertEquals("from is SQLExprTableSource", SQLExprTableSource.class,
                ((PGSelectQueryBlock) ((SQLSelect) ((PGSelectStatement) statement).getSelect()).getQuery()).getFrom()
                        .getClass());
    }


    /**
     * 解析子查询SQL
     *
     * @see SQLSubqueryTableSourceProcessor
     */
    @Test
    public void testSQLSubqueryTableSource() {
        SQLStatement statement = SQLUtils.parseSingleStatement(
                "select t1.a1 from (select a1 from tablex) t1",
                JdbcConstants.POSTGRESQL);
        Assert.assertNotNull("sqlExpr can't null", statement);
        Assert.assertEquals("from is SQLSubqueryTableSource",
                SQLSubqueryTableSource.class,
                ((PGSelectQueryBlock) ((SQLSelect) ((PGSelectStatement) statement).getSelect()).getQuery()).getFrom()
                        .getClass());
    }

    /**
     * 解析union
     *
     * @see SQLUnionQueryTableSourceProcessor
     */
    @Test
    public void testUnionTableSource() {
        SQLStatement statement = SQLUtils.parseSingleStatement(
                SqlConstants.TABLE_SQL_UNION_QUERY_TABLE_SOURCE,
                JdbcConstants.POSTGRESQL);
        Assert.assertNotNull("sqlExpr can't null", statement);
        Assert.assertEquals("from is SQLUnionQueryTableSource",
                SQLUnionQueryTableSource.class,
                ((PGSelectQueryBlock) ((SQLSelect) ((PGSelectStatement) statement).getSelect()).getQuery()).getFrom()
                        .getClass());
    }


}
