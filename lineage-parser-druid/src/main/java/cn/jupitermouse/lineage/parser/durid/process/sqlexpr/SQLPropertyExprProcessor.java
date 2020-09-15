package cn.jupitermouse.lineage.parser.durid.process.sqlexpr;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = SQLPropertyExpr.class)
public class SQLPropertyExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) expr;
        content.addItem(SqlExprContent.builder()
                .name(sqlPropertyExpr.getName())
                .owner(sqlPropertyExpr.getOwnernName())
                .build());
    }
}
