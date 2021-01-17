// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException
{
    private final CrashReport theReportedExceptionCrashReport;
    
    public ReportedException(final CrashReport report) {
        this.theReportedExceptionCrashReport = report;
    }
    
    public CrashReport getCrashReport() {
        return this.theReportedExceptionCrashReport;
    }
    
    @Override
    public Throwable getCause() {
        return this.theReportedExceptionCrashReport.getCrashCause();
    }
    
    @Override
    public String getMessage() {
        return this.theReportedExceptionCrashReport.getDescription();
    }
}
