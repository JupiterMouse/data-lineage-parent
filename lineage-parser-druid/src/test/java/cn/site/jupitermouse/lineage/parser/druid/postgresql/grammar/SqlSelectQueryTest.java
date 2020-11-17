package cn.site.jupitermouse.lineage.parser.druid.postgresql.grammar;

import cn.site.jupitermouse.lineage.parser.druid.process.sqlselectquery.PGSelectQueryBlockProcessor;
import cn.site.jupitermouse.lineage.parser.druid.process.sqlselectquery.SQLUnionQueryProcessor;
import cn.site.jupitermouse.lineage.parser.druid.postgresql.utils.SqlConstants;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * SqlSelectQuery
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public class SqlSelectQueryTest {

    /**
     * SQLUnionQuery
     *
     * @see SQLUnionQueryProcessor
     */
    @Test
    public void testSQLUnionQuery() {
        SQLStatement statement = SQLUtils.parseSingleStatement(
                SqlConstants.QUERY_PG_SQL_UNION_QUERY, JdbcConstants.POSTGRESQL);
        org.springframework.util.Assert.notNull(statement, "statement can't null");
        Assert.assertEquals("sql SubQuery need SQLUnionQuery", SQLUnionQuery.class, ((SQLCreateViewStatement) statement)
                .getSubQuery().getQuery().getClass());
    }

    /**
     * PGSelectQueryBlock
     *
     * @see PGSelectQueryBlockProcessor
     */
    @Test
    public void testPGSelectQueryBlock() {
        SQLStatement statement = SQLUtils.parseSingleStatement(
                SqlConstants.QUERY_PG_PG_SELECT_QUERY_BLOCK, JdbcConstants.POSTGRESQL);
        org.springframework.util.Assert.notNull(statement, "statement can't null");
        Assert.assertEquals("sql SubQuery need SQLUnionQuery", PGSelectQueryBlock.class, ((SQLCreateViewStatement) statement)
                .getSubQuery().getQuery().getClass());
    }

}
