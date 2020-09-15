package cn.jupitermouse.lineage.common.exception;

/**
 * <p>
 * JSON异常
 * </p>
 *
 * @author JupiterMouse 2020/9/16
 * @since 1.0
 */
public class CommonException extends RuntimeException {

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String format, Object... args) {
        super(String.format(format, args));
    }

    public CommonException(Throwable cause) {
        super(cause);
    }
}
