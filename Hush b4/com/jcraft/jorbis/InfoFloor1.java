// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class InfoFloor1
{
    static final int VIF_POSIT = 63;
    static final int VIF_CLASS = 16;
    static final int VIF_PARTS = 31;
    int partitions;
    int[] partitionclass;
    int[] class_dim;
    int[] class_subs;
    int[] class_book;
    int[][] class_subbook;
    int mult;
    int[] postlist;
    float maxover;
    float maxunder;
    float maxerr;
    int twofitminsize;
    int twofitminused;
    int twofitweight;
    float twofitatten;
    int unusedminsize;
    int unusedmin_n;
    int n;
    
    InfoFloor1() {
        this.partitionclass = new int[31];
        this.class_dim = new int[16];
        this.class_subs = new int[16];
        this.class_book = new int[16];
        this.class_subbook = new int[16][];
        this.postlist = new int[65];
        for (int i = 0; i < this.class_subbook.length; ++i) {
            this.class_subbook[i] = new int[8];
        }
    }
    
    void free() {
        this.partitionclass = null;
        this.class_dim = null;
        this.class_subs = null;
        this.class_book = null;
        this.class_subbook = null;
        this.postlist = null;
    }
    
    Object copy_info() {
        final InfoFloor1 infoFloor1 = new InfoFloor1();
        infoFloor1.partitions = this.partitions;
        System.arraycopy(this.partitionclass, 0, infoFloor1.partitionclass, 0, 31);
        System.arraycopy(this.class_dim, 0, infoFloor1.class_dim, 0, 16);
        System.arraycopy(this.class_subs, 0, infoFloor1.class_subs, 0, 16);
        System.arraycopy(this.class_book, 0, infoFloor1.class_book, 0, 16);
        for (int i = 0; i < 16; ++i) {
            System.arraycopy(this.class_subbook[i], 0, infoFloor1.class_subbook[i], 0, 8);
        }
        infoFloor1.mult = this.mult;
        System.arraycopy(this.postlist, 0, infoFloor1.postlist, 0, 65);
        infoFloor1.maxover = this.maxover;
        infoFloor1.maxunder = this.maxunder;
        infoFloor1.maxerr = this.maxerr;
        infoFloor1.twofitminsize = this.twofitminsize;
        infoFloor1.twofitminused = this.twofitminused;
        infoFloor1.twofitweight = this.twofitweight;
        infoFloor1.twofitatten = this.twofitatten;
        infoFloor1.unusedminsize = this.unusedminsize;
        infoFloor1.unusedmin_n = this.unusedmin_n;
        infoFloor1.n = this.n;
        return infoFloor1;
    }
}
