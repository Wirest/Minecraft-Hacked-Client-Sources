// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.text.Format;
import java.text.MessageFormat;
import java.util.regex.Pattern;

public class FormattedMessage implements Message
{
    private static final long serialVersionUID = -665975803997290697L;
    private static final int HASHVAL = 31;
    private static final String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    private static final Pattern MSG_PATTERN;
    private String messagePattern;
    private transient Object[] argArray;
    private String[] stringArgs;
    private transient String formattedMessage;
    private final Throwable throwable;
    private Message message;
    
    public FormattedMessage(final String messagePattern, final Object[] arguments, final Throwable throwable) {
        this.messagePattern = messagePattern;
        this.argArray = arguments;
        this.throwable = throwable;
    }
    
    public FormattedMessage(final String messagePattern, final Object[] arguments) {
        this.messagePattern = messagePattern;
        this.argArray = arguments;
        this.throwable = null;
    }
    
    public FormattedMessage(final String messagePattern, final Object arg) {
        this.messagePattern = messagePattern;
        this.argArray = new Object[] { arg };
        this.throwable = null;
    }
    
    public FormattedMessage(final String messagePattern, final Object arg1, final Object arg2) {
        this(messagePattern, new Object[] { arg1, arg2 });
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage == null) {
            if (this.message == null) {
                this.message = this.getMessage(this.messagePattern, this.argArray, this.throwable);
            }
            this.formattedMessage = this.message.getFormattedMessage();
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
    
    protected Message getMessage(final String msgPattern, final Object[] args, final Throwable throwable) {
        try {
            final MessageFormat format = new MessageFormat(msgPattern);
            final Format[] formats = format.getFormats();
            if (formats != null && formats.length > 0) {
                return new MessageFormatMessage(msgPattern, args);
            }
        }
        catch (Exception ex) {}
        try {
            if (FormattedMessage.MSG_PATTERN.matcher(msgPattern).find()) {
                return new StringFormattedMessage(msgPattern, args);
            }
        }
        catch (Exception ex2) {}
        return new ParameterizedMessage(msgPattern, args, throwable);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FormattedMessage that = (FormattedMessage)o;
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
        return "FormattedMessage[messagePattern=" + this.messagePattern + ", args=" + Arrays.toString(this.argArray) + "]";
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
        if (this.throwable != null) {
            return this.throwable;
        }
        if (this.message == null) {
            this.message = this.getMessage(this.messagePattern, this.argArray, this.throwable);
        }
        return this.message.getThrowable();
    }
    
    static {
        MSG_PATTERN = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
    }
}
