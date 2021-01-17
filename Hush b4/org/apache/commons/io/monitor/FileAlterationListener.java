// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.monitor;

import java.io.File;

public interface FileAlterationListener
{
    void onStart(final FileAlterationObserver p0);
    
    void onDirectoryCreate(final File p0);
    
    void onDirectoryChange(final File p0);
    
    void onDirectoryDelete(final File p0);
    
    void onFileCreate(final File p0);
    
    void onFileChange(final File p0);
    
    void onFileDelete(final File p0);
    
    void onStop(final FileAlterationObserver p0);
}
