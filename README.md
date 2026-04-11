# Virus Breach
### CMPT 276 — Group 15

A 2D tile-based hacking game built with JavaFX.
Navigate the board, collect data, find the decryption key, and escape through the exit while avoiding firewalls and the antivirus enemy.

## Requirements

- Java 25
- Maven 3.8+

## Build

```shell
mvn package -Dmaven.test.skip=true
```

This compiles the project and produces a self-contained executable jar at `target/virusbreach-1.0-SNAPSHOT.jar`.

## Run

**Option 1 — Maven (recommended during development):**
```shell
mvn javafx:run
```

**Option 2 — Run the jar directly:**
```shell
mvn package -Dmaven.test.skip=true
java -jar target/virusbreach-1.0-SNAPSHOT.jar
```

## Test

Run the full test suite:
```shell
mvn test
```

Run a specific test class:
```shell
mvn test -Dtest=PositionTest
```

## Code Coverage

Generate a JaCoCo HTML coverage report:
```shell
mvn clean test jacoco:report -Dmaven.test.failure.ignore=true
open target/site/jacoco/index.html
```

## Documentation

Generate Javadoc documentation:
```shell
mvn javadoc:javadoc
open target/reports/apidocs/index.html
```
