import base_loader.temp_base_checker.BaseStructureException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import base_structure.*;
import base_loader.BaseLoader;
import typed_query.TypedQuery;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SelectQueriesGenerator {

    private Base base;

    SelectQueriesGenerator(@NotNull File yamlFile) throws FileNotFoundException, BaseStructureException {
        BaseLoader loader = new BaseLoader(yamlFile);
        base = loader.loadBase();
    }

    @NotNull
    private List<@NotNull Table> getTablesForPattern(@NotNull String tablePattern) {
        Pattern compiledTablePattern = Pattern.compile(tablePattern);
        return base.getSchemas()
                .stream()
                .map(scheme -> scheme.getTables()
                        .stream()
                        .filter(table -> compiledTablePattern.matcher(scheme.getName() + "." + table.getName()).matches())
                )
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    /**
     * tablePattern - regexp of qualified table name
     *     e.g. "sakila\.actor ", "sakila\..*", ".*\.person"
     * query - text to find in database e.g. "Alice", "42", "true"
     * caseSensitive - whether to use LIKE or ILIKE operation for varchar columns
     */
    @NotNull
    public List<@NotNull String> generateSelects(@NotNull String tablePattern, @NotNull String query, boolean caseSensitive) {
        List<Table> matchedTables = getTablesForPattern(tablePattern);
        TypedQuery typedQuery = new TypedQuery(query);
        return matchedTables
                .stream()
                .map(table -> generateSelect(table, typedQuery, caseSensitive))
                .collect(Collectors.toList());
    }

    @NotNull
    private String generateSelect(@NotNull Table table, @NotNull TypedQuery typedQuery, boolean caseSensitive) {
        List<String> predicates = table.getColumns()
                .stream()
                .map(column -> generatePredicate(column, typedQuery, caseSensitive))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        String wherePart = predicates.isEmpty() ? "" : "\nWHERE " + String.join("\n\tOR ", predicates);
        return "SELECT * FROM " + table.getName() + wherePart;
    }

    @Nullable
    private String generatePredicate(@NotNull Column column, @NotNull TypedQuery typedQuery, boolean caseSensitive) {
        Type type = column.getType();
        String typedQueryRepresentation = typedQuery.getRepresentation(type);
        if (typedQueryRepresentation == null) {
            return null;
        }
        String comparingOperator = generateComparingOperator(type, caseSensitive);
        return column.getName() + " " + comparingOperator + " " + typedQueryRepresentation;
    }

    @NotNull
    private String generateComparingOperator(@NotNull Type type, boolean caseSensitive) {
        if (type == Type.VARCHAR) {
            if (caseSensitive) {
                return "ILIKE";
            } else {
                return "LIKE";
            }
        }
        return "=";
    }
}
