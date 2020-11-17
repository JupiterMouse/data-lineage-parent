package cn.site.jupitermouse.lineage.graph.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import cn.site.jupitermouse.lineage.graph.contants.NeoConstant;
import cn.site.jupitermouse.lineage.graph.util.LineageUtil;
import lombok.*;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Transient;

/**
 * <p>
 * 主要是处理多个节点之间的关系
 * Process节点主键及pk字段生成规则如下：
 * 示例：
 * sourceNodePkList：x,y
 * targetNodePk: z
 * md5(targetNodePk + sourceNodePkList排序后使用下划线'_'连接)
 * </p>
 *
 * @author isaac 2020/10/09 9:53
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@NodeEntity(NeoConstant.Type.NODE_PROCESS)
public class ProcessNode extends BaseNodeEntity {

    /**
     * 关系类型
     * default: 表关系  TABLE_PROCESS - TABLE
     * 字段关系 FIELD_PROCESS -FIELD
     */
    @Builder.Default
    private String processType = NeoConstant.ProcessType.TABLE_PROCESS;
    /**
     * 来源类型 hive|datax|sqoop|sql
     */
    private String type;
    /**
     * 处理事件详情，如具体sql语句，create table xxx
     */
    private String event;
    /**
     * 存储Node.pk
     * 示例：
     * create table A as select B.col1, C.col2 from B,C where xxx
     * sourceNode: B, C
     * targetNode: A
     */
    @Transient
    private List<String> sourceNodePkList;
    @Transient
    private String targetNodePk;

    @Property(NeoConstant.ProcessType.PROPERTY_DISPATCH_JOB)
    private String job;

    public ProcessNode(String processType, List<String> sourceNodePkList, String targetNodePk) {
        Optional.ofNullable(processType).ifPresent(this::setProcessType);
        this.setSourceNodePkList(sourceNodePkList);
        this.setTargetNodePk(targetNodePk);
        this.setPk(LineageUtil.genProcessNodePk(this));
    }

    public ProcessNode(String processType, String sourceNodePk, String targetNodePk) {
        Optional.ofNullable(processType).ifPresent(this::setProcessType);
        this.setSourceNodePkList(Collections.singletonList(sourceNodePk));
        this.setTargetNodePk(targetNodePk);
        this.setPk(LineageUtil.genProcessNodePk(this));
    }
}
