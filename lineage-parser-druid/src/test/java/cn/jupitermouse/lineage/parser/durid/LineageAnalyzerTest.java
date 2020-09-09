package cn.jupitermouse.lineage.parser.durid;

import java.util.List;

import cn.jupitermouse.lineage.parser.durid.analyse.LineageAnalyzer;
import cn.jupitermouse.lineage.parser.model.ColumnNode;
import cn.jupitermouse.lineage.parser.model.TableNode;
import cn.jupitermouse.lineage.parser.model.TreeNode;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;

/**
 * <p>
 * LineageAnalyzerTest
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
public class LineageAnalyzerTest {

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
            "select " +
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
    @Test
    public void testTableLineageAnalyzer() {
        String sql = CREATE_VIEW_SQL_ONE;
        LineageAnalyzer lineageAnalyzer = new LineageAnalyzer();
        TreeNode<TableNode> tableNodeTreeNode = lineageAnalyzer.lineageTreeAnalyzer(sql, JdbcConstants.POSTGRESQL);
        System.out.println();
    }

    @Test
    public void testColumnLineageAnalyzer2() {
        String sql = CREATE_VIEW_SQL_TWO;
        LineageAnalyzer lineageAnalyzer = new LineageAnalyzer();
        List<TreeNode<ColumnNode>> treeNodes = lineageAnalyzer
                .originColumnLineageTreeAnalyzer(sql, JdbcConstants.POSTGRESQL);
        System.out.println();
    }

}
