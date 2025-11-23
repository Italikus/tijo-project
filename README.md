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

## Dokumentacja API

Projekt backend wykorzystuje SpringDoc OpenAPI (Swagger) do automatycznej dokumentacji REST API.

### Dostęp do dokumentacji

Po uruchomieniu backendu dokumentacja API jest dostępna pod następującymi adresami:

1. **Swagger UI** (interaktywna dokumentacja):
   ```
   http://localhost:8080/swagger-ui.html
   ```
   Swagger UI umożliwia przeglądanie wszystkich endpointów, ich parametrów, oraz testowanie API bezpośrednio z przeglądarki.

2. **OpenAPI JSON** (specyfikacja w formacie JSON):
   ```
   http://localhost:8080/api-docs
   ```
   Możesz użyć tego URL do importu API do narzędzi takich jak Postman, Insomnia czy innych klientów REST API.

### Zawartość dokumentacji

Dokumentacja zawiera szczegółowe informacje o wszystkich endpointach:

#### Cars API (`/api/v1/cars`)
- `GET /api/v1/cars` - Pobranie listy pojazdów (z paginacją)
- `POST /api/v1/cars` - Dodanie nowego pojazdu (wymagana walidacja VIN)
- `PUT /api/v1/cars/{id}` - Edycja pojazdu
- `DELETE /api/v1/cars/{id}` - Usunięcie pojazdu

#### Owners API (`/api/v1/owners`)
- `GET /api/v1/owners` - Pobranie listy właścicieli (z paginacją)
- `POST /api/v1/owners` - Dodanie nowego właściciela (wymagana walidacja PESEL)
- `DELETE /api/v1/owners/{id}` - Usunięcie właściciela
- `POST /api/v1/owners/{id}/cars/{carId}` - Przypisanie pojazdu do właściciela


## Technologie użyte w projekcie
### Backend
- Java 21
- Spring Boot 3.5.7:
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
  - spring-boot-starter-validation
- SpringDoc OpenAPI 2.8.4 (Swagger UI)
- H2 Database (tryb runtime)
- Lombok
- Maven Wrapper
- JUnit / Mockito / Spring Boot Test (testy jednostkowe/integracyjne)

### Frontend
- Angular 20 (Core, Router, Forms)
- TypeScript 5.9
- RxJS 7.8