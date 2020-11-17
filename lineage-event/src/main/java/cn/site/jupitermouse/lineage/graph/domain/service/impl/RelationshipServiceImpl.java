package cn.site.jupitermouse.lineage.graph.domain.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.site.jupitermouse.lineage.common.exception.CommonException;
import cn.site.jupitermouse.lineage.common.util.StringPool;
import cn.site.jupitermouse.lineage.graph.domain.service.RelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * PROCESS_IN  TABLE|FIELD -(PROCESS_IN)> Process
 * 以多对一的方式合并去建立关系
 * </p>
 *
 * @author JupiterMouse 2020/10/09
 * @since 1.0
 */
@Service
@Slf4j
public class RelationshipServiceImpl implements RelationshipService {
    private static final String DELIMITER = "','";

    /**
     * s ->(n:1) e
     */
    private static final String MERGE_PROCESS_INPUTS = "MATCH (s),(e:Process) WHERE " +
            "s.pk in [%s] and e.pk = '%s'  "
            + "MERGE (s)-[r:PROCESS_IN]->(e) set r.timestamp=timestamp() "
            + "RETURN id(r) as relId";

    private final SessionFactory sessionFactory;

    public RelationshipServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void mergeRelProcessInputs(List<String> starts, String end) {
        if (CollectionUtils.isEmpty(starts)) {
            return;
        }
        String cql = String.format(
                MERGE_PROCESS_INPUTS,
                // （'pk1','pk2','pk3'）
                starts.stream().collect(Collectors.joining(DELIMITER, StringPool.SINGLE_QUOTE, StringPool.SINGLE_QUOTE)),
                end);
        log.debug("execute cql is [{}]", cql);
        Session session = sessionFactory.openSession();
        Result result = session.query(cql, Collections.emptyMap());
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        result.forEach(resultMapList::add);
        if (resultMapList.size() != starts.size()) {
            log.error("execute recode num [{}] != success num [{}]. maybe neo4j question. current cql is [{}]",
                    starts.size(), resultMapList.size(), cql);
            // record
            throw new CommonException("execute recode num [%s] != success num [%s]. maybe neo4j question",
                    starts.size(), resultMapList.size());
        }
    }

}
