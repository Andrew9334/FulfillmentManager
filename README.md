# Stock Tracker API

Проект для управления продуктами и отслеживания их состояния на различных центрах выполнения (Fulfillment Centers). API предоставляет функции для загрузки данных о продуктах из Google Sheets, работы с данными продуктов в базе данных и выполнения основных операций (CRUD).

## Основные технологии

- **Spring Boot** — для создания REST API.
- **Spring Data JPA** — для работы с базой данных.
- **PostgreSQL** — база данных для хранения информации о продуктах.
- **Swagger 2** — для автоматической документации API.
- **Mockito и Spring MockMvc** — для юнит-тестирования.

## Установка и запуск

### 1. Клонировать репозиторий

Для начала клонируйте репозиторий:
```bash
git clone https://github.com/your-username/stock-tracker.git
```

### 2. Настроить PostgreSQL
Убедитесь, что у вас установлен PostgreSQL и он запущен. Создайте базу данных для проекта:
```bash
CREATE DATABASE stock_tracker;
```

Настройте подключение к базе данных в файле application.properties (или application.yml):
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/stock_tracker
spring.datasource.username=your-db-username
spring.datasource.password=your-db-password
spring.jpa.hibernate.ddl-auto=update
```

Замените your-db-username и your-db-password на свои данные для подключения к базе.

### 3. Запуск приложения
Для того чтобы запустить приложение, выполните следующую команду:
```bash
mvn spring-boot:run
```

После этого приложение будет доступно по адресу: http://localhost:8080.

### 4. Загрузка данных из Google Sheets
Для загрузки данных продуктов из Google Sheets, выполните POST-запрос к следующему эндпоинту `/products/load-from-sheet`:

### Параметры запроса:

`spreadsheetId` — Идентификатор вашего Google Spreadsheet.
`range` — Диапазон ячеек, из которых нужно загрузить данные.
`accessToken` — Токен доступа для работы с Google Sheets API.
Пример запроса:
`POST http://localhost:8080/products/load-from-sheet?spreadsheetId=your_spreadsheet_id&range=Sheet1!A1:E10&accessToken=your_access_token`

В ответ вы получите сообщение:
`{
"message": "Products loaded from Google Sheets"
}`

## Основные операции API
### 5.1 Получение списка продуктов по статусу
Получение списка продуктов с определенным статусом (например, SELLABLE):
`GET /products/by-status?status=SELLABLE&page=0&size=10`

Параметры:

* status — Статус продукта (например, SELLABLE, UNFULFILLABLE, INBOUND).
* page — Номер страницы.
* size — Количество элементов на странице.

### 5.2 Получение списка продуктов по центру выполнения
Получение списка продуктов для определенного центра выполнения:
`GET /products/by-fulfillment-center?fulfillmentCenter=fc1&page=0&size=10`

Параметры:

`fulfillmentCenter` — Идентификатор центра выполнения (например, fc1).
`page` — Номер страницы.
`size` — Количество элементов на странице.

### 5.3 Получение общей стоимости всех продуктов с определенным статусом
Получение общей стоимости всех продуктов с заданным статусом:
`GET /products/total-value-by-status?status=SELLABLE`

Пример ответа:
`{
"value": 1500.0
}`

## Тестирование API
Для тестирования API можно использовать Swagger. Swagger UI будет доступен по следующему URL:
http://localhost:8080/swagger-ui.html

Swagger предоставляет интерфейс для выполнения запросов к API и тестирования всех эндпоинтов.

## Запуск тестов
Для запуска тестов проекта используйте команду:
```bash
mvn test
```

Примечания

Убедитесь, что у вас настроен доступ к Google Sheets API и у вас есть токен для аутентификации.
Для удобства использования и тестирования API рекомендуется использовать инструменты, такие как Postman или cURL, для отправки запросов.