// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.config;

import org.apache.http.util.Args;

public class MessageConstraints implements Cloneable
{
    public static final MessageConstraints DEFAULT;
    private final int maxLineLength;
    private final int maxHeaderCount;
    
    MessageConstraints(final int maxLineLength, final int maxHeaderCount) {
        this.maxLineLength = maxLineLength;
        this.maxHeaderCount = maxHeaderCount;
    }
    
    public int getMaxLineLength() {
        return this.maxLineLength;
    }
    
    public int getMaxHeaderCount() {
        return this.maxHeaderCount;
    }
    
    @Override
    protected MessageConstraints clone() throws CloneNotSupportedException {
        return (MessageConstraints)super.clone();
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[maxLineLength=").append(this.maxLineLength).append(", maxHeaderCount=").append(this.maxHeaderCount).append("]");
        return builder.toString();
    }
    
    public static MessageConstraints lineLen(final int max) {
        return new MessageConstraints(Args.notNegative(max, "Max line length"), -1);
    }
    
    public static Builder custom() {
        return new Builder();
    }
    
    public static Builder copy(final MessageConstraints config) {
        Args.notNull(config, "Message constraints");
        return new Builder().setMaxHeaderCount(config.getMaxHeaderCount()).setMaxLineLength(config.getMaxLineLength());
    }
    
    static {
        DEFAULT = new Builder().build();
    }
    
    public static class Builder
    {
        private int maxLineLength;
        private int maxHeaderCount;
        
        Builder() {
            this.maxLineLength = -1;
            this.maxHeaderCount = -1;
        }
        
        public Builder setMaxLineLength(final int maxLineLength) {
            this.maxLineLength = maxLineLength;
            return this;
        }
        
        public Builder setMaxHeaderCount(final int maxHeaderCount) {
            this.maxHeaderCount = maxHeaderCount;
            return this;
        }
        
        public MessageConstraints build() {
            return new MessageConstraints(this.maxLineLength, this.maxHeaderCount);
        }
    }
}
