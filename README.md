# Минималистичный блокнот на Spring Boot

Приложение для ведения заметок с аутентификацией и регистрацией пользователей.

## Технологии

- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Maven
- Docker Compose

## Структура проекта

```
notebook-app/
├── src/main/java/com/example/notebook/
│   ├── NotebookApplication.java      # Главный класс приложения
│   ├── config/
│   │   └── SecurityConfig.java       # Конфигурация безопасности
│   ├── controller/
│   │   ├── AuthController.java       # Контроллер аутентификации
│   │   └── NoteController.java       # Контроллер заметок
│   ├── model/
│   │   ├── User.java                 # Модель пользователя
│   │   └── Note.java                 # Модель заметки
│   ├── repository/
│   │   ├── UserRepository.java       # Репозиторий пользователей
│   │   └── NoteRepository.java       # Репозиторий заметок
│   └── service/
│       ├── UserService.java          # Сервис пользователей
│       ├── NoteService.java          # Сервис заметок
│       └── CustomUserDetailsService.java # Детали пользователя для Spring Security
├── src/main/resources/
│   ├── templates/
│   │   ├── login.html                # Страница входа
│   │   ├── register.html             # Страница регистрации
│   │   ├── notes.html                # Список заметок
│   │   └── note-form.html            # Форма создания/редактирования
│   ├── static/css/
│   │   └── style.css                 # Стили
│   └── application.properties        # Конфигурация приложения
├── docker-compose.yml                # Конфигурация PostgreSQL
└── pom.xml                           # Maven зависимости
```

## Запуск

### 1. Запуск базы данных

```bash
cd notebook-app
docker-compose up -d
```

Или с использованием новой команды Docker:

```bash
docker compose up -d
```

### 2. Запуск приложения

```bash
mvn spring-boot:run
```

Или запустить собранный jar-файл:

```bash
java -jar target/notebook-app-1.0.0.jar
```

### 3. Доступ к приложению

Откройте браузер и перейдите по адресу: http://localhost:8080

## Функционал

- **Регистрация**: Создание нового аккаунта
- **Вход/Выход**: Аутентификация пользователей
- **Создание заметок**: Добавление новых заметок с заголовком и содержимым
- **Редактирование**: Изменение существующих заметок
- **Удаление**: Удаление заметок
- **Просмотр**: Отображение списка всех заметок пользователя

## API Endpoints

| Метод | Путь | Описание |
|-------|------|----------|
| GET | /login | Страница входа |
| GET | /register | Страница регистрации |
| POST | /register | Регистрация пользователя |
| GET | /notes | Список заметок |
| GET | /notes/new | Форма создания заметки |
| POST | /notes | Создание заметки |
| GET | /notes/{id}/edit | Форма редактирования |
| POST | /notes/{id} | Обновление заметки |
| POST | /notes/{id}/delete | Удаление заметки |

## Конфигурация

По умолчанию приложение использует:
- Порт: 8080
- PostgreSQL: localhost:5432
- База данных: notebook_db
- Пользователь БД: notebook_user
- Пароль БД: notebook_pass

Для изменения настроек отредактируйте `src/main/resources/application.properties`.
