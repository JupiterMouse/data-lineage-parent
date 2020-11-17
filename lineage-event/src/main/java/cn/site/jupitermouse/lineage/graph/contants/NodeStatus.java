package cn.site.jupitermouse.lineage.graph.contants;

/**
 * <p>
 * Status of the entity
 * can be active or deleted. Deleted entities are not removed from Neo4j store.
 * </p>
 *
 * @author isaac 2020/09/29 10:34
 * @since 1.0.0
 */
public enum NodeStatus {

    /**
     * ACTIVE
     */
    ACTIVE,
    DELETED
}
