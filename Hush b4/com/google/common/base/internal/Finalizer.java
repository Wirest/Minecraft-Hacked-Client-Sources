// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base.internal;

import java.lang.reflect.Method;
import java.lang.ref.Reference;
import java.util.logging.Level;
import java.lang.reflect.Field;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;

public class Finalizer implements Runnable
{
    private static final Logger logger;
    private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue;
    private static final Field inheritableThreadLocals;
    
    public static void startFinalizer(final Class<?> finalizableReferenceClass, final ReferenceQueue<Object> queue, final PhantomReference<Object> frqReference) {
        if (!finalizableReferenceClass.getName().equals("com.google.common.base.FinalizableReference")) {
            throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
        }
        final Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
        final Thread thread = new Thread(finalizer);
        thread.setName(Finalizer.class.getName());
        thread.setDaemon(true);
        try {
            if (Finalizer.inheritableThreadLocals != null) {
                Finalizer.inheritableThreadLocals.set(thread, null);
            }
        }
        catch (Throwable t) {
            Finalizer.logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", t);
        }
        thread.start();
    }
    
    private Finalizer(final Class<?> finalizableReferenceClass, final ReferenceQueue<Object> queue, final PhantomReference<Object> frqReference) {
        this.queue = queue;
        this.finalizableReferenceClassReference = new WeakReference<Class<?>>(finalizableReferenceClass);
        this.frqReference = frqReference;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                while (this.cleanUp(this.queue.remove())) {}
            }
            catch (InterruptedException e) {
                continue;
            }
            break;
        }
    }
    
    private boolean cleanUp(Reference<?> reference) {
        final Method finalizeReferentMethod = this.getFinalizeReferentMethod();
        if (finalizeReferentMethod == null) {
            return false;
        }
        do {
            reference.clear();
            if (reference == this.frqReference) {
                return false;
            }
            try {
                finalizeReferentMethod.invoke(reference, new Object[0]);
            }
            catch (Throwable t) {
                Finalizer.logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
            }
        } while ((reference = this.queue.poll()) != null);
        return true;
    }
    
    private Method getFinalizeReferentMethod() {
        final Class<?> finalizableReferenceClass = this.finalizableReferenceClassReference.get();
        if (finalizableReferenceClass == null) {
            return null;
        }
        try {
            return finalizableReferenceClass.getMethod("finalizeReferent", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException e) {
            throw new AssertionError((Object)e);
        }
    }
    
    public static Field getInheritableThreadLocalsField() {
        try {
            final Field inheritableThreadLocals = Thread.class.getDeclaredField("inheritableThreadLocals");
            inheritableThreadLocals.setAccessible(true);
            return inheritableThreadLocals;
        }
        catch (Throwable t) {
            Finalizer.logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
            return null;
        }
    }
    
    static {
        logger = Logger.getLogger(Finalizer.class.getName());
        inheritableThreadLocals = getInheritableThreadLocalsField();
    }
}
