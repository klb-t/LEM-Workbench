# Ekosystem — koncepcje współpracy

> 2026-09-30 · Notatka z brainstormu. Możliwe kierunki, nie opis gotowych integracji ani zlecenie ich wdrożenia.

## 1. Wspólny opis ekosystemu

Projekty rozwijamy jako ekosystem wzajemnie użytecznych zdolności, a nie sztywny łańcuch aplikacji o rozłącznych rolach. Każdy może udostępniać innym wiedzę, narzędzia, sposoby interakcji i doświadczenia, a także z nich korzystać. Współpraca nie wymaga rezygnacji z samodzielnej użyteczności projektów.

**Najbardziej bezpośredni kierunek to ChatADHD jako interfejs do AGEDS oraz do części lub całości WatchDoga.** Nie tylko do zadawania pytań o wyniki, ale również do pracy z materiałem i kierowania zadaniami. Dalej można rozważać ChatADHD jako interfejs do pozostałych projektów. Szersza filozofia dopuszcza wszelkie możliwe powiązania interfejsów z danymi i sterowaniem: rozmowę, głos, graf, widok przestrzenny, gesty czy urządzenia. To horyzont koncepcyjny, nie obecny plan implementacji; czat nie musi zastępować innych interfejsów.

**Przepływ jest dwukierunkowy.** iOmatrix nie jest wyłącznie wejściem i wyjściem: może korzystać ze struktur wiedzy i pamięci rozwijanych w ekosystemie, np. do zapisywania nauczonych rzeczy o użytkowniku, jego słowniku, preferencjach, otoczeniu i sposobach działania. Wiedza ta nie musi należeć do interfejsu ChatADHD. Analogicznie inne projekty mogą wzajemnie korzystać ze swoich metod i doświadczeń.

Wspólne obszary do rozważenia to pamięć i kontekst pracy, schowek dla grafów i innych struktur, zaznaczanie nieostre, przechodzenie między reprezentacjami, pamięć korekt i niedokończonych zadań, pochodzenie informacji oraz przenoszenie nauczonych procedur. Obserwacja, wypowiedź użytkownika, hipoteza modelu i wynik działania pozostają rozróżnialne. Współdzielenie nie oznacza automatycznego dostępu do wszystkich danych ani uprawnień do działania.

Mapa obejmuje ChatADHD, iOmatrix (repozytorium Custom-Keyboard-Pro), Loom, AGEDS, WatchDog, program LEM i jego Workbench oraz PixelSpace AR. Otwarta pozostaje także na książkę/meta-książkę, wątki Legal Flow, narzędzia multimedialne i rekonstrukcję scen, agentów i avatar, DevBox i środowiska pracy, analizę archiwów oraz programy badawcze takie jak RCH. Dawny projekt może wrócić jako samodzielny produkt, współdzielona zdolność albo źródło metod; nie oznacza to automatycznego wznowienia wszystkich prac.

Nie rozstrzygamy tutaj jednej aplikacji, bazy, technologii, podziału repozytoriów ani ostatecznego modelu danych. Różne grafy nie muszą mieć tej samej semantyki. Zachowujemy alternatywy i szukamy rzeczywistych korzyści współpracy zamiast łączyć wszystko na siłę. Ta notatka nie zmienia bieżących priorytetów, kontraktów ani kryteriów gotowości funkcji.

## 2. Znaczenie dla LEM Workbench i programu LEM

To repozytorium jest warsztatem eksperymentalnym programu LEM, nie gotową realizacją całej koncepcji reprezentacji i przekształcania wiedzy.

- **Rzeczywiste problemy z ekosystemu:** ChatADHD i Loom dostarczają pytań o strukturę wiedzy i dobór kontekstu; iOmatrix o pamięć użytkownika, intencję i zmianę modalności; AGEDS i WatchDog o pochodzenie, niepewność i interpretację.
- **Przepływ w drugą stronę:** sprawdzone wyniki badań LEM mogą pomagać w reprezentowaniu i przekształcaniu informacji w pozostałych projektach. Hipoteza, obiecujący eksperyment i użyteczna zdolność pozostają różnymi etapami.
- **Książka/meta-książka jako partner badawczy:** zachowanie sensu, humoru, niedopowiedzenia i odrębnych perspektyw przy zmianie języka lub formy może być wymagającym obszarem badań. Nie zakładamy, że sens zawsze da się całkowicie oddzielić od sposobu wyrażenia.
- **Wspólny warsztat eksperymentalny:** WatchDog, RCH i inne programy mogą wymieniać metody porównywania wariantów, przechowywania surowych wyników i zapamiętywania nieudanych prób. Wspólny warsztat nie oznacza wspólnej teorii ani wzajemnego potwierdzania hipotez.
- **Interfejsy do badań:** ChatADHD może pomagać formułować i omawiać eksperymenty, iOmatrix nimi sterować, a PixelSpace i wizualizatory pokazywać relacje i wyniki z różnych perspektyw.

Pozostałe aplikacje mają pozostawać użyteczne bez rozstrzygnięcia całego programu LEM. Ta notatka nie rozszerza deklarowanych możliwości Workbencha; obowiązują reguły rzeczywistych pomiarów i oddzielania surowych wyników od interpretacji.
