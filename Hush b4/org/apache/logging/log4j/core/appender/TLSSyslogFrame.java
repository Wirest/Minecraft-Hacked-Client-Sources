// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

public class TLSSyslogFrame
{
    public static final char SPACE = ' ';
    private String message;
    private int messageLengthInBytes;
    
    public TLSSyslogFrame(final String message) {
        this.setMessage(message);
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
        this.setLengthInBytes();
    }
    
    private void setLengthInBytes() {
        this.messageLengthInBytes = this.message.length();
    }
    
    public byte[] getBytes() {
        final String frame = this.toString();
        return frame.getBytes();
    }
    
    @Override
    public String toString() {
        final String length = Integer.toString(this.messageLengthInBytes);
        return length + ' ' + this.message;
    }
    
    @Override
    public boolean equals(final Object frame) {
        return super.equals(frame);
    }
    
    public boolean equals(final TLSSyslogFrame frame) {
        return this.isLengthEquals(frame) && this.isMessageEquals(frame);
    }
    
    private boolean isLengthEquals(final TLSSyslogFrame frame) {
        return this.messageLengthInBytes == frame.messageLengthInBytes;
    }
    
    private boolean isMessageEquals(final TLSSyslogFrame frame) {
        return this.message.equals(frame.message);
    }
}
