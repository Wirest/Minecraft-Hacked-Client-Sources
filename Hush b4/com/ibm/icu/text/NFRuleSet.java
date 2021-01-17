// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;
import com.ibm.icu.impl.Utility;
import java.util.List;
import java.util.ArrayList;
import com.ibm.icu.impl.PatternProps;

final class NFRuleSet
{
    private String name;
    private NFRule[] rules;
    private NFRule negativeNumberRule;
    private NFRule[] fractionRules;
    private boolean isFractionRuleSet;
    private boolean isParseable;
    private int recursionCount;
    private static final int RECURSION_LIMIT = 50;
    
    public NFRuleSet(final String[] descriptions, final int index) throws IllegalArgumentException {
        this.negativeNumberRule = null;
        this.fractionRules = new NFRule[3];
        this.isFractionRuleSet = false;
        this.isParseable = true;
        this.recursionCount = 0;
        String description = descriptions[index];
        if (description.length() == 0) {
            throw new IllegalArgumentException("Empty rule set description");
        }
        if (description.charAt(0) == '%') {
            int pos = description.indexOf(58);
            if (pos == -1) {
                throw new IllegalArgumentException("Rule set name doesn't end in colon");
            }
            this.name = description.substring(0, pos);
            while (pos < description.length() && PatternProps.isWhiteSpace(description.charAt(++pos))) {}
            description = description.substring(pos);
            descriptions[index] = description;
        }
        else {
            this.name = "%default";
        }
        if (description.length() == 0) {
            throw new IllegalArgumentException("Empty rule set description");
        }
        if (this.name.endsWith("@noparse")) {
            this.name = this.name.substring(0, this.name.length() - 8);
            this.isParseable = false;
        }
    }
    
    public void parseRules(final String description, final RuleBasedNumberFormat owner) {
        List<String> ruleDescriptions = new ArrayList<String>();
        int oldP = 0;
        int p = description.indexOf(59);
        while (oldP != -1) {
            if (p != -1) {
                ruleDescriptions.add(description.substring(oldP, p));
                oldP = p + 1;
            }
            else {
                if (oldP < description.length()) {
                    ruleDescriptions.add(description.substring(oldP));
                }
                oldP = p;
            }
            p = description.indexOf(59, p + 1);
        }
        final List<NFRule> tempRules = new ArrayList<NFRule>();
        NFRule predecessor = null;
        for (int i = 0; i < ruleDescriptions.size(); ++i) {
            final Object temp = NFRule.makeRules(ruleDescriptions.get(i), this, predecessor, owner);
            if (temp instanceof NFRule) {
                tempRules.add((NFRule)temp);
                predecessor = (NFRule)temp;
            }
            else if (temp instanceof NFRule[]) {
                final NFRule[] rulesToAdd = (NFRule[])temp;
                for (int j = 0; j < rulesToAdd.length; ++j) {
                    tempRules.add(rulesToAdd[j]);
                    predecessor = rulesToAdd[j];
                }
            }
        }
        ruleDescriptions = null;
        long defaultBaseValue = 0L;
        int k = 0;
        while (k < tempRules.size()) {
            final NFRule rule = tempRules.get(k);
            switch ((int)rule.getBaseValue()) {
                case 0: {
                    rule.setBaseValue(defaultBaseValue);
                    if (!this.isFractionRuleSet) {
                        ++defaultBaseValue;
                    }
                    ++k;
                    continue;
                }
                case -1: {
                    this.negativeNumberRule = rule;
                    tempRules.remove(k);
                    continue;
                }
                case -2: {
                    this.fractionRules[0] = rule;
                    tempRules.remove(k);
                    continue;
                }
                case -3: {
                    this.fractionRules[1] = rule;
                    tempRules.remove(k);
                    continue;
                }
                case -4: {
                    this.fractionRules[2] = rule;
                    tempRules.remove(k);
                    continue;
                }
                default: {
                    if (rule.getBaseValue() < defaultBaseValue) {
                        throw new IllegalArgumentException("Rules are not in order, base: " + rule.getBaseValue() + " < " + defaultBaseValue);
                    }
                    defaultBaseValue = rule.getBaseValue();
                    if (!this.isFractionRuleSet) {
                        ++defaultBaseValue;
                    }
                    ++k;
                    continue;
                }
            }
        }
        tempRules.toArray(this.rules = new NFRule[tempRules.size()]);
    }
    
    public void makeIntoFractionRuleSet() {
        this.isFractionRuleSet = true;
    }
    
    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof NFRuleSet)) {
            return false;
        }
        final NFRuleSet that2 = (NFRuleSet)that;
        if (!this.name.equals(that2.name) || !Utility.objectEquals(this.negativeNumberRule, that2.negativeNumberRule) || !Utility.objectEquals(this.fractionRules[0], that2.fractionRules[0]) || !Utility.objectEquals(this.fractionRules[1], that2.fractionRules[1]) || !Utility.objectEquals(this.fractionRules[2], that2.fractionRules[2]) || this.rules.length != that2.rules.length || this.isFractionRuleSet != that2.isFractionRuleSet) {
            return false;
        }
        for (int i = 0; i < this.rules.length; ++i) {
            if (!this.rules[i].equals(that2.rules[i])) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(this.name + ":\n");
        for (int i = 0; i < this.rules.length; ++i) {
            result.append("    " + this.rules[i].toString() + "\n");
        }
        if (this.negativeNumberRule != null) {
            result.append("    " + this.negativeNumberRule.toString() + "\n");
        }
        if (this.fractionRules[0] != null) {
            result.append("    " + this.fractionRules[0].toString() + "\n");
        }
        if (this.fractionRules[1] != null) {
            result.append("    " + this.fractionRules[1].toString() + "\n");
        }
        if (this.fractionRules[2] != null) {
            result.append("    " + this.fractionRules[2].toString() + "\n");
        }
        return result.toString();
    }
    
    public boolean isFractionSet() {
        return this.isFractionRuleSet;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isPublic() {
        return !this.name.startsWith("%%");
    }
    
    public boolean isParseable() {
        return this.isParseable;
    }
    
    public void format(final long number, final StringBuffer toInsertInto, final int pos) {
        final NFRule applicableRule = this.findNormalRule(number);
        if (++this.recursionCount >= 50) {
            this.recursionCount = 0;
            throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
        }
        applicableRule.doFormat(number, toInsertInto, pos);
        --this.recursionCount;
    }
    
    public void format(final double number, final StringBuffer toInsertInto, final int pos) {
        final NFRule applicableRule = this.findRule(number);
        if (++this.recursionCount >= 50) {
            this.recursionCount = 0;
            throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
        }
        applicableRule.doFormat(number, toInsertInto, pos);
        --this.recursionCount;
    }
    
    private NFRule findRule(double number) {
        if (this.isFractionRuleSet) {
            return this.findFractionRuleSetRule(number);
        }
        if (number < 0.0) {
            if (this.negativeNumberRule != null) {
                return this.negativeNumberRule;
            }
            number = -number;
        }
        if (number != Math.floor(number)) {
            if (number < 1.0 && this.fractionRules[1] != null) {
                return this.fractionRules[1];
            }
            if (this.fractionRules[0] != null) {
                return this.fractionRules[0];
            }
        }
        if (this.fractionRules[2] != null) {
            return this.fractionRules[2];
        }
        return this.findNormalRule(Math.round(number));
    }
    
    private NFRule findNormalRule(long number) {
        if (this.isFractionRuleSet) {
            return this.findFractionRuleSetRule((double)number);
        }
        if (number < 0L) {
            if (this.negativeNumberRule != null) {
                return this.negativeNumberRule;
            }
            number = -number;
        }
        int lo = 0;
        int hi = this.rules.length;
        if (hi <= 0) {
            return this.fractionRules[2];
        }
        while (lo < hi) {
            final int mid = lo + hi >>> 1;
            if (this.rules[mid].getBaseValue() == number) {
                return this.rules[mid];
            }
            if (this.rules[mid].getBaseValue() > number) {
                hi = mid;
            }
            else {
                lo = mid + 1;
            }
        }
        if (hi == 0) {
            throw new IllegalStateException("The rule set " + this.name + " cannot format the value " + number);
        }
        NFRule result = this.rules[hi - 1];
        if (result.shouldRollBack((double)number)) {
            if (hi == 1) {
                throw new IllegalStateException("The rule set " + this.name + " cannot roll back from the rule '" + result + "'");
            }
            result = this.rules[hi - 2];
        }
        return result;
    }
    
    private NFRule findFractionRuleSetRule(final double number) {
        long leastCommonMultiple = this.rules[0].getBaseValue();
        for (int i = 1; i < this.rules.length; ++i) {
            leastCommonMultiple = lcm(leastCommonMultiple, this.rules[i].getBaseValue());
        }
        final long numerator = Math.round(number * leastCommonMultiple);
        long difference = Long.MAX_VALUE;
        int winner = 0;
        for (int j = 0; j < this.rules.length; ++j) {
            long tempDifference = numerator * this.rules[j].getBaseValue() % leastCommonMultiple;
            if (leastCommonMultiple - tempDifference < tempDifference) {
                tempDifference = leastCommonMultiple - tempDifference;
            }
            if (tempDifference < difference) {
                difference = tempDifference;
                winner = j;
                if (difference == 0L) {
                    break;
                }
            }
        }
        if (winner + 1 < this.rules.length && this.rules[winner + 1].getBaseValue() == this.rules[winner].getBaseValue() && (Math.round(number * this.rules[winner].getBaseValue()) < 1L || Math.round(number * this.rules[winner].getBaseValue()) >= 2L)) {
            ++winner;
        }
        return this.rules[winner];
    }
    
    private static long lcm(final long x, final long y) {
        long x2 = x;
        long y2 = y;
        int p2 = 0;
        while ((x2 & 0x1L) == 0x0L && (y2 & 0x1L) == 0x0L) {
            ++p2;
            x2 >>= 1;
            y2 >>= 1;
        }
        long t;
        if ((x2 & 0x1L) == 0x1L) {
            t = -y2;
        }
        else {
            t = x2;
        }
        while (t != 0L) {
            while ((t & 0x1L) == 0x0L) {
                t >>= 1;
            }
            if (t > 0L) {
                x2 = t;
            }
            else {
                y2 = -t;
            }
            t = x2 - y2;
        }
        final long gcd = x2 << p2;
        return x / gcd * y;
    }
    
    public Number parse(final String text, final ParsePosition parsePosition, final double upperBound) {
        final ParsePosition highWaterMark = new ParsePosition(0);
        Number result = 0L;
        Number tempResult = null;
        if (text.length() == 0) {
            return result;
        }
        if (this.negativeNumberRule != null) {
            tempResult = this.negativeNumberRule.doParse(text, parsePosition, false, upperBound);
            if (parsePosition.getIndex() > highWaterMark.getIndex()) {
                result = tempResult;
                highWaterMark.setIndex(parsePosition.getIndex());
            }
            parsePosition.setIndex(0);
        }
        for (int i = 0; i < 3; ++i) {
            if (this.fractionRules[i] != null) {
                tempResult = this.fractionRules[i].doParse(text, parsePosition, false, upperBound);
                if (parsePosition.getIndex() > highWaterMark.getIndex()) {
                    result = tempResult;
                    highWaterMark.setIndex(parsePosition.getIndex());
                }
                parsePosition.setIndex(0);
            }
        }
        for (int i = this.rules.length - 1; i >= 0 && highWaterMark.getIndex() < text.length(); --i) {
            if (this.isFractionRuleSet || this.rules[i].getBaseValue() < upperBound) {
                tempResult = this.rules[i].doParse(text, parsePosition, this.isFractionRuleSet, upperBound);
                if (parsePosition.getIndex() > highWaterMark.getIndex()) {
                    result = tempResult;
                    highWaterMark.setIndex(parsePosition.getIndex());
                }
                parsePosition.setIndex(0);
            }
        }
        parsePosition.setIndex(highWaterMark.getIndex());
        return result;
    }
}
