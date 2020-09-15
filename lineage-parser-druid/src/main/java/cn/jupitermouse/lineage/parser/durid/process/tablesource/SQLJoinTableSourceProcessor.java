package cn.jupitermouse.lineage.parser.durid.process.tablesource;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.contants.ParserConstant;
import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;

/**
 * <p>
 * SQLJoinTableSource
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
@SQLObjectType(clazz = SQLJoinTableSource.class)
public class SQLJoinTableSourceProcessor implements TableSourceProcessor {

    @Override
    public void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> parent,
            SQLTableSource sqlTableSource) {
        // 建立中介节点 start
        TableNode proxyTable = TableNode.builder()
                .isVirtualTemp(true)
                .expression(SQLUtils.toSQLString(sqlTableSource))
                .name(ParserConstant.TEMP_TABLE_PREFIX + sequence.incrementAndGet())
                .alias(sqlTableSource.getAlias())
                .build();

        TreeNode<TableNode> proxyNode = TreeNode.of(proxyTable);
        parent.addChild(proxyNode);

        SQLJoinTableSource sqlJoinTableSource = (SQLJoinTableSource) sqlTableSource;
        ProcessorRegister.getTableSourceProcessor(sqlJoinTableSource.getLeft().getClass())
                .process(dbType, sequence, proxyNode, sqlJoinTableSource.getLeft());
        ProcessorRegister.getTableSourceProcessor(sqlJoinTableSource.getRight().getClass())
                .process(dbType, sequence, proxyNode, sqlJoinTableSource.getRight());

    }

}
