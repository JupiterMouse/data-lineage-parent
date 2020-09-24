package cn.jupitermouse.lineage.graph.domain.model;


import org.neo4j.ogm.annotation.Id;

/**
 * <p>
 * BaseNodeEntity
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public abstract class BaseNodeEntity extends BaseEntity {

    /**
     * 节点Id 组合索引
     */
    @Id
    private String graphId;

    /**
     * 展示的名称
     */
    private String name;

    //===============================================================================
    //  业务唯一性字段
    //===============================================================================
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 业务数据库代码
     */
    private String datasourceCode;

    /**
     * 集群代码
     */
    private String clusterName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    protected boolean valid() {
        return true;
    }

    protected String generateId() {
        return null;
    }


    protected String generateId(String clusterName) {
        return null;
    }

    protected String generateCluster() {
        return this.tenantId + "-" + this.datasourceCode;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }


    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }


    public String getDatasourceCode() {
        return datasourceCode;
    }

    public void setDatasourceCode(String datasourceCode) {
        this.datasourceCode = datasourceCode;
    }
}
