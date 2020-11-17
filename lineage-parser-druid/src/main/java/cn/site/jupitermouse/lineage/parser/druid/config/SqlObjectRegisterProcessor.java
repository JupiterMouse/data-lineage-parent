package cn.site.jupitermouse.lineage.parser.druid.config;

import cn.site.jupitermouse.lineage.parser.druid.anotation.SQLObjectType;
import cn.site.jupitermouse.lineage.parser.druid.process.ProcessorRegister;
import com.alibaba.druid.sql.ast.SQLObject;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 注册后置处理器
 * </p>
 *
 * @author JupiterMouse 2020/09/16
 * @since 1.0
 */
@Component
@ConditionalOnClass(SQLObject.class)
public class SqlObjectRegisterProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> cla = bean.getClass();
        SQLObjectType sqlObjectType = cla.getAnnotation(SQLObjectType.class);
        if (sqlObjectType == null) {
            return bean;
        }
        Class<?> clazz = sqlObjectType.clazz();
        ProcessorRegister.register(clazz, bean);
        return bean;
    }
}
