package cn.jupitermouse.lineage.event.infra.config.handler;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import cn.jupitermouse.lineage.common.message.BaseMessage;
import cn.jupitermouse.lineage.common.util.JSON;
import cn.jupitermouse.lineage.event.infra.config.constant.MessageConstant;
import cn.jupitermouse.lineage.event.infra.config.message.EventStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

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
public class MessageHandler {

    private final EventStrategyService strategyService;

    public MessageHandler(EventStrategyService strategyService) {
        this.strategyService = strategyService;
    }

    @KafkaListener(topics = MessageConstant.LINEAGE_TOPIC, containerFactory = "ackContainerFactory")
    public void handleMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String data = record.value();
        BaseMessage baseMessage = JSON.toObj(data, BaseMessage.class);
        strategyService.dealEvent(baseMessage.getType(), baseMessage.getData());
        try {
            strategyService.dealEvent(baseMessage.getType(), baseMessage.getData());
        } catch (Exception e) {
            // 记录
            log.error(e.getMessage(), e);
        } finally {
            // 手动提交 offset
            acknowledgment.acknowledge();
        }
    }

}
