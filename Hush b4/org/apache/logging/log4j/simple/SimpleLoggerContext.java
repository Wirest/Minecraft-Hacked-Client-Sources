// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.simple;

import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.message.MessageFactory;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ConcurrentMap;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.Properties;
import org.apache.logging.log4j.spi.LoggerContext;

public class SimpleLoggerContext implements LoggerContext
{
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    protected static final String SYSTEM_PREFIX = "org.apache.logging.log4j.simplelog.";
    private final Properties simpleLogProps;
    private final PropertiesUtil props;
    private final boolean showLogName;
    private final boolean showShortName;
    private final boolean showDateTime;
    private final boolean showContextMap;
    private final String dateTimeFormat;
    private final Level defaultLevel;
    private final PrintStream stream;
    private final ConcurrentMap<String, Logger> loggers;
    
    public SimpleLoggerContext() {
        this.simpleLogProps = new Properties();
        this.loggers = new ConcurrentHashMap<String, Logger>();
        this.props = new PropertiesUtil("log4j2.simplelog.properties");
        this.showContextMap = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showContextMap", false);
        this.showLogName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showlogname", false);
        this.showShortName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showShortLogname", true);
        this.showDateTime = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showdatetime", false);
        final String lvl = this.props.getStringProperty("org.apache.logging.log4j.simplelog.level");
        this.defaultLevel = Level.toLevel(lvl, Level.ERROR);
        this.dateTimeFormat = (this.showDateTime ? this.props.getStringProperty("org.apache.logging.log4j.simplelog.dateTimeFormat", "yyyy/MM/dd HH:mm:ss:SSS zzz") : null);
        final String fileName = this.props.getStringProperty("org.apache.logging.log4j.simplelog.logFile", "system.err");
        PrintStream ps;
        if ("system.err".equalsIgnoreCase(fileName)) {
            ps = System.err;
        }
        else if ("system.out".equalsIgnoreCase(fileName)) {
            ps = System.out;
        }
        else {
            try {
                final FileOutputStream os = new FileOutputStream(fileName);
                ps = new PrintStream(os);
            }
            catch (FileNotFoundException fnfe) {
                ps = System.err;
            }
        }
        this.stream = ps;
    }
    
    @Override
    public Logger getLogger(final String name) {
        return this.getLogger(name, null);
    }
    
    @Override
    public Logger getLogger(final String name, final MessageFactory messageFactory) {
        if (this.loggers.containsKey(name)) {
            final Logger logger = this.loggers.get(name);
            AbstractLogger.checkMessageFactory(logger, messageFactory);
            return logger;
        }
        this.loggers.putIfAbsent(name, new SimpleLogger(name, this.defaultLevel, this.showLogName, this.showShortName, this.showDateTime, this.showContextMap, this.dateTimeFormat, messageFactory, this.props, this.stream));
        return this.loggers.get(name);
    }
    
    @Override
    public boolean hasLogger(final String name) {
        return false;
    }
    
    @Override
    public Object getExternalContext() {
        return null;
    }
}
