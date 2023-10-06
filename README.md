# Getting Started with Spring Logging Helper

Spring Logging Helper provides powerful and flexible logging capabilities for your application, with special features including JSON logging and filtering of request and response attributes.

Sample Code is [here](https://github.com/Camaine/spring-logging-helper/tree/main/src/main/java/org/springlogginghelper/sample)

## Prerequisites

To use this library, you'll need:

* Java 8 or higher.
* Spring Boot 2.7.3 or higher

## Installation

### Using Gradle or Maven

To include the Logging library in your Maven project, add the following to your pom.xml:
````
<dependency>
  <groupId>io.github.camaine</groupId>
  <artifactId>spring-logging-helper</artifactId>
  <version>1.0.0</version>
  <classifier>plain</classifier>
</dependency>
````

For Gradle projects, add the following to your build.gradle:
````
dependencies {
    implementation 'io.github.camaine:spring-logging-helper:1.0.0'
}
````

### Using Local JAR

If you're using Maven, you'll need to add the system path of your JAR in the dependencies section of your pom.xml:

````
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>spring-logging-helper</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/spring-logging-helper-1.0.0.jar</systemPath>
</dependency>
````
Here, replace com.yourcompany with your actual group ID, spring-logging-helper with the actual artifact ID of your library, 1.0.0 with the actual version of your library, and ${project.basedir}/lib/spring-logging-helper-1.0.0.jar with the actual path of your JAR file relative to the project base directory.
Gradle

For Gradle projects, you can directly add the JAR file as a file dependency. Add the following line to the dependencies section in your build.gradle:

````
dependencies {
    implementation files('lib/spring-logging-helper-1.0.0.jar')
}
````

Replace 'lib/spring-logging-helper-1.0.0.jar' with the actual path of your JAR file relative to the project base directory.

## Basic Usage

To enable logging, simply include the @EnableLogging annotation in your configuration file:
````
@EnableLogging
@Configuration
public class LoggingConfig extends LoggingConfiguration {
// Your additional configurations
}
````

This will automatically set up the necessary beans for the logging system.
You can customize the logging by excluding certain attributes from being logged. Here's how:
````
@EnableLogging
@Configuration
public class LoggingConfig extends LoggingConfiguration{
    public LoggingConfig(ConfigDetails configDetails) {
        super(configDetails);
    }

    @Bean
    public void attributeExclude() {
        //Single Attribute Exclude
        addExcludeRequest("testAttributeExclude");
        addExcludeResponse("test");
        addExcludeException(new RuntimeException());

        //Multi Attribute Exclude
        addExcludeRequestList(List.of("testAttributeExclude1", "testAttributeExclude2"));
        addExcludeResponseList(List.of("test1", "test2"));
        addExcludeExceptionList(List.of(new RuntimeException(), new IllegalArgumentException()));
    }

    @Bean
    public void addHeaderAttribute() {
        addIncludeHeader("tenant-context");
    }
}
````
In the LoggingConfig class, you can configure the attributes to be excluded from request and response logging, and the exceptions to be excluded from logging.

The addExcludeRequest, addExcludeResponse, addExcludeException methods can be used to exclude single attributes or exceptions, while the addExcludeRequestList, addExcludeResponseList, addExcludeExceptionList methods can be used to exclude multiple attributes or exceptions at once.

You can also include specific headers to be logged using the addIncludeHeader method.

Now your logging system will exclude "attributeName" from the requests and responses.
To log a method, simply annotate it with @Logging:
````
@Logging
@RequestMapping("/your-endpoint")
public String yourController(@RequestBody YourRequestBodyModel model) {
    // Your method implementation
}
````
To log a method with a exception message, use LoggingException.logException(e) in the catch block

````
@Logging
@RequestMapping("/testExceptionCatch")
public String testExceptionCatchEndpoint() {
    try {
        throw new RuntimeException("This is a test exception catch.");
    } catch(RuntimeException e) {
        LoggingException.logException(e);
    }
    return "test";
}
````

To log custom messages, use LoggingMessage.logMessage(message) method

````
@Logging
@EventListener
public void handleCustomEvent(CustomEvent event) {
    LoggingCustom.logCustom("Listener", event.getMessage());
}
````
