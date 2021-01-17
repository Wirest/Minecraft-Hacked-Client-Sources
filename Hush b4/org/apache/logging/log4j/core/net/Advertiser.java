// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import java.util.Map;

public interface Advertiser
{
    Object advertise(final Map<String, String> p0);
    
    void unadvertise(final Object p0);
}
