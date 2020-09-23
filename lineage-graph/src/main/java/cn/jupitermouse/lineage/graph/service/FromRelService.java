package cn.jupitermouse.lineage.graph.service;

import java.util.List;

import cn.jupitermouse.lineage.graph.model.BaseNodeEntity;
import cn.jupitermouse.lineage.graph.model.FieldEntity;
import cn.jupitermouse.lineage.graph.model.TableEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public interface FromRelService {

    //===============================================================================
    //  节点来源关系建立
    //===============================================================================

    /**
     * 创建关系节点 来源关系 table -> tables, field -> fields
     *
     * @param start 从节点列表  如List<Fields> -> Table
     * @param ends  目标节点 为大一级的节点
     */
    void createNodeFromRel(BaseNodeEntity start, List<BaseNodeEntity> ends);

    /**
     * 创建关系节点 来源关系 table -> tables
     *
     * @param table        字段
     * @param sourceTables 来源字段
     */
    void createTableFromTables(TableEntity table, List<TableEntity> sourceTables);

    /**
     * 创建关系节点 来源关系 field -> fields
     *
     * @param field        字段
     * @param sourceFields 来源字段
     */
    void createFieldFromFields(FieldEntity field, List<FieldEntity> sourceFields);
}
