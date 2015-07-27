# codename
In order to run the server you need to install in your local environment:

- Java JDK 1.8+
- Maven 3.2.3+
- Git to clone this repository


1) Cloning the repository and building the sources

git clone https://github.com/grogdj/codename.git

> cd codename/
> mvn clean install


2) running the application
> cd codename-html5/
> mvn wildfly:run

3) access the application
In your browser:
http://localhost:8080/codename-server/



-----

To be able to modify the client side on the fly, you can run the client side app  using the node js server (only for dev purposes)

install Node

> cd codename-html5/
> npm install 
> npm start

Now point your browser at: http://localhost:8000/app/

Notice that you need to have the backed running (mvn wildfly:run in codename-html5/)



Services to initialize data:

http://localhost:8080/codename-server/rest/public/app/init

