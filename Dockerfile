# Construction du jar (les tests sont executes a part avec ./mvnw verify).
FROM maven:3.9-eclipse-temurin-21 AS construction
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src src
RUN mvn -q -B -DskipTests package

# Image d'execution.
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=construction /app/target/*.jar app.jar
# Heure de Paris, identique a celle de la base au chargement des donnees de demo.
ENV TZ=Europe/Paris
# Memoire limitee chez l'hebergeur : la JVM s'adapte a la memoire du conteneur.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
