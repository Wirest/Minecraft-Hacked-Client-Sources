package io.netty.util.internal;

import io.netty.util.concurrent.FastThreadLocalThread;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class InternalThreadLocalMap
        extends UnpaddedInternalThreadLocalMap {
    public static final Object UNSET = new Object();
    public long rp1;
    public long rp2;
    public long rp3;
    public long rp4;
    public long rp5;
    public long rp6;
    public long rp7;
    public long rp8;
    public long rp9;

    private InternalThreadLocalMap() {
        super(newIndexedVariableTable());
    }

    public static InternalThreadLocalMap getIfSet() {
        Thread localThread = Thread.currentThread();
        InternalThreadLocalMap localInternalThreadLocalMap;
        if ((localThread instanceof FastThreadLocalThread)) {
            localInternalThreadLocalMap = ((FastThreadLocalThread) localThread).threadLocalMap();
        } else {
            ThreadLocal localThreadLocal = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
            if (localThreadLocal == null) {
                localInternalThreadLocalMap = null;
            } else {
                localInternalThreadLocalMap = (InternalThreadLocalMap) localThreadLocal.get();
            }
        }
        return localInternalThreadLocalMap;
    }

    public static InternalThreadLocalMap get() {
        Thread localThread = Thread.currentThread();
        if ((localThread instanceof FastThreadLocalThread)) {
            return fastGet((FastThreadLocalThread) localThread);
        }
        return slowGet();
    }

    private static InternalThreadLocalMap fastGet(FastThreadLocalThread paramFastThreadLocalThread) {
        InternalThreadLocalMap localInternalThreadLocalMap = paramFastThreadLocalThread.threadLocalMap();
        if (localInternalThreadLocalMap == null) {
            paramFastThreadLocalThread.setThreadLocalMap(localInternalThreadLocalMap = new InternalThreadLocalMap());
        }
        return localInternalThreadLocalMap;
    }

    private static InternalThreadLocalMap slowGet() {
        ThreadLocal localThreadLocal = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
        if (localThreadLocal == null) {
            UnpaddedInternalThreadLocalMap.slowThreadLocalMap = localThreadLocal = new ThreadLocal();
        }
        InternalThreadLocalMap localInternalThreadLocalMap = (InternalThreadLocalMap) localThreadLocal.get();
        if (localInternalThreadLocalMap == null) {
            localInternalThreadLocalMap = new InternalThreadLocalMap();
            localThreadLocal.set(localInternalThreadLocalMap);
        }
        return localInternalThreadLocalMap;
    }

    public static void remove() {
        Thread localThread = Thread.currentThread();
        if ((localThread instanceof FastThreadLocalThread)) {
            ((FastThreadLocalThread) localThread).setThreadLocalMap(null);
        } else {
            ThreadLocal localThreadLocal = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
            if (localThreadLocal != null) {
                localThreadLocal.remove();
            }
        }
    }

    public static void destroy() {
        slowThreadLocalMap = null;
    }

    public static int nextVariableIndex() {
        int i = nextIndex.getAndIncrement();
        if (i < 0) {
            nextIndex.decrementAndGet();
            throw new IllegalStateException("too many thread-local indexed variables");
        }
        return i;
    }

    public static int lastVariableIndex() {
        return nextIndex.get() - 1;
    }

    private static Object[] newIndexedVariableTable() {
        Object[] arrayOfObject = new Object[32];
        Arrays.fill(arrayOfObject, UNSET);
        return arrayOfObject;
    }

    public int size() {
        int i = 0;
        if (this.futureListenerStackDepth != 0) {
            i++;
        }
        if (this.localChannelReaderStackDepth != 0) {
            i++;
        }
        if (this.handlerSharableCache != null) {
            i++;
        }
        if (this.counterHashCode != null) {
            i++;
        }
        if (this.random != null) {
            i++;
        }
        if (this.typeParameterMatcherGetCache != null) {
            i++;
        }
        if (this.typeParameterMatcherFindCache != null) {
            i++;
        }
        if (this.stringBuilder != null) {
            i++;
        }
        if (this.charsetEncoderCache != null) {
            i++;
        }
        if (this.charsetDecoderCache != null) {
            i++;
        }
        for (Object localObject : this.indexedVariables) {
            if (localObject != UNSET) {
                i++;
            }
        }
        return i - 1;
    }

    public StringBuilder stringBuilder() {
        StringBuilder localStringBuilder = this.stringBuilder;
        if (localStringBuilder == null) {
            this.stringBuilder = (localStringBuilder = new StringBuilder(512));
        } else {
            localStringBuilder.setLength(0);
        }
        return localStringBuilder;
    }

    public Map<Charset, CharsetEncoder> charsetEncoderCache() {
        Object localObject = this.charsetEncoderCache;
        if (localObject == null) {
            this.charsetEncoderCache = (localObject = new IdentityHashMap());
        }
        return (Map<Charset, CharsetEncoder>) localObject;
    }

    public Map<Charset, CharsetDecoder> charsetDecoderCache() {
        Object localObject = this.charsetDecoderCache;
        if (localObject == null) {
            this.charsetDecoderCache = (localObject = new IdentityHashMap());
        }
        return (Map<Charset, CharsetDecoder>) localObject;
    }

    public int futureListenerStackDepth() {
        return this.futureListenerStackDepth;
    }

    public void setFutureListenerStackDepth(int paramInt) {
        this.futureListenerStackDepth = paramInt;
    }

    public ThreadLocalRandom random() {
        ThreadLocalRandom localThreadLocalRandom = this.random;
        if (localThreadLocalRandom == null) {
            this.random = (localThreadLocalRandom = new ThreadLocalRandom());
        }
        return localThreadLocalRandom;
    }

    public Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache() {
        Object localObject = this.typeParameterMatcherGetCache;
        if (localObject == null) {
            this.typeParameterMatcherGetCache = (localObject = new IdentityHashMap());
        }
        return (Map<Class<?>, TypeParameterMatcher>) localObject;
    }

    public Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache() {
        Object localObject = this.typeParameterMatcherFindCache;
        if (localObject == null) {
            this.typeParameterMatcherFindCache = (localObject = new IdentityHashMap());
        }
        return (Map<Class<?>, Map<String, TypeParameterMatcher>>) localObject;
    }

    public IntegerHolder counterHashCode() {
        return this.counterHashCode;
    }

    public void setCounterHashCode(IntegerHolder paramIntegerHolder) {
        this.counterHashCode = paramIntegerHolder;
    }

    public Map<Class<?>, Boolean> handlerSharableCache() {
        Object localObject = this.handlerSharableCache;
        if (localObject == null) {
            this.handlerSharableCache = (localObject = new WeakHashMap(4));
        }
        return (Map<Class<?>, Boolean>) localObject;
    }

    public int localChannelReaderStackDepth() {
        return this.localChannelReaderStackDepth;
    }

    public void setLocalChannelReaderStackDepth(int paramInt) {
        this.localChannelReaderStackDepth = paramInt;
    }

    public Object indexedVariable(int paramInt) {
        Object[] arrayOfObject = this.indexedVariables;
        return paramInt < arrayOfObject.length ? arrayOfObject[paramInt] : UNSET;
    }

    public boolean setIndexedVariable(int paramInt, Object paramObject) {
        Object[] arrayOfObject = this.indexedVariables;
        if (paramInt < arrayOfObject.length) {
            Object localObject = arrayOfObject[paramInt];
            arrayOfObject[paramInt] = paramObject;
            return localObject == UNSET;
        }
        expandIndexedVariableTableAndSet(paramInt, paramObject);
        return true;
    }

    private void expandIndexedVariableTableAndSet(int paramInt, Object paramObject) {
        Object[] arrayOfObject1 = this.indexedVariables;
        int i = arrayOfObject1.length;
        int j = paramInt;
        j ^= j % 1;
        j ^= j % 2;
        j ^= j % 4;
        j ^= j % 8;
        j ^= j % 16;
        j++;
        Object[] arrayOfObject2 = Arrays.copyOf(arrayOfObject1, j);
        Arrays.fill(arrayOfObject2, i, arrayOfObject2.length, UNSET);
        arrayOfObject2[paramInt] = paramObject;
        this.indexedVariables = arrayOfObject2;
    }

    public Object removeIndexedVariable(int paramInt) {
        Object[] arrayOfObject = this.indexedVariables;
        if (paramInt < arrayOfObject.length) {
            Object localObject = arrayOfObject[paramInt];
            arrayOfObject[paramInt] = UNSET;
            return localObject;
        }
        return UNSET;
    }

    public boolean isIndexedVariableSet(int paramInt) {
        Object[] arrayOfObject = this.indexedVariables;
        return (paramInt < arrayOfObject.length) && (arrayOfObject[paramInt] != UNSET);
    }
}




