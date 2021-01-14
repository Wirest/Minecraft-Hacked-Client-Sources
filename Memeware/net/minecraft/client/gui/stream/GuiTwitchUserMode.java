package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatUserMode;
import tv.twitch.chat.ChatUserSubscription;

public class GuiTwitchUserMode extends GuiScreen {
    private static final EnumChatFormatting field_152331_a = EnumChatFormatting.DARK_GREEN;
    private static final EnumChatFormatting field_152335_f = EnumChatFormatting.RED;
    private static final EnumChatFormatting field_152336_g = EnumChatFormatting.DARK_PURPLE;
    private final ChatUserInfo field_152337_h;
    private final IChatComponent field_152338_i;
    private final List field_152332_r = Lists.newArrayList();
    private final IStream field_152333_s;
    private int field_152334_t;
    private static final String __OBFID = "CL_00001837";

    public GuiTwitchUserMode(IStream p_i1064_1_, ChatUserInfo p_i1064_2_) {
        this.field_152333_s = p_i1064_1_;
        this.field_152337_h = p_i1064_2_;
        this.field_152338_i = new ChatComponentText(p_i1064_2_.displayName);
        this.field_152332_r.addAll(func_152328_a(p_i1064_2_.modes, p_i1064_2_.subscriptions, p_i1064_1_));
    }

    public static List func_152328_a(Set p_152328_0_, Set p_152328_1_, IStream p_152328_2_) {
        String var3 = p_152328_2_ == null ? null : p_152328_2_.func_152921_C();
        boolean var4 = p_152328_2_ != null && p_152328_2_.func_152927_B();
        ArrayList var5 = Lists.newArrayList();
        Iterator var6 = p_152328_0_.iterator();
        IChatComponent var8;
        ChatComponentText var9;

        while (var6.hasNext()) {
            ChatUserMode var7 = (ChatUserMode) var6.next();
            var8 = func_152329_a(var7, var3, var4);

            if (var8 != null) {
                var9 = new ChatComponentText("- ");
                var9.appendSibling(var8);
                var5.add(var9);
            }
        }

        var6 = p_152328_1_.iterator();

        while (var6.hasNext()) {
            ChatUserSubscription var10 = (ChatUserSubscription) var6.next();
            var8 = func_152330_a(var10, var3, var4);

            if (var8 != null) {
                var9 = new ChatComponentText("- ");
                var9.appendSibling(var8);
                var5.add(var9);
            }
        }

        return var5;
    }

    public static IChatComponent func_152330_a(ChatUserSubscription p_152330_0_, String p_152330_1_, boolean p_152330_2_) {
        ChatComponentTranslation var3 = null;

        if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) {
            if (p_152330_1_ == null) {
                var3 = new ChatComponentTranslation("stream.user.subscription.subscriber", new Object[0]);
            } else if (p_152330_2_) {
                var3 = new ChatComponentTranslation("stream.user.subscription.subscriber.self", new Object[0]);
            } else {
                var3 = new ChatComponentTranslation("stream.user.subscription.subscriber.other", new Object[]{p_152330_1_});
            }

            var3.getChatStyle().setColor(field_152331_a);
        } else if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_TURBO) {
            var3 = new ChatComponentTranslation("stream.user.subscription.turbo", new Object[0]);
            var3.getChatStyle().setColor(field_152336_g);
        }

        return var3;
    }

    public static IChatComponent func_152329_a(ChatUserMode p_152329_0_, String p_152329_1_, boolean p_152329_2_) {
        ChatComponentTranslation var3 = null;

        if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) {
            var3 = new ChatComponentTranslation("stream.user.mode.administrator", new Object[0]);
            var3.getChatStyle().setColor(field_152336_g);
        } else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BANNED) {
            if (p_152329_1_ == null) {
                var3 = new ChatComponentTranslation("stream.user.mode.banned", new Object[0]);
            } else if (p_152329_2_) {
                var3 = new ChatComponentTranslation("stream.user.mode.banned.self", new Object[0]);
            } else {
                var3 = new ChatComponentTranslation("stream.user.mode.banned.other", new Object[]{p_152329_1_});
            }

            var3.getChatStyle().setColor(field_152335_f);
        } else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) {
            if (p_152329_1_ == null) {
                var3 = new ChatComponentTranslation("stream.user.mode.broadcaster", new Object[0]);
            } else if (p_152329_2_) {
                var3 = new ChatComponentTranslation("stream.user.mode.broadcaster.self", new Object[0]);
            } else {
                var3 = new ChatComponentTranslation("stream.user.mode.broadcaster.other", new Object[0]);
            }

            var3.getChatStyle().setColor(field_152331_a);
        } else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) {
            if (p_152329_1_ == null) {
                var3 = new ChatComponentTranslation("stream.user.mode.moderator", new Object[0]);
            } else if (p_152329_2_) {
                var3 = new ChatComponentTranslation("stream.user.mode.moderator.self", new Object[0]);
            } else {
                var3 = new ChatComponentTranslation("stream.user.mode.moderator.other", new Object[]{p_152329_1_});
            }

            var3.getChatStyle().setColor(field_152331_a);
        } else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_STAFF) {
            var3 = new ChatComponentTranslation("stream.user.mode.staff", new Object[0]);
            var3.getChatStyle().setColor(field_152336_g);
        }

        return var3;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        int var1 = this.width / 3;
        int var2 = var1 - 130;
        this.buttonList.add(new GuiButton(1, var1 * 0 + var2 / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.timeout", new Object[0])));
        this.buttonList.add(new GuiButton(0, var1 * 1 + var2 / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.ban", new Object[0])));
        this.buttonList.add(new GuiButton(2, var1 * 2 + var2 / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.mod", new Object[0])));
        this.buttonList.add(new GuiButton(5, var1 * 0 + var2 / 2, this.height - 45, 130, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(3, var1 * 1 + var2 / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unban", new Object[0])));
        this.buttonList.add(new GuiButton(4, var1 * 2 + var2 / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unmod", new Object[0])));
        int var3 = 0;
        IChatComponent var5;

        for (Iterator var4 = this.field_152332_r.iterator(); var4.hasNext(); var3 = Math.max(var3, this.fontRendererObj.getStringWidth(var5.getFormattedText()))) {
            var5 = (IChatComponent) var4.next();
        }

        this.field_152334_t = this.width / 2 - var3 / 2;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.field_152333_s.func_152917_b("/ban " + this.field_152337_h.displayName);
            } else if (button.id == 3) {
                this.field_152333_s.func_152917_b("/unban " + this.field_152337_h.displayName);
            } else if (button.id == 2) {
                this.field_152333_s.func_152917_b("/mod " + this.field_152337_h.displayName);
            } else if (button.id == 4) {
                this.field_152333_s.func_152917_b("/unmod " + this.field_152337_h.displayName);
            } else if (button.id == 1) {
                this.field_152333_s.func_152917_b("/timeout " + this.field_152337_h.displayName);
            }

            this.mc.displayGuiScreen((GuiScreen) null);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_152338_i.getUnformattedText(), this.width / 2, 70, 16777215);
        int var4 = 80;

        for (Iterator var5 = this.field_152332_r.iterator(); var5.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT) {
            IChatComponent var6 = (IChatComponent) var5.next();
            this.drawString(this.fontRendererObj, var6.getFormattedText(), this.field_152334_t, var4, 16777215);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
