// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;

abstract class NFSubstitution
{
    int pos;
    NFRuleSet ruleSet;
    DecimalFormat numberFormat;
    RuleBasedNumberFormat rbnf;
    
    public static NFSubstitution makeSubstitution(final int pos, final NFRule rule, final NFRule rulePredecessor, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        if (description.length() == 0) {
            return new NullSubstitution(pos, ruleSet, formatter, description);
        }
        switch (description.charAt(0)) {
            case '<': {
                if (rule.getBaseValue() == -1L) {
                    throw new IllegalArgumentException("<< not allowed in negative-number rule");
                }
                if (rule.getBaseValue() == -2L || rule.getBaseValue() == -3L || rule.getBaseValue() == -4L) {
                    return new IntegralPartSubstitution(pos, ruleSet, formatter, description);
                }
                if (ruleSet.isFractionSet()) {
                    return new NumeratorSubstitution(pos, (double)rule.getBaseValue(), formatter.getDefaultRuleSet(), formatter, description);
                }
                return new MultiplierSubstitution(pos, rule.getDivisor(), ruleSet, formatter, description);
            }
            case '>': {
                if (rule.getBaseValue() == -1L) {
                    return new AbsoluteValueSubstitution(pos, ruleSet, formatter, description);
                }
                if (rule.getBaseValue() == -2L || rule.getBaseValue() == -3L || rule.getBaseValue() == -4L) {
                    return new FractionalPartSubstitution(pos, ruleSet, formatter, description);
                }
                if (ruleSet.isFractionSet()) {
                    throw new IllegalArgumentException(">> not allowed in fraction rule set");
                }
                return new ModulusSubstitution(pos, rule.getDivisor(), rulePredecessor, ruleSet, formatter, description);
            }
            case '=': {
                return new SameValueSubstitution(pos, ruleSet, formatter, description);
            }
            default: {
                throw new IllegalArgumentException("Illegal substitution character");
            }
        }
    }
    
    NFSubstitution(final int pos, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, String description) {
        this.ruleSet = null;
        this.numberFormat = null;
        this.rbnf = null;
        this.pos = pos;
        this.rbnf = formatter;
        if (description.length() >= 2 && description.charAt(0) == description.charAt(description.length() - 1)) {
            description = description.substring(1, description.length() - 1);
        }
        else if (description.length() != 0) {
            throw new IllegalArgumentException("Illegal substitution syntax");
        }
        if (description.length() == 0) {
            this.ruleSet = ruleSet;
        }
        else if (description.charAt(0) == '%') {
            this.ruleSet = formatter.findRuleSet(description);
        }
        else if (description.charAt(0) == '#' || description.charAt(0) == '0') {
            (this.numberFormat = new DecimalFormat(description)).setDecimalFormatSymbols(formatter.getDecimalFormatSymbols());
        }
        else {
            if (description.charAt(0) != '>') {
                throw new IllegalArgumentException("Illegal substitution syntax");
            }
            this.ruleSet = ruleSet;
            this.numberFormat = null;
        }
    }
    
    public void setDivisor(final int radix, final int exponent) {
    }
    
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }
        if (this.getClass() == that.getClass()) {
            final NFSubstitution that2 = (NFSubstitution)that;
            return this.pos == that2.pos && (this.ruleSet != null || that2.ruleSet == null) && ((this.numberFormat != null) ? this.numberFormat.equals(that2.numberFormat) : (that2.numberFormat == null));
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
        if (this.ruleSet != null) {
            return this.tokenChar() + this.ruleSet.getName() + this.tokenChar();
        }
        return this.tokenChar() + this.numberFormat.toPattern() + this.tokenChar();
    }
    
    public void doSubstitution(final long number, final StringBuffer toInsertInto, final int position) {
        if (this.ruleSet != null) {
            final long numberToFormat = this.transformNumber(number);
            this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos);
        }
        else {
            double numberToFormat2 = this.transformNumber((double)number);
            if (this.numberFormat.getMaximumFractionDigits() == 0) {
                numberToFormat2 = Math.floor(numberToFormat2);
            }
            toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat2));
        }
    }
    
    public void doSubstitution(final double number, final StringBuffer toInsertInto, final int position) {
        final double numberToFormat = this.transformNumber(number);
        if (numberToFormat == Math.floor(numberToFormat) && this.ruleSet != null) {
            this.ruleSet.format((long)numberToFormat, toInsertInto, position + this.pos);
        }
        else if (this.ruleSet != null) {
            this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos);
        }
        else {
            toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
        }
    }
    
    public abstract long transformNumber(final long p0);
    
    public abstract double transformNumber(final double p0);
    
    public Number doParse(final String text, final ParsePosition parsePosition, final double baseValue, double upperBound, final boolean lenientParse) {
        upperBound = this.calcUpperBound(upperBound);
        Number tempResult;
        if (this.ruleSet != null) {
            tempResult = this.ruleSet.parse(text, parsePosition, upperBound);
            if (lenientParse && !this.ruleSet.isFractionSet() && parsePosition.getIndex() == 0) {
                tempResult = this.rbnf.getDecimalFormat().parse(text, parsePosition);
            }
        }
        else {
            tempResult = this.numberFormat.parse(text, parsePosition);
        }
        if (parsePosition.getIndex() == 0) {
            return tempResult;
        }
        double result = tempResult.doubleValue();
        result = this.composeRuleValue(result, baseValue);
        if (result == (long)result) {
            return (long)result;
        }
        return new Double(result);
    }
    
    public abstract double composeRuleValue(final double p0, final double p1);
    
    public abstract double calcUpperBound(final double p0);
    
    public final int getPos() {
        return this.pos;
    }
    
    abstract char tokenChar();
    
    public boolean isNullSubstitution() {
        return false;
    }
    
    public boolean isModulusSubstitution() {
        return false;
    }
}
