package cn.site.jupitermouse.lineage.main;

/**
 * <p>
 * 启动类
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */

import cn.site.jupitermouse.lineage.graph.domain.repository.SimpleJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * <p>
 * 启动类
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@SpringBootApplication(
        scanBasePackages = {DataLineageApp.BASE_PACKAGE}, exclude = {DataSourceAutoConfiguration.class}
)
@EnableNeo4jRepositories(repositoryBaseClass = SimpleJpaRepositoryImpl.class,
        basePackages = "cn.site.jupitermouse.lineage.graph.domain.repository")
@EntityScan(basePackages = "cn.site.jupitermouse.lineage.graph.domain.model")
@EnableKafka
public class DataLineageApp {

    public final static String BASE_PACKAGE = "cn.site.jupitermouse.lineage.*";

    public static void main(String[] args) {
        SpringApplication.run(DataLineageApp.class, args);
    }

}
