package cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;

/**
 * <p>
 * 字段前缀解析
 * </p>
 * eg: <p>select dwd.a1</p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = SQLPropertyExpr.class)
public class SQLPropertyExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) expr;
        // 出口
        content.addItem(SqlExprContent.builder()
                .name(sqlPropertyExpr.getName())
                .owner(sqlPropertyExpr.getOwnernName())
                .build());
    }
}
