// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.File;

@Deprecated
public class FileCleaner
{
    static final FileCleaningTracker theInstance;
    
    @Deprecated
    public static void track(final File file, final Object marker) {
        FileCleaner.theInstance.track(file, marker);
    }
    
    @Deprecated
    public static void track(final File file, final Object marker, final FileDeleteStrategy deleteStrategy) {
        FileCleaner.theInstance.track(file, marker, deleteStrategy);
    }
    
    @Deprecated
    public static void track(final String path, final Object marker) {
        FileCleaner.theInstance.track(path, marker);
    }
    
    @Deprecated
    public static void track(final String path, final Object marker, final FileDeleteStrategy deleteStrategy) {
        FileCleaner.theInstance.track(path, marker, deleteStrategy);
    }
    
    @Deprecated
    public static int getTrackCount() {
        return FileCleaner.theInstance.getTrackCount();
    }
    
    @Deprecated
    public static synchronized void exitWhenFinished() {
        FileCleaner.theInstance.exitWhenFinished();
    }
    
    public static FileCleaningTracker getInstance() {
        return FileCleaner.theInstance;
    }
    
    static {
        theInstance = new FileCleaningTracker();
    }
}
