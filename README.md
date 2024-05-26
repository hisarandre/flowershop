<h1 align="center">Welcome to the Flower Shop 👋</h1>
<p>
</p>

> A stock management service for a flower shop.

## Versions
- Spring Boot: 3.3.0
- Java : 21
- Maven: 4.0.0
- React : ^18.3.1
- PostGreSQL : 16

## Run the app

To launch the app, you can run it locally.


### Run local

0. First, clone this repository

Launch the database :
1. Make sure that you have PostGreSQL installed
2. Register a new Server :

- HostName : localhost
- Port : 5432

3. Create a new user :
- Define the name and the password
- In the privileges tab, turn on: can login, create databases and inherit rights from the parent roles
- Create a new database and set the owner with the user you created
- Create the tables with the file : back/src/main.resources/schema.sql
- Insert data with the file : back/src/main.resources/data.sql
- Grant access to each table to the user with a query :`GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE your_name_table TO your_user`;

4. Open the config/application.properties file in your Spring Boot project.
5. Complete the configuration with user password

Launch the backend :
1. Make sure you have the required versions of Java and dependencies installed.
2. Open a terminal or command prompt and navigate to the project directory.
3. Run the following command to build the project and create an executable JAR file:
   ` mvn package`
4. Once the build is successful, you can launch the app using the following command:
   ` java -jar target/flowershop-0.0.1-SNAPSHOT.jar `
   This will start the app on the configured server address : http://localhost:1010

## Testing

## Endpoints

You can check the endpoints with requirements on Swagger :
http://localhost:1010/swagger-ui/index.html
