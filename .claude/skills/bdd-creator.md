---
name: "bdd-creator"
description: "Tworzy frontendowe scenariusze BBD w oparciu o dane z JIRy, 
gdy ktos poprosi o stworzenie scenariuszy na podstawie linku bądz numeru ticketu NEXTGEN-7662
i zapisuje je w pliku .feature w formacie BDD, Tytuł powinien opisywać zachowanie użytkwonika
i idealnie zaiwerać jedynie trzy stepy Given, When, Then, bez implementacji stepów"
---

Kiedy tworzysz scenariusze BDD w oparciu o dane z JIRA,

1. Odwiedź strone ticketu, adres ticketu zaczyna się od https://lyrecomanagement.atlassian.net/browse/ + numer ticketu np https://lyrecomanagement.atlassian.net/browse/NEXTGEN-7662
2. Przeanalizuj dane z Jira
3. Zapisz scenariusze w pliku .feature w folderze src/test/resources/features w formacie BDD podążajać za wzorcem:
wy
## Język:
Gherkin, Angielski

## Tytuł scenariusza:
Powinien zmieścić się w jednej linii, zaczynać się od numery ticketu np: NEXTGEN-7662 - dalsza część tytułu
Powinien opisywać konkretne zachowanie użytkownika

## Givens
Opisują one stan systemu przed wykonaniem scenariusza
Niech zaczynają się od There is/are ...

## Whens
Opisują akcje użytkownika
Niech zaczynają się od I/User ...

## Then
Trzymaj się schematu, There is/are ... 

## Ands
Staraj się rzadko używać, a jeśli to konieczne to zawsze po jednym z innych kluczowych keywordow [Given, When, Then]

Wykorzystuj Scenarios Outline tak często jak się da
parametryzuj stepy tak często jak się da

Niech scenariujsze, będą zgrabne, czytelne i zrozumiałe.
Powinny być bardziej biznesowe niż techniczne.
Nie duplikuj scenariuszy! 1 scenariusz = 1 test. 
Jęsli w jednym scenariuszu sprawdzasz już wymaganie testowe drugiego scenariusza to niepotrzebny scenariusz możesz pominąć