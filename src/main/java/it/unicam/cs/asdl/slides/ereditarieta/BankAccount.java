package it.unicam.cs.asdl.slides.ereditarieta;

/**
 * Rappresenta il comportamento comune a tutti i conti bancari dell'esempio.
 * <p>
 * La classe è {@code abstract} perché non esiste, nel modello didattico, una
 * regola generale valida per tutti i conti alla fine del mese. Ogni conto
 * concreto deve quindi specificare il proprio comportamento implementando
 * {@link #endOfMonth()}.
 * <p>
 * Questa classe mostra il ruolo di una superclasse: contiene lo stato comune
 * ({@code saldo}, {@code nome}, {@code iban}) e i metodi condivisi
 * ({@link #deposit(double)}, {@link #withdraw(double)} e
 * {@link #transfer(double, BankAccount)}). Le sottoclassi possono riusare questi
 * metodi tramite ereditarietà e, quando necessario, richiamarli esplicitamente
 * con {@code super}.
 */
public abstract class BankAccount implements Comparable<BankAccount> {

    private double saldo;

    private String nome;

    private final String iban;

    /**
     * Costruisce un conto bancario con saldo iniziale, intestatario e IBAN.
     * <p>
     * L'IBAN viene usato come identificatore logico del conto: due conti sono
     * considerati uguali se hanno lo stesso IBAN, indipendentemente dal tipo
     * concreto della sottoclasse.
     *
     * @param saldo saldo iniziale del conto, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice identificativo del conto, immutabile
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo} è negativo
     */
    public BankAccount(double saldo, String nome, String iban) {
        if (nome == null || iban == null)
            throw new NullPointerException(
                    "Tentativo di creare un conto con nome o iban null");
        if (saldo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare un conto con un saldo iniziale negativo");
        this.saldo = saldo;
        this.nome = nome;
        this.iban = iban;
    }

    /**
     * Restituisce il nome dell'intestatario.
     *
     * @return il nome dell'intestatario del conto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Modifica il nome dell'intestatario del conto.
     * <p>
     * Il nome appartiene allo stato modificabile dell'oggetto, ma non alla sua
     * identità logica: l'identità è determinata dall'IBAN.
     *
     * @param nome nuovo nome dell'intestatario
     * @throws NullPointerException se {@code nome} è {@code null}
     */
    public void setNome(String nome) {
        if (nome == null)
            throw new NullPointerException(
                    "Tentativo di assegnare un nome nullo");
        this.nome = nome;
    }

    /**
     * Restituisce il saldo corrente.
     *
     * @return il saldo corrente del conto
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Restituisce l'IBAN del conto.
     *
     * @return l'IBAN immutabile del conto
     */
    public String getIban() {
        return iban;
    }

    /**
     * Confronta questo conto con un altro oggetto secondo l'identità logica.
     * <p>
     * Nel modello dell'esempio l'IBAN è la chiave primaria: due oggetti conto,
     * anche di sottoclassi diverse, sono logicamente lo stesso conto se hanno lo
     * stesso IBAN.
     *
     * @param obj oggetto con cui confrontare questo conto
     * @return {@code true} se {@code obj} è un conto bancario con lo stesso
     *         IBAN, {@code false} altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof BankAccount))
            return false;
        BankAccount other = (BankAccount) obj;
        return this.iban.equals(other.iban);
    }

    /**
     * Restituisce un codice hash coerente con {@link #equals(Object)}.
     * <p>
     * Poiché {@code equals} usa soltanto l'IBAN, anche {@code hashCode} deve
     * dipendere soltanto dall'IBAN. Così due conti uguali secondo {@code equals}
     * hanno sempre lo stesso hash.
     *
     * @return codice hash del conto
     */
    @Override
    public int hashCode() {
        return iban.hashCode();
    }

    /**
     * Esegue le operazioni specifiche del conto alla fine del mese.
     * <p>
     * Questo metodo è astratto: la superclasse stabilisce che ogni conto deve
     * avere un comportamento di fine mese, ma non ne conosce l'implementazione
     * generale. Le sottoclassi concrete sono obbligate a fornirla.
     */
    public abstract void endOfMonth();

    /**
     * Deposita un importo nel conto.
     * <p>
     * Il metodo modifica direttamente il campo privato {@code saldo}. Una
     * sottoclasse non può accedere a quel campo, ma può riusare questa logica
     * chiamando {@code super.deposit(importo)}.
     *
     * @param importo ammontare da depositare, non negativo
     * @throws IllegalArgumentException se {@code importo} è negativo
     */
    public void deposit(double importo) {
        if (importo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di deposito di importo negativo");
        saldo += importo;
    }

    /**
     * Preleva un importo dal conto.
     *
     * @param importo ammontare da prelevare, non negativo
     * @throws IllegalArgumentException se {@code importo} è negativo
     * @throws InsufficientFundException se {@code importo} è maggiore del saldo
     *         corrente
     */
    public void withdraw(double importo) {
        if (importo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di prelievo di importo negativo");
        if (importo > saldo)
            throw new InsufficientFundException(
                    "Tentativo di prelevare più del posseduto");
        saldo -= importo;
    }

    /**
     * Confronta i conti secondo l'ordinamento naturale degli IBAN.
     * <p>
     * L'ordinamento naturale è coerente con {@code equals}: due conti hanno
     * {@code compareTo} uguale a zero se e solo se hanno lo stesso IBAN.
     *
     * @param other conto con cui effettuare il confronto
     * @return un valore negativo, zero o positivo se questo conto precede,
     *         coincide o segue {@code other} nell'ordinamento degli IBAN
     * @throws NullPointerException se {@code other} è {@code null}
     */
    @Override
    public int compareTo(BankAccount other) {
        if (other == null)
            throw new NullPointerException(
                    "Tentativo di confrontare un conto con null");
        return this.iban.compareTo(other.iban);
    }

    /**
     * Trasferisce un importo da questo conto a un altro conto.
     * <p>
     * I parametri e il campo {@code altroConto} sono dichiarati con il tipo
     * della superclasse {@code BankAccount}. Questo è un esempio di
     * polimorfismo: il metodo può lavorare con qualunque sottoclasse concreta
     * di {@code BankAccount}. Le chiamate a {@code withdraw} e {@code deposit}
     * sono soggette a selezione posticipata e quindi possono eseguire versioni
     * ridefinite nelle sottoclassi.
     *
     * @param importo valore da trasferire, non negativo
     * @param altroConto conto destinatario del trasferimento
     * @throws NullPointerException se {@code altroConto} è {@code null}
     * @throws IllegalArgumentException se {@code importo} è negativo
     * @throws InsufficientFundException se questo conto non ha saldo sufficiente
     */
    public void transfer(double importo, BankAccount altroConto) {
        if (altroConto == null)
            throw new NullPointerException(
                    "Tentativo di trasferire un importo su un conto null");
        if (importo < 0)
            throw new IllegalArgumentException(
                    "Tentativo di trasferire un importo negativo su un altro conto");
        this.withdraw(importo);
        altroConto.deposit(importo);
    }

    /**
     * Restituisce una descrizione testuale utile nei test manuali e nel debug.
     *
     * @return stringa che descrive intestatario, IBAN e saldo corrente
     */
    @Override
    public String toString() {
        return "BankAccount[iban=" + iban + ", nome=" + nome + ", saldo="
                + saldo + "]";
    }
}
