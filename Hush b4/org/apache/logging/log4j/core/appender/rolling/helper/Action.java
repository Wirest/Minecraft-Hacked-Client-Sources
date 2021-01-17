// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.IOException;

public interface Action extends Runnable
{
    boolean execute() throws IOException;
    
    void close();
    
    boolean isComplete();
}
