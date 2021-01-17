// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;

class NumeratorSubstitution extends NFSubstitution
{
    double denominator;
    boolean withZeros;
    
    NumeratorSubstitution(final int pos, final double denominator, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        super(pos, ruleSet, formatter, fixdesc(description));
        this.denominator = denominator;
        this.withZeros = description.endsWith("<<");
    }
    
    static String fixdesc(final String description) {
        return description.endsWith("<<") ? description.substring(0, description.length() - 1) : description;
    }
    
    @Override
    public boolean equals(final Object that) {
        if (super.equals(that)) {
            final NumeratorSubstitution that2 = (NumeratorSubstitution)that;
            return this.denominator == that2.denominator;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    @Override
    public void doSubstitution(final double number, final StringBuffer toInsertInto, int position) {
        final double numberToFormat = this.transformNumber(number);
        if (this.withZeros && this.ruleSet != null) {
            long nf = (long)numberToFormat;
            final int len = toInsertInto.length();
            while ((nf *= 10L) < this.denominator) {
                toInsertInto.insert(position + this.pos, ' ');
                this.ruleSet.format(0L, toInsertInto, position + this.pos);
            }
            position += toInsertInto.length() - len;
        }
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
    
    @Override
    public long transformNumber(final long number) {
        return Math.round(number * this.denominator);
    }
    
    @Override
    public double transformNumber(final double number) {
        return (double)Math.round(number * this.denominator);
    }
    
    @Override
    public Number doParse(String text, final ParsePosition parsePosition, final double baseValue, final double upperBound, final boolean lenientParse) {
        int zeroCount = 0;
        if (this.withZeros) {
            String workText = text;
            final ParsePosition workPos = new ParsePosition(1);
            while (workText.length() > 0 && workPos.getIndex() != 0) {
                workPos.setIndex(0);
                this.ruleSet.parse(workText, workPos, 1.0).intValue();
                if (workPos.getIndex() == 0) {
                    break;
                }
                ++zeroCount;
                parsePosition.setIndex(parsePosition.getIndex() + workPos.getIndex());
                workText = workText.substring(workPos.getIndex());
                while (workText.length() > 0 && workText.charAt(0) == ' ') {
                    workText = workText.substring(1);
                    parsePosition.setIndex(parsePosition.getIndex() + 1);
                }
            }
            text = text.substring(parsePosition.getIndex());
            parsePosition.setIndex(0);
        }
        Number result = super.doParse(text, parsePosition, this.withZeros ? 1.0 : baseValue, upperBound, false);
        if (this.withZeros) {
            long n;
            long d;
            for (n = result.longValue(), d = 1L; d <= n; d *= 10L) {}
            while (zeroCount > 0) {
                d *= 10L;
                --zeroCount;
            }
            result = new Double(n / (double)d);
        }
        return result;
    }
    
    @Override
    public double composeRuleValue(final double newRuleValue, final double oldRuleValue) {
        return newRuleValue / oldRuleValue;
    }
    
    @Override
    public double calcUpperBound(final double oldUpperBound) {
        return this.denominator;
    }
    
    @Override
    char tokenChar() {
        return '<';
    }
}
