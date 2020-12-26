# game-common

Contains game code shared by client and server

## Packaging the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `game-common-1.0.0-SNAPSHOT-runner.jar` file in the `/build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```