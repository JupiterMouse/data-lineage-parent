package cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;

/**
 * <p>
 * SQLBinaryOpExpr
 * </p>
 * use case <p>select ((a1+a2)-a3)*a4/a5 as a</p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = SQLBinaryOpExpr.class)
public class SQLBinaryOpExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) expr;
        ProcessorRegister.getSQLExprProcessor(sqlBinaryOpExpr.getLeft().getClass()).process(dbType, sqlBinaryOpExpr.getLeft(), content);
        ProcessorRegister.getSQLExprProcessor(sqlBinaryOpExpr.getRight().getClass()).process(dbType, sqlBinaryOpExpr.getRight(), content);
    }
}
