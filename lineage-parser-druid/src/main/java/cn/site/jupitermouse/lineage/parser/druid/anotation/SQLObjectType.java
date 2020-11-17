package cn.site.jupitermouse.lineage.parser.druid.anotation;

import java.lang.annotation.*;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * <p>
 * SQLObjectType 种类
 * </p>
 *
 * @author JupiterMouse 2020/09/09
 * @since 1.0
 */
@Component
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SQLObjectType {

    @Nullable Class<?> clazz();

    Class<?>[] parent() default {};
}
