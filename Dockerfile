FROM openjdk:23
COPY "./target/parcial_3-1.jar" "app.jar"
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]