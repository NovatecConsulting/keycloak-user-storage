![](https://github.com/nt-ca-aqe/keycloak-user-storage/workflows/Java%20CI%20on%20REST%20version/badge.svg)
# Keycloak User Storage SPI demo
This is a demonstration on how to connect keycloak to a out-of-the-box unsupported user storage type/format. (For demonstration purposes an external MySQL database)

## Setup

### Requirements
* Current version of Java installed (preferable JDK11 or 13 from https://adoptopenjdk.net/)
* Current version of Docker installed (for the MySQL server)
* A Java IDE (like [Eclipse](https://www.eclipse.org/downloads/), [SpringToolSuite](https://spring.io/tools), [IntelliJ](https://www.jetbrains.com/idea/download), [Visual Studio Code](https://code.visualstudio.com/))
* [Postman](https://www.getpostman.com/downloads/) to test requests to the REST Api
* A keycloak instance

### Getting Started
The process to get this running is due to its architecture split in several parts. The first part is to get the user storage SPI deployed to Keycloak. 

The second part is to run the RESTful service to provide access to the database. This also requires a database instance to be running.

The final part is to configure the deployed user storage SPI to interact with the started RESTful service. This part is only necessary because the plugin can this way support multiple different RESTful services and thereby multiple databases. This allows multi-tenancy at runtime by only deploying **one** user storage SPI instance instead of hardcoded variants for every tenant.

##### 

#### Importing the project
1. Clone this git repository or download it as zip file
2. Import the whole directory into your Java IDE

#### Steps to deploy the user storage provider
1. Execute `./gradlew jar` and wait till it's finished processing
2. Copy the `keycloak-user-store-1.0.0-SNAPSHOT.jar` from your `./build/libs/`-folder to `<pathToKeycloak>/standalone/deployments/`
3. WildFly (= application server of keycloak) should now automatically deploy the JAR-file and make it available in Keycloak (Providing that keycloak is running)

####  

## Reference Documentation

For further reference, please consider the following sections:

* [Official Keycloak SPI documentation](https://www.keycloak.org/docs/latest/server_development/index.html#_user-storage-spi)
