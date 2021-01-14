package org.apache.logging.log4j.spi;

import java.util.*;

public class DefaultThreadContextStack
        implements ThreadContextStack {
    private static final long serialVersionUID = 5050501L;
    private static ThreadLocal<List<String>> stack = new ThreadLocal();
    private final boolean useStack;

    public DefaultThreadContextStack(boolean paramBoolean) {
        this.useStack = paramBoolean;
    }

    public String pop() {
        if (!this.useStack) {
            return "";
        }
        List localList = (List) stack.get();
        if ((localList == null) || (localList.size() == 0)) {
            throw new NoSuchElementException("The ThreadContext stack is empty");
        }
        ArrayList localArrayList = new ArrayList(localList);
        int i = localArrayList.size() - 1;
        String str = (String) localArrayList.remove(i);
        stack.set(Collections.unmodifiableList(localArrayList));
        return str;
    }

    public String peek() {
        List localList = (List) stack.get();
        if ((localList == null) || (localList.size() == 0)) {
            return null;
        }
        int i = localList.size() - 1;
        return (String) localList.get(i);
    }

    public void push(String paramString) {
        if (!this.useStack) {
            return;
        }
        add(paramString);
    }

    public int getDepth() {
        List localList = (List) stack.get();
        return localList == null ? 0 : localList.size();
    }

    public List<String> asList() {
        List localList = (List) stack.get();
        if (localList == null) {
            return Collections.emptyList();
        }
        return localList;
    }

    public void trim(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        List localList = (List) stack.get();
        if (localList == null) {
            return;
        }
        ArrayList localArrayList = new ArrayList();
        int i = Math.min(paramInt, localList.size());
        for (int j = 0; j < i; j++) {
            localArrayList.add(localList.get(j));
        }
        stack.set(localArrayList);
    }

    public ThreadContextStack copy() {
        List localList = null;
        if ((!this.useStack) || ((localList = (List) stack.get()) == null)) {
            return new MutableThreadContextStack(new ArrayList());
        }
        return new MutableThreadContextStack(localList);
    }

    public void clear() {
        stack.remove();
    }

    public int size() {
        List localList = (List) stack.get();
        return localList == null ? 0 : localList.size();
    }

    public boolean isEmpty() {
        List localList = (List) stack.get();
        return (localList == null) || (localList.isEmpty());
    }

    public boolean contains(Object paramObject) {
        List localList = (List) stack.get();
        return (localList != null) && (localList.contains(paramObject));
    }

    public Iterator<String> iterator() {
        List localList1 = (List) stack.get();
        if (localList1 == null) {
            List localList2 = Collections.emptyList();
            return localList2.iterator();
        }
        return localList1.iterator();
    }

    public Object[] toArray() {
        List localList = (List) stack.get();
        if (localList == null) {
            return new String[0];
        }
        return localList.toArray(new Object[localList.size()]);
    }

    public <T> T[] toArray(T[] paramArrayOfT) {
        List localList = (List) stack.get();
        if (localList == null) {
            if (paramArrayOfT.length > 0) {
                paramArrayOfT[0] = null;
            }
            return paramArrayOfT;
        }
        return localList.toArray(paramArrayOfT);
    }

    public boolean add(String paramString) {
        if (!this.useStack) {
            return false;
        }
        List localList = (List) stack.get();
        ArrayList localArrayList = localList == null ? new ArrayList() : new ArrayList(localList);
        localArrayList.add(paramString);
        stack.set(Collections.unmodifiableList(localArrayList));
        return true;
    }

    public boolean remove(Object paramObject) {
        if (!this.useStack) {
            return false;
        }
        List localList = (List) stack.get();
        if ((localList == null) || (localList.size() == 0)) {
            return false;
        }
        ArrayList localArrayList = new ArrayList(localList);
        boolean bool = localArrayList.remove(paramObject);
        stack.set(Collections.unmodifiableList(localArrayList));
        return bool;
    }

    public boolean containsAll(Collection<?> paramCollection) {
        if (paramCollection.isEmpty()) {
            return true;
        }
        List localList = (List) stack.get();
        return (localList != null) && (localList.containsAll(paramCollection));
    }

    public boolean addAll(Collection<? extends String> paramCollection) {
        if ((!this.useStack) || (paramCollection.isEmpty())) {
            return false;
        }
        List localList = (List) stack.get();
        ArrayList localArrayList = localList == null ? new ArrayList() : new ArrayList(localList);
        localArrayList.addAll(paramCollection);
        stack.set(Collections.unmodifiableList(localArrayList));
        return true;
    }

    public boolean removeAll(Collection<?> paramCollection) {
        if ((!this.useStack) || (paramCollection.isEmpty())) {
            return false;
        }
        List localList = (List) stack.get();
        if ((localList == null) || (localList.isEmpty())) {
            return false;
        }
        ArrayList localArrayList = new ArrayList(localList);
        boolean bool = localArrayList.removeAll(paramCollection);
        stack.set(Collections.unmodifiableList(localArrayList));
        return bool;
    }

    public boolean retainAll(Collection<?> paramCollection) {
        if ((!this.useStack) || (paramCollection.isEmpty())) {
            return false;
        }
        List localList = (List) stack.get();
        if ((localList == null) || (localList.isEmpty())) {
            return false;
        }
        ArrayList localArrayList = new ArrayList(localList);
        boolean bool = localArrayList.retainAll(paramCollection);
        stack.set(Collections.unmodifiableList(localArrayList));
        return bool;
    }

    public String toString() {
        List localList = (List) stack.get();
        return localList == null ? "[]" : localList.toString();
    }
}




