FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x run.sh

EXPOSE 8081

CMD ["./run.sh"]
