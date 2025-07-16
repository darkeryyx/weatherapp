# WeatherApp – Take-Home Assignment Teil 2

Dieses Branch erweitert den **Forecast-Service** (Teil 1) um **Adress-Geocoding** via OpenCage und 5-Tage-Forecast via OpenWeatherMap.
- **OpenCage Geocoder**: Adresse → Koordinaten
- **OpenWeatherMap 5-day Forecast API**: Koordinaten → Wetterdaten im 3-Stunden-Takt

### WICHTIG: Umgebung konfigurieren

Bevor die App startet, müssen zwei Umgebungsvariablen gesetzt werden:
```
# Windows PowerShell
$Env:OPENCAGE_API_KEY="HIER_DEIN_OPENCAGE_KEY"
$Env:OPENWEATHER_API_KEY="HIER_DEIN_OPENWEATHER_KEY"

# macOS/Linux (Bash)
export OPENCAGE_API_KEY="HIER_DEIN_OPENCAGE_KEY"
export OPENWEATHER_API_KEY="HIER_DEIN_OPENWEATHER_KEY"
```

Projekt starten:
```bash
# Branch wechseln
git checkout part-2-geocoding

# Build & Run (Windows PowerShell)
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run

# Build & Run (macOS/Linux)
./mvnw clean install
./mvnw spring-boot:run

# Alternativ in IntelliJ IDEA:
# Run-Button in WeatherappApplication.java klicken
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
curl "http://localhost:8080/forecast?lat=50.9375&lon=6.9603"

# Adress-Anfrage
curl "http://localhost:8080/forecast?country=Germany&city=Cologne&street=Domkloster&housenumber=4"

# Anfragen auch mit curl.exe möglich
```