# tools.wscat

[![Build Status][build-status-image]][build-status]

[build-status-image]: https://travis-ci.org/kaazing/tools.wscat.svg?branch=develop
[build-status]: https://travis-ci.org/kaazing/tools.wscat

# About this Project

Project for 'cat' over websocket 

# Building this Project

## Minimum requirements for building the project
* Java Developer Kit (JDK) or Java Runtime Environment (JRE) Java 7 (version 1.7.0_21) or higher
* Maven 3.0.5

## Steps for building this project
0. mvn clean install

# Running this Project

(List steps to run this project.)

0. `mvn clean verify`
1. `cd target` 
2. `java -jar wscat.jar <url>`  (for example `java -jar wscat.jar ws://echo.websocket.org/`)
