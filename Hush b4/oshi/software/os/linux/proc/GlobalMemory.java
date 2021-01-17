// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux.proc;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
import oshi.hardware.Memory;

public class GlobalMemory implements Memory
{
    private long totalMemory;
    
    public GlobalMemory() {
        this.totalMemory = 0L;
    }
    
    public long getAvailable() {
        long returnCurrentUsageMemory = 0L;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("/proc/meminfo"));
        }
        catch (FileNotFoundException e) {
            return returnCurrentUsageMemory;
        }
        in.useDelimiter("\n");
        while (in.hasNext()) {
            final String checkLine = in.next();
            if (checkLine.startsWith("MemFree:") || checkLine.startsWith("MemAvailable:")) {
                final String[] memorySplit = checkLine.split("\\s+");
                returnCurrentUsageMemory = new Long(memorySplit[1]);
                if (memorySplit[2].equals("kB")) {
                    returnCurrentUsageMemory *= 1024L;
                }
                if (memorySplit[0].equals("MemAvailable:")) {
                    break;
                }
                continue;
            }
        }
        in.close();
        return returnCurrentUsageMemory;
    }
    
    public long getTotal() {
        if (this.totalMemory == 0L) {
            Scanner in = null;
            try {
                in = new Scanner(new FileReader("/proc/meminfo"));
            }
            catch (FileNotFoundException e) {
                return this.totalMemory = 0L;
            }
            in.useDelimiter("\n");
            while (in.hasNext()) {
                final String checkLine = in.next();
                if (checkLine.startsWith("MemTotal:")) {
                    final String[] memorySplit = checkLine.split("\\s+");
                    this.totalMemory = new Long(memorySplit[1]);
                    if (memorySplit[2].equals("kB")) {
                        this.totalMemory *= 1024L;
                        break;
                    }
                    break;
                }
            }
            in.close();
        }
        return this.totalMemory;
    }
}
