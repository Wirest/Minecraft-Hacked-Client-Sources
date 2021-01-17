// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

final class WindowsFileVersion
{
    private final int product_version_ms;
    private final int product_version_ls;
    
    WindowsFileVersion(final int product_version_ms, final int product_version_ls) {
        this.product_version_ms = product_version_ms;
        this.product_version_ls = product_version_ls;
    }
    
    @Override
    public String toString() {
        final int f1 = this.product_version_ms >> 16 & 0xFFFF;
        final int f2 = this.product_version_ms & 0xFFFF;
        final int f3 = this.product_version_ls >> 16 & 0xFFFF;
        final int f4 = this.product_version_ls & 0xFFFF;
        return f1 + "." + f2 + "." + f3 + "." + f4;
    }
}
