// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.changes;

import java.util.Collection;
import java.util.Iterator;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ChangeSet
{
    private final Set<Change> changes;
    
    public ChangeSet() {
        this.changes = new LinkedHashSet<Change>();
    }
    
    public void delete(final String filename) {
        this.addDeletion(new Change(filename, 1));
    }
    
    public void deleteDir(final String dirName) {
        this.addDeletion(new Change(dirName, 4));
    }
    
    public void add(final ArchiveEntry pEntry, final InputStream pInput) {
        this.add(pEntry, pInput, true);
    }
    
    public void add(final ArchiveEntry pEntry, final InputStream pInput, final boolean replace) {
        this.addAddition(new Change(pEntry, pInput, replace));
    }
    
    private void addAddition(final Change pChange) {
        if (2 != pChange.type() || pChange.getInput() == null) {
            return;
        }
        if (!this.changes.isEmpty()) {
            final Iterator<Change> it = this.changes.iterator();
            while (it.hasNext()) {
                final Change change = it.next();
                if (change.type() == 2 && change.getEntry() != null) {
                    final ArchiveEntry entry = change.getEntry();
                    if (!entry.equals(pChange.getEntry())) {
                        continue;
                    }
                    if (pChange.isReplaceMode()) {
                        it.remove();
                        this.changes.add(pChange);
                    }
                    return;
                }
            }
        }
        this.changes.add(pChange);
    }
    
    private void addDeletion(final Change pChange) {
        if ((1 != pChange.type() && 4 != pChange.type()) || pChange.targetFile() == null) {
            return;
        }
        final String source = pChange.targetFile();
        if (source != null && !this.changes.isEmpty()) {
            final Iterator<Change> it = this.changes.iterator();
            while (it.hasNext()) {
                final Change change = it.next();
                if (change.type() == 2 && change.getEntry() != null) {
                    final String target = change.getEntry().getName();
                    if (target == null) {
                        continue;
                    }
                    if (1 == pChange.type() && source.equals(target)) {
                        it.remove();
                    }
                    else {
                        if (4 != pChange.type() || !target.matches(source + "/.*")) {
                            continue;
                        }
                        it.remove();
                    }
                }
            }
        }
        this.changes.add(pChange);
    }
    
    Set<Change> getChanges() {
        return new LinkedHashSet<Change>(this.changes);
    }
}
