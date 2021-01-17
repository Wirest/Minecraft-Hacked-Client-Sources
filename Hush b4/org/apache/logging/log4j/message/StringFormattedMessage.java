// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.IllegalFormatException;
import org.apache.logging.log4j.Logger;

public class StringFormattedMessage implements Message
{
    private static final Logger LOGGER;
    private static final long serialVersionUID = -665975803997290697L;
    private static final int HASHVAL = 31;
    private String messagePattern;
    private transient Object[] argArray;
    private String[] stringArgs;
    private transient String formattedMessage;
    private transient Throwable throwable;
    
    public StringFormattedMessage(final String messagePattern, final Object... arguments) {
        this.messagePattern = messagePattern;
        this.argArray = arguments;
        if (arguments != null && arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable) {
            this.throwable = (Throwable)arguments[arguments.length - 1];
        }
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage == null) {
            this.formattedMessage = this.formatMessage(this.messagePattern, this.argArray);
        }
        return this.formattedMessage;
    }
    
    @Override
    public String getFormat() {
        return this.messagePattern;
    }
    
    @Override
    public Object[] getParameters() {
        if (this.argArray != null) {
            return this.argArray;
        }
        return this.stringArgs;
    }
    
    protected String formatMessage(final String msgPattern, final Object... args) {
        try {
            return String.format(msgPattern, args);
        }
        catch (IllegalFormatException ife) {
            StringFormattedMessage.LOGGER.error("Unable to format msg: " + msgPattern, ife);
            return msgPattern;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final StringFormattedMessage that = (StringFormattedMessage)o;
        if (this.messagePattern != null) {
            if (this.messagePattern.equals(that.messagePattern)) {
                return Arrays.equals(this.stringArgs, that.stringArgs);
            }
        }
        else if (that.messagePattern == null) {
            return Arrays.equals(this.stringArgs, that.stringArgs);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
        result = 31 * result + ((this.stringArgs != null) ? Arrays.hashCode(this.stringArgs) : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "StringFormatMessage[messagePattern=" + this.messagePattern + ", args=" + Arrays.toString(this.argArray) + "]";
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        this.getFormattedMessage();
        out.writeUTF(this.formattedMessage);
        out.writeUTF(this.messagePattern);
        out.writeInt(this.argArray.length);
        this.stringArgs = new String[this.argArray.length];
        int i = 0;
        for (final Object obj : this.argArray) {
            this.stringArgs[i] = obj.toString();
            ++i;
        }
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.formattedMessage = in.readUTF();
        this.messagePattern = in.readUTF();
        final int length = in.readInt();
        this.stringArgs = new String[length];
        for (int i = 0; i < length; ++i) {
            this.stringArgs[i] = in.readUTF();
        }
    }
    
    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
