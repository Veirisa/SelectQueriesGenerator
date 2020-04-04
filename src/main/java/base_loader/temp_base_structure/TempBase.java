package base_loader.temp_base_structure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TempBase implements TempEntityContainer<TempScheme> {

    private List<TempScheme> schemas;

    @NotNull
    public List<@NotNull TempScheme> getSchemas() {
        return schemas;
    }

    public void setSchemas(@Nullable List<@NotNull TempScheme> schemas) {
        this.schemas = Optional.ofNullable(schemas).orElse(new ArrayList<>());
    }

    @NotNull
    @Override
    public List<@NotNull TempScheme> getTempEntities() {
        return getSchemas();
    }
}
