package cn.site.jupitermouse.lineage.graph.annotation;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

/**
 * <p>
 * SourceType
 * </p>
 *
 * @author JupiterMouse 2020/11/12
 * @since 1.0
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SourceHandler {
    String value();
}
