package cn.site.jupitermouse.lineage.parser.druid.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 数据血缘解析时表节点
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableNode {

    /**
     * schema
     */
    private String schemaName;
    /**
     * 表名
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 是否为虚拟表
     */
    @Builder.Default
    private Boolean isVirtualTemp = false;

    /**
     * 特殊类型节点的处理
     */
    private String queryType;
    /**
     * 字段列表
     */
    private final List<ColumnNode> columns = new ArrayList<>();
    /**
     * 表达式
     */
    private String expression;
}
