# Train Tracker – трекер наблюдений за поездами
**Примечание:** Данный репозиторий является **форком** проекта [train-tracker] из предыдущей работы по Docker. В рамках текущей практической работы по CI/CD в него добавлены unit/интеграционные тесты, линтер (Checkstyle), настроен пайплайн GitHub Actions (сборка, линтинг, тесты с проверкой покрытия, сборка и публикация Docker-образа в Docker Hub). 
## Описание проекта

**Train Tracker** – это backend-сервис для энтузиастов, которые отслеживают поезда.  
Приложение позволяет вести учёт:

- **Моделей поездов** (название, производитель, год выпуска, мощность, макс. скорость)
- **Экземпляров поездов** (серийный номер, дата выпуска, фото, привязка к модели)
- **Наблюдений** – когда и где был замечен конкретный экземпляр поезда, с возможностью добавить фото и заметку

Реализовано полноценное REST API с валидацией, DTO, мапперами (MapStruct), глобальной обработкой ошибок и логированием.  
Всё взаимодействие через HTTP-запросы (curl/Postman).

## Технологический стек

- **Java 17** + **Spring Boot 3.3.5**
- **Spring Data JPA**, **Hibernate**
- **PostgreSQL 15** (в отдельном контейнере)
- **Docker**, **Docker Compose** (многоступенчатая сборка)
- **MapStruct** (маппинг Entity ↔ DTO)
- **Lombok**, **Validation API**

## Структура проекта

```
train-tracker/
├── app/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/tracker/
│           │   ├── TrainTrackerApplication.java
│           │   ├── controller/
│           │   │   ├── TrainModelController.java
│           │   │   ├── TrainInstanceController.java
│           │   │   └── SightingController.java
│           │   ├── dto/
│           │   │   ├── TrainModelDto.java
│           │   │   ├── TrainInstanceDto.java
│           │   │   └── SightingDto.java
│           │   ├── mapper/
│           │   │   ├── TrainModelMapper.java
│           │   │   ├── TrainInstanceMapper.java
│           │   │   └── SightingMapper.java
│           │   ├── model/
│           │   │   ├── TrainModel.java
│           │   │   ├── TrainInstance.java
│           │   │   └── Sighting.java
│           │   ├── repository/
│           │   │   ├── TrainModelRepository.java
│           │   │   ├── TrainInstanceRepository.java
│           │   │   └── SightingRepository.java
│           │   ├── service/
│           │   │   ├── TrainModelService.java
│           │   │   ├── TrainInstanceService.java
│           │   │   └── SightingService.java
│           │   └── exception/
│           │       ├── ResourceNotFoundException.java
│           │       └── GlobalExceptionHandler.java
│           └── resources/
│               └── application.properties
├── db/
│   ├── Dockerfile
│   └── init.sql
├── docker-compose.yml
├── .env
├── .gitignore
└── README.md
```

## Запуск

1. **Клонируйте репозиторий**  
   ```bash
   git clone <your-repo-url>
   cd train-tracker
   ```

2. **Создайте файл `.env`** в корне проекта:  
   ```
   DB_USER=user
   DB_PASSWORD=postgres
   ```

3. **Запустите контейнеры**  
   ```bash
   docker-compose up --build
   ```
   - Приложение станет доступно по адресу: `http://localhost:8080`

4. **Остановка**  
   ```bash
   docker-compose down
   ```
   Данные сохраняются в Docker-томе `postgres_data`.

## API Endpoints

### 1. Модели поездов (`/api/models`)

| Метод | URL | Описание | Пример тела запроса |
|-------|-----|----------|----------------------|
| GET | `/api/models` | Получить все модели | – |
| GET | `/api/models/{id}` | Получить модель по ID | – |
| POST | `/api/models` | Создать модель | `{"name":"ЭП20","manufacturer":"НЭВЗ","yearOfProduction":2012,"powerKw":8800,"maxSpeedKmh":200}` |
| PUT | `/api/models/{id}` | Обновить модель | (как при создании) |
| DELETE | `/api/models/{id}` | Удалить модель | – |

### 2. Экземпляры поездов (`/api/instances`)

| Метод | URL | Описание | Пример тела запроса |
|-------|-----|----------|----------------------|
| GET | `/api/instances` | Все экземпляры (можно фильтровать `?modelId=1`) | – |
| GET | `/api/instances/{id}` | Получить экземпляр по ID | – |
| POST | `/api/instances` | Создать экземпляр | `{"serialNumber":"ЭП20-001","manufactureDate":"2013-05-01","imageUrl":"http://...","modelId":1}` |
| PUT | `/api/instances/{id}` | Обновить экземпляр | (аналогично) |
| DELETE | `/api/instances/{id}` | Удалить экземпляр | – |

### 3. Нотации (наблюдения) (`/api/sightings`)

| Метод | URL | Описание | Пример тела запроса |
|-------|-----|----------|----------------------|
| GET | `/api/sightings` | Все нотации (фильтр `?instanceId=1`) | – |
| GET | `/api/sightings/{id}` | Получить нотацию по ID | – |
| POST | `/api/sightings` | Добавить наблюдение | `{"region":"Московская обл.","railway":"Октябрьская","sightingDate":"2025-04-20","imageUrl":"http://...","note":"Проезд","instanceId":1}` |
| PUT | `/api/sightings/{id}` | Обновить нотацию | (аналогично) |
| DELETE | `/api/sightings/{id}` | Удалить нотацию | – |

## Примеры запросов (curl)

### Создание модели
```bash
curl -X POST http://localhost:8080/api/models \
  -H "Content-Type: application/json" \
  -d '{"name":"ВЛ80","manufacturer":"НЭВЗ","yearOfProduction":1979,"powerKw":6520,"maxSpeedKmh":110}'
```

### Создание экземпляра (привязка к модели с id=1)
```bash
curl -X POST http://localhost:8080/api/instances \
  -H "Content-Type: application/json" \
  -d '{"serialNumber":"ВЛ80-001","manufactureDate":"1980-06-01","imageUrl":"https://example.com/vl80.jpg","modelId":1}'
```

### Добавление нотации для экземпляра id=1
```bash
curl -X POST http://localhost:8080/api/sightings \
  -H "Content-Type: application/json" \
  -d '{"region":"Ленинградская область","railway":"Октябрьская","sightingDate":"2025-04-22","imageUrl":"https://example.com/sighting.jpg","note":"Замечен на перегоне","instanceId":1}'
```

### Получить все нотации для экземпляра 1
```bash
curl "http://localhost:8080/api/sightings?instanceId=1"
```

### Обновить модель
```bash
curl -X PUT http://localhost:8080/api/models/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"ВЛ80 (модернизированный)","manufacturer":"НЭВЗ","yearOfProduction":1979,"powerKw":6800,"maxSpeedKmh":120}'
```

### Удалить нотацию
```bash
curl -X DELETE http://localhost:8080/api/sightings/2
```

## Валидация и обработка ошибок

- **Валидация входных данных** через `@Valid` (JSR-380):
  - Поля не могут быть пустыми (`@NotBlank`)
  - Ограничения длины (`@Size`)
  - Даты не могут быть в будущем (`@PastOrPresent`)
  - Числовые поля проверяются на разумный диапазон (`@Min`, `@Max`)

- **Глобальный обработчик ошибок** (`GlobalExceptionHandler`) возвращает понятные JSON-ответы:
  - `404 Not Found` – ресурс не существует
  - `400 Bad Request` – ошибки валидации (с указанием полей)
  - `500 Internal Server Error` – неожиданные ошибки

Пример ответа при ошибке валидации:
```json
{
  "status": 400,
  "message": "Ошибка валидации",
  "errors": {
    "name": "Название модели не может быть пустым"
  },
  "timestamp": "2025-04-23T10:30:00"
}
```

## Ключевые особенности реализации (Docker)

- ✅ **Docker-сеть** – пользовательская bridge-сеть `app-network` для изоляции.
- ✅ **Проброс портов** – открыт только `8080` для API. БД (5432) недоступна с хоста.
- ✅ **Volume** – именованный том `postgres_data` для сохранности данных БД.
- ✅ **Безопасность** – пароли передаются через `.env` (файл в `.gitignore`).
- ✅ **Многоступенчатый Dockerfile** – сначала сборка (Maven+JDK), затем runtime (только JRE).
- ✅ **Healthcheck** для PostgreSQL – приложение стартует только после готовности БД.

### Коллекция запросов для Bruno

В корне проекта находится папка `TrainTracker/` – это готовая коллекция запросов для [Bruno](https://www.usebruno.com/). Она содержит 17 предустановленных запросов, покрывающих все эндпоинты API:

- **Models** – CRUD операций с моделями поездов (5 запросов)
- **Instances** – CRUD операций с экземплярами (6 запросов)
- **Sightings** – CRUD операций с нотациями (6 запросов)

#### Как использовать

1. Установите Bruno (бесплатный клиент для API).
2. В Bruno откройте коллекцию через `Open Collection` и выберите папку `TrainTracker`.
3. Создайте окружение (Environment) с переменной:
   ```
   baseUrl = http://localhost:8080
   ```
4. Убедитесь, что контейнеры запущены (`docker-compose up`).
5. Выполняйте запросы в логическом порядке: сначала создайте модель, затем экземпляр, потом нотацию.

---

**Проект выполнен в рамках практической работы по Docker.**  
Все стандарты современной разработки соблюдены: чистая архитектура, обработка ошибок, валидация, логирование, безопасное конфигурирование.
