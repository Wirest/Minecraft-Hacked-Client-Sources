// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.util.ChatComponentText;
import java.util.List;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.Minecraft;

public class GuiUtilRenderComponents
{
    public static String func_178909_a(final String p_178909_0_, final boolean p_178909_1_) {
        return (!p_178909_1_ && !Minecraft.getMinecraft().gameSettings.chatColours) ? EnumChatFormatting.getTextWithoutFormattingCodes(p_178909_0_) : p_178909_0_;
    }
    
    public static List<IChatComponent> func_178908_a(final IChatComponent p_178908_0_, final int p_178908_1_, final FontRenderer p_178908_2_, final boolean p_178908_3_, final boolean p_178908_4_) {
        int i = 0;
        IChatComponent ichatcomponent = new ChatComponentText("");
        final List<IChatComponent> list = (List<IChatComponent>)Lists.newArrayList();
        final List<IChatComponent> list2 = (List<IChatComponent>)Lists.newArrayList((Iterable<?>)p_178908_0_);
        for (int j = 0; j < list2.size(); ++j) {
            final IChatComponent ichatcomponent2 = list2.get(j);
            String s = ichatcomponent2.getUnformattedTextForChat();
            boolean flag = false;
            if (s.contains("\n")) {
                final int k = s.indexOf(10);
                final String s2 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                final ChatComponentText chatcomponenttext = new ChatComponentText(s2);
                chatcomponenttext.setChatStyle(ichatcomponent2.getChatStyle().createShallowCopy());
                list2.add(j + 1, chatcomponenttext);
                flag = true;
            }
            final String s3 = func_178909_a(String.valueOf(ichatcomponent2.getChatStyle().getFormattingCode()) + s, p_178908_4_);
            final String s4 = s3.endsWith("\n") ? s3.substring(0, s3.length() - 1) : s3;
            int i2 = p_178908_2_.getStringWidth(s4);
            ChatComponentText chatcomponenttext2 = new ChatComponentText(s4);
            chatcomponenttext2.setChatStyle(ichatcomponent2.getChatStyle().createShallowCopy());
            if (i + i2 > p_178908_1_) {
                String s5 = p_178908_2_.trimStringToWidth(s3, p_178908_1_ - i, false);
                String s6 = (s5.length() < s3.length()) ? s3.substring(s5.length()) : null;
                if (s6 != null && s6.length() > 0) {
                    int l = s5.lastIndexOf(" ");
                    if (l >= 0 && p_178908_2_.getStringWidth(s3.substring(0, l)) > 0) {
                        s5 = s3.substring(0, l);
                        if (p_178908_3_) {
                            ++l;
                        }
                        s6 = s3.substring(l);
                    }
                    else if (i > 0 && !s3.contains(" ")) {
                        s5 = "";
                        s6 = s3;
                    }
                    final ChatComponentText chatcomponenttext3 = new ChatComponentText(s6);
                    chatcomponenttext3.setChatStyle(ichatcomponent2.getChatStyle().createShallowCopy());
                    list2.add(j + 1, chatcomponenttext3);
                }
                i2 = p_178908_2_.getStringWidth(s5);
                chatcomponenttext2 = new ChatComponentText(s5);
                chatcomponenttext2.setChatStyle(ichatcomponent2.getChatStyle().createShallowCopy());
                flag = true;
            }
            if (i + i2 <= p_178908_1_) {
                i += i2;
                ichatcomponent.appendSibling(chatcomponenttext2);
            }
            else {
                flag = true;
            }
            if (flag) {
                list.add(ichatcomponent);
                i = 0;
                ichatcomponent = new ChatComponentText("");
            }
        }
        list.add(ichatcomponent);
        return list;
    }
}
