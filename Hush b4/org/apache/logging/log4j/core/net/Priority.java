// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Level;

public class Priority
{
    private final Facility facility;
    private final Severity severity;
    
    public Priority(final Facility facility, final Severity severity) {
        this.facility = facility;
        this.severity = severity;
    }
    
    public static int getPriority(final Facility facility, final Level level) {
        return (facility.getCode() << 3) + Severity.getSeverity(level).getCode();
    }
    
    public Facility getFacility() {
        return this.facility;
    }
    
    public Severity getSeverity() {
        return this.severity;
    }
    
    public int getValue() {
        return this.facility.getCode() << 3 + this.severity.getCode();
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.getValue());
    }
}
