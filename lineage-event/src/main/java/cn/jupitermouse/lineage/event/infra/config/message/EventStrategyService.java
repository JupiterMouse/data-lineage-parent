package cn.jupitermouse.lineage.event.infra.config.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.jupitermouse.lineage.common.exception.CommonException;

/**
 * <p>
 * 来源处理策略
 * </p>
 *
 * @author JupiterMouse 2020/09/16
 * @since 1.0
 */
@Component
public class EventStrategyService {

    private final Map<String, IBaseEventService> eventServiceMap = new HashMap<>();

    /**
     * 构造函数
     *
     * @param eventServices spring容器中所有IBaseEventService的实现类
     */
    public EventStrategyService(List<IBaseEventService> eventServices) {
        for (IBaseEventService eventService : eventServices) {
            eventServiceMap.put(eventService.getType(), eventService);
        }
    }

    /**
     * 根据事件类型调用不同的实现类处理
     */
    public boolean dealEvent(String sourceType, String message) {
        IBaseEventService eventService = eventServiceMap.get(sourceType);
        if (eventService == null) {
            throw new CommonException("error.type.found" + sourceType);
        }
        return eventService.dealEvent(message);
    }
}
