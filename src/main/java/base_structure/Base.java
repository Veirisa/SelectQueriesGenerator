package base_structure;

import org.jetbrains.annotations.NotNull;
import base_loader.temp_base_structure.TempBase;

import java.util.List;
import java.util.stream.Collectors;

public class Base {

    private List<Scheme> schemas;

    public Base(@NotNull TempBase tempBase) {
        schemas = tempBase.getSchemas().stream().map(Scheme::new).collect(Collectors.toList());
    }

    @NotNull
    public List<@NotNull Scheme> getSchemas() {
        return schemas;
    }

}
