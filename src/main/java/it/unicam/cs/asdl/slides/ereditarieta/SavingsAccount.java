package it.unicam.cs.asdl.slides.ereditarieta;

/**
 * Conto di risparmio che accredita interessi alla fine di ogni mese.
 * <p>
 * Questa classe estende {@link BankAccount} aggiungendo un tasso di interesse
 * annuale. Mostra un caso semplice di sottoclasse che riusa lo stato e il
 * comportamento comune della superclasse e fornisce l'implementazione del
 * metodo astratto {@link #endOfMonth()}.
 */
public class SavingsAccount extends BankAccount {

    private double interestRate;

    /**
     * Costruisce un conto di risparmio con tasso di interesse di default pari
     * all'1% annuo.
     *
     * @param saldo saldo iniziale, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice IBAN del conto
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo} è negativo
     */
    public SavingsAccount(double saldo, String nome, String iban) {
        super(saldo, nome, iban);
        this.interestRate = 1.0;
    }

    /**
     * Costruisce un conto di risparmio con tasso di interesse specificato.
     *
     * @param saldo saldo iniziale, non negativo
     * @param nome nome dell'intestatario
     * @param iban codice IBAN del conto
     * @param interestRate tasso di interesse annuale percentuale, non negativo
     * @throws NullPointerException se {@code nome} o {@code iban} sono
     *         {@code null}
     * @throws IllegalArgumentException se {@code saldo} o {@code interestRate}
     *         sono negativi
     */
    public SavingsAccount(double saldo, String nome, String iban,
                          double interestRate) {
        super(saldo, nome, iban);
        checkInterestRate(interestRate);
        this.interestRate = interestRate;
    }

    /**
     * Calcola e accredita gli interessi mensili.
     * <p>
     * Il tasso memorizzato è annuale; per semplicità l'interesse mensile viene
     * calcolato dividendo per 12 e assumendo che il saldo sia rimasto costante
     * durante il mese.
     */
    @Override
    public void endOfMonth() {
        double interesse = (getSaldo() * interestRate / 100) / 12;
        /*
         * Il campo saldo è private nella superclasse: la sottoclasse non lo
         * modifica direttamente, ma usa il metodo ereditato deposit.
         */
        deposit(interesse);
    }

    /**
     * Restituisce il tasso di interesse annuale.
     *
     * @return tasso di interesse annuale percentuale
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Modifica il tasso di interesse annuale.
     *
     * @param interestRate nuovo tasso di interesse annuale percentuale
     * @throws IllegalArgumentException se {@code interestRate} è negativo
     */
    public void setTassoDiInteresse(double interestRate) {
        checkInterestRate(interestRate);
        this.interestRate = interestRate;
    }

    private static void checkInterestRate(double interestRate) {
        if (interestRate < 0)
            throw new IllegalArgumentException(
                    "Tentativo di assegnare un tasso di interesse negativo");
    }
}
