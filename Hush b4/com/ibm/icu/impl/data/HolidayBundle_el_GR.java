// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.SimpleHoliday;
import com.ibm.icu.util.Holiday;
import java.util.ListResourceBundle;

public class HolidayBundle_el_GR extends ListResourceBundle
{
    private static final Holiday[] fHolidays;
    private static final Object[][] fContents;
    
    public synchronized Object[][] getContents() {
        return HolidayBundle_el_GR.fContents;
    }
    
    static {
        fHolidays = new Holiday[] { SimpleHoliday.NEW_YEARS_DAY, SimpleHoliday.EPIPHANY, new SimpleHoliday(2, 25, 0, "Independence Day"), SimpleHoliday.MAY_DAY, SimpleHoliday.ASSUMPTION, new SimpleHoliday(9, 28, 0, "Ochi Day"), SimpleHoliday.CHRISTMAS, SimpleHoliday.BOXING_DAY, new EasterHoliday(-2, true, "Good Friday"), new EasterHoliday(0, true, "Easter Sunday"), new EasterHoliday(1, true, "Easter Monday"), new EasterHoliday(50, true, "Whit Monday") };
        fContents = new Object[][] { { "holidays", HolidayBundle_el_GR.fHolidays } };
    }
}
