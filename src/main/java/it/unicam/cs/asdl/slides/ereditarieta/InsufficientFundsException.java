package it.unicam.cs.asdl.slides.ereditarieta;

/**
 * Eccezione non controllata lanciata quando un'operazione richiede più denaro
 * di quanto sia disponibile sul conto.
 * <p>
 * Nell'esempio è una sottoclasse di {@link RuntimeException}: i metodi che la
 * possono lanciare la documentano nel Javadoc, ma il chiamante non è obbligato
 * dal compilatore a catturarla con {@code try/catch}.
 */
public class InsufficientFundsException extends RuntimeException {

    private static final long serialVersionUID = 6741919390005964432L;

    /**
     * Costruisce l'eccezione senza messaggio specifico.
     */
    public InsufficientFundsException() {
        super();
    }

    /**
     * Costruisce l'eccezione con un messaggio descrittivo.
     *
     * @param message messaggio associato all'eccezione
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}
