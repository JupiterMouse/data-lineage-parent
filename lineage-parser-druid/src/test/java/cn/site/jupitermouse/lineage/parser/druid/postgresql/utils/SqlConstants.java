package cn.site.jupitermouse.lineage.parser.druid.postgresql.utils;

/**
 * <p>
 * SQL 例子
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public class SqlConstants {

    //===============================================================================
    //  STATEMENT
    //===============================================================================

    /**
     * STATEMENT_CREATE_VIEW
     */
    public static final String STATEMENT_CREATE_VIEW = "create or replace view dim.dim_dpt_text_rel_v as\n" +
            "select hrp1000.objid,\n" +
            "       hrp1000.stext\n" +
            "from dwd.dwd_sap_hrp1000_v hrp1000";
    /**
     * STATEMENT_PG_INSERT
     */
    public static final String STATEMENT_PG_INSERT = "insert into dwd.dwd_hr_ts_di\n" +
            "select \n" +
            "       depi.gsbmo,\n" +
            "       depi.gsbmt,\n" +
            "       depi.usrid_long\n" +
            " from table1 depi";


    //===============================================================================
    //  SubQuery
    //===============================================================================

    /**
     * PG_SELECT_QUERY_BLOCK
     */
    public static final String QUERY_PG_PG_SELECT_QUERY_BLOCK = "create view view_test as \n" +
            "select temp.a1,temp.a2 (\n" +
            "    select a1,a2 from table1\n" +
            ") temp";

    /**
     * PG_SQL_UNION_QUERY
     */
    public static final String QUERY_PG_SQL_UNION_QUERY = "create view view_test as \n" +
            "    select a1,a2,a3 from table_a \n" +
            "union \n" +
            "    select b1,b2,b3 from table_b\n" +
            "union all\n" +
            "    select c1,c2,c3 from table_c";


    //===============================================================================
    //  SQLExpr
    //===============================================================================
    /**
     * Case when
     */
    public static final String SQL_EXPR_PG_CASE_WHEN = "CASE \n" +
            "\tWHEN position('WT' IN (emp_code)) = 1\n" +
            "\tAND char_length(emp_code) = 5 THEN replace(emp_code, 'WT', '30000')\n" +
            "\tWHEN position('WT' IN (emp_code)) = 1\n" +
            "\tAND char_length(emp_code) = 6 THEN replace(emp_code, 'WT', '3000')\n" +
            "\tWHEN position('WT' IN (emp_code)) = 1\n" +
            "\tAND char_length(emp_code) = 7 THEN replace(emp_code, 'WT', '300')\n" +
            "\tWHEN substring(emp_code, 1, 1) = '0' THEN CAST(CAST(emp_code AS bigint) AS text)\n" +
            "\tELSE emp_code\n" +
            "END";


    //===============================================================================
    //  SQLExprTableSource
    //===============================================================================
    /**
     * SQLJoinTableSource
     */
    public static final String TABLE_SQL_JOIN_TABLE_SOURCE = "select t1.a1,temp1.a2 from table1 t1\n" +
            "left join (\n" +
            "    select t2.a1,t2.a2 from table2 t2\n" +
            ") temp1 \n" +
            "on t1.a1 = temp1.a1";

    public static final String TABLE_SQL_JOIN_TABLE_SOURCE2 = "select t1.a1,t2.a2 from table1 t1,table2 t2";

    /**
     * SQLJoinTableSource
     */
    public static final String TABLE_SQL_UNION_QUERY_TABLE_SOURCE = "select t.a from (\n" +
            "    select t1.a1 from table1 t1\n" +
            "union\n" +
            "    select t2.b from table2 t2\n" +
            "union all\n" +
            "    select t3.c from table3 t3\n" +
            ")t";

    //===============================================================================
    //  Special Sql
    //===============================================================================

    public static final String RECURSIVE_AFTER_LEFT_JOIN = "         select \n" +
            "         hrp1000.objid,\n" +
            "         osh9002.dpt_code,\n" +
            "         osh9002.bukrs\n" +
            "         from \n" +
            "         dwd.dwd_sap_hrp1000_v hrp1000\n" +
            "         left join (with recursive dpt_bukrs(dpt_code, p_dpt_code, bukrs) as\n" +
            "                                       (\n" +
            "                                           select ddrv.dpt_code,\n" +
            "                                                  ddrv.p_dpt_code,\n" +
            "                                                  osh9002.bukrs\n" +
            "                                           from dim.dim_hr_dpt_rel_v ddrv\n" +
            "                                                    left join dwd.dwd_sap_hrp9002_v osh9002\n" +
            "                                                              on ddrv.dpt_code = osh9002.objid and\n" +
            "                                                                 current_date between osh9002.begda and osh9002.endda\n" +
            "                                           WHERE current_date between ddrv.begda and ddrv.endda\n" +
            "                                           union all\n" +
            "                                           select ddrv.dpt_code,\n" +
            "                                                  ddrv_1.p_dpt_code,\n" +
            "                                                  osh9002.bukrs\n" +
            "                                           from dpt_bukrs ddrv\n" +
            "                                                    join dim.dim_hr_dpt_rel_v ddrv_1 on ddrv.p_dpt_code = ddrv_1.dpt_code\n" +
            "                                                    left join dwd.dwd_sap_hrp9002_v osh9002\n" +
            "                                                              on ddrv_1.dpt_code = osh9002.objid and\n" +
            "                                                                 current_date between osh9002.begda and osh9002.endda\n" +
            "                                           WHERE ddrv.bukrs is null\n" +
            "                                             and current_date between ddrv_1.begda and ddrv_1.endda\n" +
            "                                       )\n" +
            "                    select dpt_code, bukrs\n" +
            "                    from dpt_bukrs\n" +
            "                    where bukrs is not null) osh9002\n" +
            "\t\t\t\t\t\ton hrp1000.objid = osh9002.dpt_code";


    public static final String RECURSIVE_AFTER_SELECT = "select hrp1000.objid,                            \n" +
            "       hrp1000.stext,                            \n" +
            "       hrp1000.endda,                            \n" +
            "       bu.bu_code        valid_flag,             \n" +
            "       bu.bu_code     bu_code,                \n" +
            "       bu.stext          bu_stext\n" +
            "from (\n" +
            "         select hrp1000.objid,\n" +
            "                hrp1000.stext,\n" +
            "                hrp1000.endda endda\n" +
            "         from dwd.dwd_sap_hrp1000_v hrp1000\n" +
            "         where hrp1000.otype = 'O'\n" +
            "           and current_date between begda and endda\n" +
            "     ) hrp1000\n" +
            "         left join (\n" +
            "         SELECT a.dpt_code,a.bu_code,b.stext\n" +
            "         from (\n" +
            "         WITH RECURSIVE dpt ( dpt_code, p_dpt_code, bu_code ) AS (\n" +
            "        SELECT\n" +
            "          dpt_code,\n" +
            "          p_dpt_code,\n" +
            "          decode(hrp9003.zsybf,'X',dpt_code,null) bu_code \n" +
            "        FROM\n" +
            "          dim.dim_hr_dpt_rel_v dptv\n" +
            "          left join dwd.dwd_sap_hrp9003_v hrp9003 \n" +
            "          on dptv.dpt_code = hrp9003.objid and CURRENT_DATE BETWEEN hrp9003.begda AND hrp9003.endda\n" +
            "        WHERE\n" +
            "          CURRENT_DATE BETWEEN dptv.begda AND dptv.endda \n" +
            "          \n" +
            "        UNION ALL\n" +
            "        SELECT\n" +
            "          dpt.dpt_code,\n" +
            "          dpt2.p_dpt_code,\n" +
            "          decode(hr9003.zsybf,'X',dpt2.dpt_code,null) bu_code \n" +
            "        FROM   dpt \n" +
            "          join dim.dim_hr_dpt_rel_v dpt2\n" +
            "          on  dpt2.dpt_code = dpt.p_dpt_code \n" +
            "          left join dwd.dwd_sap_hrp9003_v hr9003 \n" +
            "          on dpt2.dpt_code = hr9003.objid and CURRENT_DATE BETWEEN hr9003.begda AND hr9003.endda \n" +
            "          WHERE dpt.bu_code is null\n" +
            "          AND CURRENT_DATE BETWEEN dpt2.begda and dpt2.endda\n" +
            "          )\n" +
            "          SELECT\n" +
            "          dpt.dpt_code,\n" +
            "          dpt.bu_code \n" +
            "        FROM dpt \n" +
            "        WHERE dpt.bu_code is not null) a\n" +
            "\t\t\t\t  join (select hrp1000.objid,\n" +
            "                                          hrp1000.stext\n" +
            "                                   from dwd.dwd_sap_hrp1000_v hrp1000\n" +
            "                                   where hrp1000.endda >= current_date\n" +
            "                                     and current_date >= hrp1000.begda) b on a.bu_code = b.objid) bu\n" +
            "                   on bu.dpt_code = hrp1000.objid";
}
