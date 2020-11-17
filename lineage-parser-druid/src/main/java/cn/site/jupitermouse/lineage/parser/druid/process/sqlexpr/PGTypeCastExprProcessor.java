package cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGTypeCastExpr;

/**
 * <p>
 * PGTypeCastExpr
 * Use case:
 * CASE WHEN condition THEN result
 * [WHEN ...]
 * [ELSE result]
 * END
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
        ProcessorRegister.getSQLExprProcessor(pgTypeCastExprExpr.getClass())
                .process(dbType, pgTypeCastExprExpr, content);
    }
}
