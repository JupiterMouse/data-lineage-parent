package cn.site.jupitermouse.lineage.parser.druid.process;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.site.jupitermouse.lineage.parser.druid.process.sqlexpr.SQLExprProcessor;
import cn.site.jupitermouse.lineage.parser.druid.process.statement.StatementProcessor;
import cn.site.jupitermouse.lineage.parser.druid.process.sqlselectquery.SQLSelectQueryProcessor;
import cn.site.jupitermouse.lineage.parser.druid.process.tablesource.TableSourceProcessor;
import com.alibaba.druid.sql.ast.SQLExpr;

/**
 * <p>
 * Processor注册器 按类型处理
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public class ProcessorRegister {

    /**
     * SQLStatement 处理器
     */
    private static final Map<Type, StatementProcessor> STATEMENT_PROCESSOR_MAP = new HashMap<>();

    /**
     * SQLSelectQuery 处理器
     */
    private static final Map<Type, SQLSelectQueryProcessor> SQL_SELECT_QUERY_PROCESSOR_MAP = new HashMap<>();

    /**
     * SQLTableSource 处理器
     */
    private static final Map<Type, TableSourceProcessor> TABLE_SOURCE_PROCESSOR_MAP = new HashMap<>();

    /**
     * SQLTableSource 处理器
     */
    private static final Map<Type, SQLExprProcessor> TABLE_SQL_EXPR_MAP = new HashMap<>();

    private ProcessorRegister() {

    }

    public static void register(Class<?> clazz, Object bean) {
        if (bean instanceof StatementProcessor) {
            STATEMENT_PROCESSOR_MAP.put(clazz, (StatementProcessor) bean);
        } else if (bean instanceof SQLSelectQueryProcessor) {
            SQL_SELECT_QUERY_PROCESSOR_MAP.put(clazz, (SQLSelectQueryProcessor) bean);
        } else if (bean instanceof TableSourceProcessor) {
            TABLE_SOURCE_PROCESSOR_MAP.put(clazz, (TableSourceProcessor) bean);
        } else if (bean instanceof SQLExprProcessor) {
            TABLE_SQL_EXPR_MAP.put(clazz, (SQLExprProcessor) bean);
        }
    }


    public static StatementProcessor getStatementProcessor(Type clazz) {
        StatementProcessor statementProcessor = STATEMENT_PROCESSOR_MAP.get(clazz);
        if (Objects.isNull(statementProcessor)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return statementProcessor;
    }

    public static SQLSelectQueryProcessor getSQLSelectQueryProcessor(Type clazz) {
        SQLSelectQueryProcessor sqlSelectQueryProcessor = SQL_SELECT_QUERY_PROCESSOR_MAP.get(clazz);
        if (Objects.isNull(sqlSelectQueryProcessor)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return sqlSelectQueryProcessor;
    }

    public static TableSourceProcessor getTableSourceProcessor(Type clazz) {
        TableSourceProcessor tableSourceProcessor = TABLE_SOURCE_PROCESSOR_MAP.get(clazz);
        if (Objects.isNull(tableSourceProcessor)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return tableSourceProcessor;
    }

    public static SQLExprProcessor getSQLExprProcessor(Type clazz) {
        SQLExprProcessor sqlExprProcessor = TABLE_SQL_EXPR_MAP.get(clazz);
        if (Objects.isNull(sqlExprProcessor)) {
            throw new UnsupportedOperationException(clazz.getTypeName());
        }
        return sqlExprProcessor;
    }

    public static SQLExprProcessor getSQLSelectQueryProcessor(SQLExpr expr) {
        Class<? extends SQLExpr> clazz = expr.getClass();
        SQLExprProcessor sqlExprProcessor = TABLE_SQL_EXPR_MAP.get(clazz);
        if (Objects.isNull(sqlExprProcessor)) {
            throw new UnsupportedOperationException(clazz.getName());
        }
        return sqlExprProcessor;
    }

}
