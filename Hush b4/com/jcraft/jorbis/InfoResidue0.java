// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

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
