package cat.uvic.teknos.dam.controlbox.client.exceptions;

/**
 * Excepción personalizada para errores ocurridos en el cliente de productos.
 * Envuelve errores de comunicación, serialización o respuesta inesperada del servidor.
 */
public class ClientException extends RuntimeException {

    public ClientException() {
        super();
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    public ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
