package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MutableThreadContextStack
        implements ThreadContextStack {
    private static final long serialVersionUID = 50505011L;
    private final List<String> list;

    public MutableThreadContextStack(List<String> paramList) {
        this.list = new ArrayList(paramList);
    }

    private MutableThreadContextStack(MutableThreadContextStack paramMutableThreadContextStack) {
        this.list = new ArrayList(paramMutableThreadContextStack.list);
    }

    public String pop() {
        if (this.list.isEmpty()) {
            return null;
        }
        int i = this.list.size() - 1;
        String str = (String) this.list.remove(i);
        return str;
    }

    public String peek() {
        if (this.list.isEmpty()) {
            return null;
        }
        int i = this.list.size() - 1;
        return (String) this.list.get(i);
    }

    public void push(String paramString) {
        this.list.add(paramString);
    }

    public int getDepth() {
        return this.list.size();
    }

    public List<String> asList() {
        return this.list;
    }

    public void trim(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        if (this.list == null) {
            return;
        }
        ArrayList localArrayList = new ArrayList(this.list.size());
        int i = Math.min(paramInt, this.list.size());
        for (int j = 0; j < i; j++) {
            localArrayList.add(this.list.get(j));
        }
        this.list.clear();
        this.list.addAll(localArrayList);
    }

    public ThreadContextStack copy() {
        return new MutableThreadContextStack(this);
    }

    public void clear() {
        this.list.clear();
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(Object paramObject) {
        return this.list.contains(paramObject);
    }

    public Iterator<String> iterator() {
        return this.list.iterator();
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public <T> T[] toArray(T[] paramArrayOfT) {
        return this.list.toArray(paramArrayOfT);
    }

    public boolean add(String paramString) {
        return this.list.add(paramString);
    }

    public boolean remove(Object paramObject) {
        return this.list.remove(paramObject);
    }

    public boolean containsAll(Collection<?> paramCollection) {
        return this.list.containsAll(paramCollection);
    }

    public boolean addAll(Collection<? extends String> paramCollection) {
        return this.list.addAll(paramCollection);
    }

    public boolean removeAll(Collection<?> paramCollection) {
        return this.list.removeAll(paramCollection);
    }

    public boolean retainAll(Collection<?> paramCollection) {
        return this.list.retainAll(paramCollection);
    }

    public String toString() {
        return String.valueOf(this.list);
    }
}




