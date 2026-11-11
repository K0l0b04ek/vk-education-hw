# ДЗ №1. Автоматизация тестирования API

Автотесты для публичного REST API [JSONPlaceholder](https://jsonplaceholder.typicode.com) (ресурс `/posts`).

**Стек:** Java 21, Gradle, JUnit 5, RestAssured, JSON Schema Validator, GitHub Actions.

## Что покрыто

| Класс | Что проверяет |
|---|---|
| `PostsPositiveTest` | CRUD (GET/POST/PUT/PATCH/DELETE), фильтр, идемпотентность PUT, безопасность GET, заголовки |
| `PostsNegativeTest` | несуществующие и невалидные id (404), неизвестный путь, пустые выборки, отсутствие утечек во внутренние детали, поведение при пустом теле |
| `PostsBoundaryTest` | границы id (0/1/100/101), границы пагинации, длина `title` (0...10000), Unicode и спецсимволы |
| `PostsValidationTest` | JSON Schema (пост, список, комментарии), формат email, согласованность `posts` и `users`, пагинация без дублей |

Теги JUnit: `smoke` (быстрый набор) и `regression` (остальное).

**Наблюдение:** JSONPlaceholder - fake API: он не сохраняет изменения и не валидирует вход
(например, принимает пустой JSON). Такие тесты явно помечены как фиксация фактического поведения.

## Структура

```
src/test/java/com/example/api/
  Specs.java          # общие RequestSpec / ResponseSpec
  PostsClient.java    # ApiClient: тесты не знают про HTTP
  Posts*Test.java     # тесты по группам: Positive, Negative, Boundary, Validation
src/test/resources/schema/post.json
.github/workflows/api-tests.yml   # запуск в CI
```

## Запуск

```bash
./gradlew test                    # все тесты (или `gradle test`, если нет wrapper)
./gradlew test -Dgroups=smoke     # только smoke
```
