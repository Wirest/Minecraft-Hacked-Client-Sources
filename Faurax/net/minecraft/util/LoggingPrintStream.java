package net.minecraft.util;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingPrintStream extends PrintStream
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final String domain;
    private static final String __OBFID = "CL_00002275";

    public LoggingPrintStream(String p_i45927_1_, OutputStream p_i45927_2_)
    {
        super(p_i45927_2_);
        this.domain = p_i45927_1_;
    }

    public void println(String p_println_1_)
    {
        this.logString(p_println_1_);
    }

    public void println(Object p_println_1_)
    {
        this.logString(String.valueOf(p_println_1_));
    }

    private void logString(String p_179882_1_)
    {
        StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
        StackTraceElement var3 = var2[Math.min(3, var2.length)];
        LOGGER.info("[{}]@.({}:{}): {}", new Object[] {this.domain, var3.getFileName(), Integer.valueOf(var3.getLineNumber()), p_179882_1_});
    }
}
