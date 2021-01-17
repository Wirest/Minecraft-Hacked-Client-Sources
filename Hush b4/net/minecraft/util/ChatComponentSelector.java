// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentSelector extends ChatComponentStyle
{
    private final String selector;
    
    public ChatComponentSelector(final String selectorIn) {
        this.selector = selectorIn;
    }
    
    public String getSelector() {
        return this.selector;
    }
    
    @Override
    public String getUnformattedTextForChat() {
        return this.selector;
    }
    
    @Override
    public ChatComponentSelector createCopy() {
        final ChatComponentSelector chatcomponentselector = new ChatComponentSelector(this.selector);
        chatcomponentselector.setChatStyle(this.getChatStyle().createShallowCopy());
        for (final IChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponentselector.appendSibling(ichatcomponent.createCopy());
        }
        return chatcomponentselector;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentSelector)) {
            return false;
        }
        final ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_equals_1_;
        return this.selector.equals(chatcomponentselector.selector) && super.equals(p_equals_1_);
    }
    
    @Override
    public String toString() {
        return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}
