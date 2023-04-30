# Инструкция по запуску
Проект работает на порте 8080

##C Docker'ом
- Запустить `docker-compose.yml` (для этого в директории с проектом ввести `docker-compose up`), он запуллит образы `postgres:latest` и `martell805/credits` 
и с их помощью создаст Docker-compose `credits`

##Без Docker'а
### База данных
- Проект использует PostgreSQL
- Создать роль с именем `admin`, паролем `admin` и всеми правами<br>
- Создать базу данных `credits`
- PostgreSQL должен работать на порте `5432`
### Приложение
- Используется Java 17
- Запустить `CreditSApplication.java`
- К приложению подключен Swagger
