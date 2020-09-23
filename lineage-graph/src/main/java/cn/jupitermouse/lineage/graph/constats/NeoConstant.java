package cn.jupitermouse.lineage.graph.constats;

/**
 * <p>
 * 常量池实现
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public class NeoConstant {

    private NeoConstant() {
    }

    /**
     * /{业务代码}/{database}/{schema}/{table}
     */
    public static final String FT_TABLE_INDEX = "/%s/%s/%s/%s";

    /**
     * /{业务代码}/{database}/{schema}/{table}/{column}
     */
    public static final String FT_COLUMN_INDEX = "/%s/%s/%s/%s/%s";

    public static final String DEFAULT_BUSINESS_CODE = "default";

    public static final String DEFAULT_DB_NAME = "db";

    public static final String DEFAULT_DB_SCHEMA = "schema";

    public static class Graph {

        public static final String NODE_DATABASE = "Database";
        public static final String NODE_SCHEMA = "Schema";
        public static final String NODE_TABLE = "Table";
        public static final String NODE_FIELD = "Field";
        public static final String REL_HAS = "HAS";
        public static final String REL_OF = "OF";
        public static final String REL_FROM = "FROM";
    }

}
