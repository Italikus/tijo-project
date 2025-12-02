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

## Testy (Opis testów jednostkowych, integracyjnych z odnośnikami do lokalizacji w projekcie)

Poniżej znajduje się krótkie podsumowanie rodzajów testów znajdujących się w module backend (`tijo-backend`) oraz lista plików testowych z odnośnikami do lokalizacji w projekcie.

### Opis
- Testy jednostkowe: testują pojedyncze klasy i metody w izolacji, często z użyciem mocków (Mockito).
- Testy integracyjne: uruchamiają część kontekstu Springa (np. całą aplikację lub warstwę web) aby przetestować współdziałanie komponentów.

### Testy jednostkowe (backend)
- [CarServiceTest.java](tijo-backend/src/test/java/com/example/tijo/backend/service/CarServiceTest.java) — testy jednostkowe serwisu `CarService` korzystające z Mockito (`@ExtendWith(MockitoExtension.class)`) (logika filtrowania, klasyfikacji, CRUD z mockowanym repozytorium).
- [OwnerDtoTest.java](tijo-backend/src/test/java/com/example/tijo/backend/model/dto/OwnerDtoTest.java) — testy mapowania encji `Owner` na DTO (`OwnerDto`).
- [PeselValidatorTest.java](tijo-backend/src/test/java/com/example/tijo/backend/validation/PeselValidatorTest.java) — testy walidatora numeru PESEL (różne przypadki poprawne/niepoprawne).

### Testy integracyjne (backend)
- [TijoBackendApplicationTests.java](tijo-backend/src/test/java/com/example/tijo/backend/TijoBackendApplicationTests.java) — test kontekstowy aplikacji (`@SpringBootTest`) sprawdzający, czy kontekst Spring ładuje się poprawnie.
- [CarControllerTest.java](tijo-backend/src/test/java/com/example/tijo/backend/controller/CarControllerTest.java) — testy warstwy web dla kontrolera `CarController` używające `@WebMvcTest` oraz `MockMvc` (testy endpointów GET/POST/PUT/DELETE z mockowanym serwisem).
- [OwnerControllerTest.java](tijo-backend/src/test/java/com/example/tijo/backend/controller/OwnerControllerTest.java) — testy warstwy web dla kontrolera `OwnerController` używające `@WebMvcTest` oraz `MockMvc` (sprawdzanie obsługi endpointów właścicieli, walidacji i przypisywania samochodów).

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

## Przypadki testowe dla testera manualnego (TestCase)

Poniżej przedstawiono 10 przypadków testowych dla aplikacji frontendowej służących do weryfikacji funkcjonalności systemu zarządzania pojazdami i właścicielami.

### TC001 - Dodanie nowego pojazdu z poprawnymi danymi

| **ID**                 | TC001 |
|------------------------|-------|
| **Tytuł**              | Dodanie nowego pojazdu z poprawnymi danymi |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona na http://localhost:4200, backend działa na porcie 8080, użytkownik znajduje się na stronie **Cars** |
| **Kroki testowe**      | 1. Wypełnij pole **Brand** wartością "Toyota".<br>2. Wypełnij pole **Model** wartością "Corolla".<br>3. Wypełnij pole **VIN** wartością "1HGBH41JXMN109186".<br>4. Wypełnij pole **Horse Power** wartością "132".<br>5. Wypełnij pole **Production Year** wartością "2023".<br>6. Kliknij przycisk **Create Car**. |
| **Oczekiwany rezultat**| Pojazd zostaje dodany do systemu, tabela odświeża się i wyświetla nowy rekord z danymi: Toyota Corolla, VIN:  , 132 KM, rok 2023. Formularz zostaje wyczyszczony. |

### TC002 - Dodanie pojazdu z nieprawidłowym numerem VIN

| **ID**                 | TC002 |
|------------------------|-------|
| **Tytuł**              | Dodanie pojazdu z nieprawidłowym numerem VIN |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona, użytkownik znajduje się na stronie **Cars** |
| **Kroki testowe**      | 1. Wypełnij pole **Brand** wartością "Ford".<br>2. Wypełnij pole **Model** wartością "Focus".<br>3. Wypełnij pole **VIN** wartością "ABC123" (nieprawidłowy format).<br>4. Wypełnij pole **Horse Power** wartością "150".<br>5. Wypełnij pole **Production Year** wartością "2022".<br>6. Kliknij przycisk **Create Car**. |
| **Oczekiwany rezultat**| System zwraca błąd walidacji. Pojazd nie zostaje dodany do bazy danych. Wyświetla się komunikat o błędzie dotyczący nieprawidłowego formatu VIN. |

### TC003 - Usunięcie pojazdu z listy

| **ID**                 | TC003 |
|------------------------|-------|
| **Tytuł**              | Usunięcie pojazdu z listy |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona, na stronie **Cars** znajduje się co najmniej jeden pojazd w tabeli |
| **Kroki testowe**      | 1. Zlokalizuj pojazd w tabeli, który ma zostać usunięty.<br>2. Kliknij przycisk **Delete** w kolumnie **Actions** dla wybranego pojazdu.<br>3. Poczekaj na odświeżenie tabeli. |
| **Oczekiwany rezultat**| Pojazd zostaje usunięty z bazy danych i znika z tabeli. Lista pojazdów odświeża się automatycznie. |

### TC004 - Wyświetlenie pustej listy pojazdów

| **ID**                 | TC004 |
|------------------------|-------|
| **Tytuł**              | Wyświetlenie pustej listy pojazdów |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona, w bazie danych nie ma żadnych pojazdów |
| **Kroki testowe**      | 1. Przejdź do strony **Cars**.<br>2. Sprawdź wyświetlaną zawartość strony. |
| **Oczekiwany rezultat**| Tabela z pojazdami nie jest wyświetlana. Widoczny jest komunikat **"No cars available."** |

### TC005 - Dodanie nowego właściciela z prawidłowym numerem PESEL

| **ID**                 | TC005 |
|------------------------|-------|
| **Tytuł**              | Dodanie nowego właściciela z prawidłowym numerem PESEL |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona, użytkownik znajduje się na stronie **Owners** |
| **Kroki testowe**      | 1. Wypełnij pole **Name** wartością "Jan".<br>2. Wypełnij pole **Surname** wartością "Kowalski".<br>3. Wypełnij pole **PESEL** wartością "44051401458" (prawidłowy PESEL).<br>4. Kliknij przycisk **Create Owner**. |
| **Oczekiwany rezultat**| Właściciel zostaje dodany do systemu, tabela odświeża się i wyświetla nowy rekord: Jan Kowalski, PESEL: , Owned Cars: 0. Formularz zostaje wyczyszczony. |

### TC006 - Dodanie właściciela z nieprawidłowym numerem PESEL

| **ID**                 | TC006 |
|------------------------|-------|
| **Tytuł**              | Dodanie właściciela z nieprawidłowym numerem PESEL |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona, użytkownik znajduje się na stronie **Owners** |
| **Kroki testowe**      | 1. Wypełnij pole **Name** wartością "Anna".<br>2. Wypełnij pole **Surname** wartością "Nowak".<br>3. Wypełnij pole **PESEL** wartością "12345678901" (nieprawidłowa suma kontrolna).<br>4. Kliknij przycisk **Create Owner**. |
| **Oczekiwany rezultat**| System zwraca błąd walidacji. Właściciel nie zostaje dodany do bazy danych. Wyświetla się komunikat o błędzie dotyczący nieprawidłowego numeru PESEL. Formularz nie zostaje wyczyszczony, aby użytkownik mógł poprawić dane. |

### TC007 - Usunięcie właściciela z listy

| **ID**                 | TC007 |
|------------------------|-------|
| **Tytuł**              | Usunięcie właściciela z listy |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona, na stronie **Owners** znajduje się co najmniej jeden właściciel w tabeli |
| **Kroki testowe**      | 1. Zlokalizuj właściciela w tabeli, który ma zostać usunięty.<br>2. Kliknij przycisk **Delete** w kolumnie **Actions** dla wybranego właściciela.<br>3. Poczekaj na odświeżenie tabeli. |
| **Oczekiwany rezultat**| Właściciel zostaje usunięty z bazy danych i znika z tabeli. Lista właścicieli odświeża się automatycznie. |

### TC008 - Przypisanie pojazdu do właściciela

| **ID**                 | TC008 |
|------------------------|-------|
| **Tytuł**              | Przypisanie pojazdu do właściciela |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona na stronie **Owners**, w systemie istnieje co najmniej jeden właściciel (np. ID=1) i jeden pojazd (np. ID=1) |
| **Kroki testowe**      | 1. W sekcji **"Assign car to owner"** wypełnij pole **Owner ID** wartością "1".<br>2. Wypełnij pole **Car ID** wartością "1".<br>3. Kliknij przycisk **Assign**.<br>4. Poczekaj na odświeżenie tabeli. |
| **Oczekiwany rezultat**| Pojazd zostaje przypisany do właściciela. W tabeli właścicieli wartość kolumny **Owned Cars** dla właściciela o ID=1 zwiększa się o 1. Formularz przypisywania zostaje wyczyszczony. |

### TC009 - Próba przypisania nieistniejącego pojazdu do właściciela

| **ID**                 | TC009 |
|------------------------|-------|
| **Tytuł**              | Próba przypisania nieistniejącego pojazdu do właściciela |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona na stronie **Owners**, w systemie istnieje właściciel o ID=1, ale nie istnieje pojazd o ID=999 |
| **Kroki testowe**      | 1. W sekcji **"Assign car to owner"** wypełnij pole **Owner ID** wartością "1".<br>2. Wypełnij pole **Car ID** wartością "999" (nieistniejący pojazd).<br>3. Kliknij przycisk **Assign**. |
| **Oczekiwany rezultat**| System zwraca błąd (404 Not Found). Wyświetla się komunikat o błędzie informujący, że pojazd o podanym ID nie istnieje. Operacja przypisania nie zostaje wykonana. |

### TC010 - Wyświetlenie pustej listy właścicieli

| **ID**                 | TC010 |
|------------------------|-------|
| **Tytuł**              | Wyświetlenie pustej listy właścicieli |
| **Warunki początkowe** | Aplikacja frontendowa jest uruchomiona, w bazie danych nie ma żadnych właścicieli |
| **Kroki testowe**      | 1. Przejdź do strony **Owners**.<br>2. Sprawdź wyświetlaną zawartość strony. |
| **Oczekiwany rezultat**| Tabela z właścicielami nie jest wyświetlana. Widoczny jest komunikat **"No owners available."** |

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