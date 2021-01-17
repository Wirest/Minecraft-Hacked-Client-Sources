// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Iterator;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Comparator;
import com.ibm.icu.impl.locale.AsciiUtil;
import java.util.Map;

public final class LocaleIDParser
{
    private char[] id;
    private int index;
    private StringBuilder buffer;
    private boolean canonicalize;
    private boolean hadCountry;
    Map<String, String> keywords;
    String baseName;
    private static final char KEYWORD_SEPARATOR = '@';
    private static final char HYPHEN = '-';
    private static final char KEYWORD_ASSIGN = '=';
    private static final char COMMA = ',';
    private static final char ITEM_SEPARATOR = ';';
    private static final char DOT = '.';
    private static final char UNDERSCORE = '_';
    private static final char DONE = '\uffff';
    
    public LocaleIDParser(final String localeID) {
        this(localeID, false);
    }
    
    public LocaleIDParser(final String localeID, final boolean canonicalize) {
        this.id = localeID.toCharArray();
        this.index = 0;
        this.buffer = new StringBuilder(this.id.length + 5);
        this.canonicalize = canonicalize;
    }
    
    private void reset() {
        this.index = 0;
        this.buffer = new StringBuilder(this.id.length + 5);
    }
    
    private void append(final char c) {
        this.buffer.append(c);
    }
    
    private void addSeparator() {
        this.append('_');
    }
    
    private String getString(final int start) {
        return this.buffer.substring(start);
    }
    
    private void set(final int pos, final String s) {
        this.buffer.delete(pos, this.buffer.length());
        this.buffer.insert(pos, s);
    }
    
    private void append(final String s) {
        this.buffer.append(s);
    }
    
    private char next() {
        if (this.index == this.id.length) {
            ++this.index;
            return '\uffff';
        }
        return this.id[this.index++];
    }
    
    private void skipUntilTerminatorOrIDSeparator() {
        while (!this.isTerminatorOrIDSeparator(this.next())) {}
        --this.index;
    }
    
    private boolean atTerminator() {
        return this.index >= this.id.length || this.isTerminator(this.id[this.index]);
    }
    
    private boolean isTerminator(final char c) {
        return c == '@' || c == '\uffff' || c == '.';
    }
    
    private boolean isTerminatorOrIDSeparator(final char c) {
        return c == '_' || c == '-' || this.isTerminator(c);
    }
    
    private boolean haveExperimentalLanguagePrefix() {
        if (this.id.length > 2) {
            char c = this.id[1];
            if (c == '-' || c == '_') {
                c = this.id[0];
                return c == 'x' || c == 'X' || c == 'i' || c == 'I';
            }
        }
        return false;
    }
    
    private boolean haveKeywordAssign() {
        for (int i = this.index; i < this.id.length; ++i) {
            if (this.id[i] == '=') {
                return true;
            }
        }
        return false;
    }
    
    private int parseLanguage() {
        final int startLength = this.buffer.length();
        if (this.haveExperimentalLanguagePrefix()) {
            this.append(AsciiUtil.toLower(this.id[0]));
            this.append('-');
            this.index = 2;
        }
        char c;
        while (!this.isTerminatorOrIDSeparator(c = this.next())) {
            this.append(AsciiUtil.toLower(c));
        }
        --this.index;
        if (this.buffer.length() - startLength == 3) {
            final String lang = LocaleIDs.threeToTwoLetterLanguage(this.getString(0));
            if (lang != null) {
                this.set(0, lang);
            }
        }
        return 0;
    }
    
    private void skipLanguage() {
        if (this.haveExperimentalLanguagePrefix()) {
            this.index = 2;
        }
        this.skipUntilTerminatorOrIDSeparator();
    }
    
    private int parseScript() {
        if (!this.atTerminator()) {
            final int oldIndex = this.index;
            ++this.index;
            int oldBlen = this.buffer.length();
            boolean firstPass = true;
            char c;
            while (!this.isTerminatorOrIDSeparator(c = this.next()) && AsciiUtil.isAlpha(c)) {
                if (firstPass) {
                    this.addSeparator();
                    this.append(AsciiUtil.toUpper(c));
                    firstPass = false;
                }
                else {
                    this.append(AsciiUtil.toLower(c));
                }
            }
            --this.index;
            if (this.index - oldIndex != 5) {
                this.index = oldIndex;
                this.buffer.delete(oldBlen, this.buffer.length());
            }
            else {
                ++oldBlen;
            }
            return oldBlen;
        }
        return this.buffer.length();
    }
    
    private void skipScript() {
        if (!this.atTerminator()) {
            final int oldIndex = this.index;
            ++this.index;
            char c;
            while (!this.isTerminatorOrIDSeparator(c = this.next()) && AsciiUtil.isAlpha(c)) {}
            --this.index;
            if (this.index - oldIndex != 5) {
                this.index = oldIndex;
            }
        }
    }
    
    private int parseCountry() {
        if (!this.atTerminator()) {
            final int oldIndex = this.index;
            ++this.index;
            int oldBlen = this.buffer.length();
            boolean firstPass = true;
            char c;
            while (!this.isTerminatorOrIDSeparator(c = this.next())) {
                if (firstPass) {
                    this.hadCountry = true;
                    this.addSeparator();
                    ++oldBlen;
                    firstPass = false;
                }
                this.append(AsciiUtil.toUpper(c));
            }
            --this.index;
            final int charsAppended = this.buffer.length() - oldBlen;
            if (charsAppended != 0) {
                if (charsAppended < 2 || charsAppended > 3) {
                    this.index = oldIndex;
                    --oldBlen;
                    this.buffer.delete(oldBlen, this.buffer.length());
                    this.hadCountry = false;
                }
                else if (charsAppended == 3) {
                    final String region = LocaleIDs.threeToTwoLetterRegion(this.getString(oldBlen));
                    if (region != null) {
                        this.set(oldBlen, region);
                    }
                }
            }
            return oldBlen;
        }
        return this.buffer.length();
    }
    
    private void skipCountry() {
        if (!this.atTerminator()) {
            if (this.id[this.index] == '_' || this.id[this.index] == '-') {
                ++this.index;
            }
            final int oldIndex = this.index;
            this.skipUntilTerminatorOrIDSeparator();
            final int charsSkipped = this.index - oldIndex;
            if (charsSkipped < 2 || charsSkipped > 3) {
                this.index = oldIndex;
            }
        }
    }
    
    private int parseVariant() {
        int oldBlen = this.buffer.length();
        boolean start = true;
        boolean needSeparator = true;
        boolean skipping = false;
        boolean firstPass = true;
        char c;
        while ((c = this.next()) != '\uffff') {
            if (c == '.') {
                start = false;
                skipping = true;
            }
            else if (c == '@') {
                if (this.haveKeywordAssign()) {
                    break;
                }
                skipping = false;
                start = false;
                needSeparator = true;
            }
            else if (start) {
                start = false;
                if (c == '_' || c == '-') {
                    continue;
                }
                --this.index;
            }
            else {
                if (skipping) {
                    continue;
                }
                if (needSeparator) {
                    needSeparator = false;
                    if (firstPass && !this.hadCountry) {
                        this.addSeparator();
                        ++oldBlen;
                    }
                    this.addSeparator();
                    if (firstPass) {
                        ++oldBlen;
                        firstPass = false;
                    }
                }
                c = AsciiUtil.toUpper(c);
                if (c == '-' || c == ',') {
                    c = '_';
                }
                this.append(c);
            }
        }
        --this.index;
        return oldBlen;
    }
    
    public String getLanguage() {
        this.reset();
        return this.getString(this.parseLanguage());
    }
    
    public String getScript() {
        this.reset();
        this.skipLanguage();
        return this.getString(this.parseScript());
    }
    
    public String getCountry() {
        this.reset();
        this.skipLanguage();
        this.skipScript();
        return this.getString(this.parseCountry());
    }
    
    public String getVariant() {
        this.reset();
        this.skipLanguage();
        this.skipScript();
        this.skipCountry();
        return this.getString(this.parseVariant());
    }
    
    public String[] getLanguageScriptCountryVariant() {
        this.reset();
        return new String[] { this.getString(this.parseLanguage()), this.getString(this.parseScript()), this.getString(this.parseCountry()), this.getString(this.parseVariant()) };
    }
    
    public void setBaseName(final String baseName) {
        this.baseName = baseName;
    }
    
    public void parseBaseName() {
        if (this.baseName != null) {
            this.set(0, this.baseName);
        }
        else {
            this.reset();
            this.parseLanguage();
            this.parseScript();
            this.parseCountry();
            this.parseVariant();
            final int len = this.buffer.length();
            if (len > 0 && this.buffer.charAt(len - 1) == '_') {
                this.buffer.deleteCharAt(len - 1);
            }
        }
    }
    
    public String getBaseName() {
        if (this.baseName != null) {
            return this.baseName;
        }
        this.parseBaseName();
        return this.getString(0);
    }
    
    public String getName() {
        this.parseBaseName();
        this.parseKeywords();
        return this.getString(0);
    }
    
    private boolean setToKeywordStart() {
        int i = this.index;
        while (i < this.id.length) {
            if (this.id[i] == '@') {
                if (this.canonicalize) {
                    for (int j = ++i; j < this.id.length; ++j) {
                        if (this.id[j] == '=') {
                            this.index = i;
                            return true;
                        }
                    }
                    break;
                }
                if (++i < this.id.length) {
                    this.index = i;
                    return true;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    private static boolean isDoneOrKeywordAssign(final char c) {
        return c == '\uffff' || c == '=';
    }
    
    private static boolean isDoneOrItemSeparator(final char c) {
        return c == '\uffff' || c == ';';
    }
    
    private String getKeyword() {
        final int start = this.index;
        while (!isDoneOrKeywordAssign(this.next())) {}
        --this.index;
        return AsciiUtil.toLowerString(new String(this.id, start, this.index - start).trim());
    }
    
    private String getValue() {
        final int start = this.index;
        while (!isDoneOrItemSeparator(this.next())) {}
        --this.index;
        return new String(this.id, start, this.index - start).trim();
    }
    
    private Comparator<String> getKeyComparator() {
        final Comparator<String> comp = new Comparator<String>() {
            public int compare(final String lhs, final String rhs) {
                return lhs.compareTo(rhs);
            }
        };
        return comp;
    }
    
    public Map<String, String> getKeywordMap() {
        if (this.keywords == null) {
            TreeMap<String, String> m = null;
            if (this.setToKeywordStart()) {
                do {
                    final String key = this.getKeyword();
                    if (key.length() == 0) {
                        break;
                    }
                    final char c = this.next();
                    if (c != '=') {
                        if (c == '\uffff') {
                            break;
                        }
                        continue;
                    }
                    else {
                        final String value = this.getValue();
                        if (value.length() == 0) {
                            continue;
                        }
                        if (m == null) {
                            m = new TreeMap<String, String>(this.getKeyComparator());
                        }
                        else if (m.containsKey(key)) {
                            continue;
                        }
                        m.put(key, value);
                    }
                } while (this.next() == ';');
            }
            this.keywords = ((m != null) ? m : Collections.emptyMap());
        }
        return this.keywords;
    }
    
    private int parseKeywords() {
        int oldBlen = this.buffer.length();
        final Map<String, String> m = this.getKeywordMap();
        if (!m.isEmpty()) {
            boolean first = true;
            for (final Map.Entry<String, String> e : m.entrySet()) {
                this.append(first ? '@' : ';');
                first = false;
                this.append(e.getKey());
                this.append('=');
                this.append(e.getValue());
            }
            if (!first) {
                ++oldBlen;
            }
        }
        return oldBlen;
    }
    
    public Iterator<String> getKeywords() {
        final Map<String, String> m = this.getKeywordMap();
        return m.isEmpty() ? null : m.keySet().iterator();
    }
    
    public String getKeywordValue(final String keywordName) {
        final Map<String, String> m = this.getKeywordMap();
        return m.isEmpty() ? null : m.get(AsciiUtil.toLowerString(keywordName.trim()));
    }
    
    public void defaultKeywordValue(final String keywordName, final String value) {
        this.setKeywordValue(keywordName, value, false);
    }
    
    public void setKeywordValue(final String keywordName, final String value) {
        this.setKeywordValue(keywordName, value, true);
    }
    
    private void setKeywordValue(String keywordName, String value, final boolean reset) {
        if (keywordName == null) {
            if (reset) {
                this.keywords = Collections.emptyMap();
            }
        }
        else {
            keywordName = AsciiUtil.toLowerString(keywordName.trim());
            if (keywordName.length() == 0) {
                throw new IllegalArgumentException("keyword must not be empty");
            }
            if (value != null) {
                value = value.trim();
                if (value.length() == 0) {
                    throw new IllegalArgumentException("value must not be empty");
                }
            }
            final Map<String, String> m = this.getKeywordMap();
            if (m.isEmpty()) {
                if (value != null) {
                    (this.keywords = new TreeMap<String, String>(this.getKeyComparator())).put(keywordName, value.trim());
                }
            }
            else if (reset || !m.containsKey(keywordName)) {
                if (value != null) {
                    m.put(keywordName, value);
                }
                else {
                    m.remove(keywordName);
                    if (m.isEmpty()) {
                        this.keywords = Collections.emptyMap();
                    }
                }
            }
        }
    }
}
