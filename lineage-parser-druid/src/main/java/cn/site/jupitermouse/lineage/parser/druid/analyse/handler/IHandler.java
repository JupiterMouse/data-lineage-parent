package cn.site.jupitermouse.lineage.parser.druid.analyse.handler;


import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlResponseContext;

/**
 * <p>
 * 血缘处理
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public interface IHandler {

    /**
     * 表血缘处理
     *
     * @param request  sql 请求
     * @param response 响应
     */
    void handleRequest(SqlRequestContext request, SqlResponseContext response);
}
