# BGSM
## Board Games Share Market
Welcome in BGSM Project! Project is developed as final project in Kodilla Java Developer Plus course.

### 1. Project description 
The project consist Java service, based on REST architecture. Frontend side made in Vaadin. 
Application is a service, where users can share board games. User have options to publish own
board games, put offers for another users and rent board games from the others. Games are divided into categories.
Categories makes easier to find interesting content. 

### 2. Demo
Application tested on localhost. First scope was to make app as microservices. This idea is suspended at the moment,
however will be developed in the future to bring an example of microservices architecture. 
At the moment you can run user-service independly or with eureka discovery microservice and have fun for using 
service under localhost:8080

### 3. Requirements
Technologies used in project:
```bash
1) Java 13.0.2
2) Maven 3.6.3
3) MySQL 8
```
### 4. Launch application 
Docker will be applied in the future.

First run discovery service:
```bash
cd eureka
mvnw spring-boot:run
```
Second run main service
```bash
cd user-service
mvnw spring-boot:run
```
Now application is running on `http://localhost:8080`
To start you have to register your user, add your games, add offers and enjoy searching by categories.

### 5. Spring security
Application uses Spring Security to authenticate and authorize content. 
Basic content is unsecured, like main page, sign up page, login page.
Most of the content is available after logging in.
Administrator panel is open only after logging as admin.
There is predefined 3 test user: user, moderator and admin. Passwords are the same as login. 

### 6. Endpoint description
There is several rest controllers with endpoints. There are ready for microservices, but not used at the moment.

### 7. Future plans
Plans to improve project with:
```bash
1) microservices: additional functionality like add photos as microservices
2) split backend layer and frontend layer for two separate microservices
2) extend tests to achieve more coverage
3) automatic mailing features like confirm of add offer, confirm rent game etc.
4) internal chat system, users will have platform to communicate each other, make questions about
offered games etc.
5) rating system and comment system for games on the platform
6) Swagger endpoints documentation
7) possibility to log in wit external platform creditentials like: login with google, facebook.
```

### 8. Troubleshooting 
If You encounter any problems regarding operation, please let us know. 