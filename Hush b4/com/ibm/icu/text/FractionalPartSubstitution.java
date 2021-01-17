// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;

class FractionalPartSubstitution extends NFSubstitution
{
    private boolean byDigits;
    private boolean useSpaces;
    
    FractionalPartSubstitution(final int pos, final NFRuleSet ruleSet, final RuleBasedNumberFormat formatter, final String description) {
        super(pos, ruleSet, formatter, description);
        this.byDigits = false;
        this.useSpaces = true;
        if (description.equals(">>") || description.equals(">>>") || ruleSet == this.ruleSet) {
            this.byDigits = true;
            if (description.equals(">>>")) {
                this.useSpaces = false;
            }
        }
        else {
            this.ruleSet.makeIntoFractionRuleSet();
        }
    }
    
    @Override
    public void doSubstitution(final double number, final StringBuffer toInsertInto, final int position) {
        if (!this.byDigits) {
            super.doSubstitution(number, toInsertInto, position);
        }
        else {
            final DigitList dl = new DigitList();
            dl.set(number, 20, true);
            boolean pad = false;
            while (dl.count > Math.max(0, dl.decimalAt)) {
                if (pad && this.useSpaces) {
                    toInsertInto.insert(position + this.pos, ' ');
                }
                else {
                    pad = true;
                }
                final NFRuleSet ruleSet = this.ruleSet;
                final byte[] digits = dl.digits;
                final DigitList list = dl;
                final int count = list.count - 1;
                list.count = count;
                ruleSet.format(digits[count] - 48, toInsertInto, position + this.pos);
            }
            while (dl.decimalAt < 0) {
                if (pad && this.useSpaces) {
                    toInsertInto.insert(position + this.pos, ' ');
                }
                else {
                    pad = true;
                }
                this.ruleSet.format(0L, toInsertInto, position + this.pos);
                final DigitList list2 = dl;
                ++list2.decimalAt;
            }
        }
    }
    
    @Override
    public long transformNumber(final long number) {
        return 0L;
    }
    
    @Override
    public double transformNumber(final double number) {
        return number - Math.floor(number);
    }
    
    @Override
    public Number doParse(final String text, final ParsePosition parsePosition, final double baseValue, final double upperBound, final boolean lenientParse) {
        if (!this.byDigits) {
            return super.doParse(text, parsePosition, baseValue, 0.0, lenientParse);
        }
        String workText = text;
        final ParsePosition workPos = new ParsePosition(1);
        double result = 0.0;
        final DigitList dl = new DigitList();
        while (workText.length() > 0 && workPos.getIndex() != 0) {
            workPos.setIndex(0);
            int digit = this.ruleSet.parse(workText, workPos, 10.0).intValue();
            if (lenientParse && workPos.getIndex() == 0) {
                final Number n = this.rbnf.getDecimalFormat().parse(workText, workPos);
                if (n != null) {
                    digit = n.intValue();
                }
            }
            if (workPos.getIndex() != 0) {
                dl.append(48 + digit);
                parsePosition.setIndex(parsePosition.getIndex() + workPos.getIndex());
                workText = workText.substring(workPos.getIndex());
                while (workText.length() > 0 && workText.charAt(0) == ' ') {
                    workText = workText.substring(1);
                    parsePosition.setIndex(parsePosition.getIndex() + 1);
                }
            }
        }
        result = ((dl.count == 0) ? 0.0 : dl.getDouble());
        result = this.composeRuleValue(result, baseValue);
        return new Double(result);
    }
    
    @Override
    public double composeRuleValue(final double newRuleValue, final double oldRuleValue) {
        return newRuleValue + oldRuleValue;
    }
    
    @Override
    public double calcUpperBound(final double oldUpperBound) {
        return 0.0;
    }
    
    @Override
    char tokenChar() {
        return '>';
    }
}
