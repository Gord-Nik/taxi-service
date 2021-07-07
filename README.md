# taxi service app!
![img_1.png](img_1.png)
The goal of this project is to create a taxi service with authentication system, custom injector and annotations and all the features that might come in handy while managing the app:
create new car / manufacturer
display all drivers
add driver to the car
remove driver from car, etc.

Technologies used:
Backend: Java, JDBC, Servlets
Logger: Log4j
Frontend: HTML, CSS, JSP, JSTL
Database: MySql
Web-server: Tomcat
Packaging: Apache Maven

Setup
Configure Apache Tomcat
Install MySQL and MySQL Workbench
Create a schema and all the necessary tables by using the script from resources/init_db.sql in MySQL Workbench
In the /util/ConnectionUtil.java change the URL, MYSQL_DRIVER, USERNAME and PASSWORD properties to the ones you specified when installing MySQL or you can use the ones that are already present
After starting tomcat go to http://localhost:your port that you specified while configuring tomcat, click on "Register" to add a new driver.
This project is also deployed on heroku: DEMO