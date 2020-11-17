package cn.site.jupitermouse.lineage.graph.domain.model;

import java.util.Optional;

import cn.site.jupitermouse.lineage.graph.contants.NeoConstant;
import cn.site.jupitermouse.lineage.graph.domain.NodeQualifiedName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * <p>
 * Platfrom Node 基本存储字段
 * platformName  * </p>
 *
 * @author JupiterMouse 2020/09/28
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Builder
@NodeEntity(NeoConstant.Type.NODE_PLATFORM)
public class PlatformNode extends BaseNodeEntity {

    public PlatformNode(String platformName) {
        Optional.ofNullable(platformName).ifPresent(this::setPlatformName);
        String pk = NodeQualifiedName.ofPlatform(this.getPlatformName()).toString();
        this.setPk(pk);
        // display name
        this.setName(this.getPlatformName());
    }
}
