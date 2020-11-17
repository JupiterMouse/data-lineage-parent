package cn.site.jupitermouse.lineage.parser.druid.process.sqlselectquery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.contants.ParserConstant;
import cn.site.jupitermouse.lineage.parser.druid.model.ColumnNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.process.SqlExprContent;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import org.springframework.util.StringUtils;

/**
 * <p>
 * PGSelectQueryBlock
 * use case:
 * create view view_test as
 * select temp.a1,temp.a2 (
 * select a1,a2 from table1
 * ) temp
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
@SQLObjectType(clazz = PGSelectQueryBlock.class)
public class PGSelectQueryBlockProcessor extends AbstractSQLSelectQueryProcessor {

    @Override
    public void process(String dbType, AtomicInteger sequence, TreeNode<TableNode> parent,
                        SQLSelectQuery sqlSelectQuery) {
        PGSelectQueryBlock pgSelectQueryBlock = (PGSelectQueryBlock) sqlSelectQuery;
        // 建立表节点，并关系父级关系
        TableNode proxyTable = TableNode.builder()
                .isVirtualTemp(true)
                .expression(SQLUtils.toSQLString(pgSelectQueryBlock))
                .name(ParserConstant.TEMP_TABLE_PREFIX + sequence.incrementAndGet())
                .alias(this.getSubqueryTableSourceAlias(pgSelectQueryBlock))
                .build();
        TreeNode<TableNode> proxyNode = TreeNode.of(proxyTable);
        parent.addChild(proxyNode);
        // 生成字段
        List<ColumnNode> columnList = pgSelectQueryBlock.getSelectList()
                .stream()
                .map(sqlSelectItem -> this.convertSelectItem2Column(dbType, sqlSelectItem))
                .collect(Collectors.toList());
        // TODO 如果字段的表名没有, 考虑填充表名，以提高字段来源检索。
        // 表字段填充到表
        proxyTable.getColumns().addAll(columnList);
        // 继续向下处理
        ProcessorRegister
                .getTableSourceProcessor(pgSelectQueryBlock.getFrom().getClass())
                .process(dbType, sequence, proxyNode, pgSelectQueryBlock.getFrom());
    }

    /**
     * 构建字段，带来源字段
     *
     * @param dbType        数据库类型
     * @param sqlSelectItem SQLSelectItem
     * @return ColumnNode
     */
    private ColumnNode convertSelectItem2Column(String dbType, SQLSelectItem sqlSelectItem) {
        //      1. 如果字段由多字段构成
        //        a. 别名不为空
        //   ​	设置别名为第一层字段，来源字段为列表
        //        b. 别名为空
        //   ​	    // todo，现在考虑为多字段必须写上别名
        //        暂时考虑不为空
        //      2. 如果字段由单字段构成
        //        a. 别名为空。
        //   ​	取出字段名，取出表名。
        //        b. 别名不为空。
        //      3. 考虑来源字段为文本｜int 值
        //         // todo 现在考虑为字段为文本｜ int值时过滤掉
        //   ​	设置别名为第一层字段，来源字段为列表
        SQLExpr sqlExpr = sqlSelectItem.getExpr();
        SqlExprContent sqlExprContent = SqlExprContent.of();
        ProcessorRegister.getSQLExprProcessor(sqlExpr.getClass()).process(dbType, sqlExpr, sqlExprContent);
        String alias = sqlSelectItem.getAlias();
        if (sqlExprContent.isEmptyChildren()) {
            String name = sqlExprContent.getName();
            String ownerTable = sqlExprContent.getOwner();
            ColumnNode columnNode = ColumnNode.builder()
                    .name(name)
                    .tableName(ownerTable)
                    .build();
            if (!StringUtils.isEmpty(alias)) {
                columnNode.setAlias(alias);
            }
            return columnNode;
        }
        ColumnNode firstColumnNode = ColumnNode.builder().alias(alias).build();

        List<SqlExprContent> allItems = sqlExprContent.getAllItems();
        List<ColumnNode> sourceColumnNodeList = new ArrayList<>();
        allItems.forEach(item -> sourceColumnNodeList
                .add(ColumnNode.builder().name(item.getName()).tableName(item.getOwner()).build()));
        firstColumnNode.getSourceColumns().addAll(sourceColumnNodeList);

        return firstColumnNode;
    }

}
