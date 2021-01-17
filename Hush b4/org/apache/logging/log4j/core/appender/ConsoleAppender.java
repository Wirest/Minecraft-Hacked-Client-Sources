// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.lang.reflect.Constructor;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.io.UnsupportedEncodingException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.io.OutputStream;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Console", category = "Core", elementType = "appender", printObject = true)
public final class ConsoleAppender extends AbstractOutputStreamAppender
{
    private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
    private static ConsoleManagerFactory factory;
    
    private ConsoleAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final OutputStreamManager manager, final boolean ignoreExceptions) {
        super(name, layout, filter, ignoreExceptions, true, manager);
    }
    
    @PluginFactory
    public static ConsoleAppender createAppender(@PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") final Filter filter, @PluginAttribute("target") final String t, @PluginAttribute("name") final String name, @PluginAttribute("follow") final String follow, @PluginAttribute("ignoreExceptions") final String ignore) {
        if (name == null) {
            ConsoleAppender.LOGGER.error("No name provided for ConsoleAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }
        final boolean isFollow = Boolean.parseBoolean(follow);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final Target target = (t == null) ? Target.SYSTEM_OUT : Target.valueOf(t);
        return new ConsoleAppender(name, layout, filter, getManager(isFollow, target, layout), ignoreExceptions);
    }
    
    private static OutputStreamManager getManager(final boolean follow, final Target target, final Layout<? extends Serializable> layout) {
        final String type = target.name();
        final OutputStream os = getOutputStream(follow, target);
        return OutputStreamManager.getManager(target.name() + "." + follow, new FactoryData(os, type, layout), ConsoleAppender.factory);
    }
    
    private static OutputStream getOutputStream(final boolean follow, final Target target) {
        final String enc = Charset.defaultCharset().name();
        PrintStream printStream = null;
        try {
            PrintStream printStream2;
            if (target == Target.SYSTEM_OUT) {
                if (follow) {
                    final SystemOutStream out;
                    printStream2 = new PrintStream(out, true, enc);
                    out = new SystemOutStream();
                }
                else {
                    printStream2 = System.out;
                }
            }
            else if (follow) {
                final SystemErrStream out2;
                printStream2 = new PrintStream(out2, true, enc);
                out2 = new SystemErrStream();
            }
            else {
                printStream2 = System.err;
            }
            printStream = printStream2;
        }
        catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Unsupported default encoding " + enc, ex);
        }
        final PropertiesUtil propsUtil = PropertiesUtil.getProperties();
        if (!propsUtil.getStringProperty("os.name").startsWith("Windows") || propsUtil.getBooleanProperty("log4j.skipJansi")) {
            return printStream;
        }
        try {
            final ClassLoader loader = Loader.getClassLoader();
            final Class<?> clazz = loader.loadClass("org.fusesource.jansi.WindowsAnsiOutputStream");
            final Constructor<?> constructor = clazz.getConstructor(OutputStream.class);
            return (OutputStream)constructor.newInstance(printStream);
        }
        catch (ClassNotFoundException cnfe) {
            ConsoleAppender.LOGGER.debug("Jansi is not installed, cannot find {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
        }
        catch (NoSuchMethodException nsme) {
            ConsoleAppender.LOGGER.warn("{} is missing the proper constructor", "org.fusesource.jansi.WindowsAnsiOutputStream");
        }
        catch (Exception ex2) {
            ConsoleAppender.LOGGER.warn("Unable to instantiate {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
        }
        return printStream;
    }
    
    static {
        ConsoleAppender.factory = new ConsoleManagerFactory();
    }
    
    public enum Target
    {
        SYSTEM_OUT, 
        SYSTEM_ERR;
    }
    
    private static class SystemErrStream extends OutputStream
    {
        public SystemErrStream() {
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public void flush() {
            System.err.flush();
        }
        
        @Override
        public void write(final byte[] b) throws IOException {
            System.err.write(b);
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            System.err.write(b, off, len);
        }
        
        @Override
        public void write(final int b) {
            System.err.write(b);
        }
    }
    
    private static class SystemOutStream extends OutputStream
    {
        public SystemOutStream() {
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public void flush() {
            System.out.flush();
        }
        
        @Override
        public void write(final byte[] b) throws IOException {
            System.out.write(b);
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            System.out.write(b, off, len);
        }
        
        @Override
        public void write(final int b) throws IOException {
            System.out.write(b);
        }
    }
    
    private static class FactoryData
    {
        private final OutputStream os;
        private final String type;
        private final Layout<? extends Serializable> layout;
        
        public FactoryData(final OutputStream os, final String type, final Layout<? extends Serializable> layout) {
            this.os = os;
            this.type = type;
            this.layout = layout;
        }
    }
    
    private static class ConsoleManagerFactory implements ManagerFactory<OutputStreamManager, FactoryData>
    {
        @Override
        public OutputStreamManager createManager(final String name, final FactoryData data) {
            return new OutputStreamManager(data.os, data.type, data.layout);
        }
    }
}
