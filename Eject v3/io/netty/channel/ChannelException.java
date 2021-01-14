package io.netty.channel;

public class ChannelException
        extends RuntimeException {
    private static final long serialVersionUID = 2908618315971075004L;

    public ChannelException() {
    }

    public ChannelException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public ChannelException(String paramString) {
        super(paramString);
    }

    public ChannelException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}




