package cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;

/**
 * <p>
 * SQLCharExpr
 * eg: <p>select 'str1' + 'st2r' as c</p>
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = SQLCharExpr.class)
public class SQLCharExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        // SQLCharExpr
        SQLCharExpr sqlCharExpr = (SQLCharExpr) expr;
        // 出口
//        content.addItem(SqlExprContent
//                .builder()
//                .name(sqlCharExpr.getText())
//                .isConstant(true)
//                .build());
        // TODO 常量解析待开发
    }
}
