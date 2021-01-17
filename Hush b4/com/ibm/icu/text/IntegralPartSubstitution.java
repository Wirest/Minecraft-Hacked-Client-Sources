// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

class IntegralPartSubstitution extends NFSubstitution
{
    IntegralPartSubstitution(final int pos, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        super(pos, ruleSet, formatter, description);
    }
    
    @Override
    public long transformNumber(final long number) {
        return number;
    }
    
    @Override
    public double transformNumber(final double number) {
        return Math.floor(number);
    }
    
    @Override
    public double composeRuleValue(final double newRuleValue, final double oldRuleValue) {
        return newRuleValue + oldRuleValue;
    }
    
    @Override
    public double calcUpperBound(final double oldUpperBound) {
        return Double.MAX_VALUE;
    }
    
    @Override
    char tokenChar() {
        return '<';
    }
}
