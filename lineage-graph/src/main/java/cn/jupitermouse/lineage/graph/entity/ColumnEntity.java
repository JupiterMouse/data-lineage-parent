package cn.jupitermouse.lineage.graph.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 * <p>
 * 图中字段呈现。字段唯一性
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NodeEntity(label = "Column2")
public class ColumnEntity {

    /**
     * 字段的定位符号 /{业务代码}/{database}/{schema}/{table}/{column}
     */
    @Id
    private String id;

    /**
     * 字段名
     */
    @Property(name = "name")
    private String name;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 创建一条指向 DAG 节点的出边，边的类型是 COLUMN_FROM_COLUMN
     */
    @Relationship(type = "COLUMN_FROM_COLUMN", direction = Relationship.DIRECTION)
    private List<ColumnEntity> columnSource;

    /**
     * 创建一条指向 DAG 节点的出边，边的类型是 COLUMN_OF_TABLE
     */
    @Relationship(type = "COLUMN_OF_TABLE", direction = Relationship.DIRECTION)
    private TableEntity owner;
}
