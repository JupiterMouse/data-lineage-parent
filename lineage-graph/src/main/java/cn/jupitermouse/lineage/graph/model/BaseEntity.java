package cn.jupitermouse.lineage.graph.model;

import java.util.Date;
import java.util.Map;

import cn.jupitermouse.lineage.graph.model.convert.MapCompositeAttributeConverter;
import org.neo4j.ogm.annotation.typeconversion.Convert;

/**
 * <p>
 * BaseEntity
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public abstract class BaseEntity {

    /**
     * 图真实ID
     */
    private Long id;

    /**
     * 节点属性值
     */
//    @Properties(prefix = "", delimiter = "")
    @Convert(MapCompositeAttributeConverter.class)
    private Map<String, String> attrs;

    //    @Version
    private Long version;

    //===============================================================================
    //  变更字段
    //===============================================================================
    /**
     * 创建时间
     */
    private Date creationDate;
    /**
     * 更新时间
     */
    private Date lastUpdateDate;

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
