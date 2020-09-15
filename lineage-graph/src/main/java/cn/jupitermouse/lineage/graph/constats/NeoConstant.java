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

}
