FROM tomcat:8-jre8-alpine

# Removing Tomcat's default web app.
RUN rm -rf /usr/local/tomcat/webapps/

# Setting the context root as Pokemon.
# If we had used ROOT.war, it would correspond to an empty context root.
ADD Pokemon.war /usr/local/tomcat/webapps/Pokemon.war

