// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.logging.impl;

import java.text.SimpleDateFormat;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.logging.LogConfigurationException;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.text.DateFormat;
import java.util.Properties;
import java.io.Serializable;
import org.apache.commons.logging.Log;

public class SimpleLog implements Log, Serializable
{
    private static final long serialVersionUID = 136942970684951178L;
    protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
    protected static final Properties simpleLogProps;
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    protected static volatile boolean showLogName;
    protected static volatile boolean showShortName;
    protected static volatile boolean showDateTime;
    protected static volatile String dateTimeFormat;
    protected static DateFormat dateFormatter;
    public static final int LOG_LEVEL_TRACE = 1;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_WARN = 4;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_FATAL = 6;
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_OFF = 7;
    protected volatile String logName;
    protected volatile int currentLogLevel;
    private volatile String shortLogName;
    
    private static String getStringProperty(final String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException ex) {}
        return (prop == null) ? SimpleLog.simpleLogProps.getProperty(name) : prop;
    }
    
    private static String getStringProperty(final String name, final String dephault) {
        final String prop = getStringProperty(name);
        return (prop == null) ? dephault : prop;
    }
    
    private static boolean getBooleanProperty(final String name, final boolean dephault) {
        final String prop = getStringProperty(name);
        return (prop == null) ? dephault : "true".equalsIgnoreCase(prop);
    }
    
    public SimpleLog(String name) {
        this.logName = null;
        this.shortLogName = null;
        this.logName = name;
        this.setLevel(3);
        String lvl = getStringProperty("org.apache.commons.logging.simplelog.log." + this.logName);
        for (int i = String.valueOf(name).lastIndexOf("."); null == lvl && i > -1; lvl = getStringProperty("org.apache.commons.logging.simplelog.log." + name), i = String.valueOf(name).lastIndexOf(".")) {
            name = name.substring(0, i);
        }
        if (null == lvl) {
            lvl = getStringProperty("org.apache.commons.logging.simplelog.defaultlog");
        }
        if ("all".equalsIgnoreCase(lvl)) {
            this.setLevel(0);
        }
        else if ("trace".equalsIgnoreCase(lvl)) {
            this.setLevel(1);
        }
        else if ("debug".equalsIgnoreCase(lvl)) {
            this.setLevel(2);
        }
        else if ("info".equalsIgnoreCase(lvl)) {
            this.setLevel(3);
        }
        else if ("warn".equalsIgnoreCase(lvl)) {
            this.setLevel(4);
        }
        else if ("error".equalsIgnoreCase(lvl)) {
            this.setLevel(5);
        }
        else if ("fatal".equalsIgnoreCase(lvl)) {
            this.setLevel(6);
        }
        else if ("off".equalsIgnoreCase(lvl)) {
            this.setLevel(7);
        }
    }
    
    public void setLevel(final int currentLogLevel) {
        this.currentLogLevel = currentLogLevel;
    }
    
    public int getLevel() {
        return this.currentLogLevel;
    }
    
    protected void log(final int type, final Object message, final Throwable t) {
        final StringBuffer buf = new StringBuffer();
        if (SimpleLog.showDateTime) {
            final Date now = new Date();
            final String dateText;
            synchronized (SimpleLog.dateFormatter) {
                dateText = SimpleLog.dateFormatter.format(now);
            }
            buf.append(dateText);
            buf.append(" ");
        }
        switch (type) {
            case 1: {
                buf.append("[TRACE] ");
                break;
            }
            case 2: {
                buf.append("[DEBUG] ");
                break;
            }
            case 3: {
                buf.append("[INFO] ");
                break;
            }
            case 4: {
                buf.append("[WARN] ");
                break;
            }
            case 5: {
                buf.append("[ERROR] ");
                break;
            }
            case 6: {
                buf.append("[FATAL] ");
                break;
            }
        }
        if (SimpleLog.showShortName) {
            if (this.shortLogName == null) {
                final String slName = this.logName.substring(this.logName.lastIndexOf(".") + 1);
                this.shortLogName = slName.substring(slName.lastIndexOf("/") + 1);
            }
            buf.append(String.valueOf(this.shortLogName)).append(" - ");
        }
        else if (SimpleLog.showLogName) {
            buf.append(String.valueOf(this.logName)).append(" - ");
        }
        buf.append(String.valueOf(message));
        if (t != null) {
            buf.append(" <");
            buf.append(t.toString());
            buf.append(">");
            final StringWriter sw = new StringWriter(1024);
            final PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            buf.append(sw.toString());
        }
        this.write(buf);
    }
    
    protected void write(final StringBuffer buffer) {
        System.err.println(buffer.toString());
    }
    
    protected boolean isLevelEnabled(final int logLevel) {
        return logLevel >= this.currentLogLevel;
    }
    
    public final void debug(final Object message) {
        if (this.isLevelEnabled(2)) {
            this.log(2, message, null);
        }
    }
    
    public final void debug(final Object message, final Throwable t) {
        if (this.isLevelEnabled(2)) {
            this.log(2, message, t);
        }
    }
    
    public final void trace(final Object message) {
        if (this.isLevelEnabled(1)) {
            this.log(1, message, null);
        }
    }
    
    public final void trace(final Object message, final Throwable t) {
        if (this.isLevelEnabled(1)) {
            this.log(1, message, t);
        }
    }
    
    public final void info(final Object message) {
        if (this.isLevelEnabled(3)) {
            this.log(3, message, null);
        }
    }
    
    public final void info(final Object message, final Throwable t) {
        if (this.isLevelEnabled(3)) {
            this.log(3, message, t);
        }
    }
    
    public final void warn(final Object message) {
        if (this.isLevelEnabled(4)) {
            this.log(4, message, null);
        }
    }
    
    public final void warn(final Object message, final Throwable t) {
        if (this.isLevelEnabled(4)) {
            this.log(4, message, t);
        }
    }
    
    public final void error(final Object message) {
        if (this.isLevelEnabled(5)) {
            this.log(5, message, null);
        }
    }
    
    public final void error(final Object message, final Throwable t) {
        if (this.isLevelEnabled(5)) {
            this.log(5, message, t);
        }
    }
    
    public final void fatal(final Object message) {
        if (this.isLevelEnabled(6)) {
            this.log(6, message, null);
        }
    }
    
    public final void fatal(final Object message, final Throwable t) {
        if (this.isLevelEnabled(6)) {
            this.log(6, message, t);
        }
    }
    
    public final boolean isDebugEnabled() {
        return this.isLevelEnabled(2);
    }
    
    public final boolean isErrorEnabled() {
        return this.isLevelEnabled(5);
    }
    
    public final boolean isFatalEnabled() {
        return this.isLevelEnabled(6);
    }
    
    public final boolean isInfoEnabled() {
        return this.isLevelEnabled(3);
    }
    
    public final boolean isTraceEnabled() {
        return this.isLevelEnabled(1);
    }
    
    public final boolean isWarnEnabled() {
        return this.isLevelEnabled(4);
    }
    
    private static ClassLoader getContextClassLoader() {
        ClassLoader classLoader = null;
        try {
            final Method method = Thread.class.getMethod("getContextClassLoader", (Class[])null);
            try {
                classLoader = (ClassLoader)method.invoke(Thread.currentThread(), (Object[])null);
            }
            catch (IllegalAccessException e2) {}
            catch (InvocationTargetException e) {
                if (!(e.getTargetException() instanceof SecurityException)) {
                    throw new LogConfigurationException("Unexpected InvocationTargetException", e.getTargetException());
                }
            }
        }
        catch (NoSuchMethodException ex) {}
        if (classLoader == null) {
            classLoader = SimpleLog.class.getClassLoader();
        }
        return classLoader;
    }
    
    private static InputStream getResourceAsStream(final String name) {
        return AccessController.doPrivileged((PrivilegedAction<InputStream>)new PrivilegedAction() {
            public Object run() {
                final ClassLoader threadCL = getContextClassLoader();
                if (threadCL != null) {
                    return threadCL.getResourceAsStream(name);
                }
                return ClassLoader.getSystemResourceAsStream(name);
            }
        });
    }
    
    static {
        simpleLogProps = new Properties();
        SimpleLog.showLogName = false;
        SimpleLog.showShortName = true;
        SimpleLog.showDateTime = false;
        SimpleLog.dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
        SimpleLog.dateFormatter = null;
        final InputStream in = getResourceAsStream("simplelog.properties");
        if (null != in) {
            try {
                SimpleLog.simpleLogProps.load(in);
                in.close();
            }
            catch (IOException ex) {}
        }
        SimpleLog.showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", SimpleLog.showLogName);
        SimpleLog.showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", SimpleLog.showShortName);
        SimpleLog.showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", SimpleLog.showDateTime);
        if (SimpleLog.showDateTime) {
            SimpleLog.dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", SimpleLog.dateTimeFormat);
            try {
                SimpleLog.dateFormatter = new SimpleDateFormat(SimpleLog.dateTimeFormat);
            }
            catch (IllegalArgumentException e) {
                SimpleLog.dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
                SimpleLog.dateFormatter = new SimpleDateFormat(SimpleLog.dateTimeFormat);
            }
        }
    }
}
