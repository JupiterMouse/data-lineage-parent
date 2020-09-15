package cn.jupitermouse.lineage.parser.durid.process.tablesource;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;

/**
 * <p>
 * SQLSubqueryTableSource
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
@SQLObjectType(clazz = SQLSubqueryTableSource.class)
public class SQLSubqueryTableSourceProcessor implements TableSourceProcessor {

    @Override
    public void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> parent,
            SQLTableSource sqlTableSource) {
        SQLSelectQuery sqlSelectQuery = ((SQLSubqueryTableSource) sqlTableSource).getSelect().getQuery();
        ProcessorRegister.getSQLSelectQueryProcessor(sqlSelectQuery.getClass())
                .process(dbType, sequence, parent, sqlSelectQuery);
    }

}
