// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.HashMap;
import com.ibm.icu.impl.Row;
import java.util.Map;

public class LocaleMatcher
{
    private static final boolean DEBUG = false;
    private static final double DEFAULT_THRESHOLD = 0.5;
    private final ULocale defaultLanguage;
    Map<ULocale, Row.R2<ULocale, Double>> maximizedLanguageToWeight;
    LanguageMatcherData matcherData;
    private static LanguageMatcherData defaultWritten;
    private static HashMap<String, String> canonicalMap;
    
    public LocaleMatcher(final LocalePriorityList languagePriorityList) {
        this(languagePriorityList, LocaleMatcher.defaultWritten);
    }
    
    public LocaleMatcher(final String languagePriorityListString) {
        this(LocalePriorityList.add(languagePriorityListString).build());
    }
    
    @Deprecated
    public LocaleMatcher(final LocalePriorityList languagePriorityList, final LanguageMatcherData matcherData) {
        this.maximizedLanguageToWeight = new LinkedHashMap<ULocale, Row.R2<ULocale, Double>>();
        this.matcherData = matcherData;
        for (final ULocale language : languagePriorityList) {
            this.add(language, languagePriorityList.getWeight(language));
        }
        final Iterator<ULocale> it = languagePriorityList.iterator();
        this.defaultLanguage = (it.hasNext() ? it.next() : null);
    }
    
    public double match(final ULocale desired, final ULocale desiredMax, final ULocale supported, final ULocale supportedMax) {
        return this.matcherData.match(desired, desiredMax, supported, supportedMax);
    }
    
    public ULocale canonicalize(final ULocale ulocale) {
        final String lang = ulocale.getLanguage();
        final String lang2 = LocaleMatcher.canonicalMap.get(lang);
        final String script = ulocale.getScript();
        final String script2 = LocaleMatcher.canonicalMap.get(script);
        final String region = ulocale.getCountry();
        final String region2 = LocaleMatcher.canonicalMap.get(region);
        if (lang2 != null || script2 != null || region2 != null) {
            return new ULocale((lang2 == null) ? lang : lang2, (script2 == null) ? script : script2, (region2 == null) ? region : region2);
        }
        return ulocale;
    }
    
    public ULocale getBestMatch(final LocalePriorityList languageList) {
        double bestWeight = 0.0;
        ULocale bestTableMatch = null;
        for (final ULocale language : languageList) {
            final Row.R2<ULocale, Double> matchRow = this.getBestMatchInternal(language);
            final double weight = matchRow.get1() * languageList.getWeight(language);
            if (weight > bestWeight) {
                bestWeight = weight;
                bestTableMatch = matchRow.get0();
            }
        }
        if (bestWeight < 0.5) {
            bestTableMatch = this.defaultLanguage;
        }
        return bestTableMatch;
    }
    
    public ULocale getBestMatch(final String languageList) {
        return this.getBestMatch(LocalePriorityList.add(languageList).build());
    }
    
    public ULocale getBestMatch(final ULocale ulocale) {
        return this.getBestMatchInternal(ulocale).get0();
    }
    
    @Override
    public String toString() {
        return "{" + this.defaultLanguage + ", " + this.maximizedLanguageToWeight + "}";
    }
    
    private Row.R2<ULocale, Double> getBestMatchInternal(ULocale languageCode) {
        languageCode = this.canonicalize(languageCode);
        final ULocale maximized = this.addLikelySubtags(languageCode);
        double bestWeight = 0.0;
        ULocale bestTableMatch = null;
        for (final ULocale tableKey : this.maximizedLanguageToWeight.keySet()) {
            final Row.R2<ULocale, Double> row = this.maximizedLanguageToWeight.get(tableKey);
            final double match = this.match(languageCode, maximized, tableKey, row.get0());
            final double weight = match * row.get1();
            if (weight > bestWeight) {
                bestWeight = weight;
                bestTableMatch = tableKey;
            }
        }
        if (bestWeight < 0.5) {
            bestTableMatch = this.defaultLanguage;
        }
        return Row.of(bestTableMatch, bestWeight);
    }
    
    private void add(ULocale language, final Double weight) {
        language = this.canonicalize(language);
        final Row.R2<ULocale, Double> row = Row.of(this.addLikelySubtags(language), weight);
        this.maximizedLanguageToWeight.put(language, row);
    }
    
    private ULocale addLikelySubtags(final ULocale languageCode) {
        final ULocale result = ULocale.addLikelySubtags(languageCode);
        if (result == null || result.equals(languageCode)) {
            final String language = languageCode.getLanguage();
            final String script = languageCode.getScript();
            final String region = languageCode.getCountry();
            return new ULocale(((language.length() == 0) ? "und" : language) + "_" + ((script.length() == 0) ? "Zzzz" : script) + "_" + ((region.length() == 0) ? "ZZ" : region));
        }
        return result;
    }
    
    static {
        LocaleMatcher.defaultWritten = new LanguageMatcherData().addDistance("no", "nb", 100, "The language no is normally taken as nb in content; we might alias this for lookup.").addDistance("nn", "nb", 96).addDistance("nn", "no", 96).addDistance("da", "no", 90, "Danish and norwegian are reasonably close.").addDistance("da", "nb", 90).addDistance("hr", "br", 96, "Serbo-croatian variants are all very close.").addDistance("sh", "br", 96).addDistance("sr", "br", 96).addDistance("sh", "hr", 96).addDistance("sr", "hr", 96).addDistance("sh", "sr", 96).addDistance("sr-Latn", "sr-Cyrl", 90, "Most serbs can read either script.").addDistance("*-Hans", "*-Hant", 85, true, "Readers of simplified can read traditional much better than reverse.").addDistance("*-Hant", "*-Hans", 75, true).addDistance("en-*-US", "en-*-CA", 98, "US is different than others, and Canadian is inbetween.").addDistance("en-*-US", "en-*-*", 97).addDistance("en-*-CA", "en-*-*", 98).addDistance("en-*-*", "en-*-*", 99).addDistance("es-*-ES", "es-*-ES", 100, "Latin American Spanishes are closer to each other. Approximate by having es-ES be further from everything else.").addDistance("es-*-ES", "es-*-*", 93).addDistance("*", "*", 1, "[Default value -- must be at end!] Normally there is no comprehension of different languages.").addDistance("*-*", "*-*", 20, "[Default value -- must be at end!] Normally there is little comprehension of different scripts.").addDistance("*-*-*", "*-*-*", 96, "[Default value -- must be at end!] Normally there are small differences across regions.").freeze();
        (LocaleMatcher.canonicalMap = new HashMap<String, String>()).put("iw", "he");
        LocaleMatcher.canonicalMap.put("mo", "ro");
        LocaleMatcher.canonicalMap.put("tl", "fil");
    }
    
    private static class LocalePatternMatcher
    {
        private String lang;
        private String script;
        private String region;
        private Level level;
        static Pattern pattern;
        
        public LocalePatternMatcher(final String toMatch) {
            final Matcher matcher = LocalePatternMatcher.pattern.matcher(toMatch);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Bad pattern: " + toMatch);
            }
            this.lang = matcher.group(1);
            this.script = matcher.group(2);
            this.region = matcher.group(3);
            this.level = ((this.region != null) ? Level.region : ((this.script != null) ? Level.script : Level.language));
            if (this.lang.equals("*")) {
                this.lang = null;
            }
            if (this.script != null && this.script.equals("*")) {
                this.script = null;
            }
            if (this.region != null && this.region.equals("*")) {
                this.region = null;
            }
        }
        
        boolean matches(final ULocale ulocale) {
            return (this.lang == null || this.lang.equals(ulocale.getLanguage())) && (this.script == null || this.script.equals(ulocale.getScript())) && (this.region == null || this.region.equals(ulocale.getCountry()));
        }
        
        public Level getLevel() {
            return this.level;
        }
        
        public String getLanguage() {
            return (this.lang == null) ? "*" : this.lang;
        }
        
        public String getScript() {
            return (this.script == null) ? "*" : this.script;
        }
        
        public String getRegion() {
            return (this.region == null) ? "*" : this.region;
        }
        
        @Override
        public String toString() {
            String result = this.getLanguage();
            if (this.level != Level.language) {
                result = result + "-" + this.getScript();
                if (this.level != Level.script) {
                    result = result + "-" + this.getRegion();
                }
            }
            return result;
        }
        
        static {
            LocalePatternMatcher.pattern = Pattern.compile("([a-zA-Z]{1,8}|\\*)(?:-([a-zA-Z]{4}|\\*))?(?:-([a-zA-Z]{2}|[0-9]{3}|\\*))?");
        }
    }
    
    enum Level
    {
        language, 
        script, 
        region;
    }
    
    private static class ScoreData implements Freezable<ScoreData>
    {
        LinkedHashSet<Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double>> scores;
        final double worst;
        final Level level;
        private boolean frozen;
        
        public ScoreData(final Level level) {
            this.scores = new LinkedHashSet<Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double>>();
            this.frozen = false;
            this.level = level;
            this.worst = (1 - ((level == Level.language) ? 90 : ((level == Level.script) ? 20 : 4))) / 100.0;
        }
        
        void addDataToScores(final String desired, final String supported, final Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data) {
            this.scores.add(data);
        }
        
        double getScore(final ULocale desiredLocale, final ULocale dMax, final String desiredRaw, final String desiredMax, final ULocale supportedLocale, final ULocale sMax, final String supportedRaw, final String supportedMax) {
            final boolean desiredChange = desiredRaw.equals(desiredMax);
            final boolean supportedChange = supportedRaw.equals(supportedMax);
            double distance;
            if (!desiredMax.equals(supportedMax)) {
                distance = this.getRawScore(dMax, sMax);
                if (desiredChange == supportedChange) {
                    distance *= 0.75;
                }
                else if (desiredChange) {
                    distance *= 0.5;
                }
            }
            else if (desiredChange == supportedChange) {
                distance = 0.0;
            }
            else {
                distance = 0.25 * this.worst;
            }
            return distance;
        }
        
        private double getRawScore(final ULocale desiredLocale, final ULocale supportedLocale) {
            for (final Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> datum : this.scores) {
                if (datum.get0().matches(desiredLocale) && datum.get1().matches(supportedLocale)) {
                    return datum.get2();
                }
            }
            return this.worst;
        }
        
        @Override
        public String toString() {
            return this.level + ", " + this.scores;
        }
        
        public ScoreData cloneAsThawed() {
            try {
                final ScoreData result = (ScoreData)this.clone();
                result.scores = (LinkedHashSet<Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double>>)result.scores.clone();
                result.frozen = false;
                return result;
            }
            catch (CloneNotSupportedException e) {
                throw new IllegalArgumentException(e);
            }
        }
        
        public ScoreData freeze() {
            return this;
        }
        
        public boolean isFrozen() {
            return this.frozen;
        }
    }
    
    public static class LanguageMatcherData implements Freezable<LanguageMatcherData>
    {
        ScoreData languageScores;
        ScoreData scriptScores;
        ScoreData regionScores;
        private boolean frozen;
        
        @Deprecated
        public LanguageMatcherData() {
            this.languageScores = new ScoreData(Level.language);
            this.scriptScores = new ScoreData(Level.script);
            this.regionScores = new ScoreData(Level.region);
            this.frozen = false;
        }
        
        @Deprecated
        public double match(final ULocale a, final ULocale aMax, final ULocale b, final ULocale bMax) {
            double diff = 0.0;
            diff += this.languageScores.getScore(a, aMax, a.getLanguage(), aMax.getLanguage(), b, bMax, b.getLanguage(), bMax.getLanguage());
            diff += this.scriptScores.getScore(a, aMax, a.getScript(), aMax.getScript(), b, bMax, b.getScript(), bMax.getScript());
            diff += this.regionScores.getScore(a, aMax, a.getCountry(), aMax.getCountry(), b, bMax, b.getCountry(), bMax.getCountry());
            if (!a.getVariant().equals(b.getVariant())) {
                ++diff;
            }
            if (diff < 0.0) {
                diff = 0.0;
            }
            else if (diff > 1.0) {
                diff = 1.0;
            }
            return 1.0 - diff;
        }
        
        @Deprecated
        private LanguageMatcherData addDistance(final String desired, final String supported, final int percent) {
            return this.addDistance(desired, supported, percent, false, null);
        }
        
        @Deprecated
        public LanguageMatcherData addDistance(final String desired, final String supported, final int percent, final String comment) {
            return this.addDistance(desired, supported, percent, false, comment);
        }
        
        @Deprecated
        public LanguageMatcherData addDistance(final String desired, final String supported, final int percent, final boolean oneway) {
            return this.addDistance(desired, supported, percent, oneway, null);
        }
        
        private LanguageMatcherData addDistance(final String desired, final String supported, final int percent, final boolean oneway, final String comment) {
            final double score = 1.0 - percent / 100.0;
            final LocalePatternMatcher desiredMatcher = new LocalePatternMatcher(desired);
            final Level desiredLen = desiredMatcher.getLevel();
            final LocalePatternMatcher supportedMatcher = new LocalePatternMatcher(supported);
            final Level supportedLen = supportedMatcher.getLevel();
            if (desiredLen != supportedLen) {
                throw new IllegalArgumentException();
            }
            final Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data = Row.of(desiredMatcher, supportedMatcher, score);
            final Row.R3<LocalePatternMatcher, LocalePatternMatcher, Double> data2 = oneway ? null : Row.of(supportedMatcher, desiredMatcher, score);
            switch (desiredLen) {
                case language: {
                    final String dlanguage = desiredMatcher.getLanguage();
                    final String slanguage = supportedMatcher.getLanguage();
                    this.languageScores.addDataToScores(dlanguage, slanguage, data);
                    if (!oneway) {
                        this.languageScores.addDataToScores(slanguage, dlanguage, data2);
                        break;
                    }
                    break;
                }
                case script: {
                    final String dscript = desiredMatcher.getScript();
                    final String sscript = supportedMatcher.getScript();
                    this.scriptScores.addDataToScores(dscript, sscript, data);
                    if (!oneway) {
                        this.scriptScores.addDataToScores(sscript, dscript, data2);
                        break;
                    }
                    break;
                }
                case region: {
                    final String dregion = desiredMatcher.getRegion();
                    final String sregion = supportedMatcher.getRegion();
                    this.regionScores.addDataToScores(dregion, sregion, data);
                    if (!oneway) {
                        this.regionScores.addDataToScores(sregion, dregion, data2);
                        break;
                    }
                    break;
                }
            }
            return this;
        }
        
        @Deprecated
        public LanguageMatcherData cloneAsThawed() {
            try {
                final LanguageMatcherData result = (LanguageMatcherData)this.clone();
                result.languageScores = this.languageScores.cloneAsThawed();
                result.scriptScores = this.scriptScores.cloneAsThawed();
                result.regionScores = this.regionScores.cloneAsThawed();
                result.frozen = false;
                return result;
            }
            catch (CloneNotSupportedException e) {
                throw new IllegalArgumentException(e);
            }
        }
        
        @Deprecated
        public LanguageMatcherData freeze() {
            return this;
        }
        
        @Deprecated
        public boolean isFrozen() {
            return this.frozen;
        }
    }
}
