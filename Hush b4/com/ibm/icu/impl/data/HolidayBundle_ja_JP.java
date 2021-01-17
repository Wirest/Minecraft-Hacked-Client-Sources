// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.data;

import com.ibm.icu.util.SimpleHoliday;
import com.ibm.icu.util.Holiday;
import java.util.ListResourceBundle;

public class HolidayBundle_ja_JP extends ListResourceBundle
{
    private static final Holiday[] fHolidays;
    private static final Object[][] fContents;
    
    public synchronized Object[][] getContents() {
        return HolidayBundle_ja_JP.fContents;
    }
    
    static {
        fHolidays = new Holiday[] { new SimpleHoliday(1, 11, 0, "National Foundation Day") };
        fContents = new Object[][] { { "holidays", HolidayBundle_ja_JP.fHolidays } };
    }
}
