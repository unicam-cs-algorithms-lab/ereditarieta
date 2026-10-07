package it.unicam.cs.asdl.slides.ereditarieta;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test della sottoclasse SavingsAccount.
 */
public class SavingsAccountTest {

    @Test
    public void testCostruttoreDefault() {
        SavingsAccount conto = new SavingsAccount(1200, "Luca", "IT001");

        assertEquals(1200, conto.getSaldo(), 0.000001);
        assertEquals(1.0, conto.getInterestRate(), 0.000001);
    }

    @Test
    public void testCostruttoreConTassoSpecifico() {
        SavingsAccount conto = new SavingsAccount(1200, "Luca", "IT001", 12);

        assertEquals(12, conto.getInterestRate(), 0.000001);
    }

    @Test
    public void testTassoNegativoNonAccettato() {
        assertThrows(IllegalArgumentException.class,
                () -> new SavingsAccount(100, "Luca", "IT001", -1));

        SavingsAccount conto = new SavingsAccount(100, "Luca", "IT001", 1);
        assertThrows(IllegalArgumentException.class,
                () -> conto.setTassoDiInteresse(-1));
    }

    @Test
    public void testEndOfMonthAccreditaInteresseMensile() {
        SavingsAccount conto = new SavingsAccount(1200, "Luca", "IT001", 12);

        conto.endOfMonth();

        // Il tasso è annuale: 12% di 1200 è 144 all'anno, quindi 12 al mese.
        assertEquals(1212, conto.getSaldo(), 0.000001);
    }

    @Test
    public void testSetTassoDiInteresse() {
        SavingsAccount conto = new SavingsAccount(1200, "Luca", "IT001", 0);
        conto.setTassoDiInteresse(6);
        conto.endOfMonth();

        assertEquals(1206, conto.getSaldo(), 0.000001);
    }
}
