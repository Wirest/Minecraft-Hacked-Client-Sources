// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

abstract class LinuxDeviceTask
{
    public static final int INPROGRESS = 1;
    public static final int COMPLETED = 2;
    public static final int FAILED = 3;
    private Object result;
    private IOException exception;
    private int state;
    
    LinuxDeviceTask() {
        this.state = 1;
    }
    
    public final void doExecute() {
        try {
            this.result = this.execute();
            this.state = 2;
        }
        catch (IOException e) {
            this.exception = e;
            this.state = 3;
        }
    }
    
    public final IOException getException() {
        return this.exception;
    }
    
    public final Object getResult() {
        return this.result;
    }
    
    public final int getState() {
        return this.state;
    }
    
    protected abstract Object execute() throws IOException;
}
