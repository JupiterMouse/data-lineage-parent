package cn.jupitermouse.lineage.metadata.ingest.infra.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import cn.jupitermouse.lineage.common.exception.CommonException;

/**
 * <p>
 * 获取元数据信息
 * </p>
 *
 * @author JupiterMouse 2020/09/23
 * @since 1.0
 */
public class MetaDataUtil {

    public static String getCatalog(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return connection.getCatalog();
        } catch (SQLException e) {
            throw new CommonException("get metadata error", e);
        }
    }

    public static String getSchema(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return connection.getSchema();
        } catch (SQLException e) {
            throw new CommonException("get metadata error", e);
        }
    }

    public static List<Map<String, Object>> schemas(DataSource dataSource, String catalog, String schemaName) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); ResultSet rs = connection.getMetaData()
                .getSchemas(catalog, schemaName)) {
            while (rs != null && rs.next()) {
                Map<String, Object> schemaMap = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    schemaMap.put(metaData.getColumnName(i).toUpperCase(), rs.getObject(i));
                }
                list.add(schemaMap);
            }
        } catch (SQLException e) {
            throw new CommonException("get metadata error", e);
        }
        return list;
    }

    public static List<Map<String, Object>> tableInfo(DataSource dataSource, String catalog, String schema,
            String tableName) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); ResultSet rs = connection.getMetaData()
                .getTables(catalog, schema, tableName, new String[]{"TABLE", "VIEW"})) {
            while (rs != null && rs.next()) {
                Map<String, Object> table = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    table.put(metaData.getColumnName(i).toUpperCase(), rs.getObject(i));
                }
                list.add(table);
            }
        } catch (SQLException e) {
            throw new CommonException("get metadata error", e);
        }
        return list;
    }

    public static List<Map<String, Object>> fieldInfo(DataSource dataSource, String catalog, String schema,
            String tableName, String fieldName) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); ResultSet rs = connection.getMetaData()
                .getColumns(catalog, schema, tableName, fieldName)) {
            while (rs != null && rs.next()) {
                Map<String, Object> field = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    field.put(metaData.getColumnName(i).toUpperCase(), rs.getObject(i));
                }
                list.add(field);
            }
        } catch (SQLException e) {
            throw new CommonException("get metadata error", e);
        }
        return list;
    }


}
