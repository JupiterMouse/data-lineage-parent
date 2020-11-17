package cn.site.jupitermouse.lineage.parser.druid.process.tablesource;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;

/**
 * <p>
 * SQLExprTableSource
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
@SQLObjectType(clazz = SQLExprTableSource.class)
public class SQLExprTableSourceProcessor implements TableSourceProcessor {

    @Override
    public void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> parent, SQLTableSource sqlTableSource
    ) {
        // 建立TEMP节点 start
        TableNode proxyTable = TableNode.builder()
                .expression(SQLUtils.toSQLString(sqlTableSource))
                .alias(sqlTableSource.getAlias())
                .build();
        TreeNode<TableNode> proxyNode = TreeNode.of(proxyTable);
        parent.addChild(proxyNode);

        SQLExpr sqlExprTableSourceExpr = ((SQLExprTableSource) sqlTableSource).getExpr();
        SqlExprContent sqlExprContent = new SqlExprContent();
        ProcessorRegister.getSQLExprProcessor(sqlExprTableSourceExpr.getClass())
                .process(dbType, sqlExprTableSourceExpr, sqlExprContent);
        proxyTable.setName(sqlExprContent.getName());
        proxyTable.setSchemaName(sqlExprContent.getOwner());
    }
}
