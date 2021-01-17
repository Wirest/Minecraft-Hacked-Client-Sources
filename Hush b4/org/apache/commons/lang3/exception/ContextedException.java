// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.exception;

import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public class ContextedException extends Exception implements ExceptionContext
{
    private static final long serialVersionUID = 20110706L;
    private final ExceptionContext exceptionContext;
    
    public ContextedException() {
        this.exceptionContext = new DefaultExceptionContext();
    }
    
    public ContextedException(final String message) {
        super(message);
        this.exceptionContext = new DefaultExceptionContext();
    }
    
    public ContextedException(final Throwable cause) {
        super(cause);
        this.exceptionContext = new DefaultExceptionContext();
    }
    
    public ContextedException(final String message, final Throwable cause) {
        super(message, cause);
        this.exceptionContext = new DefaultExceptionContext();
    }
    
    public ContextedException(final String message, final Throwable cause, ExceptionContext context) {
        super(message, cause);
        if (context == null) {
            context = new DefaultExceptionContext();
        }
        this.exceptionContext = context;
    }
    
    @Override
    public ContextedException addContextValue(final String label, final Object value) {
        this.exceptionContext.addContextValue(label, value);
        return this;
    }
    
    @Override
    public ContextedException setContextValue(final String label, final Object value) {
        this.exceptionContext.setContextValue(label, value);
        return this;
    }
    
    @Override
    public List<Object> getContextValues(final String label) {
        return this.exceptionContext.getContextValues(label);
    }
    
    @Override
    public Object getFirstContextValue(final String label) {
        return this.exceptionContext.getFirstContextValue(label);
    }
    
    @Override
    public List<Pair<String, Object>> getContextEntries() {
        return this.exceptionContext.getContextEntries();
    }
    
    @Override
    public Set<String> getContextLabels() {
        return this.exceptionContext.getContextLabels();
    }
    
    @Override
    public String getMessage() {
        return this.getFormattedExceptionMessage(super.getMessage());
    }
    
    public String getRawMessage() {
        return super.getMessage();
    }
    
    @Override
    public String getFormattedExceptionMessage(final String baseMessage) {
        return this.exceptionContext.getFormattedExceptionMessage(baseMessage);
    }
}
