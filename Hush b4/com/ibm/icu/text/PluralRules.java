// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Arrays;
import java.util.HashSet;
import com.ibm.icu.util.Output;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.PluralRulesLoader;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.impl.Utility;
import java.util.Locale;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

public class PluralRules implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final RuleList rules;
    private final Set<String> keywords;
    private int repeatLimit;
    private transient int hashCode;
    private transient Map<String, List<Double>> _keySamplesMap;
    private transient Map<String, Boolean> _keyLimitedMap;
    public static final String KEYWORD_ZERO = "zero";
    public static final String KEYWORD_ONE = "one";
    public static final String KEYWORD_TWO = "two";
    public static final String KEYWORD_FEW = "few";
    public static final String KEYWORD_MANY = "many";
    public static final String KEYWORD_OTHER = "other";
    public static final double NO_UNIQUE_VALUE = -0.00123456777;
    private static final Constraint NO_CONSTRAINT;
    private static final Rule DEFAULT_RULE;
    public static final PluralRules DEFAULT;
    
    public static PluralRules parseDescription(String description) throws ParseException {
        description = description.trim();
        if (description.length() == 0) {
            return PluralRules.DEFAULT;
        }
        return new PluralRules(parseRuleChain(description));
    }
    
    public static PluralRules createRules(final String description) {
        try {
            return parseDescription(description);
        }
        catch (ParseException e) {
            return null;
        }
    }
    
    private static Constraint parseConstraint(String description) throws ParseException {
        description = description.trim().toLowerCase(Locale.ENGLISH);
        Constraint result = null;
        final String[] or_together = Utility.splitString(description, "or");
        for (int i = 0; i < or_together.length; ++i) {
            Constraint andConstraint = null;
            final String[] and_together = Utility.splitString(or_together[i], "and");
            for (int j = 0; j < and_together.length; ++j) {
                Constraint newConstraint = PluralRules.NO_CONSTRAINT;
                final String condition = and_together[j].trim();
                final String[] tokens = Utility.splitWhitespace(condition);
                int mod = 0;
                boolean inRange = true;
                boolean integersOnly = true;
                long lowBound = Long.MAX_VALUE;
                long highBound = Long.MIN_VALUE;
                long[] vals = null;
                boolean isRange = false;
                int x = 0;
                String t = tokens[x++];
                if (!"n".equals(t)) {
                    throw unexpected(t, condition);
                }
                if (x < tokens.length) {
                    t = tokens[x++];
                    if ("mod".equals(t)) {
                        mod = Integer.parseInt(tokens[x++]);
                        t = nextToken(tokens, x++, condition);
                    }
                    if ("is".equals(t)) {
                        t = nextToken(tokens, x++, condition);
                        if ("not".equals(t)) {
                            inRange = false;
                            t = nextToken(tokens, x++, condition);
                        }
                    }
                    else {
                        isRange = true;
                        if ("not".equals(t)) {
                            inRange = false;
                            t = nextToken(tokens, x++, condition);
                        }
                        if ("in".equals(t)) {
                            t = nextToken(tokens, x++, condition);
                        }
                        else {
                            if (!"within".equals(t)) {
                                throw unexpected(t, condition);
                            }
                            integersOnly = false;
                            t = nextToken(tokens, x++, condition);
                        }
                    }
                    if (isRange) {
                        final String[] range_list = Utility.splitString(t, ",");
                        vals = new long[range_list.length * 2];
                        for (int k1 = 0, k2 = 0; k1 < range_list.length; ++k1, k2 += 2) {
                            final String range = range_list[k1];
                            final String[] pair = Utility.splitString(range, "..");
                            long low;
                            long high;
                            if (pair.length == 2) {
                                low = Long.parseLong(pair[0]);
                                high = Long.parseLong(pair[1]);
                                if (low > high) {
                                    throw unexpected(range, condition);
                                }
                            }
                            else {
                                if (pair.length != 1) {
                                    throw unexpected(range, condition);
                                }
                                high = (low = Long.parseLong(pair[0]));
                            }
                            vals[k2] = low;
                            vals[k2 + 1] = high;
                            lowBound = Math.min(lowBound, low);
                            highBound = Math.max(highBound, high);
                        }
                        if (vals.length == 2) {
                            vals = null;
                        }
                    }
                    else {
                        highBound = (lowBound = Long.parseLong(t));
                    }
                    if (x != tokens.length) {
                        throw unexpected(tokens[x], condition);
                    }
                    newConstraint = new RangeConstraint(mod, inRange, integersOnly, lowBound, highBound, vals);
                }
                if (andConstraint == null) {
                    andConstraint = newConstraint;
                }
                else {
                    andConstraint = new AndConstraint(andConstraint, newConstraint);
                }
            }
            if (result == null) {
                result = andConstraint;
            }
            else {
                result = new OrConstraint(result, andConstraint);
            }
        }
        return result;
    }
    
    private static ParseException unexpected(final String token, final String context) {
        return new ParseException("unexpected token '" + token + "' in '" + context + "'", -1);
    }
    
    private static String nextToken(final String[] tokens, final int x, final String context) throws ParseException {
        if (x < tokens.length) {
            return tokens[x];
        }
        throw new ParseException("missing token at end of '" + context + "'", -1);
    }
    
    private static Rule parseRule(String description) throws ParseException {
        final int x = description.indexOf(58);
        if (x == -1) {
            throw new ParseException("missing ':' in rule description '" + description + "'", 0);
        }
        final String keyword = description.substring(0, x).trim();
        if (!isValidKeyword(keyword)) {
            throw new ParseException("keyword '" + keyword + " is not valid", 0);
        }
        description = description.substring(x + 1).trim();
        if (description.length() == 0) {
            throw new ParseException("missing constraint in '" + description + "'", x + 1);
        }
        final Constraint constraint = parseConstraint(description);
        final Rule rule = new ConstrainedRule(keyword, constraint);
        return rule;
    }
    
    private static RuleChain parseRuleChain(final String description) throws ParseException {
        RuleChain rc = null;
        final String[] rules = Utility.split(description, ';');
        for (int i = 0; i < rules.length; ++i) {
            final Rule r = parseRule(rules[i].trim());
            if (rc == null) {
                rc = new RuleChain(r);
            }
            else {
                rc = rc.addRule(r);
            }
        }
        return rc;
    }
    
    public static PluralRules forLocale(final ULocale locale) {
        return PluralRulesLoader.loader.forLocale(locale, PluralType.CARDINAL);
    }
    
    public static PluralRules forLocale(final ULocale locale, final PluralType type) {
        return PluralRulesLoader.loader.forLocale(locale, type);
    }
    
    private static boolean isValidKeyword(final String token) {
        return PatternProps.isIdentifier(token);
    }
    
    private PluralRules(final RuleList rules) {
        this.rules = rules;
        this.keywords = Collections.unmodifiableSet((Set<? extends String>)rules.getKeywords());
    }
    
    public String select(final double number) {
        return this.rules.select(number);
    }
    
    public Set<String> getKeywords() {
        return this.keywords;
    }
    
    public double getUniqueKeywordValue(final String keyword) {
        final Collection<Double> values = this.getAllKeywordValues(keyword);
        if (values != null && values.size() == 1) {
            return values.iterator().next();
        }
        return -0.00123456777;
    }
    
    public Collection<Double> getAllKeywordValues(final String keyword) {
        if (!this.keywords.contains(keyword)) {
            return (Collection<Double>)Collections.emptyList();
        }
        final Collection<Double> result = this.getKeySamplesMap().get(keyword);
        if (result.size() > 2 && !this.getKeyLimitedMap().get(keyword)) {
            return null;
        }
        return result;
    }
    
    public Collection<Double> getSamples(final String keyword) {
        if (!this.keywords.contains(keyword)) {
            return null;
        }
        return this.getKeySamplesMap().get(keyword);
    }
    
    private Map<String, Boolean> getKeyLimitedMap() {
        this.initKeyMaps();
        return this._keyLimitedMap;
    }
    
    private Map<String, List<Double>> getKeySamplesMap() {
        this.initKeyMaps();
        return this._keySamplesMap;
    }
    
    private synchronized void initKeyMaps() {
        if (this._keySamplesMap == null) {
            final int MAX_SAMPLES = 3;
            final Map<String, Boolean> temp = new HashMap<String, Boolean>();
            for (final String k : this.keywords) {
                temp.put(k, this.rules.isLimited(k));
            }
            this._keyLimitedMap = temp;
            final Map<String, List<Double>> sampleMap = new HashMap<String, List<Double>>();
            int keywordsRemaining = this.keywords.size();
            for (int limit = Math.max(5, this.getRepeatLimit() * 3) * 2, i = 0; keywordsRemaining > 0 && i < limit; ++i) {
                final double val = i / 2.0;
                final String keyword = this.select(val);
                final boolean keyIsLimited = this._keyLimitedMap.get(keyword);
                List<Double> list = sampleMap.get(keyword);
                if (list == null) {
                    list = new ArrayList<Double>(3);
                    sampleMap.put(keyword, list);
                }
                else if (!keyIsLimited && list.size() == 3) {
                    continue;
                }
                list.add(val);
                if (!keyIsLimited && list.size() == 3) {
                    --keywordsRemaining;
                }
            }
            if (keywordsRemaining > 0) {
                for (final String j : this.keywords) {
                    if (!sampleMap.containsKey(j)) {
                        sampleMap.put(j, Collections.emptyList());
                        if (--keywordsRemaining == 0) {
                            break;
                        }
                        continue;
                    }
                }
            }
            for (final Map.Entry<String, List<Double>> entry : sampleMap.entrySet()) {
                sampleMap.put(entry.getKey(), Collections.unmodifiableList((List<? extends Double>)entry.getValue()));
            }
            this._keySamplesMap = sampleMap;
        }
    }
    
    public static ULocale[] getAvailableULocales() {
        return PluralRulesLoader.loader.getAvailableULocales();
    }
    
    public static ULocale getFunctionalEquivalent(final ULocale locale, final boolean[] isAvailable) {
        return PluralRulesLoader.loader.getFunctionalEquivalent(locale, isAvailable);
    }
    
    @Override
    public String toString() {
        return "keywords: " + this.keywords + " limit: " + this.getRepeatLimit() + " rules: " + this.rules.toString();
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            int newHashCode = this.keywords.hashCode();
            for (int i = 0; i < 12; ++i) {
                newHashCode = newHashCode * 31 + this.select(i).hashCode();
            }
            if (newHashCode == 0) {
                newHashCode = 1;
            }
            this.hashCode = newHashCode;
        }
        return this.hashCode;
    }
    
    @Override
    public boolean equals(final Object rhs) {
        return rhs instanceof PluralRules && this.equals((PluralRules)rhs);
    }
    
    public boolean equals(final PluralRules rhs) {
        if (rhs == null) {
            return false;
        }
        if (rhs == this) {
            return true;
        }
        if (this.hashCode() != rhs.hashCode()) {
            return false;
        }
        if (!rhs.getKeywords().equals(this.keywords)) {
            return false;
        }
        for (int limit = Math.max(this.getRepeatLimit(), rhs.getRepeatLimit()), i = 0; i < limit * 2; ++i) {
            if (!this.select(i).equals(rhs.select(i))) {
                return false;
            }
        }
        return true;
    }
    
    private int getRepeatLimit() {
        if (this.repeatLimit == 0) {
            this.repeatLimit = this.rules.getRepeatLimit() + 1;
        }
        return this.repeatLimit;
    }
    
    public KeywordStatus getKeywordStatus(final String keyword, final int offset, Set<Double> explicits, final Output<Double> uniqueValue) {
        if (uniqueValue != null) {
            uniqueValue.value = null;
        }
        if (!this.rules.getKeywords().contains(keyword)) {
            return KeywordStatus.INVALID;
        }
        final Collection<Double> values = this.getAllKeywordValues(keyword);
        if (values == null) {
            return KeywordStatus.UNBOUNDED;
        }
        final int originalSize = values.size();
        if (explicits == null) {
            explicits = Collections.emptySet();
        }
        if (originalSize > explicits.size()) {
            if (originalSize == 1) {
                if (uniqueValue != null) {
                    uniqueValue.value = values.iterator().next();
                }
                return KeywordStatus.UNIQUE;
            }
            return KeywordStatus.BOUNDED;
        }
        else {
            final HashSet<Double> subtractedSet = new HashSet<Double>(values);
            for (final Double explicit : explicits) {
                subtractedSet.remove(explicit - offset);
            }
            if (subtractedSet.size() == 0) {
                return KeywordStatus.SUPPRESSED;
            }
            if (uniqueValue != null && subtractedSet.size() == 1) {
                uniqueValue.value = subtractedSet.iterator().next();
            }
            return (originalSize == 1) ? KeywordStatus.UNIQUE : KeywordStatus.BOUNDED;
        }
    }
    
    static {
        NO_CONSTRAINT = new Constraint() {
            private static final long serialVersionUID = 9163464945387899416L;
            
            public boolean isFulfilled(final double n) {
                return true;
            }
            
            public boolean isLimited() {
                return false;
            }
            
            @Override
            public String toString() {
                return "n is any";
            }
            
            public int updateRepeatLimit(final int limit) {
                return limit;
            }
        };
        DEFAULT_RULE = new Rule() {
            private static final long serialVersionUID = -5677499073940822149L;
            
            public String getKeyword() {
                return "other";
            }
            
            public boolean appliesTo(final double n) {
                return true;
            }
            
            public boolean isLimited() {
                return false;
            }
            
            @Override
            public String toString() {
                return "(other)";
            }
            
            public int updateRepeatLimit(final int limit) {
                return limit;
            }
        };
        DEFAULT = new PluralRules(new RuleChain(PluralRules.DEFAULT_RULE));
    }
    
    public enum PluralType
    {
        CARDINAL, 
        ORDINAL;
    }
    
    private static class RangeConstraint implements Constraint, Serializable
    {
        private static final long serialVersionUID = 1L;
        private int mod;
        private boolean inRange;
        private boolean integersOnly;
        private long lowerBound;
        private long upperBound;
        private long[] range_list;
        
        RangeConstraint(final int mod, final boolean inRange, final boolean integersOnly, final long lowerBound, final long upperBound, final long[] range_list) {
            this.mod = mod;
            this.inRange = inRange;
            this.integersOnly = integersOnly;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.range_list = range_list;
        }
        
        public boolean isFulfilled(double n) {
            if (this.integersOnly && n - (long)n != 0.0) {
                return !this.inRange;
            }
            if (this.mod != 0) {
                n %= this.mod;
            }
            boolean test = n >= this.lowerBound && n <= this.upperBound;
            if (test && this.range_list != null) {
                test = false;
                for (int i = 0; !test && i < this.range_list.length; test = (n >= this.range_list[i] && n <= this.range_list[i + 1]), i += 2) {}
            }
            return this.inRange == test;
        }
        
        public boolean isLimited() {
            return this.integersOnly && this.inRange && this.mod == 0;
        }
        
        public int updateRepeatLimit(final int limit) {
            final int mylimit = (this.mod == 0) ? ((int)this.upperBound) : this.mod;
            return Math.max(mylimit, limit);
        }
        
        @Override
        public String toString() {
            class ListBuilder
            {
                StringBuilder sb;
                
                ListBuilder() {
                    this.sb = new StringBuilder("[");
                }
                
                ListBuilder add(final String s) {
                    return this.add(s, null);
                }
                
                ListBuilder add(final String s, final Object o) {
                    if (this.sb.length() > 1) {
                        this.sb.append(", ");
                    }
                    this.sb.append(s);
                    if (o != null) {
                        this.sb.append(": ").append(o.toString());
                    }
                    return this;
                }
                
                @Override
                public String toString() {
                    final String s = this.sb.append(']').toString();
                    this.sb = null;
                    return s;
                }
            }
            final ListBuilder lb = new ListBuilder();
            if (this.mod > 1) {
                lb.add("mod", this.mod);
            }
            if (this.inRange) {
                lb.add("in");
            }
            else {
                lb.add("except");
            }
            if (this.integersOnly) {
                lb.add("ints");
            }
            if (this.lowerBound == this.upperBound) {
                lb.add(String.valueOf(this.lowerBound));
            }
            else {
                lb.add(String.valueOf(this.lowerBound) + "-" + String.valueOf(this.upperBound));
            }
            if (this.range_list != null) {
                lb.add(Arrays.toString(this.range_list));
            }
            return lb.toString();
        }
    }
    
    private abstract static class BinaryConstraint implements Constraint, Serializable
    {
        private static final long serialVersionUID = 1L;
        protected final Constraint a;
        protected final Constraint b;
        private final String conjunction;
        
        protected BinaryConstraint(final Constraint a, final Constraint b, final String c) {
            this.a = a;
            this.b = b;
            this.conjunction = c;
        }
        
        public int updateRepeatLimit(final int limit) {
            return this.a.updateRepeatLimit(this.b.updateRepeatLimit(limit));
        }
        
        @Override
        public String toString() {
            return this.a.toString() + this.conjunction + this.b.toString();
        }
    }
    
    private static class AndConstraint extends BinaryConstraint
    {
        private static final long serialVersionUID = 7766999779862263523L;
        
        AndConstraint(final Constraint a, final Constraint b) {
            super(a, b, " && ");
        }
        
        public boolean isFulfilled(final double n) {
            return this.a.isFulfilled(n) && this.b.isFulfilled(n);
        }
        
        public boolean isLimited() {
            return this.a.isLimited() || this.b.isLimited();
        }
    }
    
    private static class OrConstraint extends BinaryConstraint
    {
        private static final long serialVersionUID = 1405488568664762222L;
        
        OrConstraint(final Constraint a, final Constraint b) {
            super(a, b, " || ");
        }
        
        public boolean isFulfilled(final double n) {
            return this.a.isFulfilled(n) || this.b.isFulfilled(n);
        }
        
        public boolean isLimited() {
            return this.a.isLimited() && this.b.isLimited();
        }
    }
    
    private static class ConstrainedRule implements Rule, Serializable
    {
        private static final long serialVersionUID = 1L;
        private final String keyword;
        private final Constraint constraint;
        
        public ConstrainedRule(final String keyword, final Constraint constraint) {
            this.keyword = keyword;
            this.constraint = constraint;
        }
        
        public Rule and(final Constraint c) {
            return new ConstrainedRule(this.keyword, new AndConstraint(this.constraint, c));
        }
        
        public Rule or(final Constraint c) {
            return new ConstrainedRule(this.keyword, new OrConstraint(this.constraint, c));
        }
        
        public String getKeyword() {
            return this.keyword;
        }
        
        public boolean appliesTo(final double n) {
            return this.constraint.isFulfilled(n);
        }
        
        public int updateRepeatLimit(final int limit) {
            return this.constraint.updateRepeatLimit(limit);
        }
        
        public boolean isLimited() {
            return this.constraint.isLimited();
        }
        
        @Override
        public String toString() {
            return this.keyword + ": " + this.constraint;
        }
    }
    
    private static class RuleChain implements RuleList, Serializable
    {
        private static final long serialVersionUID = 1L;
        private final Rule rule;
        private final RuleChain next;
        
        public RuleChain(final Rule rule) {
            this(rule, null);
        }
        
        private RuleChain(final Rule rule, final RuleChain next) {
            this.rule = rule;
            this.next = next;
        }
        
        public RuleChain addRule(final Rule nextRule) {
            return new RuleChain(nextRule, this);
        }
        
        private Rule selectRule(final double n) {
            Rule r = null;
            if (this.next != null) {
                r = this.next.selectRule(n);
            }
            if (r == null && this.rule.appliesTo(n)) {
                r = this.rule;
            }
            return r;
        }
        
        public String select(final double n) {
            final Rule r = this.selectRule(n);
            if (r == null) {
                return "other";
            }
            return r.getKeyword();
        }
        
        public Set<String> getKeywords() {
            final Set<String> result = new HashSet<String>();
            result.add("other");
            for (RuleChain rc = this; rc != null; rc = rc.next) {
                result.add(rc.rule.getKeyword());
            }
            return result;
        }
        
        public boolean isLimited(final String keyword) {
            RuleChain rc = this;
            boolean result = false;
            while (rc != null) {
                if (keyword.equals(rc.rule.getKeyword())) {
                    if (!rc.rule.isLimited()) {
                        return false;
                    }
                    result = true;
                }
                rc = rc.next;
            }
            return result;
        }
        
        public int getRepeatLimit() {
            int result = 0;
            for (RuleChain rc = this; rc != null; rc = rc.next) {
                result = rc.rule.updateRepeatLimit(result);
            }
            return result;
        }
        
        @Override
        public String toString() {
            String s = this.rule.toString();
            if (this.next != null) {
                s = this.next.toString() + "; " + s;
            }
            return s;
        }
    }
    
    public enum KeywordStatus
    {
        INVALID, 
        SUPPRESSED, 
        UNIQUE, 
        BOUNDED, 
        UNBOUNDED;
    }
    
    private interface Rule extends Serializable
    {
        String getKeyword();
        
        boolean appliesTo(final double p0);
        
        boolean isLimited();
        
        int updateRepeatLimit(final int p0);
    }
    
    private interface RuleList extends Serializable
    {
        String select(final double p0);
        
        Set<String> getKeywords();
        
        int getRepeatLimit();
        
        boolean isLimited(final String p0);
    }
    
    private interface Constraint extends Serializable
    {
        boolean isFulfilled(final double p0);
        
        boolean isLimited();
        
        int updateRepeatLimit(final int p0);
    }
}
