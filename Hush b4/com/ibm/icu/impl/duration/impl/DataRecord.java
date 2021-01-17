// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration.impl;

import java.util.List;
import java.util.ArrayList;

public class DataRecord
{
    byte pl;
    String[][] pluralNames;
    byte[] genders;
    String[] singularNames;
    String[] halfNames;
    String[] numberNames;
    String[] mediumNames;
    String[] shortNames;
    String[] measures;
    String[] rqdSuffixes;
    String[] optSuffixes;
    String[] halves;
    byte[] halfPlacements;
    byte[] halfSupport;
    String fifteenMinutes;
    String fiveMinutes;
    boolean requiresDigitSeparator;
    String digitPrefix;
    String countSep;
    String shortUnitSep;
    String[] unitSep;
    boolean[] unitSepRequiresDP;
    boolean[] requiresSkipMarker;
    byte numberSystem;
    char zero;
    char decimalSep;
    boolean omitSingularCount;
    boolean omitDualCount;
    byte zeroHandling;
    byte decimalHandling;
    byte fractionHandling;
    String skippedUnitMarker;
    boolean allowZero;
    boolean weeksAloneOnly;
    byte useMilliseconds;
    ScopeData[] scopeData;
    
    public static DataRecord read(final String ln, final RecordReader in) {
        if (!in.open("DataRecord")) {
            throw new InternalError("did not find DataRecord while reading " + ln);
        }
        final DataRecord record = new DataRecord();
        record.pl = in.namedIndex("pl", EPluralization.names);
        record.pluralNames = in.stringTable("pluralName");
        record.genders = in.namedIndexArray("gender", EGender.names);
        record.singularNames = in.stringArray("singularName");
        record.halfNames = in.stringArray("halfName");
        record.numberNames = in.stringArray("numberName");
        record.mediumNames = in.stringArray("mediumName");
        record.shortNames = in.stringArray("shortName");
        record.measures = in.stringArray("measure");
        record.rqdSuffixes = in.stringArray("rqdSuffix");
        record.optSuffixes = in.stringArray("optSuffix");
        record.halves = in.stringArray("halves");
        record.halfPlacements = in.namedIndexArray("halfPlacement", EHalfPlacement.names);
        record.halfSupport = in.namedIndexArray("halfSupport", EHalfSupport.names);
        record.fifteenMinutes = in.string("fifteenMinutes");
        record.fiveMinutes = in.string("fiveMinutes");
        record.requiresDigitSeparator = in.bool("requiresDigitSeparator");
        record.digitPrefix = in.string("digitPrefix");
        record.countSep = in.string("countSep");
        record.shortUnitSep = in.string("shortUnitSep");
        record.unitSep = in.stringArray("unitSep");
        record.unitSepRequiresDP = in.boolArray("unitSepRequiresDP");
        record.requiresSkipMarker = in.boolArray("requiresSkipMarker");
        record.numberSystem = in.namedIndex("numberSystem", ENumberSystem.names);
        record.zero = in.character("zero");
        record.decimalSep = in.character("decimalSep");
        record.omitSingularCount = in.bool("omitSingularCount");
        record.omitDualCount = in.bool("omitDualCount");
        record.zeroHandling = in.namedIndex("zeroHandling", EZeroHandling.names);
        record.decimalHandling = in.namedIndex("decimalHandling", EDecimalHandling.names);
        record.fractionHandling = in.namedIndex("fractionHandling", EFractionHandling.names);
        record.skippedUnitMarker = in.string("skippedUnitMarker");
        record.allowZero = in.bool("allowZero");
        record.weeksAloneOnly = in.bool("weeksAloneOnly");
        record.useMilliseconds = in.namedIndex("useMilliseconds", EMilliSupport.names);
        if (in.open("ScopeDataList")) {
            final List<ScopeData> list = new ArrayList<ScopeData>();
            ScopeData data;
            while (null != (data = ScopeData.read(in))) {
                list.add(data);
            }
            if (in.close()) {
                record.scopeData = list.toArray(new ScopeData[list.size()]);
            }
        }
        if (in.close()) {
            return record;
        }
        throw new InternalError("null data read while reading " + ln);
    }
    
    public void write(final RecordWriter out) {
        out.open("DataRecord");
        out.namedIndex("pl", EPluralization.names, this.pl);
        out.stringTable("pluralName", this.pluralNames);
        out.namedIndexArray("gender", EGender.names, this.genders);
        out.stringArray("singularName", this.singularNames);
        out.stringArray("halfName", this.halfNames);
        out.stringArray("numberName", this.numberNames);
        out.stringArray("mediumName", this.mediumNames);
        out.stringArray("shortName", this.shortNames);
        out.stringArray("measure", this.measures);
        out.stringArray("rqdSuffix", this.rqdSuffixes);
        out.stringArray("optSuffix", this.optSuffixes);
        out.stringArray("halves", this.halves);
        out.namedIndexArray("halfPlacement", EHalfPlacement.names, this.halfPlacements);
        out.namedIndexArray("halfSupport", EHalfSupport.names, this.halfSupport);
        out.string("fifteenMinutes", this.fifteenMinutes);
        out.string("fiveMinutes", this.fiveMinutes);
        out.bool("requiresDigitSeparator", this.requiresDigitSeparator);
        out.string("digitPrefix", this.digitPrefix);
        out.string("countSep", this.countSep);
        out.string("shortUnitSep", this.shortUnitSep);
        out.stringArray("unitSep", this.unitSep);
        out.boolArray("unitSepRequiresDP", this.unitSepRequiresDP);
        out.boolArray("requiresSkipMarker", this.requiresSkipMarker);
        out.namedIndex("numberSystem", ENumberSystem.names, this.numberSystem);
        out.character("zero", this.zero);
        out.character("decimalSep", this.decimalSep);
        out.bool("omitSingularCount", this.omitSingularCount);
        out.bool("omitDualCount", this.omitDualCount);
        out.namedIndex("zeroHandling", EZeroHandling.names, this.zeroHandling);
        out.namedIndex("decimalHandling", EDecimalHandling.names, this.decimalHandling);
        out.namedIndex("fractionHandling", EFractionHandling.names, this.fractionHandling);
        out.string("skippedUnitMarker", this.skippedUnitMarker);
        out.bool("allowZero", this.allowZero);
        out.bool("weeksAloneOnly", this.weeksAloneOnly);
        out.namedIndex("useMilliseconds", EMilliSupport.names, this.useMilliseconds);
        if (this.scopeData != null) {
            out.open("ScopeDataList");
            for (int i = 0; i < this.scopeData.length; ++i) {
                this.scopeData[i].write(out);
            }
            out.close();
        }
        out.close();
    }
    
    public static class ScopeData
    {
        String prefix;
        boolean requiresDigitPrefix;
        String suffix;
        
        public void write(final RecordWriter out) {
            out.open("ScopeData");
            out.string("prefix", this.prefix);
            out.bool("requiresDigitPrefix", this.requiresDigitPrefix);
            out.string("suffix", this.suffix);
            out.close();
        }
        
        public static ScopeData read(final RecordReader in) {
            if (in.open("ScopeData")) {
                final ScopeData scope = new ScopeData();
                scope.prefix = in.string("prefix");
                scope.requiresDigitPrefix = in.bool("requiresDigitPrefix");
                scope.suffix = in.string("suffix");
                if (in.close()) {
                    return scope;
                }
            }
            return null;
        }
    }
    
    public interface ETimeLimit
    {
        public static final byte NOLIMIT = 0;
        public static final byte LT = 1;
        public static final byte MT = 2;
        public static final String[] names = { "NOLIMIT", "LT", "MT" };
    }
    
    public interface ETimeDirection
    {
        public static final byte NODIRECTION = 0;
        public static final byte PAST = 1;
        public static final byte FUTURE = 2;
        public static final String[] names = { "NODIRECTION", "PAST", "FUTURE" };
    }
    
    public interface EUnitVariant
    {
        public static final byte PLURALIZED = 0;
        public static final byte MEDIUM = 1;
        public static final byte SHORT = 2;
        public static final String[] names = { "PLURALIZED", "MEDIUM", "SHORT" };
    }
    
    public interface ECountVariant
    {
        public static final byte INTEGER = 0;
        public static final byte INTEGER_CUSTOM = 1;
        public static final byte HALF_FRACTION = 2;
        public static final byte DECIMAL1 = 3;
        public static final byte DECIMAL2 = 4;
        public static final byte DECIMAL3 = 5;
        public static final String[] names = { "INTEGER", "INTEGER_CUSTOM", "HALF_FRACTION", "DECIMAL1", "DECIMAL2", "DECIMAL3" };
    }
    
    public interface EPluralization
    {
        public static final byte NONE = 0;
        public static final byte PLURAL = 1;
        public static final byte DUAL = 2;
        public static final byte PAUCAL = 3;
        public static final byte HEBREW = 4;
        public static final byte ARABIC = 5;
        public static final String[] names = { "NONE", "PLURAL", "DUAL", "PAUCAL", "HEBREW", "ARABIC" };
    }
    
    public interface EHalfPlacement
    {
        public static final byte PREFIX = 0;
        public static final byte AFTER_FIRST = 1;
        public static final byte LAST = 2;
        public static final String[] names = { "PREFIX", "AFTER_FIRST", "LAST" };
    }
    
    public interface ENumberSystem
    {
        public static final byte DEFAULT = 0;
        public static final byte CHINESE_TRADITIONAL = 1;
        public static final byte CHINESE_SIMPLIFIED = 2;
        public static final byte KOREAN = 3;
        public static final String[] names = { "DEFAULT", "CHINESE_TRADITIONAL", "CHINESE_SIMPLIFIED", "KOREAN" };
    }
    
    public interface EZeroHandling
    {
        public static final byte ZPLURAL = 0;
        public static final byte ZSINGULAR = 1;
        public static final String[] names = { "ZPLURAL", "ZSINGULAR" };
    }
    
    public interface EDecimalHandling
    {
        public static final byte DPLURAL = 0;
        public static final byte DSINGULAR = 1;
        public static final byte DSINGULAR_SUBONE = 2;
        public static final byte DPAUCAL = 3;
        public static final String[] names = { "DPLURAL", "DSINGULAR", "DSINGULAR_SUBONE", "DPAUCAL" };
    }
    
    public interface EFractionHandling
    {
        public static final byte FPLURAL = 0;
        public static final byte FSINGULAR_PLURAL = 1;
        public static final byte FSINGULAR_PLURAL_ANDAHALF = 2;
        public static final byte FPAUCAL = 3;
        public static final String[] names = { "FPLURAL", "FSINGULAR_PLURAL", "FSINGULAR_PLURAL_ANDAHALF", "FPAUCAL" };
    }
    
    public interface EHalfSupport
    {
        public static final byte YES = 0;
        public static final byte NO = 1;
        public static final byte ONE_PLUS = 2;
        public static final String[] names = { "YES", "NO", "ONE_PLUS" };
    }
    
    public interface EMilliSupport
    {
        public static final byte YES = 0;
        public static final byte NO = 1;
        public static final byte WITH_SECONDS = 2;
        public static final String[] names = { "YES", "NO", "WITH_SECONDS" };
    }
    
    public interface ESeparatorVariant
    {
        public static final byte NONE = 0;
        public static final byte SHORT = 1;
        public static final byte FULL = 2;
        public static final String[] names = { "NONE", "SHORT", "FULL" };
    }
    
    public interface EGender
    {
        public static final byte M = 0;
        public static final byte F = 1;
        public static final byte N = 2;
        public static final String[] names = { "M", "F", "N" };
    }
}
