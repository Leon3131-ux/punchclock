# M223: Punchclock
Das ist eine Implementation der Beispielapplikation für das Modul 223

## Loslegen
1. Projekt in IntelliJ Klonen oder Importieren
2. Gradle Build ausführen
3. Neue Spring Run Configuration erstellen und PunchclockApplication als main Klasse verwenden
4. Run Configuration laufen lassen

## Datenbank
Die Datenbank ist eine H2 Datenbank.

Falls die Datenbank nicht nach jedem shutdown gelöscht werden soll,
muss dies entsprechend in den `application.properties` festgelegt werden.

Dazu den Schlüssel `spring.jpa.hibernate.ddl-auto` auf `create` setzten
und nach dem ersten start der Applikation `db.initialize` auf `false` setzen.

### Initialisierungs-Skript
Die klasse `InitDb` initialisiert die Permissions und erstellt folgende Benutzer:
1. Benutzername `superadmin`, Passwort `1`, Permissions `SUPER_ADMINISTRATE`
2. Benutzername `admin`, Passwort `1`, Permissions `ADMINISTRATE`
3. Benutzername `user`, Passwort `1`, Permissions: keine

## Beschreibung
Mit Punchclock können Benutzer ihre Ankunfts- und Gehzeiten erfassen.
Benutzer können auch ihre Einträge noch nachbearbeiten oder löschen.
Auf `/home` seite, wird, falls vorhanden, der aktuelle Eintrag für den Rest des Tages angezeigt.

Administratoren können Benutzer innerhalb ihrer Firma verwalten und deren Einträge dazu.

Die Benutzerverwaltung für Administratoren und Super-Administratoren ist auf `/users` verfügbar.

Die Eintragsverwaltung für alle Benutzer ist auf `/entries` verfügbar.

Super-Administratoren können zusätzlich noch Firem auf `/companies` verwalten.

*Die Pfäde sind nur für das Angular-Frontend gemeint


