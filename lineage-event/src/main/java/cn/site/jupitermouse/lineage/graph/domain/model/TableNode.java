package cn.site.jupitermouse.lineage.graph.domain.model;

import java.util.Optional;

import cn.site.jupitermouse.lineage.common.util.StringPool;
import cn.site.jupitermouse.lineage.graph.contants.NeoConstant;
import cn.site.jupitermouse.lineage.graph.domain.NodeQualifiedName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * <p>
 * Table Node
 * </p>
 *
 * @author isaac 2020/09/28
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@Data
@NodeEntity(NeoConstant.Type.NODE_TABLE)
public class TableNode extends BaseNodeEntity {

    private String tableName;

    //===============================================================================
    //  Other information fields
    //===============================================================================

    /**
     * 创表语句
     */
    private String sql;
    /**
     * create table table1 as select * from table2
     * createTableFrom: table2
     */
    private String createTableFrom;
    /**
     * insert overwrite table1 select * from table2
     * insertOverwriteFrom: table2
     */
    private String insertOverwriteFrom;
    private String insertOverwriteSql;

    public TableNode(String platformName, String clusterName,
                     String catalogName, String originSchemaName,
                     String tableName) {
        Optional.ofNullable(platformName).ifPresent(this::setPlatformName);
        Optional.ofNullable(clusterName).ifPresent(this::setClusterName);
        Optional.ofNullable(catalogName).ifPresent(this::setCatalogName);
        Optional.ofNullable(originSchemaName).ifPresent(this::setOriginSchemaName);
        Optional.ofNullable(tableName).ifPresent(this::setTableName);
        Optional.ofNullable(this.getTableName()).ifPresent(this::setName);
        if (StringUtils.isNotEmpty(this.getCatalogName())) {
            this.setSchemaName(this.getCatalogName() + StringPool.DOT + this.getOriginSchemaName());
        } else {
            this.setSchemaName(originSchemaName);
        }
        // platfrom/cluster/[catalog.]schema/table
        String pk =
                NodeQualifiedName.ofTable(this.getPlatformName(), this.getClusterName(), this.getSchemaName(),
                        this.getTableName()).toString();
        this.setPk(pk);
    }

    public TableNode(String platformName, String clusterName, String originSchemaName, String tableName) {
        this(platformName, clusterName, null, originSchemaName, tableName);
    }


}

