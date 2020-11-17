package cn.site.jupitermouse.lineage.graph.util;


import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.site.jupitermouse.lineage.common.exception.ParserException;
import cn.site.jupitermouse.lineage.common.util.StringPool;
import cn.site.jupitermouse.lineage.graph.handler.sql.SqlMessage;
import cn.site.jupitermouse.lineage.parser.druid.analyse.SqlRequestContext;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Sql 拆分处理
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public class SqlKafkaUtil {
    private static final String[] IGNORE_PROP = new String[]{"ext"};

    private SqlKafkaUtil() {
    }

    /**
     * SqlMessage convert SqlRequestContext
     *
     * @param sqlMessage 接收的SQL消息
     * @return List<SqlRequestContext>
     */
    public static List<SqlRequestContext> convertSqlRequest(SqlMessage sqlMessage) {
        String sql = sqlMessage.getSql();
        if (StringUtils.isEmpty(sql)) {
            throw new ParserException("sql.is.empty");
        }
        List<String> sqlList = sqlExtract2List(sql);
        // 如果不包含 insert
        return sqlList.stream()
                .filter(SqlKafkaUtil::filterSql)
                .map(str -> {
                    SqlRequestContext sqlRequestContext = new SqlRequestContext();
                    BeanUtils.copyProperties(sqlMessage, sqlRequestContext, IGNORE_PROP);
                    return sqlRequestContext;
                })
                .collect(Collectors.toList());
    }

    private static boolean filterSql(String sql) {
        return sql.toLowerCase().contains("insert") || sql.toLowerCase().contains("create");
    }

    private static List<String> sqlExtract2List(String text) {
        // 多条SQL拆分转换成一条
        LineNumberReader lineReader = new LineNumberReader(new StringReader(text));
        List<String> sqlList = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        lineReader.lines().forEach(line -> {
            line = line.trim();
            // 注释符开头认为是注释
            if (line.startsWith(StringPool.TWO_MIDDLE_LINE)) {
                sqlBuilder.append(line).append(StringPool.NEWLINE);
                // ;认为是结尾
            } else if (line.endsWith(StringPool.SEMICOLON)) {
                sqlBuilder.append(line).append(StringPool.NEWLINE);
                sqlList.add(sqlBuilder.toString());
                sqlBuilder.delete(0, sqlBuilder.length());
            } else {
                sqlBuilder.append(line).append(StringPool.SPACE);
            }
        });
        // 如果最后一条语句没有;结尾，补充
        if (!StringUtils.isEmpty(sqlBuilder)) {
            String r = new LineNumberReader(new StringReader(sqlBuilder.toString().trim()))
                    .lines()
                    .filter(line -> !line.trim().startsWith(StringPool.TWO_MIDDLE_LINE))
                    .findFirst()
                    .orElse(StringPool.EMPTY);
            if (!StringUtils.isEmpty(r)) {
                // 全为注释 noting to do 否则补充一条SQL
                sqlList.add(sqlBuilder.toString());
            }
        }
        return sqlList;
    }

}
