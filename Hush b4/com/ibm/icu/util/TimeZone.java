// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.TimeZoneAdapter;
import com.ibm.icu.impl.OlsonTimeZone;
import java.util.Set;
import com.ibm.icu.impl.JavaTimeZone;
import java.util.Date;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.text.TimeZoneFormat;
import java.util.Locale;
import com.ibm.icu.impl.Grego;
import java.util.logging.Logger;
import java.io.Serializable;

public abstract class TimeZone implements Serializable, Cloneable, Freezable<TimeZone>
{
    private static final Logger LOGGER;
    private static final long serialVersionUID = -744942128318337471L;
    public static final int TIMEZONE_ICU = 0;
    public static final int TIMEZONE_JDK = 1;
    public static final int SHORT = 0;
    public static final int LONG = 1;
    public static final int SHORT_GENERIC = 2;
    public static final int LONG_GENERIC = 3;
    public static final int SHORT_GMT = 4;
    public static final int LONG_GMT = 5;
    public static final int SHORT_COMMONLY_USED = 6;
    public static final int GENERIC_LOCATION = 7;
    public static final String UNKNOWN_ZONE_ID = "Etc/Unknown";
    static final String GMT_ZONE_ID = "Etc/GMT";
    public static final TimeZone UNKNOWN_ZONE;
    public static final TimeZone GMT_ZONE;
    private String ID;
    private static TimeZone defaultZone;
    private static String TZDATA_VERSION;
    private static int TZ_IMPL;
    private static final String TZIMPL_CONFIG_KEY = "com.ibm.icu.util.TimeZone.DefaultTimeZoneType";
    private static final String TZIMPL_CONFIG_ICU = "ICU";
    private static final String TZIMPL_CONFIG_JDK = "JDK";
    
    public TimeZone() {
    }
    
    @Deprecated
    protected TimeZone(final String ID) {
        if (ID == null) {
            throw new NullPointerException();
        }
        this.ID = ID;
    }
    
    public abstract int getOffset(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    public int getOffset(final long date) {
        final int[] result = new int[2];
        this.getOffset(date, false, result);
        return result[0] + result[1];
    }
    
    public void getOffset(long date, final boolean local, final int[] offsets) {
        offsets[0] = this.getRawOffset();
        if (!local) {
            date += offsets[0];
        }
        final int[] fields = new int[6];
        int pass = 0;
        while (true) {
            Grego.timeToFields(date, fields);
            offsets[1] = this.getOffset(1, fields[0], fields[1], fields[2], fields[3], fields[5]) - offsets[0];
            if (pass != 0 || !local || offsets[1] == 0) {
                break;
            }
            date -= offsets[1];
            ++pass;
        }
    }
    
    public abstract void setRawOffset(final int p0);
    
    public abstract int getRawOffset();
    
    public String getID() {
        return this.ID;
    }
    
    public void setID(final String ID) {
        if (ID == null) {
            throw new NullPointerException();
        }
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen TimeZone instance.");
        }
        this.ID = ID;
    }
    
    public final String getDisplayName() {
        return this._getDisplayName(3, false, ULocale.getDefault(ULocale.Category.DISPLAY));
    }
    
    public final String getDisplayName(final Locale locale) {
        return this._getDisplayName(3, false, ULocale.forLocale(locale));
    }
    
    public final String getDisplayName(final ULocale locale) {
        return this._getDisplayName(3, false, locale);
    }
    
    public final String getDisplayName(final boolean daylight, final int style) {
        return this.getDisplayName(daylight, style, ULocale.getDefault(ULocale.Category.DISPLAY));
    }
    
    public String getDisplayName(final boolean daylight, final int style, final Locale locale) {
        return this.getDisplayName(daylight, style, ULocale.forLocale(locale));
    }
    
    public String getDisplayName(final boolean daylight, final int style, final ULocale locale) {
        if (style < 0 || style > 7) {
            throw new IllegalArgumentException("Illegal style: " + style);
        }
        return this._getDisplayName(style, daylight, locale);
    }
    
    private String _getDisplayName(final int style, final boolean daylight, final ULocale locale) {
        if (locale == null) {
            throw new NullPointerException("locale is null");
        }
        String result = null;
        if (style == 7 || style == 3 || style == 2) {
            final TimeZoneFormat tzfmt = TimeZoneFormat.getInstance(locale);
            final long date = System.currentTimeMillis();
            final Output<TimeZoneFormat.TimeType> timeType = new Output<TimeZoneFormat.TimeType>(TimeZoneFormat.TimeType.UNKNOWN);
            switch (style) {
                case 7: {
                    result = tzfmt.format(TimeZoneFormat.Style.GENERIC_LOCATION, this, date, timeType);
                    break;
                }
                case 3: {
                    result = tzfmt.format(TimeZoneFormat.Style.GENERIC_LONG, this, date, timeType);
                    break;
                }
                case 2: {
                    result = tzfmt.format(TimeZoneFormat.Style.GENERIC_SHORT, this, date, timeType);
                    break;
                }
            }
            if ((daylight && timeType.value == TimeZoneFormat.TimeType.STANDARD) || (!daylight && timeType.value == TimeZoneFormat.TimeType.DAYLIGHT)) {
                final int offset = daylight ? (this.getRawOffset() + this.getDSTSavings()) : this.getRawOffset();
                result = ((style == 2) ? tzfmt.formatOffsetShortLocalizedGMT(offset) : tzfmt.formatOffsetLocalizedGMT(offset));
            }
        }
        else if (style == 5 || style == 4) {
            final TimeZoneFormat tzfmt = TimeZoneFormat.getInstance(locale);
            final int offset2 = (daylight && this.useDaylightTime()) ? (this.getRawOffset() + this.getDSTSavings()) : this.getRawOffset();
            switch (style) {
                case 5: {
                    result = tzfmt.formatOffsetLocalizedGMT(offset2);
                    break;
                }
                case 4: {
                    result = tzfmt.formatOffsetISO8601Basic(offset2, false, false, false);
                    break;
                }
            }
        }
        else {
            assert style == 6;
            final long date2 = System.currentTimeMillis();
            final TimeZoneNames tznames = TimeZoneNames.getInstance(locale);
            TimeZoneNames.NameType nameType = null;
            switch (style) {
                case 1: {
                    nameType = (daylight ? TimeZoneNames.NameType.LONG_DAYLIGHT : TimeZoneNames.NameType.LONG_STANDARD);
                    break;
                }
                case 0:
                case 6: {
                    nameType = (daylight ? TimeZoneNames.NameType.SHORT_DAYLIGHT : TimeZoneNames.NameType.SHORT_STANDARD);
                    break;
                }
            }
            result = tznames.getDisplayName(ZoneMeta.getCanonicalCLDRID(this), nameType, date2);
            if (result == null) {
                final TimeZoneFormat tzfmt2 = TimeZoneFormat.getInstance(locale);
                final int offset3 = (daylight && this.useDaylightTime()) ? (this.getRawOffset() + this.getDSTSavings()) : this.getRawOffset();
                result = ((style == 1) ? tzfmt2.formatOffsetLocalizedGMT(offset3) : tzfmt2.formatOffsetShortLocalizedGMT(offset3));
            }
        }
        assert result != null;
        return result;
    }
    
    public int getDSTSavings() {
        if (this.useDaylightTime()) {
            return 3600000;
        }
        return 0;
    }
    
    public abstract boolean useDaylightTime();
    
    public boolean observesDaylightTime() {
        return this.useDaylightTime() || this.inDaylightTime(new Date());
    }
    
    public abstract boolean inDaylightTime(final Date p0);
    
    public static TimeZone getTimeZone(final String ID) {
        return getTimeZone(ID, TimeZone.TZ_IMPL, false);
    }
    
    public static TimeZone getFrozenTimeZone(final String ID) {
        return getTimeZone(ID, TimeZone.TZ_IMPL, true);
    }
    
    public static TimeZone getTimeZone(final String ID, final int type) {
        return getTimeZone(ID, type, false);
    }
    
    private static synchronized TimeZone getTimeZone(final String ID, final int type, final boolean frozen) {
        TimeZone result;
        if (type == 1) {
            result = JavaTimeZone.createTimeZone(ID);
            if (result != null) {
                return frozen ? result.freeze() : result;
            }
        }
        else {
            if (ID == null) {
                throw new NullPointerException();
            }
            result = ZoneMeta.getSystemTimeZone(ID);
        }
        if (result == null) {
            result = ZoneMeta.getCustomTimeZone(ID);
        }
        if (result == null) {
            TimeZone.LOGGER.fine("\"" + ID + "\" is a bogus id so timezone is falling back to Etc/Unknown(GMT).");
            result = TimeZone.UNKNOWN_ZONE;
        }
        return frozen ? result : result.cloneAsThawed();
    }
    
    public static synchronized void setDefaultTimeZoneType(final int type) {
        if (type != 0 && type != 1) {
            throw new IllegalArgumentException("Invalid timezone type");
        }
        TimeZone.TZ_IMPL = type;
    }
    
    public static int getDefaultTimeZoneType() {
        return TimeZone.TZ_IMPL;
    }
    
    public static Set<String> getAvailableIDs(final SystemTimeZoneType zoneType, final String region, final Integer rawOffset) {
        return ZoneMeta.getAvailableIDs(zoneType, region, rawOffset);
    }
    
    public static String[] getAvailableIDs(final int rawOffset) {
        final Set<String> ids = getAvailableIDs(SystemTimeZoneType.ANY, null, rawOffset);
        return ids.toArray(new String[0]);
    }
    
    public static String[] getAvailableIDs(final String country) {
        final Set<String> ids = getAvailableIDs(SystemTimeZoneType.ANY, country, null);
        return ids.toArray(new String[0]);
    }
    
    public static String[] getAvailableIDs() {
        final Set<String> ids = getAvailableIDs(SystemTimeZoneType.ANY, null, null);
        return ids.toArray(new String[0]);
    }
    
    public static int countEquivalentIDs(final String id) {
        return ZoneMeta.countEquivalentIDs(id);
    }
    
    public static String getEquivalentID(final String id, final int index) {
        return ZoneMeta.getEquivalentID(id, index);
    }
    
    public static synchronized TimeZone getDefault() {
        if (TimeZone.defaultZone == null) {
            if (TimeZone.TZ_IMPL == 1) {
                TimeZone.defaultZone = new JavaTimeZone();
            }
            else {
                final java.util.TimeZone temp = java.util.TimeZone.getDefault();
                TimeZone.defaultZone = getFrozenTimeZone(temp.getID());
            }
        }
        return TimeZone.defaultZone.cloneAsThawed();
    }
    
    public static synchronized void setDefault(final TimeZone tz) {
        TimeZone.defaultZone = tz;
        java.util.TimeZone jdkZone = null;
        if (TimeZone.defaultZone instanceof JavaTimeZone) {
            jdkZone = ((JavaTimeZone)TimeZone.defaultZone).unwrap();
        }
        else if (tz != null) {
            if (tz instanceof OlsonTimeZone) {
                final String icuID = tz.getID();
                jdkZone = java.util.TimeZone.getTimeZone(icuID);
                if (!icuID.equals(jdkZone.getID())) {
                    jdkZone = null;
                }
            }
            if (jdkZone == null) {
                jdkZone = TimeZoneAdapter.wrap(tz);
            }
        }
        java.util.TimeZone.setDefault(jdkZone);
    }
    
    public boolean hasSameRules(final TimeZone other) {
        return other != null && this.getRawOffset() == other.getRawOffset() && this.useDaylightTime() == other.useDaylightTime();
    }
    
    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj != null && this.getClass() == obj.getClass() && this.ID.equals(((TimeZone)obj).ID));
    }
    
    @Override
    public int hashCode() {
        return this.ID.hashCode();
    }
    
    public static synchronized String getTZDataVersion() {
        if (TimeZone.TZDATA_VERSION == null) {
            final UResourceBundle tzbundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64");
            TimeZone.TZDATA_VERSION = tzbundle.getString("TZVersion");
        }
        return TimeZone.TZDATA_VERSION;
    }
    
    public static String getCanonicalID(final String id) {
        return getCanonicalID(id, null);
    }
    
    public static String getCanonicalID(final String id, final boolean[] isSystemID) {
        String canonicalID = null;
        boolean systemTzid = false;
        if (id != null && id.length() != 0) {
            if (id.equals("Etc/Unknown")) {
                canonicalID = "Etc/Unknown";
                systemTzid = false;
            }
            else {
                canonicalID = ZoneMeta.getCanonicalCLDRID(id);
                if (canonicalID != null) {
                    systemTzid = true;
                }
                else {
                    canonicalID = ZoneMeta.getCustomID(id);
                }
            }
        }
        if (isSystemID != null) {
            isSystemID[0] = systemTzid;
        }
        return canonicalID;
    }
    
    public static String getRegion(final String id) {
        String region = null;
        if (!id.equals("Etc/Unknown")) {
            region = ZoneMeta.getRegion(id);
        }
        if (region == null) {
            throw new IllegalArgumentException("Unknown system zone id: " + id);
        }
        return region;
    }
    
    public boolean isFrozen() {
        return false;
    }
    
    public TimeZone freeze() {
        throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
    }
    
    public TimeZone cloneAsThawed() {
        try {
            final TimeZone other = (TimeZone)super.clone();
            return other;
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        LOGGER = Logger.getLogger("com.ibm.icu.util.TimeZone");
        UNKNOWN_ZONE = new SimpleTimeZone(0, "Etc/Unknown").freeze();
        GMT_ZONE = new SimpleTimeZone(0, "Etc/GMT").freeze();
        TimeZone.defaultZone = null;
        TimeZone.TZDATA_VERSION = null;
        TimeZone.TZ_IMPL = 0;
        final String type = ICUConfig.get("com.ibm.icu.util.TimeZone.DefaultTimeZoneType", "ICU");
        if (type.equalsIgnoreCase("JDK")) {
            TimeZone.TZ_IMPL = 1;
        }
    }
    
    public enum SystemTimeZoneType
    {
        ANY, 
        CANONICAL, 
        CANONICAL_LOCATION;
    }
}
