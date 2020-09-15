package cn.jupitermouse.lineage.parser.durid.process.sqlselectquery;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.jupitermouse.lineage.parser.contants.ParserConstant;
import cn.jupitermouse.lineage.parser.durid.anotation.SQLObjectType;
import cn.jupitermouse.lineage.parser.durid.process.ProcessorRegister;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import org.apache.commons.collections.CollectionUtils;

/**
 * <p>
 * SQLUnionQuery 处理
 * </p>
 *
 * @author JupiterMouse 2020/09/10
 * @since 1.0
 */
@SQLObjectType(clazz = SQLUnionQuery.class)
public class SQLUnionQueryProcessor extends AbstractSQLSelectQueryProcessor {

    @Override
    public void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> parent,
            SQLSelectQuery sqlSelectQuery) {
        TableNode proxyTable = TableNode.builder()
                .isVirtualTemp(true)
                .expression(SQLUtils.toSQLString(sqlSelectQuery))
                .name(ParserConstant.TEMP_TABLE_PREFIX + sequence.incrementAndGet())
                .alias(this.getSubqueryTableSourceAlias(sqlSelectQuery))
                .build();
        TreeNode<TableNode> proxyNode = TreeNode.of(proxyTable);
        parent.addChild(proxyNode);

        // todo help SQLUnionQuery merge 考虑回溯时进行字段合并

        List<SQLSelectQuery> selectQueryList = ((SQLUnionQuery) sqlSelectQuery).getRelations();
        // end
        if (CollectionUtils.isNotEmpty(selectQueryList)) {
            selectQueryList.forEach(item ->
                    ProcessorRegister.getSQLSelectQueryProcessor(item.getClass())
                            .process(dbType, sequence, proxyNode, item)
            );
        }
    }
}
