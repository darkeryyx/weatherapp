# Weather Forecast Service - Teil 1

## Beschreibung
REST WebService mit Mock-Wettervorhersagen für gegebene Koordinaten.

## Schnellstart
```bash
#Branch auschecken
git checkout part-1-forecast

# Starten
# Windows PowerShell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw clean install
./mvnw spring-boot:run

#Alternativ IntelliJ IDEA
über den run Button (in WeatherappApplication.java)
```

## API Endpunkt

#### GET /forecast?lat={lat}&lon={lon}
**Parameter:**
- `lat`: Breitengrad (-90 bis 90)
- `lon`: Längengrad (-180 bis 180)

**Beispiele:**
```bash
curl.exe -i "http://localhost:8080/forecast?lat=51.96&lon=7.62"

curl.exe -i "http://localhost:8080/forecast?lat=51.96&lon=7002"
# → HTTP 400 Bad Request mit JSON-Fehlermeldung

```

**Response (200):**
```json
{
  "time": 1661871600,
  "temp": 22.45,
  "feels_like": 24.12,
  "humidity": 65
}
```

## Implementierung
- **Mock-Daten**: Realistische Zufallswerte
- **Validierung**: Koordinaten-Grenzen werden geprüft
- **Fehlerbehandlung**: HTTP 400 bei ungültigen Koordinaten

