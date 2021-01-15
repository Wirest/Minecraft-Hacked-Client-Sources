package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentSelector extends ChatComponentStyle
{
    /**
     * The selector used to find the matching entities of this text component
     */
    private final String selector;

    public ChatComponentSelector(String selectorIn)
    {
        this.selector = selectorIn;
    }

    /**
     * Gets the selector of this component, in plain text.
     */
    public String getSelector()
    {
        return this.selector;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    @Override
	public String getUnformattedTextForChat()
    {
        return this.selector;
    }

    public ChatComponentSelector createCopyImpl()
    {
        ChatComponentSelector var1 = new ChatComponentSelector(this.selector);
        var1.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator var2 = this.getSiblings().iterator();

        while (var2.hasNext())
        {
            IChatComponent var3 = (IChatComponent)var2.next();
            var1.appendSibling(var3.createCopy());
        }

        return var1;
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ChatComponentSelector))
        {
            return false;
        }
        else
        {
            ChatComponentSelector var2 = (ChatComponentSelector)p_equals_1_;
            return this.selector.equals(var2.selector) && super.equals(p_equals_1_);
        }
    }

    @Override
	public String toString()
    {
        return "SelectorComponent{pattern=\'" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    @Override
	public IChatComponent createCopy()
    {
        return this.createCopyImpl();
    }
}
