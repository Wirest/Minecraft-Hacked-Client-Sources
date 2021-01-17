// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import java.util.List;
import oshi.software.os.linux.proc.CentralProcessor;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
import java.util.ArrayList;
import oshi.software.os.linux.proc.GlobalMemory;
import oshi.hardware.Memory;
import oshi.hardware.Processor;
import oshi.hardware.HardwareAbstractionLayer;

public class LinuxHardwareAbstractionLayer implements HardwareAbstractionLayer
{
    private static final String SEPARATOR = "\\s+:\\s";
    private Processor[] _processors;
    private Memory _memory;
    
    public LinuxHardwareAbstractionLayer() {
        this._processors = null;
        this._memory = null;
    }
    
    public Memory getMemory() {
        if (this._memory == null) {
            this._memory = new GlobalMemory();
        }
        return this._memory;
    }
    
    public Processor[] getProcessors() {
        if (this._processors == null) {
            final List<Processor> processors = new ArrayList<Processor>();
            Scanner in = null;
            try {
                in = new Scanner(new FileReader("/proc/cpuinfo"));
            }
            catch (FileNotFoundException e) {
                System.err.println("Problem with: /proc/cpuinfo");
                System.err.println(e.getMessage());
                return null;
            }
            in.useDelimiter("\n");
            CentralProcessor cpu = null;
            while (in.hasNext()) {
                final String toBeAnalyzed = in.next();
                if (toBeAnalyzed.equals("")) {
                    if (cpu != null) {
                        processors.add(cpu);
                    }
                    cpu = null;
                }
                else {
                    if (cpu == null) {
                        cpu = new CentralProcessor();
                    }
                    if (toBeAnalyzed.startsWith("model name\t")) {
                        cpu.setName(toBeAnalyzed.split("\\s+:\\s")[1]);
                    }
                    else if (toBeAnalyzed.startsWith("flags\t")) {
                        final String[] flags = toBeAnalyzed.split("\\s+:\\s")[1].split(" ");
                        boolean found = false;
                        for (final String flag : flags) {
                            if (flag.equalsIgnoreCase("LM")) {
                                found = true;
                                break;
                            }
                        }
                        cpu.setCpu64(found);
                    }
                    else if (toBeAnalyzed.startsWith("cpu family\t")) {
                        cpu.setFamily(toBeAnalyzed.split("\\s+:\\s")[1]);
                    }
                    else if (toBeAnalyzed.startsWith("model\t")) {
                        cpu.setModel(toBeAnalyzed.split("\\s+:\\s")[1]);
                    }
                    else if (toBeAnalyzed.startsWith("stepping\t")) {
                        cpu.setStepping(toBeAnalyzed.split("\\s+:\\s")[1]);
                    }
                    else {
                        if (!toBeAnalyzed.startsWith("vendor_id")) {
                            continue;
                        }
                        cpu.setVendor(toBeAnalyzed.split("\\s+:\\s")[1]);
                    }
                }
            }
            in.close();
            if (cpu != null) {
                processors.add(cpu);
            }
            this._processors = processors.toArray(new Processor[0]);
        }
        return this._processors;
    }
}
