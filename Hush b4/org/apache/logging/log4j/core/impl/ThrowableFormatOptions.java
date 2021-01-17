// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;
import org.apache.logging.log4j.core.helpers.Constants;
import java.util.List;

public final class ThrowableFormatOptions
{
    private static final int DEFAULT_LINES = Integer.MAX_VALUE;
    protected static final ThrowableFormatOptions DEFAULT;
    private static final String FULL = "full";
    private static final String NONE = "none";
    private static final String SHORT = "short";
    private final int lines;
    private final String separator;
    private final List<String> packages;
    public static final String CLASS_NAME = "short.className";
    public static final String METHOD_NAME = "short.methodName";
    public static final String LINE_NUMBER = "short.lineNumber";
    public static final String FILE_NAME = "short.fileName";
    public static final String MESSAGE = "short.message";
    public static final String LOCALIZED_MESSAGE = "short.localizedMessage";
    
    protected ThrowableFormatOptions(final int lines, final String separator, final List<String> packages) {
        this.lines = lines;
        this.separator = ((separator == null) ? Constants.LINE_SEP : separator);
        this.packages = packages;
    }
    
    protected ThrowableFormatOptions(final List<String> packages) {
        this(Integer.MAX_VALUE, null, packages);
    }
    
    protected ThrowableFormatOptions() {
        this(Integer.MAX_VALUE, null, null);
    }
    
    public int getLines() {
        return this.lines;
    }
    
    public String getSeparator() {
        return this.separator;
    }
    
    public List<String> getPackages() {
        return this.packages;
    }
    
    public boolean allLines() {
        return this.lines == Integer.MAX_VALUE;
    }
    
    public boolean anyLines() {
        return this.lines > 0;
    }
    
    public int minLines(final int maxLines) {
        return (this.lines > maxLines) ? maxLines : this.lines;
    }
    
    public boolean hasPackages() {
        return this.packages != null && !this.packages.isEmpty();
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append("{").append(this.allLines() ? "full" : ((this.lines == 2) ? "short" : (this.anyLines() ? String.valueOf(this.lines) : "none"))).append("}");
        s.append("{separator(").append(this.separator).append(")}");
        if (this.hasPackages()) {
            s.append("{filters(");
            for (final String p : this.packages) {
                s.append(p).append(",");
            }
            s.deleteCharAt(s.length() - 1);
            s.append(")}");
        }
        return s.toString();
    }
    
    public static ThrowableFormatOptions newInstance(String[] options) {
        if (options == null || options.length == 0) {
            return ThrowableFormatOptions.DEFAULT;
        }
        if (options.length == 1 && options[0] != null && options[0].length() > 0) {
            final String[] opts = options[0].split(",", 2);
            final String first = opts[0].trim();
            final Scanner scanner = new Scanner(first);
            if (opts.length > 1 && (first.equalsIgnoreCase("full") || first.equalsIgnoreCase("short") || first.equalsIgnoreCase("none") || scanner.hasNextInt())) {
                options = new String[] { first, opts[1].trim() };
            }
            scanner.close();
        }
        int lines = ThrowableFormatOptions.DEFAULT.lines;
        String separator = ThrowableFormatOptions.DEFAULT.separator;
        List<String> packages = ThrowableFormatOptions.DEFAULT.packages;
        for (final String rawOption : options) {
            if (rawOption != null) {
                final String option = rawOption.trim();
                if (!option.isEmpty()) {
                    if (option.startsWith("separator(") && option.endsWith(")")) {
                        separator = option.substring("separator(".length(), option.length() - 1);
                    }
                    else if (option.startsWith("filters(") && option.endsWith(")")) {
                        final String filterStr = option.substring("filters(".length(), option.length() - 1);
                        if (filterStr.length() > 0) {
                            final String[] array = filterStr.split(",");
                            if (array.length > 0) {
                                packages = new ArrayList<String>(array.length);
                                for (String token : array) {
                                    token = token.trim();
                                    if (token.length() > 0) {
                                        packages.add(token);
                                    }
                                }
                            }
                        }
                    }
                    else if (option.equalsIgnoreCase("none")) {
                        lines = 0;
                    }
                    else if (option.equalsIgnoreCase("short") || option.equalsIgnoreCase("short.className") || option.equalsIgnoreCase("short.methodName") || option.equalsIgnoreCase("short.lineNumber") || option.equalsIgnoreCase("short.fileName") || option.equalsIgnoreCase("short.message") || option.equalsIgnoreCase("short.localizedMessage")) {
                        lines = 2;
                    }
                    else if (!option.equalsIgnoreCase("full")) {
                        lines = Integer.parseInt(option);
                    }
                }
            }
        }
        return new ThrowableFormatOptions(lines, separator, packages);
    }
    
    static {
        DEFAULT = new ThrowableFormatOptions();
    }
}
