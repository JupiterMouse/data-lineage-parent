package cn.jupitermouse.lineage.parser.durid.process;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.annotation.AnnotationUtils;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.sqlexpr.SQLExprProcessor;
import cn.jupitermouse.lineage.parser.durid.process.sqlselectquery.SQLSelectQueryProcessor;
import cn.jupitermouse.lineage.parser.durid.process.statement.StatementProcessor;
import cn.jupitermouse.lineage.parser.durid.process.tablesource.TableSourceProcessor;
import cn.jupitermouse.lineage.parser.utils.ApplicationContextHelper;
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

    static {
        Collection<StatementProcessor> processors = ApplicationContextHelper.getContext()
                .getBeansOfType(StatementProcessor.class).values();
        processors.forEach(statementProcessor -> {
            SQLObjectType annotation = AnnotationUtils
                    .findAnnotation(statementProcessor.getClass(), SQLObjectType.class);
            if (annotation != null) {
                STATEMENT_PROCESSOR_MAP.put(annotation.clazz(), statementProcessor);
            }
        });

        Collection<SQLSelectQueryProcessor> sqlSelectProcessor = ApplicationContextHelper.getContext()
                .getBeansOfType(SQLSelectQueryProcessor.class).values();
        sqlSelectProcessor.forEach(processor -> {
            SQLObjectType annotation = AnnotationUtils
                    .findAnnotation(processor.getClass(), SQLObjectType.class);
            if (annotation != null) {
                SQL_SELECT_QUERY_PROCESSOR_MAP.put(annotation.clazz(), processor);
            }
        });

        Collection<TableSourceProcessor> tableSourceProcessors = ApplicationContextHelper.getContext()
                .getBeansOfType(TableSourceProcessor.class).values();
        tableSourceProcessors.forEach(processor -> {
            SQLObjectType annotation = AnnotationUtils
                    .findAnnotation(processor.getClass(), SQLObjectType.class);
            if (annotation != null) {
                TABLE_SOURCE_PROCESSOR_MAP.put(annotation.clazz(), processor);
            }
        });

        Collection<SQLExprProcessor> sqlExprProcessors = ApplicationContextHelper.getContext()
                .getBeansOfType(SQLExprProcessor.class).values();
        sqlExprProcessors.forEach(processor -> {
            SQLObjectType annotation = AnnotationUtils
                    .findAnnotation(processor.getClass(), SQLObjectType.class);
            if (annotation != null) {
                TABLE_SQL_EXPR_MAP.put(annotation.clazz(), processor);
            }
        });
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

    public static SQLExprProcessor getSQLExprProcessor(SQLExpr expr) {
        Class<? extends SQLExpr> clazz = expr.getClass();
        SQLExprProcessor sqlExprProcessor = TABLE_SQL_EXPR_MAP.get(clazz);
        if (Objects.isNull(sqlExprProcessor)) {
            throw new UnsupportedOperationException(clazz.getName());
        }
        return sqlExprProcessor;
    }

}
