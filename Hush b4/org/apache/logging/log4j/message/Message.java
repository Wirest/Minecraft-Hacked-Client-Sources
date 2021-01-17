// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.io.Serializable;

public interface Message extends Serializable
{
    String getFormattedMessage();
    
    String getFormat();
    
    Object[] getParameters();
    
    Throwable getThrowable();
}
