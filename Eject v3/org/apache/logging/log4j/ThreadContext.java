package org.apache.logging.log4j;

import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.spi.*;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ProviderUtil;

import java.io.Serializable;
import java.util.*;

public final class ThreadContext {
    public static final Map<String, String> EMPTY_MAP = ;
    public static final ThreadContextStack EMPTY_STACK = new MutableThreadContextStack(new ArrayList());
    private static final String DISABLE_MAP = "disableThreadContextMap";
    private static final String DISABLE_STACK = "disableThreadContextStack";
    private static final String DISABLE_ALL = "disableThreadContext";
    private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static boolean all;
    private static boolean useMap;
    private static boolean useStack;
    private static ThreadContextMap contextMap;
    private static ThreadContextStack contextStack;

    static {
        PropertiesUtil localPropertiesUtil = PropertiesUtil.getProperties();
        all = localPropertiesUtil.getBooleanProperty("disableThreadContext");
        useStack = (!localPropertiesUtil.getBooleanProperty("disableThreadContextStack")) && (!all);
        contextStack = new DefaultThreadContextStack(useStack);
        useMap = (!localPropertiesUtil.getBooleanProperty("disableThreadContextMap")) && (!all);
        String str1 = localPropertiesUtil.getStringProperty("log4j2.threadContextMap");
        ClassLoader localClassLoader = ProviderUtil.findClassLoader();
        if (str1 != null) {
            try {
                Class localClass1 = localClassLoader.loadClass(str1);
                if (ThreadContextMap.class.isAssignableFrom(localClass1)) {
                    contextMap = (ThreadContextMap) localClass1.newInstance();
                }
            } catch (ClassNotFoundException localClassNotFoundException1) {
                LOGGER.error("Unable to locate configured LoggerContextFactory {}", new Object[]{str1});
            } catch (Exception localException1) {
                LOGGER.error("Unable to create configured LoggerContextFactory {}", new Object[]{str1, localException1});
            }
        }
        if ((contextMap == null) && (ProviderUtil.hasProviders())) {
            LoggerContextFactory localLoggerContextFactory = LogManager.getFactory();
            Iterator localIterator = ProviderUtil.getProviders();
            while (localIterator.hasNext()) {
                Provider localProvider = (Provider) localIterator.next();
                str1 = localProvider.getThreadContextMap();
                String str2 = localProvider.getClassName();
                if ((str1 != null) && (localLoggerContextFactory.getClass().getName().equals(str2))) {
                    try {
                        Class localClass2 = localClassLoader.loadClass(str1);
                        if (ThreadContextMap.class.isAssignableFrom(localClass2)) {
                            contextMap = (ThreadContextMap) localClass2.newInstance();
                            break;
                        }
                    } catch (ClassNotFoundException localClassNotFoundException2) {
                        LOGGER.error("Unable to locate configured LoggerContextFactory {}", new Object[]{str1});
                        contextMap = new DefaultThreadContextMap(useMap);
                    } catch (Exception localException2) {
                        LOGGER.error("Unable to create configured LoggerContextFactory {}", new Object[]{str1, localException2});
                        contextMap = new DefaultThreadContextMap(useMap);
                    }
                }
            }
        }
        if (contextMap == null) {
            contextMap = new DefaultThreadContextMap(useMap);
        }
    }

    public static void put(String paramString1, String paramString2) {
        contextMap.put(paramString1, paramString2);
    }

    public static String get(String paramString) {
        return contextMap.get(paramString);
    }

    public static void remove(String paramString) {
        contextMap.remove(paramString);
    }

    public static void clear() {
        contextMap.clear();
    }

    public static boolean containsKey(String paramString) {
        return contextMap.containsKey(paramString);
    }

    public static Map<String, String> getContext() {
        return contextMap.getCopy();
    }

    public static Map<String, String> getImmutableContext() {
        Map localMap = contextMap.getImmutableMapOrNull();
        return localMap == null ? EMPTY_MAP : localMap;
    }

    public static boolean isEmpty() {
        return contextMap.isEmpty();
    }

    public static void clearStack() {
        contextStack.clear();
    }

    public static ContextStack cloneStack() {
        return contextStack.copy();
    }

    public static ContextStack getImmutableStack() {
        return contextStack;
    }

    public static void setStack(Collection<String> paramCollection) {
        if ((paramCollection.size() == 0) || (!useStack)) {
            return;
        }
        contextStack.clear();
        contextStack.addAll(paramCollection);
    }

    public static int getDepth() {
        return contextStack.getDepth();
    }

    public static String pop() {
        return contextStack.pop();
    }

    public static String peek() {
        return contextStack.peek();
    }

    public static void push(String paramString) {
        contextStack.push(paramString);
    }

    public static void push(String paramString, Object... paramVarArgs) {
        contextStack.push(ParameterizedMessage.format(paramString, paramVarArgs));
    }

    public static void removeStack() {
        contextStack.clear();
    }

    public static void trim(int paramInt) {
        contextStack.trim(paramInt);
    }

    public static abstract interface ContextStack
            extends Serializable {
        public abstract void clear();

        public abstract String pop();

        public abstract String peek();

        public abstract void push(String paramString);

        public abstract int getDepth();

        public abstract List<String> asList();

        public abstract void trim(int paramInt);

        public abstract ContextStack copy();
    }
}




