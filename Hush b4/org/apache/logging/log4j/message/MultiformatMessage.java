// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

public interface MultiformatMessage extends Message
{
    String getFormattedMessage(final String[] p0);
    
    String[] getFormats();
}
