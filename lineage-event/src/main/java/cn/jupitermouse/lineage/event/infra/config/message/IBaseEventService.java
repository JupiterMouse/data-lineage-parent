package cn.jupitermouse.lineage.event.infra.config.message;

/**
 * <p>
 * 基础事件处理接口
 * </p>
 *
 * @author JupiterMouse 2020/09/16
 * @since 1.0
 */
public interface IBaseEventService {

    /**
     * 处理事件
     *
     * @param message 消息
     * @return status
     */
    boolean dealEvent(String message);

    /**
     * 获取事件类型
     *
     * @return type
     */
    String getType();
}
