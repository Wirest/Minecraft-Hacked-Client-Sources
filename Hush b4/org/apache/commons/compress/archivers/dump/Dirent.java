// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.dump;

class Dirent
{
    private final int ino;
    private final int parentIno;
    private final int type;
    private final String name;
    
    Dirent(final int ino, final int parentIno, final int type, final String name) {
        this.ino = ino;
        this.parentIno = parentIno;
        this.type = type;
        this.name = name;
    }
    
    int getIno() {
        return this.ino;
    }
    
    int getParentIno() {
        return this.parentIno;
    }
    
    int getType() {
        return this.type;
    }
    
    String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return String.format("[%d]: %s", this.ino, this.name);
    }
}
