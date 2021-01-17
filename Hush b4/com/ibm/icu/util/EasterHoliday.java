// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

public class EasterHoliday extends Holiday
{
    public static final EasterHoliday SHROVE_TUESDAY;
    public static final EasterHoliday ASH_WEDNESDAY;
    public static final EasterHoliday PALM_SUNDAY;
    public static final EasterHoliday MAUNDY_THURSDAY;
    public static final EasterHoliday GOOD_FRIDAY;
    public static final EasterHoliday EASTER_SUNDAY;
    public static final EasterHoliday EASTER_MONDAY;
    public static final EasterHoliday ASCENSION;
    public static final EasterHoliday PENTECOST;
    public static final EasterHoliday WHIT_SUNDAY;
    public static final EasterHoliday WHIT_MONDAY;
    public static final EasterHoliday CORPUS_CHRISTI;
    
    public EasterHoliday(final String name) {
        super(name, new EasterRule(0, false));
    }
    
    public EasterHoliday(final int daysAfter, final String name) {
        super(name, new EasterRule(daysAfter, false));
    }
    
    public EasterHoliday(final int daysAfter, final boolean orthodox, final String name) {
        super(name, new EasterRule(daysAfter, orthodox));
    }
    
    static {
        SHROVE_TUESDAY = new EasterHoliday(-48, "Shrove Tuesday");
        ASH_WEDNESDAY = new EasterHoliday(-47, "Ash Wednesday");
        PALM_SUNDAY = new EasterHoliday(-7, "Palm Sunday");
        MAUNDY_THURSDAY = new EasterHoliday(-3, "Maundy Thursday");
        GOOD_FRIDAY = new EasterHoliday(-2, "Good Friday");
        EASTER_SUNDAY = new EasterHoliday(0, "Easter Sunday");
        EASTER_MONDAY = new EasterHoliday(1, "Easter Monday");
        ASCENSION = new EasterHoliday(39, "Ascension");
        PENTECOST = new EasterHoliday(49, "Pentecost");
        WHIT_SUNDAY = new EasterHoliday(49, "Whit Sunday");
        WHIT_MONDAY = new EasterHoliday(50, "Whit Monday");
        CORPUS_CHRISTI = new EasterHoliday(60, "Corpus Christi");
    }
}
