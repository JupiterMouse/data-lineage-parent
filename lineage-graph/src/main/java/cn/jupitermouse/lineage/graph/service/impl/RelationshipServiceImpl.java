package cn.jupitermouse.lineage.graph.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.jupitermouse.lineage.graph.service.RelationshipService;
import cn.jupitermouse.lineage.parser.exception.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/24
 * @since 1.0
 */
@Service
@Slf4j
public class RelationshipServiceImpl implements RelationshipService {

    private static final String MERGE_REL_CQL_FT = "MATCH (s:%s),(e:%s) WHERE s.graphId='%s' and e.graphId in [%s]  "
            + "MERGE (s)-[r:%s]->(e)  set %sr.timestamp=timestamp() "
            + "RETURN s.graphId as startGraphId,e.graphId as endGraphId,id(r) as relId";

    private final SessionFactory sessionFactory;

    public RelationshipServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void nodeRelNodes(@NonNull String startLabel, @NotNull String startId, @NotNull String endLabel,
            @NotNull List<String> endIds, @NotNull String relType,
            Map<String, String> parameters) {
        StringBuffer parameterStr = new StringBuffer();
        if (!CollectionUtils.isEmpty(parameters)) {
            parameters.forEach((k, v) -> {
                parameterStr.append("r.").append(k).append("=")
                        .append(Optional.ofNullable(v).map(x -> "'" + x + "'").orElse("null"))
                        .append(",");
            });
        }
        String cql = String.format(MERGE_REL_CQL_FT,
                startLabel,
                endLabel,
                startId,
                endIds.stream().collect(Collectors.joining(",", "'", "'")),
                relType,
                parameterStr.toString()
        );
        log.debug("=======>\n" + cql);
        final Session session = sessionFactory.openSession();
        final Result result = session.query(cql, Collections.emptyMap());
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        if (resultMapList.size() != endIds.size()) {
            // record
            log.error("=======>\n" + cql);
            throw new ParserException("rel error!");
        }
    }
}
