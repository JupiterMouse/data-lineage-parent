package cn.jupitermouse.lineage.main.app;

import cn.jupitermouse.lineage.graph.domain.model.QualifiedName;
import org.junit.Test;

/**
 * <p>
 *
 * </p>
 *
 * @author JupiterMouse 2020/09/23
 * @since 1.0
 */
public class QualifiedNameTest {

    private final Long tenantId = 0L;
    private final String datasourceCode = "hdsp";

    @Test
    public void testCatalog() {
        QualifiedName qualifiedName = QualifiedName.ofCatalog(tenantId + datasourceCode, "c");
        System.out.println(qualifiedName.toString());
        System.out.println(qualifiedName);
        QualifiedName qualifiedName1 = QualifiedName.fromString(qualifiedName.toString());
        System.out.println();
    }

}
