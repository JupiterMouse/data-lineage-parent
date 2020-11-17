package cn.site.jupitermouse.lineage.parser.druid.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 数据血缘解析时字段节点
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnNode {

    /**
     * 列所属的表，考虑
     */
    private TableNode owner;
    /**
     * 表
     */
    private String tableName;
    /**
     * 名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 来源列
     */
    private final List<ColumnNode> sourceColumns = new ArrayList<>();
    /**
     * 此节点表达式
     */
    private String expression;

    /**
     * 字段所在的表树Id
     */
    private Long tableTreeNodeId;

    /**
     * 表的表达式
     */
    private String tableExpression;

    /**
     * 字段是否为常量
     */
    @Builder.Default
    private boolean isConstant = false;
}
