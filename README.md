# WeatherApp – Take-Home Assignment Teil 4

Dieses Branch erweitert den **Forecast-Service** (Teil 1), die **Webservice-Anbindung** (Teil 2) und die HTTP 
**Authentifizierungsmöglichkeit** (Teil 3) um Unittests.

### WICHTIG: Umgebung konfigurieren

Bevor die App startet, müssen zwei Umgebungsvariablen gesetzt werden:
```
# Windows PowerShell
$Env:OPENCAGE_API_KEY="HIER_DEIN_OPENCAGE_KEY"
$Env:OPENWEATHER_API_KEY="HIER_DEIN_OPENWEATHER_KEY"

# macOS/Linux (Bash)
export OPENCAGE_API_KEY="HIER_DEIN_OPENCAGE_KEY"
export OPENWEATHER_API_KEY="HIER_DEIN_OPENWEATHER_KEY"

# Um die App in der IDE zu starten, müssen die Variablen in den Konfigurationen der App gesetzt werden
z.B. in IntelliJ: Run Configurations -> Modify options -> Environment variables

# oder setz die Variablen direkt in "src/main/resources/application.properties" ein
```

Projekt starten:
```bash
# Branch wechseln
git checkout part-4-tests

# Build & Run (Windows PowerShell)
.\mvnw.cmd clean install #Alle Unittests werden automatisch ausgeführt
.\mvnw.cmd spring-boot:run

# Build & Run (macOS/Linux)
./mvnw clean install #Alle Unittests werden automatisch ausgeführt
./mvnw spring-boot:run

# Alternativ in IntelliJ IDEA:
# Run-Button in WeatherappApplication.java klicken
```
Nur die Tests durchlaufen lassen:
```bash
.\mvnw.cmd clean test 
```
## Endpoints

### 1. Koordinaten-basiert (aus Teil 1)
GET /forecast?lat={lat}&lon={lon}

### 2. Adress-basiert (Teil 2)
GET /forecast?country={country}&city={city}&street={street}&housenumber={housenumber}

---

**Beispiele**
```
# Koordinaten-Anfrage
curl.exe -u user:{password} "http://localhost:8080/forecast?lat=50.9375&lon=6.9603"

# Adress-Anfrage
curl.exe -u user:{password} "http://localhost:8080/forecast?country=Germany&city=Cologne&street=Domkloster&housenumber=4"  

```