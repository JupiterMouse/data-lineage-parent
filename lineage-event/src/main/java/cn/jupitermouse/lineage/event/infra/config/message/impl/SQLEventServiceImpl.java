package cn.jupitermouse.lineage.event.infra.config.message.impl;

import org.springframework.stereotype.Component;

import cn.jupitermouse.lineage.common.constant.SourceTypeEnum;
import cn.jupitermouse.lineage.common.message.SqlRequestDTO;
import cn.jupitermouse.lineage.common.util.JSON;
import cn.jupitermouse.lineage.event.infra.config.message.IBaseEventService;
import cn.jupitermouse.lineage.graph.service.LineageElementService;

/**
 * <p>
 * SQL 类型处理
 * </p>
 *
 * @author JupiterMouse 2020/09/16
 * @since 1.0
 */
@Component
public class SQLEventServiceImpl implements IBaseEventService {

    private final LineageElementService lineageElementService;

    public SQLEventServiceImpl(LineageElementService lineageElementService) {
        this.lineageElementService = lineageElementService;
    }

    @Override
    public boolean dealEvent(String message) {
        SqlRequestDTO sqlRequestDTO = JSON.toObj(message, SqlRequestDTO.class);
        lineageElementService
                .ingestTableLineage(sqlRequestDTO.getCluster(), sqlRequestDTO.getDatabase(), sqlRequestDTO.getSchema(),
                        sqlRequestDTO.getDbType(), sqlRequestDTO.getSql());
        lineageElementService
                .ingestColumnLineage(sqlRequestDTO.getCluster(), sqlRequestDTO.getDatabase(), sqlRequestDTO.getSchema(),
                        sqlRequestDTO.getDbType(), sqlRequestDTO.getSql());
        return true;
    }

    @Override
    public String getType() {
        return SourceTypeEnum.SQL.getKey();
    }
}
