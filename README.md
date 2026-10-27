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
notes-jsb/
├── src/
│   ├── main/
│   │   ├── java/com/notesjsb/
│   │   │   ├── NotesJSB.java              # Главный класс приложения
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java    # Конфигурация безопасности
│   │   │   ├── controller/
│   │   │   │   ├── ApiController.java     # Контроллер api endpoints
│   │   │   │   ├── AuthController.java    # Контроллер аутентификации
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── NoteController.java    # Контроллер заметок
│   │   │   ├── model/
│   │   │   │   ├── dto/
│   │   │   │   │   └── NoteDto.java
│   │   │   │   ├── Note.java              # Модель заметки
│   │   │   │   └── User.java              # Модель пользователя
│   │   │   ├── repository/
│   │   │   │   ├── NoteRepository.java    # Репозиторий заметок
│   │   │   │   └── UserRepository.java    # Репозиторий пользователей
│   │   │   └── service/
│   │   │       ├── CustomUserDetailsService.java # Детали пользователя для Spring Security
│   │   │       ├── NoteService.java       # Сервис заметок
│   │   │       └── UserService.java       # Сервис пользователей
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   └── js/
│   │       │       └── notes.js
│   │       ├── templates/
│   │       │   ├── login.html                     # Страница входа
│   │       │   ├── notes.html                     # Список заметок
│   │       │   └── register.html                  # Страница регистрации
│   │       ├── META-INF/
│   │       └── application.properties             # Конфигурация приложения
├── docker-compose.yml                             # Конфигурация PostgreSQL
├── .env                                           # Переменные окружения
└── pom.xml                                        # Maven зависимости
```

## Quick start

```bash
cd notes-jsb
docker-compose up -d
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
