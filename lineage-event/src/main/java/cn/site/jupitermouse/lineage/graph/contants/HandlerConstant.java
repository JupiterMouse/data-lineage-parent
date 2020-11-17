package cn.site.jupitermouse.lineage.graph.contants;


/**
 * <p>
 * HandlerConstant
 * </p>
 *
 * @author JupiterMouse 2020/10/09
 * @since 1.0
 */
public class HandlerConstant {
    private HandlerConstant() {

    }

    public static final String REFRESH_TYPE = "RefreshTypeLineageExecutor";
    public static final String MERGE_TYPE = "MergeTypeLineageExecutor";

    public static final String SOURCE_TYPE_SQL_PARSER = "SQL_PARSER";
    public static final String SOURCE_TYPE_DATAX = "DATAX";
    public static final String SOURCE_TYPE_SQOOP = "SQOOP";
    public static final String SOURCE_TYPE_HIVE_HOOK = "HIVE-HOOK";
}
