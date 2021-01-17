// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import java.io.Serializable;

public abstract class AbstractLayout<T extends Serializable> implements Layout<T>
{
    protected static final Logger LOGGER;
    protected byte[] header;
    protected byte[] footer;
    
    @Override
    public byte[] getHeader() {
        return this.header;
    }
    
    public void setHeader(final byte[] header) {
        this.header = header;
    }
    
    @Override
    public byte[] getFooter() {
        return this.footer;
    }
    
    public void setFooter(final byte[] footer) {
        this.footer = footer;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
