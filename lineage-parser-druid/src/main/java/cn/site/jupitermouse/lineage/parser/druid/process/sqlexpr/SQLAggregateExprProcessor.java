package cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;

/**
 * <p>
 * SQLAggregateExpr
 * use case:
 * max()
 * min()
 * ...
 * </p>
 *
 * @author JupiterMouse 2020/10/13
 * @since 1.0
 */
@SQLObjectType(clazz = SQLAggregateExpr.class)
public class SQLAggregateExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLAggregateExpr sqlAggregateExpr = (SQLAggregateExpr) expr;
        sqlAggregateExpr.getArguments()
                .forEach(sqlExpr -> ProcessorRegister.getSQLExprProcessor(sqlExpr.getClass())
                        .process(dbType, sqlExpr, content));
    }
}
