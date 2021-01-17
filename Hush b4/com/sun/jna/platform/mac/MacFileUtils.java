// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

import com.sun.jna.Native;
import com.sun.jna.Library;
import java.util.List;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import com.sun.jna.platform.FileUtils;

public class MacFileUtils extends FileUtils
{
    @Override
    public boolean hasTrash() {
        return true;
    }
    
    @Override
    public void moveToTrash(final File[] files) throws IOException {
        final File home = new File(System.getProperty("user.home"));
        final File trash = new File(home, ".Trash");
        if (!trash.exists()) {
            throw new IOException("The Trash was not found in its expected location (" + trash + ")");
        }
        final List<File> failed = new ArrayList<File>();
        for (int i = 0; i < files.length; ++i) {
            final File src = files[i];
            if (FileManager.INSTANCE.FSPathMoveObjectToTrashSync(src.getAbsolutePath(), null, 0) != 0) {
                failed.add(src);
            }
        }
        if (failed.size() > 0) {
            throw new IOException("The following files could not be trashed: " + failed);
        }
    }
    
    public interface FileManager extends Library
    {
        public static final int kFSFileOperationDefaultOptions = 0;
        public static final int kFSFileOperationsOverwrite = 1;
        public static final int kFSFileOperationsSkipSourcePermissionErrors = 2;
        public static final int kFSFileOperationsDoNotMoveAcrossVolumes = 4;
        public static final int kFSFileOperationsSkipPreflight = 8;
        public static final FileManager INSTANCE = (FileManager)Native.loadLibrary("CoreServices", FileManager.class);
        
        int FSPathMoveObjectToTrashSync(final String p0, final PointerByReference p1, final int p2);
    }
}
