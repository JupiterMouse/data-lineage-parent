package cn.site.jupitermouse.lineage.graph.handler;

import java.util.Map;
import java.util.Objects;

import cn.site.jupitermouse.lineage.common.message.SourceType;
import cn.site.jupitermouse.lineage.common.util.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 消息处理
 * </p>
 *
 * @author JupiterMouse 2020/09/15
 * @since 1.0
 */
@Component
@Slf4j
public class MessageExecutor {

    private final Map<String, BaseMessageHandler> messageHandlerMap;
    private final BaseStorageHandler mergeStorageHandler;

    public MessageExecutor(Map<String, BaseMessageHandler> messageHandlerMap,
                           BaseStorageHandler mergeStorageHandler) {
        this.messageHandlerMap = messageHandlerMap;
        this.mergeStorageHandler = mergeStorageHandler;
    }

    @KafkaListener(topics = "lineage", containerFactory = "ackContainerFactory")
    public void handleMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            String data = record.value();
            SourceType sourceType = JSON.toObj(data, SourceType.class);
            Objects.requireNonNull(sourceType, "sourceType not null");
            // 消息处理
            BaseMessageHandler messageHandler = messageHandlerMap.get(sourceType.getSourceType().toUpperCase());
            Objects.requireNonNull(messageHandler, "messageHandler not null");
            // 上下文
            LineageContext lineageContext = messageHandler.handle(record);
            Objects.requireNonNull(messageHandler, "messageHandler not null");
            // 存储
            mergeStorageHandler.handle(lineageContext);
        } catch (Exception e) {
            // 记录
            log.error(e.getMessage(), e);
        } finally {
            // 手动提交 offset
            acknowledgment.acknowledge();
        }
    }

}
