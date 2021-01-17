// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.data;

import com.ibm.icu.util.SimpleHoliday;
import com.ibm.icu.util.Holiday;
import java.util.ListResourceBundle;

public class HolidayBundle_en_CA extends ListResourceBundle
{
    private static final Holiday[] fHolidays;
    private static final Object[][] fContents;
    
    public synchronized Object[][] getContents() {
        return HolidayBundle_en_CA.fContents;
    }
    
    static {
        fHolidays = new Holiday[] { SimpleHoliday.NEW_YEARS_DAY, new SimpleHoliday(4, 19, 0, "Victoria Day"), new SimpleHoliday(6, 1, 0, "Canada Day"), new SimpleHoliday(7, 1, 2, "Civic Holiday"), new SimpleHoliday(8, 1, 2, "Labor Day"), new SimpleHoliday(9, 8, 2, "Thanksgiving"), new SimpleHoliday(10, 11, 0, "Remembrance Day"), SimpleHoliday.CHRISTMAS, SimpleHoliday.BOXING_DAY, SimpleHoliday.NEW_YEARS_EVE };
        fContents = new Object[][] { { "holidays", HolidayBundle_en_CA.fHolidays }, { "Labor Day", "Labour Day" } };
    }
}
