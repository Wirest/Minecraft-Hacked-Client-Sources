// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import java.io.Serializable;
import java.util.Iterator;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.util.ProviderUtil;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.List;
import org.apache.logging.log4j.spi.MutableThreadContextStack;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.logging.log4j.message.ParameterizedMessage;
import java.util.Collection;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextStack;
import java.util.Map;

public final class ThreadContext
{
    public static final Map<String, String> EMPTY_MAP;
    public static final ThreadContextStack EMPTY_STACK;
    private static final String DISABLE_MAP = "disableThreadContextMap";
    private static final String DISABLE_STACK = "disableThreadContextStack";
    private static final String DISABLE_ALL = "disableThreadContext";
    private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
    private static boolean all;
    private static boolean useMap;
    private static boolean useStack;
    private static ThreadContextMap contextMap;
    private static ThreadContextStack contextStack;
    private static final Logger LOGGER;
    
    private ThreadContext() {
    }
    
    public static void put(final String key, final String value) {
        ThreadContext.contextMap.put(key, value);
    }
    
    public static String get(final String key) {
        return ThreadContext.contextMap.get(key);
    }
    
    public static void remove(final String key) {
        ThreadContext.contextMap.remove(key);
    }
    
    public static void clear() {
        ThreadContext.contextMap.clear();
    }
    
    public static boolean containsKey(final String key) {
        return ThreadContext.contextMap.containsKey(key);
    }
    
    public static Map<String, String> getContext() {
        return ThreadContext.contextMap.getCopy();
    }
    
    public static Map<String, String> getImmutableContext() {
        final Map<String, String> map = ThreadContext.contextMap.getImmutableMapOrNull();
        return (map == null) ? ThreadContext.EMPTY_MAP : map;
    }
    
    public static boolean isEmpty() {
        return ThreadContext.contextMap.isEmpty();
    }
    
    public static void clearStack() {
        ThreadContext.contextStack.clear();
    }
    
    public static ContextStack cloneStack() {
        return ThreadContext.contextStack.copy();
    }
    
    public static ContextStack getImmutableStack() {
        return ThreadContext.contextStack;
    }
    
    public static void setStack(final Collection<String> stack) {
        if (stack.size() == 0 || !ThreadContext.useStack) {
            return;
        }
        ThreadContext.contextStack.clear();
        ThreadContext.contextStack.addAll(stack);
    }
    
    public static int getDepth() {
        return ThreadContext.contextStack.getDepth();
    }
    
    public static String pop() {
        return ThreadContext.contextStack.pop();
    }
    
    public static String peek() {
        return ThreadContext.contextStack.peek();
    }
    
    public static void push(final String message) {
        ThreadContext.contextStack.push(message);
    }
    
    public static void push(final String message, final Object... args) {
        ThreadContext.contextStack.push(ParameterizedMessage.format(message, args));
    }
    
    public static void removeStack() {
        ThreadContext.contextStack.clear();
    }
    
    public static void trim(final int depth) {
        ThreadContext.contextStack.trim(depth);
    }
    
    static {
        EMPTY_MAP = Collections.emptyMap();
        EMPTY_STACK = new MutableThreadContextStack(new ArrayList<String>());
        LOGGER = StatusLogger.getLogger();
        final PropertiesUtil managerProps = PropertiesUtil.getProperties();
        ThreadContext.all = managerProps.getBooleanProperty("disableThreadContext");
        ThreadContext.useStack = (!managerProps.getBooleanProperty("disableThreadContextStack") && !ThreadContext.all);
        ThreadContext.contextStack = new DefaultThreadContextStack(ThreadContext.useStack);
        ThreadContext.useMap = (!managerProps.getBooleanProperty("disableThreadContextMap") && !ThreadContext.all);
        String threadContextMapName = managerProps.getStringProperty("log4j2.threadContextMap");
        final ClassLoader cl = ProviderUtil.findClassLoader();
        if (threadContextMapName != null) {
            try {
                final Class<?> clazz = cl.loadClass(threadContextMapName);
                if (ThreadContextMap.class.isAssignableFrom(clazz)) {
                    ThreadContext.contextMap = (ThreadContextMap)clazz.newInstance();
                }
            }
            catch (ClassNotFoundException cnfe) {
                ThreadContext.LOGGER.error("Unable to locate configured LoggerContextFactory {}", threadContextMapName);
            }
            catch (Exception ex) {
                ThreadContext.LOGGER.error("Unable to create configured LoggerContextFactory {}", threadContextMapName, ex);
            }
        }
        if (ThreadContext.contextMap == null && ProviderUtil.hasProviders()) {
            final LoggerContextFactory factory = LogManager.getFactory();
            final Iterator<Provider> providers = ProviderUtil.getProviders();
            while (providers.hasNext()) {
                final Provider provider = providers.next();
                threadContextMapName = provider.getThreadContextMap();
                final String factoryClassName = provider.getClassName();
                if (threadContextMapName != null && factory.getClass().getName().equals(factoryClassName)) {
                    try {
                        final Class<?> clazz2 = cl.loadClass(threadContextMapName);
                        if (ThreadContextMap.class.isAssignableFrom(clazz2)) {
                            ThreadContext.contextMap = (ThreadContextMap)clazz2.newInstance();
                            break;
                        }
                        continue;
                    }
                    catch (ClassNotFoundException cnfe2) {
                        ThreadContext.LOGGER.error("Unable to locate configured LoggerContextFactory {}", threadContextMapName);
                        ThreadContext.contextMap = new DefaultThreadContextMap(ThreadContext.useMap);
                    }
                    catch (Exception ex2) {
                        ThreadContext.LOGGER.error("Unable to create configured LoggerContextFactory {}", threadContextMapName, ex2);
                        ThreadContext.contextMap = new DefaultThreadContextMap(ThreadContext.useMap);
                    }
                }
            }
        }
        if (ThreadContext.contextMap == null) {
            ThreadContext.contextMap = new DefaultThreadContextMap(ThreadContext.useMap);
        }
    }
    
    public interface ContextStack extends Serializable
    {
        void clear();
        
        String pop();
        
        String peek();
        
        void push(final String p0);
        
        int getDepth();
        
        List<String> asList();
        
        void trim(final int p0);
        
        ContextStack copy();
    }
}
