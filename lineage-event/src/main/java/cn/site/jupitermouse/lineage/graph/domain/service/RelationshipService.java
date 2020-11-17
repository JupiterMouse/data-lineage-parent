package cn.site.jupitermouse.lineage.graph.domain.service;

import java.util.List;

/**
 * <p>
 * 关联创建
 * </p>
 *
 * @author JupiterMouse 2020/10/09
 * @since 1.0
 */
public interface RelationshipService {

    /**
     * 批量合并process 以多对一的方式合并去建立关系 TABLE|FIELD -(PROCESS_IN)> Process
     *
     * @param starts 开始节点的列表
     * @param end    结束节点
     */
    void mergeRelProcessInputs(List<String> starts, String end);
}
