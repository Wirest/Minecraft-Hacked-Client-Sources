// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.selector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.impl.ReflectiveCallerClassUtility;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import java.net.URI;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.atomic.AtomicReference;

public class ClassLoaderContextSelector implements ContextSelector
{
    private static final AtomicReference<LoggerContext> CONTEXT;
    private static final PrivateSecurityManager SECURITY_MANAGER;
    private static final StatusLogger LOGGER;
    private static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP;
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return this.getContext(fqcn, loader, currentContext, null);
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        if (currentContext) {
            final LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
            if (ctx != null) {
                return ctx;
            }
            return this.getDefault();
        }
        else {
            if (loader != null) {
                return this.locateContext(loader, configLocation);
            }
            if (ReflectiveCallerClassUtility.isSupported()) {
                try {
                    Class<?> clazz = Class.class;
                    boolean next = false;
                    int index = 2;
                    while (clazz != null) {
                        clazz = ReflectiveCallerClassUtility.getCaller(index);
                        if (clazz == null) {
                            break;
                        }
                        if (clazz.getName().equals(fqcn)) {
                            next = true;
                        }
                        else if (next) {
                            break;
                        }
                        ++index;
                    }
                    if (clazz != null) {
                        return this.locateContext(clazz.getClassLoader(), configLocation);
                    }
                }
                catch (Exception ex) {}
            }
            if (ClassLoaderContextSelector.SECURITY_MANAGER != null) {
                final Class<?> clazz = ClassLoaderContextSelector.SECURITY_MANAGER.getCaller(fqcn);
                if (clazz != null) {
                    final ClassLoader ldr = (clazz.getClassLoader() != null) ? clazz.getClassLoader() : ClassLoader.getSystemClassLoader();
                    return this.locateContext(ldr, configLocation);
                }
            }
            final Throwable t = new Throwable();
            boolean next = false;
            String name = null;
            for (final StackTraceElement element : t.getStackTrace()) {
                if (element.getClassName().equals(fqcn)) {
                    next = true;
                }
                else if (next) {
                    name = element.getClassName();
                    break;
                }
            }
            if (name != null) {
                try {
                    return this.locateContext(Loader.loadClass(name).getClassLoader(), configLocation);
                }
                catch (ClassNotFoundException ex2) {}
            }
            final LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
            if (lc != null) {
                return lc;
            }
            return this.getDefault();
        }
    }
    
    @Override
    public void removeContext(final LoggerContext context) {
        for (final Map.Entry<String, AtomicReference<WeakReference<LoggerContext>>> entry : ClassLoaderContextSelector.CONTEXT_MAP.entrySet()) {
            final LoggerContext ctx = entry.getValue().get().get();
            if (ctx == context) {
                ClassLoaderContextSelector.CONTEXT_MAP.remove(entry.getKey());
            }
        }
    }
    
    @Override
    public List<LoggerContext> getLoggerContexts() {
        final List<LoggerContext> list = new ArrayList<LoggerContext>();
        final Collection<AtomicReference<WeakReference<LoggerContext>>> coll = ClassLoaderContextSelector.CONTEXT_MAP.values();
        for (final AtomicReference<WeakReference<LoggerContext>> ref : coll) {
            final LoggerContext ctx = ref.get().get();
            if (ctx != null) {
                list.add(ctx);
            }
        }
        return Collections.unmodifiableList((List<? extends LoggerContext>)list);
    }
    
    private LoggerContext locateContext(final ClassLoader loader, final URI configLocation) {
        final String name = loader.toString();
        AtomicReference<WeakReference<LoggerContext>> ref = ClassLoaderContextSelector.CONTEXT_MAP.get(name);
        if (ref == null) {
            if (configLocation == null) {
                for (ClassLoader parent = loader.getParent(); parent != null; parent = parent.getParent()) {
                    ref = ClassLoaderContextSelector.CONTEXT_MAP.get(parent.toString());
                    if (ref != null) {
                        final WeakReference<LoggerContext> r = ref.get();
                        final LoggerContext ctx = r.get();
                        if (ctx != null) {
                            return ctx;
                        }
                    }
                }
            }
            LoggerContext ctx2 = new LoggerContext(name, null, configLocation);
            final AtomicReference<WeakReference<LoggerContext>> r2 = new AtomicReference<WeakReference<LoggerContext>>();
            r2.set(new WeakReference<LoggerContext>(ctx2));
            ClassLoaderContextSelector.CONTEXT_MAP.putIfAbsent(loader.toString(), r2);
            ctx2 = ClassLoaderContextSelector.CONTEXT_MAP.get(name).get().get();
            return ctx2;
        }
        final WeakReference<LoggerContext> r3 = ref.get();
        LoggerContext ctx3 = r3.get();
        if (ctx3 != null) {
            if (ctx3.getConfigLocation() == null && configLocation != null) {
                ClassLoaderContextSelector.LOGGER.debug("Setting configuration to {}", configLocation);
                ctx3.setConfigLocation(configLocation);
            }
            else if (ctx3.getConfigLocation() != null && configLocation != null && !ctx3.getConfigLocation().equals(configLocation)) {
                ClassLoaderContextSelector.LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", configLocation, ctx3.getConfigLocation());
            }
            return ctx3;
        }
        ctx3 = new LoggerContext(name, null, configLocation);
        ref.compareAndSet(r3, new WeakReference<LoggerContext>(ctx3));
        return ctx3;
    }
    
    private LoggerContext getDefault() {
        final LoggerContext ctx = ClassLoaderContextSelector.CONTEXT.get();
        if (ctx != null) {
            return ctx;
        }
        ClassLoaderContextSelector.CONTEXT.compareAndSet(null, new LoggerContext("Default"));
        return ClassLoaderContextSelector.CONTEXT.get();
    }
    
    static {
        CONTEXT = new AtomicReference<LoggerContext>();
        LOGGER = StatusLogger.getLogger();
        CONTEXT_MAP = new ConcurrentHashMap<String, AtomicReference<WeakReference<LoggerContext>>>();
        if (ReflectiveCallerClassUtility.isSupported()) {
            SECURITY_MANAGER = null;
        }
        else {
            PrivateSecurityManager securityManager;
            try {
                securityManager = new PrivateSecurityManager();
                if (securityManager.getCaller(ClassLoaderContextSelector.class.getName()) == null) {
                    securityManager = null;
                    ClassLoaderContextSelector.LOGGER.error("Unable to obtain call stack from security manager.");
                }
            }
            catch (Exception e) {
                securityManager = null;
                ClassLoaderContextSelector.LOGGER.debug("Unable to install security manager", e);
            }
            SECURITY_MANAGER = securityManager;
        }
    }
    
    private static class PrivateSecurityManager extends SecurityManager
    {
        public Class<?> getCaller(final String fqcn) {
            final Class<?>[] classes = this.getClassContext();
            boolean next = false;
            for (final Class<?> clazz : classes) {
                if (clazz.getName().equals(fqcn)) {
                    next = true;
                }
                else if (next) {
                    return clazz;
                }
            }
            return null;
        }
    }
}
