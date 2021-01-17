// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.lang.ref.PhantomReference;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Collection;
import java.lang.ref.ReferenceQueue;

public class FileCleaningTracker
{
    ReferenceQueue<Object> q;
    final Collection<Tracker> trackers;
    final List<String> deleteFailures;
    volatile boolean exitWhenFinished;
    Thread reaper;
    
    public FileCleaningTracker() {
        this.q = new ReferenceQueue<Object>();
        this.trackers = (Collection<Tracker>)Collections.synchronizedSet(new HashSet<Object>());
        this.deleteFailures = Collections.synchronizedList(new ArrayList<String>());
        this.exitWhenFinished = false;
    }
    
    public void track(final File file, final Object marker) {
        this.track(file, marker, null);
    }
    
    public void track(final File file, final Object marker, final FileDeleteStrategy deleteStrategy) {
        if (file == null) {
            throw new NullPointerException("The file must not be null");
        }
        this.addTracker(file.getPath(), marker, deleteStrategy);
    }
    
    public void track(final String path, final Object marker) {
        this.track(path, marker, null);
    }
    
    public void track(final String path, final Object marker, final FileDeleteStrategy deleteStrategy) {
        if (path == null) {
            throw new NullPointerException("The path must not be null");
        }
        this.addTracker(path, marker, deleteStrategy);
    }
    
    private synchronized void addTracker(final String path, final Object marker, final FileDeleteStrategy deleteStrategy) {
        if (this.exitWhenFinished) {
            throw new IllegalStateException("No new trackers can be added once exitWhenFinished() is called");
        }
        if (this.reaper == null) {
            (this.reaper = new Reaper()).start();
        }
        this.trackers.add(new Tracker(path, deleteStrategy, marker, this.q));
    }
    
    public int getTrackCount() {
        return this.trackers.size();
    }
    
    public List<String> getDeleteFailures() {
        return this.deleteFailures;
    }
    
    public synchronized void exitWhenFinished() {
        this.exitWhenFinished = true;
        if (this.reaper != null) {
            synchronized (this.reaper) {
                this.reaper.interrupt();
            }
        }
    }
    
    private final class Reaper extends Thread
    {
        Reaper() {
            super("File Reaper");
            this.setPriority(10);
            this.setDaemon(true);
        }
        
        @Override
        public void run() {
            while (true) {
                if (FileCleaningTracker.this.exitWhenFinished) {
                    if (FileCleaningTracker.this.trackers.size() <= 0) {
                        break;
                    }
                }
                try {
                    final Tracker tracker = (Tracker)FileCleaningTracker.this.q.remove();
                    FileCleaningTracker.this.trackers.remove(tracker);
                    if (!tracker.delete()) {
                        FileCleaningTracker.this.deleteFailures.add(tracker.getPath());
                    }
                    tracker.clear();
                }
                catch (InterruptedException e) {}
            }
        }
    }
    
    private static final class Tracker extends PhantomReference<Object>
    {
        private final String path;
        private final FileDeleteStrategy deleteStrategy;
        
        Tracker(final String path, final FileDeleteStrategy deleteStrategy, final Object marker, final ReferenceQueue<? super Object> queue) {
            super(marker, queue);
            this.path = path;
            this.deleteStrategy = ((deleteStrategy == null) ? FileDeleteStrategy.NORMAL : deleteStrategy);
        }
        
        public String getPath() {
            return this.path;
        }
        
        public boolean delete() {
            return this.deleteStrategy.deleteQuietly(new File(this.path));
        }
    }
}
