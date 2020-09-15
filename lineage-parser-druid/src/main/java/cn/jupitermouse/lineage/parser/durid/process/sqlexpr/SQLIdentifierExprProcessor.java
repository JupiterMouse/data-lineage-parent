package cn.jupitermouse.lineage.parser.durid.process.sqlexpr;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;

/**
 * <p>
 * SQLIdentifierExpr 出口
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = SQLIdentifierExpr.class)
public class SQLIdentifierExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) expr;
        // 第一层 除了SQLIdentifierExpr 外，其它可看作是需要查找来源字段的
        content.addItem(SqlExprContent
                .builder()
                .name(sqlIdentifierExpr.getName())
                .build());
    }

}
