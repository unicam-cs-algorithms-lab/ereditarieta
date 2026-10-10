package it.unicam.cs.asdl.slides.ereditarieta;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test dei comportamenti comuni definiti nella superclasse BankAccount.
 * <p>
 * Anche se BankAccount è astratta, i suoi metodi concreti vengono ereditati e
 * possono essere verificati usando sottoclassi concrete come SavingsAccount e
 * CheckingAccount.
 */
public class BankAccountTest {

    @Test
    public void testCostruttoreRifiutaDatiNonValidi() {
        // Una sottoclasse concreta deve comunque rispettare i controlli
        // definiti nel costruttore della superclasse.
        assertThrows(NullPointerException.class,
                () -> new SavingsAccount(100, null, "IT001"));
        assertThrows(NullPointerException.class,
                () -> new SavingsAccount(100, "Luca", null));
        assertThrows(IllegalArgumentException.class,
                () -> new SavingsAccount(-1, "Luca", "IT001"));
    }

    @Test
    public void testDepositEWithdrawEreditati() {
        SavingsAccount conto = new SavingsAccount(100, "Luca", "IT001", 0);

        // deposit e withdraw sono implementati nella superclasse e sono
        // direttamente disponibili sulla sottoclasse.
        conto.deposit(50);
        assertEquals(150, conto.getSaldo(), 0.000001);

        conto.withdraw(20);
        assertEquals(130, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testDepositEWithdrawRifiutanoImportiNonValidi() {
        SavingsAccount conto = new SavingsAccount(100, "Luca", "IT001", 0);

        assertThrows(IllegalArgumentException.class, () -> conto.deposit(-1));
        assertThrows(IllegalArgumentException.class, () -> conto.withdraw(-1));
        assertThrows(InsufficientFundsException.class,
                () -> conto.withdraw(101));
    }

    @Test
    public void testSetNomeNonCambiaIdentitaLogica() {
        SavingsAccount conto = new SavingsAccount(100, "Luca", "IT001", 0);
        SavingsAccount stessoIban = new SavingsAccount(200, "Altro", "IT001",
                5);

        conto.setNome("Mario");

        // L'identità logica è l'IBAN, non il nome dell'intestatario o il saldo.
        assertEquals(stessoIban, conto);
        assertEquals(stessoIban.hashCode(), conto.hashCode());
        assertThrows(NullPointerException.class, () -> conto.setNome(null));
    }

    @Test
    public void testEqualsHashCodeECompareToUsanoIban() {
        BankAccount conto1 = new SavingsAccount(100, "Luca", "IT001", 1);
        BankAccount conto2 = new CheckingAccount(50, "Mario", "IT001", 2);
        BankAccount conto3 = new SavingsAccount(100, "Luca", "IT002", 1);

        // Due oggetti di sottoclassi diverse possono rappresentare lo stesso
        // conto se hanno lo stesso IBAN.
        assertEquals(conto1, conto2);
        assertEquals(conto1.hashCode(), conto2.hashCode());
        assertEquals(0, conto1.compareTo(conto2));

        assertNotEquals(conto1, conto3);
        assertTrue(conto1.compareTo(conto3) < 0);
        assertThrows(NullPointerException.class, () -> conto1.compareTo(null));
    }

    @Test
    public void testTransferUsaSelezionePosticipata() {
        CheckingAccount sorgente = new CheckingAccount(100, "Sorgente",
                "IT001", 1);
        CheckingAccount destinazione = new CheckingAccount(50, "Dest",
                "IT002", 1);

        sorgente.transfer(10, destinazione);

        assertEquals(90, sorgente.getSaldo(), 0.000001);
        assertEquals(60, destinazione.getSaldo(), 0.000001);

        // transfer è definito nella superclasse, ma chiama withdraw e deposit.
        // Poiché gli oggetti reali sono CheckingAccount, vengono eseguite le
        // versioni ridefinite, che contano le operazioni da addebitare a fine
        // mese.
        sorgente.endOfMonth();
        destinazione.endOfMonth();
        assertEquals(89, sorgente.getSaldo(), 0.000001);
        assertEquals(59, destinazione.getSaldo(), 0.000001);
    }

    @Test
    public void testTransferRifiutaDestinatarioONumeroNonValido() {
        BankAccount conto = new SavingsAccount(100, "Luca", "IT001", 0);
        BankAccount altro = new SavingsAccount(0, "Mario", "IT002", 0);

        assertThrows(NullPointerException.class, () -> conto.transfer(10, null));
        assertThrows(IllegalArgumentException.class,
                () -> conto.transfer(-1, altro));
        assertThrows(InsufficientFundsException.class,
                () -> conto.transfer(101, altro));
    }
}
