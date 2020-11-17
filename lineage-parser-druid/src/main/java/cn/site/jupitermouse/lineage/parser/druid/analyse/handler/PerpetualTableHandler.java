package cn.site.jupitermouse.lineage.parser.druid.analyse.handler;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlResponseContext;
import cn.site.jupitermouse.lineage.parser.druid.exception.ParserException;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import cn.site.jupitermouse.lineage.parser.druid.constant.PriorityConstants;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 表血缘的处理 必须, order 为first
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
@Order(PriorityConstants.LITTLE_HIGH)
@Component
public class PerpetualTableHandler implements IHandler {

    @Override
    public void handleRequest(SqlRequestContext request, SqlResponseContext response) {
        verify(request);
        handleTableRelation(request, response);
    }

    public void handleTableRelation(SqlRequestContext sqlContext, SqlResponseContext response) {
        AtomicInteger sequence = new AtomicInteger();
        TreeNode<TableNode> root = new TreeNode<>();
        SQLStatement statement;
        try {
            statement = SQLUtils.parseSingleStatement(
                    sqlContext.getSql(),
                    sqlContext.getDbType().toLowerCase());
        } catch (Exception e) {
            throw new ParserException("statement.parser.err", e);
        }
        response.setStatementType(statement.getDbType().getClass().getSimpleName().toUpperCase());
        // 处理
        ProcessorRegister.getStatementProcessor(statement.getClass())
                .process(sqlContext.getDbType(), sequence, root, statement);
        // save
        response.setLineageTableTree(root);
    }

    private void verify(SqlRequestContext sqlContext) {
        if (Objects.isNull(sqlContext)) {
            throw new ParserException("sql.is.null");
        }
        if (StringUtils.isEmpty(sqlContext.getSql())) {
            throw new ParserException("sql.content.is.empty");
        }
        if (StringUtils.isEmpty(sqlContext.getDbType())) {
            throw new ParserException("sql.type.is.empty");
        }
    }

}
