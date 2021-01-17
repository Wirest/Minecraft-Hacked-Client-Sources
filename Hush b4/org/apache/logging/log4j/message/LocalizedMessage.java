// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.MissingResourceException;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedMessage implements Message, LoggerNameAwareMessage
{
    private static final long serialVersionUID = 3893703791567290742L;
    private String bundleId;
    private transient ResourceBundle bundle;
    private Locale locale;
    private transient StatusLogger logger;
    private String loggerName;
    private String messagePattern;
    private String[] stringArgs;
    private transient Object[] argArray;
    private String formattedMessage;
    private transient Throwable throwable;
    
    public LocalizedMessage(final String messagePattern, final Object[] arguments) {
        this((ResourceBundle)null, null, messagePattern, arguments);
    }
    
    public LocalizedMessage(final String bundleId, final String key, final Object[] arguments) {
        this(bundleId, null, key, arguments);
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final String key, final Object[] arguments) {
        this(bundle, null, key, arguments);
    }
    
    public LocalizedMessage(final String bundleId, final Locale locale, final String key, final Object[] arguments) {
        this.logger = StatusLogger.getLogger();
        this.messagePattern = key;
        this.argArray = arguments;
        this.throwable = null;
        this.setup(bundleId, null, locale);
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final Locale locale, final String key, final Object[] arguments) {
        this.logger = StatusLogger.getLogger();
        this.messagePattern = key;
        this.argArray = arguments;
        this.throwable = null;
        this.setup(null, bundle, locale);
    }
    
    public LocalizedMessage(final Locale locale, final String key, final Object[] arguments) {
        this((ResourceBundle)null, locale, key, arguments);
    }
    
    public LocalizedMessage(final String messagePattern, final Object arg) {
        this((ResourceBundle)null, null, messagePattern, new Object[] { arg });
    }
    
    public LocalizedMessage(final String bundleId, final String key, final Object arg) {
        this(bundleId, null, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final String key, final Object arg) {
        this(bundle, null, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final String bundleId, final Locale locale, final String key, final Object arg) {
        this(bundleId, locale, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final Locale locale, final String key, final Object arg) {
        this(bundle, locale, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final Locale locale, final String key, final Object arg) {
        this((ResourceBundle)null, locale, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final String messagePattern, final Object arg1, final Object arg2) {
        this((ResourceBundle)null, null, messagePattern, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final String bundleId, final String key, final Object arg1, final Object arg2) {
        this(bundleId, null, key, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final String key, final Object arg1, final Object arg2) {
        this(bundle, null, key, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final String bundleId, final Locale locale, final String key, final Object arg1, final Object arg2) {
        this(bundleId, locale, key, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final Locale locale, final String key, final Object arg1, final Object arg2) {
        this(bundle, locale, key, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final Locale locale, final String key, final Object arg1, final Object arg2) {
        this((ResourceBundle)null, locale, key, new Object[] { arg1, arg2 });
    }
    
    @Override
    public void setLoggerName(final String name) {
        this.loggerName = name;
    }
    
    @Override
    public String getLoggerName() {
        return this.loggerName;
    }
    
    private void setup(final String bundleId, final ResourceBundle bundle, final Locale locale) {
        this.bundleId = bundleId;
        this.bundle = bundle;
        this.locale = locale;
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        }
        ResourceBundle bundle = this.bundle;
        if (bundle == null) {
            if (this.bundleId != null) {
                bundle = this.getBundle(this.bundleId, this.locale, false);
            }
            else {
                bundle = this.getBundle(this.loggerName, this.locale, true);
            }
        }
        final String messagePattern = this.getFormat();
        final String msgPattern = (bundle == null || !bundle.containsKey(messagePattern)) ? messagePattern : bundle.getString(messagePattern);
        final Object[] array = (this.argArray == null) ? this.stringArgs : this.argArray;
        final FormattedMessage msg = new FormattedMessage(msgPattern, array);
        this.formattedMessage = msg.getFormattedMessage();
        this.throwable = msg.getThrowable();
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
    
    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    protected ResourceBundle getBundle(final String key, final Locale locale, final boolean loop) {
        ResourceBundle rb = null;
        if (key == null) {
            return null;
        }
        try {
            if (locale != null) {
                rb = ResourceBundle.getBundle(key, locale);
            }
            else {
                rb = ResourceBundle.getBundle(key);
            }
        }
        catch (MissingResourceException ex) {
            if (!loop) {
                this.logger.debug("Unable to locate ResourceBundle " + key);
                return null;
            }
        }
        String substr = key;
        int i;
        while (rb == null && (i = substr.lastIndexOf(46)) > 0) {
            substr = substr.substring(0, i);
            try {
                if (locale != null) {
                    rb = ResourceBundle.getBundle(substr, locale);
                }
                else {
                    rb = ResourceBundle.getBundle(substr);
                }
            }
            catch (MissingResourceException ex2) {
                this.logger.debug("Unable to locate ResourceBundle " + substr);
            }
        }
        return rb;
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        this.getFormattedMessage();
        out.writeUTF(this.formattedMessage);
        out.writeUTF(this.messagePattern);
        out.writeUTF(this.bundleId);
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
        this.bundleId = in.readUTF();
        final int length = in.readInt();
        this.stringArgs = new String[length];
        for (int i = 0; i < length; ++i) {
            this.stringArgs[i] = in.readUTF();
        }
        this.logger = StatusLogger.getLogger();
        this.bundle = null;
        this.argArray = null;
    }
}
