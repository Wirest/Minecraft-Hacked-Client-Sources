// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.Appender;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class DefaultConfiguration extends BaseConfiguration
{
    public static final String DEFAULT_NAME = "Default";
    public static final String DEFAULT_LEVEL = "org.apache.logging.log4j.level";
    
    public DefaultConfiguration() {
        this.setName("Default");
        final Layout<? extends Serializable> layout = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", null, null, null, null);
        final Appender appender = ConsoleAppender.createAppender(layout, null, "SYSTEM_OUT", "Console", "false", "true");
        appender.start();
        this.addAppender(appender);
        final LoggerConfig root = this.getRootLogger();
        root.addAppender(appender, null, null);
        final String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
        final Level level = (levelName != null && Level.valueOf(levelName) != null) ? Level.valueOf(levelName) : Level.ERROR;
        root.setLevel(level);
    }
    
    @Override
    protected void doConfigure() {
    }
}
