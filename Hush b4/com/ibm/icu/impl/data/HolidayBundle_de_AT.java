// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.SimpleHoliday;
import com.ibm.icu.util.Holiday;
import java.util.ListResourceBundle;

public class HolidayBundle_de_AT extends ListResourceBundle
{
    private static final Holiday[] fHolidays;
    private static final Object[][] fContents;
    
    public synchronized Object[][] getContents() {
        return HolidayBundle_de_AT.fContents;
    }
    
    static {
        fHolidays = new Holiday[] { SimpleHoliday.NEW_YEARS_DAY, SimpleHoliday.EPIPHANY, EasterHoliday.GOOD_FRIDAY, EasterHoliday.EASTER_SUNDAY, EasterHoliday.EASTER_MONDAY, EasterHoliday.ASCENSION, EasterHoliday.WHIT_SUNDAY, EasterHoliday.WHIT_MONDAY, EasterHoliday.CORPUS_CHRISTI, SimpleHoliday.ASSUMPTION, SimpleHoliday.ALL_SAINTS_DAY, SimpleHoliday.IMMACULATE_CONCEPTION, SimpleHoliday.CHRISTMAS, SimpleHoliday.ST_STEPHENS_DAY, new SimpleHoliday(4, 1, 0, "National Holiday"), new SimpleHoliday(9, 31, -2, "National Holiday") };
        fContents = new Object[][] { { "holidays", HolidayBundle_de_AT.fHolidays }, { "Christmas", "Christtag" }, { "New Year's Day", "Neujahrstag" } };
    }
}
