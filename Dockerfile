FROM openjdk:17-jdk

COPY target/hiSchoolBE.jar .

EXPOSE 8080

ENTRYPOINT ["java","-jar","hiSchoolBE.jar"]