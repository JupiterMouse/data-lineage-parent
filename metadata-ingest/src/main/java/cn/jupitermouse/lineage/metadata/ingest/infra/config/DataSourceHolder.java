package cn.jupitermouse.lineage.metadata.ingest.infra.config;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

import cn.jupitermouse.lineage.common.exception.CommonException;
import cn.jupitermouse.lineage.common.util.ApplicationContextHelper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 数据源持久
 * </p>
 *
 * @author JupiterMouse 2020/09/23
 * @since 1.0
 */
@Slf4j
public class DataSourceHolder {

    private static final Map<String, DataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<>();
    private static final Map<String, HikariConfig> DATASOURCE_CONFIG_MAP;

    static {
        DataSourceProperties dataSourceProperties = ApplicationContextHelper.getContext()
                .getBean(DataSourceProperties.class);
        DATASOURCE_CONFIG_MAP = dataSourceProperties.getDatasource();
    }

    public static DataSource getDataSource(String cluster) {
        DataSource dataSource = DATA_SOURCE_MAP.get(cluster);
        if (dataSource == null) {
            HikariConfig hikariConfig = Optional.ofNullable(DATASOURCE_CONFIG_MAP.get(cluster))
                    .orElseThrow(() -> new CommonException("not found cluster:[%s] config ", cluster));
            dataSource = new HikariDataSource(hikariConfig);
            try {
                dataSource.getConnection();
            } catch (SQLException e) {
                log.error("{} datasource.connection.error", cluster);
                e.printStackTrace();
            }
            DATA_SOURCE_MAP.put(cluster, dataSource);
        }
        return dataSource;
    }

}
