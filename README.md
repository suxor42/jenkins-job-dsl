# Gradle project to create jenkins job via jenkins jod-dsl

## Requierements
* Groovy SDK
* Gradle
* job-dsl plugin on Jenkins

## Running on jenkins
 To run the script on jenkins:
 1. create a gradle build step with the following tasks `clean test libs config_setup`
 1. create a job-dsl build step with:
 
| Setting | Value |
| ------- | ----- |
| DSL scripts | jobs/*.groovy |
| Additional classpath | libs/*.jar |
