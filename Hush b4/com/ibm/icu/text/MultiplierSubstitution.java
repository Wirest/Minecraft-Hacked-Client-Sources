// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

class MultiplierSubstitution extends NFSubstitution
{
    double divisor;
    
    MultiplierSubstitution(final int pos, final double divisor, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        super(pos, ruleSet, formatter, description);
        this.divisor = divisor;
        if (divisor == 0.0) {
            throw new IllegalStateException("Substitution with bad divisor (" + divisor + ") " + description.substring(0, pos) + " | " + description.substring(pos));
        }
    }
    
    @Override
    public void setDivisor(final int radix, final int exponent) {
        this.divisor = Math.pow(radix, exponent);
        if (this.divisor == 0.0) {
            throw new IllegalStateException("Substitution with divisor 0");
        }
    }
    
    @Override
    public boolean equals(final Object that) {
        if (super.equals(that)) {
            final MultiplierSubstitution that2 = (MultiplierSubstitution)that;
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
    public long transformNumber(final long number) {
        return (long)Math.floor(number / this.divisor);
    }
    
    @Override
    public double transformNumber(final double number) {
        if (this.ruleSet == null) {
            return number / this.divisor;
        }
        return Math.floor(number / this.divisor);
    }
    
    @Override
    public double composeRuleValue(final double newRuleValue, final double oldRuleValue) {
        return newRuleValue * this.divisor;
    }
    
    @Override
    public double calcUpperBound(final double oldUpperBound) {
        return this.divisor;
    }
    
    @Override
    char tokenChar() {
        return '<';
    }
}
