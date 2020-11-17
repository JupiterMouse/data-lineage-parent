package cn.site.jupitermouse.lineage.parser.druid.process.statement;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * AbstractStatement
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Slf4j
public abstract class AbstractStatementProcessor implements StatementProcessor {

    @Override
    public void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> root, SQLStatement statement) {
        this.doProcess(dbType, sequence, root, statement);
        this.after(dbType, sequence, root, statement);
    }

    protected void doProcess(String dbType, AtomicInteger sequence, TreeNode<TableNode> root, SQLStatement statement) {
    }

    protected void constructRootNode(String dbType, TreeNode<TableNode> root, SQLStatement statement,
                                     SQLExprTableSource sqlExprTableSource) {
        SQLExpr sqlExpr = sqlExprTableSource.getExpr();
        SqlExprContent content = new SqlExprContent();
        ProcessorRegister.getSQLExprProcessor(sqlExpr.getClass()).process(dbType, sqlExpr, content);
        String tableName = content.getName();
        String schemaName = content.getOwner();
        TableNode tableNode = TableNode.builder()
                .schemaName(schemaName)
                .name(tableName)
                .isVirtualTemp(false)
                .build();
        root.setValue(tableNode);
        try {
            tableNode.setExpression(SQLUtils.toSQLString(statement));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }


    protected void after(String dbType, AtomicInteger sequence, TreeNode<TableNode> root, SQLStatement statement) {
        //  处理完之后的操作
        //  合并Union
    }
}
