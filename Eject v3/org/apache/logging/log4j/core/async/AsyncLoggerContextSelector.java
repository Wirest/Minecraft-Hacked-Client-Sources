package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.selector.ContextSelector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AsyncLoggerContextSelector
        implements ContextSelector {
    private static final AsyncLoggerContext CONTEXT = new AsyncLoggerContext("AsyncLoggerContext");

    public LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean) {
        return CONTEXT;
    }

    public List<LoggerContext> getLoggerContexts() {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(CONTEXT);
        return Collections.unmodifiableList(localArrayList);
    }

    public LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean, URI paramURI) {
        return CONTEXT;
    }

    public void removeContext(LoggerContext paramLoggerContext) {
    }
}




