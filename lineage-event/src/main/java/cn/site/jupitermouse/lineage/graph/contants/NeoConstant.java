package cn.site.jupitermouse.lineage.graph.contants;

/**
 * <p>
 * Constant class
 * </p>
 *
 * @author JupiterMouse 2020/09/27
 * @since 1.0
 */
public class NeoConstant {

    private NeoConstant() {
    }

    public static final String ENTITY_EXTRA_PREFIX = "ext";
    public static final String ENTITY_PLATFORM_DEFAULT = "DEFAULT";
    public static final String ENTITY_CLUSTER_DEFAULT = "DEFAULT";

    public static class Node {
        public static final String DEFAULT_PLATFORM = "default";
        public static final String DEFAULT_CLUSTER = "default";
    }

    public static class RelationShip {
        private RelationShip() {
        }

        /**
         * Cluster -(CLUSTER_FROM_PLATFORM)->Platform
         */
        public static final String REL_CLUSTER_FROM_PLATFORM = "CLUSTER_FROM_PLATFORM";
        public static final String REL_SCHEMA_FROM_CLUSTER = "SCHEMA_FROM_CLUSTER";
        public static final String REL_TABLE_FROM_SCHEMA = "TABLE_FROM_SCHEMA";
        public static final String REL_CREATE_TABLE_AS_SELECT = "CREATE_TABLE_AS_SELECT";
        public static final String REL_INSERT_OVERWRITE_TABLE_SELECT = "INSERT_OVERWRITE_TABLE_SELECT";
        public static final String REL_FIELD_FROM_TABLE = "FIELD_FROM_TABLE";
        public static final String REL_PROCESS_INPUT = "PROCESS_INPUT";
        public static final String REL_PROCESS_OUTPUT = "PROCESS_OUTPUT";
        public static final String REL_CHILD_TAG = "HAVE_CHILD_TAG";
    }


    public static class Type {
        private Type() {
        }

        public static final String NODE_PLATFORM = "Platform";
        public static final String NODE_CLUSTER = "Cluster";
        public static final String NODE_SCHEMA = "Schema";
        public static final String NODE_TABLE = "Table";
        public static final String NODE_FIELD = "Field";
        public static final String NODE_PROCESS = "Process";
        public static final String NODE_TAG = "Tag";
    }

    public static class Status {
        private Status() {
        }

        public static final String ACTIVE = "ACTIVE";
        public static final String DELETED = "DELETED";
        public static final String DISABLE = "DISABLE";
    }

    public static class ProcessType {
        private ProcessType() {
        }

        public static final String TABLE_PROCESS = "TABLE";
        public static final String FIELD_PROCESS = "FIELD";
        public static final String PROPERTY_DISPATCH_JOB = "_job";
    }

    public static class LineageDirection {
        private LineageDirection() {
        }

        public static final String LINEAGE = "LINEAGE";
        public static final String IMPACT = "IMPACT";
        public static final String BOTH = "BOTH";
    }

    public static class PkType {
        private PkType() {
        }

        public static final String TABLE_PK_TYPE = "TABLE";
        public static final String FIELD_PK_TYPE = "FIELD";
    }

    public static class Index {
        private Index() {
        }

        public static final String PK = "pk";
        public static final String KEY = "_index";
    }

    public static class SourceType {
        public static final String SQL = "SQL";
        public static final String DATAX = "DATAX";
        public static final String SQOOP = "SQOOP";
        public static final String HIVE_HOOK = "HIVE-HOOK";
    }
}
