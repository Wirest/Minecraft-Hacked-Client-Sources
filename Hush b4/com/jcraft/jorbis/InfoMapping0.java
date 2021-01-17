// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

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
