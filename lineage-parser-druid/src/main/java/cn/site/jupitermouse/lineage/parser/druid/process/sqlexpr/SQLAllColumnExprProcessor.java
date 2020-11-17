package cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;

/**
 * <p>
 * select * 的处理
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
@SQLObjectType(clazz = SQLAllColumnExpr.class)
public class SQLAllColumnExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        // 需后置处理，节点处理时由下至上
        // *  select *
        content.addItem(SqlExprContent.builder()
                .name("*")
                .build());
        // select a.*, b*. 识别为 SQLIdentifierExpr
    }

}
