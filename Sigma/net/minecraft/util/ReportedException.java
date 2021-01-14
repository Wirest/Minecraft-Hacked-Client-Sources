package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * Instance of CrashReport.
     */
    private final CrashReport theReportedExceptionCrashReport;
    private static final String __OBFID = "CL_00001579";

    public ReportedException(CrashReport p_i1356_1_) {
        theReportedExceptionCrashReport = p_i1356_1_;
    }

    /**
     * Gets the CrashReport wrapped by this exception.
     */
    public CrashReport getCrashReport() {
        return theReportedExceptionCrashReport;
    }

    @Override
    public Throwable getCause() {
        return theReportedExceptionCrashReport.getCrashCause();
    }

    @Override
    public String getMessage() {
        return theReportedExceptionCrashReport.getDescription();
    }
}
