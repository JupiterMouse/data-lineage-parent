package cn.jupitermouse.lineage.main.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.jupitermouse.lineage.common.message.BaseMessage;
import cn.jupitermouse.lineage.common.message.SqlRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/15
 * @since 1.0
 */

@RestController
@RequestMapping("/event")
@Api(tags = "消息", value = "消息")
@Slf4j
public class EventController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public EventController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @ApiOperation(value = "消息写入", notes = "消息写入")
    @PostMapping
    public ResponseEntity<Void> event(SqlRequestDTO dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String data = "";
        try {
            data = objectMapper.writeValueAsString(dto);
            BaseMessage message = BaseMessage.builder()
                    .type("SQL")
                    .data(data)
                    .build();

            kafkaTemplate.send("lineage", objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
