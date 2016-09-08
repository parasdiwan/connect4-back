# connect4-back

This project is implementation of Connect 4 game as a Java web service designed in Spring MVC. 
To get the server up you need the following tools

Pre-requisites
- Java
- Maven
- MySQL
- Redis
- Jetty

Steps to setup project
- Create a database `gluck` in mysql and import project's schema from schema.sql. You can use `mysql -u <username> -p gluck < schema.sql`
- Install all the above tools and navigate to this project and type command `mvn clean install jetty:run` 
- Install redis and run it on the default port 6379
