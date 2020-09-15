package cn.jupitermouse.lineage.parser.durid.process.sqlexpr;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.durid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGTypeCastExpr;

/**
 * <p>
 * PGTypeCastExpr
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = PGTypeCastExpr.class)
public class PGTypeCastExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        PGTypeCastExpr pgTypeCastExpr = (PGTypeCastExpr) expr;
        SQLExpr pgTypeCastExprExpr = pgTypeCastExpr.getExpr();
        ProcessorRegister.getSQLExprProcessor(pgTypeCastExprExpr).process(dbType, pgTypeCastExprExpr, content);
    }
}
