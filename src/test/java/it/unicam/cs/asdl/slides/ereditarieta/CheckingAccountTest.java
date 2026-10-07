package it.unicam.cs.asdl.slides.ereditarieta;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test della sottoclasse CheckingAccount.
 */
public class CheckingAccountTest {

    @Test
    public void testCostruttoreDefaultECommissioni() {
        CheckingAccount conto = new CheckingAccount(100, "Luca", "IT001");

        conto.deposit(50);
        conto.withdraw(20);
        conto.endOfMonth();

        // Due operazioni riuscite, costo di default 1 euro: saldo finale
        // 100 + 50 - 20 - 2 = 128.
        assertEquals(128, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testCostoOperazioneSpecifico() {
        CheckingAccount conto = new CheckingAccount(100, "Luca", "IT001", 2.5);

        conto.deposit(10);
        conto.withdraw(5);
        conto.endOfMonth();

        assertEquals(100, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testCostoOperazioneNegativoNonAccettato() {
        assertThrows(IllegalArgumentException.class,
                () -> new CheckingAccount(100, "Luca", "IT001", -1));
    }

    @Test
    public void testOperazioneFallitaNonVieneConteggiata() {
        CheckingAccount conto = new CheckingAccount(100, "Luca", "IT001", 5);

        assertThrows(IllegalArgumentException.class, () -> conto.deposit(-1));
        assertThrows(InsufficientFundException.class, () -> conto.withdraw(200));
        conto.endOfMonth();

        // Nessuna delle due operazioni è stata effettuata davvero, quindi non
        // deve essere applicata alcuna commissione.
        assertEquals(100, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testEndOfMonthAzzeraContatoreOperazioni() {
        CheckingAccount conto = new CheckingAccount(100, "Luca", "IT001", 3);

        conto.deposit(0);
        conto.endOfMonth();
        assertEquals(97, conto.getSaldo(), 0.000001);

        conto.endOfMonth();
        // Il secondo fine mese non addebita di nuovo la stessa operazione.
        assertEquals(97, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testCommissioniPossonoEssereInsufficienti() {
        CheckingAccount conto = new CheckingAccount(1, "Luca", "IT001", 2);

        conto.deposit(0);

        assertThrows(InsufficientFundException.class, () -> conto.endOfMonth());
    }
}
