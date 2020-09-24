package cn.jupitermouse.lineage.graph.domain.model;

import java.util.Arrays;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 使用数据源的标准名称
 * </p>
 *
 * @author JupiterMouse 2020/09/22
 * @since 1.0
 */
public class QualifiedName {

    /**
     * cluster  由databaseCode 和 tenantId组成
     */
    private final String clusterName;
    /**
     * = databaseName
     */
    private final String catalogName;
    /**
     * 对于mysql，可看作schema为null
     */
    private final String schemaName;
    /**
     * table & view
     */
    private final String tableName;
    /**
     * field & 分区 & 分布键
     */
    private final String fieldName;
    private String qualifiedName;
    private Type type;

    private QualifiedName(
            @NonNull final String clusterName,
            final String catalogName,
            final String schemaName,
            final String tableName,
            final String fieldName) {
        this.clusterName = standardizeRequired("clusterName", clusterName);
        this.catalogName = standardizeOptional(catalogName, true);
        this.schemaName = standardizeOptional(schemaName, true);
        this.tableName = standardizeOptional(tableName, true);
        this.fieldName = standardizeOptional(fieldName, true);
//        if (StringUtils.isNotEmpty(viewName)) {
//            type = Type.MVIEW;
//        } else if (StringUtils.isNotEmpty(fieldName)) {
//            type = Type.Field;
//        } else if (StringUtils.isNotEmpty(tableName)) {
//            type = Type.TABLE;
//        } else if (StringUtils.isNotEmpty(databaseName)) {
//            type = Type.DATABASE;
//        } else {
//            type = Type.CATALOG;
//        }

    }

    private QualifiedName(
            @NonNull final String clusterName,
            @NonNull final String catalogName,
            @Nullable final String schemaName,
            @Nullable final String tableName,
            @Nullable final String fieldName,
            @NonNull final Type type) {
        this.clusterName = clusterName;
        this.catalogName = catalogName;
        this.schemaName = schemaName;
        this.fieldName = fieldName;
        this.tableName = tableName;
        this.type = type;
    }

    public static QualifiedName fromString(@NonNull final String s) {
        final String name = s.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("passed in an empty definition name");
        }

        final String[] parts = name.split("/", 5);
        String clusterName = parts[0];
        switch (parts.length - 1) {
            case 1:
                return ofCatalog(clusterName, parts[1]);
            case 2:
                return ofSchema(clusterName, parts[1], parts[2]);
            case 3:
                return ofTable(clusterName, parts[1], parts[2], parts[3]);
            case 4:
                return ofField(clusterName, parts[1], parts[2], parts[3], parts[4]);
            default:
                throw new IllegalArgumentException("Unable to convert '" + s + "' into a qualifiedDefinition");
        }
    }

    private static String standardizeOptional(@Nullable final String value, final boolean forceLowerCase) {
        if (value == null) {
            return "";
        } else {
            String returnValue = value.trim();
            if (forceLowerCase) {
                returnValue = returnValue.toLowerCase();
            }
            return returnValue;
        }
    }

    private static String standardizeRequired(final String name, @Nullable final String value) {
        if (value == null) {
            throw new IllegalStateException(name + " cannot be null");
        }

        final String returnValue = value.trim();
        if (returnValue.isEmpty()) {
            throw new IllegalStateException(name + " cannot be an empty string");
        }

        return returnValue.toLowerCase();
    }

    public static QualifiedName ofCatalog(@NonNull final String clusterName,
            @NonNull final String catalogName) {
        return new QualifiedName(clusterName, catalogName, null, null, null);
    }
    public static QualifiedName ofSchema(
            @NonNull final String clusterName,
            @NonNull final String catalogName,
            @NonNull final String schemaName) {
        return new QualifiedName(clusterName, catalogName, schemaName, null, null);
    }

    public static QualifiedName ofTable(
            @NonNull final String clusterName,
            @NonNull final String catalogName,
            @NonNull final String databaseName,
            @NonNull final String tableName
    ) {
        return new QualifiedName(clusterName, catalogName, databaseName, tableName, null);
    }

    public static QualifiedName ofField(
            @NonNull final String clusterName,
            @NonNull final String catalogName,
            @NonNull final String databaseName,
            @NonNull final String tableName,
            @NonNull final String fieldName
    ) {
        return new QualifiedName(clusterName, catalogName, databaseName, tableName, fieldName);
    }


    /**
     * Type of the connector resource.
     */
    public enum Type {
        /**
         * Catalog type.
         */
        CATALOG("^([^\\/]+)$"),

        /**
         * Database type.
         */
        DATABASE("^([^\\/]+)\\/([^\\/]+)$"),

        /**
         * Table type.
         */
        TABLE("^([^\\/]+)\\/([^\\/]+)\\/([^\\/]+)$"),

        /**
         * Field type.
         */
        Field("^(.*)$"),

        MVIEW("^([^\\/]+)\\/([^\\/]+)\\/([^\\/]+)\\/([^\\/]+)$");

        private final String regexValue;

        Type(final String value) {
            this.regexValue = value;
        }

        public String getRegexValue() {
            return regexValue;
        }

        public static Type fromValue(final String value) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException(
                    "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
        }
    }

    @Override
    public String toString() {
        if (qualifiedName == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(clusterName);
            sb.append('/').append(catalogName);

            if (StringUtils.isNotEmpty(tableName)) {
                sb.append('/');
            }
            if (StringUtils.isNotEmpty(schemaName)) {
                sb.append(schemaName);
            }
            if (StringUtils.isNotEmpty(tableName)) {
                sb.append('/').append(tableName);
            }
            if (StringUtils.isNotEmpty(fieldName)) {
                sb.append('/').append(fieldName);
            }
            qualifiedName = sb.toString();
        }
        return qualifiedName;
    }
}
