// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.IOException;
import java.io.File;

public class FileDeleteStrategy
{
    public static final FileDeleteStrategy NORMAL;
    public static final FileDeleteStrategy FORCE;
    private final String name;
    
    protected FileDeleteStrategy(final String name) {
        this.name = name;
    }
    
    public boolean deleteQuietly(final File fileToDelete) {
        if (fileToDelete == null || !fileToDelete.exists()) {
            return true;
        }
        try {
            return this.doDelete(fileToDelete);
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public void delete(final File fileToDelete) throws IOException {
        if (fileToDelete.exists() && !this.doDelete(fileToDelete)) {
            throw new IOException("Deletion failed: " + fileToDelete);
        }
    }
    
    protected boolean doDelete(final File fileToDelete) throws IOException {
        return fileToDelete.delete();
    }
    
    @Override
    public String toString() {
        return "FileDeleteStrategy[" + this.name + "]";
    }
    
    static {
        NORMAL = new FileDeleteStrategy("Normal");
        FORCE = new ForceFileDeleteStrategy();
    }
    
    static class ForceFileDeleteStrategy extends FileDeleteStrategy
    {
        ForceFileDeleteStrategy() {
            super("Force");
        }
        
        @Override
        protected boolean doDelete(final File fileToDelete) throws IOException {
            FileUtils.forceDelete(fileToDelete);
            return true;
        }
    }
}
