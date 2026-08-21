# MiniSpring

A lightweight IoC container implemented from scratch in Java to develop a deeper understanding of the mechanisms underlying Spring's dependency injection and bean management.

## Overview

MiniSpring explores the core mechanisms behind an IoC container using Java reflection. The project currently supports:

* Component registration and retrieval
* Singleton bean management
* Constructor-based dependency injection
* Component scanning and recursive package scanning
* Interface dependency resolution
* Dependency validation

The project is intentionally kept small and focuses on understanding the underlying mechanisms rather than replicating the full Spring framework.

## Architecture

At a high level:

```text
@Component Classes
       ↓
Component Scanner
       ↓
ApplicationContext
       ↓
Dependency Resolution
       ↓
Bean Instantiation
       ↓
Bean Registry
```

`ApplicationContext` acts as the IoC container, responsible for discovering components, resolving dependencies, creating objects, and managing their lifecycle within the context.

## Testing

The project uses JUnit 5 to test the behaviour of the container, including:

* Bean registration and retrieval
* Singleton behaviour
* Constructor injection
* Recursive component scanning
* Interface dependency resolution
* Invalid dependency configurations

Test-specific components are isolated under `src/test/java`.

## Tech Stack

* Java
* Gradle
* JUnit 5
* Java Reflection API

