// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class UnpaddedInternalThreadLocalMap
{
    static ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap;
    static final AtomicInteger nextIndex;
    Object[] indexedVariables;
    int futureListenerStackDepth;
    int localChannelReaderStackDepth;
    Map<Class<?>, Boolean> handlerSharableCache;
    IntegerHolder counterHashCode;
    ThreadLocalRandom random;
    Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache;
    Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache;
    StringBuilder stringBuilder;
    Map<Charset, CharsetEncoder> charsetEncoderCache;
    Map<Charset, CharsetDecoder> charsetDecoderCache;
    
    UnpaddedInternalThreadLocalMap(final Object[] indexedVariables) {
        this.indexedVariables = indexedVariables;
    }
    
    static {
        nextIndex = new AtomicInteger();
    }
}
