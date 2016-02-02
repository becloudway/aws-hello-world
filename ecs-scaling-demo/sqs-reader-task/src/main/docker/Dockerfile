FROM java:8

RUN mkdir /sqs-reader

COPY sqs-reader.jar /sqs-reader/

WORKDIR /sqs-reader

ENTRYPOINT ["java","-Xmx256m", "-jar","sqs-reader.jar"]
