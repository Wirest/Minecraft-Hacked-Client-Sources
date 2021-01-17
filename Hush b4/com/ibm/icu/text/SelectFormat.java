// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParsePosition;
import java.text.FieldPosition;
import com.ibm.icu.impl.PatternProps;
import java.text.Format;

public class SelectFormat extends Format
{
    private static final long serialVersionUID = 2993154333257524984L;
    private String pattern;
    private transient MessagePattern msgPattern;
    
    public SelectFormat(final String pattern) {
        this.pattern = null;
        this.applyPattern(pattern);
    }
    
    private void reset() {
        this.pattern = null;
        if (this.msgPattern != null) {
            this.msgPattern.clear();
        }
    }
    
    public void applyPattern(final String pattern) {
        this.pattern = pattern;
        if (this.msgPattern == null) {
            this.msgPattern = new MessagePattern();
        }
        try {
            this.msgPattern.parseSelectStyle(pattern);
        }
        catch (RuntimeException e) {
            this.reset();
            throw e;
        }
    }
    
    public String toPattern() {
        return this.pattern;
    }
    
    static int findSubMessage(final MessagePattern pattern, int partIndex, final String keyword) {
        final int count = pattern.countParts();
        int msgStart = 0;
        do {
            final MessagePattern.Part part = pattern.getPart(partIndex++);
            final MessagePattern.Part.Type type = part.getType();
            if (type == MessagePattern.Part.Type.ARG_LIMIT) {
                break;
            }
            assert type == MessagePattern.Part.Type.ARG_SELECTOR;
            if (pattern.partSubstringMatches(part, keyword)) {
                return partIndex;
            }
            if (msgStart == 0 && pattern.partSubstringMatches(part, "other")) {
                msgStart = partIndex;
            }
            partIndex = pattern.getLimitPartIndex(partIndex);
        } while (++partIndex < count);
        return msgStart;
    }
    
    public final String format(final String keyword) {
        if (!PatternProps.isIdentifier(keyword)) {
            throw new IllegalArgumentException("Invalid formatting argument.");
        }
        if (this.msgPattern == null || this.msgPattern.countParts() == 0) {
            throw new IllegalStateException("Invalid format error.");
        }
        final int msgStart = findSubMessage(this.msgPattern, 0, keyword);
        if (!this.msgPattern.jdkAposMode()) {
            final int msgLimit = this.msgPattern.getLimitPartIndex(msgStart);
            return this.msgPattern.getPatternString().substring(this.msgPattern.getPart(msgStart).getLimit(), this.msgPattern.getPatternIndex(msgLimit));
        }
        StringBuilder result = null;
        int prevIndex = this.msgPattern.getPart(msgStart).getLimit();
        int i = msgStart;
        int index;
        while (true) {
            final MessagePattern.Part part = this.msgPattern.getPart(++i);
            final MessagePattern.Part.Type type = part.getType();
            index = part.getIndex();
            if (type == MessagePattern.Part.Type.MSG_LIMIT) {
                break;
            }
            if (type == MessagePattern.Part.Type.SKIP_SYNTAX) {
                if (result == null) {
                    result = new StringBuilder();
                }
                result.append(this.pattern, prevIndex, index);
                prevIndex = part.getLimit();
            }
            else {
                if (type != MessagePattern.Part.Type.ARG_START) {
                    continue;
                }
                if (result == null) {
                    result = new StringBuilder();
                }
                result.append(this.pattern, prevIndex, index);
                prevIndex = index;
                i = this.msgPattern.getLimitPartIndex(i);
                index = this.msgPattern.getPart(i).getLimit();
                MessagePattern.appendReducedApostrophes(this.pattern, prevIndex, index, result);
                prevIndex = index;
            }
        }
        if (result == null) {
            return this.pattern.substring(prevIndex, index);
        }
        return result.append(this.pattern, prevIndex, index).toString();
    }
    
    @Override
    public StringBuffer format(final Object keyword, final StringBuffer toAppendTo, final FieldPosition pos) {
        if (keyword instanceof String) {
            toAppendTo.append(this.format((String)keyword));
            return toAppendTo;
        }
        throw new IllegalArgumentException("'" + keyword + "' is not a String");
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final SelectFormat sf = (SelectFormat)obj;
        return (this.msgPattern == null) ? (sf.msgPattern == null) : this.msgPattern.equals(sf.msgPattern);
    }
    
    @Override
    public int hashCode() {
        if (this.pattern != null) {
            return this.pattern.hashCode();
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "pattern='" + this.pattern + "'";
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (this.pattern != null) {
            this.applyPattern(this.pattern);
        }
    }
}
