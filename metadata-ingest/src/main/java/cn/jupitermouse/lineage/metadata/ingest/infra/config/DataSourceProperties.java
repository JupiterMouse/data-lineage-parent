package cn.jupitermouse.lineage.metadata.ingest.infra.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/23
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "lineage")
@Data
public class DataSourceProperties {

    private Map<String, HikariConfig> datasource = new HashMap<>();
}
