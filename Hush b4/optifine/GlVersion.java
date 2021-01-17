// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class GlVersion
{
    private int major;
    private int minor;
    private int release;
    private String suffix;
    
    public GlVersion(final int p_i43_1_, final int p_i43_2_) {
        this(p_i43_1_, p_i43_2_, 0);
    }
    
    public GlVersion(final int p_i44_1_, final int p_i44_2_, final int p_i44_3_) {
        this(p_i44_1_, p_i44_2_, p_i44_3_, null);
    }
    
    public GlVersion(final int p_i45_1_, final int p_i45_2_, final int p_i45_3_, final String p_i45_4_) {
        this.major = p_i45_1_;
        this.minor = p_i45_2_;
        this.release = p_i45_3_;
        this.suffix = p_i45_4_;
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
        return (this.minor > 9) ? (this.major * 100 + this.minor) : ((this.release > 9) ? (this.major * 100 + this.minor * 10 + 9) : (this.major * 100 + this.minor * 10 + this.release));
    }
    
    @Override
    public String toString() {
        return (this.suffix == null) ? (this.major + "." + this.minor + "." + this.release) : (this.major + "." + this.minor + "." + this.release + this.suffix);
    }
}
