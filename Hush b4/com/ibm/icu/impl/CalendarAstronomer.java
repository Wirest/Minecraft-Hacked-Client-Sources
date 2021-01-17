// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.TimeZone;
import java.util.Date;

public class CalendarAstronomer
{
    public static final double SIDEREAL_DAY = 23.93446960027;
    public static final double SOLAR_DAY = 24.065709816;
    public static final double SYNODIC_MONTH = 29.530588853;
    public static final double SIDEREAL_MONTH = 27.32166;
    public static final double TROPICAL_YEAR = 365.242191;
    public static final double SIDEREAL_YEAR = 365.25636;
    public static final int SECOND_MS = 1000;
    public static final int MINUTE_MS = 60000;
    public static final int HOUR_MS = 3600000;
    public static final long DAY_MS = 86400000L;
    public static final long JULIAN_EPOCH_MS = -210866760000000L;
    static final long EPOCH_2000_MS = 946598400000L;
    private static final double PI = 3.141592653589793;
    private static final double PI2 = 6.283185307179586;
    private static final double RAD_HOUR = 3.819718634205488;
    private static final double DEG_RAD = 0.017453292519943295;
    private static final double RAD_DEG = 57.29577951308232;
    static final double JD_EPOCH = 2447891.5;
    static final double SUN_ETA_G = 4.87650757829735;
    static final double SUN_OMEGA_G = 4.935239984568769;
    static final double SUN_E = 0.016713;
    public static final SolarLongitude VERNAL_EQUINOX;
    public static final SolarLongitude SUMMER_SOLSTICE;
    public static final SolarLongitude AUTUMN_EQUINOX;
    public static final SolarLongitude WINTER_SOLSTICE;
    static final double moonL0 = 5.556284436750021;
    static final double moonP0 = 0.6342598060246725;
    static final double moonN0 = 5.559050068029439;
    static final double moonI = 0.08980357792017056;
    static final double moonE = 0.0549;
    static final double moonA = 384401.0;
    static final double moonT0 = 0.009042550854582622;
    static final double moonPi = 0.016592845198710092;
    public static final MoonAge NEW_MOON;
    public static final MoonAge FIRST_QUARTER;
    public static final MoonAge FULL_MOON;
    public static final MoonAge LAST_QUARTER;
    private long time;
    private double fLongitude;
    private double fLatitude;
    private long fGmtOffset;
    private static final double INVALID = Double.MIN_VALUE;
    private transient double julianDay;
    private transient double julianCentury;
    private transient double sunLongitude;
    private transient double meanAnomalySun;
    private transient double moonLongitude;
    private transient double moonEclipLong;
    private transient double eclipObliquity;
    private transient double siderealT0;
    private transient double siderealTime;
    private transient Equatorial moonPosition;
    
    public CalendarAstronomer() {
        this(System.currentTimeMillis());
    }
    
    public CalendarAstronomer(final Date d) {
        this(d.getTime());
    }
    
    public CalendarAstronomer(final long aTime) {
        this.fLongitude = 0.0;
        this.fLatitude = 0.0;
        this.fGmtOffset = 0L;
        this.julianDay = Double.MIN_VALUE;
        this.julianCentury = Double.MIN_VALUE;
        this.sunLongitude = Double.MIN_VALUE;
        this.meanAnomalySun = Double.MIN_VALUE;
        this.moonLongitude = Double.MIN_VALUE;
        this.moonEclipLong = Double.MIN_VALUE;
        this.eclipObliquity = Double.MIN_VALUE;
        this.siderealT0 = Double.MIN_VALUE;
        this.siderealTime = Double.MIN_VALUE;
        this.moonPosition = null;
        this.time = aTime;
    }
    
    public CalendarAstronomer(final double longitude, final double latitude) {
        this();
        this.fLongitude = normPI(longitude * 0.017453292519943295);
        this.fLatitude = normPI(latitude * 0.017453292519943295);
        this.fGmtOffset = (long)(this.fLongitude * 24.0 * 3600000.0 / 6.283185307179586);
    }
    
    public void setTime(final long aTime) {
        this.time = aTime;
        this.clearCache();
    }
    
    public void setDate(final Date date) {
        this.setTime(date.getTime());
    }
    
    public void setJulianDay(final double jdn) {
        this.time = (long)(jdn * 8.64E7) - 210866760000000L;
        this.clearCache();
        this.julianDay = jdn;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public Date getDate() {
        return new Date(this.time);
    }
    
    public double getJulianDay() {
        if (this.julianDay == Double.MIN_VALUE) {
            this.julianDay = (this.time + 210866760000000L) / 8.64E7;
        }
        return this.julianDay;
    }
    
    public double getJulianCentury() {
        if (this.julianCentury == Double.MIN_VALUE) {
            this.julianCentury = (this.getJulianDay() - 2415020.0) / 36525.0;
        }
        return this.julianCentury;
    }
    
    public double getGreenwichSidereal() {
        if (this.siderealTime == Double.MIN_VALUE) {
            final double UT = normalize(this.time / 3600000.0, 24.0);
            this.siderealTime = normalize(this.getSiderealOffset() + UT * 1.002737909, 24.0);
        }
        return this.siderealTime;
    }
    
    private double getSiderealOffset() {
        if (this.siderealT0 == Double.MIN_VALUE) {
            final double JD = Math.floor(this.getJulianDay() - 0.5) + 0.5;
            final double S = JD - 2451545.0;
            final double T = S / 36525.0;
            this.siderealT0 = normalize(6.697374558 + 2400.051336 * T + 2.5862E-5 * T * T, 24.0);
        }
        return this.siderealT0;
    }
    
    public double getLocalSidereal() {
        return normalize(this.getGreenwichSidereal() + this.fGmtOffset / 3600000.0, 24.0);
    }
    
    private long lstToUT(final double lst) {
        final double lt = normalize((lst - this.getSiderealOffset()) * 0.9972695663, 24.0);
        final long base = 86400000L * ((this.time + this.fGmtOffset) / 86400000L) - this.fGmtOffset;
        return base + (long)(lt * 3600000.0);
    }
    
    public final Equatorial eclipticToEquatorial(final Ecliptic ecliptic) {
        return this.eclipticToEquatorial(ecliptic.longitude, ecliptic.latitude);
    }
    
    public final Equatorial eclipticToEquatorial(final double eclipLong, final double eclipLat) {
        final double obliq = this.eclipticObliquity();
        final double sinE = Math.sin(obliq);
        final double cosE = Math.cos(obliq);
        final double sinL = Math.sin(eclipLong);
        final double cosL = Math.cos(eclipLong);
        final double sinB = Math.sin(eclipLat);
        final double cosB = Math.cos(eclipLat);
        final double tanB = Math.tan(eclipLat);
        return new Equatorial(Math.atan2(sinL * cosE - tanB * sinE, cosL), Math.asin(sinB * cosE + cosB * sinE * sinL));
    }
    
    public final Equatorial eclipticToEquatorial(final double eclipLong) {
        return this.eclipticToEquatorial(eclipLong, 0.0);
    }
    
    public Horizon eclipticToHorizon(final double eclipLong) {
        final Equatorial equatorial = this.eclipticToEquatorial(eclipLong);
        final double H = this.getLocalSidereal() * 3.141592653589793 / 12.0 - equatorial.ascension;
        final double sinH = Math.sin(H);
        final double cosH = Math.cos(H);
        final double sinD = Math.sin(equatorial.declination);
        final double cosD = Math.cos(equatorial.declination);
        final double sinL = Math.sin(this.fLatitude);
        final double cosL = Math.cos(this.fLatitude);
        final double altitude = Math.asin(sinD * sinL + cosD * cosL * cosH);
        final double azimuth = Math.atan2(-cosD * cosL * sinH, sinD - sinL * Math.sin(altitude));
        return new Horizon(azimuth, altitude);
    }
    
    public double getSunLongitude() {
        if (this.sunLongitude == Double.MIN_VALUE) {
            final double[] result = this.getSunLongitude(this.getJulianDay());
            this.sunLongitude = result[0];
            this.meanAnomalySun = result[1];
        }
        return this.sunLongitude;
    }
    
    double[] getSunLongitude(final double julian) {
        final double day = julian - 2447891.5;
        final double epochAngle = norm2PI(0.017202791632524146 * day);
        final double meanAnomaly = norm2PI(epochAngle + 4.87650757829735 - 4.935239984568769);
        return new double[] { norm2PI(this.trueAnomaly(meanAnomaly, 0.016713) + 4.935239984568769), meanAnomaly };
    }
    
    public Equatorial getSunPosition() {
        return this.eclipticToEquatorial(this.getSunLongitude(), 0.0);
    }
    
    public long getSunTime(final double desired, final boolean next) {
        return this.timeOfAngle(new AngleFunc() {
            public double eval() {
                return CalendarAstronomer.this.getSunLongitude();
            }
        }, desired, 365.242191, 60000L, next);
    }
    
    public long getSunTime(final SolarLongitude desired, final boolean next) {
        return this.getSunTime(desired.value, next);
    }
    
    public long getSunRiseSet(final boolean rise) {
        final long t0 = this.time;
        final long noon = (this.time + this.fGmtOffset) / 86400000L * 86400000L - this.fGmtOffset + 43200000L;
        this.setTime(noon + (rise ? -6L : 6L) * 3600000L);
        final long t2 = this.riseOrSet(new CoordFunc() {
            public Equatorial eval() {
                return CalendarAstronomer.this.getSunPosition();
            }
        }, rise, 0.009302604913129777, 0.009890199094634533, 5000L);
        this.setTime(t0);
        return t2;
    }
    
    public Equatorial getMoonPosition() {
        if (this.moonPosition == null) {
            final double sunLong = this.getSunLongitude();
            final double day = this.getJulianDay() - 2447891.5;
            final double meanLongitude = norm2PI(0.22997150421858628 * day + 5.556284436750021);
            double meanAnomalyMoon = norm2PI(meanLongitude - 0.001944368345221015 * day - 0.6342598060246725);
            final double evection = 0.022233749341155764 * Math.sin(2.0 * (meanLongitude - sunLong) - meanAnomalyMoon);
            final double annual = 0.003242821750205464 * Math.sin(this.meanAnomalySun);
            final double a3 = 0.00645771823237902 * Math.sin(this.meanAnomalySun);
            meanAnomalyMoon += evection - annual - a3;
            final double center = 0.10975677534091541 * Math.sin(meanAnomalyMoon);
            final double a4 = 0.0037350045992678655 * Math.sin(2.0 * meanAnomalyMoon);
            this.moonLongitude = meanLongitude + evection + center - annual + a4;
            final double variation = 0.011489502465878671 * Math.sin(2.0 * (this.moonLongitude - sunLong));
            this.moonLongitude += variation;
            double nodeLongitude = norm2PI(5.559050068029439 - 9.242199067718253E-4 * day);
            nodeLongitude -= 0.0027925268031909274 * Math.sin(this.meanAnomalySun);
            final double y = Math.sin(this.moonLongitude - nodeLongitude);
            final double x = Math.cos(this.moonLongitude - nodeLongitude);
            this.moonEclipLong = Math.atan2(y * Math.cos(0.08980357792017056), x) + nodeLongitude;
            final double moonEclipLat = Math.asin(y * Math.sin(0.08980357792017056));
            this.moonPosition = this.eclipticToEquatorial(this.moonEclipLong, moonEclipLat);
        }
        return this.moonPosition;
    }
    
    public double getMoonAge() {
        this.getMoonPosition();
        return norm2PI(this.moonEclipLong - this.sunLongitude);
    }
    
    public double getMoonPhase() {
        return 0.5 * (1.0 - Math.cos(this.getMoonAge()));
    }
    
    public long getMoonTime(final double desired, final boolean next) {
        return this.timeOfAngle(new AngleFunc() {
            public double eval() {
                return CalendarAstronomer.this.getMoonAge();
            }
        }, desired, 29.530588853, 60000L, next);
    }
    
    public long getMoonTime(final MoonAge desired, final boolean next) {
        return this.getMoonTime(desired.value, next);
    }
    
    public long getMoonRiseSet(final boolean rise) {
        return this.riseOrSet(new CoordFunc() {
            public Equatorial eval() {
                return CalendarAstronomer.this.getMoonPosition();
            }
        }, rise, 0.009302604913129777, 0.009890199094634533, 60000L);
    }
    
    private long timeOfAngle(final AngleFunc func, final double desired, final double periodDays, final long epsilon, final boolean next) {
        double lastAngle = func.eval();
        final double deltaAngle = norm2PI(desired - lastAngle);
        double lastDeltaT;
        double deltaT = lastDeltaT = (deltaAngle + (next ? 0.0 : -6.283185307179586)) * (periodDays * 8.64E7) / 6.283185307179586;
        final long startTime = this.time;
        this.setTime(this.time + (long)deltaT);
        do {
            final double angle = func.eval();
            final double factor = Math.abs(deltaT / normPI(angle - lastAngle));
            deltaT = normPI(desired - angle) * factor;
            if (Math.abs(deltaT) > Math.abs(lastDeltaT)) {
                final long delta = (long)(periodDays * 8.64E7 / 8.0);
                this.setTime(startTime + (next ? delta : (-delta)));
                return this.timeOfAngle(func, desired, periodDays, epsilon, next);
            }
            lastDeltaT = deltaT;
            lastAngle = angle;
            this.setTime(this.time + (long)deltaT);
        } while (Math.abs(deltaT) > epsilon);
        return this.time;
    }
    
    private long riseOrSet(final CoordFunc func, final boolean rise, final double diameter, final double refraction, final long epsilon) {
        Equatorial pos = null;
        final double tanL = Math.tan(this.fLatitude);
        long deltaT = Long.MAX_VALUE;
        int count = 0;
        do {
            pos = func.eval();
            final double angle = Math.acos(-tanL * Math.tan(pos.declination));
            final double lst = ((rise ? (6.283185307179586 - angle) : angle) + pos.ascension) * 24.0 / 6.283185307179586;
            final long newTime = this.lstToUT(lst);
            deltaT = newTime - this.time;
            this.setTime(newTime);
        } while (++count < 5 && Math.abs(deltaT) > epsilon);
        final double cosD = Math.cos(pos.declination);
        final double psi = Math.acos(Math.sin(this.fLatitude) / cosD);
        final double x = diameter / 2.0 + refraction;
        final double y = Math.asin(Math.sin(x) / Math.sin(psi));
        final long delta = (long)(240.0 * y * 57.29577951308232 / cosD * 1000.0);
        return this.time + (rise ? (-delta) : delta);
    }
    
    private static final double normalize(final double value, final double range) {
        return value - range * Math.floor(value / range);
    }
    
    private static final double norm2PI(final double angle) {
        return normalize(angle, 6.283185307179586);
    }
    
    private static final double normPI(final double angle) {
        return normalize(angle + 3.141592653589793, 6.283185307179586) - 3.141592653589793;
    }
    
    private double trueAnomaly(final double meanAnomaly, final double eccentricity) {
        double E = meanAnomaly;
        double delta;
        do {
            delta = E - eccentricity * Math.sin(E) - meanAnomaly;
            E -= delta / (1.0 - eccentricity * Math.cos(E));
        } while (Math.abs(delta) > 1.0E-5);
        return 2.0 * Math.atan(Math.tan(E / 2.0) * Math.sqrt((1.0 + eccentricity) / (1.0 - eccentricity)));
    }
    
    private double eclipticObliquity() {
        if (this.eclipObliquity == Double.MIN_VALUE) {
            final double epoch = 2451545.0;
            final double T = (this.getJulianDay() - 2451545.0) / 36525.0;
            this.eclipObliquity = 23.439292 - 0.013004166666666666 * T - 1.6666666666666665E-7 * T * T + 5.027777777777778E-7 * T * T * T;
            this.eclipObliquity *= 0.017453292519943295;
        }
        return this.eclipObliquity;
    }
    
    private void clearCache() {
        this.julianDay = Double.MIN_VALUE;
        this.julianCentury = Double.MIN_VALUE;
        this.sunLongitude = Double.MIN_VALUE;
        this.meanAnomalySun = Double.MIN_VALUE;
        this.moonLongitude = Double.MIN_VALUE;
        this.moonEclipLong = Double.MIN_VALUE;
        this.eclipObliquity = Double.MIN_VALUE;
        this.siderealTime = Double.MIN_VALUE;
        this.siderealT0 = Double.MIN_VALUE;
        this.moonPosition = null;
    }
    
    public String local(final long localMillis) {
        return new Date(localMillis - TimeZone.getDefault().getRawOffset()).toString();
    }
    
    private static String radToHms(final double angle) {
        final int hrs = (int)(angle * 3.819718634205488);
        final int min = (int)((angle * 3.819718634205488 - hrs) * 60.0);
        final int sec = (int)((angle * 3.819718634205488 - hrs - min / 60.0) * 3600.0);
        return Integer.toString(hrs) + "h" + min + "m" + sec + "s";
    }
    
    private static String radToDms(final double angle) {
        final int deg = (int)(angle * 57.29577951308232);
        final int min = (int)((angle * 57.29577951308232 - deg) * 60.0);
        final int sec = (int)((angle * 57.29577951308232 - deg - min / 60.0) * 3600.0);
        return Integer.toString(deg) + "°" + min + "'" + sec + "\"";
    }
    
    static {
        VERNAL_EQUINOX = new SolarLongitude(0.0);
        SUMMER_SOLSTICE = new SolarLongitude(1.5707963267948966);
        AUTUMN_EQUINOX = new SolarLongitude(3.141592653589793);
        WINTER_SOLSTICE = new SolarLongitude(4.71238898038469);
        NEW_MOON = new MoonAge(0.0);
        FIRST_QUARTER = new MoonAge(1.5707963267948966);
        FULL_MOON = new MoonAge(3.141592653589793);
        LAST_QUARTER = new MoonAge(4.71238898038469);
    }
    
    private static class SolarLongitude
    {
        double value;
        
        SolarLongitude(final double val) {
            this.value = val;
        }
    }
    
    private static class MoonAge
    {
        double value;
        
        MoonAge(final double val) {
            this.value = val;
        }
    }
    
    public static final class Ecliptic
    {
        public final double latitude;
        public final double longitude;
        
        public Ecliptic(final double lat, final double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }
        
        @Override
        public String toString() {
            return Double.toString(this.longitude * 57.29577951308232) + "," + this.latitude * 57.29577951308232;
        }
    }
    
    public static final class Equatorial
    {
        public final double ascension;
        public final double declination;
        
        public Equatorial(final double asc, final double dec) {
            this.ascension = asc;
            this.declination = dec;
        }
        
        @Override
        public String toString() {
            return Double.toString(this.ascension * 57.29577951308232) + "," + this.declination * 57.29577951308232;
        }
        
        public String toHmsString() {
            return radToHms(this.ascension) + "," + radToDms(this.declination);
        }
    }
    
    public static final class Horizon
    {
        public final double altitude;
        public final double azimuth;
        
        public Horizon(final double alt, final double azim) {
            this.altitude = alt;
            this.azimuth = azim;
        }
        
        @Override
        public String toString() {
            return Double.toString(this.altitude * 57.29577951308232) + "," + this.azimuth * 57.29577951308232;
        }
    }
    
    private interface CoordFunc
    {
        Equatorial eval();
    }
    
    private interface AngleFunc
    {
        double eval();
    }
}
