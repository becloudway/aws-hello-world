FROM java:8

RUN mkdir /sqs-writer

COPY sqs-writer.jar /sqs-writer/

WORKDIR /sqs-writer

ENTRYPOINT ["java","-Xmx256m", "-jar","sqs-writer.jar"]
