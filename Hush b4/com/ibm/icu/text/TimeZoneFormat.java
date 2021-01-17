// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.TimeZoneNamesImpl;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;
import com.ibm.icu.lang.UCharacter;
import java.util.List;
import java.util.BitSet;
import java.util.ArrayList;
import java.util.Date;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import com.ibm.icu.util.Calendar;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Collection;
import java.text.ParsePosition;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZone;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import java.io.ObjectStreamField;
import com.ibm.icu.impl.TextTrieMap;
import java.util.EnumSet;
import com.ibm.icu.impl.TimeZoneGenericNames;
import com.ibm.icu.util.ULocale;
import java.io.Serializable;
import com.ibm.icu.util.Freezable;

public class TimeZoneFormat extends UFormat implements Freezable<TimeZoneFormat>, Serializable
{
    private static final long serialVersionUID = 2281246852693575022L;
    private static final int ISO_Z_STYLE_FLAG = 128;
    private static final int ISO_LOCAL_STYLE_FLAG = 256;
    private ULocale _locale;
    private TimeZoneNames _tznames;
    private String _gmtPattern;
    private String[] _gmtOffsetPatterns;
    private String[] _gmtOffsetDigits;
    private String _gmtZeroFormat;
    private boolean _parseAllStyles;
    private transient volatile TimeZoneGenericNames _gnames;
    private transient String _gmtPatternPrefix;
    private transient String _gmtPatternSuffix;
    private transient Object[][] _gmtOffsetPatternItems;
    private transient boolean _abuttingOffsetHoursAndMinutes;
    private transient String _region;
    private transient boolean _frozen;
    private static final String TZID_GMT = "Etc/GMT";
    private static final String[] ALT_GMT_STRINGS;
    private static final String DEFAULT_GMT_PATTERN = "GMT{0}";
    private static final String DEFAULT_GMT_ZERO = "GMT";
    private static final String[] DEFAULT_GMT_DIGITS;
    private static final char DEFAULT_GMT_OFFSET_SEP = ':';
    private static final String ASCII_DIGITS = "0123456789";
    private static final String ISO8601_UTC = "Z";
    private static final String UNKNOWN_ZONE_ID = "Etc/Unknown";
    private static final String UNKNOWN_SHORT_ZONE_ID = "unk";
    private static final String UNKNOWN_LOCATION = "Unknown";
    private static final GMTOffsetPatternType[] PARSE_GMT_OFFSET_TYPES;
    private static final int MILLIS_PER_HOUR = 3600000;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int MAX_OFFSET = 86400000;
    private static final int MAX_OFFSET_HOUR = 23;
    private static final int MAX_OFFSET_MINUTE = 59;
    private static final int MAX_OFFSET_SECOND = 59;
    private static final int UNKNOWN_OFFSET = Integer.MAX_VALUE;
    private static TimeZoneFormatCache _tzfCache;
    private static final EnumSet<TimeZoneNames.NameType> ALL_SIMPLE_NAME_TYPES;
    private static final EnumSet<TimeZoneGenericNames.GenericNameType> ALL_GENERIC_NAME_TYPES;
    private static volatile TextTrieMap<String> ZONE_ID_TRIE;
    private static volatile TextTrieMap<String> SHORT_ZONE_ID_TRIE;
    private static final ObjectStreamField[] serialPersistentFields;
    
    protected TimeZoneFormat(final ULocale locale) {
        this._locale = locale;
        this._tznames = TimeZoneNames.getInstance(locale);
        String gmtPattern = null;
        String hourFormats = null;
        this._gmtZeroFormat = "GMT";
        try {
            final ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", locale);
            try {
                gmtPattern = bundle.getStringWithFallback("zoneStrings/gmtFormat");
            }
            catch (MissingResourceException ex) {}
            try {
                hourFormats = bundle.getStringWithFallback("zoneStrings/hourFormat");
            }
            catch (MissingResourceException ex2) {}
            try {
                this._gmtZeroFormat = bundle.getStringWithFallback("zoneStrings/gmtZeroFormat");
            }
            catch (MissingResourceException ex3) {}
        }
        catch (MissingResourceException ex4) {}
        if (gmtPattern == null) {
            gmtPattern = "GMT{0}";
        }
        this.initGMTPattern(gmtPattern);
        final String[] gmtOffsetPatterns = new String[GMTOffsetPatternType.values().length];
        if (hourFormats != null) {
            final String[] hourPatterns = hourFormats.split(";", 2);
            gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_H.ordinal()] = truncateOffsetPattern(hourPatterns[0]);
            gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_HM.ordinal()] = hourPatterns[0];
            gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_HMS.ordinal()] = expandOffsetPattern(hourPatterns[0]);
            gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_H.ordinal()] = truncateOffsetPattern(hourPatterns[1]);
            gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_HM.ordinal()] = hourPatterns[1];
            gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_HMS.ordinal()] = expandOffsetPattern(hourPatterns[1]);
        }
        else {
            for (final GMTOffsetPatternType patType : GMTOffsetPatternType.values()) {
                gmtOffsetPatterns[patType.ordinal()] = patType.defaultPattern();
            }
        }
        this.initGMTOffsetPatterns(gmtOffsetPatterns);
        this._gmtOffsetDigits = TimeZoneFormat.DEFAULT_GMT_DIGITS;
        final NumberingSystem ns = NumberingSystem.getInstance(locale);
        if (!ns.isAlgorithmic()) {
            this._gmtOffsetDigits = toCodePoints(ns.getDescription());
        }
    }
    
    public static TimeZoneFormat getInstance(final ULocale locale) {
        if (locale == null) {
            throw new NullPointerException("locale is null");
        }
        return TimeZoneFormat._tzfCache.getInstance(locale, locale);
    }
    
    public TimeZoneNames getTimeZoneNames() {
        return this._tznames;
    }
    
    private TimeZoneGenericNames getTimeZoneGenericNames() {
        if (this._gnames == null) {
            synchronized (this) {
                if (this._gnames == null) {
                    this._gnames = TimeZoneGenericNames.getInstance(this._locale);
                }
            }
        }
        return this._gnames;
    }
    
    public TimeZoneFormat setTimeZoneNames(final TimeZoneNames tznames) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
        this._tznames = tznames;
        this._gnames = new TimeZoneGenericNames(this._locale, this._tznames);
        return this;
    }
    
    public String getGMTPattern() {
        return this._gmtPattern;
    }
    
    public TimeZoneFormat setGMTPattern(final String pattern) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
        this.initGMTPattern(pattern);
        return this;
    }
    
    public String getGMTOffsetPattern(final GMTOffsetPatternType type) {
        return this._gmtOffsetPatterns[type.ordinal()];
    }
    
    public TimeZoneFormat setGMTOffsetPattern(final GMTOffsetPatternType type, final String pattern) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
        if (pattern == null) {
            throw new NullPointerException("Null GMT offset pattern");
        }
        final Object[] parsedItems = parseOffsetPattern(pattern, type.required());
        this._gmtOffsetPatterns[type.ordinal()] = pattern;
        this._gmtOffsetPatternItems[type.ordinal()] = parsedItems;
        this.checkAbuttingHoursAndMinutes();
        return this;
    }
    
    public String getGMTOffsetDigits() {
        final StringBuilder buf = new StringBuilder(this._gmtOffsetDigits.length);
        for (final String digit : this._gmtOffsetDigits) {
            buf.append(digit);
        }
        return buf.toString();
    }
    
    public TimeZoneFormat setGMTOffsetDigits(final String digits) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
        if (digits == null) {
            throw new NullPointerException("Null GMT offset digits");
        }
        final String[] digitArray = toCodePoints(digits);
        if (digitArray.length != 10) {
            throw new IllegalArgumentException("Length of digits must be 10");
        }
        this._gmtOffsetDigits = digitArray;
        return this;
    }
    
    public String getGMTZeroFormat() {
        return this._gmtZeroFormat;
    }
    
    public TimeZoneFormat setGMTZeroFormat(final String gmtZeroFormat) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
        if (gmtZeroFormat == null) {
            throw new NullPointerException("Null GMT zero format");
        }
        if (gmtZeroFormat.length() == 0) {
            throw new IllegalArgumentException("Empty GMT zero format");
        }
        this._gmtZeroFormat = gmtZeroFormat;
        return this;
    }
    
    public TimeZoneFormat setDefaultParseOptions(final EnumSet<ParseOption> options) {
        this._parseAllStyles = options.contains(ParseOption.ALL_STYLES);
        return this;
    }
    
    public EnumSet<ParseOption> getDefaultParseOptions() {
        if (this._parseAllStyles) {
            return EnumSet.of(ParseOption.ALL_STYLES);
        }
        return EnumSet.noneOf(ParseOption.class);
    }
    
    public final String formatOffsetISO8601Basic(final int offset, final boolean useUtcIndicator, final boolean isShort, final boolean ignoreSeconds) {
        return this.formatOffsetISO8601(offset, true, useUtcIndicator, isShort, ignoreSeconds);
    }
    
    public final String formatOffsetISO8601Extended(final int offset, final boolean useUtcIndicator, final boolean isShort, final boolean ignoreSeconds) {
        return this.formatOffsetISO8601(offset, false, useUtcIndicator, isShort, ignoreSeconds);
    }
    
    public String formatOffsetLocalizedGMT(final int offset) {
        return this.formatOffsetLocalizedGMT(offset, false);
    }
    
    public String formatOffsetShortLocalizedGMT(final int offset) {
        return this.formatOffsetLocalizedGMT(offset, true);
    }
    
    public final String format(final Style style, final TimeZone tz, final long date) {
        return this.format(style, tz, date, null);
    }
    
    public String format(final Style style, final TimeZone tz, final long date, final Output<TimeType> timeType) {
        String result = null;
        if (timeType != null) {
            timeType.value = TimeType.UNKNOWN;
        }
        switch (style) {
            case GENERIC_LOCATION: {
                result = this.getTimeZoneGenericNames().getGenericLocationName(ZoneMeta.getCanonicalCLDRID(tz));
                break;
            }
            case GENERIC_LONG: {
                result = this.getTimeZoneGenericNames().getDisplayName(tz, TimeZoneGenericNames.GenericNameType.LONG, date);
                break;
            }
            case GENERIC_SHORT: {
                result = this.getTimeZoneGenericNames().getDisplayName(tz, TimeZoneGenericNames.GenericNameType.SHORT, date);
                break;
            }
            case SPECIFIC_LONG: {
                result = this.formatSpecific(tz, TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, date, timeType);
                break;
            }
            case SPECIFIC_SHORT: {
                result = this.formatSpecific(tz, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, date, timeType);
                break;
            }
        }
        if (result == null) {
            final int[] offsets = { 0, 0 };
            tz.getOffset(date, false, offsets);
            final int offset = offsets[0] + offsets[1];
            switch (style) {
                case GENERIC_LOCATION:
                case GENERIC_LONG:
                case SPECIFIC_LONG:
                case LOCALIZED_GMT: {
                    result = this.formatOffsetLocalizedGMT(offset);
                    break;
                }
                case GENERIC_SHORT:
                case SPECIFIC_SHORT:
                case LOCALIZED_GMT_SHORT: {
                    result = this.formatOffsetShortLocalizedGMT(offset);
                    break;
                }
                case ISO_BASIC_SHORT: {
                    result = this.formatOffsetISO8601Basic(offset, true, true, true);
                    break;
                }
                case ISO_BASIC_LOCAL_SHORT: {
                    result = this.formatOffsetISO8601Basic(offset, false, true, true);
                    break;
                }
                case ISO_BASIC_FIXED: {
                    result = this.formatOffsetISO8601Basic(offset, true, false, true);
                    break;
                }
                case ISO_BASIC_LOCAL_FIXED: {
                    result = this.formatOffsetISO8601Basic(offset, false, false, true);
                    break;
                }
                case ISO_BASIC_FULL: {
                    result = this.formatOffsetISO8601Basic(offset, true, false, false);
                    break;
                }
                case ISO_BASIC_LOCAL_FULL: {
                    result = this.formatOffsetISO8601Basic(offset, false, false, false);
                    break;
                }
                case ISO_EXTENDED_FIXED: {
                    result = this.formatOffsetISO8601Extended(offset, true, false, true);
                    break;
                }
                case ISO_EXTENDED_LOCAL_FIXED: {
                    result = this.formatOffsetISO8601Extended(offset, false, false, true);
                    break;
                }
                case ISO_EXTENDED_FULL: {
                    result = this.formatOffsetISO8601Extended(offset, true, false, false);
                    break;
                }
                case ISO_EXTENDED_LOCAL_FULL: {
                    result = this.formatOffsetISO8601Extended(offset, false, false, false);
                    break;
                }
                case ZONE_ID: {
                    result = tz.getID();
                    break;
                }
                case ZONE_ID_SHORT: {
                    result = ZoneMeta.getShortID(tz);
                    if (result == null) {
                        result = "unk";
                        break;
                    }
                    break;
                }
                case EXEMPLAR_LOCATION: {
                    result = this.formatExemplarLocation(tz);
                    break;
                }
            }
            if (timeType != null) {
                timeType.value = ((offsets[1] != 0) ? TimeType.DAYLIGHT : TimeType.STANDARD);
            }
        }
        assert result != null;
        return result;
    }
    
    public final int parseOffsetISO8601(final String text, final ParsePosition pos) {
        return parseOffsetISO8601(text, pos, false, null);
    }
    
    public int parseOffsetLocalizedGMT(final String text, final ParsePosition pos) {
        return this.parseOffsetLocalizedGMT(text, pos, false, null);
    }
    
    public int parseOffsetShortLocalizedGMT(final String text, final ParsePosition pos) {
        return this.parseOffsetLocalizedGMT(text, pos, true, null);
    }
    
    public TimeZone parse(final Style style, final String text, final ParsePosition pos, final EnumSet<ParseOption> options, Output<TimeType> timeType) {
        if (timeType == null) {
            timeType = new Output<TimeType>(TimeType.UNKNOWN);
        }
        else {
            timeType.value = TimeType.UNKNOWN;
        }
        final int startIdx = pos.getIndex();
        final int maxPos = text.length();
        final boolean fallbackLocalizedGMT = style == Style.SPECIFIC_LONG || style == Style.GENERIC_LONG || style == Style.GENERIC_LOCATION;
        final boolean fallbackShortLocalizedGMT = style == Style.SPECIFIC_SHORT || style == Style.GENERIC_SHORT;
        int evaluated = 0;
        final ParsePosition tmpPos = new ParsePosition(startIdx);
        int parsedOffset = Integer.MAX_VALUE;
        int parsedPos = -1;
        if (fallbackLocalizedGMT || fallbackShortLocalizedGMT) {
            final Output<Boolean> hasDigitOffset = new Output<Boolean>(false);
            final int offset = this.parseOffsetLocalizedGMT(text, tmpPos, fallbackShortLocalizedGMT, hasDigitOffset);
            if (tmpPos.getErrorIndex() == -1) {
                if (tmpPos.getIndex() == maxPos || hasDigitOffset.value) {
                    pos.setIndex(tmpPos.getIndex());
                    return this.getTimeZoneForOffset(offset);
                }
                parsedOffset = offset;
                parsedPos = tmpPos.getIndex();
            }
            evaluated |= (Style.LOCALIZED_GMT.flag | Style.LOCALIZED_GMT_SHORT.flag);
        }
        switch (style) {
            case LOCALIZED_GMT: {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final int offset = this.parseOffsetLocalizedGMT(text, tmpPos);
                if (tmpPos.getErrorIndex() == -1) {
                    pos.setIndex(tmpPos.getIndex());
                    return this.getTimeZoneForOffset(offset);
                }
                evaluated |= Style.LOCALIZED_GMT_SHORT.flag;
                break;
            }
            case LOCALIZED_GMT_SHORT: {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final int offset = this.parseOffsetShortLocalizedGMT(text, tmpPos);
                if (tmpPos.getErrorIndex() == -1) {
                    pos.setIndex(tmpPos.getIndex());
                    return this.getTimeZoneForOffset(offset);
                }
                evaluated |= Style.LOCALIZED_GMT.flag;
                break;
            }
            case ISO_BASIC_SHORT:
            case ISO_BASIC_FIXED:
            case ISO_BASIC_FULL:
            case ISO_EXTENDED_FIXED:
            case ISO_EXTENDED_FULL: {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final int offset = this.parseOffsetISO8601(text, tmpPos);
                if (tmpPos.getErrorIndex() == -1) {
                    pos.setIndex(tmpPos.getIndex());
                    return this.getTimeZoneForOffset(offset);
                }
                break;
            }
            case ISO_BASIC_LOCAL_SHORT:
            case ISO_BASIC_LOCAL_FIXED:
            case ISO_BASIC_LOCAL_FULL:
            case ISO_EXTENDED_LOCAL_FIXED:
            case ISO_EXTENDED_LOCAL_FULL: {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final Output<Boolean> hasDigitOffset = new Output<Boolean>(false);
                final int offset = parseOffsetISO8601(text, tmpPos, false, hasDigitOffset);
                if (tmpPos.getErrorIndex() == -1 && hasDigitOffset.value) {
                    pos.setIndex(tmpPos.getIndex());
                    return this.getTimeZoneForOffset(offset);
                }
                break;
            }
            case SPECIFIC_LONG:
            case SPECIFIC_SHORT: {
                EnumSet<TimeZoneNames.NameType> nameTypes = null;
                if (style == Style.SPECIFIC_LONG) {
                    nameTypes = EnumSet.of(TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT);
                }
                else {
                    assert style == Style.SPECIFIC_SHORT;
                    nameTypes = EnumSet.of(TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT);
                }
                final Collection<TimeZoneNames.MatchInfo> specificMatches = this._tznames.find(text, startIdx, nameTypes);
                if (specificMatches == null) {
                    break;
                }
                TimeZoneNames.MatchInfo specificMatch = null;
                for (final TimeZoneNames.MatchInfo match : specificMatches) {
                    if (startIdx + match.matchLength() > parsedPos) {
                        specificMatch = match;
                        parsedPos = startIdx + match.matchLength();
                    }
                }
                if (specificMatch != null) {
                    timeType.value = this.getTimeType(specificMatch.nameType());
                    pos.setIndex(parsedPos);
                    return TimeZone.getTimeZone(this.getTimeZoneID(specificMatch.tzID(), specificMatch.mzID()));
                }
                break;
            }
            case GENERIC_LOCATION:
            case GENERIC_LONG:
            case GENERIC_SHORT: {
                EnumSet<TimeZoneGenericNames.GenericNameType> genericNameTypes = null;
                switch (style) {
                    case GENERIC_LOCATION: {
                        genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.LOCATION);
                        break;
                    }
                    case GENERIC_LONG: {
                        genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.LONG, TimeZoneGenericNames.GenericNameType.LOCATION);
                        break;
                    }
                    case GENERIC_SHORT: {
                        genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.SHORT, TimeZoneGenericNames.GenericNameType.LOCATION);
                        break;
                    }
                }
                final TimeZoneGenericNames.GenericMatchInfo bestGeneric = this.getTimeZoneGenericNames().findBestMatch(text, startIdx, genericNameTypes);
                if (bestGeneric != null && startIdx + bestGeneric.matchLength() > parsedPos) {
                    timeType.value = bestGeneric.timeType();
                    pos.setIndex(startIdx + bestGeneric.matchLength());
                    return TimeZone.getTimeZone(bestGeneric.tzID());
                }
                break;
            }
            case ZONE_ID: {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final String id = parseZoneID(text, tmpPos);
                if (tmpPos.getErrorIndex() == -1) {
                    pos.setIndex(tmpPos.getIndex());
                    return TimeZone.getTimeZone(id);
                }
                break;
            }
            case ZONE_ID_SHORT: {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final String id = parseShortZoneID(text, tmpPos);
                if (tmpPos.getErrorIndex() == -1) {
                    pos.setIndex(tmpPos.getIndex());
                    return TimeZone.getTimeZone(id);
                }
                break;
            }
            case EXEMPLAR_LOCATION: {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final String id = this.parseExemplarLocation(text, tmpPos);
                if (tmpPos.getErrorIndex() == -1) {
                    pos.setIndex(tmpPos.getIndex());
                    return TimeZone.getTimeZone(id);
                }
                break;
            }
        }
        evaluated |= style.flag;
        if (parsedPos > startIdx) {
            assert parsedOffset != Integer.MAX_VALUE;
            pos.setIndex(parsedPos);
            return this.getTimeZoneForOffset(parsedOffset);
        }
        else {
            String parsedID = null;
            TimeType parsedTimeType = TimeType.UNKNOWN;
            assert parsedPos < 0;
            assert parsedOffset == Integer.MAX_VALUE;
            if (parsedPos < maxPos && ((evaluated & 0x80) == 0x0 || (evaluated & 0x100) == 0x0)) {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final Output<Boolean> hasDigitOffset2 = new Output<Boolean>(false);
                final int offset = parseOffsetISO8601(text, tmpPos, false, hasDigitOffset2);
                if (tmpPos.getErrorIndex() == -1) {
                    if (tmpPos.getIndex() == maxPos || hasDigitOffset2.value) {
                        pos.setIndex(tmpPos.getIndex());
                        return this.getTimeZoneForOffset(offset);
                    }
                    if (parsedPos < tmpPos.getIndex()) {
                        parsedOffset = offset;
                        parsedID = null;
                        parsedTimeType = TimeType.UNKNOWN;
                        parsedPos = tmpPos.getIndex();
                        assert parsedPos == startIdx + 1;
                    }
                }
            }
            if (parsedPos < maxPos && (evaluated & Style.LOCALIZED_GMT.flag) == 0x0) {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final Output<Boolean> hasDigitOffset2 = new Output<Boolean>(false);
                final int offset = this.parseOffsetLocalizedGMT(text, tmpPos, false, hasDigitOffset2);
                if (tmpPos.getErrorIndex() == -1) {
                    if (tmpPos.getIndex() == maxPos || hasDigitOffset2.value) {
                        pos.setIndex(tmpPos.getIndex());
                        return this.getTimeZoneForOffset(offset);
                    }
                    if (parsedPos < tmpPos.getIndex()) {
                        parsedOffset = offset;
                        parsedID = null;
                        parsedTimeType = TimeType.UNKNOWN;
                        parsedPos = tmpPos.getIndex();
                    }
                }
            }
            if (parsedPos < maxPos && (evaluated & Style.LOCALIZED_GMT_SHORT.flag) == 0x0) {
                tmpPos.setIndex(startIdx);
                tmpPos.setErrorIndex(-1);
                final Output<Boolean> hasDigitOffset2 = new Output<Boolean>(false);
                final int offset = this.parseOffsetLocalizedGMT(text, tmpPos, true, hasDigitOffset2);
                if (tmpPos.getErrorIndex() == -1) {
                    if (tmpPos.getIndex() == maxPos || hasDigitOffset2.value) {
                        pos.setIndex(tmpPos.getIndex());
                        return this.getTimeZoneForOffset(offset);
                    }
                    if (parsedPos < tmpPos.getIndex()) {
                        parsedOffset = offset;
                        parsedID = null;
                        parsedTimeType = TimeType.UNKNOWN;
                        parsedPos = tmpPos.getIndex();
                    }
                }
            }
            final boolean parseAllStyles = (options == null) ? this.getDefaultParseOptions().contains(ParseOption.ALL_STYLES) : options.contains(ParseOption.ALL_STYLES);
            if (parseAllStyles) {
                if (parsedPos < maxPos) {
                    final Collection<TimeZoneNames.MatchInfo> specificMatches2 = this._tznames.find(text, startIdx, TimeZoneFormat.ALL_SIMPLE_NAME_TYPES);
                    TimeZoneNames.MatchInfo specificMatch2 = null;
                    int matchPos = -1;
                    if (specificMatches2 != null) {
                        for (final TimeZoneNames.MatchInfo match2 : specificMatches2) {
                            if (startIdx + match2.matchLength() > matchPos) {
                                specificMatch2 = match2;
                                matchPos = startIdx + match2.matchLength();
                            }
                        }
                    }
                    if (parsedPos < matchPos) {
                        parsedPos = matchPos;
                        parsedID = this.getTimeZoneID(specificMatch2.tzID(), specificMatch2.mzID());
                        parsedTimeType = this.getTimeType(specificMatch2.nameType());
                        parsedOffset = Integer.MAX_VALUE;
                    }
                }
                if (parsedPos < maxPos) {
                    final TimeZoneGenericNames.GenericMatchInfo genericMatch = this.getTimeZoneGenericNames().findBestMatch(text, startIdx, TimeZoneFormat.ALL_GENERIC_NAME_TYPES);
                    if (genericMatch != null && parsedPos < startIdx + genericMatch.matchLength()) {
                        parsedPos = startIdx + genericMatch.matchLength();
                        parsedID = genericMatch.tzID();
                        parsedTimeType = genericMatch.timeType();
                        parsedOffset = Integer.MAX_VALUE;
                    }
                }
                if (parsedPos < maxPos && (evaluated & Style.ZONE_ID.flag) == 0x0) {
                    tmpPos.setIndex(startIdx);
                    tmpPos.setErrorIndex(-1);
                    final String id2 = parseZoneID(text, tmpPos);
                    if (tmpPos.getErrorIndex() == -1 && parsedPos < tmpPos.getIndex()) {
                        parsedPos = tmpPos.getIndex();
                        parsedID = id2;
                        parsedTimeType = TimeType.UNKNOWN;
                        parsedOffset = Integer.MAX_VALUE;
                    }
                }
                if (parsedPos < maxPos && (evaluated & Style.ZONE_ID_SHORT.flag) == 0x0) {
                    tmpPos.setIndex(startIdx);
                    tmpPos.setErrorIndex(-1);
                    final String id2 = parseShortZoneID(text, tmpPos);
                    if (tmpPos.getErrorIndex() == -1 && parsedPos < tmpPos.getIndex()) {
                        parsedPos = tmpPos.getIndex();
                        parsedID = id2;
                        parsedTimeType = TimeType.UNKNOWN;
                        parsedOffset = Integer.MAX_VALUE;
                    }
                }
            }
            if (parsedPos > startIdx) {
                TimeZone parsedTZ = null;
                if (parsedID != null) {
                    parsedTZ = TimeZone.getTimeZone(parsedID);
                }
                else {
                    assert parsedOffset != Integer.MAX_VALUE;
                    parsedTZ = this.getTimeZoneForOffset(parsedOffset);
                }
                timeType.value = parsedTimeType;
                pos.setIndex(parsedPos);
                return parsedTZ;
            }
            pos.setErrorIndex(startIdx);
            return null;
        }
    }
    
    public TimeZone parse(final Style style, final String text, final ParsePosition pos, final Output<TimeType> timeType) {
        return this.parse(style, text, pos, null, timeType);
    }
    
    public final TimeZone parse(final String text, final ParsePosition pos) {
        return this.parse(Style.GENERIC_LOCATION, text, pos, EnumSet.of(ParseOption.ALL_STYLES), null);
    }
    
    public final TimeZone parse(final String text) throws ParseException {
        final ParsePosition pos = new ParsePosition(0);
        final TimeZone tz = this.parse(text, pos);
        if (pos.getErrorIndex() >= 0) {
            throw new ParseException("Unparseable time zone: \"" + text + "\"", 0);
        }
        assert tz != null;
        return tz;
    }
    
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        TimeZone tz = null;
        long date = System.currentTimeMillis();
        if (obj instanceof TimeZone) {
            tz = (TimeZone)obj;
        }
        else {
            if (!(obj instanceof Calendar)) {
                throw new IllegalArgumentException("Cannot format given Object (" + obj.getClass().getName() + ") as a time zone");
            }
            tz = ((Calendar)obj).getTimeZone();
            date = ((Calendar)obj).getTimeInMillis();
        }
        assert tz != null;
        final String result = this.formatOffsetLocalizedGMT(tz.getOffset(date));
        toAppendTo.append(result);
        if (pos.getFieldAttribute() == DateFormat.Field.TIME_ZONE || pos.getField() == 17) {
            pos.setBeginIndex(0);
            pos.setEndIndex(result.length());
        }
        return toAppendTo;
    }
    
    @Override
    public AttributedCharacterIterator formatToCharacterIterator(final Object obj) {
        StringBuffer toAppendTo = new StringBuffer();
        final FieldPosition pos = new FieldPosition(0);
        toAppendTo = this.format(obj, toAppendTo, pos);
        final AttributedString as = new AttributedString(toAppendTo.toString());
        as.addAttribute(DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE);
        return as.getIterator();
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return this.parse(source, pos);
    }
    
    private String formatOffsetLocalizedGMT(int offset, final boolean isShort) {
        if (offset == 0) {
            return this._gmtZeroFormat;
        }
        final StringBuilder buf = new StringBuilder();
        boolean positive = true;
        if (offset < 0) {
            offset = -offset;
            positive = false;
        }
        final int offsetH = offset / 3600000;
        offset %= 3600000;
        final int offsetM = offset / 60000;
        offset %= 60000;
        final int offsetS = offset / 1000;
        if (offsetH > 23 || offsetM > 59 || offsetS > 59) {
            throw new IllegalArgumentException("Offset out of range :" + offset);
        }
        Object[] offsetPatternItems;
        if (positive) {
            if (offsetS != 0) {
                offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.POSITIVE_HMS.ordinal()];
            }
            else if (offsetM != 0 || !isShort) {
                offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.POSITIVE_HM.ordinal()];
            }
            else {
                offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.POSITIVE_H.ordinal()];
            }
        }
        else if (offsetS != 0) {
            offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.NEGATIVE_HMS.ordinal()];
        }
        else if (offsetM != 0 || !isShort) {
            offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.NEGATIVE_HM.ordinal()];
        }
        else {
            offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.NEGATIVE_H.ordinal()];
        }
        buf.append(this._gmtPatternPrefix);
        for (final Object item : offsetPatternItems) {
            if (item instanceof String) {
                buf.append((String)item);
            }
            else if (item instanceof GMTOffsetField) {
                final GMTOffsetField field = (GMTOffsetField)item;
                switch (field.getType()) {
                    case 'H': {
                        this.appendOffsetDigits(buf, offsetH, isShort ? 1 : 2);
                        break;
                    }
                    case 'm': {
                        this.appendOffsetDigits(buf, offsetM, 2);
                        break;
                    }
                    case 's': {
                        this.appendOffsetDigits(buf, offsetS, 2);
                        break;
                    }
                }
            }
        }
        buf.append(this._gmtPatternSuffix);
        return buf.toString();
    }
    
    private String formatOffsetISO8601(final int offset, final boolean isBasic, final boolean useUtcIndicator, final boolean isShort, final boolean ignoreSeconds) {
        int absOffset = (offset < 0) ? (-offset) : offset;
        if (useUtcIndicator && (absOffset < 1000 || (ignoreSeconds && absOffset < 60000))) {
            return "Z";
        }
        final OffsetFields minFields = isShort ? OffsetFields.H : OffsetFields.HM;
        final OffsetFields maxFields = ignoreSeconds ? OffsetFields.HM : OffsetFields.HMS;
        final Character sep = isBasic ? null : Character.valueOf(':');
        if (absOffset >= 86400000) {
            throw new IllegalArgumentException("Offset out of range :" + offset);
        }
        final int[] fields = { absOffset / 3600000, 0, 0 };
        absOffset %= 3600000;
        fields[1] = absOffset / 60000;
        absOffset %= 60000;
        fields[2] = absOffset / 1000;
        assert fields[0] >= 0 && fields[0] <= 23;
        assert fields[1] >= 0 && fields[1] <= 59;
        assert fields[2] >= 0 && fields[2] <= 59;
        int lastIdx;
        for (lastIdx = maxFields.ordinal(); lastIdx > minFields.ordinal() && fields[lastIdx] == 0; --lastIdx) {}
        final StringBuilder buf = new StringBuilder();
        char sign = '+';
        if (offset < 0) {
            for (int idx = 0; idx <= lastIdx; ++idx) {
                if (fields[idx] != 0) {
                    sign = '-';
                    break;
                }
            }
        }
        buf.append(sign);
        for (int idx = 0; idx <= lastIdx; ++idx) {
            if (sep != null && idx != 0) {
                buf.append(sep);
            }
            if (fields[idx] < 10) {
                buf.append('0');
            }
            buf.append(fields[idx]);
        }
        return buf.toString();
    }
    
    private String formatSpecific(final TimeZone tz, final TimeZoneNames.NameType stdType, final TimeZoneNames.NameType dstType, final long date, final Output<TimeType> timeType) {
        assert stdType == TimeZoneNames.NameType.SHORT_STANDARD;
        assert dstType == TimeZoneNames.NameType.SHORT_DAYLIGHT;
        final boolean isDaylight = tz.inDaylightTime(new Date(date));
        final String name = isDaylight ? this.getTimeZoneNames().getDisplayName(ZoneMeta.getCanonicalCLDRID(tz), dstType, date) : this.getTimeZoneNames().getDisplayName(ZoneMeta.getCanonicalCLDRID(tz), stdType, date);
        if (name != null && timeType != null) {
            timeType.value = (isDaylight ? TimeType.DAYLIGHT : TimeType.STANDARD);
        }
        return name;
    }
    
    private String formatExemplarLocation(final TimeZone tz) {
        String location = this.getTimeZoneNames().getExemplarLocationName(ZoneMeta.getCanonicalCLDRID(tz));
        if (location == null) {
            location = this.getTimeZoneNames().getExemplarLocationName("Etc/Unknown");
            if (location == null) {
                location = "Unknown";
            }
        }
        return location;
    }
    
    private String getTimeZoneID(final String tzID, final String mzID) {
        String id = tzID;
        if (id == null) {
            assert mzID != null;
            id = this._tznames.getReferenceZoneID(mzID, this.getTargetRegion());
            if (id == null) {
                throw new IllegalArgumentException("Invalid mzID: " + mzID);
            }
        }
        return id;
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
    
    private TimeType getTimeType(final TimeZoneNames.NameType nameType) {
        switch (nameType) {
            case LONG_STANDARD:
            case SHORT_STANDARD: {
                return TimeType.STANDARD;
            }
            case LONG_DAYLIGHT:
            case SHORT_DAYLIGHT: {
                return TimeType.DAYLIGHT;
            }
            default: {
                return TimeType.UNKNOWN;
            }
        }
    }
    
    private void initGMTPattern(final String gmtPattern) {
        final int idx = gmtPattern.indexOf("{0}");
        if (idx < 0) {
            throw new IllegalArgumentException("Bad localized GMT pattern: " + gmtPattern);
        }
        this._gmtPattern = gmtPattern;
        this._gmtPatternPrefix = unquote(gmtPattern.substring(0, idx));
        this._gmtPatternSuffix = unquote(gmtPattern.substring(idx + 3));
    }
    
    private static String unquote(final String s) {
        if (s.indexOf(39) < 0) {
            return s;
        }
        boolean isPrevQuote = false;
        boolean inQuote = false;
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (c == '\'') {
                if (isPrevQuote) {
                    buf.append(c);
                    isPrevQuote = false;
                }
                else {
                    isPrevQuote = true;
                }
                inQuote = !inQuote;
            }
            else {
                isPrevQuote = false;
                buf.append(c);
            }
        }
        return buf.toString();
    }
    
    private void initGMTOffsetPatterns(final String[] gmtOffsetPatterns) {
        final int size = GMTOffsetPatternType.values().length;
        if (gmtOffsetPatterns.length < size) {
            throw new IllegalArgumentException("Insufficient number of elements in gmtOffsetPatterns");
        }
        final Object[][] gmtOffsetPatternItems = new Object[size][];
        for (final GMTOffsetPatternType t : GMTOffsetPatternType.values()) {
            final int idx = t.ordinal();
            final Object[] parsedItems = parseOffsetPattern(gmtOffsetPatterns[idx], t.required());
            gmtOffsetPatternItems[idx] = parsedItems;
        }
        System.arraycopy(gmtOffsetPatterns, 0, this._gmtOffsetPatterns = new String[size], 0, size);
        this._gmtOffsetPatternItems = gmtOffsetPatternItems;
        this.checkAbuttingHoursAndMinutes();
    }
    
    private void checkAbuttingHoursAndMinutes() {
        this._abuttingOffsetHoursAndMinutes = false;
        for (final Object[] items : this._gmtOffsetPatternItems) {
            boolean afterH = false;
            for (final Object item : items) {
                if (item instanceof GMTOffsetField) {
                    final GMTOffsetField fld = (GMTOffsetField)item;
                    if (afterH) {
                        this._abuttingOffsetHoursAndMinutes = true;
                    }
                    else if (fld.getType() == 'H') {
                        afterH = true;
                    }
                }
                else if (afterH) {
                    break;
                }
            }
        }
    }
    
    private static Object[] parseOffsetPattern(final String pattern, final String letters) {
        boolean isPrevQuote = false;
        boolean inQuote = false;
        final StringBuilder text = new StringBuilder();
        char itemType = '\0';
        int itemLength = 1;
        boolean invalidPattern = false;
        final List<Object> items = new ArrayList<Object>();
        final BitSet checkBits = new BitSet(letters.length());
        for (int i = 0; i < pattern.length(); ++i) {
            final char ch = pattern.charAt(i);
            if (ch == '\'') {
                if (isPrevQuote) {
                    text.append('\'');
                    isPrevQuote = false;
                }
                else {
                    isPrevQuote = true;
                    if (itemType != '\0') {
                        if (!GMTOffsetField.isValid(itemType, itemLength)) {
                            invalidPattern = true;
                            break;
                        }
                        items.add(new GMTOffsetField(itemType, itemLength));
                        itemType = '\0';
                    }
                }
                inQuote = !inQuote;
            }
            else {
                isPrevQuote = false;
                if (inQuote) {
                    text.append(ch);
                }
                else {
                    final int patFieldIdx = letters.indexOf(ch);
                    if (patFieldIdx >= 0) {
                        if (ch == itemType) {
                            ++itemLength;
                        }
                        else {
                            if (itemType == '\0') {
                                if (text.length() > 0) {
                                    items.add(text.toString());
                                    text.setLength(0);
                                }
                            }
                            else {
                                if (!GMTOffsetField.isValid(itemType, itemLength)) {
                                    invalidPattern = true;
                                    break;
                                }
                                items.add(new GMTOffsetField(itemType, itemLength));
                            }
                            itemType = ch;
                            itemLength = 1;
                            checkBits.set(patFieldIdx);
                        }
                    }
                    else {
                        if (itemType != '\0') {
                            if (!GMTOffsetField.isValid(itemType, itemLength)) {
                                invalidPattern = true;
                                break;
                            }
                            items.add(new GMTOffsetField(itemType, itemLength));
                            itemType = '\0';
                        }
                        text.append(ch);
                    }
                }
            }
        }
        if (!invalidPattern) {
            if (itemType == '\0') {
                if (text.length() > 0) {
                    items.add(text.toString());
                    text.setLength(0);
                }
            }
            else if (GMTOffsetField.isValid(itemType, itemLength)) {
                items.add(new GMTOffsetField(itemType, itemLength));
            }
            else {
                invalidPattern = true;
            }
        }
        if (invalidPattern || checkBits.cardinality() != letters.length()) {
            throw new IllegalStateException("Bad localized GMT offset pattern: " + pattern);
        }
        return items.toArray(new Object[items.size()]);
    }
    
    private static String expandOffsetPattern(final String offsetHM) {
        final int idx_mm = offsetHM.indexOf("mm");
        if (idx_mm < 0) {
            throw new RuntimeException("Bad time zone hour pattern data");
        }
        String sep = ":";
        final int idx_H = offsetHM.substring(0, idx_mm).lastIndexOf("H");
        if (idx_H >= 0) {
            sep = offsetHM.substring(idx_H + 1, idx_mm);
        }
        return offsetHM.substring(0, idx_mm + 2) + sep + "ss" + offsetHM.substring(idx_mm + 2);
    }
    
    private static String truncateOffsetPattern(final String offsetHM) {
        final int idx_mm = offsetHM.indexOf("mm");
        if (idx_mm < 0) {
            throw new RuntimeException("Bad time zone hour pattern data");
        }
        final int idx_HH = offsetHM.substring(0, idx_mm).lastIndexOf("HH");
        if (idx_HH >= 0) {
            return offsetHM.substring(0, idx_HH + 2);
        }
        final int idx_H = offsetHM.substring(0, idx_mm).lastIndexOf("H");
        if (idx_H >= 0) {
            return offsetHM.substring(0, idx_H + 1);
        }
        throw new RuntimeException("Bad time zone hour pattern data");
    }
    
    private void appendOffsetDigits(final StringBuilder buf, final int n, final int minDigits) {
        assert n >= 0 && n < 60;
        final int numDigits = (n >= 10) ? 2 : 1;
        for (int i = 0; i < minDigits - numDigits; ++i) {
            buf.append(this._gmtOffsetDigits[0]);
        }
        if (numDigits == 2) {
            buf.append(this._gmtOffsetDigits[n / 10]);
        }
        buf.append(this._gmtOffsetDigits[n % 10]);
    }
    
    private TimeZone getTimeZoneForOffset(final int offset) {
        if (offset == 0) {
            return TimeZone.getTimeZone("Etc/GMT");
        }
        return ZoneMeta.getCustomTimeZone(offset);
    }
    
    private int parseOffsetLocalizedGMT(final String text, final ParsePosition pos, final boolean isShort, final Output<Boolean> hasDigitOffset) {
        final int start = pos.getIndex();
        int offset = 0;
        final int[] parsedLength = { 0 };
        if (hasDigitOffset != null) {
            hasDigitOffset.value = false;
        }
        offset = this.parseOffsetLocalizedGMTPattern(text, start, isShort, parsedLength);
        if (parsedLength[0] > 0) {
            if (hasDigitOffset != null) {
                hasDigitOffset.value = true;
            }
            pos.setIndex(start + parsedLength[0]);
            return offset;
        }
        offset = this.parseOffsetDefaultLocalizedGMT(text, start, parsedLength);
        if (parsedLength[0] > 0) {
            if (hasDigitOffset != null) {
                hasDigitOffset.value = true;
            }
            pos.setIndex(start + parsedLength[0]);
            return offset;
        }
        if (text.regionMatches(true, start, this._gmtZeroFormat, 0, this._gmtZeroFormat.length())) {
            pos.setIndex(start + this._gmtZeroFormat.length());
            return 0;
        }
        for (final String defGMTZero : TimeZoneFormat.ALT_GMT_STRINGS) {
            if (text.regionMatches(true, start, defGMTZero, 0, defGMTZero.length())) {
                pos.setIndex(start + defGMTZero.length());
                return 0;
            }
        }
        pos.setErrorIndex(start);
        return 0;
    }
    
    private int parseOffsetLocalizedGMTPattern(final String text, final int start, final boolean isShort, final int[] parsedLen) {
        int idx = start;
        int offset = 0;
        boolean parsed = false;
        int len = this._gmtPatternPrefix.length();
        if (len <= 0 || text.regionMatches(true, idx, this._gmtPatternPrefix, 0, len)) {
            idx += len;
            final int[] offsetLen = { 0 };
            offset = this.parseOffsetFields(text, idx, false, offsetLen);
            if (offsetLen[0] != 0) {
                idx += offsetLen[0];
                len = this._gmtPatternSuffix.length();
                if (len <= 0 || text.regionMatches(true, idx, this._gmtPatternSuffix, 0, len)) {
                    idx += len;
                    parsed = true;
                }
            }
        }
        parsedLen[0] = (parsed ? (idx - start) : 0);
        return offset;
    }
    
    private int parseOffsetFields(final String text, final int start, final boolean isShort, final int[] parsedLen) {
        int outLen = 0;
        int offset = 0;
        int sign = 1;
        if (parsedLen != null && parsedLen.length >= 1) {
            parsedLen[0] = 0;
        }
        int offsetS;
        int offsetH;
        int offsetM = offsetH = (offsetS = 0);
        final int[] fields = { 0, 0, 0 };
        for (final GMTOffsetPatternType gmtPatType : TimeZoneFormat.PARSE_GMT_OFFSET_TYPES) {
            final Object[] items = this._gmtOffsetPatternItems[gmtPatType.ordinal()];
            assert items != null;
            outLen = this.parseOffsetFieldsWithPattern(text, start, items, false, fields);
            if (outLen > 0) {
                sign = (gmtPatType.isPositive() ? 1 : -1);
                offsetH = fields[0];
                offsetM = fields[1];
                offsetS = fields[2];
                break;
            }
        }
        if (outLen > 0 && this._abuttingOffsetHoursAndMinutes) {
            int tmpLen = 0;
            int tmpSign = 1;
            for (final GMTOffsetPatternType gmtPatType2 : TimeZoneFormat.PARSE_GMT_OFFSET_TYPES) {
                final Object[] items2 = this._gmtOffsetPatternItems[gmtPatType2.ordinal()];
                assert items2 != null;
                tmpLen = this.parseOffsetFieldsWithPattern(text, start, items2, true, fields);
                if (tmpLen > 0) {
                    tmpSign = (gmtPatType2.isPositive() ? 1 : -1);
                    break;
                }
            }
            if (tmpLen > outLen) {
                outLen = tmpLen;
                sign = tmpSign;
                offsetH = fields[0];
                offsetM = fields[1];
                offsetS = fields[2];
            }
        }
        if (parsedLen != null && parsedLen.length >= 1) {
            parsedLen[0] = outLen;
        }
        if (outLen > 0) {
            offset = ((offsetH * 60 + offsetM) * 60 + offsetS) * 1000 * sign;
        }
        return offset;
    }
    
    private int parseOffsetFieldsWithPattern(final String text, final int start, final Object[] patternItems, final boolean forceSingleHourDigit, final int[] fields) {
        assert fields != null && fields.length >= 3;
        final int n = 0;
        final int n2 = 1;
        final int n3 = 2;
        final int n4 = 0;
        fields[n3] = n4;
        fields[n] = (fields[n2] = n4);
        boolean failed = false;
        int offsetS;
        int offsetH;
        int offsetM = offsetH = (offsetS = 0);
        int idx = start;
        final int[] tmpParsedLen = { 0 };
        for (int i = 0; i < patternItems.length; ++i) {
            if (patternItems[i] instanceof String) {
                final String patStr = (String)patternItems[i];
                final int len = patStr.length();
                if (!text.regionMatches(true, idx, patStr, 0, len)) {
                    failed = true;
                    break;
                }
                idx += len;
            }
            else {
                assert patternItems[i] instanceof GMTOffsetField;
                final GMTOffsetField field = (GMTOffsetField)patternItems[i];
                final char fieldType = field.getType();
                if (fieldType == 'H') {
                    final int maxDigits = forceSingleHourDigit ? 1 : 2;
                    offsetH = this.parseOffsetFieldWithLocalizedDigits(text, idx, 1, maxDigits, 0, 23, tmpParsedLen);
                }
                else if (fieldType == 'm') {
                    offsetM = this.parseOffsetFieldWithLocalizedDigits(text, idx, 2, 2, 0, 59, tmpParsedLen);
                }
                else if (fieldType == 's') {
                    offsetS = this.parseOffsetFieldWithLocalizedDigits(text, idx, 2, 2, 0, 59, tmpParsedLen);
                }
                if (tmpParsedLen[0] == 0) {
                    failed = true;
                    break;
                }
                idx += tmpParsedLen[0];
            }
        }
        if (failed) {
            return 0;
        }
        fields[0] = offsetH;
        fields[1] = offsetM;
        fields[2] = offsetS;
        return idx - start;
    }
    
    private int parseOffsetDefaultLocalizedGMT(final String text, final int start, final int[] parsedLen) {
        int idx = start;
        int offset = 0;
        int parsed = 0;
        int gmtLen = 0;
        for (final String gmt : TimeZoneFormat.ALT_GMT_STRINGS) {
            final int len = gmt.length();
            if (text.regionMatches(true, idx, gmt, 0, len)) {
                gmtLen = len;
                break;
            }
        }
        Label_0267: {
            if (gmtLen != 0) {
                idx += gmtLen;
                if (idx + 1 < text.length()) {
                    int sign = 1;
                    final char c = text.charAt(idx);
                    if (c == '+') {
                        sign = 1;
                    }
                    else {
                        if (c != '-') {
                            break Label_0267;
                        }
                        sign = -1;
                    }
                    ++idx;
                    final int[] lenWithSep = { 0 };
                    final int offsetWithSep = this.parseDefaultOffsetFields(text, idx, ':', lenWithSep);
                    if (lenWithSep[0] == text.length() - idx) {
                        offset = offsetWithSep * sign;
                        idx += lenWithSep[0];
                    }
                    else {
                        final int[] lenAbut = { 0 };
                        final int offsetAbut = this.parseAbuttingOffsetFields(text, idx, lenAbut);
                        if (lenWithSep[0] > lenAbut[0]) {
                            offset = offsetWithSep * sign;
                            idx += lenWithSep[0];
                        }
                        else {
                            offset = offsetAbut * sign;
                            idx += lenAbut[0];
                        }
                    }
                    parsed = idx - start;
                }
            }
        }
        parsedLen[0] = parsed;
        return offset;
    }
    
    private int parseDefaultOffsetFields(final String text, final int start, final char separator, final int[] parsedLen) {
        final int max = text.length();
        int idx = start;
        final int[] len = { 0 };
        int hour = 0;
        int min = 0;
        int sec = 0;
        hour = this.parseOffsetFieldWithLocalizedDigits(text, idx, 1, 2, 0, 23, len);
        if (len[0] != 0) {
            idx += len[0];
            if (idx + 1 < max && text.charAt(idx) == separator) {
                min = this.parseOffsetFieldWithLocalizedDigits(text, idx + 1, 2, 2, 0, 59, len);
                if (len[0] != 0) {
                    idx += 1 + len[0];
                    if (idx + 1 < max && text.charAt(idx) == separator) {
                        sec = this.parseOffsetFieldWithLocalizedDigits(text, idx + 1, 2, 2, 0, 59, len);
                        if (len[0] != 0) {
                            idx += 1 + len[0];
                        }
                    }
                }
            }
        }
        if (idx == start) {
            return parsedLen[0] = 0;
        }
        parsedLen[0] = idx - start;
        return hour * 3600000 + min * 60000 + sec * 1000;
    }
    
    private int parseAbuttingOffsetFields(final String text, final int start, final int[] parsedLen) {
        final int MAXDIGITS = 6;
        final int[] digits = new int[6];
        final int[] parsed = new int[6];
        int idx = start;
        final int[] len = { 0 };
        int numDigits = 0;
        for (int i = 0; i < 6; ++i) {
            digits[i] = this.parseSingleLocalizedDigit(text, idx, len);
            if (digits[i] < 0) {
                break;
            }
            idx += len[0];
            parsed[i] = idx - start;
            ++numDigits;
        }
        if (numDigits == 0) {
            return parsedLen[0] = 0;
        }
        int offset = 0;
        while (numDigits > 0) {
            int hour = 0;
            int min = 0;
            int sec = 0;
            assert numDigits > 0 && numDigits <= 6;
            switch (numDigits) {
                case 1: {
                    hour = digits[0];
                    break;
                }
                case 2: {
                    hour = digits[0] * 10 + digits[1];
                    break;
                }
                case 3: {
                    hour = digits[0];
                    min = digits[1] * 10 + digits[2];
                    break;
                }
                case 4: {
                    hour = digits[0] * 10 + digits[1];
                    min = digits[2] * 10 + digits[3];
                    break;
                }
                case 5: {
                    hour = digits[0];
                    min = digits[1] * 10 + digits[2];
                    sec = digits[3] * 10 + digits[4];
                    break;
                }
                case 6: {
                    hour = digits[0] * 10 + digits[1];
                    min = digits[2] * 10 + digits[3];
                    sec = digits[4] * 10 + digits[5];
                    break;
                }
            }
            if (hour <= 23 && min <= 59 && sec <= 59) {
                offset = hour * 3600000 + min * 60000 + sec * 1000;
                parsedLen[0] = parsed[numDigits - 1];
                break;
            }
            --numDigits;
        }
        return offset;
    }
    
    private int parseOffsetFieldWithLocalizedDigits(final String text, final int start, final int minDigits, final int maxDigits, final int minVal, final int maxVal, final int[] parsedLen) {
        parsedLen[0] = 0;
        int decVal = 0;
        int numDigits = 0;
        int idx = start;
        for (int[] digitLen = { 0 }; idx < text.length() && numDigits < maxDigits; ++numDigits, idx += digitLen[0]) {
            final int digit = this.parseSingleLocalizedDigit(text, idx, digitLen);
            if (digit < 0) {
                break;
            }
            final int tmpVal = decVal * 10 + digit;
            if (tmpVal > maxVal) {
                break;
            }
            decVal = tmpVal;
        }
        if (numDigits < minDigits || decVal < minVal) {
            decVal = -1;
            numDigits = 0;
        }
        else {
            parsedLen[0] = idx - start;
        }
        return decVal;
    }
    
    private int parseSingleLocalizedDigit(final String text, final int start, final int[] len) {
        int digit = -1;
        len[0] = 0;
        if (start < text.length()) {
            final int cp = Character.codePointAt(text, start);
            for (int i = 0; i < this._gmtOffsetDigits.length; ++i) {
                if (cp == this._gmtOffsetDigits[i].codePointAt(0)) {
                    digit = i;
                    break;
                }
            }
            if (digit < 0) {
                digit = UCharacter.digit(cp);
            }
            if (digit >= 0) {
                len[0] = Character.charCount(cp);
            }
        }
        return digit;
    }
    
    private static String[] toCodePoints(final String str) {
        final int len = str.codePointCount(0, str.length());
        final String[] codePoints = new String[len];
        int i = 0;
        int offset = 0;
        while (i < len) {
            final int code = str.codePointAt(offset);
            final int codeLen = Character.charCount(code);
            codePoints[i] = str.substring(offset, offset + codeLen);
            offset += codeLen;
            ++i;
        }
        return codePoints;
    }
    
    private static int parseOffsetISO8601(final String text, final ParsePosition pos, final boolean extendedOnly, final Output<Boolean> hasDigitOffset) {
        if (hasDigitOffset != null) {
            hasDigitOffset.value = false;
        }
        final int start = pos.getIndex();
        if (start >= text.length()) {
            pos.setErrorIndex(start);
            return 0;
        }
        final char firstChar = text.charAt(start);
        if (Character.toUpperCase(firstChar) == "Z".charAt(0)) {
            pos.setIndex(start + 1);
            return 0;
        }
        int sign;
        if (firstChar == '+') {
            sign = 1;
        }
        else {
            if (firstChar != '-') {
                pos.setErrorIndex(start);
                return 0;
            }
            sign = -1;
        }
        final ParsePosition posOffset = new ParsePosition(start + 1);
        int offset = parseAsciiOffsetFields(text, posOffset, ':', OffsetFields.H, OffsetFields.HMS);
        if (posOffset.getErrorIndex() == -1 && !extendedOnly && posOffset.getIndex() - start <= 3) {
            final ParsePosition posBasic = new ParsePosition(start + 1);
            final int tmpOffset = parseAbuttingAsciiOffsetFields(text, posBasic, OffsetFields.H, OffsetFields.HMS, false);
            if (posBasic.getErrorIndex() == -1 && posBasic.getIndex() > posOffset.getIndex()) {
                offset = tmpOffset;
                posOffset.setIndex(posBasic.getIndex());
            }
        }
        if (posOffset.getErrorIndex() != -1) {
            pos.setErrorIndex(start);
            return 0;
        }
        pos.setIndex(posOffset.getIndex());
        if (hasDigitOffset != null) {
            hasDigitOffset.value = true;
        }
        return sign * offset;
    }
    
    private static int parseAbuttingAsciiOffsetFields(final String text, final ParsePosition pos, final OffsetFields minFields, final OffsetFields maxFields, final boolean fixedHourWidth) {
        final int start = pos.getIndex();
        final int minDigits = 2 * (minFields.ordinal() + 1) - (fixedHourWidth ? 0 : 1);
        final int maxDigits = 2 * (maxFields.ordinal() + 1);
        final int[] digits = new int[maxDigits];
        int numDigits = 0;
        for (int idx = start; numDigits < digits.length && idx < text.length(); ++numDigits, ++idx) {
            final int digit = "0123456789".indexOf(text.charAt(idx));
            if (digit < 0) {
                break;
            }
            digits[numDigits] = digit;
        }
        if (fixedHourWidth && (numDigits & 0x1) != 0x0) {
            --numDigits;
        }
        if (numDigits < minDigits) {
            pos.setErrorIndex(start);
            return 0;
        }
        int hour = 0;
        int min = 0;
        int sec = 0;
        boolean bParsed = false;
        while (numDigits >= minDigits) {
            switch (numDigits) {
                case 1: {
                    hour = digits[0];
                    break;
                }
                case 2: {
                    hour = digits[0] * 10 + digits[1];
                    break;
                }
                case 3: {
                    hour = digits[0];
                    min = digits[1] * 10 + digits[2];
                    break;
                }
                case 4: {
                    hour = digits[0] * 10 + digits[1];
                    min = digits[2] * 10 + digits[3];
                    break;
                }
                case 5: {
                    hour = digits[0];
                    min = digits[1] * 10 + digits[2];
                    sec = digits[3] * 10 + digits[4];
                    break;
                }
                case 6: {
                    hour = digits[0] * 10 + digits[1];
                    min = digits[2] * 10 + digits[3];
                    sec = digits[4] * 10 + digits[5];
                    break;
                }
            }
            if (hour <= 23 && min <= 59 && sec <= 59) {
                bParsed = true;
                break;
            }
            numDigits -= (fixedHourWidth ? 2 : 1);
            min = (hour = (sec = 0));
        }
        if (!bParsed) {
            pos.setErrorIndex(start);
            return 0;
        }
        pos.setIndex(start + numDigits);
        return ((hour * 60 + min) * 60 + sec) * 1000;
    }
    
    private static int parseAsciiOffsetFields(final String text, final ParsePosition pos, final char sep, final OffsetFields minFields, final OffsetFields maxFields) {
        final int start = pos.getIndex();
        final int[] fieldVal = { 0, 0, 0 };
        final int[] fieldLen = { 0, -1, -1 };
        for (int idx = start, fieldIdx = 0; idx < text.length() && fieldIdx <= maxFields.ordinal(); ++idx) {
            final char c = text.charAt(idx);
            if (c == sep) {
                if (fieldIdx == 0) {
                    if (fieldLen[0] == 0) {
                        break;
                    }
                    ++fieldIdx;
                }
                else {
                    if (fieldLen[fieldIdx] != -1) {
                        break;
                    }
                    fieldLen[fieldIdx] = 0;
                }
            }
            else {
                if (fieldLen[fieldIdx] == -1) {
                    break;
                }
                final int digit = "0123456789".indexOf(c);
                if (digit < 0) {
                    break;
                }
                fieldVal[fieldIdx] = fieldVal[fieldIdx] * 10 + digit;
                final int[] array = fieldLen;
                final int n = fieldIdx;
                ++array[n];
                if (fieldLen[fieldIdx] >= 2) {
                    ++fieldIdx;
                }
            }
        }
        int offset = 0;
        int parsedLen = 0;
        OffsetFields parsedFields = null;
        if (fieldLen[0] != 0) {
            if (fieldVal[0] > 23) {
                offset = fieldVal[0] / 10 * 3600000;
                parsedFields = OffsetFields.H;
                parsedLen = 1;
            }
            else {
                offset = fieldVal[0] * 3600000;
                parsedLen = fieldLen[0];
                parsedFields = OffsetFields.H;
                if (fieldLen[1] == 2) {
                    if (fieldVal[1] <= 59) {
                        offset += fieldVal[1] * 60000;
                        parsedLen += 1 + fieldLen[1];
                        parsedFields = OffsetFields.HM;
                        if (fieldLen[2] == 2) {
                            if (fieldVal[2] <= 59) {
                                offset += fieldVal[2] * 1000;
                                parsedLen += 1 + fieldLen[2];
                                parsedFields = OffsetFields.HMS;
                            }
                        }
                    }
                }
            }
        }
        if (parsedFields == null || parsedFields.ordinal() < minFields.ordinal()) {
            pos.setErrorIndex(start);
            return 0;
        }
        pos.setIndex(start + parsedLen);
        return offset;
    }
    
    private static String parseZoneID(final String text, final ParsePosition pos) {
        String resolvedID = null;
        if (TimeZoneFormat.ZONE_ID_TRIE == null) {
            synchronized (TimeZoneFormat.class) {
                if (TimeZoneFormat.ZONE_ID_TRIE == null) {
                    final TextTrieMap<String> trie = new TextTrieMap<String>(true);
                    final String[] arr$;
                    final String[] ids = arr$ = TimeZone.getAvailableIDs();
                    for (final String id : arr$) {
                        trie.put(id, id);
                    }
                    TimeZoneFormat.ZONE_ID_TRIE = trie;
                }
            }
        }
        final int[] matchLen = { 0 };
        final Iterator<String> itr = TimeZoneFormat.ZONE_ID_TRIE.get(text, pos.getIndex(), matchLen);
        if (itr != null) {
            resolvedID = itr.next();
            pos.setIndex(pos.getIndex() + matchLen[0]);
        }
        else {
            pos.setErrorIndex(pos.getIndex());
        }
        return resolvedID;
    }
    
    private static String parseShortZoneID(final String text, final ParsePosition pos) {
        String resolvedID = null;
        if (TimeZoneFormat.SHORT_ZONE_ID_TRIE == null) {
            synchronized (TimeZoneFormat.class) {
                if (TimeZoneFormat.SHORT_ZONE_ID_TRIE == null) {
                    final TextTrieMap<String> trie = new TextTrieMap<String>(true);
                    final Set<String> canonicalIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
                    for (final String id : canonicalIDs) {
                        final String shortID = ZoneMeta.getShortID(id);
                        if (shortID != null) {
                            trie.put(shortID, id);
                        }
                    }
                    trie.put("unk", "Etc/Unknown");
                    TimeZoneFormat.SHORT_ZONE_ID_TRIE = trie;
                }
            }
        }
        final int[] matchLen = { 0 };
        final Iterator<String> itr = TimeZoneFormat.SHORT_ZONE_ID_TRIE.get(text, pos.getIndex(), matchLen);
        if (itr != null) {
            resolvedID = itr.next();
            pos.setIndex(pos.getIndex() + matchLen[0]);
        }
        else {
            pos.setErrorIndex(pos.getIndex());
        }
        return resolvedID;
    }
    
    private String parseExemplarLocation(final String text, final ParsePosition pos) {
        final int startIdx = pos.getIndex();
        int parsedPos = -1;
        String tzID = null;
        final EnumSet<TimeZoneNames.NameType> nameTypes = EnumSet.of(TimeZoneNames.NameType.EXEMPLAR_LOCATION);
        final Collection<TimeZoneNames.MatchInfo> exemplarMatches = this._tznames.find(text, startIdx, nameTypes);
        if (exemplarMatches != null) {
            TimeZoneNames.MatchInfo exemplarMatch = null;
            for (final TimeZoneNames.MatchInfo match : exemplarMatches) {
                if (startIdx + match.matchLength() > parsedPos) {
                    exemplarMatch = match;
                    parsedPos = startIdx + match.matchLength();
                }
            }
            if (exemplarMatch != null) {
                tzID = this.getTimeZoneID(exemplarMatch.tzID(), exemplarMatch.mzID());
                pos.setIndex(parsedPos);
            }
        }
        if (tzID == null) {
            pos.setErrorIndex(startIdx);
        }
        return tzID;
    }
    
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        final ObjectOutputStream.PutField fields = oos.putFields();
        fields.put("_locale", this._locale);
        fields.put("_tznames", this._tznames);
        fields.put("_gmtPattern", this._gmtPattern);
        fields.put("_gmtOffsetPatterns", this._gmtOffsetPatterns);
        fields.put("_gmtOffsetDigits", this._gmtOffsetDigits);
        fields.put("_gmtZeroFormat", this._gmtZeroFormat);
        fields.put("_parseAllStyles", this._parseAllStyles);
        oos.writeFields();
    }
    
    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        final ObjectInputStream.GetField fields = ois.readFields();
        this._locale = (ULocale)fields.get("_locale", null);
        if (this._locale == null) {
            throw new InvalidObjectException("Missing field: locale");
        }
        this._tznames = (TimeZoneNames)fields.get("_tznames", null);
        if (this._tznames == null) {
            throw new InvalidObjectException("Missing field: tznames");
        }
        this._gmtPattern = (String)fields.get("_gmtPattern", null);
        if (this._gmtPattern == null) {
            throw new InvalidObjectException("Missing field: gmtPattern");
        }
        final String[] tmpGmtOffsetPatterns = (String[])fields.get("_gmtOffsetPatterns", null);
        if (tmpGmtOffsetPatterns == null) {
            throw new InvalidObjectException("Missing field: gmtOffsetPatterns");
        }
        if (tmpGmtOffsetPatterns.length < 4) {
            throw new InvalidObjectException("Incompatible field: gmtOffsetPatterns");
        }
        this._gmtOffsetPatterns = new String[6];
        if (tmpGmtOffsetPatterns.length == 4) {
            for (int i = 0; i < 4; ++i) {
                this._gmtOffsetPatterns[i] = tmpGmtOffsetPatterns[i];
            }
            this._gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_H.ordinal()] = truncateOffsetPattern(this._gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_HM.ordinal()]);
            this._gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_H.ordinal()] = truncateOffsetPattern(this._gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_HM.ordinal()]);
        }
        else {
            this._gmtOffsetPatterns = tmpGmtOffsetPatterns;
        }
        this._gmtOffsetDigits = (String[])fields.get("_gmtOffsetDigits", null);
        if (this._gmtOffsetDigits == null) {
            throw new InvalidObjectException("Missing field: gmtOffsetDigits");
        }
        if (this._gmtOffsetDigits.length != 10) {
            throw new InvalidObjectException("Incompatible field: gmtOffsetDigits");
        }
        this._gmtZeroFormat = (String)fields.get("_gmtZeroFormat", null);
        if (this._gmtZeroFormat == null) {
            throw new InvalidObjectException("Missing field: gmtZeroFormat");
        }
        this._parseAllStyles = fields.get("_parseAllStyles", false);
        if (fields.defaulted("_parseAllStyles")) {
            throw new InvalidObjectException("Missing field: parseAllStyles");
        }
        if (this._tznames instanceof TimeZoneNamesImpl) {
            this._tznames = TimeZoneNames.getInstance(this._locale);
            this._gnames = null;
        }
        else {
            this._gnames = new TimeZoneGenericNames(this._locale, this._tznames);
        }
        this.initGMTPattern(this._gmtPattern);
        this.initGMTOffsetPatterns(this._gmtOffsetPatterns);
    }
    
    public boolean isFrozen() {
        return this._frozen;
    }
    
    public TimeZoneFormat freeze() {
        this._frozen = true;
        return this;
    }
    
    public TimeZoneFormat cloneAsThawed() {
        final TimeZoneFormat copy = (TimeZoneFormat)super.clone();
        copy._frozen = false;
        return copy;
    }
    
    static {
        ALT_GMT_STRINGS = new String[] { "GMT", "UTC", "UT" };
        DEFAULT_GMT_DIGITS = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        PARSE_GMT_OFFSET_TYPES = new GMTOffsetPatternType[] { GMTOffsetPatternType.POSITIVE_HMS, GMTOffsetPatternType.NEGATIVE_HMS, GMTOffsetPatternType.POSITIVE_HM, GMTOffsetPatternType.NEGATIVE_HM, GMTOffsetPatternType.POSITIVE_H, GMTOffsetPatternType.NEGATIVE_H };
        TimeZoneFormat._tzfCache = new TimeZoneFormatCache();
        ALL_SIMPLE_NAME_TYPES = EnumSet.of(TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, TimeZoneNames.NameType.EXEMPLAR_LOCATION);
        ALL_GENERIC_NAME_TYPES = EnumSet.of(TimeZoneGenericNames.GenericNameType.LOCATION, TimeZoneGenericNames.GenericNameType.LONG, TimeZoneGenericNames.GenericNameType.SHORT);
        serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("_locale", ULocale.class), new ObjectStreamField("_tznames", TimeZoneNames.class), new ObjectStreamField("_gmtPattern", String.class), new ObjectStreamField("_gmtOffsetPatterns", String[].class), new ObjectStreamField("_gmtOffsetDigits", String[].class), new ObjectStreamField("_gmtZeroFormat", String.class), new ObjectStreamField("_parseAllStyles", Boolean.TYPE) };
    }
    
    public enum Style
    {
        GENERIC_LOCATION(1), 
        GENERIC_LONG(2), 
        GENERIC_SHORT(4), 
        SPECIFIC_LONG(8), 
        SPECIFIC_SHORT(16), 
        LOCALIZED_GMT(32), 
        LOCALIZED_GMT_SHORT(64), 
        ISO_BASIC_SHORT(128), 
        ISO_BASIC_LOCAL_SHORT(256), 
        ISO_BASIC_FIXED(128), 
        ISO_BASIC_LOCAL_FIXED(256), 
        ISO_BASIC_FULL(128), 
        ISO_BASIC_LOCAL_FULL(256), 
        ISO_EXTENDED_FIXED(128), 
        ISO_EXTENDED_LOCAL_FIXED(256), 
        ISO_EXTENDED_FULL(128), 
        ISO_EXTENDED_LOCAL_FULL(256), 
        ZONE_ID(512), 
        ZONE_ID_SHORT(1024), 
        EXEMPLAR_LOCATION(2048);
        
        final int flag;
        
        private Style(final int flag) {
            this.flag = flag;
        }
    }
    
    public enum GMTOffsetPatternType
    {
        POSITIVE_HM("+H:mm", "Hm", true), 
        POSITIVE_HMS("+H:mm:ss", "Hms", true), 
        NEGATIVE_HM("-H:mm", "Hm", false), 
        NEGATIVE_HMS("-H:mm:ss", "Hms", false), 
        POSITIVE_H("+H", "H", true), 
        NEGATIVE_H("-H", "H", false);
        
        private String _defaultPattern;
        private String _required;
        private boolean _isPositive;
        
        private GMTOffsetPatternType(final String defaultPattern, final String required, final boolean isPositive) {
            this._defaultPattern = defaultPattern;
            this._required = required;
            this._isPositive = isPositive;
        }
        
        private String defaultPattern() {
            return this._defaultPattern;
        }
        
        private String required() {
            return this._required;
        }
        
        private boolean isPositive() {
            return this._isPositive;
        }
    }
    
    public enum TimeType
    {
        UNKNOWN, 
        STANDARD, 
        DAYLIGHT;
    }
    
    public enum ParseOption
    {
        ALL_STYLES;
    }
    
    private enum OffsetFields
    {
        H, 
        HM, 
        HMS;
    }
    
    private static class GMTOffsetField
    {
        final char _type;
        final int _width;
        
        GMTOffsetField(final char type, final int width) {
            this._type = type;
            this._width = width;
        }
        
        char getType() {
            return this._type;
        }
        
        int getWidth() {
            return this._width;
        }
        
        static boolean isValid(final char type, final int width) {
            return width == 1 || width == 2;
        }
    }
    
    private static class TimeZoneFormatCache extends SoftCache<ULocale, TimeZoneFormat, ULocale>
    {
        @Override
        protected TimeZoneFormat createInstance(final ULocale key, final ULocale data) {
            final TimeZoneFormat fmt = new TimeZoneFormat(data);
            fmt.freeze();
            return fmt;
        }
    }
}
