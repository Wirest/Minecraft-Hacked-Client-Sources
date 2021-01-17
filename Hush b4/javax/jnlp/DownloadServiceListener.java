// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.net.URL;

public interface DownloadServiceListener
{
    void downloadFailed(final URL p0, final String p1);
    
    void progress(final URL p0, final String p1, final long p2, final long p3, final int p4);
    
    void upgradingArchive(final URL p0, final String p1, final int p2, final int p3);
    
    void validating(final URL p0, final String p1, final long p2, final long p3, final int p4);
}
