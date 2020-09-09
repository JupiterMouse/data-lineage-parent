package cn.jupitermouse.lineage.graph.constats;

/**
 * <p>
 * 常量池实现
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public interface NeoConstant {

    /**
     * /{业务代码}/{database}/{schema}/{table}
     */
    String FT_TABLE_INDEX = "/%s/%s/%s/%s";

    /**
     * /{业务代码}/{database}/{schema}/{table}/{column}
     */
    String FT_COLUMN_INDEX = "/%s/%s/%s/%s/%s";

    String DEFAULT_BUSINESS_CODE = "default";

    String DEFAULT_DB_NAME = "db";

    String DEFAULT_DB_SCHEMA = "schema";

}
