// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Residue0 extends FuncResidue
{
    private static int[][][] _01inverse_partword;
    static int[][] _2inverse_partword;
    
    @Override
    void pack(final Object vr, final Buffer opb) {
        final InfoResidue0 info = (InfoResidue0)vr;
        int acc = 0;
        opb.write(info.begin, 24);
        opb.write(info.end, 24);
        opb.write(info.grouping - 1, 24);
        opb.write(info.partitions - 1, 6);
        opb.write(info.groupbook, 8);
        for (int j = 0; j < info.partitions; ++j) {
            final int i = info.secondstages[j];
            if (Util.ilog(i) > 3) {
                opb.write(i, 3);
                opb.write(1, 1);
                opb.write(i >>> 3, 5);
            }
            else {
                opb.write(i, 4);
            }
            acc += Util.icount(i);
        }
        for (int j = 0; j < acc; ++j) {
            opb.write(info.booklist[j], 8);
        }
    }
    
    @Override
    Object unpack(final Info vi, final Buffer opb) {
        int acc = 0;
        final InfoResidue0 info = new InfoResidue0();
        info.begin = opb.read(24);
        info.end = opb.read(24);
        info.grouping = opb.read(24) + 1;
        info.partitions = opb.read(6) + 1;
        info.groupbook = opb.read(8);
        for (int j = 0; j < info.partitions; ++j) {
            int cascade = opb.read(3);
            if (opb.read(1) != 0) {
                cascade |= opb.read(5) << 3;
            }
            info.secondstages[j] = cascade;
            acc += Util.icount(cascade);
        }
        for (int j = 0; j < acc; ++j) {
            info.booklist[j] = opb.read(8);
        }
        if (info.groupbook >= vi.books) {
            this.free_info(info);
            return null;
        }
        for (int j = 0; j < acc; ++j) {
            if (info.booklist[j] >= vi.books) {
                this.free_info(info);
                return null;
            }
        }
        return info;
    }
    
    @Override
    Object look(final DspState vd, final InfoMode vm, final Object vr) {
        final InfoResidue0 info = (InfoResidue0)vr;
        final LookResidue0 look = new LookResidue0();
        int acc = 0;
        int maxstage = 0;
        look.info = info;
        look.map = vm.mapping;
        look.parts = info.partitions;
        look.fullbooks = vd.fullbooks;
        look.phrasebook = vd.fullbooks[info.groupbook];
        final int dim = look.phrasebook.dim;
        look.partbooks = new int[look.parts][];
        for (int j = 0; j < look.parts; ++j) {
            final int i = info.secondstages[j];
            final int stages = Util.ilog(i);
            if (stages != 0) {
                if (stages > maxstage) {
                    maxstage = stages;
                }
                look.partbooks[j] = new int[stages];
                for (int k = 0; k < stages; ++k) {
                    if ((i & 1 << k) != 0x0) {
                        look.partbooks[j][k] = info.booklist[acc++];
                    }
                }
            }
        }
        look.partvals = (int)Math.rint(Math.pow(look.parts, dim));
        look.stages = maxstage;
        look.decodemap = new int[look.partvals][];
        for (int j = 0; j < look.partvals; ++j) {
            int val = j;
            int mult = look.partvals / look.parts;
            look.decodemap[j] = new int[dim];
            for (int k = 0; k < dim; ++k) {
                final int deco = val / mult;
                val -= deco * mult;
                mult /= look.parts;
                look.decodemap[j][k] = deco;
            }
        }
        return look;
    }
    
    @Override
    void free_info(final Object i) {
    }
    
    @Override
    void free_look(final Object i) {
    }
    
    static synchronized int _01inverse(final Block vb, final Object vl, final float[][] in, final int ch, final int decodepart) {
        final LookResidue0 look = (LookResidue0)vl;
        final InfoResidue0 info = look.info;
        final int samples_per_partition = info.grouping;
        final int partitions_per_word = look.phrasebook.dim;
        final int n = info.end - info.begin;
        final int partvals = n / samples_per_partition;
        final int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
        if (Residue0._01inverse_partword.length < ch) {
            Residue0._01inverse_partword = new int[ch][][];
        }
        for (int j = 0; j < ch; ++j) {
            if (Residue0._01inverse_partword[j] == null || Residue0._01inverse_partword[j].length < partwords) {
                Residue0._01inverse_partword[j] = new int[partwords][];
            }
        }
        for (int s = 0; s < look.stages; ++s) {
            int i = 0;
            int l = 0;
            while (i < partvals) {
                if (s == 0) {
                    for (int j = 0; j < ch; ++j) {
                        final int temp = look.phrasebook.decode(vb.opb);
                        if (temp == -1) {
                            return 0;
                        }
                        Residue0._01inverse_partword[j][l] = look.decodemap[temp];
                        if (Residue0._01inverse_partword[j][l] == null) {
                            return 0;
                        }
                    }
                }
                for (int k = 0; k < partitions_per_word && i < partvals; ++k, ++i) {
                    for (int j = 0; j < ch; ++j) {
                        final int offset = info.begin + i * samples_per_partition;
                        final int index = Residue0._01inverse_partword[j][l][k];
                        if ((info.secondstages[index] & 1 << s) != 0x0) {
                            final CodeBook stagebook = look.fullbooks[look.partbooks[index][s]];
                            if (stagebook != null) {
                                if (decodepart == 0) {
                                    if (stagebook.decodevs_add(in[j], offset, vb.opb, samples_per_partition) == -1) {
                                        return 0;
                                    }
                                }
                                else if (decodepart == 1 && stagebook.decodev_add(in[j], offset, vb.opb, samples_per_partition) == -1) {
                                    return 0;
                                }
                            }
                        }
                    }
                }
                ++l;
            }
        }
        return 0;
    }
    
    static synchronized int _2inverse(final Block vb, final Object vl, final float[][] in, final int ch) {
        final LookResidue0 look = (LookResidue0)vl;
        final InfoResidue0 info = look.info;
        final int samples_per_partition = info.grouping;
        final int partitions_per_word = look.phrasebook.dim;
        final int n = info.end - info.begin;
        final int partvals = n / samples_per_partition;
        final int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
        if (Residue0._2inverse_partword == null || Residue0._2inverse_partword.length < partwords) {
            Residue0._2inverse_partword = new int[partwords][];
        }
        for (int s = 0; s < look.stages; ++s) {
            int i = 0;
            int l = 0;
            while (i < partvals) {
                if (s == 0) {
                    final int temp = look.phrasebook.decode(vb.opb);
                    if (temp == -1) {
                        return 0;
                    }
                    Residue0._2inverse_partword[l] = look.decodemap[temp];
                    if (Residue0._2inverse_partword[l] == null) {
                        return 0;
                    }
                }
                for (int k = 0; k < partitions_per_word && i < partvals; ++k, ++i) {
                    final int offset = info.begin + i * samples_per_partition;
                    final int index = Residue0._2inverse_partword[l][k];
                    if ((info.secondstages[index] & 1 << s) != 0x0) {
                        final CodeBook stagebook = look.fullbooks[look.partbooks[index][s]];
                        if (stagebook != null && stagebook.decodevv_add(in, offset, ch, vb.opb, samples_per_partition) == -1) {
                            return 0;
                        }
                    }
                }
                ++l;
            }
        }
        return 0;
    }
    
    @Override
    int inverse(final Block vb, final Object vl, final float[][] in, final int[] nonzero, final int ch) {
        int used = 0;
        for (int i = 0; i < ch; ++i) {
            if (nonzero[i] != 0) {
                in[used++] = in[i];
            }
        }
        if (used != 0) {
            return _01inverse(vb, vl, in, used, 0);
        }
        return 0;
    }
    
    static {
        Residue0._01inverse_partword = new int[2][][];
        Residue0._2inverse_partword = null;
    }
    
    class LookResidue0
    {
        InfoResidue0 info;
        int map;
        int parts;
        int stages;
        CodeBook[] fullbooks;
        CodeBook phrasebook;
        int[][] partbooks;
        int partvals;
        int[][] decodemap;
        int postbits;
        int phrasebits;
        int frames;
    }
    
    class InfoResidue0
    {
        int begin;
        int end;
        int grouping;
        int partitions;
        int groupbook;
        int[] secondstages;
        int[] booklist;
        float[] entmax;
        float[] ampmax;
        int[] subgrp;
        int[] blimit;
        
        InfoResidue0() {
            this.secondstages = new int[64];
            this.booklist = new int[256];
            this.entmax = new float[64];
            this.ampmax = new float[64];
            this.subgrp = new int[64];
            this.blimit = new int[64];
        }
    }
}
