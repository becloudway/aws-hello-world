FROM tomcat:8.0.38-jre8

WORKDIR /usr/local/tomcat

RUN rm -rf /usr/local/tomcat/webapps/

COPY ecs-api-product.war webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "stop"]
CMD ["catalina.sh", "run"]