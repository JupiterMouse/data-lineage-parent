package cn.site.jupitermouse.lineage.graph.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

/**
 * <p>
 * Use the standard name of the neo4j node id
 * </p>
 *
 * @author JupiterMouse 2020/9/28
 * @since 1.0
 */
public class NodeQualifiedName {

    private final String platformName;
    /**
     * cluster pattern: tenantId_databaseCode or custom
     */
    private final String clusterName;
    /**
     * database & catalog.schema
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

    private NodeQualifiedName(String platformName,
                              String clusterName,
                              String schemaName,
                              String tableName,
                              String fieldName) {
        this.platformName = standardizeRequired("platformName", platformName);
        this.clusterName = standardizeOptional(clusterName);
        this.schemaName = standardizeOptional(schemaName);
        this.tableName = standardizeOptional(tableName);
        this.fieldName = standardizeOptional(fieldName);
    }

    public static NodeQualifiedName fromString(@NonNull final String qualifiedName) {
        final String name = qualifiedName.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("passed in an empty definition name");
        }
        // DEFAULT/0_mysql-test/hdsp/iam_user/id
        // DEFAULT/0_pg-test/hdsp.dim/iam_user/id
        final String[] parts = name.split("/", 5);
        String platformName = parts[0];
        String clusterName = parts[1];
        switch (parts.length) {
            case 2:
                return ofSchema(platformName, clusterName, parts[1]);
            case 3:
                return ofTable(platformName, clusterName, parts[1], parts[2]);
            case 4:
                return ofField(platformName, clusterName, parts[1], parts[2], parts[3]);
            default:
                throw new IllegalArgumentException("Unable to convert '" + qualifiedName + "' into a qualifiedDefinition");
        }
    }

    private static String standardizeOptional(final String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    private static String standardizeRequired(String name, String value) {
        if (value == null) {
            value = "DEFAULT";
        }
        final String returnValue = value.trim();
        if (returnValue.isEmpty()) {
            throw new IllegalStateException(name + " cannot be an empty");
        }
        return returnValue.toLowerCase();
    }

    public static NodeQualifiedName ofPlatform(@NonNull final String platformName) {
        return new NodeQualifiedName(platformName, null, null, null, null);
    }

    public static NodeQualifiedName ofCluster(@NonNull final String platformName,
                                              @NonNull final String clusterName) {
        return new NodeQualifiedName(platformName, clusterName, null, null, null);
    }

    public static NodeQualifiedName ofSchema(@NonNull final String platformName,
                                             @NonNull final String clusterName,
                                             @NonNull final String schemaName) {
        return new NodeQualifiedName(platformName, clusterName, schemaName, null, null);
    }

    public static NodeQualifiedName ofTable(@NonNull final String platformName,
                                            @NonNull final String clusterName,
                                            @NonNull final String schemaName,
                                            @NonNull final String tableName) {
        return new NodeQualifiedName(platformName, clusterName, schemaName, tableName, null);
    }

    public static NodeQualifiedName ofField(@NonNull final String platformName,
                                            @NonNull final String clusterName,
                                            @NonNull final String schemaName,
                                            @NonNull final String tableName,
                                            @NonNull final String fieldName) {
        return new NodeQualifiedName(platformName, clusterName, schemaName, tableName, fieldName);
    }

    @Override
    public String toString() {
        if (qualifiedName == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(platformName);
            if (StringUtils.isNotEmpty(clusterName)) {
                sb.append("/").append(clusterName);
            }
            if (StringUtils.isNotEmpty(schemaName)) {
                sb.append('/').append(schemaName);
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
