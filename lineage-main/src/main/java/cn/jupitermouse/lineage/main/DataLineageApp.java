package cn.jupitermouse.lineage.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import cn.jupitermouse.lineage.graph.service.LineageElementService;
import com.alibaba.druid.util.JdbcConstants;

/**
 * <p>
 * 启动类
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@SpringBootApplication(
        scanBasePackages = {"cn.jupitermouse.lineage"}
)
@EnableNeo4jRepositories(basePackages = {"cn.jupitermouse.lineage.graph.repository"})
// soved not a valid entity class. Please check the entity mapping
@EntityScan(basePackages = "cn.jupitermouse.lineage.graph.entity")
public class DataLineageApp {

    public static final String CREATE_VIEW_SQL_ONE = "create or replace view dim.dim_dpt_text_rel_v as\n" +
            "select hrp1000.objid + hrp1000.stext + osh1001_3.sobid + sum(osh1000_1.stext)  as asobjid,            --部门编码\n"
            +
            "       hrp1000.stext as asstext,            --部门名称\n" +
            "       osh1001_3.sobid sob_code, --上级部门编码\n" +
            "       osh1000_1.stext sob_stext, --上级部门描述\n" +
            " hrp1000.stext " +
            "from dwd.dwd_sap_hrp1000_v hrp1000\n" +
            "         left join (select distinct objid, sobid\n" +
            "                    from dwd.dwd_sap_hrp1001_v\n" +
            "                    where OTYPE = 'O'\n" +
            "                      AND PLVAR = '01'\n" +
            "                      AND RSIGN = 'A'\n" +
            "                      AND RELAT = '002'\n" +
            "                      AND SCLAS = 'O'\n" +
            "                      AND BEGDA <= current_date\n" +
            "                      AND ENDDA >= current_date) osh1001_3\n" +
            "                   on hrp1000.objid = osh1001_3.objid\n" +
            "         left join (select objid, stext from dwd.dwd_sap_hrp1000_v where current_date between begda and endda) osh1000_1\n"
            +
            "                   on osh1001_3.sobid = osh1000_1.objid\n" +
            "where hrp1000.otype = 'O'\n" +
            "  AND hrp1000.BEGDA <= current_date\n" +
            "  AND hrp1000.ENDDA >= current_date\n" +
            "order by objid\n";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DataLineageApp.class, args);
        LineageElementService bean = context.getBean(LineageElementService.class);
        bean.IngestTableLineage(CREATE_VIEW_SQL_ONE, JdbcConstants.POSTGRESQL);
    }

}
