# Fair Billing Application

## Table of Contents
- [Description](#description)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Setup and Run Instructions](#setup-and-run-instructions)
  - [Run via Docker](#or-run-via-docker)
- [Run tests](#running-tests)
- [Useful Docker Commands](#other-useful-docker-commands)

## Description
This application takes a log file, containing a list of user sessions in the following format `timestamp, username, action`:
```
  14:02:03 ALICE99 Start
  14:02:05 CHARLIE End
  14:02:34 ALICE99 End
  14:02:58 ALICE99 Start
```
and prints out a report of the users; containing the number of sessions, and the minimum possible total
duration of their sessions in seconds. For example, for the above data, the following will be printed out:
```
ALICE99 2 31
CHARLIE 1 2
```

**Notes**
- The data within the input log file is assumed to have been in chronological order.
- Records in the log file will be from within a single day.
- A user can have more than one session active concurrently. 
- Where there is an “End” with no possible matching start, the start time will be assumed to be the earliest time of any record in the file. 
- Where there is a “Start” with no possible matching “End”, the end time will be assumed to be the latest time of any record in the file. 
- Any Invalid: timestamps, username and/or Start/End marker will be silently ignored and not included in calculations.

## Project Structure
```
+---src
|   +---main
|   |   +---java
|   |   |   \---com
|   |   |       \---bt
|   |   |           \---fairbilling
|   |   |                   FairBilling.java
|   |   |                   UserSession.java
|   |   |
|   |   \---resources
|   |       |   application.properties
|   |       |
|   |       \---input
|   |               input.txt
|   \---test
|       +---java
|       |   \---com
|       |       \---bt
|       |           \---fairbilling
|       |                   FairBillingTest.java
|       |                   UserSessionTest.java
|       \---resources
|               validInput.txt
|   Dockerfile
|   mvnw
|   mvnw.cmd
|   pom.xml
|   README.md
```

## Requirements
- Java 8
- Docker

Maven wrapper provided. Alternatively you could use your own Maven version. 

## Setup and Run Instructions

### Run via Docker

Note: the Maven commands shown are using the Maven wrapper provided.

#### 1. Build the JAR file using Maven
```sh
./mvnw clean install
```
or for Windows
```sh
.\mvnw.cmd clean install
```

#### 2. Build the Docker image
```sh
docker build -t fairbilling .
```
#### 3. Run the Application

Ensure your input file is within the `src/main/resources/input` directory, see `input.txt` as an example. Then run the following command,replacing `input.txt with` the name of your file:
```sh
docker run fairbilling /logs/input.txt
```
### Running Tests
```sh
./mvnw test
```
or for Windows
```sh
.\mvnw.cmd test
```
---
## Other Useful Docker commands

1. `docker ps` - list all the running containers
2. `docker stop <container-name>` - stop a running container
3. `docker rm <container-name>` - remove a container
4. `docker images` - list all the Docker images currently available on your machine
5. `docker rmi <image-name>` - remove a docker image
6. `docker logs <container-name>` - view docker logs for a given container