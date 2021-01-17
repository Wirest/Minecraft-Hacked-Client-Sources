// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.Collection;
import io.netty.util.Recycler;
import java.util.ArrayList;

public final class RecyclableArrayList extends ArrayList<Object>
{
    private static final long serialVersionUID = -8605125654176467947L;
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private static final Recycler<RecyclableArrayList> RECYCLER;
    private final Recycler.Handle handle;
    
    public static RecyclableArrayList newInstance() {
        return newInstance(8);
    }
    
    public static RecyclableArrayList newInstance(final int minCapacity) {
        final RecyclableArrayList ret = RecyclableArrayList.RECYCLER.get();
        ret.ensureCapacity(minCapacity);
        return ret;
    }
    
    private RecyclableArrayList(final Recycler.Handle handle) {
        this(handle, 8);
    }
    
    private RecyclableArrayList(final Recycler.Handle handle, final int initialCapacity) {
        super(initialCapacity);
        this.handle = handle;
    }
    
    @Override
    public boolean addAll(final Collection<?> c) {
        checkNullElements(c);
        return super.addAll(c);
    }
    
    @Override
    public boolean addAll(final int index, final Collection<?> c) {
        checkNullElements(c);
        return super.addAll(index, c);
    }
    
    private static void checkNullElements(final Collection<?> c) {
        if (c instanceof RandomAccess && c instanceof List) {
            final List<?> list = (List<?>)(List)c;
            for (int size = list.size(), i = 0; i < size; ++i) {
                if (list.get(i) == null) {
                    throw new IllegalArgumentException("c contains null values");
                }
            }
        }
        else {
            for (final Object element : c) {
                if (element == null) {
                    throw new IllegalArgumentException("c contains null values");
                }
            }
        }
    }
    
    @Override
    public boolean add(final Object element) {
        if (element == null) {
            throw new NullPointerException("element");
        }
        return super.add(element);
    }
    
    @Override
    public void add(final int index, final Object element) {
        if (element == null) {
            throw new NullPointerException("element");
        }
        super.add(index, element);
    }
    
    @Override
    public Object set(final int index, final Object element) {
        if (element == null) {
            throw new NullPointerException("element");
        }
        return super.set(index, element);
    }
    
    public boolean recycle() {
        this.clear();
        return RecyclableArrayList.RECYCLER.recycle(this, this.handle);
    }
    
    static {
        RECYCLER = new Recycler<RecyclableArrayList>() {
            @Override
            protected RecyclableArrayList newObject(final Handle handle) {
                return new RecyclableArrayList(handle, null);
            }
        };
    }
}
