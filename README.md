## Ultimate AI's project
A chat bot which tries to take client message, tries to fetch intent from an AI server and then create a reply.

### Prerequisites
- Git
- Java 11
- MongoDB server: Local setup needed for testing. Application uses mongo db from the cloud   

### Setup project and run
Navigate to the base of the directory of 
```shell
git clone 'https://github.com/parasdiwan/ultimate-project.git'
cd ultimate-project
./gradlew clean build bootrun
```
Server starts at 8080 port

```shell
# For testing
$/ultimate-project$> ./gradlew test 
```
Sample request

```shell
curl --location --request POST 'localhost:8080/bots/1241/message' \
--header 'Content-Type: application/json' \
--data-raw '{
    "message": "hello"
}'
```

### Technologies used
- __Kotlin__: It is a more optimized and cleaner JVM language than Java. It takes the good features of Java and extends on it with its own features. Adding a lot of syntactic sugar which makes it cleaner language. It is fully interoperable with Java, hence, it is easier for Java engineers to port over to it. Target JDK: 11
- __MongoDB__: The most popular NoSQL document based database. It's also the choice of database for Ultimate AI
- __Spring-boot__: A light-weight version of spring which offers dependency inject and mvc framework capabilities. 
- __Gradle__:Build tool for JVM projects
- __JUnit5 and Mockito__: Testing and mocking framework
- __Flapdoodle__: Mocking mongodb 
- __OkHttp3__: Library for making HTTP calls to the intents AI service. Also provides a mocked web server for testing

### Assumptions
- __DB collection structure__: It is constructed as `ReplyByIntent` structure where intent is supposed to be unique and will have single replies for them. Even though the original structure would be different, the application should maintain this collection too for quick lookup of replies for intents. We can extend it to have multiple replies with extra metadata as mentioned in the schema.
- __Confidence threshold__: As discussed in the mail, confidence threshold should be a configurable value for the client/bot. This can be easily extended for the application. For default value, I concluded `0.8` as the best default measure, looking at the mentioned examples and responses from intents api.

### Things to improve
- __Error handling__: Better error handling, per error code to be done for the responses for the intents api. For now i left it because there won't be any behaviour change for the given information in the assignment.
- __Dockerized mongo for testing__: For integration tests, it would be better to use a container resembling production env. This is ensure that the tests are accurate.
- __Secrets handling__:Non-encoded secrets are exposed in the repo at the moment, which would be a security risk for production environments. This can be improved by using external configuration, secrets manager and encrypted keys.
-__Logging__:There's no logging framework defined. It can be easily introduced to enable debugging capabilities. 
