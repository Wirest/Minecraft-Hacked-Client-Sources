// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

import java.util.HashSet;
import java.util.Collection;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtils
{
    private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry;
    private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage;
    
    public static Locale toLocale(final String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return new Locale("", "");
        }
        if (str.contains("#")) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final int len = str.length();
        if (len < 2) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch0 = str.charAt(0);
        if (ch0 != '_') {
            final String[] split = str.split("_", -1);
            final int occurrences = split.length - 1;
            switch (occurrences) {
                case 0: {
                    if (StringUtils.isAllLowerCase(str) && (len == 2 || len == 3)) {
                        return new Locale(str);
                    }
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                case 1: {
                    if (StringUtils.isAllLowerCase(split[0]) && (split[0].length() == 2 || split[0].length() == 3) && split[1].length() == 2 && StringUtils.isAllUpperCase(split[1])) {
                        return new Locale(split[0], split[1]);
                    }
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                case 2: {
                    if (StringUtils.isAllLowerCase(split[0]) && (split[0].length() == 2 || split[0].length() == 3) && (split[1].length() == 0 || (split[1].length() == 2 && StringUtils.isAllUpperCase(split[1]))) && split[2].length() > 0) {
                        return new Locale(split[0], split[1], split[2]);
                    }
                    break;
                }
            }
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len < 3) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch2 = str.charAt(1);
        final char ch3 = str.charAt(2);
        if (!Character.isUpperCase(ch2) || !Character.isUpperCase(ch3)) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 3) {
            return new Locale("", str.substring(1, 3));
        }
        if (len < 5) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (str.charAt(3) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale("", str.substring(1, 3), str.substring(4));
    }
    
    public static List<Locale> localeLookupList(final Locale locale) {
        return localeLookupList(locale, locale);
    }
    
    public static List<Locale> localeLookupList(final Locale locale, final Locale defaultLocale) {
        final List<Locale> list = new ArrayList<Locale>(4);
        if (locale != null) {
            list.add(locale);
            if (locale.getVariant().length() > 0) {
                list.add(new Locale(locale.getLanguage(), locale.getCountry()));
            }
            if (locale.getCountry().length() > 0) {
                list.add(new Locale(locale.getLanguage(), ""));
            }
            if (!list.contains(defaultLocale)) {
                list.add(defaultLocale);
            }
        }
        return Collections.unmodifiableList((List<? extends Locale>)list);
    }
    
    public static List<Locale> availableLocaleList() {
        return SyncAvoid.AVAILABLE_LOCALE_LIST;
    }
    
    public static Set<Locale> availableLocaleSet() {
        return SyncAvoid.AVAILABLE_LOCALE_SET;
    }
    
    public static boolean isAvailableLocale(final Locale locale) {
        return availableLocaleList().contains(locale);
    }
    
    public static List<Locale> languagesByCountry(final String countryCode) {
        if (countryCode == null) {
            return Collections.emptyList();
        }
        List<Locale> langs = LocaleUtils.cLanguagesByCountry.get(countryCode);
        if (langs == null) {
            langs = new ArrayList<Locale>();
            final List<Locale> locales = availableLocaleList();
            for (int i = 0; i < locales.size(); ++i) {
                final Locale locale = locales.get(i);
                if (countryCode.equals(locale.getCountry()) && locale.getVariant().isEmpty()) {
                    langs.add(locale);
                }
            }
            langs = Collections.unmodifiableList((List<? extends Locale>)langs);
            LocaleUtils.cLanguagesByCountry.putIfAbsent(countryCode, langs);
            langs = LocaleUtils.cLanguagesByCountry.get(countryCode);
        }
        return langs;
    }
    
    public static List<Locale> countriesByLanguage(final String languageCode) {
        if (languageCode == null) {
            return Collections.emptyList();
        }
        List<Locale> countries = LocaleUtils.cCountriesByLanguage.get(languageCode);
        if (countries == null) {
            countries = new ArrayList<Locale>();
            final List<Locale> locales = availableLocaleList();
            for (int i = 0; i < locales.size(); ++i) {
                final Locale locale = locales.get(i);
                if (languageCode.equals(locale.getLanguage()) && locale.getCountry().length() != 0 && locale.getVariant().isEmpty()) {
                    countries.add(locale);
                }
            }
            countries = Collections.unmodifiableList((List<? extends Locale>)countries);
            LocaleUtils.cCountriesByLanguage.putIfAbsent(languageCode, countries);
            countries = LocaleUtils.cCountriesByLanguage.get(languageCode);
        }
        return countries;
    }
    
    static {
        cLanguagesByCountry = new ConcurrentHashMap<String, List<Locale>>();
        cCountriesByLanguage = new ConcurrentHashMap<String, List<Locale>>();
    }
    
    static class SyncAvoid
    {
        private static final List<Locale> AVAILABLE_LOCALE_LIST;
        private static final Set<Locale> AVAILABLE_LOCALE_SET;
        
        static {
            final List<Locale> list = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList((List<? extends Locale>)list);
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet((Set<? extends Locale>)new HashSet<Locale>(list));
        }
    }
}
