package io.netty.util;

public abstract interface ResourceLeak {
    public abstract void record();

    public abstract boolean close();
}




