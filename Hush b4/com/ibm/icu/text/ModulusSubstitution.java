// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;

class ModulusSubstitution extends NFSubstitution
{
    double divisor;
    NFRule ruleToUse;
    
    ModulusSubstitution(final int pos, final double divisor, final NFRule rulePredecessor, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        super(pos, ruleSet, formatter, description);
        this.divisor = divisor;
        if (divisor == 0.0) {
            throw new IllegalStateException("Substitution with bad divisor (" + divisor + ") " + description.substring(0, pos) + " | " + description.substring(pos));
        }
        if (description.equals(">>>")) {
            this.ruleToUse = rulePredecessor;
        }
        else {
            this.ruleToUse = null;
        }
    }
    
    @Override
    public void setDivisor(final int radix, final int exponent) {
        this.divisor = Math.pow(radix, exponent);
        if (this.divisor == 0.0) {
            throw new IllegalStateException("Substitution with bad divisor");
        }
    }
    
    @Override
    public boolean equals(final Object that) {
        if (super.equals(that)) {
            final ModulusSubstitution that2 = (ModulusSubstitution)that;
            return this.divisor == that2.divisor;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    @Override
    public void doSubstitution(final long number, final StringBuffer toInsertInto, final int position) {
        if (this.ruleToUse == null) {
            super.doSubstitution(number, toInsertInto, position);
        }
        else {
            final long numberToFormat = this.transformNumber(number);
            this.ruleToUse.doFormat(numberToFormat, toInsertInto, position + this.pos);
        }
    }
    
    @Override
    public void doSubstitution(final double number, final StringBuffer toInsertInto, final int position) {
        if (this.ruleToUse == null) {
            super.doSubstitution(number, toInsertInto, position);
        }
        else {
            final double numberToFormat = this.transformNumber(number);
            this.ruleToUse.doFormat(numberToFormat, toInsertInto, position + this.pos);
        }
    }
    
    @Override
    public long transformNumber(final long number) {
        return (long)Math.floor(number % this.divisor);
    }
    
    @Override
    public double transformNumber(final double number) {
        return Math.floor(number % this.divisor);
    }
    
    @Override
    public Number doParse(final String text, final ParsePosition parsePosition, final double baseValue, final double upperBound, final boolean lenientParse) {
        if (this.ruleToUse == null) {
            return super.doParse(text, parsePosition, baseValue, upperBound, lenientParse);
        }
        final Number tempResult = this.ruleToUse.doParse(text, parsePosition, false, upperBound);
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
    
    @Override
    public double composeRuleValue(final double newRuleValue, final double oldRuleValue) {
        return oldRuleValue - oldRuleValue % this.divisor + newRuleValue;
    }
    
    @Override
    public double calcUpperBound(final double oldUpperBound) {
        return this.divisor;
    }
    
    @Override
    public boolean isModulusSubstitution() {
        return true;
    }
    
    @Override
    char tokenChar() {
        return '>';
    }
}
