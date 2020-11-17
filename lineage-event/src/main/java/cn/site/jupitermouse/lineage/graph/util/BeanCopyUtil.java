package cn.site.jupitermouse.lineage.graph.util;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * <p>
 * 克隆辅助类
 * </p>
 *
 * @author JupiterMouse 2020/10/10
 * @since 1.0
 */
public class BeanCopyUtil {

    private BeanCopyUtil() {

    }

    /**
     * 获取所有字段为null的属性名
     * 用于BeanUtils.copyProperties()拷贝属性时，忽略空值
     *
     * @param source source
     * @return String[]
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (Objects.isNull(srcValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 获取所有字段不为null的属性名
     * 用于BeanUtils.copyProperties()拷贝属性时，忽略空值
     *
     * @param source source
     * @return String[]
     */
    public static String[] getNonNullProperties(Object source) {
        // 获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(source);
        // 获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        // 获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
            if (Objects.nonNull(propertyValue)) {
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }
}
