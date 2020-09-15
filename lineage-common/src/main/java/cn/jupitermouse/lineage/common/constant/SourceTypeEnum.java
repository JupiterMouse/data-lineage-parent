package cn.jupitermouse.lineage.common.constant;

/**
 * <p>
 * 消息类型
 * </p>
 *
 * @author JupiterMouse 2020/09/16
 * @since 1.0
 */
public enum SourceTypeEnum {

    /**
     * HOOK
     */
    HOOK("HOOK"),
    /**
     * SQL
     */
    SQL("SQL"),
    /**
     * HDSP
     */
    HDSP("HDSP"),
    /**
     * MAINTAIN
     */
    MAINTAIN("MAINTAIN");

    private String key;

    SourceTypeEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
