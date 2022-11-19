[![Java CI with Maven](https://github.com/kristiania-pgr209-2022/pg209exam-TobiasLiu1990/actions/workflows/maven.yml/badge.svg)](https://github.com/kristiania-pgr209-2022/pg209exam-TobiasLiu1990/actions/workflows/maven.yml)

# PG209 Backend programmering eksamen

Länk till Azure Websites Deployment: https://pg209exam-sg-tl-tobia.azurewebsites.net/

#### Feature lista:
* Kan skapa ny brukere - uppdaterar sidan automatiskt
* Lista över alla brukere
* Välja brukere i listan för att "loggas in"

* När man väljer brukeren i drop-listan så kan man:
  * Ändra på bruker-settings.
    * Fullname: Må innehålle minst 2 "namn" (förnamn, efternamn). Kan innehålla fler namn.
    * E-mail: Må ha @ och en domain. Ex. backend.pgr209@kristiania.backend.no
    * Age: Må vara större än 0
    * Color: Går inte att skriva in icke-existerande färg

  * Se alla konversationer (trådar)
    * När man öppnar en konversation ser man alla som är med och alla meddelanden.
    * Man kan svara på konversationen.
      * Meddelandet man skriver kommer ut direkt.
      * Men man måste refresha/byta konversation för att se sitt namn + tiden man skrev den.
      * Alla andra som är med kan då se samma sak.
      
  * Längst till höger så är ett fält för att skapa ny konversation.
    * Först skrivs titeln in.
    * Sedan kan man välja vem man vill skicka till. (Går inte i fel ordning pga hur primary key tas ut och används)
    * Sedan kan man submitta konversationen.
    * När man har skapat den så refreshas sidan.

![ISeekYouDBDiagram](https://user-images.githubusercontent.com/95290084/202867639-c79f881b-6ddc-4c26-a6f1-333da9d74a7f.png)



## Sjekkliste for innleveringen

* [x] Dere har lest eksamensteksten
* [x] Koden er sjekket inn på github.com/pg209-2022 repository
* [ ] Dere har lastet opp en ZIP-fil lastet ned fra Github
* [x] Dere har committed kode med begge prosjektdeltagernes GitHub-konto (alternativt: README beskriver hvordan dere har jobbet)

## README.md

* [ ] Inneholder link til Azure Websites deployment
* [x] Inneholder en korrekt badge til GitHub Actions
* [ ] Beskriver hva dere har løst utover minimum
* [ ] Inneholder et diagram over databasemodellen

## Koden

* [x] Oppfyller Java kodestandard med hensyn til indentering og navngiving
* [x] Er deployet korrekt til Azure Websites
* [x] Inneholder tester av HTTP og database-logikk
* [x] Bruker Flyway DB for å sette opp databasen
* [ ] Skriver ut nyttige logmeldinger

## Basisfunksjonalitet

* [x] Kan velge hvilken bruker vi skal opptre som
* [x] Viser eksisterende meldinger til brukeren
* [x] Lar brukeren opprette en ny melding
* [x] Lar brukeren svare på meldinger
* [x] For A: Kan endre navn og annen informasjon om bruker
* [x] For A: Meldingslisten viser navnet på avsender og mottakere

## Kvalitet

* [x] Datamodellen er *normalisert* - dvs at for eksempel navnet på en meldingsavsender ligger i brukertallen, ikke i meldingstabellen
* [x] Når man henter informasjon fra flere tabellen brukes join, i stedet for 1-plus-N queries (et for hovedlisten og et per svar for tilleggsinformasjon)
* [x] Det finnes test for alle JAX-RS endpoints og alle DAO-er

