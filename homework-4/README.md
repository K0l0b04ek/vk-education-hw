# ДЗ №4. Автотесты с Selenide и Page Object

UI-автотесты для демо-магазина [SauceDemo](https://www.saucedemo.com) - сайта, специально созданного для тренировки автоматизации тестирования.

**Стек:** Java 21, Gradle, Selenide, JUnit 5 (`@Test`), паттерн Page Object, GitHub Actions.

## Тесты (`SauceDemoTest`)

| Тест | Что проверяет |
|---|---|
| `successfulLoginShowsFullProductCatalog` | после логина открыт каталог "Products", в нём ровно 6 ожидаемых товаров |
| `loginWithWrongPasswordShowsError` | при неверном пароле показывается точный текст ошибки |
| `lockedOutUserCannotLogin` | заблокированный пользователь не может войти, показывается текст ошибки |
| `addedProductAppearsInCart` | после добавления товара счётчик корзины = 1, в корзине лежит ровно этот товар |
| `sortingByPriceLowToHighOrdersPricesAndKeepsAllProducts` | цены отсортированы по возрастанию, набор товаров не изменился |

## Page Object и инкапсуляция

```
src/test/java/com/example/ui/
  pages/LoginPage.java       # страница входа
  pages/InventoryPage.java   # каталог товаров
  pages/CartPage.java        # корзина
  BaseTest.java              # настройка и закрытие браузера
  SauceDemoTest.java         # тесты
```

- Все локаторы - `private` поля страниц. В тестах нет ни одного селектора и ни одного `SelenideElement`.
- Страницы отдают наружу только бизнес-методы и данные (`loginAs`, `addToCart`, `productPrices`, ...).
- Переходы между страницами выражены типами: `loginAs(...)` возвращает `InventoryPage`, `openCart()` возвращает `CartPage`.
- Конструктор `InventoryPage` и `CartPage` ждёт загрузки страницы (URL и заголовок), поэтому наличие объекта гарантирует, что мы на нужной странице.
- Локаторы - стабильные атрибуты `data-test`, а не хрупкие длинные XPath.
- Проверки (`assertEquals`) находятся только в тестах, не в страницах.

## Запуск

Нужны Java 21 и установленный Chrome.

```bash
./gradlew test                              # headless-режим (по умолчанию)
./gradlew test -Dselenide.headless=false    # с видимым окном браузера
```

Отчёты: `build/reports/tests/test/index.html`, скриншоты при падении: `build/reports/selenide`.

## Gradle Wrapper

Gradle Wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`) закоммичен в репозиторий, поэтому проект собирается без установки Gradle.

## Учёт замечаний ревью по ДЗ №1

- Wrapper добавлен в репозиторий.
- Везде явные импорты вместо `*`.
- Проверки строгие: точные тексты ошибок, точные списки и множества вместо проверок вида "не меньше"/"не 5xx".
- Для сортировки сравниваются не только размеры списков, но и множества товаров до и после.
