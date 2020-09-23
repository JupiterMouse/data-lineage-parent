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
 * 表中字段呈现。 表唯一性id构造
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NodeEntity(label = "Table2")
public class TableEntity {

    /**
     * 表的定位符号 /{业务代码}/{database}/{schema}/{table}
     */
    @Id
    private String id;

    @Property(name = "name")
    private String name;

    /**
     * 创建一条指向 DAG 节点的出边，边的类型是 TABLE_FROM_TABLE
     */
    @Relationship(type = "TABLE_FROM_TABLE", direction = Relationship.DIRECTION)
    private List<TableEntity> fromTables;
}
