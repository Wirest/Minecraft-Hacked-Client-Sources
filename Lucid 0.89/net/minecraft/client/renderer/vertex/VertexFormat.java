package net.minecraft.client.renderer.vertex;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

public class VertexFormat
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final List elements;
    private final List offsets;

    /** The next available offset in this vertex format */
    private int nextOffset;
    private int colorElementOffset;
    private List elementOffsetsById;
    private int normalElementOffset;

    public VertexFormat(VertexFormat vertexFormatIn)
    {
        this();

        for (int var2 = 0; var2 < vertexFormatIn.getElementCount(); ++var2)
        {
            this.setElement(vertexFormatIn.getElement(var2));
        }

        this.nextOffset = vertexFormatIn.getNextOffset();
    }

    public VertexFormat()
    {
        this.elements = Lists.newArrayList();
        this.offsets = Lists.newArrayList();
        this.nextOffset = 0;
        this.colorElementOffset = -1;
        this.elementOffsetsById = Lists.newArrayList();
        this.normalElementOffset = -1;
    }

    public void clear()
    {
        this.elements.clear();
        this.offsets.clear();
        this.colorElementOffset = -1;
        this.elementOffsetsById.clear();
        this.normalElementOffset = -1;
        this.nextOffset = 0;
    }

    public void setElement(VertexFormatElement element)
    {
        if (element.isPositionElement() && this.hasPosition())
        {
            LOGGER.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
        }
        else
        {
            this.elements.add(element);
            this.offsets.add(Integer.valueOf(this.nextOffset));
            element.setOffset(this.nextOffset);
            this.nextOffset += element.getSize();

            switch (VertexFormat.SwitchEnumUseage.field_177382_a[element.getUsage().ordinal()])
            {
                case 1:
                    this.normalElementOffset = element.getOffset();
                    break;

                case 2:
                    this.colorElementOffset = element.getOffset();
                    break;

                case 3:
                    this.elementOffsetsById.add(element.getIndex(), Integer.valueOf(element.getOffset()));
            }
        }
    }

    public boolean hasNormal()
    {
        return this.normalElementOffset >= 0;
    }

    public int getNormalOffset()
    {
        return this.normalElementOffset;
    }

    public boolean hasColor()
    {
        return this.colorElementOffset >= 0;
    }

    public int getColorOffset()
    {
        return this.colorElementOffset;
    }

    public boolean hasElementOffset(int id)
    {
        return this.elementOffsetsById.size() - 1 >= id;
    }

    public int getElementOffsetById(int id)
    {
        return ((Integer)this.elementOffsetsById.get(id)).intValue();
    }

    @Override
	public String toString()
    {
        String var1 = "format: " + this.elements.size() + " elements: ";

        for (int var2 = 0; var2 < this.elements.size(); ++var2)
        {
            var1 = var1 + ((VertexFormatElement)this.elements.get(var2)).toString();

            if (var2 != this.elements.size() - 1)
            {
                var1 = var1 + " ";
            }
        }

        return var1;
    }

    private boolean hasPosition()
    {
        Iterator var1 = this.elements.iterator();
        VertexFormatElement var2;

        do
        {
            if (!var1.hasNext())
            {
                return false;
            }

            var2 = (VertexFormatElement)var1.next();
        }
        while (!var2.isPositionElement());

        return true;
    }

    public int getNextOffset()
    {
        return this.nextOffset;
    }

    public List getElements()
    {
        return this.elements;
    }

    public int getElementCount()
    {
        return this.elements.size();
    }

    public VertexFormatElement getElement(int index)
    {
        return (VertexFormatElement)this.elements.get(index);
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
            VertexFormat var2 = (VertexFormat)p_equals_1_;
            return this.nextOffset != var2.nextOffset ? false : (!this.elements.equals(var2.elements) ? false : this.offsets.equals(var2.offsets));
        }
        else
        {
            return false;
        }
    }

    @Override
	public int hashCode()
    {
        int var1 = this.elements.hashCode();
        var1 = 31 * var1 + this.offsets.hashCode();
        var1 = 31 * var1 + this.nextOffset;
        return var1;
    }

    static final class SwitchEnumUseage
    {
        static final int[] field_177382_a = new int[VertexFormatElement.EnumUseage.values().length];

        static
        {
            try
            {
                field_177382_a[VertexFormatElement.EnumUseage.NORMAL.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_177382_a[VertexFormatElement.EnumUseage.COLOR.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_177382_a[VertexFormatElement.EnumUseage.UV.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
