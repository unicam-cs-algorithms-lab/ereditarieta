package it.unicam.cs.asdl.slides.ereditarieta;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test della sottoclasse TimedAccount.
 */
public class TimedAccountTest {

    @Test
    public void testCostruttoriRifiutanoParametriNonValidi() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimedAccount(100, "Luca", "IT001", -1));
        assertThrows(IllegalArgumentException.class,
                () -> new TimedAccount(100, "Luca", "IT001", 1, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new TimedAccount(100, "Luca", "IT001", 1, 1, -1));
    }

    @Test
    public void testVincoloDiUnMeseScadeAlPrimoFineMese() {
        TimedAccount conto = new TimedAccount(1200, "Luca", "IT001", 12, 12,
                1);

        conto.endOfMonth();

        // Interesse base: 12. Interesse vincolo: 12% annuo calcolato sul saldo
        // dopo l'interesse base, quindi 12.12. Totale: 1224.12.
        assertFalse(conto.isActive());
        assertEquals(1224.12, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testVincoloDiDueMesiRestaAttivoDopoUnMese() {
        TimedAccount conto = new TimedAccount(1200, "Luca", "IT001", 12, 12,
                2);

        conto.endOfMonth();

        assertTrue(conto.isActive());
        assertEquals(1212, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testVincoloDiDueMesiAccreditaInteressiAllaScadenza() {
        TimedAccount conto = new TimedAccount(1200, "Luca", "IT001", 12, 12,
                2);

        conto.endOfMonth();
        conto.endOfMonth();

        assertFalse(conto.isActive());
        assertEquals(1248.4812, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testPrelievoFaRipartireIlVincolo() {
        TimedAccount conto = new TimedAccount(1200, "Luca", "IT001", 12, 12,
                2);

        conto.endOfMonth();
        conto.withdraw(100);
        conto.endOfMonth();

        // Il prelievo ha rotto il vincolo e il conteggio è ripartito. Dopo un
        // solo nuovo mese il contratto deve quindi essere ancora attivo.
        assertTrue(conto.isActive());

        conto.endOfMonth();
        assertFalse(conto.isActive());
    }

    @Test
    public void testRinnovoSoloDopoScadenza() {
        TimedAccount conto = new TimedAccount(1200, "Luca", "IT001", 12, 12,
                1);

        assertThrows(IllegalStateException.class,
                () -> conto.renewContract(1, 1));

        conto.endOfMonth();
        assertFalse(conto.isActive());

        conto.renewContract(1, 6);
        assertTrue(conto.isActive());

        conto.endOfMonth();
        assertFalse(conto.isActive());
    }

    @Test
    public void testRinnovoRifiutaParametriNonValidi() {
        TimedAccount conto = new TimedAccount(1200, "Luca", "IT001", 0, 0,
                0);
        conto.endOfMonth();

        assertThrows(IllegalArgumentException.class,
                () -> conto.renewContract(-1, 1));
        assertThrows(IllegalArgumentException.class,
                () -> conto.renewContract(1, -1));
    }
}
