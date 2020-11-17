package cn.site.jupitermouse.lineage.common.exception;

/**
 * <p>
 * JSON异常
 * </p>
 *
 * @author JupiterMouse 2020/9/16
 * @since 1.0
 */
public class JsonException extends RuntimeException {

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String format, Object... args) {
        super(String.format(format, args));
    }

    public JsonException(Throwable cause) {
        super(cause);
    }
}
