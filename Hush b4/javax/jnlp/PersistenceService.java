// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public interface PersistenceService
{
    public static final int CACHED = 0;
    public static final int TEMPORARY = 1;
    public static final int DIRTY = 2;
    
    long create(final URL p0, final long p1) throws MalformedURLException, IOException;
    
    void delete(final URL p0) throws MalformedURLException, IOException;
    
    FileContents get(final URL p0) throws MalformedURLException, IOException, FileNotFoundException;
    
    String[] getNames(final URL p0) throws MalformedURLException, IOException;
    
    int getTag(final URL p0) throws MalformedURLException, IOException;
    
    void setTag(final URL p0, final int p1) throws MalformedURLException, IOException;
}
