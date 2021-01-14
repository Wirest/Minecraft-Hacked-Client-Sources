package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Mapping0
        extends FuncMapping {
    static int seq = 0;
    float[][] pcmbundle = (float[][]) null;
    int[] zerobundle = null;
    int[] nonzero = null;
    Object[] floormemo = null;

    void free_info(Object paramObject) {
    }

    void free_look(Object paramObject) {
    }

    Object look(DspState paramDspState, InfoMode paramInfoMode, Object paramObject) {
        Info localInfo = paramDspState.vi;
        LookMapping0 localLookMapping0 = new LookMapping0();
        InfoMapping0 localInfoMapping0 = localLookMapping0.map = (InfoMapping0) paramObject;
        localLookMapping0.mode = paramInfoMode;
        localLookMapping0.time_look = new Object[localInfoMapping0.submaps];
        localLookMapping0.floor_look = new Object[localInfoMapping0.submaps];
        localLookMapping0.residue_look = new Object[localInfoMapping0.submaps];
        localLookMapping0.time_func = new FuncTime[localInfoMapping0.submaps];
        localLookMapping0.floor_func = new FuncFloor[localInfoMapping0.submaps];
        localLookMapping0.residue_func = new FuncResidue[localInfoMapping0.submaps];
        for (int i = 0; i < localInfoMapping0.submaps; i++) {
            int j = localInfoMapping0.timesubmap[i];
            int k = localInfoMapping0.floorsubmap[i];
            int m = localInfoMapping0.residuesubmap[i];
            localLookMapping0.time_func[i] = FuncTime.time_P[localInfo.time_type[j]];
            localLookMapping0.time_look[i] = localLookMapping0.time_func[i].look(paramDspState, paramInfoMode, localInfo.time_param[j]);
            localLookMapping0.floor_func[i] = FuncFloor.floor_P[localInfo.floor_type[k]];
            localLookMapping0.floor_look[i] = localLookMapping0.floor_func[i].look(paramDspState, paramInfoMode, localInfo.floor_param[k]);
            localLookMapping0.residue_func[i] = FuncResidue.residue_P[localInfo.residue_type[m]];
            localLookMapping0.residue_look[i] = localLookMapping0.residue_func[i].look(paramDspState, paramInfoMode, localInfo.residue_param[m]);
        }
        if ((localInfo.psys != 0) && (paramDspState.analysisp != 0)) {
        }
        localLookMapping0.ch = localInfo.channels;
        return localLookMapping0;
    }

    void pack(Info paramInfo, Object paramObject, Buffer paramBuffer) {
        InfoMapping0 localInfoMapping0 = (InfoMapping0) paramObject;
        if (localInfoMapping0.submaps > 1) {
            paramBuffer.write(1, 1);
            paramBuffer.write(localInfoMapping0.submaps - 1, 4);
        } else {
            paramBuffer.write(0, 1);
        }
        if (localInfoMapping0.coupling_steps > 0) {
            paramBuffer.write(1, 1);
            paramBuffer.write(localInfoMapping0.coupling_steps - 1, 8);
            for (i = 0; i < localInfoMapping0.coupling_steps; i++) {
                paramBuffer.write(localInfoMapping0.coupling_mag[i], Util.ilog2(paramInfo.channels));
                paramBuffer.write(localInfoMapping0.coupling_ang[i], Util.ilog2(paramInfo.channels));
            }
        } else {
            paramBuffer.write(0, 1);
        }
        paramBuffer.write(0, 2);
        if (localInfoMapping0.submaps > 1) {
            for (i = 0; i < paramInfo.channels; i++) {
                paramBuffer.write(localInfoMapping0.chmuxlist[i], 4);
            }
        }
        for (int i = 0; i < localInfoMapping0.submaps; i++) {
            paramBuffer.write(localInfoMapping0.timesubmap[i], 8);
            paramBuffer.write(localInfoMapping0.floorsubmap[i], 8);
            paramBuffer.write(localInfoMapping0.residuesubmap[i], 8);
        }
    }

    Object unpack(Info paramInfo, Buffer paramBuffer) {
        InfoMapping0 localInfoMapping0 = new InfoMapping0();
        if (paramBuffer.read(1) != 0) {
            localInfoMapping0.submaps = (paramBuffer.read(4) | 0x1);
        } else {
            localInfoMapping0.submaps = 1;
        }
        if (paramBuffer.read(1) != 0) {
            localInfoMapping0.coupling_steps = (paramBuffer.read(8) | 0x1);
            for (i = 0; i < localInfoMapping0.coupling_steps; i++) {
                int j = localInfoMapping0.coupling_mag[i] = paramBuffer.read(Util.ilog2(paramInfo.channels));
                int k = localInfoMapping0.coupling_ang[i] = paramBuffer.read(Util.ilog2(paramInfo.channels));
                if ((j < 0) || (k < 0) || (j == k) || (j >= paramInfo.channels) || (k >= paramInfo.channels)) {
                    localInfoMapping0.free();
                    return null;
                }
            }
        }
        if (paramBuffer.read(2) > 0) {
            localInfoMapping0.free();
            return null;
        }
        if (localInfoMapping0.submaps > 1) {
            for (i = 0; i < paramInfo.channels; i++) {
                localInfoMapping0.chmuxlist[i] = paramBuffer.read(4);
                if (localInfoMapping0.chmuxlist[i] >= localInfoMapping0.submaps) {
                    localInfoMapping0.free();
                    return null;
                }
            }
        }
        for (int i = 0; i < localInfoMapping0.submaps; i++) {
            localInfoMapping0.timesubmap[i] = paramBuffer.read(8);
            if (localInfoMapping0.timesubmap[i] >= paramInfo.times) {
                localInfoMapping0.free();
                return null;
            }
            localInfoMapping0.floorsubmap[i] = paramBuffer.read(8);
            if (localInfoMapping0.floorsubmap[i] >= paramInfo.floors) {
                localInfoMapping0.free();
                return null;
            }
            localInfoMapping0.residuesubmap[i] = paramBuffer.read(8);
            if (localInfoMapping0.residuesubmap[i] >= paramInfo.residues) {
                localInfoMapping0.free();
                return null;
            }
        }
        return localInfoMapping0;
    }

    synchronized int inverse(Block paramBlock, Object paramObject) {
        DspState localDspState = paramBlock.vd;
        Info localInfo = localDspState.vi;
        LookMapping0 localLookMapping0 = (LookMapping0) paramObject;
        InfoMapping0 localInfoMapping0 = localLookMapping0.map;
        InfoMode localInfoMode = localLookMapping0.mode;
        int i = paramBlock.pcmend = localInfo.blocksizes[paramBlock.W];
        float[] arrayOfFloat1 = localDspState.window[paramBlock.W][paramBlock.lW][paramBlock.nW][localInfoMode.windowtype];
        if ((this.pcmbundle == null) || (this.pcmbundle.length < localInfo.channels)) {
            this.pcmbundle = new float[localInfo.channels][];
            this.nonzero = new int[localInfo.channels];
            this.zerobundle = new int[localInfo.channels];
            this.floormemo = new Object[localInfo.channels];
        }
        int m;
        int i1;
        for (int j = 0; j < localInfo.channels; j++) {
            float[] arrayOfFloat2 = paramBlock.pcm[j];
            m = localInfoMapping0.chmuxlist[j];
            this.floormemo[j] = localLookMapping0.floor_func[m].inverse1(paramBlock, localLookMapping0.floor_look[m], this.floormemo[j]);
            if (this.floormemo[j] != null) {
                this.nonzero[j] = 1;
            } else {
                this.nonzero[j] = 0;
            }
            i1 = 0;
            arrayOfFloat2[i1] = 0.0F;
        }
        for (j = 0; j < localInfoMapping0.coupling_steps; j++) {
            if ((this.nonzero[localInfoMapping0.coupling_mag[j]] != 0) || (this.nonzero[localInfoMapping0.coupling_ang[j]] != 0)) {
                this.nonzero[localInfoMapping0.coupling_mag[j]] = 1;
                this.nonzero[localInfoMapping0.coupling_ang[j]] = 1;
            }
        }
        for (j = 0; j < localInfoMapping0.submaps; j++) {
            int k = 0;
            for (m = 0; m < localInfo.channels; m++) {
                if (localInfoMapping0.chmuxlist[m] == j) {
                    if (this.nonzero[m] != 0) {
                        this.zerobundle[k] = 1;
                    } else {
                        this.zerobundle[k] = 0;
                    }
                    this.pcmbundle[(k++)] = paramBlock.pcm[m];
                }
            }
            localLookMapping0.residue_func[j].inverse(paramBlock, localLookMapping0.residue_look[j], this.pcmbundle, this.zerobundle, k);
        }
        float[] arrayOfFloat3;
        for (j = localInfoMapping0.coupling_steps - 1; j >= 0; j--) {
            arrayOfFloat3 = paramBlock.pcm[localInfoMapping0.coupling_mag[j]];
            float[] arrayOfFloat4 = paramBlock.pcm[localInfoMapping0.coupling_ang[j]];
            for (i1 = 0; i < -2; i1++) {
                float f1 = arrayOfFloat3[i1];
                float f2 = arrayOfFloat4[i1];
                if (f1 > 0.0F) {
                    arrayOfFloat3[i1] = f1;
                    arrayOfFloat4[i1] = (f1 - f2);
                    arrayOfFloat4[i1] = f1;
                    arrayOfFloat3[i1] = (f1 + f2);
                } else if (f2 > 0.0F) {
                    arrayOfFloat3[i1] = f1;
                    arrayOfFloat4[i1] = (f1 + f2);
                } else {
                    arrayOfFloat4[i1] = f1;
                    arrayOfFloat3[i1] = (f2 > 0.0F ? i1 : f1 - f2);
                }
            }
        }
        int n;
        for (j = 0; j < localInfo.channels; j++) {
            arrayOfFloat3 = paramBlock.pcm[j];
            n = localInfoMapping0.chmuxlist[j];
            localLookMapping0.floor_func[n].inverse2(paramBlock, localLookMapping0.floor_look[n], this.floormemo[j], arrayOfFloat3);
        }
        for (j = 0; j < localInfo.channels; j++) {
            arrayOfFloat3 = paramBlock.pcm[j];
            ((Mdct) localDspState.transform[paramBlock.W][0]).backward(arrayOfFloat3, arrayOfFloat3);
        }
        for (j = 0; j < localInfo.channels; j++) {
            arrayOfFloat3 = paramBlock.pcm[j];
            if (this.nonzero[j] != 0) {
                for (n = 0; n < i; n++) {
                    arrayOfFloat3[n] *= arrayOfFloat1[n];
                }
            } else {
                for (n = 0; n < i; n++) {
                    arrayOfFloat3[n] = 0.0F;
                }
            }
        }
        return 0;
    }

    class LookMapping0 {
        InfoMode mode;
        Mapping0.InfoMapping0 map;
        Object[] time_look;
        Object[] floor_look;
        Object[] floor_state;
        Object[] residue_look;
        PsyLook[] psy_look;
        FuncTime[] time_func;
        FuncFloor[] floor_func;
        FuncResidue[] residue_func;
        int ch;
        float[][] decay;
        int lastframe;

        LookMapping0() {
        }
    }

    class InfoMapping0 {
        int submaps;
        int[] chmuxlist = new int['Ā'];
        int[] timesubmap = new int[16];
        int[] floorsubmap = new int[16];
        int[] residuesubmap = new int[16];
        int[] psysubmap = new int[16];
        int coupling_steps;
        int[] coupling_mag = new int['Ā'];
        int[] coupling_ang = new int['Ā'];

        InfoMapping0() {
        }

        void free() {
            this.chmuxlist = null;
            this.timesubmap = null;
            this.floorsubmap = null;
            this.residuesubmap = null;
            this.psysubmap = null;
            this.coupling_mag = null;
            this.coupling_ang = null;
        }
    }
}




