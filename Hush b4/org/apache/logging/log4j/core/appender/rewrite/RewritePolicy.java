// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.core.LogEvent;

public interface RewritePolicy
{
    LogEvent rewrite(final LogEvent p0);
}
