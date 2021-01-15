package net.minecraft.src;

public class NbtTagValue
{
    private String tag = null;
    private String value = null;

    public NbtTagValue(String tag, String value)
    {
        this.tag = tag;
        this.value = value;
    }

    public boolean matches(String key, String value)
    {
        return false;
    }
}
