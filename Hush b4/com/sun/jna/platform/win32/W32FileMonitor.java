// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import java.io.FileNotFoundException;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;
import java.util.Map;
import com.sun.jna.platform.FileMonitor;

public class W32FileMonitor extends FileMonitor
{
    private static final int BUFFER_SIZE = 4096;
    private Thread watcher;
    private WinNT.HANDLE port;
    private final Map<File, FileInfo> fileMap;
    private final Map<WinNT.HANDLE, FileInfo> handleMap;
    private boolean disposing;
    private static int watcherThreadID;
    
    public W32FileMonitor() {
        this.fileMap = new HashMap<File, FileInfo>();
        this.handleMap = new HashMap<WinNT.HANDLE, FileInfo>();
        this.disposing = false;
    }
    
    private void handleChanges(final FileInfo finfo) throws IOException {
        final Kernel32 klib = Kernel32.INSTANCE;
        WinNT.FILE_NOTIFY_INFORMATION fni = finfo.info;
        fni.read();
        do {
            FileEvent event = null;
            final File file = new File(finfo.file, fni.getFilename());
            switch (fni.Action) {
                case 0: {
                    break;
                }
                case 3: {
                    event = new FileEvent(file, 4);
                    break;
                }
                case 1: {
                    event = new FileEvent(file, 1);
                    break;
                }
                case 2: {
                    event = new FileEvent(file, 2);
                    break;
                }
                case 4: {
                    event = new FileEvent(file, 16);
                    break;
                }
                case 5: {
                    event = new FileEvent(file, 32);
                    break;
                }
                default: {
                    System.err.println("Unrecognized file action '" + fni.Action + "'");
                    break;
                }
            }
            if (event != null) {
                this.notify(event);
            }
            fni = fni.next();
        } while (fni != null);
        if (!finfo.file.exists()) {
            this.unwatch(finfo.file);
            return;
        }
        if (!klib.ReadDirectoryChangesW(finfo.handle, finfo.info, finfo.info.size(), finfo.recursive, finfo.notifyMask, finfo.infoLength, finfo.overlapped, null) && !this.disposing) {
            final int err = klib.GetLastError();
            throw new IOException("ReadDirectoryChangesW failed on " + finfo.file + ": '" + Kernel32Util.formatMessageFromLastErrorCode(err) + "' (" + err + ")");
        }
    }
    
    private FileInfo waitForChange() {
        final Kernel32 klib = Kernel32.INSTANCE;
        final IntByReference rcount = new IntByReference();
        final BaseTSD.ULONG_PTRByReference rkey = new BaseTSD.ULONG_PTRByReference();
        final PointerByReference roverlap = new PointerByReference();
        klib.GetQueuedCompletionStatus(this.port, rcount, rkey, roverlap, -1);
        synchronized (this) {
            return this.handleMap.get(rkey.getValue());
        }
    }
    
    private int convertMask(final int mask) {
        int result = 0;
        if ((mask & 0x1) != 0x0) {
            result |= 0x40;
        }
        if ((mask & 0x2) != 0x0) {
            result |= 0x3;
        }
        if ((mask & 0x4) != 0x0) {
            result |= 0x10;
        }
        if ((mask & 0x30) != 0x0) {
            result |= 0x3;
        }
        if ((mask & 0x40) != 0x0) {
            result |= 0x8;
        }
        if ((mask & 0x8) != 0x0) {
            result |= 0x20;
        }
        if ((mask & 0x80) != 0x0) {
            result |= 0x4;
        }
        if ((mask & 0x100) != 0x0) {
            result |= 0x100;
        }
        return result;
    }
    
    @Override
    protected synchronized void watch(final File file, final int eventMask, boolean recursive) throws IOException {
        File dir = file;
        if (!dir.isDirectory()) {
            recursive = false;
            dir = file.getParentFile();
        }
        while (dir != null && !dir.exists()) {
            recursive = true;
            dir = dir.getParentFile();
        }
        if (dir == null) {
            throw new FileNotFoundException("No ancestor found for " + file);
        }
        final Kernel32 klib = Kernel32.INSTANCE;
        final int mask = 7;
        final int flags = 1107296256;
        final WinNT.HANDLE handle = klib.CreateFile(file.getAbsolutePath(), 1, mask, null, 3, flags, null);
        if (WinBase.INVALID_HANDLE_VALUE.equals(handle)) {
            throw new IOException("Unable to open " + file + " (" + klib.GetLastError() + ")");
        }
        final int notifyMask = this.convertMask(eventMask);
        final FileInfo finfo = new FileInfo(file, handle, notifyMask, recursive);
        this.fileMap.put(file, finfo);
        this.handleMap.put(handle, finfo);
        this.port = klib.CreateIoCompletionPort(handle, this.port, handle.getPointer(), 0);
        if (WinBase.INVALID_HANDLE_VALUE.equals(this.port)) {
            throw new IOException("Unable to create/use I/O Completion port for " + file + " (" + klib.GetLastError() + ")");
        }
        if (!klib.ReadDirectoryChangesW(handle, finfo.info, finfo.info.size(), recursive, notifyMask, finfo.infoLength, finfo.overlapped, null)) {
            final int err = klib.GetLastError();
            throw new IOException("ReadDirectoryChangesW failed on " + finfo.file + ", handle " + handle + ": '" + Kernel32Util.formatMessageFromLastErrorCode(err) + "' (" + err + ")");
        }
        if (this.watcher == null) {
            (this.watcher = new Thread("W32 File Monitor-" + W32FileMonitor.watcherThreadID++) {
                @Override
                public void run() {
                    while (true) {
                        final FileInfo finfo = W32FileMonitor.this.waitForChange();
                        if (finfo == null) {
                            synchronized (W32FileMonitor.this) {
                                if (W32FileMonitor.this.fileMap.isEmpty()) {
                                    W32FileMonitor.this.watcher = null;
                                    break;
                                }
                                continue;
                            }
                        }
                        else {
                            try {
                                W32FileMonitor.this.handleChanges(finfo);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).setDaemon(true);
            this.watcher.start();
        }
    }
    
    @Override
    protected synchronized void unwatch(final File file) {
        final FileInfo finfo = this.fileMap.remove(file);
        if (finfo != null) {
            this.handleMap.remove(finfo.handle);
            final Kernel32 klib = Kernel32.INSTANCE;
            klib.CloseHandle(finfo.handle);
        }
    }
    
    @Override
    public synchronized void dispose() {
        this.disposing = true;
        int i = 0;
        final Object[] keys = this.fileMap.keySet().toArray();
        while (!this.fileMap.isEmpty()) {
            this.unwatch((File)keys[i++]);
        }
        final Kernel32 klib = Kernel32.INSTANCE;
        klib.PostQueuedCompletionStatus(this.port, 0, null, null);
        klib.CloseHandle(this.port);
        this.port = null;
        this.watcher = null;
    }
    
    private class FileInfo
    {
        public final File file;
        public final WinNT.HANDLE handle;
        public final int notifyMask;
        public final boolean recursive;
        public final WinNT.FILE_NOTIFY_INFORMATION info;
        public final IntByReference infoLength;
        public final WinBase.OVERLAPPED overlapped;
        
        public FileInfo(final File f, final WinNT.HANDLE h, final int mask, final boolean recurse) {
            this.info = new WinNT.FILE_NOTIFY_INFORMATION(4096);
            this.infoLength = new IntByReference();
            this.overlapped = new WinBase.OVERLAPPED();
            this.file = f;
            this.handle = h;
            this.notifyMask = mask;
            this.recursive = recurse;
        }
    }
}
