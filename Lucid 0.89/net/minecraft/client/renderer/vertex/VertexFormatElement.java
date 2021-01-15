package net.minecraft.client.renderer.vertex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormatElement
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final VertexFormatElement.EnumType type;
    private final VertexFormatElement.EnumUseage usage;
    private int index;
    private int elementCount;
    private int offset;

    public VertexFormatElement(int indexIn, VertexFormatElement.EnumType typeIn, VertexFormatElement.EnumUseage usageIn, int count)
    {
        if (!this.func_177372_a(indexIn, usageIn))
        {
            LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
            this.usage = VertexFormatElement.EnumUseage.UV;
        }
        else
        {
            this.usage = usageIn;
        }

        this.type = typeIn;
        this.index = indexIn;
        this.elementCount = count;
        this.offset = 0;
    }

    public void setOffset(int offsetIn)
    {
        this.offset = offsetIn;
    }

    public int getOffset()
    {
        return this.offset;
    }

    private final boolean func_177372_a(int p_177372_1_, VertexFormatElement.EnumUseage p_177372_2_)
    {
        return p_177372_1_ == 0 || p_177372_2_ == VertexFormatElement.EnumUseage.UV;
    }

    public final VertexFormatElement.EnumType getType()
    {
        return this.type;
    }

    public final VertexFormatElement.EnumUseage getUsage()
    {
        return this.usage;
    }

    public final int getElementCount()
    {
        return this.elementCount;
    }

    public final int getIndex()
    {
        return this.index;
    }

    @Override
	public String toString()
    {
        return this.elementCount + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
    }

    public final int getSize()
    {
        return this.type.getSize() * this.elementCount;
    }

    public final boolean isPositionElement()
    {
        return this.usage == VertexFormatElement.EnumUseage.POSITION;
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            VertexFormatElement var2 = (VertexFormatElement)p_equals_1_;
            return this.elementCount != var2.elementCount ? false : (this.index != var2.index ? false : (this.offset != var2.offset ? false : (this.type != var2.type ? false : this.usage == var2.usage)));
        }
        else
        {
            return false;
        }
    }

    @Override
	public int hashCode()
    {
        int var1 = this.type.hashCode();
        var1 = 31 * var1 + this.usage.hashCode();
        var1 = 31 * var1 + this.index;
        var1 = 31 * var1 + this.elementCount;
        var1 = 31 * var1 + this.offset;
        return var1;
    }

    public static enum EnumType
    {
        FLOAT("FLOAT", 0, 4, "Float", 5126),
        UBYTE("UBYTE", 1, 1, "Unsigned Byte", 5121),
        BYTE("BYTE", 2, 1, "Byte", 5120),
        USHORT("USHORT", 3, 2, "Unsigned Short", 5123),
        SHORT("SHORT", 4, 2, "Short", 5122),
        UINT("UINT", 5, 4, "Unsigned Int", 5125),
        INT("INT", 6, 4, "Int", 5124);
        private final int size;
        private final String displayName;
        private final int glConstant; 

        private EnumType(String p_i46095_1_, int p_i46095_2_, int sizeIn, String displayNameIn, int glConstantIn)
        {
            this.size = sizeIn;
            this.displayName = displayNameIn;
            this.glConstant = glConstantIn;
        }

        public int getSize()
        {
            return this.size;
        }

        public String getDisplayName()
        {
            return this.displayName;
        }

        public int getGlConstant()
        {
            return this.glConstant;
        }
    }

    public static enum EnumUseage
    {
        POSITION("POSITION", 0, "Position"),
        NORMAL("NORMAL", 1, "Normal"),
        COLOR("COLOR", 2, "Vertex Color"),
        UV("UV", 3, "UV"),
        MATRIX("MATRIX", 4, "Bone Matrix"),
        BLEND_WEIGHT("BLEND_WEIGHT", 5, "Blend Weight"),
        PADDING("PADDING", 6, "Padding");
        private final String displayName; 

        private EnumUseage(String p_i46094_1_, int index, String displayNameIn)
        {
            this.displayName = displayNameIn;
        }

        public String getDisplayName()
        {
            return this.displayName;
        }
    }
}
