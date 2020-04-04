# SELECT queries generation

*`SelectQueriesGenerator`* - генератор SELECT запросов.

## Запуск

* *`mvn test`* - запуск тестов.
* *`mvn compile exec:java`* - запуск Main класса с файлом со структурой базы *`src/main/yaml/example_base.yaml`*.
* *`mvn compile exec:java -Dexec.args="<baseFilePath>"`* - запуск Main класса с заданным yaml файлом.

Main читает из входного потока по три строки (с *`tablePattern`*, *`query`*, *`caseSensitive`*) и пишет в выходной поток результат генерации в формате:

*`Generated SELECT queries (<countOfSelectQueries>):`*<br>
*`----------------------------`*<br>
*`<firstSelectQuery>`*<br>
*`----------------------------`*<br>
*`<secondSelectQuery>`*<br>
*`----------------------------`*<br>
*`...`*<br>
*`----------------------------`*<br>
*`<lastSelectQuery>`*<br>
*`----------------------------`*<br>
