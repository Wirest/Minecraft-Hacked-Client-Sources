// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language.bm;

import java.util.Comparator;
import java.util.Collections;
import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class Rule
{
    public static final RPattern ALL_STRINGS_RMATCHER;
    public static final String ALL = "ALL";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String HASH_INCLUDE = "#include";
    private static final Map<NameType, Map<RuleType, Map<String, Map<String, List<Rule>>>>> RULES;
    private final RPattern lContext;
    private final String pattern;
    private final PhonemeExpr phoneme;
    private final RPattern rContext;
    
    private static boolean contains(final CharSequence chars, final char input) {
        for (int i = 0; i < chars.length(); ++i) {
            if (chars.charAt(i) == input) {
                return true;
            }
        }
        return false;
    }
    
    private static String createResourceName(final NameType nameType, final RuleType rt, final String lang) {
        return String.format("org/apache/commons/codec/language/bm/%s_%s_%s.txt", nameType.getName(), rt.getName(), lang);
    }
    
    private static Scanner createScanner(final NameType nameType, final RuleType rt, final String lang) {
        final String resName = createResourceName(nameType, rt, lang);
        final InputStream rulesIS = Languages.class.getClassLoader().getResourceAsStream(resName);
        if (rulesIS == null) {
            throw new IllegalArgumentException("Unable to load resource: " + resName);
        }
        return new Scanner(rulesIS, "UTF-8");
    }
    
    private static Scanner createScanner(final String lang) {
        final String resName = String.format("org/apache/commons/codec/language/bm/%s.txt", lang);
        final InputStream rulesIS = Languages.class.getClassLoader().getResourceAsStream(resName);
        if (rulesIS == null) {
            throw new IllegalArgumentException("Unable to load resource: " + resName);
        }
        return new Scanner(rulesIS, "UTF-8");
    }
    
    private static boolean endsWith(final CharSequence input, final CharSequence suffix) {
        if (suffix.length() > input.length()) {
            return false;
        }
        int i = input.length() - 1;
        for (int j = suffix.length() - 1; j >= 0; --j) {
            if (input.charAt(i) != suffix.charAt(j)) {
                return false;
            }
            --i;
        }
        return true;
    }
    
    public static List<Rule> getInstance(final NameType nameType, final RuleType rt, final Languages.LanguageSet langs) {
        final Map<String, List<Rule>> ruleMap = getInstanceMap(nameType, rt, langs);
        final List<Rule> allRules = new ArrayList<Rule>();
        for (final List<Rule> rules : ruleMap.values()) {
            allRules.addAll(rules);
        }
        return allRules;
    }
    
    public static List<Rule> getInstance(final NameType nameType, final RuleType rt, final String lang) {
        return getInstance(nameType, rt, Languages.LanguageSet.from(new HashSet<String>(Arrays.asList(lang))));
    }
    
    public static Map<String, List<Rule>> getInstanceMap(final NameType nameType, final RuleType rt, final Languages.LanguageSet langs) {
        return langs.isSingleton() ? getInstanceMap(nameType, rt, langs.getAny()) : getInstanceMap(nameType, rt, "any");
    }
    
    public static Map<String, List<Rule>> getInstanceMap(final NameType nameType, final RuleType rt, final String lang) {
        final Map<String, List<Rule>> rules = Rule.RULES.get(nameType).get(rt).get(lang);
        if (rules == null) {
            throw new IllegalArgumentException(String.format("No rules found for %s, %s, %s.", nameType.getName(), rt.getName(), lang));
        }
        return rules;
    }
    
    private static Phoneme parsePhoneme(final String ph) {
        final int open = ph.indexOf("[");
        if (open < 0) {
            return new Phoneme(ph, Languages.ANY_LANGUAGE);
        }
        if (!ph.endsWith("]")) {
            throw new IllegalArgumentException("Phoneme expression contains a '[' but does not end in ']'");
        }
        final String before = ph.substring(0, open);
        final String in = ph.substring(open + 1, ph.length() - 1);
        final Set<String> langs = new HashSet<String>(Arrays.asList(in.split("[+]")));
        return new Phoneme(before, Languages.LanguageSet.from(langs));
    }
    
    private static PhonemeExpr parsePhonemeExpr(final String ph) {
        if (!ph.startsWith("(")) {
            return parsePhoneme(ph);
        }
        if (!ph.endsWith(")")) {
            throw new IllegalArgumentException("Phoneme starts with '(' so must end with ')'");
        }
        final List<Phoneme> phs = new ArrayList<Phoneme>();
        final String body = ph.substring(1, ph.length() - 1);
        for (final String part : body.split("[|]")) {
            phs.add(parsePhoneme(part));
        }
        if (body.startsWith("|") || body.endsWith("|")) {
            phs.add(new Phoneme("", Languages.ANY_LANGUAGE));
        }
        return new PhonemeList(phs);
    }
    
    private static Map<String, List<Rule>> parseRules(final Scanner scanner, final String location) {
        final Map<String, List<Rule>> lines = new HashMap<String, List<Rule>>();
        int currentLine = 0;
        boolean inMultilineComment = false;
        while (scanner.hasNextLine()) {
            ++currentLine;
            String line;
            final String rawLine = line = scanner.nextLine();
            if (inMultilineComment) {
                if (!line.endsWith("*/")) {
                    continue;
                }
                inMultilineComment = false;
            }
            else if (line.startsWith("/*")) {
                inMultilineComment = true;
            }
            else {
                final int cmtI = line.indexOf("//");
                if (cmtI >= 0) {
                    line = line.substring(0, cmtI);
                }
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("#include")) {
                    final String incl = line.substring("#include".length()).trim();
                    if (incl.contains(" ")) {
                        throw new IllegalArgumentException("Malformed import statement '" + rawLine + "' in " + location);
                    }
                    lines.putAll(parseRules(createScanner(incl), location + "->" + incl));
                }
                else {
                    final String[] parts = line.split("\\s+");
                    if (parts.length != 4) {
                        throw new IllegalArgumentException("Malformed rule statement split into " + parts.length + " parts: " + rawLine + " in " + location);
                    }
                    try {
                        final String pat = stripQuotes(parts[0]);
                        final String lCon = stripQuotes(parts[1]);
                        final String rCon = stripQuotes(parts[2]);
                        final PhonemeExpr ph = parsePhonemeExpr(stripQuotes(parts[3]));
                        final int cLine = currentLine;
                        final Rule r = new Rule(pat, lCon, rCon, ph) {
                            private final int myLine = cLine;
                            private final String loc = location;
                            
                            @Override
                            public String toString() {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Rule");
                                sb.append("{line=").append(this.myLine);
                                sb.append(", loc='").append(this.loc).append('\'');
                                sb.append('}');
                                return sb.toString();
                            }
                        };
                        final String patternKey = r.pattern.substring(0, 1);
                        List<Rule> rules = lines.get(patternKey);
                        if (rules == null) {
                            rules = new ArrayList<Rule>();
                            lines.put(patternKey, rules);
                        }
                        rules.add(r);
                    }
                    catch (IllegalArgumentException e) {
                        throw new IllegalStateException("Problem parsing line '" + currentLine + "' in " + location, e);
                    }
                }
            }
        }
        return lines;
    }
    
    private static RPattern pattern(final String regex) {
        final boolean startsWith = regex.startsWith("^");
        final boolean endsWith = regex.endsWith("$");
        final String content = regex.substring(startsWith ? 1 : 0, endsWith ? (regex.length() - 1) : regex.length());
        final boolean boxes = content.contains("[");
        if (!boxes) {
            if (startsWith && endsWith) {
                if (content.length() == 0) {
                    return new RPattern() {
                        @Override
                        public boolean isMatch(final CharSequence input) {
                            return input.length() == 0;
                        }
                    };
                }
                return new RPattern() {
                    @Override
                    public boolean isMatch(final CharSequence input) {
                        return input.equals(content);
                    }
                };
            }
            else {
                if ((startsWith || endsWith) && content.length() == 0) {
                    return Rule.ALL_STRINGS_RMATCHER;
                }
                if (startsWith) {
                    return new RPattern() {
                        @Override
                        public boolean isMatch(final CharSequence input) {
                            return startsWith(input, content);
                        }
                    };
                }
                if (endsWith) {
                    return new RPattern() {
                        @Override
                        public boolean isMatch(final CharSequence input) {
                            return endsWith(input, content);
                        }
                    };
                }
            }
        }
        else {
            final boolean startsWithBox = content.startsWith("[");
            final boolean endsWithBox = content.endsWith("]");
            if (startsWithBox && endsWithBox) {
                String boxContent = content.substring(1, content.length() - 1);
                if (!boxContent.contains("[")) {
                    final boolean negate = boxContent.startsWith("^");
                    if (negate) {
                        boxContent = boxContent.substring(1);
                    }
                    final String bContent = boxContent;
                    final boolean shouldMatch = !negate;
                    if (startsWith && endsWith) {
                        return new RPattern() {
                            @Override
                            public boolean isMatch(final CharSequence input) {
                                return input.length() == 1 && contains(bContent, input.charAt(0)) == shouldMatch;
                            }
                        };
                    }
                    if (startsWith) {
                        return new RPattern() {
                            @Override
                            public boolean isMatch(final CharSequence input) {
                                return input.length() > 0 && contains(bContent, input.charAt(0)) == shouldMatch;
                            }
                        };
                    }
                    if (endsWith) {
                        return new RPattern() {
                            @Override
                            public boolean isMatch(final CharSequence input) {
                                return input.length() > 0 && contains(bContent, input.charAt(input.length() - 1)) == shouldMatch;
                            }
                        };
                    }
                }
            }
        }
        return new RPattern() {
            Pattern pattern = Pattern.compile(regex);
            
            @Override
            public boolean isMatch(final CharSequence input) {
                final Matcher matcher = this.pattern.matcher(input);
                return matcher.find();
            }
        };
    }
    
    private static boolean startsWith(final CharSequence input, final CharSequence prefix) {
        if (prefix.length() > input.length()) {
            return false;
        }
        for (int i = 0; i < prefix.length(); ++i) {
            if (input.charAt(i) != prefix.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    private static String stripQuotes(String str) {
        if (str.startsWith("\"")) {
            str = str.substring(1);
        }
        if (str.endsWith("\"")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    
    public Rule(final String pattern, final String lContext, final String rContext, final PhonemeExpr phoneme) {
        this.pattern = pattern;
        this.lContext = pattern(lContext + "$");
        this.rContext = pattern("^" + rContext);
        this.phoneme = phoneme;
    }
    
    public RPattern getLContext() {
        return this.lContext;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    
    public PhonemeExpr getPhoneme() {
        return this.phoneme;
    }
    
    public RPattern getRContext() {
        return this.rContext;
    }
    
    public boolean patternAndContextMatches(final CharSequence input, final int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Can not match pattern at negative indexes");
        }
        final int patternLength = this.pattern.length();
        final int ipl = i + patternLength;
        return ipl <= input.length() && input.subSequence(i, ipl).equals(this.pattern) && this.rContext.isMatch(input.subSequence(ipl, input.length())) && this.lContext.isMatch(input.subSequence(0, i));
    }
    
    static {
        ALL_STRINGS_RMATCHER = new RPattern() {
            @Override
            public boolean isMatch(final CharSequence input) {
                return true;
            }
        };
        RULES = new EnumMap<NameType, Map<RuleType, Map<String, Map<String, List<Rule>>>>>(NameType.class);
        for (final NameType s : NameType.values()) {
            final Map<RuleType, Map<String, Map<String, List<Rule>>>> rts = new EnumMap<RuleType, Map<String, Map<String, List<Rule>>>>(RuleType.class);
            for (final RuleType rt : RuleType.values()) {
                final Map<String, Map<String, List<Rule>>> rs = new HashMap<String, Map<String, List<Rule>>>();
                final Languages ls = Languages.getInstance(s);
                for (final String l : ls.getLanguages()) {
                    try {
                        rs.put(l, parseRules(createScanner(s, rt, l), createResourceName(s, rt, l)));
                    }
                    catch (IllegalStateException e) {
                        throw new IllegalStateException("Problem processing " + createResourceName(s, rt, l), e);
                    }
                }
                if (!rt.equals(RuleType.RULES)) {
                    rs.put("common", parseRules(createScanner(s, rt, "common"), createResourceName(s, rt, "common")));
                }
                rts.put(rt, Collections.unmodifiableMap((Map<? extends String, ? extends Map<String, List<Rule>>>)rs));
            }
            Rule.RULES.put(s, Collections.unmodifiableMap((Map<? extends RuleType, ? extends Map<String, Map<String, List<Rule>>>>)rts));
        }
    }
    
    public static final class Phoneme implements PhonemeExpr
    {
        public static final Comparator<Phoneme> COMPARATOR;
        private final StringBuilder phonemeText;
        private final Languages.LanguageSet languages;
        
        public Phoneme(final CharSequence phonemeText, final Languages.LanguageSet languages) {
            this.phonemeText = new StringBuilder(phonemeText);
            this.languages = languages;
        }
        
        public Phoneme(final Phoneme phonemeLeft, final Phoneme phonemeRight) {
            this(phonemeLeft.phonemeText, phonemeLeft.languages);
            this.phonemeText.append((CharSequence)phonemeRight.phonemeText);
        }
        
        public Phoneme(final Phoneme phonemeLeft, final Phoneme phonemeRight, final Languages.LanguageSet languages) {
            this(phonemeLeft.phonemeText, languages);
            this.phonemeText.append((CharSequence)phonemeRight.phonemeText);
        }
        
        public Phoneme append(final CharSequence str) {
            this.phonemeText.append(str);
            return this;
        }
        
        public Languages.LanguageSet getLanguages() {
            return this.languages;
        }
        
        @Override
        public Iterable<Phoneme> getPhonemes() {
            return Collections.singleton(this);
        }
        
        public CharSequence getPhonemeText() {
            return this.phonemeText;
        }
        
        @Deprecated
        public Phoneme join(final Phoneme right) {
            return new Phoneme(this.phonemeText.toString() + right.phonemeText.toString(), this.languages.restrictTo(right.languages));
        }
        
        static {
            COMPARATOR = new Comparator<Phoneme>() {
                @Override
                public int compare(final Phoneme o1, final Phoneme o2) {
                    for (int i = 0; i < o1.phonemeText.length(); ++i) {
                        if (i >= o2.phonemeText.length()) {
                            return 1;
                        }
                        final int c = o1.phonemeText.charAt(i) - o2.phonemeText.charAt(i);
                        if (c != 0) {
                            return c;
                        }
                    }
                    if (o1.phonemeText.length() < o2.phonemeText.length()) {
                        return -1;
                    }
                    return 0;
                }
            };
        }
    }
    
    public static final class PhonemeList implements PhonemeExpr
    {
        private final List<Phoneme> phonemes;
        
        public PhonemeList(final List<Phoneme> phonemes) {
            this.phonemes = phonemes;
        }
        
        @Override
        public List<Phoneme> getPhonemes() {
            return this.phonemes;
        }
    }
    
    public interface RPattern
    {
        boolean isMatch(final CharSequence p0);
    }
    
    public interface PhonemeExpr
    {
        Iterable<Phoneme> getPhonemes();
    }
}
