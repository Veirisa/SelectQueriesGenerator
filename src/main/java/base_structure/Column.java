package base_structure;

import org.jetbrains.annotations.NotNull;
import base_loader.temp_base_structure.TempColumn;

public class Column {

    private String name;
    private Type type;

    public Column(@NotNull TempColumn tempColumn) {
        name = tempColumn.getName();
        type = convertToType(tempColumn.getType());
    }

    @NotNull
    private Type convertToType(@NotNull String type) {
        if (type.equals("boolean")) {
            return Type.BOOLEAN;
        }
        if (type.equals("integer")) {
            return Type.INTEGER;
        }
        if (type.equals("date")) {
            return Type.DATE;
        }
        if (type.matches("varchar\\(\\d+\\)")) {
            return Type.VARCHAR;
        }
        return Type.UNDEFINED;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Type getType() {
        return type;
    }
}