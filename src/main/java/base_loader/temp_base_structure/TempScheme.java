package base_loader.temp_base_structure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TempScheme implements TempEntity, TempEntityContainer<TempTable> {

    private String name;
    private List<TempTable> tables;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = Optional.ofNullable(name).orElse("");
    }

    @NotNull
    public List<@NotNull TempTable> getTables() {
        return tables;
    }

    public void setTables(@Nullable List<@NotNull TempTable> tables) {
        this.tables = Optional.ofNullable(tables).orElse(new ArrayList<>());
    }

    @NotNull
    @Override
    public String getTempEntityName() {
        return getName();
    }

    @NotNull
    @Override
    public List<TempTable> getTempEntities() {
        return getTables();
    }
}
