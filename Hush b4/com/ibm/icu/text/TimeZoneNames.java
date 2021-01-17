// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Collections;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.ICUConfig;
import java.util.Collection;
import java.util.EnumSet;
import com.ibm.icu.impl.TimeZoneNamesImpl;
import java.util.Set;
import com.ibm.icu.util.ULocale;
import java.io.Serializable;

public abstract class TimeZoneNames implements Serializable
{
    private static final long serialVersionUID = -9180227029248969153L;
    private static Cache TZNAMES_CACHE;
    private static final Factory TZNAMES_FACTORY;
    private static final String FACTORY_NAME_PROP = "com.ibm.icu.text.TimeZoneNames.Factory.impl";
    private static final String DEFAULT_FACTORY_CLASS = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";
    
    public static TimeZoneNames getInstance(final ULocale locale) {
        final String key = locale.getBaseName();
        return TimeZoneNames.TZNAMES_CACHE.getInstance(key, locale);
    }
    
    public abstract Set<String> getAvailableMetaZoneIDs();
    
    public abstract Set<String> getAvailableMetaZoneIDs(final String p0);
    
    public abstract String getMetaZoneID(final String p0, final long p1);
    
    public abstract String getReferenceZoneID(final String p0, final String p1);
    
    public abstract String getMetaZoneDisplayName(final String p0, final NameType p1);
    
    public final String getDisplayName(final String tzID, final NameType type, final long date) {
        String name = this.getTimeZoneDisplayName(tzID, type);
        if (name == null) {
            final String mzID = this.getMetaZoneID(tzID, date);
            name = this.getMetaZoneDisplayName(mzID, type);
        }
        return name;
    }
    
    public abstract String getTimeZoneDisplayName(final String p0, final NameType p1);
    
    public String getExemplarLocationName(final String tzID) {
        return TimeZoneNamesImpl.getDefaultExemplarLocationName(tzID);
    }
    
    public Collection<MatchInfo> find(final CharSequence text, final int start, final EnumSet<NameType> types) {
        throw new UnsupportedOperationException("The method is not implemented in TimeZoneNames base class.");
    }
    
    protected TimeZoneNames() {
    }
    
    static {
        TimeZoneNames.TZNAMES_CACHE = new Cache();
        Factory factory = null;
        String classname = ICUConfig.get("com.ibm.icu.text.TimeZoneNames.Factory.impl", "com.ibm.icu.impl.TimeZoneNamesFactoryImpl");
        while (true) {
            try {
                factory = (Factory)Class.forName(classname).newInstance();
                break;
            }
            catch (ClassNotFoundException cnfe) {}
            catch (IllegalAccessException iae) {}
            catch (InstantiationException ex) {}
            if (classname.equals("com.ibm.icu.impl.TimeZoneNamesFactoryImpl")) {
                break;
            }
            classname = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";
        }
        if (factory == null) {
            factory = new DefaultTimeZoneNames.FactoryImpl();
        }
        TZNAMES_FACTORY = factory;
    }
    
    public enum NameType
    {
        LONG_GENERIC, 
        LONG_STANDARD, 
        LONG_DAYLIGHT, 
        SHORT_GENERIC, 
        SHORT_STANDARD, 
        SHORT_DAYLIGHT, 
        EXEMPLAR_LOCATION;
    }
    
    public static class MatchInfo
    {
        private NameType _nameType;
        private String _tzID;
        private String _mzID;
        private int _matchLength;
        
        public MatchInfo(final NameType nameType, final String tzID, final String mzID, final int matchLength) {
            if (nameType == null) {
                throw new IllegalArgumentException("nameType is null");
            }
            if (tzID == null && mzID == null) {
                throw new IllegalArgumentException("Either tzID or mzID must be available");
            }
            if (matchLength <= 0) {
                throw new IllegalArgumentException("matchLength must be positive value");
            }
            this._nameType = nameType;
            this._tzID = tzID;
            this._mzID = mzID;
            this._matchLength = matchLength;
        }
        
        public String tzID() {
            return this._tzID;
        }
        
        public String mzID() {
            return this._mzID;
        }
        
        public NameType nameType() {
            return this._nameType;
        }
        
        public int matchLength() {
            return this._matchLength;
        }
    }
    
    public abstract static class Factory
    {
        public abstract TimeZoneNames getTimeZoneNames(final ULocale p0);
    }
    
    private static class Cache extends SoftCache<String, TimeZoneNames, ULocale>
    {
        @Override
        protected TimeZoneNames createInstance(final String key, final ULocale data) {
            return TimeZoneNames.TZNAMES_FACTORY.getTimeZoneNames(data);
        }
    }
    
    private static class DefaultTimeZoneNames extends TimeZoneNames
    {
        private static final long serialVersionUID = -995672072494349071L;
        public static final DefaultTimeZoneNames INSTANCE;
        
        @Override
        public Set<String> getAvailableMetaZoneIDs() {
            return Collections.emptySet();
        }
        
        @Override
        public Set<String> getAvailableMetaZoneIDs(final String tzID) {
            return Collections.emptySet();
        }
        
        @Override
        public String getMetaZoneID(final String tzID, final long date) {
            return null;
        }
        
        @Override
        public String getReferenceZoneID(final String mzID, final String region) {
            return null;
        }
        
        @Override
        public String getMetaZoneDisplayName(final String mzID, final NameType type) {
            return null;
        }
        
        @Override
        public String getTimeZoneDisplayName(final String tzID, final NameType type) {
            return null;
        }
        
        @Override
        public Collection<MatchInfo> find(final CharSequence text, final int start, final EnumSet<NameType> nameTypes) {
            return (Collection<MatchInfo>)Collections.emptyList();
        }
        
        static {
            INSTANCE = new DefaultTimeZoneNames();
        }
        
        public static class FactoryImpl extends Factory
        {
            @Override
            public TimeZoneNames getTimeZoneNames(final ULocale locale) {
                return DefaultTimeZoneNames.INSTANCE;
            }
        }
    }
}
