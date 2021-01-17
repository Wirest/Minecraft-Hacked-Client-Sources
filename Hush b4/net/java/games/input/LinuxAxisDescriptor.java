// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class LinuxAxisDescriptor
{
    private int type;
    private int code;
    
    public final void set(final int type, final int code) {
        this.type = type;
        this.code = code;
    }
    
    public final int getType() {
        return this.type;
    }
    
    public final int getCode() {
        return this.code;
    }
    
    public final int hashCode() {
        return this.type ^ this.code;
    }
    
    public final boolean equals(final Object other) {
        if (!(other instanceof LinuxAxisDescriptor)) {
            return false;
        }
        final LinuxAxisDescriptor descriptor = (LinuxAxisDescriptor)other;
        return descriptor.type == this.type && descriptor.code == this.code;
    }
    
    public final String toString() {
        return "LinuxAxis: type = 0x" + Integer.toHexString(this.type) + ", code = 0x" + Integer.toHexString(this.code);
    }
}
