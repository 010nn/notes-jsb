# Этап 1: Сборка проекта
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Копируем файлы конфигурации Maven и зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код и собираем приложение
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Запуск приложения
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Копируем скомпилированный jar-файл из первого этапа
COPY --from=build /app/target/*.jar app.jar

# Указываем порт, который слушает приложение
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
