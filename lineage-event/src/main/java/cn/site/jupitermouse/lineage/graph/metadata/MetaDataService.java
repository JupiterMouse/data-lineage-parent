package cn.site.jupitermouse.lineage.graph.metadata;

import java.util.List;

import cn.site.jupitermouse.lineage.graph.domain.model.FieldNode;
import cn.site.jupitermouse.lineage.graph.domain.model.SchemaNode;
import cn.site.jupitermouse.lineage.graph.domain.model.TableNode;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;

/**
 * <p>
 * MetaDataService
 * </p>
 *
 * @author JupiterMouse 2020/10/10
 * @since 1.0
 */
public interface MetaDataService {

    /**
     * 查询Schema信息
     *
     * @param schemaNode schemaNode
     */
    void querySchema(SchemaNode schemaNode);

    /**
     * 查询Table信息
     *
     * @param tableNode tableNode
     */
    void queryTable(TableNode tableNode);

    void queryField(FieldNode fieldNode);

    List<FieldNode> queryField(SqlRequestContext request, String targetTableSchema, String targetTableName);

    void queryTable(List<TableNode> tableNodeList);

    void querySchema(List<SchemaNode> schemaNodeList);

    void queryField(List<FieldNode> fieldNodeList);
}
