package net.minecraft.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class StringTranslate {
    /**
     * Pattern that matches numeric variable placeholders in a resource string, such as "%d", "%3$d", "%.2f"
     */
    private static final Pattern numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");

    /**
     * A Splitter that splits a string on the first "=".  For example, "a=b=c" would split into ["a", "b=c"].
     */
    private static final Splitter equalSignSplitter = Splitter.on('=').limit(2);

    /**
     * Is the private singleton instance of StringTranslate.
     */
    private static StringTranslate instance = new StringTranslate();
    private final Map languageList = Maps.newHashMap();

    /**
     * The time, in milliseconds since epoch, that this instance was last updated
     */
    private long lastUpdateTimeInMilliseconds;
    private static final String __OBFID = "CL_00001212";

    public StringTranslate() {
        try {
            InputStream var1 = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
            Iterator var2 = IOUtils.readLines(var1, Charsets.UTF_8).iterator();

            while (var2.hasNext()) {
                String var3 = (String) var2.next();

                if (!var3.isEmpty() && var3.charAt(0) != 35) {
                    String[] var4 = (String[]) Iterables.toArray(equalSignSplitter.split(var3), String.class);

                    if (var4 != null && var4.length == 2) {
                        String var5 = var4[0];
                        String var6 = numericVariablePattern.matcher(var4[1]).replaceAll("%$1s");
                        this.languageList.put(var5, var6);
                    }
                }
            }

            this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
        } catch (IOException var7) {
            ;
        }
    }

    /**
     * Return the StringTranslate singleton instance
     */
    static StringTranslate getInstance() {
        return instance;
    }

    /**
     * Replaces all the current instance's translations with the ones that are passed in.
     */
    public static synchronized void replaceWith(Map p_135063_0_) {
        instance.languageList.clear();
        instance.languageList.putAll(p_135063_0_);
        instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    /**
     * Translate a key to current language.
     */
    public synchronized String translateKey(String p_74805_1_) {
        return this.tryTranslateKey(p_74805_1_);
    }

    /**
     * Translate a key to current language applying String.format()
     */
    public synchronized String translateKeyFormat(String p_74803_1_, Object... p_74803_2_) {
        String var3 = this.tryTranslateKey(p_74803_1_);

        try {
            return String.format(var3, p_74803_2_);
        } catch (IllegalFormatException var5) {
            return "Format error: " + var3;
        }
    }

    /**
     * Tries to look up a translation for the given key; spits back the key if no result was found.
     */
    private String tryTranslateKey(String p_135064_1_) {
        String var2 = (String) this.languageList.get(p_135064_1_);
        return var2 == null ? p_135064_1_ : var2;
    }

    /**
     * Returns true if the passed key is in the translation table.
     */
    public synchronized boolean isKeyTranslated(String p_94520_1_) {
        return this.languageList.containsKey(p_94520_1_);
    }

    /**
     * Gets the time, in milliseconds since epoch, that this instance was last updated
     */
    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
}
