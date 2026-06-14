FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .


RUN javac Calculator.java

#Final Stage

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/Calculator.class .

ENTRYPOINT ["java","Calaculator"]
