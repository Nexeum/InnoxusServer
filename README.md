# Innoxus

## Description

This project is an implementation of Docker-in-Docker (DinD). It is used to create, manage, and manipulate Docker containers within another Docker container. This is useful for situations where an isolated Docker environment is needed.

## Technologies

This project is developed using the following technologies:

- Java with Spring Boot
- Go with Revel
- Gradle for dependency management

## Setting Up the Development Environment

### Requirements

- Java version 17
- Go version 1.22

### Setup

1. Clone the repository
2. Navigate to the project directory
3. Install the dependencies: `gradle build` (for the Java project) and `go mod download` (for the Go project)

## Running the Project

### Java with Spring Boot

Run the project with the following command: `./gradlew bootRun`

### Go with Revel

Run the project with the following command: `revel run`