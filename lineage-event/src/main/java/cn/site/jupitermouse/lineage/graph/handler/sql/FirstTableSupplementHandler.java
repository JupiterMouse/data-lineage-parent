package cn.site.jupitermouse.lineage.graph.handler.sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.site.jupitermouse.lineage.common.exception.CommonException;
import cn.site.jupitermouse.lineage.graph.domain.model.FieldNode;
import cn.site.jupitermouse.lineage.graph.metadata.MetaDataService;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlResponseContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.handler.IHandler;
import cn.site.jupitermouse.lineage.parser.druid.constant.PriorityConstants;
import cn.site.jupitermouse.lineage.parser.druid.model.ColumnNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 补全首节点column信息，在解析时针对字段不包含列名的情况
 * 1. create view xxx as select
 * 2. insert overwrite into ...
 * 3 ....
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
@Order(PriorityConstants.LITTLE_HIGH + 1)
@Component
public class FirstTableSupplementHandler implements IHandler {

    private final MetaDataService metaDataService;

    public FirstTableSupplementHandler(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    @Override
    public void handleRequest(SqlRequestContext request, SqlResponseContext response) {
        if (whetherHandle(response)) {
            TreeNode<TableNode> root = response.getLineageTableTree();
            TableNode tableNode = root.getValue();
            String targetTableName = tableNode.getName();
            String targetTableSchema = Optional.ofNullable(tableNode.getSchemaName())
                    .orElse(request.getSchemaName());
            List<FieldNode> fieldNodeList = metaDataService
                    .queryField(
                            request,
                            targetTableSchema,
                            targetTableName
                    );
            List<ColumnNode> columnNodeList = fieldNodeList.stream()
                    .map(this::convert2ColumnNode)
                    .collect(Collectors.toList());
            List<ColumnNode> childColumnList = this.findFirstHaveColumnTableNode(root).getValue().getColumns();
            if (columnNodeList.size() != childColumnList.size()) {
                throw new CommonException("column.map.error");
            }
            for (int i = 0; i < childColumnList.size(); i++) {
                columnNodeList.get(i).setOwner(tableNode);
                columnNodeList.get(i).getSourceColumns().add(childColumnList.get(i));
            }
            tableNode.getColumns().addAll(columnNodeList);
        }
    }

    boolean whetherHandle(SqlResponseContext response) {
        return CollectionUtils.isEmpty(response.getLineageTableTree().getValue().getColumns());
    }

    private ColumnNode convert2ColumnNode(FieldNode fieldNode) {
        ColumnNode columnNode = new ColumnNode();
        columnNode.setName(fieldNode.getFieldName());
        return columnNode;
    }

    /**
     * 找到第一个有字段的节点
     *
     * @param root TableNode
     * @return TreeNode<TableNode>
     */
    private TreeNode<TableNode> findFirstHaveColumnTableNode(TreeNode<TableNode> root) {
        if (!org.springframework.util.CollectionUtils.isEmpty(root.getValue().getColumns())) {
            return root;
        }
        if (org.springframework.util.CollectionUtils.isEmpty(root.getChildList()) || root.getChildList().size() != 1) {
            throw new CommonException("node.found.more");
        }
        // 第一个有字段的节点，其父级仅有一个子元素
        return root.getChildList().get(0);
    }

}
