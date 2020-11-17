package cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr;

import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.ast.SQLExpr;

/**
 * <p>
 * SQLExpr 处理器
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
public interface SQLExprProcessor {

    /**
     * SQLExpr 内容提取
     *
     * @param dbType  数据库类型
     * @param expr    SQLExpr
     * @param content SqlExprContent
     */
    void process(String dbType, SQLExpr expr, SqlExprContent content);

}
