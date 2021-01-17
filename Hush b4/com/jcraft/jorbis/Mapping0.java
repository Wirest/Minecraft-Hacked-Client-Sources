// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Mapping0 extends FuncMapping
{
    static int seq;
    float[][] pcmbundle;
    int[] zerobundle;
    int[] nonzero;
    Object[] floormemo;
    
    Mapping0() {
        this.pcmbundle = null;
        this.zerobundle = null;
        this.nonzero = null;
        this.floormemo = null;
    }
    
    @Override
    void free_info(final Object imap) {
    }
    
    @Override
    void free_look(final Object imap) {
    }
    
    @Override
    Object look(final DspState vd, final InfoMode vm, final Object m) {
        final Info vi = vd.vi;
        final LookMapping0 lookMapping0;
        final LookMapping0 look = lookMapping0 = new LookMapping0();
        final InfoMapping0 map = (InfoMapping0)m;
        lookMapping0.map = map;
        final InfoMapping0 info = map;
        look.mode = vm;
        look.time_look = new Object[info.submaps];
        look.floor_look = new Object[info.submaps];
        look.residue_look = new Object[info.submaps];
        look.time_func = new FuncTime[info.submaps];
        look.floor_func = new FuncFloor[info.submaps];
        look.residue_func = new FuncResidue[info.submaps];
        for (int i = 0; i < info.submaps; ++i) {
            final int timenum = info.timesubmap[i];
            final int floornum = info.floorsubmap[i];
            final int resnum = info.residuesubmap[i];
            look.time_func[i] = FuncTime.time_P[vi.time_type[timenum]];
            look.time_look[i] = look.time_func[i].look(vd, vm, vi.time_param[timenum]);
            look.floor_func[i] = FuncFloor.floor_P[vi.floor_type[floornum]];
            look.floor_look[i] = look.floor_func[i].look(vd, vm, vi.floor_param[floornum]);
            look.residue_func[i] = FuncResidue.residue_P[vi.residue_type[resnum]];
            look.residue_look[i] = look.residue_func[i].look(vd, vm, vi.residue_param[resnum]);
        }
        if (vi.psys == 0 || vd.analysisp != 0) {}
        look.ch = vi.channels;
        return look;
    }
    
    @Override
    void pack(final Info vi, final Object imap, final Buffer opb) {
        final InfoMapping0 info = (InfoMapping0)imap;
        if (info.submaps > 1) {
            opb.write(1, 1);
            opb.write(info.submaps - 1, 4);
        }
        else {
            opb.write(0, 1);
        }
        if (info.coupling_steps > 0) {
            opb.write(1, 1);
            opb.write(info.coupling_steps - 1, 8);
            for (int i = 0; i < info.coupling_steps; ++i) {
                opb.write(info.coupling_mag[i], Util.ilog2(vi.channels));
                opb.write(info.coupling_ang[i], Util.ilog2(vi.channels));
            }
        }
        else {
            opb.write(0, 1);
        }
        opb.write(0, 2);
        if (info.submaps > 1) {
            for (int i = 0; i < vi.channels; ++i) {
                opb.write(info.chmuxlist[i], 4);
            }
        }
        for (int i = 0; i < info.submaps; ++i) {
            opb.write(info.timesubmap[i], 8);
            opb.write(info.floorsubmap[i], 8);
            opb.write(info.residuesubmap[i], 8);
        }
    }
    
    @Override
    Object unpack(final Info vi, final Buffer opb) {
        final InfoMapping0 info = new InfoMapping0();
        if (opb.read(1) != 0) {
            info.submaps = opb.read(4) + 1;
        }
        else {
            info.submaps = 1;
        }
        if (opb.read(1) != 0) {
            info.coupling_steps = opb.read(8) + 1;
            for (int i = 0; i < info.coupling_steps; ++i) {
                final int[] coupling_mag = info.coupling_mag;
                final int n = i;
                final int read = opb.read(Util.ilog2(vi.channels));
                coupling_mag[n] = read;
                final int testM = read;
                final int[] coupling_ang = info.coupling_ang;
                final int n2 = i;
                final int read2 = opb.read(Util.ilog2(vi.channels));
                coupling_ang[n2] = read2;
                final int testA = read2;
                if (testM < 0 || testA < 0 || testM == testA || testM >= vi.channels || testA >= vi.channels) {
                    info.free();
                    return null;
                }
            }
        }
        if (opb.read(2) > 0) {
            info.free();
            return null;
        }
        if (info.submaps > 1) {
            for (int i = 0; i < vi.channels; ++i) {
                info.chmuxlist[i] = opb.read(4);
                if (info.chmuxlist[i] >= info.submaps) {
                    info.free();
                    return null;
                }
            }
        }
        for (int i = 0; i < info.submaps; ++i) {
            info.timesubmap[i] = opb.read(8);
            if (info.timesubmap[i] >= vi.times) {
                info.free();
                return null;
            }
            info.floorsubmap[i] = opb.read(8);
            if (info.floorsubmap[i] >= vi.floors) {
                info.free();
                return null;
            }
            info.residuesubmap[i] = opb.read(8);
            if (info.residuesubmap[i] >= vi.residues) {
                info.free();
                return null;
            }
        }
        return info;
    }
    
    @Override
    synchronized int inverse(final Block vb, final Object l) {
        final DspState vd = vb.vd;
        final Info vi = vd.vi;
        final LookMapping0 look = (LookMapping0)l;
        final InfoMapping0 info = look.map;
        final InfoMode mode = look.mode;
        final int pcmend = vi.blocksizes[vb.W];
        vb.pcmend = pcmend;
        final int n = pcmend;
        final float[] window = vd.window[vb.W][vb.lW][vb.nW][mode.windowtype];
        if (this.pcmbundle == null || this.pcmbundle.length < vi.channels) {
            this.pcmbundle = new float[vi.channels][];
            this.nonzero = new int[vi.channels];
            this.zerobundle = new int[vi.channels];
            this.floormemo = new Object[vi.channels];
        }
        for (int i = 0; i < vi.channels; ++i) {
            final float[] pcm = vb.pcm[i];
            final int submap = info.chmuxlist[i];
            this.floormemo[i] = look.floor_func[submap].inverse1(vb, look.floor_look[submap], this.floormemo[i]);
            if (this.floormemo[i] != null) {
                this.nonzero[i] = 1;
            }
            else {
                this.nonzero[i] = 0;
            }
            for (int j = 0; j < n / 2; ++j) {
                pcm[j] = 0.0f;
            }
        }
        for (int i = 0; i < info.coupling_steps; ++i) {
            if (this.nonzero[info.coupling_mag[i]] != 0 || this.nonzero[info.coupling_ang[i]] != 0) {
                this.nonzero[info.coupling_mag[i]] = 1;
                this.nonzero[info.coupling_ang[i]] = 1;
            }
        }
        for (int i = 0; i < info.submaps; ++i) {
            int ch_in_bundle = 0;
            for (int k = 0; k < vi.channels; ++k) {
                if (info.chmuxlist[k] == i) {
                    if (this.nonzero[k] != 0) {
                        this.zerobundle[ch_in_bundle] = 1;
                    }
                    else {
                        this.zerobundle[ch_in_bundle] = 0;
                    }
                    this.pcmbundle[ch_in_bundle++] = vb.pcm[k];
                }
            }
            look.residue_func[i].inverse(vb, look.residue_look[i], this.pcmbundle, this.zerobundle, ch_in_bundle);
        }
        for (int i = info.coupling_steps - 1; i >= 0; --i) {
            final float[] pcmM = vb.pcm[info.coupling_mag[i]];
            final float[] pcmA = vb.pcm[info.coupling_ang[i]];
            for (int j = 0; j < n / 2; ++j) {
                final float mag = pcmM[j];
                final float ang = pcmA[j];
                if (mag > 0.0f) {
                    if (ang > 0.0f) {
                        pcmA[j] = (pcmM[j] = mag) - ang;
                    }
                    else {
                        pcmM[j] = (pcmA[j] = mag) + ang;
                    }
                }
                else if (ang > 0.0f) {
                    pcmA[j] = (pcmM[j] = mag) + ang;
                }
                else {
                    pcmM[j] = (pcmA[j] = mag) - ang;
                }
            }
        }
        for (int i = 0; i < vi.channels; ++i) {
            final float[] pcm = vb.pcm[i];
            final int submap = info.chmuxlist[i];
            look.floor_func[submap].inverse2(vb, look.floor_look[submap], this.floormemo[i], pcm);
        }
        for (int i = 0; i < vi.channels; ++i) {
            final float[] pcm = vb.pcm[i];
            ((Mdct)vd.transform[vb.W][0]).backward(pcm, pcm);
        }
        for (int i = 0; i < vi.channels; ++i) {
            final float[] pcm = vb.pcm[i];
            if (this.nonzero[i] != 0) {
                for (int k = 0; k < n; ++k) {
                    final float[] array = pcm;
                    final int n2 = k;
                    array[n2] *= window[k];
                }
            }
            else {
                for (int k = 0; k < n; ++k) {
                    pcm[k] = 0.0f;
                }
            }
        }
        return 0;
    }
    
    static {
        Mapping0.seq = 0;
    }
    
    class InfoMapping0
    {
        int submaps;
        int[] chmuxlist;
        int[] timesubmap;
        int[] floorsubmap;
        int[] residuesubmap;
        int[] psysubmap;
        int coupling_steps;
        int[] coupling_mag;
        int[] coupling_ang;
        
        InfoMapping0() {
            this.chmuxlist = new int[256];
            this.timesubmap = new int[16];
            this.floorsubmap = new int[16];
            this.residuesubmap = new int[16];
            this.psysubmap = new int[16];
            this.coupling_mag = new int[256];
            this.coupling_ang = new int[256];
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
    
    class LookMapping0
    {
        InfoMode mode;
        InfoMapping0 map;
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
    }
}
