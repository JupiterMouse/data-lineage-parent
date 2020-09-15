package cn.jupitermouse.lineage.parser.durid.process.tablesource;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.durid.process.SqlExprContent;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
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
        // 建立中介节点 start
        TableNode proxyTable = TableNode.builder()
                .expression(SQLUtils.toSQLString(sqlTableSource))
                .alias(sqlTableSource.getAlias())
                .build();
        TreeNode<TableNode> proxyNode = TreeNode.of(proxyTable);
        parent.addChild(proxyNode);

        SQLExpr sqlExprTableSourceExpr = ((SQLExprTableSource) sqlTableSource).getExpr();
        SqlExprContent sqlExprContent = new SqlExprContent();
        ProcessorRegister.getSQLExprProcessor(sqlExprTableSourceExpr)
                .process(dbType, sqlExprTableSourceExpr, sqlExprContent);
        proxyTable.setName(sqlExprContent.getName());
        proxyTable.setSchemaName(sqlExprContent.getOwner());
    }
}
