// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.LinkedList;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.ibm.icu.util.TimeZone;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Iterator;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.ULocale;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import com.ibm.icu.text.TimeZoneNames;

public class TimeZoneNamesImpl extends TimeZoneNames
{
    private static final long serialVersionUID = -2179814848495897472L;
    private static final String ZONE_STRINGS_BUNDLE = "zoneStrings";
    private static final String MZ_PREFIX = "meta:";
    private static Set<String> METAZONE_IDS;
    private static final TZ2MZsCache TZ_TO_MZS_CACHE;
    private static final MZ2TZsCache MZ_TO_TZS_CACHE;
    private transient ICUResourceBundle _zoneStrings;
    private transient ConcurrentHashMap<String, ZNames> _mzNamesMap;
    private transient ConcurrentHashMap<String, TZNames> _tzNamesMap;
    private transient TextTrieMap<NameInfo> _namesTrie;
    private transient boolean _namesTrieFullyLoaded;
    private static final Pattern LOC_EXCLUSION_PATTERN;
    
    public TimeZoneNamesImpl(final ULocale locale) {
        this.initialize(locale);
    }
    
    @Override
    public synchronized Set<String> getAvailableMetaZoneIDs() {
        if (TimeZoneNamesImpl.METAZONE_IDS == null) {
            final UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
            final UResourceBundle mapTimezones = bundle.get("mapTimezones");
            final Set<String> keys = mapTimezones.keySet();
            TimeZoneNamesImpl.METAZONE_IDS = Collections.unmodifiableSet((Set<? extends String>)keys);
        }
        return TimeZoneNamesImpl.METAZONE_IDS;
    }
    
    @Override
    public Set<String> getAvailableMetaZoneIDs(final String tzID) {
        if (tzID == null || tzID.length() == 0) {
            return Collections.emptySet();
        }
        final List<MZMapEntry> maps = TimeZoneNamesImpl.TZ_TO_MZS_CACHE.getInstance(tzID, tzID);
        if (maps.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<String> mzIDs = new HashSet<String>(maps.size());
        for (final MZMapEntry map : maps) {
            mzIDs.add(map.mzID());
        }
        return Collections.unmodifiableSet((Set<? extends String>)mzIDs);
    }
    
    @Override
    public String getMetaZoneID(final String tzID, final long date) {
        if (tzID == null || tzID.length() == 0) {
            return null;
        }
        String mzID = null;
        final List<MZMapEntry> maps = TimeZoneNamesImpl.TZ_TO_MZS_CACHE.getInstance(tzID, tzID);
        for (final MZMapEntry map : maps) {
            if (date >= map.from() && date < map.to()) {
                mzID = map.mzID();
                break;
            }
        }
        return mzID;
    }
    
    @Override
    public String getReferenceZoneID(final String mzID, final String region) {
        if (mzID == null || mzID.length() == 0) {
            return null;
        }
        String refID = null;
        final Map<String, String> regionTzMap = TimeZoneNamesImpl.MZ_TO_TZS_CACHE.getInstance(mzID, mzID);
        if (!regionTzMap.isEmpty()) {
            refID = regionTzMap.get(region);
            if (refID == null) {
                refID = regionTzMap.get("001");
            }
        }
        return refID;
    }
    
    @Override
    public String getMetaZoneDisplayName(final String mzID, final NameType type) {
        if (mzID == null || mzID.length() == 0) {
            return null;
        }
        return this.loadMetaZoneNames(mzID).getName(type);
    }
    
    @Override
    public String getTimeZoneDisplayName(final String tzID, final NameType type) {
        if (tzID == null || tzID.length() == 0) {
            return null;
        }
        return this.loadTimeZoneNames(tzID).getName(type);
    }
    
    @Override
    public String getExemplarLocationName(final String tzID) {
        if (tzID == null || tzID.length() == 0) {
            return null;
        }
        final String locName = this.loadTimeZoneNames(tzID).getName(NameType.EXEMPLAR_LOCATION);
        return locName;
    }
    
    @Override
    public synchronized Collection<MatchInfo> find(final CharSequence text, final int start, final EnumSet<NameType> nameTypes) {
        if (text == null || text.length() == 0 || start < 0 || start >= text.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        final NameSearchHandler handler = new NameSearchHandler(nameTypes);
        this._namesTrie.find(text, start, handler);
        if (handler.getMaxMatchLen() == text.length() - start || this._namesTrieFullyLoaded) {
            return handler.getMatches();
        }
        final Set<String> tzIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
        for (final String tzID : tzIDs) {
            this.loadTimeZoneNames(tzID);
        }
        final Set<String> mzIDs = this.getAvailableMetaZoneIDs();
        for (final String mzID : mzIDs) {
            this.loadMetaZoneNames(mzID);
        }
        this._namesTrieFullyLoaded = true;
        handler.resetResults();
        this._namesTrie.find(text, start, handler);
        return handler.getMatches();
    }
    
    private void initialize(final ULocale locale) {
        final ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", locale);
        this._zoneStrings = (ICUResourceBundle)bundle.get("zoneStrings");
        this._tzNamesMap = new ConcurrentHashMap<String, TZNames>();
        this._mzNamesMap = new ConcurrentHashMap<String, ZNames>();
        this._namesTrie = new TextTrieMap<NameInfo>(true);
        this._namesTrieFullyLoaded = false;
        final TimeZone tz = TimeZone.getDefault();
        final String tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
        if (tzCanonicalID != null) {
            this.loadStrings(tzCanonicalID);
        }
    }
    
    private synchronized void loadStrings(final String tzCanonicalID) {
        if (tzCanonicalID == null || tzCanonicalID.length() == 0) {
            return;
        }
        this.loadTimeZoneNames(tzCanonicalID);
        final Set<String> mzIDs = this.getAvailableMetaZoneIDs(tzCanonicalID);
        for (final String mzID : mzIDs) {
            this.loadMetaZoneNames(mzID);
        }
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        final ULocale locale = this._zoneStrings.getULocale();
        out.writeObject(locale);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        final ULocale locale = (ULocale)in.readObject();
        this.initialize(locale);
    }
    
    private synchronized ZNames loadMetaZoneNames(String mzID) {
        ZNames znames = this._mzNamesMap.get(mzID);
        if (znames == null) {
            znames = ZNames.getInstance(this._zoneStrings, "meta:" + mzID);
            mzID = mzID.intern();
            for (final NameType t : NameType.values()) {
                final String name = znames.getName(t);
                if (name != null) {
                    final NameInfo info = new NameInfo();
                    info.mzID = mzID;
                    info.type = t;
                    this._namesTrie.put(name, info);
                }
            }
            final ZNames tmpZnames = this._mzNamesMap.putIfAbsent(mzID, znames);
            znames = ((tmpZnames == null) ? znames : tmpZnames);
        }
        return znames;
    }
    
    private synchronized TZNames loadTimeZoneNames(String tzID) {
        TZNames tznames = this._tzNamesMap.get(tzID);
        if (tznames == null) {
            tznames = TZNames.getInstance(this._zoneStrings, tzID.replace('/', ':'), tzID);
            tzID = tzID.intern();
            for (final NameType t : NameType.values()) {
                final String name = tznames.getName(t);
                if (name != null) {
                    final NameInfo info = new NameInfo();
                    info.tzID = tzID;
                    info.type = t;
                    this._namesTrie.put(name, info);
                }
            }
            final TZNames tmpTznames = this._tzNamesMap.putIfAbsent(tzID, tznames);
            tznames = ((tmpTznames == null) ? tznames : tmpTznames);
        }
        return tznames;
    }
    
    public static String getDefaultExemplarLocationName(final String tzID) {
        if (tzID == null || tzID.length() == 0 || TimeZoneNamesImpl.LOC_EXCLUSION_PATTERN.matcher(tzID).matches()) {
            return null;
        }
        String location = null;
        final int sep = tzID.lastIndexOf(47);
        if (sep > 0 && sep + 1 < tzID.length()) {
            location = tzID.substring(sep + 1).replace('_', ' ');
        }
        return location;
    }
    
    static {
        TZ_TO_MZS_CACHE = new TZ2MZsCache();
        MZ_TO_TZS_CACHE = new MZ2TZsCache();
        LOC_EXCLUSION_PATTERN = Pattern.compile("Etc/.*|SystemV/.*|.*/Riyadh8[7-9]");
    }
    
    private static class NameInfo
    {
        String tzID;
        String mzID;
        NameType type;
    }
    
    private static class NameSearchHandler implements TextTrieMap.ResultHandler<NameInfo>
    {
        private EnumSet<NameType> _nameTypes;
        private Collection<MatchInfo> _matches;
        private int _maxMatchLen;
        
        NameSearchHandler(final EnumSet<NameType> nameTypes) {
            this._nameTypes = nameTypes;
        }
        
        public boolean handlePrefixMatch(final int matchLength, final Iterator<NameInfo> values) {
            while (values.hasNext()) {
                final NameInfo ninfo = values.next();
                if (this._nameTypes != null && !this._nameTypes.contains(ninfo.type)) {
                    continue;
                }
                MatchInfo minfo;
                if (ninfo.tzID != null) {
                    minfo = new MatchInfo(ninfo.type, ninfo.tzID, null, matchLength);
                }
                else {
                    assert ninfo.mzID != null;
                    minfo = new MatchInfo(ninfo.type, null, ninfo.mzID, matchLength);
                }
                if (this._matches == null) {
                    this._matches = new LinkedList<MatchInfo>();
                }
                this._matches.add(minfo);
                if (matchLength <= this._maxMatchLen) {
                    continue;
                }
                this._maxMatchLen = matchLength;
            }
            return true;
        }
        
        public Collection<MatchInfo> getMatches() {
            if (this._matches == null) {
                return (Collection<MatchInfo>)Collections.emptyList();
            }
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
    
    private static class ZNames
    {
        private static final ZNames EMPTY_ZNAMES;
        private String[] _names;
        private static final String[] KEYS;
        
        protected ZNames(final String[] names) {
            this._names = names;
        }
        
        public static ZNames getInstance(final ICUResourceBundle zoneStrings, final String key) {
            final String[] names = loadData(zoneStrings, key);
            if (names == null) {
                return ZNames.EMPTY_ZNAMES;
            }
            return new ZNames(names);
        }
        
        public String getName(final NameType type) {
            if (this._names == null) {
                return null;
            }
            String name = null;
            switch (type) {
                case LONG_GENERIC: {
                    name = this._names[0];
                    break;
                }
                case LONG_STANDARD: {
                    name = this._names[1];
                    break;
                }
                case LONG_DAYLIGHT: {
                    name = this._names[2];
                    break;
                }
                case SHORT_GENERIC: {
                    name = this._names[3];
                    break;
                }
                case SHORT_STANDARD: {
                    name = this._names[4];
                    break;
                }
                case SHORT_DAYLIGHT: {
                    name = this._names[5];
                    break;
                }
                case EXEMPLAR_LOCATION: {
                    name = null;
                    break;
                }
            }
            return name;
        }
        
        protected static String[] loadData(final ICUResourceBundle zoneStrings, final String key) {
            if (zoneStrings == null || key == null || key.length() == 0) {
                return null;
            }
            ICUResourceBundle table = null;
            try {
                table = zoneStrings.getWithFallback(key);
            }
            catch (MissingResourceException e) {
                return null;
            }
            boolean isEmpty = true;
            final String[] names = new String[ZNames.KEYS.length];
            for (int i = 0; i < names.length; ++i) {
                try {
                    names[i] = table.getStringWithFallback(ZNames.KEYS[i]);
                    isEmpty = false;
                }
                catch (MissingResourceException e2) {
                    names[i] = null;
                }
            }
            if (isEmpty) {
                return null;
            }
            return names;
        }
        
        static {
            EMPTY_ZNAMES = new ZNames(null);
            KEYS = new String[] { "lg", "ls", "ld", "sg", "ss", "sd" };
        }
    }
    
    private static class TZNames extends ZNames
    {
        private String _locationName;
        private static final TZNames EMPTY_TZNAMES;
        
        public static TZNames getInstance(final ICUResourceBundle zoneStrings, final String key, final String tzID) {
            if (zoneStrings == null || key == null || key.length() == 0) {
                return TZNames.EMPTY_TZNAMES;
            }
            final String[] names = ZNames.loadData(zoneStrings, key);
            String locationName = null;
            ICUResourceBundle table = null;
            try {
                table = zoneStrings.getWithFallback(key);
                locationName = table.getStringWithFallback("ec");
            }
            catch (MissingResourceException ex) {}
            if (locationName == null) {
                locationName = TimeZoneNamesImpl.getDefaultExemplarLocationName(tzID);
            }
            if (locationName == null && names == null) {
                return TZNames.EMPTY_TZNAMES;
            }
            return new TZNames(names, locationName);
        }
        
        @Override
        public String getName(final NameType type) {
            if (type == NameType.EXEMPLAR_LOCATION) {
                return this._locationName;
            }
            return super.getName(type);
        }
        
        private TZNames(final String[] names, final String locationName) {
            super(names);
            this._locationName = locationName;
        }
        
        static {
            EMPTY_TZNAMES = new TZNames(null, null);
        }
    }
    
    private static class MZMapEntry
    {
        private String _mzID;
        private long _from;
        private long _to;
        
        MZMapEntry(final String mzID, final long from, final long to) {
            this._mzID = mzID;
            this._from = from;
            this._to = to;
        }
        
        String mzID() {
            return this._mzID;
        }
        
        long from() {
            return this._from;
        }
        
        long to() {
            return this._to;
        }
    }
    
    private static class TZ2MZsCache extends SoftCache<String, List<MZMapEntry>, String>
    {
        @Override
        protected List<MZMapEntry> createInstance(final String key, final String data) {
            List<MZMapEntry> mzMaps = null;
            final UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
            final UResourceBundle metazoneInfoBundle = bundle.get("metazoneInfo");
            final String tzkey = data.replace('/', ':');
            try {
                final UResourceBundle zoneBundle = metazoneInfoBundle.get(tzkey);
                mzMaps = new ArrayList<MZMapEntry>(zoneBundle.getSize());
                for (int idx = 0; idx < zoneBundle.getSize(); ++idx) {
                    final UResourceBundle mz = zoneBundle.get(idx);
                    final String mzid = mz.getString(0);
                    String fromStr = "1970-01-01 00:00";
                    String toStr = "9999-12-31 23:59";
                    if (mz.getSize() == 3) {
                        fromStr = mz.getString(1);
                        toStr = mz.getString(2);
                    }
                    final long from = parseDate(fromStr);
                    final long to = parseDate(toStr);
                    mzMaps.add(new MZMapEntry(mzid, from, to));
                }
            }
            catch (MissingResourceException mre) {
                mzMaps = Collections.emptyList();
            }
            return mzMaps;
        }
        
        private static long parseDate(final String text) {
            int year = 0;
            int month = 0;
            int day = 0;
            int hour = 0;
            int min = 0;
            for (int idx = 0; idx <= 3; ++idx) {
                final int n = text.charAt(idx) - '0';
                if (n < 0 || n >= 10) {
                    throw new IllegalArgumentException("Bad year");
                }
                year = 10 * year + n;
            }
            for (int idx = 5; idx <= 6; ++idx) {
                final int n = text.charAt(idx) - '0';
                if (n < 0 || n >= 10) {
                    throw new IllegalArgumentException("Bad month");
                }
                month = 10 * month + n;
            }
            for (int idx = 8; idx <= 9; ++idx) {
                final int n = text.charAt(idx) - '0';
                if (n < 0 || n >= 10) {
                    throw new IllegalArgumentException("Bad day");
                }
                day = 10 * day + n;
            }
            for (int idx = 11; idx <= 12; ++idx) {
                final int n = text.charAt(idx) - '0';
                if (n < 0 || n >= 10) {
                    throw new IllegalArgumentException("Bad hour");
                }
                hour = 10 * hour + n;
            }
            for (int idx = 14; idx <= 15; ++idx) {
                final int n = text.charAt(idx) - '0';
                if (n < 0 || n >= 10) {
                    throw new IllegalArgumentException("Bad minute");
                }
                min = 10 * min + n;
            }
            final long date = Grego.fieldsToDay(year, month - 1, day) * 86400000L + hour * 3600000L + min * 60000L;
            return date;
        }
    }
    
    private static class MZ2TZsCache extends SoftCache<String, Map<String, String>, String>
    {
        @Override
        protected Map<String, String> createInstance(final String key, final String data) {
            Map<String, String> map = null;
            final UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
            final UResourceBundle mapTimezones = bundle.get("mapTimezones");
            try {
                final UResourceBundle regionMap = mapTimezones.get(key);
                final Set<String> regions = regionMap.keySet();
                map = new HashMap<String, String>(regions.size());
                for (final String region : regions) {
                    final String tzID = regionMap.getString(region).intern();
                    map.put(region.intern(), tzID);
                }
            }
            catch (MissingResourceException e) {
                map = Collections.emptyMap();
            }
            return map;
        }
    }
}
