package typed_query;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import base_structure.Type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TypedQuery {

    private final List<String> requireShieldings = Arrays.asList("'", "%", "_");

    private String query;
    private Map<Type, String> representations = new HashMap<>();

    public TypedQuery(@NotNull String query) {
        this.query = query;
    }

    @Nullable
    public String getRepresentation(@NotNull Type type) {
        if (!representations.containsKey(type)) {
            representations.put(type, createRepsentation(type));
        }
        return representations.get(type);
    }

    @Nullable
    private String createRepsentation(@NotNull Type type) {
        if (isMatched(type)) {
            switch (type) {
                case VARCHAR:
                    if (query.isEmpty()) {
                        return "'%'"; // '%%' ~ '%', but preference is given to the shortest option
                    }
                    String varcharRepr = query.replace("\\", "\\\\");
                    for (String requireShielding : requireShieldings) {
                        varcharRepr = varcharRepr.replace(requireShielding, "\\" + requireShielding);
                    }
                    return "'%" + varcharRepr + "%'";
                case BOOLEAN:
                case INTEGER:
                    return query;
                case DATE:
                    return "'" + query + "'";
            }
        }
        return null;
    }

    private boolean isMatched(@NotNull Type type) {
        switch (type) {
            case VARCHAR:
                return true;
            case BOOLEAN:
                return query.equals("true") || query.equals("false");
            case INTEGER:
                return query.matches("-?\\d+");
            case DATE:
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.setLenient(false);
                    dateFormat.parse(query);
                    return true;
                } catch (ParseException e) {
                    return false;
                }
        }
        return false;
    }
}
