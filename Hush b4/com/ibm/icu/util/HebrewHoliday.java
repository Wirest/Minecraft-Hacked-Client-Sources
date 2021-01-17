// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

public class HebrewHoliday extends Holiday
{
    private static final HebrewCalendar gCalendar;
    public static HebrewHoliday ROSH_HASHANAH;
    public static HebrewHoliday GEDALIAH;
    public static HebrewHoliday YOM_KIPPUR;
    public static HebrewHoliday SUKKOT;
    public static HebrewHoliday HOSHANAH_RABBAH;
    public static HebrewHoliday SHEMINI_ATZERET;
    public static HebrewHoliday SIMCHAT_TORAH;
    public static HebrewHoliday HANUKKAH;
    public static HebrewHoliday TEVET_10;
    public static HebrewHoliday TU_BSHEVAT;
    public static HebrewHoliday ESTHER;
    public static HebrewHoliday PURIM;
    public static HebrewHoliday SHUSHAN_PURIM;
    public static HebrewHoliday PASSOVER;
    public static HebrewHoliday YOM_HASHOAH;
    public static HebrewHoliday YOM_HAZIKARON;
    public static HebrewHoliday YOM_HAATZMAUT;
    public static HebrewHoliday PESACH_SHEINI;
    public static HebrewHoliday LAG_BOMER;
    public static HebrewHoliday YOM_YERUSHALAYIM;
    public static HebrewHoliday SHAVUOT;
    public static HebrewHoliday TAMMUZ_17;
    public static HebrewHoliday TISHA_BAV;
    public static HebrewHoliday SELIHOT;
    
    public HebrewHoliday(final int month, final int date, final String name) {
        this(month, date, 1, name);
    }
    
    public HebrewHoliday(final int month, final int date, final int length, final String name) {
        super(name, new SimpleDateRule(month, date, HebrewHoliday.gCalendar));
    }
    
    static {
        gCalendar = new HebrewCalendar();
        HebrewHoliday.ROSH_HASHANAH = new HebrewHoliday(0, 1, 2, "Rosh Hashanah");
        HebrewHoliday.GEDALIAH = new HebrewHoliday(0, 3, "Fast of Gedaliah");
        HebrewHoliday.YOM_KIPPUR = new HebrewHoliday(0, 10, "Yom Kippur");
        HebrewHoliday.SUKKOT = new HebrewHoliday(0, 15, 6, "Sukkot");
        HebrewHoliday.HOSHANAH_RABBAH = new HebrewHoliday(0, 21, "Hoshanah Rabbah");
        HebrewHoliday.SHEMINI_ATZERET = new HebrewHoliday(0, 22, "Shemini Atzeret");
        HebrewHoliday.SIMCHAT_TORAH = new HebrewHoliday(0, 23, "Simchat Torah");
        HebrewHoliday.HANUKKAH = new HebrewHoliday(2, 25, "Hanukkah");
        HebrewHoliday.TEVET_10 = new HebrewHoliday(3, 10, "Fast of Tevet 10");
        HebrewHoliday.TU_BSHEVAT = new HebrewHoliday(4, 15, "Tu B'Shevat");
        HebrewHoliday.ESTHER = new HebrewHoliday(6, 13, "Fast of Esther");
        HebrewHoliday.PURIM = new HebrewHoliday(6, 14, "Purim");
        HebrewHoliday.SHUSHAN_PURIM = new HebrewHoliday(6, 15, "Shushan Purim");
        HebrewHoliday.PASSOVER = new HebrewHoliday(7, 15, 8, "Passover");
        HebrewHoliday.YOM_HASHOAH = new HebrewHoliday(7, 27, "Yom Hashoah");
        HebrewHoliday.YOM_HAZIKARON = new HebrewHoliday(8, 4, "Yom Hazikaron");
        HebrewHoliday.YOM_HAATZMAUT = new HebrewHoliday(8, 5, "Yom Ha'Atzmaut");
        HebrewHoliday.PESACH_SHEINI = new HebrewHoliday(8, 14, "Pesach Sheini");
        HebrewHoliday.LAG_BOMER = new HebrewHoliday(8, 18, "Lab B'Omer");
        HebrewHoliday.YOM_YERUSHALAYIM = new HebrewHoliday(8, 28, "Yom Yerushalayim");
        HebrewHoliday.SHAVUOT = new HebrewHoliday(9, 6, 2, "Shavuot");
        HebrewHoliday.TAMMUZ_17 = new HebrewHoliday(10, 17, "Fast of Tammuz 17");
        HebrewHoliday.TISHA_BAV = new HebrewHoliday(11, 9, "Fast of Tisha B'Av");
        HebrewHoliday.SELIHOT = new HebrewHoliday(12, 21, "Selihot");
    }
}
