package it.unicam.cs.asdl.slides.ereditarieta;

/**
 * Conto corrente che addebita una commissione per ogni operazione effettuata.
 * <p>
 * Questa sottoclasse introduce nuovo stato ({@code numeroOperazioni} e
 * {@code costoPerOperazione}) e ridefinisce {@link #deposit(double)} e
 * {@link #withdraw(double)} per aggiungere il conteggio delle operazioni al
 * comportamento già definito nella superclasse.
 */
public class CheckingAccount extends BankAccount {

    private int numeroOperazioni;

    private double costoPerOperazione;

    /**
     * Costruisce un conto corrente con costo di default pari a 1 euro per
     * operazione.
     *
     * @param saldo saldo iniziale, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice IBAN del conto
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo} è negativo
     */
    public CheckingAccount(double saldo, String nome, String iban) {
        super(saldo, nome, iban);
        this.costoPerOperazione = 1.0;
        this.numeroOperazioni = 0;
    }

    /**
     * Costruisce un conto corrente specificando il costo di ogni operazione.
     *
     * @param saldo saldo iniziale, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice IBAN del conto
     * @param costoPerOperazione costo unitario di una operazione, non negativo
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo} o
     *         {@code costoPerOperazione} sono negativi
     */
    public CheckingAccount(double saldo, String nome, String iban,
                           double costoPerOperazione) {
        super(saldo, nome, iban);
        if (costoPerOperazione < 0)
            throw new IllegalArgumentException(
                    "Tentativo di creare un CheckingAccount con un costo per operazione negativo");
        this.costoPerOperazione = costoPerOperazione;
        this.numeroOperazioni = 0;
    }

    /**
     * Addebita le spese delle operazioni effettuate nel mese e azzera il
     * contatore per il mese successivo.
     * <p>
     * Il prelievo delle commissioni viene eseguito con {@code super.withdraw}:
     * in questo modo si riusa la logica di controllo del saldo della
     * superclasse, ma non si incrementa il contatore delle operazioni.
     *
     * @throws InsufficientFundException se il saldo non copre le commissioni
     */
    @Override
    public void endOfMonth() {
        double costo = this.numeroOperazioni * this.costoPerOperazione;
        super.withdraw(costo);
        this.numeroOperazioni = 0;
    }

    /**
     * Deposita un importo e, se l'operazione riesce, la conta tra le operazioni
     * del mese.
     * <p>
     * La chiamata a {@code super.deposit(importo)} è essenziale: senza
     * {@code super}, una chiamata a {@code deposit(importo)} richiamerebbe di
     * nuovo questo stesso metodo, causando ricorsione infinita.
     *
     * @param importo importo da depositare, non negativo
     * @throws IllegalArgumentException se {@code importo} è negativo
     */
    @Override
    public void deposit(double importo) {
        super.deposit(importo);
        this.numeroOperazioni++;
    }

    /**
     * Preleva un importo e, se l'operazione riesce, la conta tra le operazioni
     * del mese.
     *
     * @param importo importo da prelevare, non negativo
     * @throws IllegalArgumentException se {@code importo} è negativo
     * @throws InsufficientFundException se il saldo non è sufficiente
     */
    @Override
    public void withdraw(double importo) {
        super.withdraw(importo);
        this.numeroOperazioni++;
    }
}
