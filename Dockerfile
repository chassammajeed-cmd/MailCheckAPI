FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN javac -d build src/app/MainApp.java

EXPOSE 8081

CMD ["java", "-cp", "build", "app.MainApp"]
