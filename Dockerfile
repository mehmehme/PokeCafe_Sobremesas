# Etapa de build: usa o OpenJDK 21 para compilar o projeto com Gradle
FROM openjdk:21-jdk-slim AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o Gradle Wrapper (scripts e configurações)
COPY gradlew .
COPY gradle gradle

# Copia o arquivo de build do Gradle
COPY build.gradle .
COPY settings.gradle . # Incluído se você tiver settings.gradle

# Copia o código-fonte da aplicação
COPY src src

# Dá permissão de execução ao script gradlew
RUN chmod +x gradlew

# Executa o build da aplicação, pulando os testes e COM MAIS LOGS PARA DEPURAR
RUN ./gradlew bootJar -Pprod -x test --stacktrace --info

# Etapa final: cria a imagem definitiva com a aplicação empacotada com OpenJDK 21 Slim
FROM openjdk:21-jdk-slim

# Cria um volume temporário para arquivos que possam ser escritos pela aplicação (boa prática)
VOLUME /tmp

# Copia o arquivo .jar gerado na etapa de build para o contêiner final
# O nome do JAR geralmente segue o padrão 'build/libs/nome-do-projeto-versao.jar'
ARG JAR_FILE=build/libs/PokeCafe-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Define o ponto de entrada para executar a aplicação Java
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Expõe a porta 8080 para acesso externo à aplicação
EXPOSE 8080