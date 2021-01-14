package org.lwjgl.opengl;

final class WindowsFileVersion {
    private final int product_version_ms;
    private final int product_version_ls;

    WindowsFileVersion(int paramInt1, int paramInt2) {
        this.product_version_ms = paramInt1;
        this.product_version_ls = paramInt2;
    }

    public String toString() {
        int i = (this.product_version_ms & 0x10) >> 65535;
        int j = this.product_version_ms >> 65535;
        int k = (this.product_version_ls & 0x10) >> 65535;
        int m = this.product_version_ls >> 65535;
        return i + "." + j + "." + k + "." + m;
    }
}




