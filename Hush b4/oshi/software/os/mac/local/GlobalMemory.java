// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.mac.local;

import java.util.Iterator;
import oshi.util.ExecutingCommand;
import oshi.hardware.Memory;

public class GlobalMemory implements Memory
{
    private long totalMemory;
    
    public GlobalMemory() {
        this.totalMemory = 0L;
    }
    
    public long getAvailable() {
        long returnCurrentUsageMemory = 0L;
        for (final String line : ExecutingCommand.runNative("vm_stat")) {
            if (line.startsWith("Pages free:")) {
                final String[] memorySplit = line.split(":\\s+");
                returnCurrentUsageMemory += new Long(memorySplit[1].replace(".", ""));
            }
            else {
                if (!line.startsWith("Pages speculative:")) {
                    continue;
                }
                final String[] memorySplit = line.split(":\\s+");
                returnCurrentUsageMemory += new Long(memorySplit[1].replace(".", ""));
            }
        }
        returnCurrentUsageMemory *= 4096L;
        return returnCurrentUsageMemory;
    }
    
    public long getTotal() {
        if (this.totalMemory == 0L) {
            this.totalMemory = new Long(ExecutingCommand.getFirstAnswer("sysctl -n hw.memsize"));
        }
        return this.totalMemory;
    }
}
