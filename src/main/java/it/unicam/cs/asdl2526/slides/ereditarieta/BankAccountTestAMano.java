package it.unicam.cs.asdl2526.slides.ereditarieta;

/**
 * Semplice classe con main per testare a mano alcune funzionalità delle classi
 * nella gerarchia di BankAccount.
 * 
 * @author Luca Tesei
 *
 */
public class BankAccountTestAMano {
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
            TimedAccount lucaVincolatoCast = (TimedAccount) lucaVincolato;
            if (!lucaVincolatoCast.isActive())
                System.out.println("Contratto di vincolo non attivo");
            lucaCheck.withdraw(50);
            lucaCheck.deposit(30);
            lucaCheck.endOfMonth();
            lucaSave.endOfMonth();
            lucaVincolato.endOfMonth();
            i++;
        } while (i <= 18);
    }
}
