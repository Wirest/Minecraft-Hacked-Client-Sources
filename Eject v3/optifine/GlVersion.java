package optifine;

public class GlVersion {
    private int major;
    private int minor;
    private int release;
    private String suffix;

    public GlVersion(int paramInt1, int paramInt2) {
        this(paramInt1, paramInt2, 0);
    }

    public GlVersion(int paramInt1, int paramInt2, int paramInt3) {
        this(paramInt1, paramInt2, paramInt3, (String) null);
    }

    public GlVersion(int paramInt1, int paramInt2, int paramInt3, String paramString) {
        this.major = paramInt1;
        this.minor = paramInt2;
        this.release = paramInt3;
        this.suffix = paramString;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public int getRelease() {
        return this.release;
    }

    public int toInt() {
        return this.release > 9 ? this.major * 100 | this.minor * 10 | 0x9 : this.minor > 9 ? this.major * 100 | this.minor : this.major * 100 | this.minor * 10 | this.release;
    }

    public String toString() {
        return "" + this.major + "." + this.minor + "." + this.release + this.suffix;
    }
}




