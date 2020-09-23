package cn.jupitermouse.lineage.graph.model.convert;

import java.util.Collections;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import org.neo4j.ogm.typeconversion.CompositeAttributeConverter;

/**
 * <p>
 * 节点MAP属性处理
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public class MapCompositeAttributeConverter implements CompositeAttributeConverter<Map<String, ?>> {

    @Override
    public Map<String, ?> toGraphProperties(Map<String, ?> value) {
        if (CollectionUtils.isEmpty(value)) {
            return Collections.emptyMap();
        }
        return value;
    }

    @Override
    public Map<String, ?> toEntityAttribute(Map<String, ?> value) {
        if (CollectionUtils.isEmpty(value)) {
            return Collections.emptyMap();
        }
        return value;
    }
}
