// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform;

import java.util.List;
import java.util.ArrayList;
import com.sun.jna.platform.mac.MacFileUtils;
import com.sun.jna.platform.win32.W32FileUtils;
import java.io.IOException;
import java.io.File;

public abstract class FileUtils
{
    public boolean hasTrash() {
        return false;
    }
    
    public abstract void moveToTrash(final File[] p0) throws IOException;
    
    public static FileUtils getInstance() {
        return Holder.INSTANCE;
    }
    
    private static class Holder
    {
        public static final FileUtils INSTANCE;
        
        static {
            final String os = System.getProperty("os.name");
            if (os.startsWith("Windows")) {
                INSTANCE = new W32FileUtils();
            }
            else if (os.startsWith("Mac")) {
                INSTANCE = new MacFileUtils();
            }
            else {
                INSTANCE = new DefaultFileUtils();
            }
        }
    }
    
    private static class DefaultFileUtils extends FileUtils
    {
        private File getTrashDirectory() {
            final File home = new File(System.getProperty("user.home"));
            File trash = new File(home, ".Trash");
            if (!trash.exists()) {
                trash = new File(home, "Trash");
                if (!trash.exists()) {
                    final File desktop = new File(home, "Desktop");
                    if (desktop.exists()) {
                        trash = new File(desktop, ".Trash");
                        if (!trash.exists()) {
                            trash = new File(desktop, "Trash");
                            if (!trash.exists()) {
                                trash = new File(System.getProperty("fileutils.trash", "Trash"));
                            }
                        }
                    }
                }
            }
            return trash;
        }
        
        @Override
        public boolean hasTrash() {
            return this.getTrashDirectory().exists();
        }
        
        @Override
        public void moveToTrash(final File[] files) throws IOException {
            final File trash = this.getTrashDirectory();
            if (!trash.exists()) {
                throw new IOException("No trash location found (define fileutils.trash to be the path to the trash)");
            }
            final List<File> failed = new ArrayList<File>();
            for (int i = 0; i < files.length; ++i) {
                final File src = files[i];
                final File target = new File(trash, src.getName());
                if (!src.renameTo(target)) {
                    failed.add(src);
                }
            }
            if (failed.size() > 0) {
                throw new IOException("The following files could not be trashed: " + failed);
            }
        }
    }
}
