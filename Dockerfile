FROM maven:3.5-jdk-8 as BUILD

COPY src /usr/src/fproxy/src
COPY pom.xml /usr/src/fproxy
RUN mvn -f /usr/src/fproxy/pom.xml clean package

FROM openjdk:8
COPY --from=build /usr/src/fproxy/target/*-fat.jar /usr/fproxy/target/
COPY start.sh /usr/fproxy/

EXPOSE 8080
#ENTRYPOINT ["/usr/fproxy/start.sh"]
ENTRYPOINT ["java","-jar","/usr/fproxy/target/fproxy-1.0.0-SNAPSHOT-fat.jar"]
