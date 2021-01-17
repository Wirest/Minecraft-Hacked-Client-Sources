// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

public enum Protocol
{
    TCP, 
    UDP;
    
    public boolean isEqual(final String name) {
        return this.name().equalsIgnoreCase(name);
    }
}
