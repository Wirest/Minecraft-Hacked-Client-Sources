// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.SimpleHoliday;
import com.ibm.icu.util.Holiday;
import java.util.ListResourceBundle;

public class HolidayBundle_fr_FR extends ListResourceBundle
{
    private static final Holiday[] fHolidays;
    private static final Object[][] fContents;
    
    public synchronized Object[][] getContents() {
        return HolidayBundle_fr_FR.fContents;
    }
    
    static {
        fHolidays = new Holiday[] { SimpleHoliday.NEW_YEARS_DAY, new SimpleHoliday(4, 1, 0, "Labor Day"), new SimpleHoliday(4, 8, 0, "Victory Day"), new SimpleHoliday(6, 14, 0, "Bastille Day"), SimpleHoliday.ASSUMPTION, SimpleHoliday.ALL_SAINTS_DAY, new SimpleHoliday(10, 11, 0, "Armistice Day"), SimpleHoliday.CHRISTMAS, EasterHoliday.EASTER_SUNDAY, EasterHoliday.EASTER_MONDAY, EasterHoliday.ASCENSION, EasterHoliday.WHIT_SUNDAY, EasterHoliday.WHIT_MONDAY };
        fContents = new Object[][] { { "holidays", HolidayBundle_fr_FR.fHolidays } };
    }
}
