# Etapa 1: build do projeto com Gradle
FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle . # Se existir
COPY src src

RUN chmod +x gradlew

# Executa o build (gera o .jar), ignora os testes, com logs
RUN ./gradlew bootJar -Pprod -x test --stacktrace --info

# Etapa 2: imagem final para executar o .jar
FROM openjdk:21-jdk-slim

WORKDIR /app
VOLUME /tmp

# Copia o .jar gerado da etapa de build para esta imagem
COPY --from=build /app/build/libs/*.jar app.jar

# Ponto de entrada da aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Expõe a porta padrão do Spring Boot
EXPOSE 8080
