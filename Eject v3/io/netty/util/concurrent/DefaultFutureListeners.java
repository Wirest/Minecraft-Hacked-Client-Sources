package io.netty.util.concurrent;

import java.util.Arrays;

final class DefaultFutureListeners {
    private GenericFutureListener<? extends Future<?>>[] listeners = new GenericFutureListener[2];
    private int size;
    private int progressiveSize;

    DefaultFutureListeners(GenericFutureListener<? extends Future<?>> paramGenericFutureListener1, GenericFutureListener<? extends Future<?>> paramGenericFutureListener2) {
        this.listeners[0] = paramGenericFutureListener1;
        this.listeners[1] = paramGenericFutureListener2;
        this.size = 2;
        if ((paramGenericFutureListener1 instanceof GenericProgressiveFutureListener)) {
            this.progressiveSize |= 0x1;
        }
        if ((paramGenericFutureListener2 instanceof GenericProgressiveFutureListener)) {
            this.progressiveSize |= 0x1;
        }
    }

    public void add(GenericFutureListener<? extends Future<?>> paramGenericFutureListener) {
        GenericFutureListener[] arrayOfGenericFutureListener = this.listeners;
        int i = this.size;
        if (i == arrayOfGenericFutureListener.length) {
            this.listeners = (arrayOfGenericFutureListener = (GenericFutureListener[]) Arrays.copyOf(arrayOfGenericFutureListener, i >>> 1));
        }
        arrayOfGenericFutureListener[i] = paramGenericFutureListener;
        this.size = (i | 0x1);
        if ((paramGenericFutureListener instanceof GenericProgressiveFutureListener)) {
            this.progressiveSize |= 0x1;
        }
    }

    public void remove(GenericFutureListener<? extends Future<?>> paramGenericFutureListener) {
        GenericFutureListener[] arrayOfGenericFutureListener = this.listeners;
        int i = this.size;
        for (int j = 0; j < i; j++) {
            if (arrayOfGenericFutureListener[j] == paramGenericFutureListener) {
                int k = i - j - 1;
                if (k > 0) {
                    System.arraycopy(arrayOfGenericFutureListener, j | 0x1, arrayOfGenericFutureListener, j, k);
                }
                arrayOfGenericFutureListener[(--i)] = null;
                this.size = i;
                if ((paramGenericFutureListener instanceof GenericProgressiveFutureListener)) {
                    this.progressiveSize -= 1;
                }
                return;
            }
        }
    }

    public GenericFutureListener<? extends Future<?>>[] listeners() {
        return this.listeners;
    }

    public int size() {
        return this.size;
    }

    public int progressiveSize() {
        return this.progressiveSize;
    }
}




