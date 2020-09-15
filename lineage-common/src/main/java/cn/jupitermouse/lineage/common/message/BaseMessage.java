package cn.jupitermouse.lineage.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 消息接收类型
 * </p>
 *
 * @author JupiterMouse 2020/09/15
 * @since 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessage {

    /**
     * 消息来源类型
     */
    private String type;
    /**
     * 消息内容
     */
    private String data;
}
