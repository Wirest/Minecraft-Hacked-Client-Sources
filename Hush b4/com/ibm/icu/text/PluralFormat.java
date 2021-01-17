// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import com.ibm.icu.impl.Utility;
import java.text.ParsePosition;
import java.text.FieldPosition;
import java.util.Map;
import com.ibm.icu.util.ULocale;

public class PluralFormat extends UFormat
{
    private static final long serialVersionUID = 1L;
    private ULocale ulocale;
    private PluralRules pluralRules;
    private String pattern;
    private transient MessagePattern msgPattern;
    private Map<String, String> parsedValues;
    private NumberFormat numberFormat;
    private transient double offset;
    private transient PluralSelectorAdapter pluralRulesWrapper;
    
    public PluralFormat() {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(null, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public PluralFormat(final ULocale ulocale) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(null, PluralRules.PluralType.CARDINAL, ulocale);
    }
    
    public PluralFormat(final PluralRules rules) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(rules, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public PluralFormat(final ULocale ulocale, final PluralRules rules) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(rules, PluralRules.PluralType.CARDINAL, ulocale);
    }
    
    public PluralFormat(final ULocale ulocale, final PluralRules.PluralType type) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(null, type, ulocale);
    }
    
    public PluralFormat(final String pattern) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(null, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
        this.applyPattern(pattern);
    }
    
    public PluralFormat(final ULocale ulocale, final String pattern) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(null, PluralRules.PluralType.CARDINAL, ulocale);
        this.applyPattern(pattern);
    }
    
    public PluralFormat(final PluralRules rules, final String pattern) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(rules, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
        this.applyPattern(pattern);
    }
    
    public PluralFormat(final ULocale ulocale, final PluralRules rules, final String pattern) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(rules, PluralRules.PluralType.CARDINAL, ulocale);
        this.applyPattern(pattern);
    }
    
    public PluralFormat(final ULocale ulocale, final PluralRules.PluralType type, final String pattern) {
        this.ulocale = null;
        this.pluralRules = null;
        this.pattern = null;
        this.parsedValues = null;
        this.numberFormat = null;
        this.offset = 0.0;
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.init(null, type, ulocale);
        this.applyPattern(pattern);
    }
    
    private void init(final PluralRules rules, final PluralRules.PluralType type, final ULocale locale) {
        this.ulocale = locale;
        this.pluralRules = ((rules == null) ? PluralRules.forLocale(this.ulocale, type) : rules);
        this.resetPattern();
        this.numberFormat = NumberFormat.getInstance(this.ulocale);
    }
    
    private void resetPattern() {
        this.pattern = null;
        if (this.msgPattern != null) {
            this.msgPattern.clear();
        }
        this.offset = 0.0;
    }
    
    public void applyPattern(final String pattern) {
        this.pattern = pattern;
        if (this.msgPattern == null) {
            this.msgPattern = new MessagePattern();
        }
        try {
            this.msgPattern.parsePluralStyle(pattern);
            this.offset = this.msgPattern.getPluralOffset(0);
        }
        catch (RuntimeException e) {
            this.resetPattern();
            throw e;
        }
    }
    
    public String toPattern() {
        return this.pattern;
    }
    
    static int findSubMessage(final MessagePattern pattern, int partIndex, final PluralSelector selector, final double number) {
        final int count = pattern.countParts();
        MessagePattern.Part part = pattern.getPart(partIndex);
        double offset;
        if (part.getType().hasNumericValue()) {
            offset = pattern.getNumericValue(part);
            ++partIndex;
        }
        else {
            offset = 0.0;
        }
        String keyword = null;
        boolean haveKeywordMatch = false;
        int msgStart = 0;
        do {
            part = pattern.getPart(partIndex++);
            final MessagePattern.Part.Type type = part.getType();
            if (type == MessagePattern.Part.Type.ARG_LIMIT) {
                break;
            }
            assert type == MessagePattern.Part.Type.ARG_SELECTOR;
            if (pattern.getPartType(partIndex).hasNumericValue()) {
                part = pattern.getPart(partIndex++);
                if (number == pattern.getNumericValue(part)) {
                    return partIndex;
                }
            }
            else if (!haveKeywordMatch) {
                if (pattern.partSubstringMatches(part, "other")) {
                    if (msgStart == 0) {
                        msgStart = partIndex;
                        if (keyword != null && keyword.equals("other")) {
                            haveKeywordMatch = true;
                        }
                    }
                }
                else {
                    if (keyword == null) {
                        keyword = selector.select(number - offset);
                        if (msgStart != 0 && keyword.equals("other")) {
                            haveKeywordMatch = true;
                        }
                    }
                    if (!haveKeywordMatch && pattern.partSubstringMatches(part, keyword)) {
                        msgStart = partIndex;
                        haveKeywordMatch = true;
                    }
                }
            }
            partIndex = pattern.getLimitPartIndex(partIndex);
        } while (++partIndex < count);
        return msgStart;
    }
    
    public final String format(double number) {
        if (this.msgPattern == null || this.msgPattern.countParts() == 0) {
            return this.numberFormat.format(number);
        }
        int partIndex = findSubMessage(this.msgPattern, 0, this.pluralRulesWrapper, number);
        number -= this.offset;
        StringBuilder result = null;
        int prevIndex = this.msgPattern.getPart(partIndex).getLimit();
        int index;
        while (true) {
            final MessagePattern.Part part = this.msgPattern.getPart(++partIndex);
            final MessagePattern.Part.Type type = part.getType();
            index = part.getIndex();
            if (type == MessagePattern.Part.Type.MSG_LIMIT) {
                break;
            }
            if (type == MessagePattern.Part.Type.REPLACE_NUMBER || (type == MessagePattern.Part.Type.SKIP_SYNTAX && this.msgPattern.jdkAposMode())) {
                if (result == null) {
                    result = new StringBuilder();
                }
                result.append(this.pattern, prevIndex, index);
                if (type == MessagePattern.Part.Type.REPLACE_NUMBER) {
                    result.append(this.numberFormat.format(number));
                }
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
                partIndex = this.msgPattern.getLimitPartIndex(partIndex);
                index = this.msgPattern.getPart(partIndex).getLimit();
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
    public StringBuffer format(final Object number, final StringBuffer toAppendTo, final FieldPosition pos) {
        if (number instanceof Number) {
            toAppendTo.append(this.format(((Number)number).doubleValue()));
            return toAppendTo;
        }
        throw new IllegalArgumentException("'" + number + "' is not a Number");
    }
    
    public Number parse(final String text, final ParsePosition parsePosition) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public void setLocale(ULocale ulocale) {
        if (ulocale == null) {
            ulocale = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        this.init(null, PluralRules.PluralType.CARDINAL, ulocale);
    }
    
    public void setNumberFormat(final NumberFormat format) {
        this.numberFormat = format;
    }
    
    @Override
    public boolean equals(final Object rhs) {
        if (this == rhs) {
            return true;
        }
        if (rhs == null || this.getClass() != rhs.getClass()) {
            return false;
        }
        final PluralFormat pf = (PluralFormat)rhs;
        return Utility.objectEquals(this.ulocale, pf.ulocale) && Utility.objectEquals(this.pluralRules, pf.pluralRules) && Utility.objectEquals(this.msgPattern, pf.msgPattern) && Utility.objectEquals(this.numberFormat, pf.numberFormat);
    }
    
    public boolean equals(final PluralFormat rhs) {
        return this.equals((Object)rhs);
    }
    
    @Override
    public int hashCode() {
        return this.pluralRules.hashCode() ^ this.parsedValues.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("locale=" + this.ulocale);
        buf.append(", rules='" + this.pluralRules + "'");
        buf.append(", pattern='" + this.pattern + "'");
        buf.append(", format='" + this.numberFormat + "'");
        return buf.toString();
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.parsedValues = null;
        if (this.pattern != null) {
            this.applyPattern(this.pattern);
        }
    }
    
    private final class PluralSelectorAdapter implements PluralSelector
    {
        public String select(final double number) {
            return PluralFormat.this.pluralRules.select(number);
        }
    }
    
    interface PluralSelector
    {
        String select(final double p0);
    }
}
