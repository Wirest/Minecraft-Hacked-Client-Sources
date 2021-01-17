// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import org.apache.logging.log4j.core.Appender;
import java.util.Map;
import org.apache.logging.log4j.core.config.LoggerConfig;
import java.util.Set;
import javax.management.QueryExp;
import javax.management.ObjectName;
import javax.management.NotCompliantMBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.InstanceAlreadyExistsException;
import java.util.Iterator;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.Executor;
import java.beans.PropertyChangeListener;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.Executors;
import javax.management.JMException;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.selector.ContextSelector;

public final class Server
{
    private static final String PROPERTY_DISABLE_JMX = "log4j2.disable.jmx";
    
    private Server() {
    }
    
    public static String escape(final String name) {
        final StringBuilder sb = new StringBuilder(name.length() * 2);
        boolean needsQuotes = false;
        for (int i = 0; i < name.length(); ++i) {
            final char c = name.charAt(i);
            switch (c) {
                case '*':
                case ',':
                case ':':
                case '=':
                case '?':
                case '\\': {
                    sb.append('\\');
                    needsQuotes = true;
                    break;
                }
            }
            sb.append(c);
        }
        if (needsQuotes) {
            sb.insert(0, '\"');
            sb.append('\"');
        }
        return sb.toString();
    }
    
    public static void registerMBeans(final ContextSelector selector) throws JMException {
        if (Boolean.getBoolean("log4j2.disable.jmx")) {
            StatusLogger.getLogger().debug("JMX disabled for log4j2. Not registering MBeans.");
            return;
        }
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        registerMBeans(selector, mbs);
    }
    
    public static void registerMBeans(final ContextSelector selector, final MBeanServer mbs) throws JMException {
        if (Boolean.getBoolean("log4j2.disable.jmx")) {
            StatusLogger.getLogger().debug("JMX disabled for log4j2. Not registering MBeans.");
            return;
        }
        final Executor executor = Executors.newFixedThreadPool(1);
        registerStatusLogger(mbs, executor);
        registerContextSelector(selector, mbs, executor);
        final List<LoggerContext> contexts = selector.getLoggerContexts();
        registerContexts(contexts, mbs, executor);
        for (final LoggerContext context : contexts) {
            context.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    if (!"config".equals(evt.getPropertyName())) {
                        return;
                    }
                    unregisterLoggerConfigs(context, mbs);
                    unregisterAppenders(context, mbs);
                    try {
                        registerLoggerConfigs(context, mbs, executor);
                        registerAppenders(context, mbs, executor);
                    }
                    catch (Exception ex) {
                        StatusLogger.getLogger().error("Could not register mbeans", ex);
                    }
                }
            });
        }
    }
    
    private static void registerStatusLogger(final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        final StatusLoggerAdmin mbean = new StatusLoggerAdmin(executor);
        mbs.registerMBean(mbean, mbean.getObjectName());
    }
    
    private static void registerContextSelector(final ContextSelector selector, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        final ContextSelectorAdmin mbean = new ContextSelectorAdmin(selector);
        mbs.registerMBean(mbean, mbean.getObjectName());
    }
    
    private static void registerContexts(final List<LoggerContext> contexts, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        for (final LoggerContext ctx : contexts) {
            final LoggerContextAdmin mbean = new LoggerContextAdmin(ctx, executor);
            mbs.registerMBean(mbean, mbean.getObjectName());
        }
    }
    
    private static void unregisterLoggerConfigs(final LoggerContext context, final MBeanServer mbs) {
        final String pattern = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s";
        final String search = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s", context.getName(), "*");
        unregisterAllMatching(search, mbs);
    }
    
    private static void unregisterAppenders(final LoggerContext context, final MBeanServer mbs) {
        final String pattern = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s";
        final String search = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s", context.getName(), "*");
        unregisterAllMatching(search, mbs);
    }
    
    private static void unregisterAllMatching(final String search, final MBeanServer mbs) {
        try {
            final ObjectName pattern = new ObjectName(search);
            final Set<ObjectName> found = mbs.queryNames(pattern, null);
            for (final ObjectName objectName : found) {
                mbs.unregisterMBean(objectName);
            }
        }
        catch (Exception ex) {
            StatusLogger.getLogger().error("Could not unregister " + search, ex);
        }
    }
    
    private static void registerLoggerConfigs(final LoggerContext ctx, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        final Map<String, LoggerConfig> map = ctx.getConfiguration().getLoggers();
        for (final String name : map.keySet()) {
            final LoggerConfig cfg = map.get(name);
            final LoggerConfigAdmin mbean = new LoggerConfigAdmin(ctx.getName(), cfg);
            mbs.registerMBean(mbean, mbean.getObjectName());
        }
    }
    
    private static void registerAppenders(final LoggerContext ctx, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        final Map<String, Appender> map = ctx.getConfiguration().getAppenders();
        for (final String name : map.keySet()) {
            final Appender appender = map.get(name);
            final AppenderAdmin mbean = new AppenderAdmin(ctx.getName(), appender);
            mbs.registerMBean(mbean, mbean.getObjectName());
        }
    }
}
