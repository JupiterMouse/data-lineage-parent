package cn.site.jupitermouse.lineage.parser.druid.analyse;

import java.util.List;

import cn.site.jupitermouse.lineage.parser.druid.model.TreeNode;
import cn.site.jupitermouse.lineage.parser.druid.model.ColumnNode;
import cn.site.jupitermouse.lineage.parser.druid.model.TableNode;
import lombok.Data;

/**
 * <p>
 * SQL 处理请求信息
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
@Data
public class SqlResponseContext {
    /**
     * 语句类型 INSERT ..., CREATE VIEW AS... , ...
     */
    private String statementType;
    /**
     * 引擎处理的类型
     */
    private String engineProcessingType;
    /**
     * 表血缘解析结果
     */
    private TreeNode<TableNode> lineageTableTree;
    /**
     * 字段血缘解析结果
     */
    private List<TreeNode<ColumnNode>> lineageColumnTreeList;

}
