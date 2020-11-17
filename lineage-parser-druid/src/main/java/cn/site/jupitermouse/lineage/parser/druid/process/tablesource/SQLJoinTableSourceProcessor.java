package cn.site.jupitermouse.lineage.parser.druid.process.tablesource;

import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.contants.ParserConstant;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;

/**
 * <p>
 * SQLJoinTableSource
 * </p>
 * eg: <p>
 * table1
 * LEFT JOIN
 * (SELECT t2.a1, t2.a2 FROM table2 t2) temp1
 * ON
 * t1.a1 = temp1.a1
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
