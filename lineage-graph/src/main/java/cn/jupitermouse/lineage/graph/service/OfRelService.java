package cn.jupitermouse.lineage.graph.service;

import java.util.List;

import cn.jupitermouse.lineage.graph.model.*;

/**
 * <p>
 * BelongRelService
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public interface OfRelService {

    //===============================================================================
    //  节点从属关系建立
    //===============================================================================

    /**
     * 创建关系节点 从属关系 filed -> table
     *
     * @param starts 从节点列表  如List<Fields> -> Table
     * @param end    目标节点 为大一级的节点
     */
    @Deprecated
    void createNodeOfRelRel(List<BaseNodeEntity> starts, BaseNodeEntity end);

    /**
     * 创建关系节点 从属关系 schemas -> database
     *
     * @param schemas  从节点列表
     * @param database 目标节点
     */
    @Deprecated
    void createSchemaOfDatabaseRel(List<SchemaEntity> schemas, DatabaseEntity database);


    /**
     * 创建关系节点 从属关系 tables -> database : mysql
     *
     * @param tables   从节点列表
     * @param database 目标节点
     */
    @Deprecated
    void createTableOfDatabaseRel(List<TableEntity> tables, DatabaseEntity database);

    /**
     * 创建关系节点 从属关系 tables -> database : mysql
     *
     * @param database 目标节点
     */
    void createTableOrSchemaOfDatabaseRel(DatabaseEntity database);

    /**
     * 创建关系节点 从属关系 tables -> database : mysql
     *
     * @param databaseEntityList 目标节点列表
     */
    void createTableOrSchemaOfDatabaseRelList(List<DatabaseEntity> databaseEntityList);

    /**
     * 创建关系节点 从属关系 tables -> schema postgresql
     *
     * @param tables 从节点列表
     * @param schema 目标节点
     */
    @Deprecated
    void createTableOfSchemaRel(List<TableEntity> tables, SchemaEntity schema);

    /**
     * 创建关系节点 从属关系 tables -> schema postgresql
     *
     * @param schema 目标节点
     */
    void createTableOfSchemaRel(SchemaEntity schema);


    /**
     * 创建关系节点 从属关系 tables -> schema postgresql
     *
     * @param schemaEntityList 目标节点
     */
    void createTableOfSchemaRelList(List<SchemaEntity> schemaEntityList);

    /**
     * 创建关系节点 从属关系 fields -> table
     *
     * @param fields 从节点列表
     * @param table  目标节点
     */
    @Deprecated
    void createFieldOfTableRel(List<FieldEntity> fields, TableEntity table);

    /**
     * 创建关系节点 从属关系 fields -> table
     *
     * @param table 目标节点
     */
    void createFieldOfTableRel(TableEntity table);

    /**
     * 创建关系节点 从属关系 fields -> table
     *
     * @param tableList 目标节点
     */
    void createFieldOfTableRelList(List<TableEntity> tableList);
}
