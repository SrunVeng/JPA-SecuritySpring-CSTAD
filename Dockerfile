FROM ghcr.io/graalvm/jdk-community:21
WORKDIR app
ADD ## /app
EXPOSE 8080
ENTRYPOINT["java","-jar","##"]