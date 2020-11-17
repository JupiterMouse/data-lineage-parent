package cn.site.jupitermouse.lineage.parser.druid.process.tablesource;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;

/**
 * <p>
 * SQLSubqueryTableSource
 * </p>
 * eg: <p>select t1.a1 from (select a1 from tablex) t1</p>
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
