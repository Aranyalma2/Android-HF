# Házi feladat specifikáció

Információk [itt](https://viauac00.github.io/laborok/hf)

## Mobil- és webes szoftverek
### 2022.10.23
### Bringázz okosan
### Németh Bálint - (MJB2CX)
### nemeth.balint.02@edu.bme.hu
### Laborvezető: Szabó Liza

## Bemutatás

Az alkalmazás lényege, hogy a kerékpárra rögzítve az eszközt, képest aktuális sebesség, gyorsulás és idő adatok megjelenítésére és naplózására. Legfőbb felhasználása a számomra, hogy aktuális adatokat láthassak folyamatosan kerékpározás közben.

## Főbb funkciók

Az alkalmazás fő activity-ről indítható a tevékenység, beállítások menü és megnyithatóak a korábbi mentett tevékenységek. Az éppen aktív sport egy gombra befejezhető, és ilyenkor mentésre kerülnek az adatok.
Az elindított bringázás alatt megjelenik az aktuális sebesség és gyorsulás adatok. Aktuális idő, megtett táv és tevékenység kezdete óta eltelt idő.
Beállítani az alakalmazás nyelvét (eltérő a rendszertől), mértékegység formátumot lehet.
A korábbi sport adatok lista menüben sorolódnek fel, ahonnan lekattintva azt részletes adatokat lehet megnézni. Táv, max sebesség, átlag sebesség, időtartam.
Lehetőségunk van megosztani ezen adatokat barátainkkal más alkalmazásokon keresztül.

## Választott technológiák:

- Fragments
- RecyclerView
- Room adatbázis tárolás (History)
- SharedPreferences (Settings)
- Intent (Share)
