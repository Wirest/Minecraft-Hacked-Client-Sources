// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.ThreadContext;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.List;

public class DefaultThreadContextStack implements ThreadContextStack
{
    private static final long serialVersionUID = 5050501L;
    private static ThreadLocal<List<String>> stack;
    private final boolean useStack;
    
    public DefaultThreadContextStack(final boolean useStack) {
        this.useStack = useStack;
    }
    
    @Override
    public String pop() {
        if (!this.useStack) {
            return "";
        }
        final List<String> list = DefaultThreadContextStack.stack.get();
        if (list == null || list.size() == 0) {
            throw new NoSuchElementException("The ThreadContext stack is empty");
        }
        final List<String> copy = new ArrayList<String>(list);
        final int last = copy.size() - 1;
        final String result = copy.remove(last);
        DefaultThreadContextStack.stack.set(Collections.unmodifiableList((List<? extends String>)copy));
        return result;
    }
    
    @Override
    public String peek() {
        final List<String> list = DefaultThreadContextStack.stack.get();
        if (list == null || list.size() == 0) {
            return null;
        }
        final int last = list.size() - 1;
        return list.get(last);
    }
    
    @Override
    public void push(final String message) {
        if (!this.useStack) {
            return;
        }
        this.add(message);
    }
    
    @Override
    public int getDepth() {
        final List<String> list = DefaultThreadContextStack.stack.get();
        return (list == null) ? 0 : list.size();
    }
    
    @Override
    public List<String> asList() {
        final List<String> list = DefaultThreadContextStack.stack.get();
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }
    
    @Override
    public void trim(final int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        final List<String> list = DefaultThreadContextStack.stack.get();
        if (list == null) {
            return;
        }
        final List<String> copy = new ArrayList<String>();
        for (int count = Math.min(depth, list.size()), i = 0; i < count; ++i) {
            copy.add(list.get(i));
        }
        DefaultThreadContextStack.stack.set(copy);
    }
    
    @Override
    public ThreadContextStack copy() {
        List<String> result = null;
        if (!this.useStack || (result = DefaultThreadContextStack.stack.get()) == null) {
            return new MutableThreadContextStack(new ArrayList<String>());
        }
        return new MutableThreadContextStack(result);
    }
    
    @Override
    public void clear() {
        DefaultThreadContextStack.stack.remove();
    }
    
    @Override
    public int size() {
        final List<String> result = DefaultThreadContextStack.stack.get();
        return (result == null) ? 0 : result.size();
    }
    
    @Override
    public boolean isEmpty() {
        final List<String> result = DefaultThreadContextStack.stack.get();
        return result == null || result.isEmpty();
    }
    
    @Override
    public boolean contains(final Object o) {
        final List<String> result = DefaultThreadContextStack.stack.get();
        return result != null && result.contains(o);
    }
    
    @Override
    public Iterator<String> iterator() {
        final List<String> immutable = DefaultThreadContextStack.stack.get();
        if (immutable == null) {
            final List<String> empty = Collections.emptyList();
            return empty.iterator();
        }
        return immutable.iterator();
    }
    
    @Override
    public Object[] toArray() {
        final List<String> result = DefaultThreadContextStack.stack.get();
        if (result == null) {
            return new String[0];
        }
        return result.toArray(new Object[result.size()]);
    }
    
    @Override
    public <T> T[] toArray(final T[] ts) {
        final List<String> result = DefaultThreadContextStack.stack.get();
        if (result == null) {
            if (ts.length > 0) {
                ts[0] = null;
            }
            return ts;
        }
        return result.toArray(ts);
    }
    
    @Override
    public boolean add(final String s) {
        if (!this.useStack) {
            return false;
        }
        final List<String> list = DefaultThreadContextStack.stack.get();
        final List<String> copy = (list == null) ? new ArrayList<String>() : new ArrayList<String>(list);
        copy.add(s);
        DefaultThreadContextStack.stack.set(Collections.unmodifiableList((List<? extends String>)copy));
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        if (!this.useStack) {
            return false;
        }
        final List<String> list = DefaultThreadContextStack.stack.get();
        if (list == null || list.size() == 0) {
            return false;
        }
        final List<String> copy = new ArrayList<String>(list);
        final boolean result = copy.remove(o);
        DefaultThreadContextStack.stack.set(Collections.unmodifiableList((List<? extends String>)copy));
        return result;
    }
    
    @Override
    public boolean containsAll(final Collection<?> objects) {
        if (objects.isEmpty()) {
            return true;
        }
        final List<String> list = DefaultThreadContextStack.stack.get();
        return list != null && list.containsAll(objects);
    }
    
    @Override
    public boolean addAll(final Collection<? extends String> strings) {
        if (!this.useStack || strings.isEmpty()) {
            return false;
        }
        final List<String> list = DefaultThreadContextStack.stack.get();
        final List<String> copy = (list == null) ? new ArrayList<String>() : new ArrayList<String>(list);
        copy.addAll(strings);
        DefaultThreadContextStack.stack.set(Collections.unmodifiableList((List<? extends String>)copy));
        return true;
    }
    
    @Override
    public boolean removeAll(final Collection<?> objects) {
        if (!this.useStack || objects.isEmpty()) {
            return false;
        }
        final List<String> list = DefaultThreadContextStack.stack.get();
        if (list == null || list.isEmpty()) {
            return false;
        }
        final List<String> copy = new ArrayList<String>(list);
        final boolean result = copy.removeAll(objects);
        DefaultThreadContextStack.stack.set(Collections.unmodifiableList((List<? extends String>)copy));
        return result;
    }
    
    @Override
    public boolean retainAll(final Collection<?> objects) {
        if (!this.useStack || objects.isEmpty()) {
            return false;
        }
        final List<String> list = DefaultThreadContextStack.stack.get();
        if (list == null || list.isEmpty()) {
            return false;
        }
        final List<String> copy = new ArrayList<String>(list);
        final boolean result = copy.retainAll(objects);
        DefaultThreadContextStack.stack.set(Collections.unmodifiableList((List<? extends String>)copy));
        return result;
    }
    
    @Override
    public String toString() {
        final List<String> list = DefaultThreadContextStack.stack.get();
        return (list == null) ? "[]" : list.toString();
    }
    
    static {
        DefaultThreadContextStack.stack = new ThreadLocal<List<String>>();
    }
}
