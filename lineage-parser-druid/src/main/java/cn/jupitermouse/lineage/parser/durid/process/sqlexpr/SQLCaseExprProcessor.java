package cn.jupitermouse.lineage.parser.durid.process.sqlexpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.durid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;

/**
 * <p>
 * SQLCaseExpr
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = SQLCaseExpr.class)
public class SQLCaseExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLCaseExpr sqlCaseExpr = (SQLCaseExpr) expr;
        this.getAllCaseExprChild(sqlCaseExpr)
                .forEach(expr1 -> ProcessorRegister.getSQLExprProcessor(expr1).process(dbType, expr1, content));
    }

    private List<SQLExpr> getAllCaseExprChild(SQLCaseExpr sqlCaseExpr) {
        List<SQLExpr> list = new ArrayList<>();
        if (Objects.nonNull(sqlCaseExpr.getValueExpr())) {
            list.add(sqlCaseExpr.getValueExpr());
        }
        if (Objects.nonNull(sqlCaseExpr.getElseExpr())) {
            list.add(sqlCaseExpr.getElseExpr());
        }
        List<SQLExpr> sqlItemExprList = sqlCaseExpr.getItems()
                .stream()
                .map(SQLCaseExpr.Item::getValueExpr)
                .collect(Collectors.toList());
        list.addAll(sqlItemExprList);
        return list;
    }
}
