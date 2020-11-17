package cn.site.jupitermouse.lineage.parser.druid.analyse.handler;

import java.util.List;

import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlResponseContext;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 按照Ordered默认顺序处理
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
@Component
public class DefaultHandlerChain implements IHandlerChain {

    private final List<IHandler> handlerList;

    public DefaultHandlerChain(List<IHandler> handlerList) {
        this.handlerList = handlerList;
    }

    @Override
    public void handle(SqlRequestContext request, SqlResponseContext response) {
        handlerList.forEach(handler -> handler.handleRequest(request, response));
    }

}
