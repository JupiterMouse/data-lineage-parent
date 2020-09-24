package cn.jupitermouse.lineage.graph.service;

import java.util.List;
import java.util.Map;

import org.springframework.lang.NonNull;

/**
 * <p>
 * 关系的处理
 * </p>
 *
 * @author JupiterMouse 2020/09/24
 * @since 1.0
 */
public interface RelationshipService {

    void nodeRelNodes(@NonNull String startLabel, @NonNull String startId, @NonNull String endLabel, @NonNull List<String> endIds, @NonNull String relType,
            Map<String, String> parameters);
}
