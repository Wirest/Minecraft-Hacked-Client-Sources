// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.changes;

import java.util.ArrayList;
import java.util.List;

public class ChangeSetResults
{
    private final List<String> addedFromChangeSet;
    private final List<String> addedFromStream;
    private final List<String> deleted;
    
    public ChangeSetResults() {
        this.addedFromChangeSet = new ArrayList<String>();
        this.addedFromStream = new ArrayList<String>();
        this.deleted = new ArrayList<String>();
    }
    
    void deleted(final String fileName) {
        this.deleted.add(fileName);
    }
    
    void addedFromStream(final String fileName) {
        this.addedFromStream.add(fileName);
    }
    
    void addedFromChangeSet(final String fileName) {
        this.addedFromChangeSet.add(fileName);
    }
    
    public List<String> getAddedFromChangeSet() {
        return this.addedFromChangeSet;
    }
    
    public List<String> getAddedFromStream() {
        return this.addedFromStream;
    }
    
    public List<String> getDeleted() {
        return this.deleted;
    }
    
    boolean hasBeenAdded(final String filename) {
        return this.addedFromChangeSet.contains(filename) || this.addedFromStream.contains(filename);
    }
}
