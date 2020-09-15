package cn.jupitermouse.lineage.parser.durid.process.sqlexpr;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.durid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/14
 * @since 1.0
 */
@SQLObjectType(clazz = SQLCastExpr.class)
public class SQLCastExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLCastExpr sqlCastExpr = (SQLCastExpr) expr;
        SQLExpr castExprExpr = sqlCastExpr.getExpr();
        ProcessorRegister.getSQLExprProcessor(castExprExpr).process(dbType, castExprExpr, content);

    }
}
