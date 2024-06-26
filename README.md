# Innoxus

## Description

This project is an implementation of Docker-in-Docker (DinD). It is used to create, manage, and manipulate Docker containers within another Docker container. This is useful for situations where an isolated Docker environment is needed.

## Architecture

The project follows two different architectural patterns for its two main components:

- The Go component with Revel follows the Model-View-Controller (MVC) pattern. This pattern separates the application into three interconnected components, allowing for efficient code organization and modular design.

<p align="center">
  <img width="357" alt="image" src="https://github.com/Nexeum/InnoxusServer/assets/54515356/6b3592cc-d0c4-4ad7-9dc3-172296d03dc4">
</p>

- The Java component with Spring Boot follows the Domain-Driven Design (DDD) and Hexagonal Architecture (also known as Ports and Adapters) patterns. DDD focuses on the core domain and domain logic, while Hexagonal Architecture aims to create loosely coupled application components.

<p align="center">
  <img width="357" alt="image" src="https://github.com/Nexeum/InnoxusServer/assets/54515356/537e5798-acd7-42f0-a39e-d449de2f4ce4">
</p>


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
