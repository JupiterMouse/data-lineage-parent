package cn.jupitermouse.lineage.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.jupitermouse.lineage.graph.service.LineageElementService;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * 测试类
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@SpringBootTest(classes = DataLineageApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DataLineageAppTest {

    String CREATE_VIEW_SQL_ONE = "create or replace view dim.dim_dpt_text_rel_v as\n" +
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

    String CREATE_VIEW_SQL_TWO = "create or replace view dim.dim_dpt_text_rel_v as\n" +
            " select hrp1000.stext, " +
            " hrp1000.objid + hrp1000.stext + osh1001_3.sobid + sum(osh1000_1.stext)  as asobjid,"
//            "select hrp1000.objid + hrp1000.stext + osh1001_3.sobid + sum(osh1000_1.stext)  as asobjid,            --部门编码\n"
            +
            "       hrp1000.stext as asstext,            --部门名称\n" +
            "       osh1001_3.sobid sob_code, --上级部门编码\n" +
            "       osh1000_1.stext sob_stext --上级部门描述\n" +
//            " hrp1000.stext " +
            "from dwd.dwd_sap_hrp1000_v_1 hrp1000\n" +
            "         left join (select distinct objid, sobid\n" +
            "                    from dwd.dwd_sap_hrp1001_v_2\n" +
            "                    where OTYPE = 'O'\n" +
            "                      AND PLVAR = '01'\n" +
            "                      AND RSIGN = 'A'\n" +
            "                      AND RELAT = '002'\n" +
            "                      AND SCLAS = 'O'\n" +
            "                      AND BEGDA <= current_date\n" +
            "                      AND ENDDA >= current_date) osh1001_3\n" +
            "                   on hrp1000.objid = osh1001_3.objid\n" +
            "         left join (select objid, stext from dwd.dwd_sap_hrp1000_v_3 where current_date between begda and endda) osh1000_1\n"
            +
            "                   on osh1001_3.sobid = osh1000_1.objid\n" +
            "where hrp1000.otype = 'O'\n" +
            "  AND hrp1000.BEGDA <= current_date\n" +
            "  AND hrp1000.ENDDA >= current_date\n" +
            "order by objid\n";

    @Autowired
    private LineageElementService lineageElementService;

    @Test
    public void testTableLineage() {
        lineageElementService.IngestTableLineage(CREATE_VIEW_SQL_TWO, JdbcConstants.POSTGRESQL);
    }

    @Test
    public void testColumnLineage() {
        lineageElementService.IngestColumnLineage(CREATE_VIEW_SQL_TWO, JdbcConstants.POSTGRESQL);
    }

}
