package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentText extends ChatComponentStyle {
    private final String text;
    private static final String __OBFID = "CL_00001269";

    public ChatComponentText(String msg) {
        this.text = msg;
    }

    /**
     * Gets the text value of this ChatComponentText.  TODO: what are getUnformattedText and getUnformattedTextForChat
     * missing that made someone decide to create a third equivalent method that only ChatComponentText can implement?
     */
    public String getChatComponentText_TextValue() {
        return this.text;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    public String getUnformattedTextForChat() {
        return this.text;
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public ChatComponentText createCopy() {
        ChatComponentText var1 = new ChatComponentText(this.text);
        var1.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator var2 = this.getSiblings().iterator();

        while (var2.hasNext()) {
            IChatComponent var3 = (IChatComponent) var2.next();
            var1.appendSibling(var3.createCopy());
        }

        return var1;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatComponentText)) {
            return false;
        } else {
            ChatComponentText var2 = (ChatComponentText) p_equals_1_;
            return this.text.equals(var2.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
        }
    }

    public String toString() {
        return "TextComponent{text=\'" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }


}
