// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.text.TimeZoneNames;

public class TimeZoneNamesFactoryImpl extends TimeZoneNames.Factory
{
    @Override
    public TimeZoneNames getTimeZoneNames(final ULocale locale) {
        return new TimeZoneNamesImpl(locale);
    }
}
