# Ereditarietà in Java

Questo progetto accompagna le slide **Ereditarietà** del modulo di Laboratorio di Algoritmi e Strutture Dati.

Il codice sviluppa una piccola gerarchia di conti bancari. L'obiettivo non è costruire un'applicazione bancaria realistica, ma usare un dominio semplice per osservare i concetti fondamentali dell'ereditarietà in Java.

## Obiettivi didattici

Nel progetto si possono riconoscere i seguenti concetti:

- definizione di una superclasse e di più sottoclassi tramite `extends`;
- riuso di stato e comportamento comuni nella superclasse;
- aggiunta di nuovo stato e nuovi metodi nelle sottoclassi;
- ridefinizione di metodi ereditati;
- uso di `super(...)` nei costruttori delle sottoclassi;
- uso di `super.metodo(...)` per richiamare l'implementazione della superclasse;
- differenza tra campi `private` della superclasse e metodi pubblici/protetti usabili dalle sottoclassi;
- classi e metodi astratti;
- conversione da sottoclasse a superclasse;
- variabili polimorfe e selezione posticipata dei metodi;
- differenza tra metodi ridefiniti e metodi semplicemente ereditati;
- uguaglianza, hashing e ordinamento naturale ereditati dalla superclasse;
- test manuali con `main` e test automatici con JUnit 5.

## Gerarchia delle classi

La gerarchia principale è:

```text
BankAccount                  classe astratta
├── SavingsAccount           conto di risparmio con interessi mensili
│   └── TimedAccount          conto di risparmio vincolato
└── CheckingAccount           conto corrente con commissioni mensili
```

### `BankAccount`

`BankAccount` rappresenta il comportamento comune a tutti i conti dell'esempio.

Contiene lo stato comune:

- `saldo`, modificabile tramite operazioni di deposito e prelievo;
- `nome`, modificabile con un setter;
- `iban`, immutabile e usato come identità logica del conto.

La classe è `abstract` perché il metodo `endOfMonth()` non ha una buona implementazione generale. Ogni conto concreto deve specificare che cosa accade alla fine del mese.

La classe definisce inoltre:

- `deposit(double)`;
- `withdraw(double)`;
- `transfer(double, BankAccount)`;
- `equals(Object)` e `hashCode()` basati sull'IBAN;
- `compareTo(BankAccount)` basato sull'ordinamento lessicografico degli IBAN;
- `toString()` per una descrizione leggibile.

Il metodo `transfer` è un esempio importante di polimorfismo: riceve un parametro di tipo `BankAccount`, ma può lavorare con qualunque sottoclasse concreta. Le chiamate a `withdraw` e `deposit` vengono risolte a runtime in base alla classe effettiva degli oggetti coinvolti.

### `SavingsAccount`

`SavingsAccount` estende `BankAccount` aggiungendo un tasso di interesse annuale.

Implementa `endOfMonth()` calcolando l'interesse mensile e accreditandolo sul saldo. Il campo `saldo` è privato nella superclasse, quindi la sottoclasse non lo modifica direttamente: usa il metodo ereditato `deposit`.

### `CheckingAccount`

`CheckingAccount` estende `BankAccount` aggiungendo:

- `numeroOperazioni`, cioè il numero di operazioni riuscite nel mese;
- `costoPerOperazione`, cioè la commissione da applicare per ogni operazione.

Ridefinisce `deposit` e `withdraw` per riusare il comportamento della superclasse e aggiungere il conteggio delle operazioni.

Il punto didattico principale è l'uso di `super.deposit(importo)` e `super.withdraw(importo)`. Se dentro `CheckingAccount.deposit` si chiamasse semplicemente `deposit(importo)`, verrebbe richiamato di nuovo lo stesso metodo ridefinito, causando ricorsione infinita.

Alla fine del mese `endOfMonth()` calcola le commissioni dovute, le preleva dal saldo e azzera il contatore.

### `TimedAccount`

`TimedAccount` estende `SavingsAccount` e introduce un contratto di vincolo.

Oltre all'interesse di base ereditato da `SavingsAccount`, il conto accumula un interesse aggiuntivo per un certo numero di mesi. Se il vincolo arriva a scadenza senza prelievi, gli interessi aggiuntivi accumulati vengono accreditati e il contratto diventa non attivo.

Se viene effettuato un prelievo mentre il vincolo è ancora in corso, il conteggio dei mesi riparte e gli interessi aggiuntivi accumulati fino a quel momento vengono persi.

Questo esempio mostra una gerarchia a tre livelli e il riuso esplicito del comportamento di fine mese della superclasse tramite `super.endOfMonth()`.

### `InsufficientFundException`

`InsufficientFundException` è un'eccezione non controllata usata quando un'operazione richiede più denaro del saldo disponibile.

## Test manuale

La classe `BankAccountTestAMano` contiene un metodo `main` che simula alcuni mesi di vita di tre conti diversi dichiarati tutti con tipo `BankAccount`.

Questo rende visibile la selezione posticipata: la chiamata

```java
account.endOfMonth();
```

ha la stessa forma per tutti i conti, ma l'implementazione effettiva dipende dalla classe reale dell'oggetto.

## Test JUnit

I test automatici si trovano in `src/test/java`.

- `BankAccountTest` verifica il comportamento comune ereditato dalle sottoclassi: costruttore, deposito, prelievo, trasferimento, uguaglianza, hashing e ordinamento.
- `SavingsAccountTest` verifica il calcolo degli interessi e la validazione del tasso.
- `CheckingAccountTest` verifica il conteggio delle operazioni, le commissioni e il fatto che le operazioni fallite non vengano conteggiate.
- `TimedAccountTest` verifica la gestione del vincolo, la scadenza del contratto, il rinnovo e il reset del vincolo in caso di prelievo.

I test sono commentati in italiano per evidenziare il collegamento tra comportamento verificato e concetti delle slide.

## Esecuzione

Dalla radice del progetto:

```text
mvn test
```

Per compilare senza eseguire i test:

```text
mvn compile
```

Per eseguire il test manuale, avviare dall'IDE il metodo `main` della classe:

```text
BankAccountTestAMano
```

## Note didattiche

Il progetto usa volutamente costrutti di base. La configurazione Maven richiede la compilazione con livello Java 8, coerentemente con le esercitazioni e con la prova di laboratorio.

Il codice non vuole proporre un modello bancario completo. Alcune scelte, come l'uso dell'IBAN come unica identità logica o il calcolo semplificato degli interessi mensili, servono a mantenere l'esempio concentrato sui concetti di ereditarietà, riuso e polimorfismo.
