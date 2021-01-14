package optifine;

public class GlVersion
{
    private int major;
    private int minor;
    private int release;
    private String suffix;

    public GlVersion(int p_i49_1_, int p_i49_2_)
    {
        this(p_i49_1_, p_i49_2_, 0);
    }

    public GlVersion(int p_i50_1_, int p_i50_2_, int p_i50_3_)
    {
        this(p_i50_1_, p_i50_2_, p_i50_3_, (String)null);
    }

    public GlVersion(int p_i51_1_, int p_i51_2_, int p_i51_3_, String p_i51_4_)
    {
        this.major = p_i51_1_;
        this.minor = p_i51_2_;
        this.release = p_i51_3_;
        this.suffix = p_i51_4_;
    }

    public int getMajor()
    {
        return this.major;
    }

    public int getMinor()
    {
        return this.minor;
    }

    public int getRelease()
    {
        return this.release;
    }

    public int toInt()
    {
        return this.minor > 9 ? this.major * 100 + this.minor : (this.release > 9 ? this.major * 100 + this.minor * 10 + 9 : this.major * 100 + this.minor * 10 + this.release);
    }

    public String toString()
    {
        return this.suffix == null ? "" + this.major + "." + this.minor + "." + this.release : "" + this.major + "." + this.minor + "." + this.release + this.suffix;
    }
}
