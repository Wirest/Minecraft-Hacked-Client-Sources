package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class FastThreadLocal<V> {
    private static final int variablesToRemoveIndex = ;
    private final int index = InternalThreadLocalMap.nextVariableIndex();

    public static void removeAll() {
        InternalThreadLocalMap localInternalThreadLocalMap = InternalThreadLocalMap.getIfSet();
        if (localInternalThreadLocalMap == null) {
            return;
        }
        try {
            Object localObject1 = localInternalThreadLocalMap.indexedVariable(variablesToRemoveIndex);
            if ((localObject1 != null) && (localObject1 != InternalThreadLocalMap.UNSET)) {
                Set localSet = (Set) localObject1;
                FastThreadLocal[] arrayOfFastThreadLocal1 = (FastThreadLocal[]) localSet.toArray(new FastThreadLocal[localSet.size()]);
                for (FastThreadLocal localFastThreadLocal : arrayOfFastThreadLocal1) {
                    localFastThreadLocal.remove(localInternalThreadLocalMap);
                }
            }
        } finally {
            InternalThreadLocalMap.remove();
        }
    }

    public static int size() {
        InternalThreadLocalMap localInternalThreadLocalMap = InternalThreadLocalMap.getIfSet();
        if (localInternalThreadLocalMap == null) {
            return 0;
        }
        return localInternalThreadLocalMap.size();
    }

    public static void destroy() {
    }

    private static void addToVariablesToRemove(InternalThreadLocalMap paramInternalThreadLocalMap, FastThreadLocal<?> paramFastThreadLocal) {
        Object localObject = paramInternalThreadLocalMap.indexedVariable(variablesToRemoveIndex);
        Set localSet;
        if ((localObject == InternalThreadLocalMap.UNSET) || (localObject == null)) {
            localSet = Collections.newSetFromMap(new IdentityHashMap());
            paramInternalThreadLocalMap.setIndexedVariable(variablesToRemoveIndex, localSet);
        } else {
            localSet = (Set) localObject;
        }
        localSet.add(paramFastThreadLocal);
    }

    private static void removeFromVariablesToRemove(InternalThreadLocalMap paramInternalThreadLocalMap, FastThreadLocal<?> paramFastThreadLocal) {
        Object localObject = paramInternalThreadLocalMap.indexedVariable(variablesToRemoveIndex);
        if ((localObject == InternalThreadLocalMap.UNSET) || (localObject == null)) {
            return;
        }
        Set localSet = (Set) localObject;
        localSet.remove(paramFastThreadLocal);
    }

    public final V get() {
        return (V) get(InternalThreadLocalMap.get());
    }

    public final V get(InternalThreadLocalMap paramInternalThreadLocalMap) {
        Object localObject = paramInternalThreadLocalMap.indexedVariable(this.index);
        if (localObject != InternalThreadLocalMap.UNSET) {
            return (V) localObject;
        }
        return (V) initialize(paramInternalThreadLocalMap);
    }

    private V initialize(InternalThreadLocalMap paramInternalThreadLocalMap) {
        Object localObject = null;
        try {
            localObject = initialValue();
        } catch (Exception localException) {
            PlatformDependent.throwException(localException);
        }
        paramInternalThreadLocalMap.setIndexedVariable(this.index, localObject);
        addToVariablesToRemove(paramInternalThreadLocalMap, this);
        return (V) localObject;
    }

    public final void set(V paramV) {
        if (paramV != InternalThreadLocalMap.UNSET) {
            set(InternalThreadLocalMap.get(), paramV);
        } else {
            remove();
        }
    }

    public final void set(InternalThreadLocalMap paramInternalThreadLocalMap, V paramV) {
        if (paramV != InternalThreadLocalMap.UNSET) {
            if (paramInternalThreadLocalMap.setIndexedVariable(this.index, paramV)) {
                addToVariablesToRemove(paramInternalThreadLocalMap, this);
            }
        } else {
            remove(paramInternalThreadLocalMap);
        }
    }

    public final boolean isSet() {
        return isSet(InternalThreadLocalMap.getIfSet());
    }

    public final boolean isSet(InternalThreadLocalMap paramInternalThreadLocalMap) {
        return (paramInternalThreadLocalMap != null) && (paramInternalThreadLocalMap.isIndexedVariableSet(this.index));
    }

    public final void remove() {
        remove(InternalThreadLocalMap.getIfSet());
    }

    public final void remove(InternalThreadLocalMap paramInternalThreadLocalMap) {
        if (paramInternalThreadLocalMap == null) {
            return;
        }
        Object localObject = paramInternalThreadLocalMap.removeIndexedVariable(this.index);
        removeFromVariablesToRemove(paramInternalThreadLocalMap, this);
        if (localObject != InternalThreadLocalMap.UNSET) {
            try {
                onRemoval(localObject);
            } catch (Exception localException) {
                PlatformDependent.throwException(localException);
            }
        }
    }

    protected V initialValue()
            throws Exception {
        return null;
    }

    protected void onRemoval(V paramV)
            throws Exception {
    }
}




