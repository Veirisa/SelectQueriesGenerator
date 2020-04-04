import base_loader.temp_base_checker.BaseStructureException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SelectQueriesGeneratorTest {

    static private final String yamlPath = "src/test/yaml/";

    static private SelectQueriesGenerator generator;

    @Nullable
    private String getBaseStructureException(@NotNull File testBaseYaml) {
        try {
            new SelectQueriesGenerator(testBaseYaml);
        } catch (BaseStructureException e) {
            return e.getMessage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @BeforeAll
    static void createCorrectTestGenerator() {
        File correctTestBaseYaml = new File(yamlPath + "correct/test_base.yaml");
        try {
            generator = new SelectQueriesGenerator(correctTestBaseYaml);
        } catch (FileNotFoundException | BaseStructureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void emptyResultTest() {
        List<String> emptySchemeSelects =
                generator.generateSelects("empty_scheme\\..*", "", false);
        Assertions.assertEquals(0, emptySchemeSelects.size());

        List<String> emptyBaseSelects =
                generator.generateSelects(".*\\.empty_base", "", false);
        Assertions.assertEquals(1, emptyBaseSelects.size());
        Assertions.assertEquals("SELECT * FROM empty_base", emptyBaseSelects.get(0));
    }

    @Test
    public void tablePatternTest() {
        List<String> tablePatternSelects;
        Iterator<String> selectsIterator;

        tablePatternSelects = generator.generateSelects(".*\\..+1.*", "", true);
        Assertions.assertEquals(3, tablePatternSelects.size());
        selectsIterator = tablePatternSelects.iterator();
        Assertions.assertEquals("SELECT * FROM table11", selectsIterator.next());
        Assertions.assertEquals("SELECT * FROM table12", selectsIterator.next());
        Assertions.assertEquals("SELECT * FROM table21_key", selectsIterator.next());

        tablePatternSelects = generator.generateSelects(".+2.*\\..*", "", true);
        Assertions.assertEquals(2, tablePatternSelects.size());
        selectsIterator = tablePatternSelects.iterator();
        Assertions.assertEquals("SELECT * FROM table21_key", selectsIterator.next());
        Assertions.assertEquals("SELECT * FROM table22", selectsIterator.next());

        tablePatternSelects = generator.generateSelects(".*key.*", "", true);
        Assertions.assertEquals(3, tablePatternSelects.size());
        selectsIterator = tablePatternSelects.iterator();
        Assertions.assertEquals("SELECT * FROM table11", selectsIterator.next());
        Assertions.assertEquals("SELECT * FROM table12", selectsIterator.next());
        Assertions.assertEquals("SELECT * FROM table21_key", selectsIterator.next());
    }

    @Test
    public void varcharTest() {
        String expectedVarcharSelectTemp = "SELECT * FROM actor"
                + "\nWHERE first_name LIKE '%s'"
                + "\n\tOR last_name LIKE '%<s'";

        List<String> emptyQuerySelects =
                generator.generateSelects(".*\\.actor", "", false);
        Assertions.assertEquals(1, emptyQuerySelects.size());
        Assertions.assertEquals(String.format(expectedVarcharSelectTemp, "%"), emptyQuerySelects.get(0));

        List<String> varcharSelects =
                generator.generateSelects(".*\\.actor", "query", false);
        Assertions.assertEquals(1, varcharSelects.size());
        Assertions.assertEquals(String.format(expectedVarcharSelectTemp, "%query%"), varcharSelects.get(0));

        List<String> shieldingSelects =
                generator.generateSelects(".*\\.actor", "q\\u%e_r'y", false);
        Assertions.assertEquals(1, shieldingSelects.size());
        Assertions.assertEquals(String.format(expectedVarcharSelectTemp, "%q\\\\u\\%e\\_r\\'y%"), shieldingSelects.get(0));
    }

    @Test
    public void booleanTest() {
        String expectedBooleanSelectTemp = "SELECT * FROM actor"
                + "\nWHERE first_name ILIKE '%%%s%%'"
                + "\n\tOR last_name ILIKE '%%%<s%%'"
                + "\n\tOR has_kids = %<s";

        for (String query : Arrays.asList("true", "false")) {
            List<String> booleanTestSelects =
                    generator.generateSelects(".*\\.actor", query, true);
            Assertions.assertEquals(1, booleanTestSelects.size());
            Assertions.assertEquals(String.format(expectedBooleanSelectTemp, query), booleanTestSelects.get(0));
        }
    }

    @Test
    public void integerTest() {
        String expectedIntegerSelectTemp = "SELECT * FROM actor"
                + "\nWHERE actor_id = %s"
                + "\n\tOR first_name LIKE '%%%<s%%'"
                + "\n\tOR last_name LIKE '%%%<s%%'";

        for (String query : Arrays.asList("42", "-42")) {
            List<String> integerTestSelects =
                    generator.generateSelects(".*\\.actor", query, false);
            Assertions.assertEquals(1, integerTestSelects.size());
            Assertions.assertEquals(String.format(expectedIntegerSelectTemp, query), integerTestSelects.get(0));
        }
    }

    @Test
    public void dateTest() {
        String expectedIncorrectDateSelectTemp = "SELECT * FROM actor"
                + "\nWHERE first_name ILIKE '%%%s%%'"
                + "\n\tOR last_name ILIKE '%%%<s%%'";
        String expectedCorrectDateSelectTemp = expectedIncorrectDateSelectTemp
                + "\n\tOR last_update = '%<s'";

        String correctDateQuery = "2016-02-15";
        List<String> correctDateTestSelects =
                generator.generateSelects(".*\\.actor", correctDateQuery, true);
        Assertions.assertEquals(1, correctDateTestSelects.size());
        Assertions.assertEquals(String.format(expectedCorrectDateSelectTemp, correctDateQuery), correctDateTestSelects.get(0));

        String incorrectDateQuery = "2016-02-30";
        List<String> incorrectDateTestSelects =
                generator.generateSelects(".*\\.actor", incorrectDateQuery, true);
        Assertions.assertEquals(1, incorrectDateTestSelects.size());
        Assertions.assertEquals(String.format(expectedIncorrectDateSelectTemp, incorrectDateQuery), incorrectDateTestSelects.get(0));
    }

    @Test
    public void bigTest() {
        List<String> bigTestSelects =
                generator.generateSelects("(sakila|store)\\..*", "0", false);
        Assertions.assertEquals(4, bigTestSelects.size());
        Iterator<String> selectsIterator = bigTestSelects.iterator();
        Assertions.assertEquals("SELECT * FROM actor"
                + "\nWHERE actor_id = 0"
                + "\n\tOR first_name LIKE '%0%'"
                + "\n\tOR last_name LIKE '%0%'",
                selectsIterator.next());
        Assertions.assertEquals("SELECT * FROM address"
                + "\nWHERE address_id = 0"
                + "\n\tOR city_id = 0",
                selectsIterator.next());
        Assertions.assertEquals("SELECT * FROM product"
                + "\nWHERE product_id = 0"
                + "\n\tOR product_name LIKE '%0%'",
                selectsIterator.next());
        Assertions.assertEquals("SELECT * FROM category"
                + "\nWHERE category_id = 0"
                + "\n\tOR category_name LIKE '%0%'",
                selectsIterator.next());
    }

    @Test
    public void incorrectBaseStructureTest() {
        String incorrectTestBasePathTemp = yamlPath + "/incorrect/%s_test_base.yaml";

        Assertions.assertEquals(
                "TempScheme with incorrect name: 'dot.name'",
                getBaseStructureException(new File(String.format(incorrectTestBasePathTemp, "incorrect_entity_name")))
        );

        Assertions.assertEquals(
                "TempColumn with incorrect name: ''",
                getBaseStructureException(new File(String.format(incorrectTestBasePathTemp, "empty_entity_name")))
        );

        Assertions.assertEquals(
                "TempScheme contains TempTable with same names: 'duplicate'",
                getBaseStructureException(new File(String.format(incorrectTestBasePathTemp, "duplicate_entity_name")))
        );

        Assertions.assertEquals(
                "TempColumn with incorrect type: 'varchar(-45)'",
                getBaseStructureException(new File(String.format(incorrectTestBasePathTemp, "incorrect_type")))
        );
    }
}
