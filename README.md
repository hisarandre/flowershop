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
1. Make sure that you have PostGreSQL database running
2. Create a .env in the ressources folder and add `DATABASE_URL=` with the url to your db, username and paswword  


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
