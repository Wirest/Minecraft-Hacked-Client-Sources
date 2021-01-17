// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.net.URL;

public interface ExtensionInstallerService
{
    URL getExtensionLocation();
    
    String getExtensionVersion();
    
    String getInstallPath();
    
    String getInstalledJRE(final URL p0, final String p1);
    
    void hideProgressBar();
    
    void hideStatusWindow();
    
    void installFailed();
    
    void installSucceeded(final boolean p0);
    
    void setHeading(final String p0);
    
    void setJREInfo(final String p0, final String p1);
    
    void setNativeLibraryInfo(final String p0);
    
    void setStatus(final String p0);
    
    void updateProgress(final int p0);
}
