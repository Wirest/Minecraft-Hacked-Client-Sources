package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

abstract class FuncMapping {
    public static FuncMapping[] mapping_P = {new Mapping0()};

    abstract void pack(Info paramInfo, Object paramObject, Buffer paramBuffer);

    abstract Object unpack(Info paramInfo, Buffer paramBuffer);

    abstract Object look(DspState paramDspState, InfoMode paramInfoMode, Object paramObject);

    abstract void free_info(Object paramObject);

    abstract void free_look(Object paramObject);

    abstract int inverse(Block paramBlock, Object paramObject);
}




