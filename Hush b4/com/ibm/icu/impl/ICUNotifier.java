// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public abstract class ICUNotifier
{
    private final Object notifyLock;
    private NotifyThread notifyThread;
    private List<EventListener> listeners;
    
    public ICUNotifier() {
        this.notifyLock = new Object();
    }
    
    public void addListener(final EventListener l) {
        if (l == null) {
            throw new NullPointerException();
        }
        if (this.acceptsListener(l)) {
            synchronized (this.notifyLock) {
                if (this.listeners == null) {
                    this.listeners = new ArrayList<EventListener>();
                }
                else {
                    for (final EventListener ll : this.listeners) {
                        if (ll == l) {
                            return;
                        }
                    }
                }
                this.listeners.add(l);
            }
            return;
        }
        throw new IllegalStateException("Listener invalid for this notifier.");
    }
    
    public void removeListener(final EventListener l) {
        if (l == null) {
            throw new NullPointerException();
        }
        synchronized (this.notifyLock) {
            if (this.listeners != null) {
                final Iterator<EventListener> iter = this.listeners.iterator();
                while (iter.hasNext()) {
                    if (iter.next() == l) {
                        iter.remove();
                        if (this.listeners.size() == 0) {
                            this.listeners = null;
                        }
                    }
                }
            }
        }
    }
    
    public void notifyChanged() {
        if (this.listeners != null) {
            synchronized (this.notifyLock) {
                if (this.listeners != null) {
                    if (this.notifyThread == null) {
                        (this.notifyThread = new NotifyThread(this)).setDaemon(true);
                        this.notifyThread.start();
                    }
                    this.notifyThread.queue(this.listeners.toArray(new EventListener[this.listeners.size()]));
                }
            }
        }
    }
    
    protected abstract boolean acceptsListener(final EventListener p0);
    
    protected abstract void notifyListener(final EventListener p0);
    
    private static class NotifyThread extends Thread
    {
        private final ICUNotifier notifier;
        private final List<EventListener[]> queue;
        
        NotifyThread(final ICUNotifier notifier) {
            this.queue = new ArrayList<EventListener[]>();
            this.notifier = notifier;
        }
        
        public void queue(final EventListener[] list) {
            synchronized (this) {
                this.queue.add(list);
                this.notify();
            }
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        final EventListener[] list;
                        synchronized (this) {
                            while (this.queue.isEmpty()) {
                                this.wait();
                            }
                            list = this.queue.remove(0);
                        }
                        for (int i = 0; i < list.length; ++i) {
                            this.notifier.notifyListener(list[i]);
                        }
                    }
                }
                catch (InterruptedException e) {
                    continue;
                }
                break;
            }
        }
    }
}
