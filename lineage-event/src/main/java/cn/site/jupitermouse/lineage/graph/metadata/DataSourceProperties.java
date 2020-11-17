package cn.site.jupitermouse.lineage.graph.metadata;

import java.util.HashMap;
import java.util.Map;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
