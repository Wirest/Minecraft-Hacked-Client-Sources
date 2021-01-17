// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.util.SimpleTimeZone;
import com.ibm.icu.text.NumberFormat;
import java.text.ParsePosition;
import com.ibm.icu.util.Output;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import java.util.Iterator;
import java.util.Locale;
import com.ibm.icu.util.TimeZone;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Set;
import java.lang.ref.SoftReference;

public final class ZoneMeta
{
    private static final boolean ASSERT = false;
    private static final String ZONEINFORESNAME = "zoneinfo64";
    private static final String kREGIONS = "Regions";
    private static final String kZONES = "Zones";
    private static final String kNAMES = "Names";
    private static final String kGMT_ID = "GMT";
    private static final String kCUSTOM_TZ_PREFIX = "GMT";
    private static final String kWorld = "001";
    private static SoftReference<Set<String>> REF_SYSTEM_ZONES;
    private static SoftReference<Set<String>> REF_CANONICAL_SYSTEM_ZONES;
    private static SoftReference<Set<String>> REF_CANONICAL_SYSTEM_LOCATION_ZONES;
    private static String[] ZONEIDS;
    private static ICUCache<String, String> CANONICAL_ID_CACHE;
    private static ICUCache<String, String> REGION_CACHE;
    private static ICUCache<String, Boolean> SINGLE_COUNTRY_CACHE;
    private static final SystemTimeZoneCache SYSTEM_ZONE_CACHE;
    private static final int kMAX_CUSTOM_HOUR = 23;
    private static final int kMAX_CUSTOM_MIN = 59;
    private static final int kMAX_CUSTOM_SEC = 59;
    private static final CustomTimeZoneCache CUSTOM_ZONE_CACHE;
    
    private static synchronized Set<String> getSystemZIDs() {
        Set<String> systemZones = null;
        if (ZoneMeta.REF_SYSTEM_ZONES != null) {
            systemZones = ZoneMeta.REF_SYSTEM_ZONES.get();
        }
        if (systemZones == null) {
            final Set<String> systemIDs = new TreeSet<String>();
            final String[] arr$;
            final String[] allIDs = arr$ = getZoneIDs();
            for (final String id : arr$) {
                if (!id.equals("Etc/Unknown")) {
                    systemIDs.add(id);
                }
            }
            systemZones = Collections.unmodifiableSet((Set<? extends String>)systemIDs);
            ZoneMeta.REF_SYSTEM_ZONES = new SoftReference<Set<String>>(systemZones);
        }
        return systemZones;
    }
    
    private static synchronized Set<String> getCanonicalSystemZIDs() {
        Set<String> canonicalSystemZones = null;
        if (ZoneMeta.REF_CANONICAL_SYSTEM_ZONES != null) {
            canonicalSystemZones = ZoneMeta.REF_CANONICAL_SYSTEM_ZONES.get();
        }
        if (canonicalSystemZones == null) {
            final Set<String> canonicalSystemIDs = new TreeSet<String>();
            final String[] arr$;
            final String[] allIDs = arr$ = getZoneIDs();
            for (final String id : arr$) {
                if (!id.equals("Etc/Unknown")) {
                    final String canonicalID = getCanonicalCLDRID(id);
                    if (id.equals(canonicalID)) {
                        canonicalSystemIDs.add(id);
                    }
                }
            }
            canonicalSystemZones = Collections.unmodifiableSet((Set<? extends String>)canonicalSystemIDs);
            ZoneMeta.REF_CANONICAL_SYSTEM_ZONES = new SoftReference<Set<String>>(canonicalSystemZones);
        }
        return canonicalSystemZones;
    }
    
    private static synchronized Set<String> getCanonicalSystemLocationZIDs() {
        Set<String> canonicalSystemLocationZones = null;
        if (ZoneMeta.REF_CANONICAL_SYSTEM_LOCATION_ZONES != null) {
            canonicalSystemLocationZones = ZoneMeta.REF_CANONICAL_SYSTEM_LOCATION_ZONES.get();
        }
        if (canonicalSystemLocationZones == null) {
            final Set<String> canonicalSystemLocationIDs = new TreeSet<String>();
            final String[] arr$;
            final String[] allIDs = arr$ = getZoneIDs();
            for (final String id : arr$) {
                if (!id.equals("Etc/Unknown")) {
                    final String canonicalID = getCanonicalCLDRID(id);
                    if (id.equals(canonicalID)) {
                        final String region = getRegion(id);
                        if (region != null && !region.equals("001")) {
                            canonicalSystemLocationIDs.add(id);
                        }
                    }
                }
            }
            canonicalSystemLocationZones = Collections.unmodifiableSet((Set<? extends String>)canonicalSystemLocationIDs);
            ZoneMeta.REF_CANONICAL_SYSTEM_LOCATION_ZONES = new SoftReference<Set<String>>(canonicalSystemLocationZones);
        }
        return canonicalSystemLocationZones;
    }
    
    public static Set<String> getAvailableIDs(final TimeZone.SystemTimeZoneType type, String region, final Integer rawOffset) {
        Set<String> baseSet = null;
        switch (type) {
            case ANY: {
                baseSet = getSystemZIDs();
                break;
            }
            case CANONICAL: {
                baseSet = getCanonicalSystemZIDs();
                break;
            }
            case CANONICAL_LOCATION: {
                baseSet = getCanonicalSystemLocationZIDs();
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown SystemTimeZoneType");
            }
        }
        if (region == null && rawOffset == null) {
            return baseSet;
        }
        if (region != null) {
            region = region.toUpperCase(Locale.ENGLISH);
        }
        final Set<String> result = new TreeSet<String>();
        for (final String id : baseSet) {
            if (region != null) {
                final String r = getRegion(id);
                if (!region.equals(r)) {
                    continue;
                }
            }
            if (rawOffset != null) {
                final TimeZone z = getSystemTimeZone(id);
                if (z == null) {
                    continue;
                }
                if (!rawOffset.equals(z.getRawOffset())) {
                    continue;
                }
            }
            result.add(id);
        }
        if (result.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet((Set<? extends String>)result);
    }
    
    public static synchronized int countEquivalentIDs(final String id) {
        int count = 0;
        final UResourceBundle res = openOlsonResource(null, id);
        if (res != null) {
            try {
                final UResourceBundle links = res.get("links");
                final int[] v = links.getIntVector();
                count = v.length;
            }
            catch (MissingResourceException ex) {}
        }
        return count;
    }
    
    public static synchronized String getEquivalentID(final String id, final int index) {
        String result = "";
        if (index >= 0) {
            final UResourceBundle res = openOlsonResource(null, id);
            if (res != null) {
                int zoneIdx = -1;
                try {
                    final UResourceBundle links = res.get("links");
                    final int[] zones = links.getIntVector();
                    if (index < zones.length) {
                        zoneIdx = zones[index];
                    }
                }
                catch (MissingResourceException ex) {}
                if (zoneIdx >= 0) {
                    final String tmp = getZoneID(zoneIdx);
                    if (tmp != null) {
                        result = tmp;
                    }
                }
            }
        }
        return result;
    }
    
    private static synchronized String[] getZoneIDs() {
        if (ZoneMeta.ZONEIDS == null) {
            try {
                final UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                final UResourceBundle names = top.get("Names");
                ZoneMeta.ZONEIDS = names.getStringArray();
            }
            catch (MissingResourceException ex) {}
        }
        if (ZoneMeta.ZONEIDS == null) {
            ZoneMeta.ZONEIDS = new String[0];
        }
        return ZoneMeta.ZONEIDS;
    }
    
    private static String getZoneID(final int idx) {
        if (idx >= 0) {
            final String[] ids = getZoneIDs();
            if (idx < ids.length) {
                return ids[idx];
            }
        }
        return null;
    }
    
    private static int getZoneIndex(final String zid) {
        int zoneIdx = -1;
        final String[] all = getZoneIDs();
        if (all.length > 0) {
            int start = 0;
            int limit = all.length;
            int lastMid = Integer.MAX_VALUE;
            while (true) {
                final int mid = (start + limit) / 2;
                if (lastMid == mid) {
                    break;
                }
                lastMid = mid;
                final int r = zid.compareTo(all[mid]);
                if (r == 0) {
                    zoneIdx = mid;
                    break;
                }
                if (r < 0) {
                    limit = mid;
                }
                else {
                    start = mid;
                }
            }
        }
        return zoneIdx;
    }
    
    public static String getCanonicalCLDRID(final TimeZone tz) {
        if (tz instanceof OlsonTimeZone) {
            return ((OlsonTimeZone)tz).getCanonicalID();
        }
        return getCanonicalCLDRID(tz.getID());
    }
    
    public static String getCanonicalCLDRID(String tzid) {
        String canonical = ZoneMeta.CANONICAL_ID_CACHE.get(tzid);
        if (canonical == null) {
            canonical = findCLDRCanonicalID(tzid);
            if (canonical == null) {
                try {
                    final int zoneIdx = getZoneIndex(tzid);
                    if (zoneIdx >= 0) {
                        final UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                        final UResourceBundle zones = top.get("Zones");
                        final UResourceBundle zone = zones.get(zoneIdx);
                        if (zone.getType() == 7) {
                            tzid = getZoneID(zone.getInt());
                            canonical = findCLDRCanonicalID(tzid);
                        }
                        if (canonical == null) {
                            canonical = tzid;
                        }
                    }
                }
                catch (MissingResourceException ex) {}
            }
            if (canonical != null) {
                ZoneMeta.CANONICAL_ID_CACHE.put(tzid, canonical);
            }
        }
        return canonical;
    }
    
    private static String findCLDRCanonicalID(final String tzid) {
        String canonical = null;
        final String tzidKey = tzid.replace('/', ':');
        try {
            final UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            final UResourceBundle typeMap = keyTypeData.get("typeMap");
            final UResourceBundle typeKeys = typeMap.get("timezone");
            try {
                typeKeys.get(tzidKey);
                canonical = tzid;
            }
            catch (MissingResourceException ex) {}
            if (canonical == null) {
                final UResourceBundle typeAlias = keyTypeData.get("typeAlias");
                final UResourceBundle aliasesForKey = typeAlias.get("timezone");
                canonical = aliasesForKey.getString(tzidKey);
            }
        }
        catch (MissingResourceException ex2) {}
        return canonical;
    }
    
    public static String getRegion(final String tzid) {
        String region = ZoneMeta.REGION_CACHE.get(tzid);
        if (region == null) {
            final int zoneIdx = getZoneIndex(tzid);
            if (zoneIdx >= 0) {
                try {
                    final UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                    final UResourceBundle regions = top.get("Regions");
                    if (zoneIdx < regions.getSize()) {
                        region = regions.getString(zoneIdx);
                    }
                }
                catch (MissingResourceException ex) {}
                if (region != null) {
                    ZoneMeta.REGION_CACHE.put(tzid, region);
                }
            }
        }
        return region;
    }
    
    public static String getCanonicalCountry(final String tzid) {
        String country = getRegion(tzid);
        if (country != null && country.equals("001")) {
            country = null;
        }
        return country;
    }
    
    public static String getCanonicalCountry(final String tzid, final Output<Boolean> isPrimary) {
        isPrimary.value = Boolean.FALSE;
        final String country = getRegion(tzid);
        if (country != null && country.equals("001")) {
            return null;
        }
        Boolean singleZone = ZoneMeta.SINGLE_COUNTRY_CACHE.get(tzid);
        if (singleZone == null) {
            final Set<String> ids = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL_LOCATION, country, null);
            assert ids.size() >= 1;
            singleZone = (ids.size() <= 1);
            ZoneMeta.SINGLE_COUNTRY_CACHE.put(tzid, singleZone);
        }
        if (singleZone) {
            isPrimary.value = Boolean.TRUE;
        }
        else {
            try {
                final UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
                final UResourceBundle primaryZones = bundle.get("primaryZones");
                final String primaryZone = primaryZones.getString(country);
                if (tzid.equals(primaryZone)) {
                    isPrimary.value = Boolean.TRUE;
                }
                else {
                    final String canonicalID = getCanonicalCLDRID(tzid);
                    if (canonicalID != null && canonicalID.equals(primaryZone)) {
                        isPrimary.value = Boolean.TRUE;
                    }
                }
            }
            catch (MissingResourceException ex) {}
        }
        return country;
    }
    
    public static UResourceBundle openOlsonResource(UResourceBundle top, final String id) {
        UResourceBundle res = null;
        final int zoneIdx = getZoneIndex(id);
        if (zoneIdx >= 0) {
            try {
                if (top == null) {
                    top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                }
                final UResourceBundle zones = top.get("Zones");
                UResourceBundle zone = zones.get(zoneIdx);
                if (zone.getType() == 7) {
                    zone = zones.get(zone.getInt());
                }
                res = zone;
            }
            catch (MissingResourceException e) {
                res = null;
            }
        }
        return res;
    }
    
    public static TimeZone getSystemTimeZone(final String id) {
        return ((SoftCache<String, TimeZone, String>)ZoneMeta.SYSTEM_ZONE_CACHE).getInstance(id, id);
    }
    
    public static TimeZone getCustomTimeZone(final String id) {
        final int[] fields = new int[4];
        if (parseCustomID(id, fields)) {
            final Integer key = fields[0] * (fields[1] | fields[2] << 5 | fields[3] << 11);
            return ((SoftCache<Integer, TimeZone, int[]>)ZoneMeta.CUSTOM_ZONE_CACHE).getInstance(key, fields);
        }
        return null;
    }
    
    public static String getCustomID(final String id) {
        final int[] fields = new int[4];
        if (parseCustomID(id, fields)) {
            return formatCustomID(fields[1], fields[2], fields[3], fields[0] < 0);
        }
        return null;
    }
    
    static boolean parseCustomID(final String id, final int[] fields) {
        NumberFormat numberFormat = null;
        if (id != null && id.length() > "GMT".length() && id.toUpperCase(Locale.ENGLISH).startsWith("GMT")) {
            final ParsePosition pos = new ParsePosition("GMT".length());
            int sign = 1;
            int hour = 0;
            int min = 0;
            int sec = 0;
            if (id.charAt(pos.getIndex()) == '-') {
                sign = -1;
            }
            else if (id.charAt(pos.getIndex()) != '+') {
                return false;
            }
            pos.setIndex(pos.getIndex() + 1);
            numberFormat = NumberFormat.getInstance();
            numberFormat.setParseIntegerOnly(true);
            final int start = pos.getIndex();
            Number n = numberFormat.parse(id, pos);
            if (pos.getIndex() == start) {
                return false;
            }
            hour = n.intValue();
            if (pos.getIndex() < id.length()) {
                if (pos.getIndex() - start > 2 || id.charAt(pos.getIndex()) != ':') {
                    return false;
                }
                pos.setIndex(pos.getIndex() + 1);
                int oldPos = pos.getIndex();
                n = numberFormat.parse(id, pos);
                if (pos.getIndex() - oldPos != 2) {
                    return false;
                }
                min = n.intValue();
                if (pos.getIndex() < id.length()) {
                    if (id.charAt(pos.getIndex()) != ':') {
                        return false;
                    }
                    pos.setIndex(pos.getIndex() + 1);
                    oldPos = pos.getIndex();
                    n = numberFormat.parse(id, pos);
                    if (pos.getIndex() != id.length() || pos.getIndex() - oldPos != 2) {
                        return false;
                    }
                    sec = n.intValue();
                }
            }
            else {
                final int length = pos.getIndex() - start;
                if (length <= 0 || 6 < length) {
                    return false;
                }
                switch (length) {
                    case 3:
                    case 4: {
                        min = hour % 100;
                        hour /= 100;
                        break;
                    }
                    case 5:
                    case 6: {
                        sec = hour % 100;
                        min = hour / 100 % 100;
                        hour /= 10000;
                        break;
                    }
                }
            }
            if (hour <= 23 && min <= 59 && sec <= 59) {
                if (fields != null) {
                    if (fields.length >= 1) {
                        fields[0] = sign;
                    }
                    if (fields.length >= 2) {
                        fields[1] = hour;
                    }
                    if (fields.length >= 3) {
                        fields[2] = min;
                    }
                    if (fields.length >= 4) {
                        fields[3] = sec;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public static TimeZone getCustomTimeZone(final int offset) {
        boolean negative = false;
        int tmp = offset;
        if (offset < 0) {
            negative = true;
            tmp = -offset;
        }
        tmp /= 1000;
        final int sec = tmp % 60;
        tmp /= 60;
        final int min = tmp % 60;
        final int hour = tmp / 60;
        final String zid = formatCustomID(hour, min, sec, negative);
        return new SimpleTimeZone(offset, zid);
    }
    
    static String formatCustomID(final int hour, final int min, final int sec, final boolean negative) {
        final StringBuilder zid = new StringBuilder("GMT");
        if (hour != 0 || min != 0) {
            if (negative) {
                zid.append('-');
            }
            else {
                zid.append('+');
            }
            if (hour < 10) {
                zid.append('0');
            }
            zid.append(hour);
            zid.append(':');
            if (min < 10) {
                zid.append('0');
            }
            zid.append(min);
            if (sec != 0) {
                zid.append(':');
                if (sec < 10) {
                    zid.append('0');
                }
                zid.append(sec);
            }
        }
        return zid.toString();
    }
    
    public static String getShortID(final TimeZone tz) {
        String canonicalID = null;
        if (tz instanceof OlsonTimeZone) {
            canonicalID = ((OlsonTimeZone)tz).getCanonicalID();
        }
        canonicalID = getCanonicalCLDRID(tz.getID());
        if (canonicalID == null) {
            return null;
        }
        return getShortIDFromCanonical(canonicalID);
    }
    
    public static String getShortID(final String id) {
        final String canonicalID = getCanonicalCLDRID(id);
        if (canonicalID == null) {
            return null;
        }
        return getShortIDFromCanonical(canonicalID);
    }
    
    private static String getShortIDFromCanonical(final String canonicalID) {
        String shortID = null;
        final String tzidKey = canonicalID.replace('/', ':');
        try {
            final UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            final UResourceBundle typeMap = keyTypeData.get("typeMap");
            final UResourceBundle typeKeys = typeMap.get("timezone");
            shortID = typeKeys.getString(tzidKey);
        }
        catch (MissingResourceException ex) {}
        return shortID;
    }
    
    static {
        ZoneMeta.ZONEIDS = null;
        ZoneMeta.CANONICAL_ID_CACHE = new SimpleCache<String, String>();
        ZoneMeta.REGION_CACHE = new SimpleCache<String, String>();
        ZoneMeta.SINGLE_COUNTRY_CACHE = new SimpleCache<String, Boolean>();
        SYSTEM_ZONE_CACHE = new SystemTimeZoneCache();
        CUSTOM_ZONE_CACHE = new CustomTimeZoneCache();
    }
    
    private static class SystemTimeZoneCache extends SoftCache<String, OlsonTimeZone, String>
    {
        @Override
        protected OlsonTimeZone createInstance(final String key, final String data) {
            OlsonTimeZone tz = null;
            try {
                final UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                final UResourceBundle res = ZoneMeta.openOlsonResource(top, data);
                if (res != null) {
                    tz = new OlsonTimeZone(top, res, data);
                    tz.freeze();
                }
            }
            catch (MissingResourceException ex) {}
            return tz;
        }
    }
    
    private static class CustomTimeZoneCache extends SoftCache<Integer, SimpleTimeZone, int[]>
    {
        @Override
        protected SimpleTimeZone createInstance(final Integer key, final int[] data) {
            assert data.length == 4;
            assert data[0] == -1;
            assert data[1] >= 0 && data[1] <= 23;
            assert data[2] >= 0 && data[2] <= 59;
            assert data[3] >= 0 && data[3] <= 59;
            final String id = ZoneMeta.formatCustomID(data[1], data[2], data[3], data[0] < 0);
            final int offset = data[0] * ((data[1] * 60 + data[2]) * 60 + data[3]) * 1000;
            final SimpleTimeZone tz = new SimpleTimeZone(offset, id);
            tz.freeze();
            return tz;
        }
    }
}
