package net.minecraft.util.text;

import java.util.function.Function;
import java.util.function.Supplier;

public class TextComponentKeybind extends TextComponentBase
{
    public static Function<String, Supplier<String>> field_193637_b = (p_193635_0_) ->
    {
        return () -> {
            return p_193635_0_;
        };
    };
    private final String field_193638_c;
    private Supplier<String> field_193639_d;

    public TextComponentKeybind(String p_i47521_1_)
    {
        this.field_193638_c = p_i47521_1_;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    public String getUnformattedComponentText()
    {
        if (this.field_193639_d == null)
        {
            this.field_193639_d = (Supplier)field_193637_b.apply(this.field_193638_c);
        }

        return this.field_193639_d.get();
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public TextComponentKeybind createCopy()
    {
        TextComponentKeybind textcomponentkeybind = new TextComponentKeybind(this.field_193638_c);
        textcomponentkeybind.setStyle(this.getStyle().createShallowCopy());

        for (ITextComponent itextcomponent : this.getSiblings())
        {
            textcomponentkeybind.appendSibling(itextcomponent.createCopy());
        }

        return textcomponentkeybind;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof TextComponentKeybind))
        {
            return false;
        }
        else
        {
            TextComponentKeybind textcomponentkeybind = (TextComponentKeybind)p_equals_1_;
            return this.field_193638_c.equals(textcomponentkeybind.field_193638_c) && super.equals(p_equals_1_);
        }
    }

    public String toString()
    {
        return "KeybindComponent{keybind='" + this.field_193638_c + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }

    public String func_193633_h()
    {
        return this.field_193638_c;
    }
}
