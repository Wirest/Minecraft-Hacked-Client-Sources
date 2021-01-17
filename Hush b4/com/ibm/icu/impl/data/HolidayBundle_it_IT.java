// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.SimpleHoliday;
import com.ibm.icu.util.Holiday;
import java.util.ListResourceBundle;

public class HolidayBundle_it_IT extends ListResourceBundle
{
    private static final Holiday[] fHolidays;
    private static final Object[][] fContents;
    
    public synchronized Object[][] getContents() {
        return HolidayBundle_it_IT.fContents;
    }
    
    static {
        fHolidays = new Holiday[] { SimpleHoliday.NEW_YEARS_DAY, SimpleHoliday.EPIPHANY, new SimpleHoliday(3, 1, 0, "Liberation Day"), new SimpleHoliday(4, 1, 0, "Labor Day"), SimpleHoliday.ASSUMPTION, SimpleHoliday.ALL_SAINTS_DAY, SimpleHoliday.IMMACULATE_CONCEPTION, SimpleHoliday.CHRISTMAS, new SimpleHoliday(11, 26, 0, "St. Stephens Day"), SimpleHoliday.NEW_YEARS_EVE, EasterHoliday.EASTER_SUNDAY, EasterHoliday.EASTER_MONDAY };
        fContents = new Object[][] { { "holidays", HolidayBundle_it_IT.fHolidays } };
    }
}
