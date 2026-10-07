package it.unicam.cs.asdl.slides.ereditarieta;

/**
 * Conto di risparmio vincolato con un tasso aggiuntivo.
 * <p>
 * La classe estende {@link SavingsAccount} e aggiunge un contratto di vincolo:
 * se non vengono effettuati prelievi per il numero di mesi previsto, alla
 * scadenza vengono accreditati anche gli interessi aggiuntivi accumulati.
 * <p>
 * L'esempio mostra una gerarchia a tre livelli: {@code BankAccount},
 * {@code SavingsAccount}, {@code TimedAccount}. Mostra inoltre come una
 * sottoclasse possa ridefinire un metodo già ereditato, qui
 * {@link #withdraw(double)}, per aggiungere nuovo comportamento.
 */
public class TimedAccount extends SavingsAccount {

    private int numeroMesiVincolo;

    private int mesiRimanenti;

    private double tassoAggiuntivo;

    private boolean contrattoAttivo;

    private double accumuloInteressiVincolo;

    /**
     * Costruisce un conto vincolato con tasso base dell'1%, tasso aggiuntivo
     * del 3% e durata del vincolo pari a 18 mesi.
     *
     * @param saldo saldo iniziale, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice IBAN del conto
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo} è negativo
     */
    public TimedAccount(double saldo, String nome, String iban) {
        this(saldo, nome, iban, 1.0, 3.0, 18);
    }

    /**
     * Costruisce un conto vincolato con tasso base specificato, tasso
     * aggiuntivo del 3% e durata del vincolo pari a 18 mesi.
     *
     * @param saldo saldo iniziale, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice IBAN del conto
     * @param tassoInteresse tasso di interesse annuale di base
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo} o
     *         {@code tassoInteresse} sono negativi
     */
    public TimedAccount(double saldo, String nome, String iban,
                        double tassoInteresse) {
        this(saldo, nome, iban, tassoInteresse, 3.0, 18);
    }

    /**
     * Costruisce un conto vincolato con durata del vincolo di 18 mesi.
     *
     * @param saldo saldo iniziale, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice IBAN del conto
     * @param tassoInteresse tasso di interesse annuale di base
     * @param tassoAggiuntivo tasso annuale aggiuntivo del vincolo
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo},
     *         {@code tassoInteresse} o {@code tassoAggiuntivo} sono negativi
     */
    public TimedAccount(double saldo, String nome, String iban,
                        double tassoInteresse, double tassoAggiuntivo) {
        this(saldo, nome, iban, tassoInteresse, tassoAggiuntivo, 18);
    }

    /**
     * Costruisce un conto vincolato specificando tutti i parametri del
     * contratto.
     *
     * @param saldo saldo iniziale, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice IBAN del conto
     * @param tassoInteresse tasso di interesse annuale di base
     * @param tassoAggiuntivo tasso annuale aggiuntivo del vincolo
     * @param numeroMesiVincolo durata del vincolo in mesi, non negativa
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo}, uno dei tassi o
     *         {@code numeroMesiVincolo} sono negativi
     */
    public TimedAccount(double saldo, String nome, String iban,
                        double tassoInteresse, double tassoAggiuntivo,
                        int numeroMesiVincolo) {
        super(saldo, nome, iban, tassoInteresse);
        checkTassoAggiuntivo(tassoAggiuntivo);
        checkNumeroMesiVincolo(numeroMesiVincolo);
        this.numeroMesiVincolo = numeroMesiVincolo;
        this.mesiRimanenti = numeroMesiVincolo;
        this.tassoAggiuntivo = tassoAggiuntivo;
        this.contrattoAttivo = true;
        this.accumuloInteressiVincolo = 0;
    }

    /**
     * Preleva un importo dal conto.
     * <p>
     * Se il prelievo riesce durante un contratto ancora in corso, il vincolo è
     * rotto: il conteggio dei mesi riparte dall'inizio e gli interessi
     * aggiuntivi accumulati fino a quel momento vengono persi.
     *
     * @param importo importo da prelevare
     * @throws IllegalArgumentException se {@code importo} è negativo
     * @throws InsufficientFundException se il saldo non è sufficiente
     */
    @Override
    public void withdraw(double importo) {
        super.withdraw(importo);
        if (this.contrattoAttivo && this.mesiRimanenti > 0) {
            this.mesiRimanenti = this.numeroMesiVincolo;
            this.accumuloInteressiVincolo = 0;
        }
    }

    /**
     * Accredita l'interesse di base e gestisce il vincolo mensile.
     * <p>
     * La prima istruzione chiama {@code super.endOfMonth()}, cioè il
     * comportamento di fine mese del conto di risparmio. Poi la sottoclasse
     * aggiunge il proprio comportamento specifico: accumulo degli interessi del
     * vincolo, decremento dei mesi rimanenti e accredito finale alla scadenza.
     */
    @Override
    public void endOfMonth() {
        super.endOfMonth();
        if (!this.contrattoAttivo)
            return;

        double interessiVincolo = (this.getSaldo() * this.tassoAggiuntivo / 100)
                / 12;
        this.accumuloInteressiVincolo += interessiVincolo;

        if (this.mesiRimanenti > 1) {
            this.mesiRimanenti--;
        } else {
            /*
             * Alla fine dell'ultimo mese di vincolo accreditiamo gli interessi
             * aggiuntivi accumulati e chiudiamo il contratto. Usiamo
             * super.deposit per riusare la logica della superclasse senza
             * introdurre eventuali comportamenti aggiuntivi di sottoclassi.
             */
            super.deposit(this.accumuloInteressiVincolo);
            this.mesiRimanenti = 0;
            this.contrattoAttivo = false;
        }
    }

    /**
     * Rinnova il contratto di vincolo per un nuovo periodo.
     *
     * @param numeroMesiVincolo nuova durata del vincolo in mesi, non negativa
     * @param tassoAggiuntivo nuovo tasso annuale aggiuntivo, non negativo
     * @throws IllegalStateException se il contratto precedente è ancora attivo
     * @throws IllegalArgumentException se {@code numeroMesiVincolo} o
     *         {@code tassoAggiuntivo} sono negativi
     */
    public void renewContract(int numeroMesiVincolo, double tassoAggiuntivo) {
        if (this.contrattoAttivo)
            throw new IllegalStateException(
                    "Tentativo di rinnovare un contratto non ancora scaduto");
        checkNumeroMesiVincolo(numeroMesiVincolo);
        checkTassoAggiuntivo(tassoAggiuntivo);
        this.numeroMesiVincolo = numeroMesiVincolo;
        this.mesiRimanenti = numeroMesiVincolo;
        this.tassoAggiuntivo = tassoAggiuntivo;
        this.accumuloInteressiVincolo = 0;
        this.contrattoAttivo = true;
    }

    /**
     * Indica se il contratto di vincolo è attualmente attivo.
     *
     * @return {@code true} se il vincolo è attivo, {@code false} altrimenti
     */
    public boolean isActive() {
        return this.contrattoAttivo;
    }

    private static void checkTassoAggiuntivo(double tassoAggiuntivo) {
        if (tassoAggiuntivo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di usare un tasso aggiuntivo negativo");
    }

    private static void checkNumeroMesiVincolo(int numeroMesiVincolo) {
        if (numeroMesiVincolo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di usare un numero di mesi di vincolo negativo");
    }
}
