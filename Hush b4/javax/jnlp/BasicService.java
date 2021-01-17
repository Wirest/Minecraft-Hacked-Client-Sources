// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.net.URL;

public interface BasicService
{
    URL getCodeBase();
    
    boolean isOffline();
    
    boolean isWebBrowserSupported();
    
    boolean showDocument(final URL p0);
}
