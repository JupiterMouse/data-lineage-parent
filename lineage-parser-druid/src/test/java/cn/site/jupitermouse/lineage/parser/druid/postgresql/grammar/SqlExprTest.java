package cn.site.jupitermouse.lineage.parser.druid.postgresql.grammar;

import cn.site.jupitermouse.lineage.parser.druid.postgresql.utils.SqlConstants;
import cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr.*;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * <p>
 * SQLAllColumnExpr
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public class SqlExprTest {

    /**
     * @see SQLCaseExprProcessor
     */
    @Test
    public void testCaseWhen() {
        SQLExpr sqlExpr = SQLUtils.toSQLExpr(SqlConstants.SQL_EXPR_PG_CASE_WHEN, JdbcConstants.POSTGRESQL);
        Assert.notNull(sqlExpr, "sqlExpr can't null");
    }

    /**
     * @see SQLBinaryOpExprProcessor
     */
    @Test
    public void testSQLBinaryOpExpr() {
        SQLExpr sqlExpr = SQLUtils.toSQLExpr("select ((a1+a2)-a3)*a4/a5 as a", JdbcConstants.POSTGRESQL);
        Assert.notNull(sqlExpr, "sqlExpr can't null");
    }

    /**
     * @see PGTypeCastExprProcessor
     */
    @Test
    public void testSQLCastOpExpr() {
        // PGTypeCastExpr
        SQLExpr sqlExpr = SQLUtils.toSQLExpr("select col::text as c", JdbcConstants.POSTGRESQL);
        Assert.notNull(sqlExpr, "sqlExpr can't null");
    }

    /**
     * @see SQLMethodInvokeExprProcessor
     */
    @Test
    public void testSQLMethodInvokeExpr() {
        // SQLMethodInvokeExpr
        SQLExpr sqlExpr = SQLUtils.toSQLExpr("select substring(a1,a2) as c", JdbcConstants.POSTGRESQL);
        Assert.notNull(sqlExpr, "sqlExpr can't null");
    }

    /**
     * @see SQLCharExprProcessor
     */
    @Test
    public void testCharExpr() {
        // SQLCharExpr
        SQLExpr sqlExpr = SQLUtils.toSQLExpr("select 'str1' + 'st2r' as c", JdbcConstants.POSTGRESQL);
        Assert.notNull(sqlExpr, "sqlExpr can't null");
    }

    /**
     * @see SQLIdentifierExprProcessor
     */
    @Test
    public void testSQLIdentifierExpr() {
        // SQLIdentifier
        SQLExpr sqlExpr = SQLUtils.toSQLExpr("select a1 as b", JdbcConstants.POSTGRESQL);
        Assert.notNull(sqlExpr, "sqlExpr can't null");
    }

    /**
     * @see SQLPropertyExprProcessor
     */
    @Test
    public void testSQLSQLPropertyExpr() {
        // SQLPropertyExpr
        SQLExpr sqlExpr = SQLUtils.toSQLExpr("select dwd.a1", JdbcConstants.POSTGRESQL);
        Assert.notNull(sqlExpr, "sqlExpr can't null");
    }

    /**
     * @see SQLAllColumnExprProcessor
     */
    @Test
    public void testSQLAllColumnExpr() {
        // SQLPropertyExpr
        // select a.*, b.* 不是
        // select * 才是
        SQLExpr sqlExpr = SQLUtils.toSQLExpr("select a.*", JdbcConstants.POSTGRESQL);
        Assert.notNull(sqlExpr, "sqlExpr can't null");
    }
}
