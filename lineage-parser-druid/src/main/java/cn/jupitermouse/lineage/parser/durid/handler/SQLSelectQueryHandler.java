package cn.jupitermouse.lineage.parser.durid.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.jupitermouse.lineage.parser.exception.ParserException;
import cn.jupitermouse.lineage.parser.model.ColumnNode;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;

/**
 * <p>
 * SQLSelectQuery处理，可以看作是SQL树构建的入口
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public class SQLSelectQueryHandler {

    private static final String TEMP_TABLE_PREFIX = "SQL_RESULT_";

    public void processSQLSelectQuery(
            AtomicInteger sequence, TreeNode<TableNode> parent, SQLSelectQuery sqlSelectQuery, String dbType) {
        if (sqlSelectQuery instanceof PGSelectQueryBlock) {
            this.processPGSQLSelectQueryBlock(sequence, parent, (PGSelectQueryBlock) sqlSelectQuery, dbType);
        } else if (sqlSelectQuery instanceof SQLUnionQuery) {
            List<SQLSelectQuery> selectQueryList = ((SQLUnionQuery) sqlSelectQuery).getRelations();
            // 建立中介节点 start
            TreeNode<TableNode> proxyNode = new TreeNode<>();
            parent.addChild(proxyNode);
            TableNode proxyTable = new TableNode();
            proxyNode.setValue(proxyTable);
            proxyTable.setIsVirtualTemp(true);
            proxyTable.setExpression(SQLUtils.toSQLString(sqlSelectQuery));
            proxyTable.setName(TEMP_TABLE_PREFIX + sequence.incrementAndGet());
            proxyTable.setAlias(this.getSubqueryTableSourceAlias(sqlSelectQuery));

            // help SQLUnionQuery merge

            // end
            if (!CollectionUtils.isEmpty(selectQueryList)) {
                selectQueryList.forEach(item -> this.processSQLSelectQuery(sequence, proxyNode, item, dbType));
            }
        } else {
            throw new UnsupportedOperationException(sqlSelectQuery.getClass().getName());
        }
    }

    /**
     * SQLSelectStatement 考虑中 SQLCreateTableStatement 考虑中 SQLSubqueryTableSource V SQLObject Contain SQLSelect 时, 合并
     * SQLSubqueryTableSource 中的
     *
     * @param sqlObject sqlObject
     * @return alias
     */
    private String getSubqueryTableSourceAlias(SQLObject sqlObject) {
        SQLObject parentObject = sqlObject.getParent().getParent();
        if (sqlObject.getParent() == null || parentObject == null) {
            return null;
        }
        if (parentObject instanceof SQLSubqueryTableSource) {
            SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource) parentObject;
            return sqlSubqueryTableSource.getAlias();
        } else if (parentObject instanceof SQLSelectStatement || parentObject instanceof SQLCreateTableStatement) {
            throw new UnsupportedOperationException(parentObject.getClass().getName());
        } else {
            return null;
        }
    }

    private void processPGSQLSelectQueryBlock(AtomicInteger sequence, TreeNode<TableNode> parent,
            PGSelectQueryBlock pgSelectQueryBlock, String dbType) {
        // 建立表节点，并关系父级关系
        TreeNode<TableNode> proxyNode = new TreeNode<>();
        TableNode proxyTable = new TableNode();
        proxyNode.setValue(proxyTable);
        proxyTable.setIsVirtualTemp(true);
        proxyTable.setExpression(SQLUtils.toSQLString(pgSelectQueryBlock));
        proxyTable.setName(TEMP_TABLE_PREFIX + sequence.incrementAndGet());
        String currentTableAlias = this.getSubqueryTableSourceAlias(pgSelectQueryBlock);
        // 处理特殊情况的Alias
        proxyTable.setAlias(currentTableAlias);
        parent.addChild(proxyNode);

        // 生成字段
        List<ColumnNode> columnList = pgSelectQueryBlock.getSelectList()
                .stream()
                .map(this::transformSQLSelectItem2ColumnNode)
                .collect(Collectors.toList());
        // 如果字段的表名没有，那么自动填充表名。
        columnList.forEach(columnNode -> {
            if (CollectionUtils.isEmpty(columnNode.getSourceColumns())) {
                if (columnNode.getTableName() == null) {
                    columnNode.setTableName(currentTableAlias);
                }
            } else {
                columnNode.getSourceColumns().forEach(cn -> {
                    if (columnNode.getTableName() == null) {
                        columnNode.setTableName(currentTableAlias);
                    }
                });
            }
        });
        // 表字段填充到表
        proxyTable.getColumns().addAll(columnList);
        // 继续向下处理
        this.processTableSource(sequence, proxyNode, pgSelectQueryBlock.getFrom(), dbType);
    }

    /**
     * 构建字段，带来源字段
     *
     * @param sqlSelectItem SQLSelectItem
     * @return ColumnNode
     */
    private ColumnNode transformSQLSelectItem2ColumnNode(SQLSelectItem sqlSelectItem) {
        List<ColumnNode> sourceColumnList = new ArrayList<>();
        SQLExpr expr = sqlSelectItem.getExpr();
        // 封装来源字段
        this.extractColumnFromSQLExpr(expr, sourceColumnList);
        // 原始字段语句
        ColumnNode column = new ColumnNode();
        column.setAlias(sqlSelectItem.getAlias());
        String sqlString = SQLUtils.toSQLString(sqlSelectItem);
        column.setExpression(sqlString);
        if (expr instanceof SQLIdentifierExpr){
            SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) expr;
            Optional.ofNullable(identifierExpr.getName()).ifPresent(column::setName);
        }
        column.getSourceColumns().addAll(sourceColumnList);

        // 补数据，如果name 为空 alias 填上去， 如果alias为空express填上去
        if (StringUtils.isEmpty(column.getAlias())) {
            column.setAlias(sqlString);
        }
        if (StringUtils.isEmpty(column.getName())) {
            column.setName(column.getAlias());
        }
        return column;
    }

    /**
     * 递归从SQLExpr中提取字段
     *
     * @param expr       SQLExpr
     * @param columnList List<ColumnNode>
     */
    private void extractColumnFromSQLExpr(SQLExpr expr, List<ColumnNode> columnList) {
        if (expr instanceof SQLBinaryOpExpr) {
            this.extractColumnFromSQLExpr(((SQLBinaryOpExpr) expr).getLeft(), columnList);
            this.extractColumnFromSQLExpr(((SQLBinaryOpExpr) expr).getRight(), columnList);
        } else if (expr instanceof SQLAggregateExpr) {
            List<SQLExpr> arguments = ((SQLAggregateExpr) expr).getArguments();
            arguments.forEach(argument -> this.extractColumnFromSQLExpr(argument, columnList));
            // 只封装包含来源数据列的SQL
            // SQL 需要写清楚 schema hrp1000.stext (as xxx)
        } else if (expr instanceof SQLPropertyExpr) {
            ColumnNode column = new ColumnNode();
            SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) expr;
            column.setName(sqlPropertyExpr.getName());
            if (sqlPropertyExpr.getOwner() instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) sqlPropertyExpr.getOwner();
                column.setTableName(sqlIdentifierExpr.getName());
            }
            columnList.add(column);
        } else if (expr instanceof SQLIdentifierExpr) {
            // nothing to do
            //  认为无法近一步定位来源字段，暂不处理
        } else {
            throw new UnsupportedOperationException(expr.getClass().getName());
        }
    }

    private void processTableSource(AtomicInteger sequence, TreeNode<TableNode> parent, SQLTableSource sqlTableSource,
            String dbType) {
        if (sqlTableSource instanceof SQLJoinTableSource) {
            // 建立中介节点 start
            TableNode proxyTable = new TableNode();
            proxyTable.setIsVirtualTemp(true);
            proxyTable.setExpression(SQLUtils.toSQLString(sqlTableSource));
            proxyTable.setName(TEMP_TABLE_PREFIX + sequence.incrementAndGet());
            proxyTable.setAlias(sqlTableSource.getAlias());

            TreeNode<TableNode> proxyNode = new TreeNode<>();
            proxyNode.setValue(proxyTable);
            parent.addChild(proxyNode);

            SQLJoinTableSource sqlJoinTableSource = (SQLJoinTableSource) sqlTableSource;
            this.processTableSource(sequence, proxyNode, sqlJoinTableSource.getLeft(), dbType);
            this.processTableSource(sequence, proxyNode, sqlJoinTableSource.getRight(), dbType);
        } else if (sqlTableSource instanceof SQLSubqueryTableSource) {
            this.processSQLSelectQuery(sequence, parent,
                    ((SQLSubqueryTableSource) sqlTableSource).getSelect().getQuery(), dbType);
        } else if (sqlTableSource instanceof SQLExprTableSource) {
            // 建立中介节点 start
            TableNode proxyTable = new TableNode();
            proxyTable.setExpression(SQLUtils.toSQLString(sqlTableSource));
            proxyTable.setAlias(sqlTableSource.getAlias());

            TreeNode<TableNode> proxyNode = new TreeNode<>();
            proxyNode.setValue(proxyTable);
            parent.addChild(proxyNode);

            SQLExprTableSource sqlExprTableSource = (SQLExprTableSource) sqlTableSource;
            // schema table 提取
            if (sqlExprTableSource.getExpr() instanceof SQLPropertyExpr) {
                SQLPropertyExpr propertyExpr = (SQLPropertyExpr) sqlExprTableSource.getExpr();
                proxyTable.setName(propertyExpr.getName());
                proxyTable.setSchemaName(((SQLIdentifierExpr) propertyExpr.getOwner()).getName());
            } else if (sqlExprTableSource.getExpr() instanceof SQLIdentifierExpr) {
                proxyTable.setName(((SQLIdentifierExpr) sqlExprTableSource.getExpr()).getName());
            } else {
                throw new UnsupportedOperationException(sqlExprTableSource.getExpr().getClass().getName());
            }
        } else if (sqlTableSource instanceof SQLUnionQueryTableSource) {
            SQLUnionQueryTableSource sqlUnionQueryTableSource = (SQLUnionQueryTableSource) sqlTableSource;
            // union的特殊处理
            // 提取获取后面的union字段，
            //  提取为新的column 和 source
            // help SQLUnionQueryTableSource merge

            String alias = sqlUnionQueryTableSource.getAlias();
            if (StringUtils.isEmpty(alias)) {
                throw new ParserException("alias can't empty!");
            }
            List<SQLSelectQuery> relations = sqlUnionQueryTableSource.getUnion().getRelations();
            // 通过union子查询构建字段来源，解析一个子查询拿到字段即可，然后解析出对应的表
            TreeNode<TableNode> temp = new TreeNode<>();
            relations.forEach(
                    sqlSelectQuery -> this.processSQLSelectQuery(new AtomicInteger(0), temp, sqlSelectQuery, dbType));
            //  前提：第一层就能解析到真正的表
            List<TreeNode<TableNode>> childList = temp.getChildList();
            // 列名肯定是相同的，取第一个构建列名
            TreeNode<TableNode> lineageTableTreeNode = childList.get(0);
            // 构建union字段
            List<ColumnNode> unionColumnList = new ArrayList<>();
            lineageTableTreeNode.getValue().getColumns().forEach(lineageColumn -> {
                String columnName = Optional.ofNullable(lineageColumn.getAlias()).orElse(lineageColumn.getName());
                unionColumnList.add(ColumnNode.builder().name(columnName).tableName(alias).build());
            });

            // 构建union字段的来源字段
            for (TreeNode<TableNode> child : childList) {
                TableNode value = child.getValue();
                List<ColumnNode> columns = value.getColumns();
                String tableName = Optional.ofNullable(value.getAlias()).orElse(value.getName());
                for (int i = 0; i < columns.size(); i++) {
                    ColumnNode column = columns.get(i);
                    ColumnNode newColumn = ColumnNode.builder()
                            .alias(Optional.ofNullable(column.getAlias()).orElse(column.getName()))
                            .expression(column.getExpression())
                            .tableName(tableName).build();
                    unionColumnList.get(i).getSourceColumns().add(newColumn);
                }
            }
            TableNode lineageTable = new TableNode();
            lineageTable.setAlias(alias);
            lineageTable.getColumns().addAll(unionColumnList);
            lineageTable.setExpression(SQLUtils.toSQLString(sqlUnionQueryTableSource));
            TreeNode<TableNode> proxyNode = new TreeNode<>();
            proxyNode.setValue(lineageTable);
            parent.addChild(proxyNode);
            // 子查询继续递归
            relations.forEach(item -> this.processSQLSelectQuery(sequence, proxyNode, item, dbType));
        } else {
            throw new UnsupportedOperationException(sqlTableSource.getClass().getName());
        }
    }

    // help SQLUnionQueryTableSource merge

}
