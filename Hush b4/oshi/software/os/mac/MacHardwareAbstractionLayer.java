// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.mac;

import oshi.software.os.mac.local.GlobalMemory;
import java.util.List;
import oshi.software.os.mac.local.CentralProcessor;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.hardware.Memory;
import oshi.hardware.Processor;
import oshi.hardware.HardwareAbstractionLayer;

public class MacHardwareAbstractionLayer implements HardwareAbstractionLayer
{
    private Processor[] _processors;
    private Memory _memory;
    
    public Processor[] getProcessors() {
        if (this._processors == null) {
            final List<Processor> processors = new ArrayList<Processor>();
            for (int nbCPU = new Integer(ExecutingCommand.getFirstAnswer("sysctl -n hw.logicalcpu")), i = 0; i < nbCPU; ++i) {
                processors.add(new CentralProcessor());
            }
            this._processors = processors.toArray(new Processor[0]);
        }
        return this._processors;
    }
    
    public Memory getMemory() {
        if (this._memory == null) {
            this._memory = new GlobalMemory();
        }
        return this._memory;
    }
}
