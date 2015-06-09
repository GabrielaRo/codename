# codename
In order to run the server you need to install in your local environment:

- Java JDK 1.8+
- Maven 3.2.3+
- Git to clone this repository
- Wildfly 9.0.0.CR1 from here: http://wildfly.org/downloads/


1) Cloning the repository and building the sources

git clone https://github.com/grogdj/codename.git

> cd codename/
> mvn clean install

This will download all the java dependencies to build the application and it will package the application under codename-html5/target/codename-html5.war

2) deploying the application into wildfly app server

copy the codename-html5.war into the following directory:

> wildfly-9.0.0.CR1/standalone/deployments

create an empty (could be a text file, you can also rename the README.txt file) file called codename-html5.war.dodeploy 

Before move on check that you have inside the wildfly-9.0.0.CR1/standalone/deployments only two files:

codename-html5.war
codename-html5.war.dodeploy 


If you want to edit the contents of the web application you can uncompress the .war file with any app for uncompressing zip files and then making sure that the directory is called codename-html5.war/

3) Run the application server:
> cd wildfly-9.0.0.CR1/bin/
> ./standalone.sh


-------------------------------------------------
Advanced configurations for Front End Developers

If you are a front end developer and don't want to edit the WAR file you can configure a local instance of Apache to host the HTML/JS code so you can edit directly from your cloned copy of the project and run the backend code in wildfly. For doing that you need to configure Apache to use VirtualHosts.
I've followed the following two links as guide:
- http://coolestguidesontheplanet.com/set-virtual-hosts-apache-mac-osx-10-10-yosemite/
- https://discussions.apple.com/docs/DOC-3083




