// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.Locale;
import java.util.Properties;
import org.apache.logging.log4j.Logger;

public final class OptionConverter
{
    private static final Logger LOGGER;
    private static final String DELIM_START = "${";
    private static final char DELIM_STOP = '}';
    private static final int DELIM_START_LEN = 2;
    private static final int DELIM_STOP_LEN = 1;
    private static final int ONE_K = 1024;
    
    private OptionConverter() {
    }
    
    public static String[] concatenateArrays(final String[] l, final String[] r) {
        final int len = l.length + r.length;
        final String[] a = new String[len];
        System.arraycopy(l, 0, a, 0, l.length);
        System.arraycopy(r, 0, a, l.length, r.length);
        return a;
    }
    
    public static String convertSpecialChars(final String s) {
        final int len = s.length();
        final StringBuilder sbuf = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            char c = s.charAt(i++);
            if (c == '\\') {
                c = s.charAt(i++);
                if (c == 'n') {
                    c = '\n';
                }
                else if (c == 'r') {
                    c = '\r';
                }
                else if (c == 't') {
                    c = '\t';
                }
                else if (c == 'f') {
                    c = '\f';
                }
                else if (c == '\b') {
                    c = '\b';
                }
                else if (c == '\"') {
                    c = '\"';
                }
                else if (c == '\'') {
                    c = '\'';
                }
                else if (c == '\\') {
                    c = '\\';
                }
            }
            sbuf.append(c);
        }
        return sbuf.toString();
    }
    
    public static Object instantiateByKey(final Properties props, final String key, final Class<?> superClass, final Object defaultValue) {
        final String className = findAndSubst(key, props);
        if (className == null) {
            OptionConverter.LOGGER.error("Could not find value for key " + key);
            return defaultValue;
        }
        return instantiateByClassName(className.trim(), superClass, defaultValue);
    }
    
    public static boolean toBoolean(final String value, final boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        final String trimmedVal = value.trim();
        return "true".equalsIgnoreCase(trimmedVal) || (!"false".equalsIgnoreCase(trimmedVal) && defaultValue);
    }
    
    public static int toInt(final String value, final int defaultValue) {
        if (value != null) {
            final String s = value.trim();
            try {
                return Integer.parseInt(s);
            }
            catch (NumberFormatException e) {
                OptionConverter.LOGGER.error("[" + s + "] is not in proper int form.");
                e.printStackTrace();
            }
        }
        return defaultValue;
    }
    
    public static long toFileSize(final String value, final long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String str = value.trim().toUpperCase(Locale.ENGLISH);
        long multiplier = 1L;
        int index;
        if ((index = str.indexOf("KB")) != -1) {
            multiplier = 1024L;
            str = str.substring(0, index);
        }
        else if ((index = str.indexOf("MB")) != -1) {
            multiplier = 1048576L;
            str = str.substring(0, index);
        }
        else if ((index = str.indexOf("GB")) != -1) {
            multiplier = 1073741824L;
            str = str.substring(0, index);
        }
        if (str != null) {
            try {
                return Long.parseLong(str) * multiplier;
            }
            catch (NumberFormatException e) {
                OptionConverter.LOGGER.error("[" + str + "] is not in proper int form.");
                OptionConverter.LOGGER.error("[" + value + "] not in expected format.", e);
            }
        }
        return defaultValue;
    }
    
    public static String findAndSubst(final String key, final Properties props) {
        final String value = props.getProperty(key);
        if (value == null) {
            return null;
        }
        try {
            return substVars(value, props);
        }
        catch (IllegalArgumentException e) {
            OptionConverter.LOGGER.error("Bad option value [" + value + "].", e);
            return value;
        }
    }
    
    public static Object instantiateByClassName(final String className, final Class<?> superClass, final Object defaultValue) {
        if (className != null) {
            try {
                final Class<?> classObj = Loader.loadClass(className);
                if (!superClass.isAssignableFrom(classObj)) {
                    OptionConverter.LOGGER.error("A \"" + className + "\" object is not assignable to a \"" + superClass.getName() + "\" variable.");
                    OptionConverter.LOGGER.error("The class \"" + superClass.getName() + "\" was loaded by ");
                    OptionConverter.LOGGER.error("[" + superClass.getClassLoader() + "] whereas object of type ");
                    OptionConverter.LOGGER.error("\"" + classObj.getName() + "\" was loaded by [" + classObj.getClassLoader() + "].");
                    return defaultValue;
                }
                return classObj.newInstance();
            }
            catch (ClassNotFoundException e) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + className + "].", e);
            }
            catch (IllegalAccessException e2) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + className + "].", e2);
            }
            catch (InstantiationException e3) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + className + "].", e3);
            }
            catch (RuntimeException e4) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + className + "].", e4);
            }
        }
        return defaultValue;
    }
    
    public static String substVars(final String val, final Properties props) throws IllegalArgumentException {
        final StringBuilder sbuf = new StringBuilder();
        int i = 0;
        while (true) {
            int j = val.indexOf("${", i);
            if (j == -1) {
                if (i == 0) {
                    return val;
                }
                sbuf.append(val.substring(i, val.length()));
                return sbuf.toString();
            }
            else {
                sbuf.append(val.substring(i, j));
                final int k = val.indexOf(125, j);
                if (k == -1) {
                    throw new IllegalArgumentException('\"' + val + "\" has no closing brace. Opening brace at position " + j + '.');
                }
                j += 2;
                final String key = val.substring(j, k);
                String replacement = PropertiesUtil.getProperties().getStringProperty(key, null);
                if (replacement == null && props != null) {
                    replacement = props.getProperty(key);
                }
                if (replacement != null) {
                    final String recursiveReplacement = substVars(replacement, props);
                    sbuf.append(recursiveReplacement);
                }
                i = k + 1;
            }
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
