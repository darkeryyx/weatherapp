# weatherapp

**WeatherApp** ist eine Spring Boot Anwendung, die Wetterprognosen über REST‑Endpoints bereitstellt. Sie verwendet:

- **OpenCage Geocoding API**, um Adressen in Koordinaten umzuwandeln
- **OpenWeatherMap API**, um Wetterdaten im 3‑Stunden‑Intervall abzurufen

Diese README beschreibt den **main**‑Branch mit allgemeinen Voraussetzungen.
# JEDE BRANCH ENTHÄLT EINE EIGENE README MIT DETAILLIERTEN INFORMATIONEN.

---

## Voraussetzungen

- **Java 17** oder höher
- **Maven 3.6+**
- **Netzwerkzugang** zu externen APIs (OpenCage, OpenWeatherMap)

---
## Allgemeine Infos
### Build & Ausführung

In den Branches sind die Befehle mit ```mvnw.cmd``` geschrieben, damit es auf jeden Fall klappt
Es klappt aber auch nur mit ```mvn``` (wenn maven lokal installiert ist)

Die App läuft dann auf `http://localhost:8080` 

## Branch‑Strategie

* **main**: Produktionsreife, stabile Version
* **part-1-forecast**: Basisanwendung mit Zufallswerten
* **part-2-geocoder**:  Anbindung von Webservices
* **part-3-security**: HTTP Basic Authentication Konfiguration
* **part-4-testing**: Unittests



