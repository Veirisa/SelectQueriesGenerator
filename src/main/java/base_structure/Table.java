package base_structure;

import org.jetbrains.annotations.NotNull;
import base_loader.temp_base_structure.TempTable;

import java.util.List;
import java.util.stream.Collectors;

public class Table  {

    private String name;
    private List<Column> columns;

    public Table(@NotNull TempTable tempTable) {
        name = tempTable.getName();
        columns = tempTable.getColumns().stream().map(Column::new).collect(Collectors.toList());
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public List<@NotNull Column> getColumns() {
        return columns;
    }
}
