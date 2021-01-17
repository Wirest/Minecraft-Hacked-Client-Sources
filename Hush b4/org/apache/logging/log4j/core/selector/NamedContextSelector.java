// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.selector;

import org.apache.logging.log4j.core.LoggerContext;
import java.net.URI;

public interface NamedContextSelector extends ContextSelector
{
    LoggerContext locateContext(final String p0, final Object p1, final URI p2);
    
    LoggerContext removeContext(final String p0);
}
