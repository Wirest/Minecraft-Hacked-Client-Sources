package io.netty.util.internal;

import io.netty.util.Recycler;
import io.netty.util.Recycler.Handle;

import java.util.*;

public final class RecyclableArrayList
        extends ArrayList<Object> {
    private static final long serialVersionUID = -8605125654176467947L;
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private static final Recycler<RecyclableArrayList> RECYCLER = new Recycler() {
        protected RecyclableArrayList newObject(Recycler.Handle paramAnonymousHandle) {
            return new RecyclableArrayList(paramAnonymousHandle, null);
        }
    };
    private final Recycler.Handle handle;

    private RecyclableArrayList(Recycler.Handle paramHandle) {
        this(paramHandle, 8);
    }

    private RecyclableArrayList(Recycler.Handle paramHandle, int paramInt) {
        super(paramInt);
        this.handle = paramHandle;
    }

    public static RecyclableArrayList newInstance() {
        return newInstance(8);
    }

    public static RecyclableArrayList newInstance(int paramInt) {
        RecyclableArrayList localRecyclableArrayList = (RecyclableArrayList) RECYCLER.get();
        localRecyclableArrayList.ensureCapacity(paramInt);
        return localRecyclableArrayList;
    }

    private static void checkNullElements(Collection<?> paramCollection) {
        Object localObject1;
        if (((paramCollection instanceof RandomAccess)) && ((paramCollection instanceof List))) {
            localObject1 = (List) paramCollection;
            int i = ((List) localObject1).size();
            for (int j = 0; j < i; j++) {
                if (((List) localObject1).get(j) == null) {
                    throw new IllegalArgumentException("c contains null values");
                }
            }
        } else {
            localObject1 = paramCollection.iterator();
            while (((Iterator) localObject1).hasNext()) {
                Object localObject2 = ((Iterator) localObject1).next();
                if (localObject2 == null) {
                    throw new IllegalArgumentException("c contains null values");
                }
            }
        }
    }

    public boolean addAll(Collection<?> paramCollection) {
        checkNullElements(paramCollection);
        return super.addAll(paramCollection);
    }

    public boolean addAll(int paramInt, Collection<?> paramCollection) {
        checkNullElements(paramCollection);
        return super.addAll(paramInt, paramCollection);
    }

    public boolean add(Object paramObject) {
        if (paramObject == null) {
            throw new NullPointerException("element");
        }
        return super.add(paramObject);
    }

    public void add(int paramInt, Object paramObject) {
        if (paramObject == null) {
            throw new NullPointerException("element");
        }
        super.add(paramInt, paramObject);
    }

    public Object set(int paramInt, Object paramObject) {
        if (paramObject == null) {
            throw new NullPointerException("element");
        }
        return super.set(paramInt, paramObject);
    }

    public boolean recycle() {
        clear();
        return RECYCLER.recycle(this, this.handle);
    }
}




