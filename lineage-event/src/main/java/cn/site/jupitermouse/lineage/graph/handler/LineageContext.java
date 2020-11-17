package cn.site.jupitermouse.lineage.graph.handler;

import java.util.ArrayList;
import java.util.List;

import cn.site.jupitermouse.lineage.graph.domain.model.*;
import cn.site.jupitermouse.lineage.graph.handler.sql.SqlMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 血缘上下文
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineageContext {
    private final List<PlatformNode> platformNodeList = new ArrayList<>();
    private final List<ClusterNode> clusterNodeList = new ArrayList<>();
    private final List<SchemaNode> schemaNodeList = new ArrayList<>();
    private final List<TableNode> tableNodeList = new ArrayList<>();
    private final List<FieldNode> fieldNodeList = new ArrayList<>();
    private final List<ProcessNode> processNodeList = new ArrayList<>();

    private SqlMessage sqlMessage;
}
