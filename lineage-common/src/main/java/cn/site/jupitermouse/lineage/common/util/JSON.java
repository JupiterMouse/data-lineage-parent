package cn.site.jupitermouse.lineage.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.site.jupitermouse.lineage.common.exception.JsonException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

/**
 * <p>
 * JSON 工具类
 * </p>
 *
 * @author JupiterMouse 2020/9/16
 * @since 1.0
 */
public final class JSON {

    private static ObjectMapper objectMapper = ApplicationContextHelper.getContext().getBean(ObjectMapper.class);

    private JSON() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot Be Accessed!");
    }

    public static <T> T toObj(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException var3) {
            throw new JsonException("error.jackson.read", var3);
        }
    }

    public static <T> List<T> toArray(String json, Class<T> clazz) {
        try {
            CollectionType type = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(json, type);
        } catch (IOException var3) {
            throw new JsonException("error.jackson.read", var3);
        }
    }

    public static <T> String toJson(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new JsonException("error.jackson.write", e);
        }
    }

    public static <T> String mapToJson(T obj) {
        try {
            objectMapper.setSerializationInclusion(Include.ALWAYS);
            String json = objectMapper.writeValueAsString(obj);
            objectMapper.setSerializationInclusion(Include.USE_DEFAULTS);
            return json;
        } catch (IOException e) {
            throw new JsonException("error.jackson.write", e);
        }
    }
}