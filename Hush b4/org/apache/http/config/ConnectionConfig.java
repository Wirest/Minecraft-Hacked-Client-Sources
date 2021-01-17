// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.config;

import org.apache.http.Consts;
import org.apache.http.util.Args;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import org.apache.http.annotation.Immutable;

@Immutable
public class ConnectionConfig implements Cloneable
{
    public static final ConnectionConfig DEFAULT;
    private final int bufferSize;
    private final int fragmentSizeHint;
    private final Charset charset;
    private final CodingErrorAction malformedInputAction;
    private final CodingErrorAction unmappableInputAction;
    private final MessageConstraints messageConstraints;
    
    ConnectionConfig(final int bufferSize, final int fragmentSizeHint, final Charset charset, final CodingErrorAction malformedInputAction, final CodingErrorAction unmappableInputAction, final MessageConstraints messageConstraints) {
        this.bufferSize = bufferSize;
        this.fragmentSizeHint = fragmentSizeHint;
        this.charset = charset;
        this.malformedInputAction = malformedInputAction;
        this.unmappableInputAction = unmappableInputAction;
        this.messageConstraints = messageConstraints;
    }
    
    public int getBufferSize() {
        return this.bufferSize;
    }
    
    public int getFragmentSizeHint() {
        return this.fragmentSizeHint;
    }
    
    public Charset getCharset() {
        return this.charset;
    }
    
    public CodingErrorAction getMalformedInputAction() {
        return this.malformedInputAction;
    }
    
    public CodingErrorAction getUnmappableInputAction() {
        return this.unmappableInputAction;
    }
    
    public MessageConstraints getMessageConstraints() {
        return this.messageConstraints;
    }
    
    @Override
    protected ConnectionConfig clone() throws CloneNotSupportedException {
        return (ConnectionConfig)super.clone();
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[bufferSize=").append(this.bufferSize).append(", fragmentSizeHint=").append(this.fragmentSizeHint).append(", charset=").append(this.charset).append(", malformedInputAction=").append(this.malformedInputAction).append(", unmappableInputAction=").append(this.unmappableInputAction).append(", messageConstraints=").append(this.messageConstraints).append("]");
        return builder.toString();
    }
    
    public static Builder custom() {
        return new Builder();
    }
    
    public static Builder copy(final ConnectionConfig config) {
        Args.notNull(config, "Connection config");
        return new Builder().setCharset(config.getCharset()).setMalformedInputAction(config.getMalformedInputAction()).setUnmappableInputAction(config.getUnmappableInputAction()).setMessageConstraints(config.getMessageConstraints());
    }
    
    static {
        DEFAULT = new Builder().build();
    }
    
    public static class Builder
    {
        private int bufferSize;
        private int fragmentSizeHint;
        private Charset charset;
        private CodingErrorAction malformedInputAction;
        private CodingErrorAction unmappableInputAction;
        private MessageConstraints messageConstraints;
        
        Builder() {
            this.fragmentSizeHint = -1;
        }
        
        public Builder setBufferSize(final int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }
        
        public Builder setFragmentSizeHint(final int fragmentSizeHint) {
            this.fragmentSizeHint = fragmentSizeHint;
            return this;
        }
        
        public Builder setCharset(final Charset charset) {
            this.charset = charset;
            return this;
        }
        
        public Builder setMalformedInputAction(final CodingErrorAction malformedInputAction) {
            this.malformedInputAction = malformedInputAction;
            if (malformedInputAction != null && this.charset == null) {
                this.charset = Consts.ASCII;
            }
            return this;
        }
        
        public Builder setUnmappableInputAction(final CodingErrorAction unmappableInputAction) {
            this.unmappableInputAction = unmappableInputAction;
            if (unmappableInputAction != null && this.charset == null) {
                this.charset = Consts.ASCII;
            }
            return this;
        }
        
        public Builder setMessageConstraints(final MessageConstraints messageConstraints) {
            this.messageConstraints = messageConstraints;
            return this;
        }
        
        public ConnectionConfig build() {
            Charset cs = this.charset;
            if (cs == null && (this.malformedInputAction != null || this.unmappableInputAction != null)) {
                cs = Consts.ASCII;
            }
            final int bufSize = (this.bufferSize > 0) ? this.bufferSize : 8192;
            final int fragmentHintSize = (this.fragmentSizeHint >= 0) ? this.fragmentSizeHint : bufSize;
            return new ConnectionConfig(bufSize, fragmentHintSize, cs, this.malformedInputAction, this.unmappableInputAction, this.messageConstraints);
        }
    }
}
