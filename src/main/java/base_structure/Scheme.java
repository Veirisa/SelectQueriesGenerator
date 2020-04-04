package base_structure;

import org.jetbrains.annotations.NotNull;
import base_loader.temp_base_structure.TempScheme;

import java.util.List;
import java.util.stream.Collectors;

public class Scheme {

    private String name;
    private List<Table> tables;

    public Scheme(@NotNull TempScheme tempScheme)  {
        name = tempScheme.getName();
        tables = tempScheme.getTables().stream().map(Table::new).collect(Collectors.toList());
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public List<@NotNull Table> getTables() {
        return tables;
    }
}
