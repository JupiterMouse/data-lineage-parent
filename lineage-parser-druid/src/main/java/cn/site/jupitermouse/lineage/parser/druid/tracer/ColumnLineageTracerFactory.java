package cn.site.jupitermouse.lineage.parser.druid.tracer;

/**
 * <p>
 * 字段血缘工厂
 * </p>
 *
 * @author JupiterMouse 2020/10/15
 * @since 1.0
 */
public class ColumnLineageTracerFactory {

    private ColumnLineageTracerFactory() {
    }

    /**
     * 获取默认的字段解析器
     *
     * @return ColumnLineageTracer
     */
    public static ColumnLineageTracer getDefaultTracer() {
        return new DefaultColumnLineageTracer();
    }

}
