### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues


# Readme

## How to run the application
### Docker
To run the application in Docker simply run the following command in the root directory of the project:
```
docker-compose up --build
```
### Maven
To run the application with Maven simply run the following command in the root directory of the project:
```
mvn clean package
```
and then run
```
mvn spring-boot:run
```
## How to use the application
1. The endpoints can either be called directly using a tool like Postman or through the Swagger UI at http://localhost:8080/swagger-ui.html.
2. Obtain a token by calling the `POST /authenticate` endpoint with a valid username and password. The username is `user` and the password is `password`.
3. When using the Swagger UI, add the token to the Authorize button with the Bearer prefix.
4. Following authentication, the endpoints can be called in any order.

## What I did
- Added a Dockerfile and docker-compose.yml file to allow for easy deployment of the application
- Added stateless authentication using jwt tokens
- Implemented input validation for the endpoints
- Added a custom exception handlers to return a more user-friendly error message
- Seeded the database with some data (using the data.sql file where there is an initial user who can be authenticated)
- Password is hashed in Database so that it is not stored in plain text
- Completely refactored EmployeeController so that the code is more readable and maintainable and robust
- Code uses dependency injection to allow for easier testing
- Added comprehensive suite of unit tests
- Created end-to-end tests which test the application as a whole
- Upgraded the swagger UI such that it can utlize bearer tokens for authentication

## What I would have done if I had more time
- Added more unit tests with edge cases
- Added more end-to-end tests with edge cases
- Convert the project to use Kotlin (just because I like Kotlin)
- Make the project use a database that persists data (instead of the in-memory database)

## My experience in Java

I have a fair amount of experience with Java (and Kotlin) and Spring Boot (approximately 3 years on and off).
As a person who has been in the technology/software consulting field for a while I have a wide breadth
of experience in various stacks and I believe that many skills are transferable between languages and frameworks.
As such I believe that the language being used is not important as long as the fundamentals are good and the
final product is of high quality (and most importantly is working and maintainable).

