// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.util.EnglishEnums;

public enum Facility
{
    KERN(0), 
    USER(1), 
    MAIL(2), 
    DAEMON(3), 
    AUTH(4), 
    SYSLOG(5), 
    LPR(6), 
    NEWS(7), 
    UUCP(8), 
    CRON(9), 
    AUTHPRIV(10), 
    FTP(11), 
    NTP(12), 
    LOG_AUDIT(13), 
    LOG_ALERT(14), 
    CLOCK(15), 
    LOCAL0(16), 
    LOCAL1(17), 
    LOCAL2(18), 
    LOCAL3(19), 
    LOCAL4(20), 
    LOCAL5(21), 
    LOCAL6(22), 
    LOCAL7(23);
    
    private final int code;
    
    private Facility(final int code) {
        this.code = code;
    }
    
    public static Facility toFacility(final String name) {
        return toFacility(name, null);
    }
    
    public static Facility toFacility(final String name, final Facility defaultFacility) {
        return EnglishEnums.valueOf(Facility.class, name, defaultFacility);
    }
    
    public int getCode() {
        return this.code;
    }
    
    public boolean isEqual(final String name) {
        return this.name().equalsIgnoreCase(name);
    }
}
