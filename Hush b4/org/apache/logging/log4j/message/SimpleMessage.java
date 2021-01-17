// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

public class SimpleMessage implements Message
{
    private static final long serialVersionUID = -8398002534962715992L;
    private final String message;
    
    public SimpleMessage() {
        this(null);
    }
    
    public SimpleMessage(final String message) {
        this.message = message;
    }
    
    @Override
    public String getFormattedMessage() {
        return this.message;
    }
    
    @Override
    public String getFormat() {
        return this.message;
    }
    
    @Override
    public Object[] getParameters() {
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SimpleMessage that = (SimpleMessage)o;
        if (this.message != null) {
            if (!this.message.equals(that.message)) {
                return false;
            }
        }
        else if (that.message != null) {
            return false;
        }
        return true;
        b = false;
        return b;
    }
    
    @Override
    public int hashCode() {
        return (this.message != null) ? this.message.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "SimpleMessage[message=" + this.message + "]";
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
}
