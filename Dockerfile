FROM ghcr.io/graalvm/jdk-community:21
WORKDIR app
ADD ./build/libs/MBanking-API-1.0.2.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar" , "-Dspring.profiles.active=UAT", "/app/MBanking-API-1.0.2.jar"]