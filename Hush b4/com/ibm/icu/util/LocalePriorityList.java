// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.regex.Matcher;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;

public class LocalePriorityList implements Iterable<ULocale>
{
    private static final double D0 = 0.0;
    private static final Double D1;
    private static final Pattern languageSplitter;
    private static final Pattern weightSplitter;
    private final Map<ULocale, Double> languagesAndWeights;
    private static Comparator<Double> myDescendingDouble;
    
    public static Builder add(final ULocale languageCode) {
        return new Builder().add(languageCode);
    }
    
    public static Builder add(final ULocale languageCode, final double weight) {
        return new Builder().add(languageCode, weight);
    }
    
    public static Builder add(final LocalePriorityList languagePriorityList) {
        return new Builder().add(languagePriorityList);
    }
    
    public static Builder add(final String acceptLanguageString) {
        return new Builder().add(acceptLanguageString);
    }
    
    public Double getWeight(final ULocale language) {
        return this.languagesAndWeights.get(language);
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (final ULocale language : this.languagesAndWeights.keySet()) {
            if (result.length() != 0) {
                result.append(", ");
            }
            result.append(language);
            final double weight = this.languagesAndWeights.get(language);
            if (weight != LocalePriorityList.D1) {
                result.append(";q=").append(weight);
            }
        }
        return result.toString();
    }
    
    public Iterator<ULocale> iterator() {
        return this.languagesAndWeights.keySet().iterator();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        try {
            final LocalePriorityList that = (LocalePriorityList)o;
            return this.languagesAndWeights.equals(that.languagesAndWeights);
        }
        catch (RuntimeException e) {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return this.languagesAndWeights.hashCode();
    }
    
    private LocalePriorityList(final Map<ULocale, Double> languageToWeight) {
        this.languagesAndWeights = languageToWeight;
    }
    
    static {
        D1 = 1.0;
        languageSplitter = Pattern.compile("\\s*,\\s*");
        weightSplitter = Pattern.compile("\\s*(\\S*)\\s*;\\s*q\\s*=\\s*(\\S*)");
        LocalePriorityList.myDescendingDouble = new Comparator<Double>() {
            public int compare(final Double o1, final Double o2) {
                return -o1.compareTo(o2);
            }
        };
    }
    
    public static class Builder
    {
        private final Map<ULocale, Double> languageToWeight;
        
        private Builder() {
            this.languageToWeight = new LinkedHashMap<ULocale, Double>();
        }
        
        public LocalePriorityList build() {
            return this.build(false);
        }
        
        public LocalePriorityList build(final boolean preserveWeights) {
            final Map<Double, Set<ULocale>> doubleCheck = new TreeMap<Double, Set<ULocale>>(LocalePriorityList.myDescendingDouble);
            for (final ULocale lang : this.languageToWeight.keySet()) {
                final Double weight = this.languageToWeight.get(lang);
                Set<ULocale> s = doubleCheck.get(weight);
                if (s == null) {
                    doubleCheck.put(weight, s = new LinkedHashSet<ULocale>());
                }
                s.add(lang);
            }
            final Map<ULocale, Double> temp = new LinkedHashMap<ULocale, Double>();
            for (final Map.Entry<Double, Set<ULocale>> langEntry : doubleCheck.entrySet()) {
                final Double weight2 = langEntry.getKey();
                for (final ULocale lang2 : langEntry.getValue()) {
                    temp.put(lang2, preserveWeights ? weight2 : LocalePriorityList.D1);
                }
            }
            return new LocalePriorityList(Collections.unmodifiableMap((Map<?, ?>)temp), null);
        }
        
        public Builder add(final LocalePriorityList languagePriorityList) {
            for (final ULocale language : languagePriorityList.languagesAndWeights.keySet()) {
                this.add(language, languagePriorityList.languagesAndWeights.get(language));
            }
            return this;
        }
        
        public Builder add(final ULocale languageCode) {
            return this.add(languageCode, LocalePriorityList.D1);
        }
        
        public Builder add(final ULocale... languageCodes) {
            for (final ULocale languageCode : languageCodes) {
                this.add(languageCode, LocalePriorityList.D1);
            }
            return this;
        }
        
        public Builder add(final ULocale languageCode, double weight) {
            if (this.languageToWeight.containsKey(languageCode)) {
                this.languageToWeight.remove(languageCode);
            }
            if (weight <= 0.0) {
                return this;
            }
            if (weight > LocalePriorityList.D1) {
                weight = LocalePriorityList.D1;
            }
            this.languageToWeight.put(languageCode, weight);
            return this;
        }
        
        public Builder add(final String acceptLanguageList) {
            final String[] items = LocalePriorityList.languageSplitter.split(acceptLanguageList.trim());
            final Matcher itemMatcher = LocalePriorityList.weightSplitter.matcher("");
            for (final String item : items) {
                if (itemMatcher.reset(item).matches()) {
                    final ULocale language = new ULocale(itemMatcher.group(1));
                    final double weight = Double.parseDouble(itemMatcher.group(2));
                    if (weight < 0.0 || weight > LocalePriorityList.D1) {
                        throw new IllegalArgumentException("Illegal weight, must be 0..1: " + weight);
                    }
                    this.add(language, weight);
                }
                else if (item.length() != 0) {
                    this.add(new ULocale(item));
                }
            }
            return this;
        }
    }
}
