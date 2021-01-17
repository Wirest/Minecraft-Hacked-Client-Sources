// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;
import com.ibm.icu.impl.PatternProps;

final class NFRule
{
    public static final int NEGATIVE_NUMBER_RULE = -1;
    public static final int IMPROPER_FRACTION_RULE = -2;
    public static final int PROPER_FRACTION_RULE = -3;
    public static final int MASTER_RULE = -4;
    private long baseValue;
    private int radix;
    private short exponent;
    private String ruleText;
    private NFSubstitution sub1;
    private NFSubstitution sub2;
    private RuleBasedNumberFormat formatter;
    
    public static Object makeRules(String description, final NFRuleSet owner, final NFRule predecessor, final RuleBasedNumberFormat ownersOwner) {
        final NFRule rule1 = new NFRule(ownersOwner);
        description = rule1.parseRuleDescriptor(description);
        final int brack1 = description.indexOf("[");
        final int brack2 = description.indexOf("]");
        if (brack1 == -1 || brack2 == -1 || brack1 > brack2 || rule1.getBaseValue() == -3L || rule1.getBaseValue() == -1L) {
            rule1.ruleText = description;
            rule1.extractSubstitutions(owner, predecessor, ownersOwner);
            return rule1;
        }
        NFRule rule2 = null;
        final StringBuilder sbuf = new StringBuilder();
        if ((rule1.baseValue > 0L && rule1.baseValue % Math.pow(rule1.radix, rule1.exponent) == 0.0) || rule1.baseValue == -2L || rule1.baseValue == -4L) {
            rule2 = new NFRule(ownersOwner);
            if (rule1.baseValue >= 0L) {
                rule2.baseValue = rule1.baseValue;
                if (!owner.isFractionSet()) {
                    final NFRule nfRule = rule1;
                    ++nfRule.baseValue;
                }
            }
            else if (rule1.baseValue == -2L) {
                rule2.baseValue = -3L;
            }
            else if (rule1.baseValue == -4L) {
                rule2.baseValue = rule1.baseValue;
                rule1.baseValue = -2L;
            }
            rule2.radix = rule1.radix;
            rule2.exponent = rule1.exponent;
            sbuf.append(description.substring(0, brack1));
            if (brack2 + 1 < description.length()) {
                sbuf.append(description.substring(brack2 + 1));
            }
            rule2.ruleText = sbuf.toString();
            rule2.extractSubstitutions(owner, predecessor, ownersOwner);
        }
        sbuf.setLength(0);
        sbuf.append(description.substring(0, brack1));
        sbuf.append(description.substring(brack1 + 1, brack2));
        if (brack2 + 1 < description.length()) {
            sbuf.append(description.substring(brack2 + 1));
        }
        rule1.ruleText = sbuf.toString();
        rule1.extractSubstitutions(owner, predecessor, ownersOwner);
        if (rule2 == null) {
            return rule1;
        }
        return new NFRule[] { rule2, rule1 };
    }
    
    public NFRule(final RuleBasedNumberFormat formatter) {
        this.radix = 10;
        this.exponent = 0;
        this.ruleText = null;
        this.sub1 = null;
        this.sub2 = null;
        this.formatter = null;
        this.formatter = formatter;
    }
    
    private String parseRuleDescriptor(String description) {
        int p = description.indexOf(":");
        if (p == -1) {
            this.setBaseValue(0L);
        }
        else {
            final String descriptor = description.substring(0, p);
            ++p;
            while (p < description.length() && PatternProps.isWhiteSpace(description.charAt(p))) {
                ++p;
            }
            description = description.substring(p);
            if (descriptor.equals("-x")) {
                this.setBaseValue(-1L);
            }
            else if (descriptor.equals("x.x")) {
                this.setBaseValue(-2L);
            }
            else if (descriptor.equals("0.x")) {
                this.setBaseValue(-3L);
            }
            else if (descriptor.equals("x.0")) {
                this.setBaseValue(-4L);
            }
            else if (descriptor.charAt(0) >= '0' && descriptor.charAt(0) <= '9') {
                final StringBuilder tempValue = new StringBuilder();
                p = 0;
                char c = ' ';
                while (p < descriptor.length()) {
                    c = descriptor.charAt(p);
                    if (c >= '0' && c <= '9') {
                        tempValue.append(c);
                    }
                    else {
                        if (c == '/') {
                            break;
                        }
                        if (c == '>') {
                            break;
                        }
                        if (!PatternProps.isWhiteSpace(c) && c != ',') {
                            if (c != '.') {
                                throw new IllegalArgumentException("Illegal character in rule descriptor");
                            }
                        }
                    }
                    ++p;
                }
                this.setBaseValue(Long.parseLong(tempValue.toString()));
                if (c == '/') {
                    tempValue.setLength(0);
                    ++p;
                    while (p < descriptor.length()) {
                        c = descriptor.charAt(p);
                        if (c >= '0' && c <= '9') {
                            tempValue.append(c);
                        }
                        else {
                            if (c == '>') {
                                break;
                            }
                            if (!PatternProps.isWhiteSpace(c) && c != ',') {
                                if (c != '.') {
                                    throw new IllegalArgumentException("Illegal character is rule descriptor");
                                }
                            }
                        }
                        ++p;
                    }
                    this.radix = Integer.parseInt(tempValue.toString());
                    if (this.radix == 0) {
                        throw new IllegalArgumentException("Rule can't have radix of 0");
                    }
                    this.exponent = this.expectedExponent();
                }
                if (c == '>') {
                    while (p < descriptor.length()) {
                        c = descriptor.charAt(p);
                        if (c != '>' || this.exponent <= 0) {
                            throw new IllegalArgumentException("Illegal character in rule descriptor");
                        }
                        --this.exponent;
                        ++p;
                    }
                }
            }
        }
        if (description.length() > 0 && description.charAt(0) == '\'') {
            description = description.substring(1);
        }
        return description;
    }
    
    private void extractSubstitutions(final NFRuleSet owner, final NFRule predecessor, final RuleBasedNumberFormat ownersOwner) {
        this.sub1 = this.extractSubstitution(owner, predecessor, ownersOwner);
        this.sub2 = this.extractSubstitution(owner, predecessor, ownersOwner);
    }
    
    private NFSubstitution extractSubstitution(final NFRuleSet owner, final NFRule predecessor, final RuleBasedNumberFormat ownersOwner) {
        NFSubstitution result = null;
        final int subStart = this.indexOfAny(new String[] { "<<", "<%", "<#", "<0", ">>", ">%", ">#", ">0", "=%", "=#", "=0" });
        if (subStart == -1) {
            return NFSubstitution.makeSubstitution(this.ruleText.length(), this, predecessor, owner, ownersOwner, "");
        }
        int subEnd;
        if (this.ruleText.substring(subStart).startsWith(">>>")) {
            subEnd = subStart + 2;
        }
        else {
            final char c = this.ruleText.charAt(subStart);
            subEnd = this.ruleText.indexOf(c, subStart + 1);
            if (c == '<' && subEnd != -1 && subEnd < this.ruleText.length() - 1 && this.ruleText.charAt(subEnd + 1) == c) {
                ++subEnd;
            }
        }
        if (subEnd == -1) {
            return NFSubstitution.makeSubstitution(this.ruleText.length(), this, predecessor, owner, ownersOwner, "");
        }
        result = NFSubstitution.makeSubstitution(subStart, this, predecessor, owner, ownersOwner, this.ruleText.substring(subStart, subEnd + 1));
        this.ruleText = this.ruleText.substring(0, subStart) + this.ruleText.substring(subEnd + 1);
        return result;
    }
    
    public final void setBaseValue(final long newBaseValue) {
        this.baseValue = newBaseValue;
        if (this.baseValue >= 1L) {
            this.radix = 10;
            this.exponent = this.expectedExponent();
            if (this.sub1 != null) {
                this.sub1.setDivisor(this.radix, this.exponent);
            }
            if (this.sub2 != null) {
                this.sub2.setDivisor(this.radix, this.exponent);
            }
        }
        else {
            this.radix = 10;
            this.exponent = 0;
        }
    }
    
    private short expectedExponent() {
        if (this.radix == 0 || this.baseValue < 1L) {
            return 0;
        }
        final short tempResult = (short)(Math.log((double)this.baseValue) / Math.log(this.radix));
        if (Math.pow(this.radix, tempResult + 1) <= this.baseValue) {
            return (short)(tempResult + 1);
        }
        return tempResult;
    }
    
    private int indexOfAny(final String[] strings) {
        int result = -1;
        for (int i = 0; i < strings.length; ++i) {
            final int pos = this.ruleText.indexOf(strings[i]);
            if (pos != -1 && (result == -1 || pos < result)) {
                result = pos;
            }
        }
        return result;
    }
    
    @Override
    public boolean equals(final Object that) {
        if (that instanceof NFRule) {
            final NFRule that2 = (NFRule)that;
            return this.baseValue == that2.baseValue && this.radix == that2.radix && this.exponent == that2.exponent && this.ruleText.equals(that2.ruleText) && this.sub1.equals(that2.sub1) && this.sub2.equals(that2.sub2);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        if (this.baseValue == -1L) {
            result.append("-x: ");
        }
        else if (this.baseValue == -2L) {
            result.append("x.x: ");
        }
        else if (this.baseValue == -3L) {
            result.append("0.x: ");
        }
        else if (this.baseValue == -4L) {
            result.append("x.0: ");
        }
        else {
            result.append(String.valueOf(this.baseValue));
            if (this.radix != 10) {
                result.append('/');
                result.append(String.valueOf(this.radix));
            }
            for (int numCarets = this.expectedExponent() - this.exponent, i = 0; i < numCarets; ++i) {
                result.append('>');
            }
            result.append(": ");
        }
        if (this.ruleText.startsWith(" ") && (this.sub1 == null || this.sub1.getPos() != 0)) {
            result.append("'");
        }
        final StringBuilder ruleTextCopy = new StringBuilder(this.ruleText);
        ruleTextCopy.insert(this.sub2.getPos(), this.sub2.toString());
        ruleTextCopy.insert(this.sub1.getPos(), this.sub1.toString());
        result.append(ruleTextCopy.toString());
        result.append(';');
        return result.toString();
    }
    
    public final long getBaseValue() {
        return this.baseValue;
    }
    
    public double getDivisor() {
        return Math.pow(this.radix, this.exponent);
    }
    
    public void doFormat(final long number, final StringBuffer toInsertInto, final int pos) {
        toInsertInto.insert(pos, this.ruleText);
        this.sub2.doSubstitution(number, toInsertInto, pos);
        this.sub1.doSubstitution(number, toInsertInto, pos);
    }
    
    public void doFormat(final double number, final StringBuffer toInsertInto, final int pos) {
        toInsertInto.insert(pos, this.ruleText);
        this.sub2.doSubstitution(number, toInsertInto, pos);
        this.sub1.doSubstitution(number, toInsertInto, pos);
    }
    
    public boolean shouldRollBack(final double number) {
        return (this.sub1.isModulusSubstitution() || this.sub2.isModulusSubstitution()) && number % Math.pow(this.radix, this.exponent) == 0.0 && this.baseValue % Math.pow(this.radix, this.exponent) != 0.0;
    }
    
    public Number doParse(final String text, final ParsePosition parsePosition, final boolean isFractionRule, final double upperBound) {
        final ParsePosition pp = new ParsePosition(0);
        final String workText = this.stripPrefix(text, this.ruleText.substring(0, this.sub1.getPos()), pp);
        final int prefixLength = text.length() - workText.length();
        if (pp.getIndex() == 0 && this.sub1.getPos() != 0) {
            return 0L;
        }
        int highWaterMark = 0;
        double result = 0.0;
        int start = 0;
        final double tempBaseValue = (double)Math.max(0L, this.baseValue);
        do {
            pp.setIndex(0);
            double partialResult = this.matchToDelimiter(workText, start, tempBaseValue, this.ruleText.substring(this.sub1.getPos(), this.sub2.getPos()), pp, this.sub1, upperBound).doubleValue();
            if (pp.getIndex() != 0 || this.sub1.isNullSubstitution()) {
                start = pp.getIndex();
                final String workText2 = workText.substring(pp.getIndex());
                final ParsePosition pp2 = new ParsePosition(0);
                partialResult = this.matchToDelimiter(workText2, 0, partialResult, this.ruleText.substring(this.sub2.getPos()), pp2, this.sub2, upperBound).doubleValue();
                if ((pp2.getIndex() == 0 && !this.sub2.isNullSubstitution()) || prefixLength + pp.getIndex() + pp2.getIndex() <= highWaterMark) {
                    continue;
                }
                highWaterMark = prefixLength + pp.getIndex() + pp2.getIndex();
                result = partialResult;
            }
        } while (this.sub1.getPos() != this.sub2.getPos() && pp.getIndex() > 0 && pp.getIndex() < workText.length() && pp.getIndex() != start);
        parsePosition.setIndex(highWaterMark);
        if (isFractionRule && highWaterMark > 0 && this.sub1.isNullSubstitution()) {
            result = 1.0 / result;
        }
        if (result == (long)result) {
            return (long)result;
        }
        return new Double(result);
    }
    
    private String stripPrefix(final String text, final String prefix, final ParsePosition pp) {
        if (prefix.length() == 0) {
            return text;
        }
        final int pfl = this.prefixLength(text, prefix);
        if (pfl != 0) {
            pp.setIndex(pp.getIndex() + pfl);
            return text.substring(pfl);
        }
        return text;
    }
    
    private Number matchToDelimiter(final String text, final int startPos, final double baseVal, final String delimiter, final ParsePosition pp, final NFSubstitution sub, final double upperBound) {
        if (!this.allIgnorable(delimiter)) {
            final ParsePosition tempPP = new ParsePosition(0);
            int[] temp = this.findText(text, delimiter, startPos);
            int dPos = temp[0];
            int dLen = temp[1];
            while (dPos >= 0) {
                final String subText = text.substring(0, dPos);
                if (subText.length() > 0) {
                    final Number tempResult = sub.doParse(subText, tempPP, baseVal, upperBound, this.formatter.lenientParseEnabled());
                    if (tempPP.getIndex() == dPos) {
                        pp.setIndex(dPos + dLen);
                        return tempResult;
                    }
                }
                tempPP.setIndex(0);
                temp = this.findText(text, delimiter, dPos + dLen);
                dPos = temp[0];
                dLen = temp[1];
            }
            pp.setIndex(0);
            return 0L;
        }
        final ParsePosition tempPP = new ParsePosition(0);
        Number result = 0L;
        final Number tempResult2 = sub.doParse(text, tempPP, baseVal, upperBound, this.formatter.lenientParseEnabled());
        if (tempPP.getIndex() != 0 || sub.isNullSubstitution()) {
            pp.setIndex(tempPP.getIndex());
            if (tempResult2 != null) {
                result = tempResult2;
            }
        }
        return result;
    }
    
    private int prefixLength(final String str, final String prefix) {
        if (prefix.length() == 0) {
            return 0;
        }
        final RbnfLenientScanner scanner = this.formatter.getLenientScanner();
        if (scanner != null) {
            return scanner.prefixLength(str, prefix);
        }
        if (str.startsWith(prefix)) {
            return prefix.length();
        }
        return 0;
    }
    
    private int[] findText(final String str, final String key, final int startingAt) {
        final RbnfLenientScanner scanner = this.formatter.getLenientScanner();
        if (scanner == null) {
            return new int[] { str.indexOf(key, startingAt), key.length() };
        }
        return scanner.findText(str, key, startingAt);
    }
    
    private boolean allIgnorable(final String str) {
        if (str.length() == 0) {
            return true;
        }
        final RbnfLenientScanner scanner = this.formatter.getLenientScanner();
        return scanner != null && scanner.allIgnorable(str);
    }
}
