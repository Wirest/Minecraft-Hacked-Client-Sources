package dev.astroclient.client;

public class ClientRuntimeException extends RuntimeException {

    public ClientRuntimeException() {
        super("A fatal error occurred within the Client");
    }

    public ClientRuntimeException(String cause) {
        super(cause);
    }

}
