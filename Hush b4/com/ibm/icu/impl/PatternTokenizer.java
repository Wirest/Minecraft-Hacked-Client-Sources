// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;

public class PatternTokenizer
{
    private UnicodeSet ignorableCharacters;
    private UnicodeSet syntaxCharacters;
    private UnicodeSet extraQuotingCharacters;
    private UnicodeSet escapeCharacters;
    private boolean usingSlash;
    private boolean usingQuote;
    private transient UnicodeSet needingQuoteCharacters;
    private int start;
    private int limit;
    private String pattern;
    public static final char SINGLE_QUOTE = '\'';
    public static final char BACK_SLASH = '\\';
    private static int NO_QUOTE;
    private static int IN_QUOTE;
    public static final int DONE = 0;
    public static final int SYNTAX = 1;
    public static final int LITERAL = 2;
    public static final int BROKEN_QUOTE = 3;
    public static final int BROKEN_ESCAPE = 4;
    public static final int UNKNOWN = 5;
    private static final int AFTER_QUOTE = -1;
    private static final int NONE = 0;
    private static final int START_QUOTE = 1;
    private static final int NORMAL_QUOTE = 2;
    private static final int SLASH_START = 3;
    private static final int HEX = 4;
    
    public PatternTokenizer() {
        this.ignorableCharacters = new UnicodeSet();
        this.syntaxCharacters = new UnicodeSet();
        this.extraQuotingCharacters = new UnicodeSet();
        this.escapeCharacters = new UnicodeSet();
        this.usingSlash = false;
        this.usingQuote = false;
        this.needingQuoteCharacters = null;
    }
    
    public UnicodeSet getIgnorableCharacters() {
        return (UnicodeSet)this.ignorableCharacters.clone();
    }
    
    public PatternTokenizer setIgnorableCharacters(final UnicodeSet ignorableCharacters) {
        this.ignorableCharacters = (UnicodeSet)ignorableCharacters.clone();
        this.needingQuoteCharacters = null;
        return this;
    }
    
    public UnicodeSet getSyntaxCharacters() {
        return (UnicodeSet)this.syntaxCharacters.clone();
    }
    
    public UnicodeSet getExtraQuotingCharacters() {
        return (UnicodeSet)this.extraQuotingCharacters.clone();
    }
    
    public PatternTokenizer setSyntaxCharacters(final UnicodeSet syntaxCharacters) {
        this.syntaxCharacters = (UnicodeSet)syntaxCharacters.clone();
        this.needingQuoteCharacters = null;
        return this;
    }
    
    public PatternTokenizer setExtraQuotingCharacters(final UnicodeSet syntaxCharacters) {
        this.extraQuotingCharacters = (UnicodeSet)syntaxCharacters.clone();
        this.needingQuoteCharacters = null;
        return this;
    }
    
    public UnicodeSet getEscapeCharacters() {
        return (UnicodeSet)this.escapeCharacters.clone();
    }
    
    public PatternTokenizer setEscapeCharacters(final UnicodeSet escapeCharacters) {
        this.escapeCharacters = (UnicodeSet)escapeCharacters.clone();
        return this;
    }
    
    public boolean isUsingQuote() {
        return this.usingQuote;
    }
    
    public PatternTokenizer setUsingQuote(final boolean usingQuote) {
        this.usingQuote = usingQuote;
        this.needingQuoteCharacters = null;
        return this;
    }
    
    public boolean isUsingSlash() {
        return this.usingSlash;
    }
    
    public PatternTokenizer setUsingSlash(final boolean usingSlash) {
        this.usingSlash = usingSlash;
        this.needingQuoteCharacters = null;
        return this;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public PatternTokenizer setLimit(final int limit) {
        this.limit = limit;
        return this;
    }
    
    public int getStart() {
        return this.start;
    }
    
    public PatternTokenizer setStart(final int start) {
        this.start = start;
        return this;
    }
    
    public PatternTokenizer setPattern(final CharSequence pattern) {
        return this.setPattern(pattern.toString());
    }
    
    public PatternTokenizer setPattern(final String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Inconsistent arguments");
        }
        this.start = 0;
        this.limit = pattern.length();
        this.pattern = pattern;
        return this;
    }
    
    public String quoteLiteral(final CharSequence string) {
        return this.quoteLiteral(string.toString());
    }
    
    public String quoteLiteral(final String string) {
        if (this.needingQuoteCharacters == null) {
            this.needingQuoteCharacters = new UnicodeSet().addAll(this.syntaxCharacters).addAll(this.ignorableCharacters).addAll(this.extraQuotingCharacters);
            if (this.usingSlash) {
                this.needingQuoteCharacters.add(92);
            }
            if (this.usingQuote) {
                this.needingQuoteCharacters.add(39);
            }
        }
        final StringBuffer result = new StringBuffer();
        int quotedChar = PatternTokenizer.NO_QUOTE;
        int cp;
        for (int i = 0; i < string.length(); i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(string, i);
            if (this.escapeCharacters.contains(cp)) {
                if (quotedChar == PatternTokenizer.IN_QUOTE) {
                    result.append('\'');
                    quotedChar = PatternTokenizer.NO_QUOTE;
                }
                this.appendEscaped(result, cp);
            }
            else if (this.needingQuoteCharacters.contains(cp)) {
                if (quotedChar == PatternTokenizer.IN_QUOTE) {
                    UTF16.append(result, cp);
                    if (this.usingQuote && cp == 39) {
                        result.append('\'');
                    }
                }
                else if (this.usingSlash) {
                    result.append('\\');
                    UTF16.append(result, cp);
                }
                else if (this.usingQuote) {
                    if (cp == 39) {
                        result.append('\'');
                        result.append('\'');
                    }
                    else {
                        result.append('\'');
                        UTF16.append(result, cp);
                        quotedChar = PatternTokenizer.IN_QUOTE;
                    }
                }
                else {
                    this.appendEscaped(result, cp);
                }
            }
            else {
                if (quotedChar == PatternTokenizer.IN_QUOTE) {
                    result.append('\'');
                    quotedChar = PatternTokenizer.NO_QUOTE;
                }
                UTF16.append(result, cp);
            }
        }
        if (quotedChar == PatternTokenizer.IN_QUOTE) {
            result.append('\'');
        }
        return result.toString();
    }
    
    private void appendEscaped(final StringBuffer result, final int cp) {
        if (cp <= 65535) {
            result.append("\\u").append(Utility.hex(cp, 4));
        }
        else {
            result.append("\\U").append(Utility.hex(cp, 8));
        }
    }
    
    public String normalize() {
        final int oldStart = this.start;
        final StringBuffer result = new StringBuffer();
        final StringBuffer buffer = new StringBuffer();
        while (true) {
            buffer.setLength(0);
            final int status = this.next(buffer);
            if (status == 0) {
                break;
            }
            if (status != 1) {
                result.append(this.quoteLiteral(buffer));
            }
            else {
                result.append(buffer);
            }
        }
        this.start = oldStart;
        return result.toString();
    }
    
    public int next(final StringBuffer buffer) {
        if (this.start >= this.limit) {
            return 0;
        }
        int status = 5;
        int lastQuote = 5;
        int quoteStatus = 0;
        int hexCount = 0;
        int hexValue = 0;
        int cp;
        for (int i = this.start; i < this.limit; i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(this.pattern, i);
            Label_0554: {
                switch (quoteStatus) {
                    case 3: {
                        switch (cp) {
                            case 117: {
                                quoteStatus = 4;
                                hexCount = 4;
                                hexValue = 0;
                                continue;
                            }
                            case 85: {
                                quoteStatus = 4;
                                hexCount = 8;
                                hexValue = 0;
                                continue;
                            }
                            default: {
                                if (this.usingSlash) {
                                    UTF16.append(buffer, cp);
                                    quoteStatus = 0;
                                    continue;
                                }
                                buffer.append('\\');
                                quoteStatus = 0;
                                break Label_0554;
                            }
                        }
                        break;
                    }
                    case 4: {
                        hexValue <<= 4;
                        hexValue += cp;
                        switch (cp) {
                            case 48:
                            case 49:
                            case 50:
                            case 51:
                            case 52:
                            case 53:
                            case 54:
                            case 55:
                            case 56:
                            case 57: {
                                hexValue -= 48;
                                break;
                            }
                            case 97:
                            case 98:
                            case 99:
                            case 100:
                            case 101:
                            case 102: {
                                hexValue -= 87;
                                break;
                            }
                            case 65:
                            case 66:
                            case 67:
                            case 68:
                            case 69:
                            case 70: {
                                hexValue -= 55;
                                break;
                            }
                            default: {
                                this.start = i;
                                return 4;
                            }
                        }
                        if (--hexCount == 0) {
                            quoteStatus = 0;
                            UTF16.append(buffer, hexValue);
                        }
                        continue;
                    }
                    case -1: {
                        if (cp == lastQuote) {
                            UTF16.append(buffer, cp);
                            quoteStatus = 2;
                            continue;
                        }
                        quoteStatus = 0;
                        break;
                    }
                    case 1: {
                        if (cp == lastQuote) {
                            UTF16.append(buffer, cp);
                            quoteStatus = 0;
                            continue;
                        }
                        UTF16.append(buffer, cp);
                        quoteStatus = 2;
                        continue;
                    }
                    case 2: {
                        if (cp == lastQuote) {
                            quoteStatus = -1;
                            continue;
                        }
                        UTF16.append(buffer, cp);
                        continue;
                    }
                }
            }
            if (!this.ignorableCharacters.contains(cp)) {
                if (this.syntaxCharacters.contains(cp)) {
                    if (status == 5) {
                        UTF16.append(buffer, cp);
                        this.start = i + UTF16.getCharCount(cp);
                        return 1;
                    }
                    this.start = i;
                    return status;
                }
                else {
                    status = 2;
                    if (cp == 92) {
                        quoteStatus = 3;
                    }
                    else if (this.usingQuote && cp == 39) {
                        lastQuote = cp;
                        quoteStatus = 1;
                    }
                    else {
                        UTF16.append(buffer, cp);
                    }
                }
            }
        }
        this.start = this.limit;
        switch (quoteStatus) {
            case 4: {
                status = 4;
                break;
            }
            case 3: {
                if (this.usingSlash) {
                    status = 4;
                    break;
                }
                buffer.append('\\');
                break;
            }
            case 1:
            case 2: {
                status = 3;
                break;
            }
        }
        return status;
    }
    
    static {
        PatternTokenizer.NO_QUOTE = -1;
        PatternTokenizer.IN_QUOTE = -2;
    }
}
