package cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;

/**
 * <p>
 * 类型转换
 * eg: <p>select col::text as c</p>
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
        ProcessorRegister.getSQLExprProcessor(castExprExpr.getClass()).process(dbType, castExprExpr, content);

    }
}
