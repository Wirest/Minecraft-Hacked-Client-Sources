// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class ParameterizedMessage implements Message
{
    public static final String RECURSION_PREFIX = "[...";
    public static final String RECURSION_SUFFIX = "...]";
    public static final String ERROR_PREFIX = "[!!!";
    public static final String ERROR_SEPARATOR = "=>";
    public static final String ERROR_MSG_SEPARATOR = ":";
    public static final String ERROR_SUFFIX = "!!!]";
    private static final long serialVersionUID = -665975803997290697L;
    private static final int HASHVAL = 31;
    private static final char DELIM_START = '{';
    private static final char DELIM_STOP = '}';
    private static final char ESCAPE_CHAR = '\\';
    private final String messagePattern;
    private final String[] stringArgs;
    private transient Object[] argArray;
    private transient String formattedMessage;
    private transient Throwable throwable;
    
    public ParameterizedMessage(final String messagePattern, final String[] stringArgs, final Throwable throwable) {
        this.messagePattern = messagePattern;
        this.stringArgs = stringArgs;
        this.throwable = throwable;
    }
    
    public ParameterizedMessage(final String messagePattern, final Object[] objectArgs, final Throwable throwable) {
        this.messagePattern = messagePattern;
        this.throwable = throwable;
        this.stringArgs = this.parseArguments(objectArgs);
    }
    
    public ParameterizedMessage(final String messagePattern, final Object[] arguments) {
        this.messagePattern = messagePattern;
        this.stringArgs = this.parseArguments(arguments);
    }
    
    public ParameterizedMessage(final String messagePattern, final Object arg) {
        this(messagePattern, new Object[] { arg });
    }
    
    public ParameterizedMessage(final String messagePattern, final Object arg1, final Object arg2) {
        this(messagePattern, new Object[] { arg1, arg2 });
    }
    
    private String[] parseArguments(final Object[] arguments) {
        if (arguments == null) {
            return null;
        }
        final int argsCount = countArgumentPlaceholders(this.messagePattern);
        int resultArgCount = arguments.length;
        if (argsCount < arguments.length && this.throwable == null && arguments[arguments.length - 1] instanceof Throwable) {
            this.throwable = (Throwable)arguments[arguments.length - 1];
            --resultArgCount;
        }
        this.argArray = new Object[resultArgCount];
        for (int i = 0; i < resultArgCount; ++i) {
            this.argArray[i] = arguments[i];
        }
        String[] strArgs;
        if (argsCount == 1 && this.throwable == null && arguments.length > 1) {
            strArgs = new String[] { deepToString(arguments) };
        }
        else {
            strArgs = new String[resultArgCount];
            for (int j = 0; j < strArgs.length; ++j) {
                strArgs[j] = deepToString(arguments[j]);
            }
        }
        return strArgs;
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage == null) {
            this.formattedMessage = this.formatMessage(this.messagePattern, this.stringArgs);
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
    
    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    protected String formatMessage(final String msgPattern, final String[] sArgs) {
        return format(msgPattern, sArgs);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ParameterizedMessage that = (ParameterizedMessage)o;
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
    
    public static String format(final String messagePattern, final Object[] arguments) {
        if (messagePattern == null || arguments == null || arguments.length == 0) {
            return messagePattern;
        }
        final StringBuilder result = new StringBuilder();
        int escapeCounter = 0;
        int currentArgument = 0;
        for (int i = 0; i < messagePattern.length(); ++i) {
            final char curChar = messagePattern.charAt(i);
            if (curChar == '\\') {
                ++escapeCounter;
            }
            else if (curChar == '{' && i < messagePattern.length() - 1 && messagePattern.charAt(i + 1) == '}') {
                for (int escapedEscapes = escapeCounter / 2, j = 0; j < escapedEscapes; ++j) {
                    result.append('\\');
                }
                if (escapeCounter % 2 == 1) {
                    result.append('{');
                    result.append('}');
                }
                else {
                    if (currentArgument < arguments.length) {
                        result.append(arguments[currentArgument]);
                    }
                    else {
                        result.append('{').append('}');
                    }
                    ++currentArgument;
                }
                ++i;
                escapeCounter = 0;
            }
            else {
                if (escapeCounter > 0) {
                    for (int k = 0; k < escapeCounter; ++k) {
                        result.append('\\');
                    }
                    escapeCounter = 0;
                }
                result.append(curChar);
            }
        }
        return result.toString();
    }
    
    public static int countArgumentPlaceholders(final String messagePattern) {
        if (messagePattern == null) {
            return 0;
        }
        final int delim = messagePattern.indexOf(123);
        if (delim == -1) {
            return 0;
        }
        int result = 0;
        boolean isEscaped = false;
        for (int i = 0; i < messagePattern.length(); ++i) {
            final char curChar = messagePattern.charAt(i);
            if (curChar == '\\') {
                isEscaped = !isEscaped;
            }
            else if (curChar == '{') {
                if (!isEscaped && i < messagePattern.length() - 1 && messagePattern.charAt(i + 1) == '}') {
                    ++result;
                    ++i;
                }
                isEscaped = false;
            }
            else {
                isEscaped = false;
            }
        }
        return result;
    }
    
    public static String deepToString(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String)o;
        }
        final StringBuilder str = new StringBuilder();
        final Set<String> dejaVu = new HashSet<String>();
        recursiveDeepToString(o, str, dejaVu);
        return str.toString();
    }
    
    private static void recursiveDeepToString(final Object o, final StringBuilder str, final Set<String> dejaVu) {
        if (o == null) {
            str.append("null");
            return;
        }
        if (o instanceof String) {
            str.append(o);
            return;
        }
        final Class<?> oClass = o.getClass();
        if (oClass.isArray()) {
            if (oClass == byte[].class) {
                str.append(Arrays.toString((byte[])o));
            }
            else if (oClass == short[].class) {
                str.append(Arrays.toString((short[])o));
            }
            else if (oClass == int[].class) {
                str.append(Arrays.toString((int[])o));
            }
            else if (oClass == long[].class) {
                str.append(Arrays.toString((long[])o));
            }
            else if (oClass == float[].class) {
                str.append(Arrays.toString((float[])o));
            }
            else if (oClass == double[].class) {
                str.append(Arrays.toString((double[])o));
            }
            else if (oClass == boolean[].class) {
                str.append(Arrays.toString((boolean[])o));
            }
            else if (oClass == char[].class) {
                str.append(Arrays.toString((char[])o));
            }
            else {
                final String id = identityToString(o);
                if (dejaVu.contains(id)) {
                    str.append("[...").append(id).append("...]");
                }
                else {
                    dejaVu.add(id);
                    final Object[] oArray = (Object[])o;
                    str.append("[");
                    boolean first = true;
                    for (final Object current : oArray) {
                        if (first) {
                            first = false;
                        }
                        else {
                            str.append(", ");
                        }
                        recursiveDeepToString(current, str, new HashSet<String>(dejaVu));
                    }
                    str.append("]");
                }
            }
        }
        else if (o instanceof Map) {
            final String id = identityToString(o);
            if (dejaVu.contains(id)) {
                str.append("[...").append(id).append("...]");
            }
            else {
                dejaVu.add(id);
                final Map<?, ?> oMap = (Map<?, ?>)o;
                str.append("{");
                boolean isFirst = true;
                for (final Object o2 : oMap.entrySet()) {
                    final Map.Entry<?, ?> current2 = (Map.Entry<?, ?>)o2;
                    if (isFirst) {
                        isFirst = false;
                    }
                    else {
                        str.append(", ");
                    }
                    final Object key = current2.getKey();
                    final Object value = current2.getValue();
                    recursiveDeepToString(key, str, new HashSet<String>(dejaVu));
                    str.append("=");
                    recursiveDeepToString(value, str, new HashSet<String>(dejaVu));
                }
                str.append("}");
            }
        }
        else if (o instanceof Collection) {
            final String id = identityToString(o);
            if (dejaVu.contains(id)) {
                str.append("[...").append(id).append("...]");
            }
            else {
                dejaVu.add(id);
                final Collection<?> oCol = (Collection<?>)o;
                str.append("[");
                boolean isFirst = true;
                for (final Object anOCol : oCol) {
                    if (isFirst) {
                        isFirst = false;
                    }
                    else {
                        str.append(", ");
                    }
                    recursiveDeepToString(anOCol, str, new HashSet<String>(dejaVu));
                }
                str.append("]");
            }
        }
        else if (o instanceof Date) {
            final Date date = (Date)o;
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            str.append(format.format(date));
        }
        else {
            try {
                str.append(o.toString());
            }
            catch (Throwable t) {
                str.append("[!!!");
                str.append(identityToString(o));
                str.append("=>");
                final String msg = t.getMessage();
                final String className = t.getClass().getName();
                str.append(className);
                if (!className.equals(msg)) {
                    str.append(":");
                    str.append(msg);
                }
                str.append("!!!]");
            }
        }
    }
    
    public static String identityToString(final Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
    }
    
    @Override
    public String toString() {
        return "ParameterizedMessage[messagePattern=" + this.messagePattern + ", stringArgs=" + Arrays.toString(this.stringArgs) + ", throwable=" + this.throwable + "]";
    }
}
