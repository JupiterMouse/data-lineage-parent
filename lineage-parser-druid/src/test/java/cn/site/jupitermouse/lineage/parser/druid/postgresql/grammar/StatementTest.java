package cn.site.jupitermouse.lineage.parser.druid.postgresql.grammar;

import cn.site.jupitermouse.lineage.parser.druid.process.statement.PGInsertStatementProcessor;
import cn.site.jupitermouse.lineage.parser.druid.process.statement.SQLCreateViewStatementProcessor;
import cn.site.jupitermouse.lineage.parser.druid.postgresql.utils.SqlConstants;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Statement
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public class StatementTest {

    /**
     * PGInsertStatement
     *
     * @see PGInsertStatementProcessor
     */
    @Test
    public void testPgInsertStatement() {
        SQLStatement statement = SQLUtils.parseSingleStatement(
                SqlConstants.STATEMENT_PG_INSERT, JdbcConstants.POSTGRESQL);
        Assert.assertNotNull("statement can't null", statement);
        Assert.assertEquals("statement is PGInsertStatement",
                PGInsertStatement.class, statement.getClass());
    }

    /**
     * SQLCreateViewStatement
     *
     * @see SQLCreateViewStatementProcessor
     */
    @Test
    public void testCreateView() {
        SQLStatement statement = SQLUtils.parseSingleStatement(
                SqlConstants.STATEMENT_CREATE_VIEW, JdbcConstants.POSTGRESQL);
        Assert.assertEquals("statement is createViewStatement",
                SQLCreateViewStatement.class, statement.getClass());
    }

}
