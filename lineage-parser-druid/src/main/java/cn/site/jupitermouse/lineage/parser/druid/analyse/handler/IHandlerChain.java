package cn.site.jupitermouse.lineage.parser.druid.analyse.handler;

import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlResponseContext;

/**
 * <p>
 * 血缘流程处理
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public interface IHandlerChain {

    /**
     * 处理
     *
     * @param request  request
     * @param response response
     */
    void handle(SqlRequestContext request, SqlResponseContext response);

}
