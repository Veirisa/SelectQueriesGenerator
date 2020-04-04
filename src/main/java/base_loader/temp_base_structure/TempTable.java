package base_loader.temp_base_structure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TempTable implements TempEntity, TempEntityContainer<TempColumn> {

    private String name;
    private List<TempColumn> columns;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = Optional.ofNullable(name).orElse("");
    }

    @NotNull
    public List<@NotNull TempColumn> getColumns() {
        return columns;
    }

    public void setColumns(@Nullable List<@NotNull TempColumn> columns) {
        this.columns =  Optional.ofNullable(columns).orElse(new ArrayList<>());
    }

    @NotNull
    @Override
    public String getTempEntityName() {
        return getName();
    }

    @NotNull
    @Override
    public List<TempColumn> getTempEntities() {
        return getColumns();
    }
}
