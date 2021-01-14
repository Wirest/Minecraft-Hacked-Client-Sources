package io.netty.handler.timeout;

public final class ReadTimeoutException
        extends TimeoutException {
    public static final ReadTimeoutException INSTANCE = new ReadTimeoutException();
    private static final long serialVersionUID = 169287984113283421L;
}




