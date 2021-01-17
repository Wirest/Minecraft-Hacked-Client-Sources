// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import java.util.Enumeration;
import java.io.OutputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.apache.commons.compress.archivers.zip.ZipFile;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import java.util.Set;

public class ChangeSetPerformer
{
    private final Set<Change> changes;
    
    public ChangeSetPerformer(final ChangeSet changeSet) {
        this.changes = changeSet.getChanges();
    }
    
    public ChangeSetResults perform(final ArchiveInputStream in, final ArchiveOutputStream out) throws IOException {
        return this.perform(new ArchiveInputStreamIterator(in), out);
    }
    
    public ChangeSetResults perform(final ZipFile in, final ArchiveOutputStream out) throws IOException {
        return this.perform(new ZipFileIterator(in), out);
    }
    
    private ChangeSetResults perform(final ArchiveEntryIterator entryIterator, final ArchiveOutputStream out) throws IOException {
        final ChangeSetResults results = new ChangeSetResults();
        final Set<Change> workingSet = new LinkedHashSet<Change>(this.changes);
        Iterator<Change> it = workingSet.iterator();
        while (it.hasNext()) {
            final Change change = it.next();
            if (change.type() == 2 && change.isReplaceMode()) {
                this.copyStream(change.getInput(), out, change.getEntry());
                it.remove();
                results.addedFromChangeSet(change.getEntry().getName());
            }
        }
        while (entryIterator.hasNext()) {
            final ArchiveEntry entry = entryIterator.next();
            boolean copy = true;
            final Iterator<Change> it2 = workingSet.iterator();
            while (it2.hasNext()) {
                final Change change2 = it2.next();
                final int type = change2.type();
                final String name = entry.getName();
                if (type == 1 && name != null) {
                    if (name.equals(change2.targetFile())) {
                        copy = false;
                        it2.remove();
                        results.deleted(name);
                        break;
                    }
                    continue;
                }
                else {
                    if (type == 4 && name != null && name.startsWith(change2.targetFile() + "/")) {
                        copy = false;
                        results.deleted(name);
                        break;
                    }
                    continue;
                }
            }
            if (copy && !this.isDeletedLater(workingSet, entry) && !results.hasBeenAdded(entry.getName())) {
                this.copyStream(entryIterator.getInputStream(), out, entry);
                results.addedFromStream(entry.getName());
            }
        }
        it = workingSet.iterator();
        while (it.hasNext()) {
            final Change change = it.next();
            if (change.type() == 2 && !change.isReplaceMode() && !results.hasBeenAdded(change.getEntry().getName())) {
                this.copyStream(change.getInput(), out, change.getEntry());
                it.remove();
                results.addedFromChangeSet(change.getEntry().getName());
            }
        }
        out.finish();
        return results;
    }
    
    private boolean isDeletedLater(final Set<Change> workingSet, final ArchiveEntry entry) {
        final String source = entry.getName();
        if (!workingSet.isEmpty()) {
            for (final Change change : workingSet) {
                final int type = change.type();
                final String target = change.targetFile();
                if (type == 1 && source.equals(target)) {
                    return true;
                }
                if (type == 4 && source.startsWith(target + "/")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void copyStream(final InputStream in, final ArchiveOutputStream out, final ArchiveEntry entry) throws IOException {
        out.putArchiveEntry(entry);
        IOUtils.copy(in, out);
        out.closeArchiveEntry();
    }
    
    private static class ArchiveInputStreamIterator implements ArchiveEntryIterator
    {
        private final ArchiveInputStream in;
        private ArchiveEntry next;
        
        ArchiveInputStreamIterator(final ArchiveInputStream in) {
            this.in = in;
        }
        
        public boolean hasNext() throws IOException {
            final ArchiveEntry nextEntry = this.in.getNextEntry();
            this.next = nextEntry;
            return nextEntry != null;
        }
        
        public ArchiveEntry next() {
            return this.next;
        }
        
        public InputStream getInputStream() {
            return this.in;
        }
    }
    
    private static class ZipFileIterator implements ArchiveEntryIterator
    {
        private final ZipFile in;
        private final Enumeration<ZipArchiveEntry> nestedEnum;
        private ZipArchiveEntry current;
        
        ZipFileIterator(final ZipFile in) {
            this.in = in;
            this.nestedEnum = in.getEntriesInPhysicalOrder();
        }
        
        public boolean hasNext() {
            return this.nestedEnum.hasMoreElements();
        }
        
        public ArchiveEntry next() {
            return this.current = this.nestedEnum.nextElement();
        }
        
        public InputStream getInputStream() throws IOException {
            return this.in.getInputStream(this.current);
        }
    }
    
    interface ArchiveEntryIterator
    {
        boolean hasNext() throws IOException;
        
        ArchiveEntry next();
        
        InputStream getInputStream() throws IOException;
    }
}
