package cn.jupitermouse.lineage.parser.durid.process.sqlexpr;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.durid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;

/**
 * <p>
 * SQLBinaryOpExpr
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = SQLBinaryOpExpr.class)
public class SQLBinaryOpExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) expr;
        ProcessorRegister.getSQLExprProcessor(sqlBinaryOpExpr.getLeft()).process(dbType, sqlBinaryOpExpr, content);
        ProcessorRegister.getSQLExprProcessor(sqlBinaryOpExpr.getRight()).process(dbType, sqlBinaryOpExpr, content);
    }
}
