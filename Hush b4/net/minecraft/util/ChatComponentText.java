// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentText extends ChatComponentStyle
{
    private final String text;
    
    public ChatComponentText(final String msg) {
        this.text = msg;
    }
    
    public String getChatComponentText_TextValue() {
        return this.text;
    }
    
    @Override
    public String getUnformattedTextForChat() {
        return this.text;
    }
    
    @Override
    public ChatComponentText createCopy() {
        final ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
        chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());
        for (final IChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponenttext.appendSibling(ichatcomponent.createCopy());
        }
        return chatcomponenttext;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentText)) {
            return false;
        }
        final ChatComponentText chatcomponenttext = (ChatComponentText)p_equals_1_;
        return this.text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
    }
    
    @Override
    public String toString() {
        return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}
