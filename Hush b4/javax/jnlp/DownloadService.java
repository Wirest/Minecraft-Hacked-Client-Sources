// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.io.IOException;
import java.net.URL;

public interface DownloadService
{
    DownloadServiceListener getDefaultProgressWindow();
    
    boolean isExtensionPartCached(final URL p0, final String p1, final String p2);
    
    boolean isExtensionPartCached(final URL p0, final String p1, final String[] p2);
    
    boolean isPartCached(final String p0);
    
    boolean isPartCached(final String[] p0);
    
    boolean isResourceCached(final URL p0, final String p1);
    
    void loadExtensionPart(final URL p0, final String p1, final String p2, final DownloadServiceListener p3) throws IOException;
    
    void loadExtensionPart(final URL p0, final String p1, final String[] p2, final DownloadServiceListener p3) throws IOException;
    
    void loadPart(final String p0, final DownloadServiceListener p1) throws IOException;
    
    void loadPart(final String[] p0, final DownloadServiceListener p1) throws IOException;
    
    void loadResource(final URL p0, final String p1, final DownloadServiceListener p2) throws IOException;
    
    void removeExtensionPart(final URL p0, final String p1, final String p2) throws IOException;
    
    void removeExtensionPart(final URL p0, final String p1, final String[] p2) throws IOException;
    
    void removePart(final String p0) throws IOException;
    
    void removePart(final String[] p0) throws IOException;
    
    void removeResource(final URL p0, final String p1) throws IOException;
}
