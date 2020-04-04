package base_loader.temp_base_structure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TempColumn implements TempEntity {

    private String name;
    private String type;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = Optional.ofNullable(name).orElse("");
    }

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = Optional.ofNullable(type).orElse("");
    }

    @NotNull
    @Override
    public String getTempEntityName() {
        return getName();
    }
}