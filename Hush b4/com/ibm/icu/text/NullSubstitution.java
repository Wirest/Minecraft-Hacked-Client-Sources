// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;

class NullSubstitution extends NFSubstitution
{
    NullSubstitution(final int pos, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        super(pos, ruleSet, formatter, description);
    }
    
    @Override
    public boolean equals(final Object that) {
        return super.equals(that);
    }
    
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    @Override
    public String toString() {
        return "";
    }
    
    @Override
    public void doSubstitution(final long number, final StringBuffer toInsertInto, final int position) {
    }
    
    @Override
    public void doSubstitution(final double number, final StringBuffer toInsertInto, final int position) {
    }
    
    @Override
    public long transformNumber(final long number) {
        return 0L;
    }
    
    @Override
    public double transformNumber(final double number) {
        return 0.0;
    }
    
    @Override
    public Number doParse(final String text, final ParsePosition parsePosition, final double baseValue, final double upperBound, final boolean lenientParse) {
        if (baseValue == (long)baseValue) {
            return (long)baseValue;
        }
        return new Double(baseValue);
    }
    
    @Override
    public double composeRuleValue(final double newRuleValue, final double oldRuleValue) {
        return 0.0;
    }
    
    @Override
    public double calcUpperBound(final double oldUpperBound) {
        return 0.0;
    }
    
    @Override
    public boolean isNullSubstitution() {
        return true;
    }
    
    @Override
    char tokenChar() {
        return ' ';
    }
}
