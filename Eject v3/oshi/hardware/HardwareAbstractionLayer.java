package oshi.hardware;

public abstract interface HardwareAbstractionLayer {
    public abstract Processor[] getProcessors();

    public abstract Memory getMemory();
}




