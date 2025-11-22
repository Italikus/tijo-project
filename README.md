# Testowanie i Jakość Oprogramowania

## Autor
Bartłomiej Podlewski

## Temat projektu
Aplikacja do zarzadzania pojazdami i ich właścicielami
## Opis projektu
Projekt składa się z dwóch modułów:
- Backend (`tijo-backend`): Aplikacja Spring Boot (Java 21) udostępniająca REST API do zarządzania encjami `Car` oraz `Owner`. Wykorzystuje wbudowaną bazę H2 dla uproszczenia uruchomienia, walidację (Bean Validation) oraz prosty mechanizm obsługi błędów. Zastosowano Lombok do redukcji kodu boilerplate. Dostępne są kontrolery `CarController` i `OwnerController`, serwisy oraz repozytoria JPA.
- Frontend (`tijo-frontend`): Aplikacja Angular (v20) komunikująca się z backendem, umożliwiająca przegląd i zarządzanie danymi. Zawiera strony `cars` i `owners`, serwisy do komunikacji z API oraz prostą obsługę błędów.

Główne funkcjonalności:
- Dodawanie, edycja, usuwanie i pobieranie samochodów oraz właścicieli
- Walidacja numeru PESEL (własny walidator)
- Obsługa błędów i zwracanie ujednoliconej odpowiedzi
- Zapewnienie dostępu przez CORS (konfiguracja `CorsConfig`)

## Uruchomienie projektu

### Backend (Spring Boot)
Wymagania: Java 21 (JDK), Maven Wrapper w repozytorium (nie trzeba instalować Maven globalnie).

1. Przejdź do katalogu backendu:
```
cd tijo-backend
```
2. (Opcjonalnie) Uruchom testy:
```
./mvnw test
```
(Windows PowerShell: `./mvnw.cmd test` lub `mvnw.cmd test`)
3. Uruchom aplikację:
```
./mvnw spring-boot:run
```
(Windows: `./mvnw.cmd spring-boot:run`)
4. Domyślnie API będzie dostępne pod adresem: `http://localhost:8080`

### Frontend (Angular)
Wymagania: Node.js (zalecana wersja zgodna z Angular 20), npm.

1. Przejdź do katalogu frontendu:
```
cd tijo-frontend
```
2. Zainstaluj zależności:
```
npm install
```
3. Uruchom serwer deweloperski:
```
npm start
```
4. Aplikacja będzie dostępna pod adresem: `http://localhost:4200`

Upewnij się, że backend działa równolegle (port 8080), aby frontend mógł pobierać dane.

## Technologie użyte w projekcie
### Backend
- Java 21
- Spring Boot 3.5.7:
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
  - spring-boot-starter-validation
- H2 Database (tryb runtime)
- Lombok
- Maven Wrapper
- JUnit / Mockito / Spring Boot Test (testy jednostkowe/integracyjne)

### Frontend
- Angular 20 (Core, Router, Forms)
- TypeScript 5.9
- RxJS 7.8