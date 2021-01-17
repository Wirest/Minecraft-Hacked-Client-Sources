// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.vertex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormatElement
{
    private static final Logger LOGGER;
    private final EnumType type;
    private final EnumUsage usage;
    private int index;
    private int elementCount;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public VertexFormatElement(final int indexIn, final EnumType typeIn, final EnumUsage usageIn, final int count) {
        if (!this.func_177372_a(indexIn, usageIn)) {
            VertexFormatElement.LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
            this.usage = EnumUsage.UV;
        }
        else {
            this.usage = usageIn;
        }
        this.type = typeIn;
        this.index = indexIn;
        this.elementCount = count;
    }
    
    private final boolean func_177372_a(final int p_177372_1_, final EnumUsage p_177372_2_) {
        return p_177372_1_ == 0 || p_177372_2_ == EnumUsage.UV;
    }
    
    public final EnumType getType() {
        return this.type;
    }
    
    public final EnumUsage getUsage() {
        return this.usage;
    }
    
    public final int getElementCount() {
        return this.elementCount;
    }
    
    public final int getIndex() {
        return this.index;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.elementCount) + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
    }
    
    public final int getSize() {
        return this.type.getSize() * this.elementCount;
    }
    
    public final boolean isPositionElement() {
        return this.usage == EnumUsage.POSITION;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            final VertexFormatElement vertexformatelement = (VertexFormatElement)p_equals_1_;
            return this.elementCount == vertexformatelement.elementCount && this.index == vertexformatelement.index && this.type == vertexformatelement.type && this.usage == vertexformatelement.usage;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int i = this.type.hashCode();
        i = 31 * i + this.usage.hashCode();
        i = 31 * i + this.index;
        i = 31 * i + this.elementCount;
        return i;
    }
    
    public enum EnumType
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
        
        private EnumType(final String name, final int ordinal, final int sizeIn, final String displayNameIn, final int glConstantIn) {
            this.size = sizeIn;
            this.displayName = displayNameIn;
            this.glConstant = glConstantIn;
        }
        
        public int getSize() {
            return this.size;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public int getGlConstant() {
            return this.glConstant;
        }
    }
    
    public enum EnumUsage
    {
        POSITION("POSITION", 0, "Position"), 
        NORMAL("NORMAL", 1, "Normal"), 
        COLOR("COLOR", 2, "Vertex Color"), 
        UV("UV", 3, "UV"), 
        MATRIX("MATRIX", 4, "Bone Matrix"), 
        BLEND_WEIGHT("BLEND_WEIGHT", 5, "Blend Weight"), 
        PADDING("PADDING", 6, "Padding");
        
        private final String displayName;
        
        private EnumUsage(final String name, final int ordinal, final String displayNameIn) {
            this.displayName = displayNameIn;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
    }
}
