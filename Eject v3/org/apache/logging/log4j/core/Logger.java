package org.apache.logging.log4j.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.AbstractLogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Logger
        extends AbstractLogger {
    private final LoggerContext context;
    protected volatile PrivateConfig config;

    protected Logger(LoggerContext paramLoggerContext, String paramString, MessageFactory paramMessageFactory) {
        super(paramString, paramMessageFactory);
        this.context = paramLoggerContext;
        this.config = new PrivateConfig(paramLoggerContext.getConfiguration(), this);
    }

    public Logger getParent() {
        LoggerConfig localLoggerConfig = this.config.loggerConfig.getName().equals(getName()) ? this.config.loggerConfig.getParent() : this.config.loggerConfig;
        if (localLoggerConfig == null) {
            return null;
        }
        if (this.context.hasLogger(localLoggerConfig.getName())) {
            return this.context.getLogger(localLoggerConfig.getName(), getMessageFactory());
        }
        return new Logger(this.context, localLoggerConfig.getName(), getMessageFactory());
    }

    public LoggerContext getContext() {
        return this.context;
    }

    public Level getLevel() {
        return this.config.level;
    }

    public synchronized void setLevel(Level paramLevel) {
        if (paramLevel != null) {
            this.config = new PrivateConfig(this.config, paramLevel);
        }
    }

    public void log(Marker paramMarker, String paramString, Level paramLevel, Message paramMessage, Throwable paramThrowable) {
        if (paramMessage == null) {
            paramMessage = new SimpleMessage("");
        }
        this.config.config.getConfigurationMonitor().checkConfiguration();
        this.config.loggerConfig.log(getName(), paramMarker, paramString, paramLevel, paramMessage, paramThrowable);
    }

    public boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString) {
        return this.config.filter(paramLevel, paramMarker, paramString);
    }

    public boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Throwable paramThrowable) {
        return this.config.filter(paramLevel, paramMarker, paramString, paramThrowable);
    }

    public boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs) {
        return this.config.filter(paramLevel, paramMarker, paramString, paramVarArgs);
    }

    public boolean isEnabled(Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable) {
        return this.config.filter(paramLevel, paramMarker, paramObject, paramThrowable);
    }

    public boolean isEnabled(Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable) {
        return this.config.filter(paramLevel, paramMarker, paramMessage, paramThrowable);
    }

    public void addAppender(Appender paramAppender) {
        this.config.config.addLoggerAppender(this, paramAppender);
    }

    public void removeAppender(Appender paramAppender) {
        this.config.loggerConfig.removeAppender(paramAppender.getName());
    }

    public Map<String, Appender> getAppenders() {
        return this.config.loggerConfig.getAppenders();
    }

    public Iterator<Filter> getFilters() {
        Filter localFilter = this.config.loggerConfig.getFilter();
        if (localFilter == null) {
            return new ArrayList().iterator();
        }
        if ((localFilter instanceof CompositeFilter)) {
            return ((CompositeFilter) localFilter).iterator();
        }
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(localFilter);
        return localArrayList.iterator();
    }

    public int filterCount() {
        Filter localFilter = this.config.loggerConfig.getFilter();
        if (localFilter == null) {
            return 0;
        }
        if ((localFilter instanceof CompositeFilter)) {
            return ((CompositeFilter) localFilter).size();
        }
        return 1;
    }

    public void addFilter(Filter paramFilter) {
        this.config.config.addLoggerFilter(this, paramFilter);
    }

    public boolean isAdditive() {
        return this.config.loggerConfig.isAdditive();
    }

    public void setAdditive(boolean paramBoolean) {
        this.config.config.setLoggerAdditive(this, paramBoolean);
    }

    void updateConfiguration(Configuration paramConfiguration) {
        this.config = new PrivateConfig(paramConfiguration, this);
    }

    public String toString() {
        String str1 = "" + getName() + ":" + getLevel();
        if (this.context == null) {
            return str1;
        }
        String str2 = this.context.getName();
        return str1 + " in " + str2;
    }

    protected class PrivateConfig {
        public final LoggerConfig loggerConfig;
        public final Configuration config;
        private final Level level;
        private final int intLevel;
        private final Logger logger;

        public PrivateConfig(Configuration paramConfiguration, Logger paramLogger) {
            this.config = paramConfiguration;
            this.loggerConfig = paramConfiguration.getLoggerConfig(Logger.this.getName());
            this.level = this.loggerConfig.getLevel();
            this.intLevel = this.level.intLevel();
            this.logger = paramLogger;
        }

        public PrivateConfig(PrivateConfig paramPrivateConfig, Level paramLevel) {
            this.config = paramPrivateConfig.config;
            this.loggerConfig = paramPrivateConfig.loggerConfig;
            this.level = paramLevel;
            this.intLevel = this.level.intLevel();
            this.logger = paramPrivateConfig.logger;
        }

        public PrivateConfig(PrivateConfig paramPrivateConfig, LoggerConfig paramLoggerConfig) {
            this.config = paramPrivateConfig.config;
            this.loggerConfig = paramLoggerConfig;
            this.level = paramLoggerConfig.getLevel();
            this.intLevel = this.level.intLevel();
            this.logger = paramPrivateConfig.logger;
        }

        public void logEvent(LogEvent paramLogEvent) {
            this.config.getConfigurationMonitor().checkConfiguration();
            this.loggerConfig.log(paramLogEvent);
        }

        boolean filter(Level paramLevel, Marker paramMarker, String paramString) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter localFilter = this.config.getFilter();
            if (localFilter != null) {
                Filter.Result localResult = localFilter.filter(this.logger, paramLevel, paramMarker, paramString, new Object[0]);
                if (localResult != Filter.Result.NEUTRAL) {
                    return localResult == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= paramLevel.intLevel();
        }

        boolean filter(Level paramLevel, Marker paramMarker, String paramString, Throwable paramThrowable) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter localFilter = this.config.getFilter();
            if (localFilter != null) {
                Filter.Result localResult = localFilter.filter(this.logger, paramLevel, paramMarker, paramString, paramThrowable);
                if (localResult != Filter.Result.NEUTRAL) {
                    return localResult == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= paramLevel.intLevel();
        }

        boolean filter(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter localFilter = this.config.getFilter();
            if (localFilter != null) {
                Filter.Result localResult = localFilter.filter(this.logger, paramLevel, paramMarker, paramString, paramVarArgs);
                if (localResult != Filter.Result.NEUTRAL) {
                    return localResult == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= paramLevel.intLevel();
        }

        boolean filter(Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter localFilter = this.config.getFilter();
            if (localFilter != null) {
                Filter.Result localResult = localFilter.filter(this.logger, paramLevel, paramMarker, paramObject, paramThrowable);
                if (localResult != Filter.Result.NEUTRAL) {
                    return localResult == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= paramLevel.intLevel();
        }

        boolean filter(Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable) {
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter localFilter = this.config.getFilter();
            if (localFilter != null) {
                Filter.Result localResult = localFilter.filter(this.logger, paramLevel, paramMarker, paramMessage, paramThrowable);
                if (localResult != Filter.Result.NEUTRAL) {
                    return localResult == Filter.Result.ACCEPT;
                }
            }
            return this.intLevel >= paramLevel.intLevel();
        }
    }
}




