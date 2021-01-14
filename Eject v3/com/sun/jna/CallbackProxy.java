package com.sun.jna;

public abstract interface CallbackProxy
        extends Callback {
    public abstract Object callback(Object[] paramArrayOfObject);

    public abstract Class[] getParameterTypes();

    public abstract Class getReturnType();
}




