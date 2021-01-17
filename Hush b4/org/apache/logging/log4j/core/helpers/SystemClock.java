// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

public class SystemClock implements Clock
{
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
