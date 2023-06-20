FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} resourceplanning-dev-1.jar
ENTRYPOINT ["java","-jar","/resourceplanning-dev-1.jar"]