// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language.bm;

import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;

public class PhoneticEngine
{
    private static final Map<NameType, Set<String>> NAME_PREFIXES;
    private static final int DEFAULT_MAX_PHONEMES = 20;
    private final Lang lang;
    private final NameType nameType;
    private final RuleType ruleType;
    private final boolean concat;
    private final int maxPhonemes;
    
    private static String join(final Iterable<String> strings, final String sep) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> si = strings.iterator();
        if (si.hasNext()) {
            sb.append(si.next());
        }
        while (si.hasNext()) {
            sb.append(sep).append(si.next());
        }
        return sb.toString();
    }
    
    public PhoneticEngine(final NameType nameType, final RuleType ruleType, final boolean concat) {
        this(nameType, ruleType, concat, 20);
    }
    
    public PhoneticEngine(final NameType nameType, final RuleType ruleType, final boolean concat, final int maxPhonemes) {
        if (ruleType == RuleType.RULES) {
            throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
        }
        this.nameType = nameType;
        this.ruleType = ruleType;
        this.concat = concat;
        this.lang = Lang.instance(nameType);
        this.maxPhonemes = maxPhonemes;
    }
    
    private PhonemeBuilder applyFinalRules(final PhonemeBuilder phonemeBuilder, final Map<String, List<Rule>> finalRules) {
        if (finalRules == null) {
            throw new NullPointerException("finalRules can not be null");
        }
        if (finalRules.isEmpty()) {
            return phonemeBuilder;
        }
        final Set<Rule.Phoneme> phonemes = new TreeSet<Rule.Phoneme>(Rule.Phoneme.COMPARATOR);
        for (final Rule.Phoneme phoneme : phonemeBuilder.getPhonemes()) {
            PhonemeBuilder subBuilder = PhonemeBuilder.empty(phoneme.getLanguages());
            final String phonemeText = phoneme.getPhonemeText().toString();
            RulesApplication rulesApplication;
            for (int i = 0; i < phonemeText.length(); i = rulesApplication.getI()) {
                rulesApplication = new RulesApplication(finalRules, phonemeText, subBuilder, i, this.maxPhonemes).invoke();
                final boolean found = rulesApplication.isFound();
                subBuilder = rulesApplication.getPhonemeBuilder();
                if (!found) {
                    subBuilder.append(phonemeText.subSequence(i, i + 1));
                }
            }
            phonemes.addAll(subBuilder.getPhonemes());
        }
        return new PhonemeBuilder((Set)phonemes);
    }
    
    public String encode(final String input) {
        final Languages.LanguageSet languageSet = this.lang.guessLanguages(input);
        return this.encode(input, languageSet);
    }
    
    public String encode(String input, final Languages.LanguageSet languageSet) {
        final Map<String, List<Rule>> rules = Rule.getInstanceMap(this.nameType, RuleType.RULES, languageSet);
        final Map<String, List<Rule>> finalRules1 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
        final Map<String, List<Rule>> finalRules2 = Rule.getInstanceMap(this.nameType, this.ruleType, languageSet);
        input = input.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
        if (this.nameType == NameType.GENERIC) {
            if (input.length() >= 2 && input.substring(0, 2).equals("d'")) {
                final String remainder = input.substring(2);
                final String combined = "d" + remainder;
                return "(" + this.encode(remainder) + ")-(" + this.encode(combined) + ")";
            }
            for (final String l : PhoneticEngine.NAME_PREFIXES.get(this.nameType)) {
                if (input.startsWith(l + " ")) {
                    final String remainder2 = input.substring(l.length() + 1);
                    final String combined2 = l + remainder2;
                    return "(" + this.encode(remainder2) + ")-(" + this.encode(combined2) + ")";
                }
            }
        }
        final List<String> words = Arrays.asList(input.split("\\s+"));
        final List<String> words2 = new ArrayList<String>();
        switch (this.nameType) {
            case SEPHARDIC: {
                for (final String aWord : words) {
                    final String[] parts = aWord.split("'");
                    final String lastPart = parts[parts.length - 1];
                    words2.add(lastPart);
                }
                words2.removeAll(PhoneticEngine.NAME_PREFIXES.get(this.nameType));
                break;
            }
            case ASHKENAZI: {
                words2.addAll(words);
                words2.removeAll(PhoneticEngine.NAME_PREFIXES.get(this.nameType));
                break;
            }
            case GENERIC: {
                words2.addAll(words);
                break;
            }
            default: {
                throw new IllegalStateException("Unreachable case: " + this.nameType);
            }
        }
        if (this.concat) {
            input = join(words2, " ");
        }
        else {
            if (words2.size() != 1) {
                final StringBuilder result = new StringBuilder();
                for (final String word : words2) {
                    result.append("-").append(this.encode(word));
                }
                return result.substring(1);
            }
            input = words.iterator().next();
        }
        PhonemeBuilder phonemeBuilder = PhonemeBuilder.empty(languageSet);
        RulesApplication rulesApplication;
        for (int i = 0; i < input.length(); i = rulesApplication.getI(), phonemeBuilder = rulesApplication.getPhonemeBuilder()) {
            rulesApplication = new RulesApplication(rules, input, phonemeBuilder, i, this.maxPhonemes).invoke();
        }
        phonemeBuilder = this.applyFinalRules(phonemeBuilder, finalRules1);
        phonemeBuilder = this.applyFinalRules(phonemeBuilder, finalRules2);
        return phonemeBuilder.makeString();
    }
    
    public Lang getLang() {
        return this.lang;
    }
    
    public NameType getNameType() {
        return this.nameType;
    }
    
    public RuleType getRuleType() {
        return this.ruleType;
    }
    
    public boolean isConcat() {
        return this.concat;
    }
    
    public int getMaxPhonemes() {
        return this.maxPhonemes;
    }
    
    static {
        (NAME_PREFIXES = new EnumMap<NameType, Set<String>>(NameType.class)).put(NameType.ASHKENAZI, Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("bar", "ben", "da", "de", "van", "von"))));
        PhoneticEngine.NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
        PhoneticEngine.NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
    }
    
    static final class PhonemeBuilder
    {
        private final Set<Rule.Phoneme> phonemes;
        
        public static PhonemeBuilder empty(final Languages.LanguageSet languages) {
            return new PhonemeBuilder(new Rule.Phoneme("", languages));
        }
        
        private PhonemeBuilder(final Rule.Phoneme phoneme) {
            (this.phonemes = new LinkedHashSet<Rule.Phoneme>()).add(phoneme);
        }
        
        private PhonemeBuilder(final Set<Rule.Phoneme> phonemes) {
            this.phonemes = phonemes;
        }
        
        public void append(final CharSequence str) {
            for (final Rule.Phoneme ph : this.phonemes) {
                ph.append(str);
            }
        }
        
        public void apply(final Rule.PhonemeExpr phonemeExpr, final int maxPhonemes) {
            final Set<Rule.Phoneme> newPhonemes = new LinkedHashSet<Rule.Phoneme>(maxPhonemes);
        Label_0153:
            for (final Rule.Phoneme left : this.phonemes) {
                for (final Rule.Phoneme right : phonemeExpr.getPhonemes()) {
                    final Languages.LanguageSet languages = left.getLanguages().restrictTo(right.getLanguages());
                    if (!languages.isEmpty()) {
                        final Rule.Phoneme join = new Rule.Phoneme(left, right, languages);
                        if (newPhonemes.size() >= maxPhonemes) {
                            continue;
                        }
                        newPhonemes.add(join);
                        if (newPhonemes.size() >= maxPhonemes) {
                            break Label_0153;
                        }
                        continue;
                    }
                }
            }
            this.phonemes.clear();
            this.phonemes.addAll(newPhonemes);
        }
        
        public Set<Rule.Phoneme> getPhonemes() {
            return this.phonemes;
        }
        
        public String makeString() {
            final StringBuilder sb = new StringBuilder();
            for (final Rule.Phoneme ph : this.phonemes) {
                if (sb.length() > 0) {
                    sb.append("|");
                }
                sb.append(ph.getPhonemeText());
            }
            return sb.toString();
        }
    }
    
    private static final class RulesApplication
    {
        private final Map<String, List<Rule>> finalRules;
        private final CharSequence input;
        private PhonemeBuilder phonemeBuilder;
        private int i;
        private final int maxPhonemes;
        private boolean found;
        
        public RulesApplication(final Map<String, List<Rule>> finalRules, final CharSequence input, final PhonemeBuilder phonemeBuilder, final int i, final int maxPhonemes) {
            if (finalRules == null) {
                throw new NullPointerException("The finalRules argument must not be null");
            }
            this.finalRules = finalRules;
            this.phonemeBuilder = phonemeBuilder;
            this.input = input;
            this.i = i;
            this.maxPhonemes = maxPhonemes;
        }
        
        public int getI() {
            return this.i;
        }
        
        public PhonemeBuilder getPhonemeBuilder() {
            return this.phonemeBuilder;
        }
        
        public RulesApplication invoke() {
            this.found = false;
            int patternLength = 1;
            final List<Rule> rules = this.finalRules.get(this.input.subSequence(this.i, this.i + patternLength));
            if (rules != null) {
                for (final Rule rule : rules) {
                    final String pattern = rule.getPattern();
                    patternLength = pattern.length();
                    if (rule.patternAndContextMatches(this.input, this.i)) {
                        this.phonemeBuilder.apply(rule.getPhoneme(), this.maxPhonemes);
                        this.found = true;
                        break;
                    }
                }
            }
            if (!this.found) {
                patternLength = 1;
            }
            this.i += patternLength;
            return this;
        }
        
        public boolean isFound() {
            return this.found;
        }
    }
}
