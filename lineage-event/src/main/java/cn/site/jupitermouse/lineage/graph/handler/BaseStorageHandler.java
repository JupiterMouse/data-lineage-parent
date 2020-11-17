package cn.site.jupitermouse.lineage.graph.handler;

/**
 * <p>
 * 存储实现
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
public interface BaseStorageHandler {

    /**
     * 处理
     *
     * @param context 上下文
     */
    void handle(LineageContext context);
}
