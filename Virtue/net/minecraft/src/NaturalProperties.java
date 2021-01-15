package net.minecraft.src;

public class NaturalProperties
{
    public int rotation = 1;
    public boolean flip = false;

    public NaturalProperties(String type)
    {
        if (type.equals("4"))
        {
            this.rotation = 4;
        }
        else if (type.equals("2"))
        {
            this.rotation = 2;
        }
        else if (type.equals("F"))
        {
            this.flip = true;
        }
        else if (type.equals("4F"))
        {
            this.rotation = 4;
            this.flip = true;
        }
        else if (type.equals("2F"))
        {
            this.rotation = 2;
            this.flip = true;
        }
        else
        {
            Config.warn("NaturalTextures: Unknown type: " + type);
        }
    }

    public boolean isValid()
    {
        return this.rotation != 2 && this.rotation != 4 ? this.flip : true;
    }
}
