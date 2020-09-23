package cn.jupitermouse.lineage.graph.service;

import cn.jupitermouse.lineage.graph.model.BaseNodeEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public interface NodeService {

    //===============================================================================
    //  创建或更新节点
    //===============================================================================

    /**
     * 创建节点
     *
     * @param node Node
     */
    void createNode(BaseNodeEntity node);
}
