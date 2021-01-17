// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public interface StrLookup
{
    String lookup(final String p0);
    
    String lookup(final LogEvent p0, final String p1);
}
