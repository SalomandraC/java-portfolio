# Java Portfolio

Подборка учебных и практических проектов на Java — от потоков и Stream API до Spring MVC и Spring Boot.
Каждый проект лежит в своей папке и собирается независимо.

| Проект | О чём | Стек |
|---|---|---|
| [spring-boot-blog-api](spring-boot-blog-api) | REST API блога: посты и комментарии, DTO с валидацией, единый обработчик ошибок | Java 21, Spring Boot, Bean Validation |
| [spring-mvc-products](spring-mvc-products) | Каталог товаров на Spring MVC без Boot: DAO в памяти и в файле, статистика по категориям, JSP | Java 17, Spring Web MVC, JSP/JSTL, WAR + Cargo |
| [jdbc-http-logs](jdbc-http-logs) | Консольное приложение: импорт журнала веб-сервера из CSV в PostgreSQL, DAO на чистом JDBC, отчёты | Java 21, JDBC, PostgreSQL, SLF4J/Logback, JUnit 5 |
| [log-analytics-streams](log-analytics-streams) | Анализ логов сервера: запросы на Stream API против императивного кода, слияние логов, бенчмарк, тесты | Java, Stream API, JUnit 5 |
| [elevator-concurrency-sim](elevator-concurrency-sim) | Симуляция лифтов на потоках: BlockingQueue, CountDownLatch, атомики; замер времени при разной нагрузке | Java 25, java.util.concurrent |

## Сборка

Maven-проекты:

```bash
cd <project> && mvn package
```

`log-analytics-streams` — проект IntelliJ без сборщика: откройте папку в IDE и подключите JUnit 5.

`jdbc-http-logs` ждёт пароль БД в переменной окружения `DB_PASSWORD`.
