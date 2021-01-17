// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform;

import com.sun.jna.platform.win32.W32FileMonitor;
import java.util.EventObject;
import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.util.Map;

public abstract class FileMonitor
{
    public static final int FILE_CREATED = 1;
    public static final int FILE_DELETED = 2;
    public static final int FILE_MODIFIED = 4;
    public static final int FILE_ACCESSED = 8;
    public static final int FILE_NAME_CHANGED_OLD = 16;
    public static final int FILE_NAME_CHANGED_NEW = 32;
    public static final int FILE_RENAMED = 48;
    public static final int FILE_SIZE_CHANGED = 64;
    public static final int FILE_ATTRIBUTES_CHANGED = 128;
    public static final int FILE_SECURITY_CHANGED = 256;
    public static final int FILE_ANY = 511;
    private final Map<File, Integer> watched;
    private List<FileListener> listeners;
    
    public FileMonitor() {
        this.watched = new HashMap<File, Integer>();
        this.listeners = new ArrayList<FileListener>();
    }
    
    protected abstract void watch(final File p0, final int p1, final boolean p2) throws IOException;
    
    protected abstract void unwatch(final File p0);
    
    public abstract void dispose();
    
    public void addWatch(final File dir) throws IOException {
        this.addWatch(dir, 511);
    }
    
    public void addWatch(final File dir, final int mask) throws IOException {
        this.addWatch(dir, mask, dir.isDirectory());
    }
    
    public void addWatch(final File dir, final int mask, final boolean recursive) throws IOException {
        this.watched.put(dir, new Integer(mask));
        this.watch(dir, mask, recursive);
    }
    
    public void removeWatch(final File file) {
        if (this.watched.remove(file) != null) {
            this.unwatch(file);
        }
    }
    
    protected void notify(final FileEvent e) {
        for (final FileListener listener : this.listeners) {
            listener.fileChanged(e);
        }
    }
    
    public synchronized void addFileListener(final FileListener listener) {
        final List<FileListener> list = new ArrayList<FileListener>(this.listeners);
        list.add(listener);
        this.listeners = list;
    }
    
    public synchronized void removeFileListener(final FileListener x) {
        final List<FileListener> list = new ArrayList<FileListener>(this.listeners);
        list.remove(x);
        this.listeners = list;
    }
    
    @Override
    protected void finalize() {
        for (final File watchedFile : this.watched.keySet()) {
            this.removeWatch(watchedFile);
        }
        this.dispose();
    }
    
    public static FileMonitor getInstance() {
        return Holder.INSTANCE;
    }
    
    public class FileEvent extends EventObject
    {
        private final File file;
        private final int type;
        
        public FileEvent(final File file, final int type) {
            super(FileMonitor.this);
            this.file = file;
            this.type = type;
        }
        
        public File getFile() {
            return this.file;
        }
        
        public int getType() {
            return this.type;
        }
        
        @Override
        public String toString() {
            return "FileEvent: " + this.file + ":" + this.type;
        }
    }
    
    private static class Holder
    {
        public static final FileMonitor INSTANCE;
        
        static {
            final String os = System.getProperty("os.name");
            if (os.startsWith("Windows")) {
                INSTANCE = new W32FileMonitor();
                return;
            }
            throw new Error("FileMonitor not implemented for " + os);
        }
    }
    
    public interface FileListener
    {
        void fileChanged(final FileEvent p0);
    }
}
