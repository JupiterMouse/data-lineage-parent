package cn.jupitermouse.lineage.parser.durid.process.sqlexpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.durid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import org.apache.commons.collections.CollectionUtils;

/**
 * <p>
 * SQLMethodInvokeExpr
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@SQLObjectType(clazz = SQLMethodInvokeExpr.class)
public class SQLMethodInvokeExprProcessor implements SQLExprProcessor {

    @Override
    public void process(String dbType, SQLExpr expr, SqlExprContent content) {
        SQLMethodInvokeExpr sqlMethodInvokeExpr = (SQLMethodInvokeExpr) expr;
        this.getAllCaseExprChild(sqlMethodInvokeExpr)
                .forEach(ep -> ProcessorRegister.getSQLExprProcessor(ep).process(dbType, ep, content));
    }

    private List<SQLExpr> getAllCaseExprChild(SQLMethodInvokeExpr expr) {
        List<SQLExpr> list = new ArrayList<>();
        Optional.ofNullable(expr.getOwner()).ifPresent(list::add);
        Optional.ofNullable(expr.getFrom()).ifPresent(list::add);
        Optional.ofNullable(expr.getUsing()).ifPresent(list::add);
        Optional.ofNullable(expr.getFor()).ifPresent(list::add);
        if (CollectionUtils.isNotEmpty(expr.getArguments())) {
            list.addAll(expr.getArguments());
        }
        return list;
    }
}
