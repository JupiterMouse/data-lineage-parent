package cn.jupitermouse.lineage.parser.durid.process;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * SqlExprContent
 * </p>
 *
 * @author JupiterMouse 2020/09/11
 * @since 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlExprContent {

    /**
     * items 中的字段
     */
    private String owner;

    private String name;

    private List<SqlExprContent> items = new ArrayList<>();

    public boolean isFirst() {
        return name == null;
    }

    public void addItem(SqlExprContent content) {
        if (isFirst()) {
            this.owner = content.owner;
            this.name = content.name;
        } else {
            items.add(content);
        }
    }

    public List<SqlExprContent> getAllItems() {
        List<SqlExprContent> list = new ArrayList<>();
        list.add(SqlExprContent.builder().owner(this.owner).name(this.name).build());
        list.addAll(items);
        return list;
    }

    public boolean isEmptyChildren() {
        return items == null || items.size() == 0;
    }

    public static SqlExprContent of() {
        return new SqlExprContent();
    }
}
