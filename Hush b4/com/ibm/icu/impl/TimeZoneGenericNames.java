// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Collection;
import com.ibm.icu.text.TimeZoneFormat;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.text.MessageFormat;
import com.ibm.icu.text.LocaleDisplayNames;
import java.lang.ref.WeakReference;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.Freezable;
import java.io.Serializable;

public class TimeZoneGenericNames implements Serializable, Freezable<TimeZoneGenericNames>
{
    private static final long serialVersionUID = 2729910342063468417L;
    private ULocale _locale;
    private TimeZoneNames _tznames;
    private transient boolean _frozen;
    private transient String _region;
    private transient WeakReference<LocaleDisplayNames> _localeDisplayNamesRef;
    private transient MessageFormat[] _patternFormatters;
    private transient ConcurrentHashMap<String, String> _genericLocationNamesMap;
    private transient ConcurrentHashMap<String, String> _genericPartialLocationNamesMap;
    private transient TextTrieMap<NameInfo> _gnamesTrie;
    private transient boolean _gnamesTrieFullyLoaded;
    private static Cache GENERIC_NAMES_CACHE;
    private static final long DST_CHECK_RANGE = 15897600000L;
    private static final TimeZoneNames.NameType[] GENERIC_NON_LOCATION_TYPES;
    
    public TimeZoneGenericNames(final ULocale locale, final TimeZoneNames tznames) {
        this._locale = locale;
        this._tznames = tznames;
        this.init();
    }
    
    private void init() {
        if (this._tznames == null) {
            this._tznames = TimeZoneNames.getInstance(this._locale);
        }
        this._genericLocationNamesMap = new ConcurrentHashMap<String, String>();
        this._genericPartialLocationNamesMap = new ConcurrentHashMap<String, String>();
        this._gnamesTrie = new TextTrieMap<NameInfo>(true);
        this._gnamesTrieFullyLoaded = false;
        final TimeZone tz = TimeZone.getDefault();
        final String tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
        if (tzCanonicalID != null) {
            this.loadStrings(tzCanonicalID);
        }
    }
    
    private TimeZoneGenericNames(final ULocale locale) {
        this(locale, (TimeZoneNames)null);
    }
    
    public static TimeZoneGenericNames getInstance(final ULocale locale) {
        final String key = locale.getBaseName();
        return TimeZoneGenericNames.GENERIC_NAMES_CACHE.getInstance(key, locale);
    }
    
    public String getDisplayName(final TimeZone tz, final GenericNameType type, final long date) {
        String name = null;
        String tzCanonicalID = null;
        switch (type) {
            case LOCATION: {
                tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
                if (tzCanonicalID != null) {
                    name = this.getGenericLocationName(tzCanonicalID);
                    break;
                }
                break;
            }
            case LONG:
            case SHORT: {
                name = this.formatGenericNonLocationName(tz, type, date);
                if (name != null) {
                    break;
                }
                tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
                if (tzCanonicalID != null) {
                    name = this.getGenericLocationName(tzCanonicalID);
                    break;
                }
                break;
            }
        }
        return name;
    }
    
    public String getGenericLocationName(String canonicalTzID) {
        if (canonicalTzID == null || canonicalTzID.length() == 0) {
            return null;
        }
        String name = this._genericLocationNamesMap.get(canonicalTzID);
        if (name == null) {
            final Output<Boolean> isPrimary = new Output<Boolean>();
            final String countryCode = ZoneMeta.getCanonicalCountry(canonicalTzID, isPrimary);
            if (countryCode != null) {
                if (isPrimary.value) {
                    final String country = this.getLocaleDisplayNames().regionDisplayName(countryCode);
                    name = this.formatPattern(Pattern.REGION_FORMAT, country);
                }
                else {
                    final String city = this._tznames.getExemplarLocationName(canonicalTzID);
                    name = this.formatPattern(Pattern.REGION_FORMAT, city);
                }
            }
            if (name == null) {
                this._genericLocationNamesMap.putIfAbsent(canonicalTzID.intern(), "");
            }
            else {
                synchronized (this) {
                    canonicalTzID = canonicalTzID.intern();
                    final String tmp = this._genericLocationNamesMap.putIfAbsent(canonicalTzID, name.intern());
                    if (tmp == null) {
                        final NameInfo info = new NameInfo();
                        info.tzID = canonicalTzID;
                        info.type = GenericNameType.LOCATION;
                        this._gnamesTrie.put(name, info);
                    }
                    else {
                        name = tmp;
                    }
                }
            }
            return name;
        }
        if (name.length() == 0) {
            return null;
        }
        return name;
    }
    
    public TimeZoneGenericNames setFormatPattern(final Pattern patType, final String patStr) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
        if (!this._genericLocationNamesMap.isEmpty()) {
            this._genericLocationNamesMap = new ConcurrentHashMap<String, String>();
        }
        if (!this._genericPartialLocationNamesMap.isEmpty()) {
            this._genericPartialLocationNamesMap = new ConcurrentHashMap<String, String>();
        }
        this._gnamesTrie = null;
        this._gnamesTrieFullyLoaded = false;
        if (this._patternFormatters == null) {
            this._patternFormatters = new MessageFormat[Pattern.values().length];
        }
        this._patternFormatters[patType.ordinal()] = new MessageFormat(patStr);
        return this;
    }
    
    private String formatGenericNonLocationName(final TimeZone tz, final GenericNameType type, final long date) {
        assert type == GenericNameType.SHORT;
        final String tzID = ZoneMeta.getCanonicalCLDRID(tz);
        if (tzID == null) {
            return null;
        }
        final TimeZoneNames.NameType nameType = (type == GenericNameType.LONG) ? TimeZoneNames.NameType.LONG_GENERIC : TimeZoneNames.NameType.SHORT_GENERIC;
        String name = this._tznames.getTimeZoneDisplayName(tzID, nameType);
        if (name != null) {
            return name;
        }
        final String mzID = this._tznames.getMetaZoneID(tzID, date);
        if (mzID != null) {
            boolean useStandard = false;
            final int[] offsets = { 0, 0 };
            tz.getOffset(date, false, offsets);
            if (offsets[1] == 0) {
                useStandard = true;
                if (tz instanceof BasicTimeZone) {
                    final BasicTimeZone btz = (BasicTimeZone)tz;
                    final TimeZoneTransition before = btz.getPreviousTransition(date, true);
                    if (before != null && date - before.getTime() < 15897600000L && before.getFrom().getDSTSavings() != 0) {
                        useStandard = false;
                    }
                    else {
                        final TimeZoneTransition after = btz.getNextTransition(date, false);
                        if (after != null && after.getTime() - date < 15897600000L && after.getTo().getDSTSavings() != 0) {
                            useStandard = false;
                        }
                    }
                }
                else {
                    final int[] tmpOffsets = new int[2];
                    tz.getOffset(date - 15897600000L, false, tmpOffsets);
                    if (tmpOffsets[1] != 0) {
                        useStandard = false;
                    }
                    else {
                        tz.getOffset(date + 15897600000L, false, tmpOffsets);
                        if (tmpOffsets[1] != 0) {
                            useStandard = false;
                        }
                    }
                }
            }
            if (useStandard) {
                final TimeZoneNames.NameType stdNameType = (nameType == TimeZoneNames.NameType.LONG_GENERIC) ? TimeZoneNames.NameType.LONG_STANDARD : TimeZoneNames.NameType.SHORT_STANDARD;
                final String stdName = this._tznames.getDisplayName(tzID, stdNameType, date);
                if (stdName != null) {
                    name = stdName;
                    final String mzGenericName = this._tznames.getMetaZoneDisplayName(mzID, nameType);
                    if (stdName.equalsIgnoreCase(mzGenericName)) {
                        name = null;
                    }
                }
            }
            if (name == null) {
                final String mzName = this._tznames.getMetaZoneDisplayName(mzID, nameType);
                if (mzName != null) {
                    final String goldenID = this._tznames.getReferenceZoneID(mzID, this.getTargetRegion());
                    if (goldenID != null && !goldenID.equals(tzID)) {
                        final TimeZone goldenZone = TimeZone.getFrozenTimeZone(goldenID);
                        final int[] offsets2 = { 0, 0 };
                        goldenZone.getOffset(date + offsets[0] + offsets[1], true, offsets2);
                        if (offsets[0] != offsets2[0] || offsets[1] != offsets2[1]) {
                            name = this.getPartialLocationName(tzID, mzID, nameType == TimeZoneNames.NameType.LONG_GENERIC, mzName);
                        }
                        else {
                            name = mzName;
                        }
                    }
                    else {
                        name = mzName;
                    }
                }
            }
        }
        return name;
    }
    
    private synchronized String formatPattern(final Pattern pat, final String... args) {
        if (this._patternFormatters == null) {
            this._patternFormatters = new MessageFormat[Pattern.values().length];
        }
        final int idx = pat.ordinal();
        if (this._patternFormatters[idx] == null) {
            String patText;
            try {
                final ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", this._locale);
                patText = bundle.getStringWithFallback("zoneStrings/" + pat.key());
            }
            catch (MissingResourceException e) {
                patText = pat.defaultValue();
            }
            this._patternFormatters[idx] = new MessageFormat(patText);
        }
        return this._patternFormatters[idx].format(args);
    }
    
    private synchronized LocaleDisplayNames getLocaleDisplayNames() {
        LocaleDisplayNames locNames = null;
        if (this._localeDisplayNamesRef != null) {
            locNames = this._localeDisplayNamesRef.get();
        }
        if (locNames == null) {
            locNames = LocaleDisplayNames.getInstance(this._locale);
            this._localeDisplayNamesRef = new WeakReference<LocaleDisplayNames>(locNames);
        }
        return locNames;
    }
    
    private synchronized void loadStrings(final String tzCanonicalID) {
        if (tzCanonicalID == null || tzCanonicalID.length() == 0) {
            return;
        }
        this.getGenericLocationName(tzCanonicalID);
        final Set<String> mzIDs = this._tznames.getAvailableMetaZoneIDs(tzCanonicalID);
        for (final String mzID : mzIDs) {
            final String goldenID = this._tznames.getReferenceZoneID(mzID, this.getTargetRegion());
            if (!tzCanonicalID.equals(goldenID)) {
                for (final TimeZoneNames.NameType genNonLocType : TimeZoneGenericNames.GENERIC_NON_LOCATION_TYPES) {
                    final String mzGenName = this._tznames.getMetaZoneDisplayName(mzID, genNonLocType);
                    if (mzGenName != null) {
                        this.getPartialLocationName(tzCanonicalID, mzID, genNonLocType == TimeZoneNames.NameType.LONG_GENERIC, mzGenName);
                    }
                }
            }
        }
    }
    
    private synchronized String getTargetRegion() {
        if (this._region == null) {
            this._region = this._locale.getCountry();
            if (this._region.length() == 0) {
                final ULocale tmp = ULocale.addLikelySubtags(this._locale);
                this._region = tmp.getCountry();
                if (this._region.length() == 0) {
                    this._region = "001";
                }
            }
        }
        return this._region;
    }
    
    private String getPartialLocationName(final String tzID, final String mzID, final boolean isLong, final String mzDisplayName) {
        final String letter = isLong ? "L" : "S";
        final String key = tzID + "&" + mzID + "#" + letter;
        String name = this._genericPartialLocationNamesMap.get(key);
        if (name != null) {
            return name;
        }
        String location = null;
        final String countryCode = ZoneMeta.getCanonicalCountry(tzID);
        if (countryCode != null) {
            final String regionalGolden = this._tznames.getReferenceZoneID(mzID, countryCode);
            if (tzID.equals(regionalGolden)) {
                location = this.getLocaleDisplayNames().regionDisplayName(countryCode);
            }
            else {
                location = this._tznames.getExemplarLocationName(tzID);
            }
        }
        else {
            location = this._tznames.getExemplarLocationName(tzID);
            if (location == null) {
                location = tzID;
            }
        }
        name = this.formatPattern(Pattern.FALLBACK_FORMAT, location, mzDisplayName);
        synchronized (this) {
            final String tmp = this._genericPartialLocationNamesMap.putIfAbsent(key.intern(), name.intern());
            if (tmp == null) {
                final NameInfo info = new NameInfo();
                info.tzID = tzID.intern();
                info.type = (isLong ? GenericNameType.LONG : GenericNameType.SHORT);
                this._gnamesTrie.put(name, info);
            }
            else {
                name = tmp;
            }
        }
        return name;
    }
    
    public GenericMatchInfo findBestMatch(final String text, final int start, final EnumSet<GenericNameType> genericTypes) {
        if (text == null || text.length() == 0 || start < 0 || start >= text.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        GenericMatchInfo bestMatch = null;
        final Collection<TimeZoneNames.MatchInfo> tznamesMatches = this.findTimeZoneNames(text, start, genericTypes);
        if (tznamesMatches != null) {
            TimeZoneNames.MatchInfo longestMatch = null;
            for (final TimeZoneNames.MatchInfo match : tznamesMatches) {
                if (longestMatch == null || match.matchLength() > longestMatch.matchLength()) {
                    longestMatch = match;
                }
            }
            if (longestMatch != null) {
                bestMatch = this.createGenericMatchInfo(longestMatch);
                if (bestMatch.matchLength() == text.length() - start && bestMatch.timeType != TimeZoneFormat.TimeType.STANDARD) {
                    return bestMatch;
                }
            }
        }
        final Collection<GenericMatchInfo> localMatches = this.findLocal(text, start, genericTypes);
        if (localMatches != null) {
            for (final GenericMatchInfo match2 : localMatches) {
                if (bestMatch == null || match2.matchLength() >= bestMatch.matchLength()) {
                    bestMatch = match2;
                }
            }
        }
        return bestMatch;
    }
    
    public Collection<GenericMatchInfo> find(final String text, final int start, final EnumSet<GenericNameType> genericTypes) {
        if (text == null || text.length() == 0 || start < 0 || start >= text.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        Collection<GenericMatchInfo> results = this.findLocal(text, start, genericTypes);
        final Collection<TimeZoneNames.MatchInfo> tznamesMatches = this.findTimeZoneNames(text, start, genericTypes);
        if (tznamesMatches != null) {
            for (final TimeZoneNames.MatchInfo match : tznamesMatches) {
                if (results == null) {
                    results = new LinkedList<GenericMatchInfo>();
                }
                results.add(this.createGenericMatchInfo(match));
            }
        }
        return results;
    }
    
    private GenericMatchInfo createGenericMatchInfo(final TimeZoneNames.MatchInfo matchInfo) {
        GenericNameType nameType = null;
        TimeZoneFormat.TimeType timeType = TimeZoneFormat.TimeType.UNKNOWN;
        switch (matchInfo.nameType()) {
            case LONG_STANDARD: {
                nameType = GenericNameType.LONG;
                timeType = TimeZoneFormat.TimeType.STANDARD;
                break;
            }
            case LONG_GENERIC: {
                nameType = GenericNameType.LONG;
                break;
            }
            case SHORT_STANDARD: {
                nameType = GenericNameType.SHORT;
                timeType = TimeZoneFormat.TimeType.STANDARD;
                break;
            }
            case SHORT_GENERIC: {
                nameType = GenericNameType.SHORT;
                break;
            }
        }
        assert nameType != null;
        String tzID = matchInfo.tzID();
        if (tzID == null) {
            final String mzID = matchInfo.mzID();
            assert mzID != null;
            tzID = this._tznames.getReferenceZoneID(mzID, this.getTargetRegion());
        }
        assert tzID != null;
        final GenericMatchInfo gmatch = new GenericMatchInfo();
        gmatch.nameType = nameType;
        gmatch.tzID = tzID;
        gmatch.matchLength = matchInfo.matchLength();
        gmatch.timeType = timeType;
        return gmatch;
    }
    
    private Collection<TimeZoneNames.MatchInfo> findTimeZoneNames(final String text, final int start, final EnumSet<GenericNameType> types) {
        Collection<TimeZoneNames.MatchInfo> tznamesMatches = null;
        final EnumSet<TimeZoneNames.NameType> nameTypes = EnumSet.noneOf(TimeZoneNames.NameType.class);
        if (types.contains(GenericNameType.LONG)) {
            nameTypes.add(TimeZoneNames.NameType.LONG_GENERIC);
            nameTypes.add(TimeZoneNames.NameType.LONG_STANDARD);
        }
        if (types.contains(GenericNameType.SHORT)) {
            nameTypes.add(TimeZoneNames.NameType.SHORT_GENERIC);
            nameTypes.add(TimeZoneNames.NameType.SHORT_STANDARD);
        }
        if (!nameTypes.isEmpty()) {
            tznamesMatches = this._tznames.find(text, start, nameTypes);
        }
        return tznamesMatches;
    }
    
    private synchronized Collection<GenericMatchInfo> findLocal(final String text, final int start, final EnumSet<GenericNameType> types) {
        final GenericNameSearchHandler handler = new GenericNameSearchHandler(types);
        this._gnamesTrie.find(text, start, handler);
        if (handler.getMaxMatchLen() == text.length() - start || this._gnamesTrieFullyLoaded) {
            return handler.getMatches();
        }
        final Set<String> tzIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
        for (final String tzID : tzIDs) {
            this.loadStrings(tzID);
        }
        this._gnamesTrieFullyLoaded = true;
        handler.resetResults();
        this._gnamesTrie.find(text, start, handler);
        return handler.getMatches();
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.init();
    }
    
    public boolean isFrozen() {
        return this._frozen;
    }
    
    public TimeZoneGenericNames freeze() {
        this._frozen = true;
        return this;
    }
    
    public TimeZoneGenericNames cloneAsThawed() {
        TimeZoneGenericNames copy = null;
        try {
            copy = (TimeZoneGenericNames)super.clone();
            copy._frozen = false;
        }
        catch (Throwable t) {}
        return copy;
    }
    
    static {
        TimeZoneGenericNames.GENERIC_NAMES_CACHE = new Cache();
        GENERIC_NON_LOCATION_TYPES = new TimeZoneNames.NameType[] { TimeZoneNames.NameType.LONG_GENERIC, TimeZoneNames.NameType.SHORT_GENERIC };
    }
    
    public enum GenericNameType
    {
        LOCATION(new String[] { "LONG", "SHORT" }), 
        LONG(new String[0]), 
        SHORT(new String[0]);
        
        String[] _fallbackTypeOf;
        
        private GenericNameType(final String[] fallbackTypeOf) {
            this._fallbackTypeOf = fallbackTypeOf;
        }
        
        public boolean isFallbackTypeOf(final GenericNameType type) {
            final String typeStr = type.toString();
            for (final String t : this._fallbackTypeOf) {
                if (t.equals(typeStr)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public enum Pattern
    {
        REGION_FORMAT("regionFormat", "({0})"), 
        FALLBACK_FORMAT("fallbackFormat", "{1} ({0})");
        
        String _key;
        String _defaultVal;
        
        private Pattern(final String key, final String defaultVal) {
            this._key = key;
            this._defaultVal = defaultVal;
        }
        
        String key() {
            return this._key;
        }
        
        String defaultValue() {
            return this._defaultVal;
        }
    }
    
    private static class NameInfo
    {
        String tzID;
        GenericNameType type;
    }
    
    public static class GenericMatchInfo
    {
        GenericNameType nameType;
        String tzID;
        int matchLength;
        TimeZoneFormat.TimeType timeType;
        
        public GenericMatchInfo() {
            this.timeType = TimeZoneFormat.TimeType.UNKNOWN;
        }
        
        public GenericNameType nameType() {
            return this.nameType;
        }
        
        public String tzID() {
            return this.tzID;
        }
        
        public TimeZoneFormat.TimeType timeType() {
            return this.timeType;
        }
        
        public int matchLength() {
            return this.matchLength;
        }
    }
    
    private static class GenericNameSearchHandler implements TextTrieMap.ResultHandler<NameInfo>
    {
        private EnumSet<GenericNameType> _types;
        private Collection<GenericMatchInfo> _matches;
        private int _maxMatchLen;
        
        GenericNameSearchHandler(final EnumSet<GenericNameType> types) {
            this._types = types;
        }
        
        public boolean handlePrefixMatch(final int matchLength, final Iterator<NameInfo> values) {
            while (values.hasNext()) {
                final NameInfo info = values.next();
                if (this._types != null && !this._types.contains(info.type)) {
                    continue;
                }
                final GenericMatchInfo matchInfo = new GenericMatchInfo();
                matchInfo.tzID = info.tzID;
                matchInfo.nameType = info.type;
                matchInfo.matchLength = matchLength;
                if (this._matches == null) {
                    this._matches = new LinkedList<GenericMatchInfo>();
                }
                this._matches.add(matchInfo);
                if (matchLength <= this._maxMatchLen) {
                    continue;
                }
                this._maxMatchLen = matchLength;
            }
            return true;
        }
        
        public Collection<GenericMatchInfo> getMatches() {
            return this._matches;
        }
        
        public int getMaxMatchLen() {
            return this._maxMatchLen;
        }
        
        public void resetResults() {
            this._matches = null;
            this._maxMatchLen = 0;
        }
    }
    
    private static class Cache extends SoftCache<String, TimeZoneGenericNames, ULocale>
    {
        @Override
        protected TimeZoneGenericNames createInstance(final String key, final ULocale data) {
            return new TimeZoneGenericNames(data, (TimeZoneGenericNames$1)null).freeze();
        }
    }
}
