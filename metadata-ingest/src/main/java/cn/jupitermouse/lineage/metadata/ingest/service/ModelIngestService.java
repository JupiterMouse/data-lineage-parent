package cn.jupitermouse.lineage.metadata.ingest.service;

/**
 * <p>
 * ModelIngest 服务
 * </p>
 *
 * @author JupiterMouse 2020/09/23
 * @since 1.0
 */
public interface ModelIngestService {

    /**
     * schema 的提取，schema 并包含其下所有的table
     *
     * @param cluster cluster
     * @param catalog catalog(database)
     * @param schema  schema
     */
    void schemaInfoIngest(String cluster, String catalog, String schema);

    /**
     * table 的提取, table 并包含其下所有的field
     *
     * @param cluster cluster
     * @param catalog catalog(database)
     * @param schema  schema
     * @param table   table
     */
    void tableInfoIngest(String cluster, String catalog, String schema, String table);
}
