package cn.site.jupitermouse.lineage.graph.domain.model;

import static cn.site.jupitermouse.lineage.graph.contants.NeoConstant.Node.DEFAULT_CLUSTER;
import static cn.site.jupitermouse.lineage.graph.contants.NeoConstant.Node.DEFAULT_PLATFORM;

import java.time.LocalDateTime;

import cn.site.jupitermouse.lineage.graph.contants.NodeStatus;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.Transient;

/**
 * <p>
 * Node attribute abstraction
 * </p>
 *
 * @author JupiterMouse 2020/09/27
 * @since 1.0
 */
@Setter
@Getter
public abstract class BaseNodeEntity extends BaseEntity {

    @Id
    @Index(unique = true)
    private String pk;

    private String status = NodeStatus.ACTIVE.name();
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime = LocalDateTime.now();

    /**
     * Optimistic lock field
     */
//    @Version
    private Long version;
    /**
     * 图的展示名称
     */
    private String name;

    //===============================================================================
    //  redundant field
    //===============================================================================

    /**
     * 区分平台使用
     */
    private String platformName = DEFAULT_PLATFORM;
    private Long tenantId;
    private String datasourceCode;
    /**
     * 集群名称：
     * 默认取传入的clusterName
     * 特别的clusterName取不到，默认取租户 tenantId_datasourceCode填充
     */
    private String clusterName = DEFAULT_CLUSTER;

    /**
     * 对于schmea型数据库，取originSchemaName
     * 特别的，对于catalog型数据库，取 catalogName.originSchemaName
     */
    private String schemaName;

    /**
     * 接收原始的catalog
     */
    @Transient
    private String catalogName;
    /**
     * 接收原始的schemaName
     */
    @Transient
    private String originSchemaName;
}
