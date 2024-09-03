FROM ghcr.io/graalvm/jdk-community:21
WORKDIR app
ADD ./build/libs/MBanking-API-1.0.0.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar" , "/app/MBanking-API-1.0.0.jar"]