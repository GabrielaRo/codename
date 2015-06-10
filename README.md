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
Advanced configurations for FrontEnd Developers

If you are a front end developer and don't want to edit the WAR file you can configure a local instance of Apache to host the HTML/JS code so you can edit directly from your cloned copy of the project and run the backend code in wildfly. For doing that you need to configure Apache to use VirtualHosts.
I've followed the following two links as guide:
- http://coolestguidesontheplanet.com/set-virtual-hosts-apache-mac-osx-10-10-yosemite/
- https://discussions.apple.com/docs/DOC-3083

If you are in Mac OSX Yosemite, this are the steps that I've followed (from the terminal)

1- sudo open -a TextEdit /etc/apache2/httpd.conf
Search "httpd-vhosts.conf" and uncomment the line by removing the initial #
Search "mod_vhost_alias.so" and uncomment the line by removing the initial #
Search "mod_userdir.so" and uncomment the line by removing the initial #
Search "httpd-userdir.conf" and uncomment the line by removing the initial #

2- sudo open -a TextEdit /etc/apache2/extra/httpd-vhosts.conf
Add this content and replace the correct path to where the project is and the <domain-name.com>

<VirtualHost *:80>
ServerName localhost
DocumentRoot /Library/WebServer/Documents/
</VirtualHost>

<VirtualHost *:80>
    ServerName <domain-name.com>
    ServerAlias www.<domain-name.com>
    DocumentRoot "/Users/<your username>/<path to your projects>/codename/codename-html5/app"
    ErrorLog "/private/var/log/apache2/apple.com-error_log"
    CustomLog "/private/var/log/apache2/apple.com-access_log" common
    ServerAdmin web@coolestguidesontheplanet.com
</VirtualHost>

3- sudo open -a TextEdit /etc/hosts

Replace the localhost line for:

127.0.0.1	localhost <domain-name.com> www.<domain-name.com>

4- sudo open -a TextEdit /etc/apache2/extra/httpd-userdir.conf
Uncomment this line:
Include /private/etc/apache2/users/*.conf

5- Create or edit a file called: <username>.conf in /etc/apache2/users/

sudo open -a TextEdit /etc/apache2/users/<username>.conf 

Add this content:
<Directory "/Users/<username>/<path to project>/codename/codename-html5/app/">
AllowOverride All
Options Indexes MultiViews FollowSymLinks
Require all granted
</Directory>




Final step- sudo apachectl restart



