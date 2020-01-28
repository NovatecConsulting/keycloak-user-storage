![](https://github.com/nt-ca-aqe/keycloak-user-storage/workflows/Java%20CI%20on%20REST%20version/badge.svg)
# Keycloak User Storage SPI demo
This is a demonstration on how to connect keycloak to a out-of-the-box unsupported user storage type/format. (For demonstration purposes an external MySQL database)

The solution demonstrated in this branch uses a RESTful API to provide HTTP-based access to the database. This is easier to scale and to develop with the user storage SPI. This is due to the fact that JPA connections are not meant to be built at runtime.

This way you can deploy MySQL connections independently to the Keycloak server. That means you can add federation clients at runtime with ease and without restarting your Keycloak server.

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

##### Deployment

#### Steps to deploy the user storage provider
1. Switch to the `UserStorageSPI`-folder
2. Execute `./gradlew jar` and wait till it's finished processing
3. Copy the `keycloak-user-store-1.0.0-SNAPSHOT.jar` from your `./build/libs/`-folder to `<pathToKeycloak>/standalone/deployments/`
4. WildFly (= application server of keycloak) should now automatically deploy the JAR-file and make it available in Keycloak (Providing that keycloak is running)

#### Steps to start the RESTful service
1. Switch to the `DemoMySQLKeycloakAPI`
2. Start a MySQL container with docker: `docker run --name mysql-jee -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql`
    ```diff
   - Attention: This setup is not production-ready as your MySQL server can easily be accessible by anyone!
   ```
3. Create a realm called `Testrealm` or reconfigure the `application.properties` for the matching issuer URL
4. Start the RESTful service: `./gradlew bootRun`
5. Your RESTful service is now available on http://localhost:31000

#### Configure the user storage provider for your realm in keycloak
![alt text](img/keycloak-userstorage-config.png)
1. The name you want to be displayed by the user when it's a federated user. (and in the logs if something fails)
2. The URL to your RESTful service
3. The URL to the token endpoint of your keycloak realm used to authorize the access to the RESTful API
4. Client credentials to use the OAuth 2.0 client credentials grant. (Authentication of the deployed user storage SPI to your RESTful service)


## Reference Documentation

For further reference, please consider the following sections:

* [Official Keycloak SPI documentation](https://www.keycloak.org/docs/latest/server_development/index.html#_user-storage-spi)
