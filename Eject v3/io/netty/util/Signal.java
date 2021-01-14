package io.netty.util;

import java.util.concurrent.ConcurrentMap;

public final class Signal
        extends Error {
    private static final long serialVersionUID = -221145131122459977L;
    private static final ConcurrentMap<String, Boolean> map = ;
    private final UniqueName uname;

    @Deprecated
    public Signal(String paramString) {
        super(paramString);
        this.uname = new UniqueName(map, paramString, new Object[0]);
    }

    public static Signal valueOf(String paramString) {
        return new Signal(paramString);
    }

    public void expect(Signal paramSignal) {
        if (this != paramSignal) {
            throw new IllegalStateException("unexpected signal: " + paramSignal);
        }
    }

    public Throwable initCause(Throwable paramThrowable) {
        return this;
    }

    public Throwable fillInStackTrace() {
        return this;
    }

    public String toString() {
        return this.uname.name();
    }
}




