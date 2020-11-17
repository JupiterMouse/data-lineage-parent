package cn.site.jupitermouse.lineage.graph.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * <p>
 * 基础事件处理
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
public interface BaseMessageHandler {

    /**
     * 处理事件
     *
     * @param record kafka消息
     * @return 上下问
     */
    LineageContext handle(ConsumerRecord<String, String> record);

}
