// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

class SameValueSubstitution extends NFSubstitution
{
    SameValueSubstitution(final int pos, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        super(pos, ruleSet, formatter, description);
        if (description.equals("==")) {
            throw new IllegalArgumentException("== is not a legal token");
        }
    }
    
    @Override
    public long transformNumber(final long number) {
        return number;
    }
    
    @Override
    public double transformNumber(final double number) {
        return number;
    }
    
    @Override
    public double composeRuleValue(final double newRuleValue, final double oldRuleValue) {
        return newRuleValue;
    }
    
    @Override
    public double calcUpperBound(final double oldUpperBound) {
        return oldUpperBound;
    }
    
    @Override
    char tokenChar() {
        return '=';
    }
}
