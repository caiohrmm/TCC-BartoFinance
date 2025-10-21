# Dockerfile para desenvolvimento BartoFinance
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar pom.xml e baixar dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar aplicação
RUN mvn package -DskipTests

# Imagem final
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar JAR da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expor porta
EXPOSE 8080

# Executar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

