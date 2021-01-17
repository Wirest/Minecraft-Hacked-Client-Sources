// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.IllegalFormatException;
import java.util.Iterator;
import java.io.InputStream;
import java.io.IOException;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.Charsets;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.common.base.Splitter;
import java.util.regex.Pattern;

public class StringTranslate
{
    private static final Pattern numericVariablePattern;
    private static final Splitter equalSignSplitter;
    private static StringTranslate instance;
    private final Map<String, String> languageList;
    private long lastUpdateTimeInMilliseconds;
    
    static {
        numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
        equalSignSplitter = Splitter.on('=').limit(2);
        StringTranslate.instance = new StringTranslate();
    }
    
    public StringTranslate() {
        this.languageList = (Map<String, String>)Maps.newHashMap();
        try {
            final InputStream inputstream = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
            for (final String s : IOUtils.readLines(inputstream, Charsets.UTF_8)) {
                if (!s.isEmpty() && s.charAt(0) != '#') {
                    final String[] astring = Iterables.toArray(StringTranslate.equalSignSplitter.split(s), String.class);
                    if (astring == null || astring.length != 2) {
                        continue;
                    }
                    final String s2 = astring[0];
                    final String s3 = StringTranslate.numericVariablePattern.matcher(astring[1]).replaceAll("%$1s");
                    this.languageList.put(s2, s3);
                }
            }
            this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
        }
        catch (IOException ex) {}
    }
    
    static StringTranslate getInstance() {
        return StringTranslate.instance;
    }
    
    public static synchronized void replaceWith(final Map<String, String> p_135063_0_) {
        StringTranslate.instance.languageList.clear();
        StringTranslate.instance.languageList.putAll(p_135063_0_);
        StringTranslate.instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }
    
    public synchronized String translateKey(final String key) {
        return this.tryTranslateKey(key);
    }
    
    public synchronized String translateKeyFormat(final String key, final Object... format) {
        final String s = this.tryTranslateKey(key);
        try {
            return String.format(s, format);
        }
        catch (IllegalFormatException var5) {
            return "Format error: " + s;
        }
    }
    
    private String tryTranslateKey(final String key) {
        final String s = this.languageList.get(key);
        return (s == null) ? key : s;
    }
    
    public synchronized boolean isKeyTranslated(final String key) {
        return this.languageList.containsKey(key);
    }
    
    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
}
