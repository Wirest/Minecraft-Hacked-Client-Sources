// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.status.StatusLogger;
import java.net.URL;
import java.security.CodeSource;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;
import java.io.Serializable;

public class ThrowableProxy implements Serializable
{
    private static final long serialVersionUID = -2752771578252251910L;
    private static final Logger LOGGER;
    private static final PrivateSecurityManager SECURITY_MANAGER;
    private static final Method GET_SUPPRESSED;
    private static final Method ADD_SUPPRESSED;
    private final ThrowableProxy proxyCause;
    private final Throwable throwable;
    private final String name;
    private final StackTracePackageElement[] callerPackageData;
    private int commonElementCount;
    
    public ThrowableProxy(final Throwable throwable) {
        this.throwable = throwable;
        this.name = throwable.getClass().getName();
        final Map<String, CacheEntry> map = new HashMap<String, CacheEntry>();
        final Stack<Class<?>> stack = this.getCurrentStack();
        this.callerPackageData = this.resolvePackageData(stack, map, null, throwable.getStackTrace());
        this.proxyCause = ((throwable.getCause() == null) ? null : new ThrowableProxy(throwable, stack, map, throwable.getCause()));
        this.setSuppressed(throwable);
    }
    
    private ThrowableProxy(final Throwable parent, final Stack<Class<?>> stack, final Map<String, CacheEntry> map, final Throwable cause) {
        this.throwable = cause;
        this.name = cause.getClass().getName();
        this.callerPackageData = this.resolvePackageData(stack, map, parent.getStackTrace(), cause.getStackTrace());
        this.proxyCause = ((cause.getCause() == null) ? null : new ThrowableProxy(parent, stack, map, cause.getCause()));
        this.setSuppressed(cause);
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public ThrowableProxy getCause() {
        return this.proxyCause;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getCommonElementCount() {
        return this.commonElementCount;
    }
    
    public StackTracePackageElement[] getPackageData() {
        return this.callerPackageData;
    }
    
    @Override
    public String toString() {
        final String msg = this.throwable.getMessage();
        return (msg != null) ? (this.name + ": " + msg) : this.name;
    }
    
    public String getRootCauseStackTrace() {
        return this.getRootCauseStackTrace(null);
    }
    
    public String getRootCauseStackTrace(final List<String> packages) {
        final StringBuilder sb = new StringBuilder();
        if (this.proxyCause != null) {
            this.formatWrapper(sb, this.proxyCause);
            sb.append("Wrapped by: ");
        }
        sb.append(this.toString());
        sb.append("\n");
        this.formatElements(sb, 0, this.throwable.getStackTrace(), this.callerPackageData, packages);
        return sb.toString();
    }
    
    public void formatWrapper(final StringBuilder sb, final ThrowableProxy cause) {
        this.formatWrapper(sb, cause, null);
    }
    
    public void formatWrapper(final StringBuilder sb, final ThrowableProxy cause, final List<String> packages) {
        final Throwable caused = (cause.getCause() != null) ? cause.getCause().getThrowable() : null;
        if (caused != null) {
            this.formatWrapper(sb, cause.proxyCause);
            sb.append("Wrapped by: ");
        }
        sb.append(cause).append("\n");
        this.formatElements(sb, cause.commonElementCount, cause.getThrowable().getStackTrace(), cause.callerPackageData, packages);
    }
    
    public String getExtendedStackTrace() {
        return this.getExtendedStackTrace(null);
    }
    
    public String getExtendedStackTrace(final List<String> packages) {
        final StringBuilder sb = new StringBuilder(this.name);
        final String msg = this.throwable.getMessage();
        if (msg != null) {
            sb.append(": ").append(this.throwable.getMessage());
        }
        sb.append("\n");
        this.formatElements(sb, 0, this.throwable.getStackTrace(), this.callerPackageData, packages);
        if (this.proxyCause != null) {
            this.formatCause(sb, this.proxyCause, packages);
        }
        return sb.toString();
    }
    
    public String getSuppressedStackTrace() {
        final ThrowableProxy[] suppressed = this.getSuppressed();
        if (suppressed == null || suppressed.length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder("Suppressed Stack Trace Elements:\n");
        for (final ThrowableProxy proxy : suppressed) {
            sb.append(proxy.getExtendedStackTrace());
        }
        return sb.toString();
    }
    
    private void formatCause(final StringBuilder sb, final ThrowableProxy cause, final List<String> packages) {
        sb.append("Caused by: ").append(cause).append("\n");
        this.formatElements(sb, cause.commonElementCount, cause.getThrowable().getStackTrace(), cause.callerPackageData, packages);
        if (cause.getCause() != null) {
            this.formatCause(sb, cause.proxyCause, packages);
        }
    }
    
    private void formatElements(final StringBuilder sb, final int commonCount, final StackTraceElement[] causedTrace, final StackTracePackageElement[] packageData, final List<String> packages) {
        if (packages == null || packages.size() == 0) {
            for (int i = 0; i < packageData.length; ++i) {
                this.formatEntry(causedTrace[i], packageData[i], sb);
            }
        }
        else {
            int count = 0;
            for (int j = 0; j < packageData.length; ++j) {
                if (!this.isSuppressed(causedTrace[j], packages)) {
                    if (count > 0) {
                        if (count == 1) {
                            sb.append("\t....\n");
                        }
                        else {
                            sb.append("\t... suppressed ").append(count).append(" lines\n");
                        }
                        count = 0;
                    }
                    this.formatEntry(causedTrace[j], packageData[j], sb);
                }
                else {
                    ++count;
                }
            }
            if (count > 0) {
                if (count == 1) {
                    sb.append("\t...\n");
                }
                else {
                    sb.append("\t... suppressed ").append(count).append(" lines\n");
                }
            }
        }
        if (commonCount != 0) {
            sb.append("\t... ").append(commonCount).append(" more").append("\n");
        }
    }
    
    private void formatEntry(final StackTraceElement element, final StackTracePackageElement packageData, final StringBuilder sb) {
        sb.append("\tat ");
        sb.append(element);
        sb.append(" ");
        sb.append(packageData);
        sb.append("\n");
    }
    
    private boolean isSuppressed(final StackTraceElement element, final List<String> packages) {
        final String className = element.getClassName();
        for (final String pkg : packages) {
            if (className.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }
    
    private Stack<Class<?>> getCurrentStack() {
        if (ReflectiveCallerClassUtility.isSupported()) {
            final Stack<Class<?>> classes = new Stack<Class<?>>();
            int index = 1;
            for (Class<?> clazz = ReflectiveCallerClassUtility.getCaller(index); clazz != null; clazz = ReflectiveCallerClassUtility.getCaller(++index)) {
                classes.push(clazz);
            }
            return classes;
        }
        if (ThrowableProxy.SECURITY_MANAGER != null) {
            final Class<?>[] array = ThrowableProxy.SECURITY_MANAGER.getClasses();
            final Stack<Class<?>> classes2 = new Stack<Class<?>>();
            for (final Class<?> clazz2 : array) {
                classes2.push(clazz2);
            }
            return classes2;
        }
        return new Stack<Class<?>>();
    }
    
    StackTracePackageElement[] resolvePackageData(final Stack<Class<?>> stack, final Map<String, CacheEntry> map, final StackTraceElement[] rootTrace, final StackTraceElement[] stackTrace) {
        int stackLength;
        if (rootTrace != null) {
            int rootIndex;
            int stackIndex;
            for (rootIndex = rootTrace.length - 1, stackIndex = stackTrace.length - 1; rootIndex >= 0 && stackIndex >= 0 && rootTrace[rootIndex].equals(stackTrace[stackIndex]); --rootIndex, --stackIndex) {}
            this.commonElementCount = stackTrace.length - 1 - stackIndex;
            stackLength = stackIndex + 1;
        }
        else {
            this.commonElementCount = 0;
            stackLength = stackTrace.length;
        }
        final StackTracePackageElement[] packageArray = new StackTracePackageElement[stackLength];
        Class<?> clazz = stack.isEmpty() ? null : stack.peek();
        ClassLoader lastLoader = null;
        for (int i = stackLength - 1; i >= 0; --i) {
            final String className = stackTrace[i].getClassName();
            if (clazz != null && className.equals(clazz.getName())) {
                final CacheEntry entry = this.resolvePackageElement(clazz, true);
                packageArray[i] = entry.element;
                lastLoader = entry.loader;
                stack.pop();
                clazz = (stack.isEmpty() ? null : stack.peek());
            }
            else if (map.containsKey(className)) {
                final CacheEntry entry = map.get(className);
                packageArray[i] = entry.element;
                if (entry.loader != null) {
                    lastLoader = entry.loader;
                }
            }
            else {
                final CacheEntry entry = this.resolvePackageElement(this.loadClass(lastLoader, className), false);
                packageArray[i] = entry.element;
                map.put(className, entry);
                if (entry.loader != null) {
                    lastLoader = entry.loader;
                }
            }
        }
        return packageArray;
    }
    
    private CacheEntry resolvePackageElement(final Class<?> callerClass, final boolean exact) {
        String location = "?";
        String version = "?";
        ClassLoader lastLoader = null;
        if (callerClass != null) {
            try {
                final CodeSource source = callerClass.getProtectionDomain().getCodeSource();
                if (source != null) {
                    final URL locationURL = source.getLocation();
                    if (locationURL != null) {
                        final String str = locationURL.toString().replace('\\', '/');
                        int index = str.lastIndexOf("/");
                        if (index >= 0 && index == str.length() - 1) {
                            index = str.lastIndexOf("/", index - 1);
                            location = str.substring(index + 1);
                        }
                        else {
                            location = str.substring(index + 1);
                        }
                    }
                }
            }
            catch (Exception ex) {}
            final Package pkg = callerClass.getPackage();
            if (pkg != null) {
                final String ver = pkg.getImplementationVersion();
                if (ver != null) {
                    version = ver;
                }
            }
            lastLoader = callerClass.getClassLoader();
        }
        return new CacheEntry(new StackTracePackageElement(location, version, exact), lastLoader);
    }
    
    private Class<?> loadClass(final ClassLoader lastLoader, final String className) {
        if (lastLoader != null) {
            try {
                final Class<?> clazz = lastLoader.loadClass(className);
                if (clazz != null) {
                    return clazz;
                }
            }
            catch (Exception ex) {}
        }
        Class<?> clazz;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException e) {
            try {
                clazz = Class.forName(className);
            }
            catch (ClassNotFoundException e2) {
                try {
                    clazz = this.getClass().getClassLoader().loadClass(className);
                }
                catch (ClassNotFoundException e3) {
                    return null;
                }
            }
        }
        return clazz;
    }
    
    public ThrowableProxy[] getSuppressed() {
        if (ThrowableProxy.GET_SUPPRESSED != null) {
            try {
                return (ThrowableProxy[])ThrowableProxy.GET_SUPPRESSED.invoke(this.throwable, new Object[0]);
            }
            catch (Exception ignore) {
                return null;
            }
        }
        return null;
    }
    
    private void setSuppressed(final Throwable throwable) {
        if (ThrowableProxy.GET_SUPPRESSED != null && ThrowableProxy.ADD_SUPPRESSED != null) {
            try {
                final Throwable[] arr$;
                final Throwable[] array = arr$ = (Throwable[])ThrowableProxy.GET_SUPPRESSED.invoke(throwable, new Object[0]);
                for (final Throwable t : arr$) {
                    ThrowableProxy.ADD_SUPPRESSED.invoke(this, new ThrowableProxy(t));
                }
            }
            catch (Exception ex) {}
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        if (ReflectiveCallerClassUtility.isSupported()) {
            SECURITY_MANAGER = null;
        }
        else {
            PrivateSecurityManager securityManager;
            try {
                securityManager = new PrivateSecurityManager();
                if (securityManager.getClasses() == null) {
                    securityManager = null;
                    ThrowableProxy.LOGGER.error("Unable to obtain call stack from security manager.");
                }
            }
            catch (Exception e) {
                securityManager = null;
                ThrowableProxy.LOGGER.debug("Unable to install security manager.", e);
            }
            SECURITY_MANAGER = securityManager;
        }
        Method getSuppressed = null;
        Method addSuppressed = null;
        final Method[] arr$;
        final Method[] methods = arr$ = Throwable.class.getMethods();
        for (final Method method : arr$) {
            if (method.getName().equals("getSuppressed")) {
                getSuppressed = method;
            }
            else if (method.getName().equals("addSuppressed")) {
                addSuppressed = method;
            }
        }
        GET_SUPPRESSED = getSuppressed;
        ADD_SUPPRESSED = addSuppressed;
    }
    
    class CacheEntry
    {
        private final StackTracePackageElement element;
        private final ClassLoader loader;
        
        public CacheEntry(final StackTracePackageElement element, final ClassLoader loader) {
            this.element = element;
            this.loader = loader;
        }
    }
    
    private static class PrivateSecurityManager extends SecurityManager
    {
        public Class<?>[] getClasses() {
            return this.getClassContext();
        }
    }
}
