import base_loader.temp_base_checker.BaseStructureException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(@NotNull String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Wrong count of arguments - 1 argument with base file path expected");
        }
        String baseFilePath = args[0];
        try {
            SelectQueriesGenerator generator = new SelectQueriesGenerator(new File(baseFilePath));
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String tablePattern = scanner.nextLine();
                String query = scanner.nextLine();
                boolean caseSensitive = Boolean.parseBoolean(scanner.nextLine());
                List<String> selects = generator.generateSelects(tablePattern, query, caseSensitive);
                System.out.println("Generated SELECT queries (" + selects.size() + "):");
                System.out.println("----------------------------");
                for (String select : selects) {
                    System.out.println(select);
                    System.out.println("----------------------------");
                }
                System.out.println();
            }
        } catch (FileNotFoundException | BaseStructureException e) {
            e.printStackTrace();
        }
    }
}
