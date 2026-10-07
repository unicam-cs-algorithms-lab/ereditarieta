package it.unicam.cs.asdl.slides.ereditarieta;

/**
 * Piccolo test manuale della gerarchia di conti bancari.
 * <p>
 * Questa classe non sostituisce i test JUnit: serve solo per eseguire da
 * {@code main} una simulazione leggibile e osservare il polimorfismo. Le tre
 * variabili sono dichiarate con il tipo astratto {@link BankAccount}, ma gli
 * oggetti reali appartengono a sottoclassi diverse. La chiamata a
 * {@code endOfMonth()} viene risolta a runtime in base alla classe effettiva
 * dell'oggetto.
 */
public class BankAccountTestAMano {

    /**
     * Esegue una semplice simulazione mensile su tre tipi diversi di conto.
     *
     * @param args argomenti da linea di comando, non utilizzati
     */
    public static void main(String[] args) {
        int i = 0;
        BankAccount lucaCheck = new CheckingAccount(500.00, "Luca T",
                "IT0174297492179847237", 1.5);
        BankAccount lucaSave = new SavingsAccount(5000.00, "Luca T",
                "IT0174297492179842423", 3.0);
        BankAccount lucaVincolato = new TimedAccount(10000.00, "Luca T",
                "IT0174297492123425636", 1.5, 5.0, 12);
        do {
            System.out.println("Checking saldo al mese " + i + " del conto "
                    + lucaCheck.getIban() + " è " + lucaCheck.getSaldo());
            System.out.println("Saving saldo al mese " + i + " del conto "
                    + lucaSave.getIban() + " è " + lucaSave.getSaldo());
            System.out.println("Vincolato saldo al mese " + i + " del conto "
                    + lucaVincolato.getIban() + " è "
                    + lucaVincolato.getSaldo());

            /*
             * lucaVincolato è dichiarato come BankAccount: attraverso quel tipo
             * posso chiamare solo i metodi dichiarati nella superclasse. Per
             * usare isActive(), specifico di TimedAccount, è necessario un cast.
             */
            TimedAccount lucaVincolatoCast = (TimedAccount) lucaVincolato;
            if (!lucaVincolatoCast.isActive())
                System.out.println("Contratto di vincolo non attivo");

            lucaCheck.withdraw(50);
            lucaCheck.deposit(30);

            /*
             * Selezione posticipata: le tre chiamate hanno la stessa forma, ma
             * eseguono implementazioni diverse perché gli oggetti reali hanno
             * classi diverse.
             */
            lucaCheck.endOfMonth();
            lucaSave.endOfMonth();
            lucaVincolato.endOfMonth();
            i++;
        } while (i <= 18);
    }
}
