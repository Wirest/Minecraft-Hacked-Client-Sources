// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class LinuxDeviceThread extends Thread
{
    private final List tasks;
    
    public LinuxDeviceThread() {
        this.tasks = new ArrayList();
        this.setDaemon(true);
        this.start();
    }
    
    public final synchronized void run() {
        while (true) {
            if (!this.tasks.isEmpty()) {
                final LinuxDeviceTask task = this.tasks.remove(0);
                task.doExecute();
                synchronized (task) {
                    task.notify();
                }
            }
            else {
                try {
                    this.wait();
                }
                catch (InterruptedException e) {}
            }
        }
    }
    
    public final Object execute(final LinuxDeviceTask task) throws IOException {
        synchronized (this) {
            this.tasks.add(task);
            this.notify();
        }
        synchronized (task) {
            while (task.getState() == 1) {
                try {
                    task.wait();
                }
                catch (InterruptedException e) {}
            }
        }
        switch (task.getState()) {
            case 2: {
                return task.getResult();
            }
            case 3: {
                throw task.getException();
            }
            default: {
                throw new RuntimeException("Invalid task state: " + task.getState());
            }
        }
    }
}
