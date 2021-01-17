// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.concurrent.Executor;

public final class ImmediateExecutor implements Executor
{
    public static final ImmediateExecutor INSTANCE;
    
    private ImmediateExecutor() {
    }
    
    @Override
    public void execute(final Runnable command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        command.run();
    }
    
    static {
        INSTANCE = new ImmediateExecutor();
    }
}
