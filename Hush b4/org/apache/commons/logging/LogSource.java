// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging;

import org.apache.commons.logging.impl.NoOpLog;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

public class LogSource
{
    protected static Hashtable logs;
    protected static boolean log4jIsAvailable;
    protected static boolean jdk14IsAvailable;
    protected static Constructor logImplctor;
    
    private LogSource() {
    }
    
    public static void setLogImplementation(final String classname) throws LinkageError, NoSuchMethodException, SecurityException, ClassNotFoundException {
        try {
            final Class logclass = Class.forName(classname);
            final Class[] argtypes = { "".getClass() };
            LogSource.logImplctor = logclass.getConstructor((Class[])argtypes);
        }
        catch (Throwable t) {
            LogSource.logImplctor = null;
        }
    }
    
    public static void setLogImplementation(final Class logclass) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException {
        final Class[] argtypes = { "".getClass() };
        LogSource.logImplctor = logclass.getConstructor((Class[])argtypes);
    }
    
    public static Log getInstance(final String name) {
        Log log = LogSource.logs.get(name);
        if (null == log) {
            log = makeNewLogInstance(name);
            LogSource.logs.put(name, log);
        }
        return log;
    }
    
    public static Log getInstance(final Class clazz) {
        return getInstance(clazz.getName());
    }
    
    public static Log makeNewLogInstance(final String name) {
        Log log;
        try {
            final Object[] args = { name };
            log = LogSource.logImplctor.newInstance(args);
        }
        catch (Throwable t) {
            log = null;
        }
        if (null == log) {
            log = new NoOpLog(name);
        }
        return log;
    }
    
    public static String[] getLogNames() {
        return (String[])LogSource.logs.keySet().toArray(new String[LogSource.logs.size()]);
    }
    
    static {
        LogSource.logs = new Hashtable();
        LogSource.log4jIsAvailable = false;
        LogSource.jdk14IsAvailable = false;
        LogSource.logImplctor = null;
        try {
            LogSource.log4jIsAvailable = (null != Class.forName("org.apache.log4j.Logger"));
        }
        catch (Throwable t) {
            LogSource.log4jIsAvailable = false;
        }
        try {
            LogSource.jdk14IsAvailable = (null != Class.forName("java.util.logging.Logger") && null != Class.forName("org.apache.commons.logging.impl.Jdk14Logger"));
        }
        catch (Throwable t) {
            LogSource.jdk14IsAvailable = false;
        }
        String name = null;
        try {
            name = System.getProperty("org.apache.commons.logging.log");
            if (name == null) {
                name = System.getProperty("org.apache.commons.logging.Log");
            }
        }
        catch (Throwable t3) {}
        if (name != null) {
            try {
                setLogImplementation(name);
            }
            catch (Throwable t2) {
                try {
                    setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
                }
                catch (Throwable t4) {}
            }
        }
        else {
            try {
                if (LogSource.log4jIsAvailable) {
                    setLogImplementation("org.apache.commons.logging.impl.Log4JLogger");
                }
                else if (LogSource.jdk14IsAvailable) {
                    setLogImplementation("org.apache.commons.logging.impl.Jdk14Logger");
                }
                else {
                    setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
                }
            }
            catch (Throwable t2) {
                try {
                    setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
                }
                catch (Throwable t5) {}
            }
        }
    }
}
