// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.net.URI;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;
import java.util.AbstractList;

@NotThreadSafe
public class RedirectLocations extends AbstractList<Object>
{
    private final Set<URI> unique;
    private final List<URI> all;
    
    public RedirectLocations() {
        this.unique = new HashSet<URI>();
        this.all = new ArrayList<URI>();
    }
    
    public boolean contains(final URI uri) {
        return this.unique.contains(uri);
    }
    
    public void add(final URI uri) {
        this.unique.add(uri);
        this.all.add(uri);
    }
    
    public boolean remove(final URI uri) {
        final boolean removed = this.unique.remove(uri);
        if (removed) {
            final Iterator<URI> it = this.all.iterator();
            while (it.hasNext()) {
                final URI current = it.next();
                if (current.equals(uri)) {
                    it.remove();
                }
            }
        }
        return removed;
    }
    
    public List<URI> getAll() {
        return new ArrayList<URI>(this.all);
    }
    
    @Override
    public URI get(final int index) {
        return this.all.get(index);
    }
    
    @Override
    public int size() {
        return this.all.size();
    }
    
    @Override
    public Object set(final int index, final Object element) {
        final URI removed = this.all.set(index, (URI)element);
        this.unique.remove(removed);
        this.unique.add((URI)element);
        if (this.all.size() != this.unique.size()) {
            this.unique.addAll(this.all);
        }
        return removed;
    }
    
    @Override
    public void add(final int index, final Object element) {
        this.all.add(index, (URI)element);
        this.unique.add((URI)element);
    }
    
    @Override
    public URI remove(final int index) {
        final URI removed = this.all.remove(index);
        this.unique.remove(removed);
        if (this.all.size() != this.unique.size()) {
            this.unique.addAll(this.all);
        }
        return removed;
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.unique.contains(o);
    }
}
