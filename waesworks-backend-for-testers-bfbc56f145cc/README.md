# Backend For Testers

Ready-made project for a Tester/QA assignment.  
The detailed requirements for this assignment can be found at the root of this repo (Assignment for Backend Testers.pdf).  

## Requirements

- [Java 8+](https://www.oracle.com/technetwork/java/javase/downloads/index.html) JDK must be installed
- [Maven](https://maven.apache.org/download.cgi) must be installed

## Running the application

From the project root folder type in a terminal/command prompt/console:

`mvn clean spring-boot:run`

The services run on [http://localhost:8081](http://localhost:8081) by default.

You can change the port by adding the JVM property `server.port`.

Example:

`mvn clean spring-boot:run -Dserver.port=8888`

## Examples

### Swagger UI

The application integrates live documentation and examples once running.

You can access it at [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

Take into account that if you change the server port, you will need to access this URL using the changed port.

### Other examples

#### Retrieving information for one user

-  Request:
    - GET `http://localhost:8081/waesheroes/api/v1/users/details?username=dev`
-  Response body:  
```
            {
              "id": 2,
              "name": "Zuper Dooper Dev",
              "username": "dev",
              "email": "zd.dev@wearewaes.com",
              "superpower": "Debug a repellent factory storage.",
              "dateOfBirth": "1999-10-10",
              "isAdmin": false
            }
```

#### Retrieving information from all users

-  Request:
    - GET `http://localhost:8081/waesheroes/api/v1/users/all`  
    - Basic Auth: `username=admin` , `password=hero`

#### Login

-  Request:
    - GET `http://localhost:8081/waesheroes/api/v1/users/access`  
    - Basic Auth: `username=tester` , `password=maniac`

#### Sign up

-  Request:
    - POST `http://localhost:8081/waesheroes/api/v1/users`
    - Header: `Content-Type=application/json`
    - Body:
```
            {  
              "username": "new_user",  
              "isAdmin": false,  
              "dateOfBirth": "1984-09-18",  
              "email": "new.user@wearewaes.com",  
              "name": "New User",  
              "password": "wololo",
              "superpower": "Kamehameha."  
            }  
```

#### Update user

-  Request:
    - PUT `http://localhost:8081/waesheroes/api/v1/users`
    - Basic Auth: `username=dev` , `password=wizard`
    - Body:
```
            {  
              "id": 2,
              "username": "dev",
              "dateOfBirth": "1999-10-10",
              "email": "zd.dev@wearewaes.com",
              "isAdmin": false,
              "name": "Zuper Dooper Dev",
              "password": "wizard",
              "superpower": "A new power."
            }  
```

#### Delete user

-  Request:
    - DELETE `http://localhost:8081/waesheroes/api/v1/users`
    - Basic Auth: `username=tester` , `password=maniac`
    - Body:
```
            {
              "id": 3,
              "name": "Al Skept-Cal Tester",
              "username": "tester",
              "email": "as.tester@wearewaes.com",
              "superpower": "Voltage AND Current.",
              "dateOfBirth": "2014-07-15",
              "isAdmin": false
            }
```
-  Response body:
```
            User 'tester' removed from database.
```