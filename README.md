# kodilla-library
Welcome in Kodilla Library Project!

### 1. Project description 
The project consist of backend library service, based on REST architecture. 
### 2. Demo
Project is not uploaded to remote server yet. Application tested on localhost

### 3. Requirements
Please make sure You have following software:
```bash
1) Java 8
2) Gradle 6.6.1
3) Docker 19.03.13
```
### 4. Docker launch 
In order to launch project you have to do next steps:
1. build jar package. Please use terminal:
 ```
gradlew clean build
```
2. build docker image
```
docker build -t library .
```
3. run docker compose
```
docker-compose up
```
Now application is running on `http://localhost:8080`
To start you have to register your user by endpoint: 

`POST http://localhost:8080/library/user/auth/signup`

Docker configuration is available in `Dockerfile` and `docker-compose.yml`.

In  class.
You can check endpoints operation on [http://localhost:8080](http://localhost:8080) address.

### 5. Jwt authentication
Jwt authentication is applied in this project. Token type is Bearer. 
First of all you need to register your user using endpoint `./library/user/auth/signup`. 
Please use `./library/user/auth/signin` endpoint to get token. 
Endpoints `signup` and `signin` have access without authentication.
For all other endpoints need to pass authentication with valid token generated with one of the role `user`, `mod` or `admin`.
 Some endpoints are available only with `moderator` or `admin` role.  

### 6. Endpoint description
All information regarding endpoints are covered in Swagger documentation.
Please visit [http://localhost:8080/swagger-ui.html#/](http://localhost:8080/swagger-ui.html#/) after project is started and click on desired function.

![](src/main/resources/swagger.png)

You can test all endpoint via swagger.

### 7. Future plans
Plans to improve project with:
```bash
1) Spring authentication (done: Jwt authentication, 
                            endpoints are secured to use with 
                            user, admin or moderator roles)
2) Transactional features
3) Automatic mailing features
```

### 8. Troubleshooting 
If You encounter any problems regarding operation, please let us know. 