// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.stream;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import java.util.Iterator;
import java.util.Collection;
import tv.twitch.chat.ChatUserSubscription;
import tv.twitch.chat.ChatUserMode;
import java.util.Set;
import net.minecraft.util.ChatComponentText;
import com.google.common.collect.Lists;
import net.minecraft.client.stream.IStream;
import java.util.List;
import net.minecraft.util.IChatComponent;
import tv.twitch.chat.ChatUserInfo;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiScreen;

public class GuiTwitchUserMode extends GuiScreen
{
    private static final EnumChatFormatting field_152331_a;
    private static final EnumChatFormatting field_152335_f;
    private static final EnumChatFormatting field_152336_g;
    private final ChatUserInfo field_152337_h;
    private final IChatComponent field_152338_i;
    private final List<IChatComponent> field_152332_r;
    private final IStream stream;
    private int field_152334_t;
    
    static {
        field_152331_a = EnumChatFormatting.DARK_GREEN;
        field_152335_f = EnumChatFormatting.RED;
        field_152336_g = EnumChatFormatting.DARK_PURPLE;
    }
    
    public GuiTwitchUserMode(final IStream streamIn, final ChatUserInfo p_i1064_2_) {
        this.field_152332_r = (List<IChatComponent>)Lists.newArrayList();
        this.stream = streamIn;
        this.field_152337_h = p_i1064_2_;
        this.field_152338_i = new ChatComponentText(p_i1064_2_.displayName);
        this.field_152332_r.addAll(func_152328_a(p_i1064_2_.modes, p_i1064_2_.subscriptions, streamIn));
    }
    
    public static List<IChatComponent> func_152328_a(final Set<ChatUserMode> p_152328_0_, final Set<ChatUserSubscription> p_152328_1_, final IStream p_152328_2_) {
        final String s = (p_152328_2_ == null) ? null : p_152328_2_.func_152921_C();
        final boolean flag = p_152328_2_ != null && p_152328_2_.func_152927_B();
        final List<IChatComponent> list = (List<IChatComponent>)Lists.newArrayList();
        for (final ChatUserMode chatusermode : p_152328_0_) {
            final IChatComponent ichatcomponent = func_152329_a(chatusermode, s, flag);
            if (ichatcomponent != null) {
                final IChatComponent ichatcomponent2 = new ChatComponentText("- ");
                ichatcomponent2.appendSibling(ichatcomponent);
                list.add(ichatcomponent2);
            }
        }
        for (final ChatUserSubscription chatusersubscription : p_152328_1_) {
            final IChatComponent ichatcomponent3 = func_152330_a(chatusersubscription, s, flag);
            if (ichatcomponent3 != null) {
                final IChatComponent ichatcomponent4 = new ChatComponentText("- ");
                ichatcomponent4.appendSibling(ichatcomponent3);
                list.add(ichatcomponent4);
            }
        }
        return list;
    }
    
    public static IChatComponent func_152330_a(final ChatUserSubscription p_152330_0_, final String p_152330_1_, final boolean p_152330_2_) {
        IChatComponent ichatcomponent = null;
        if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) {
            if (p_152330_1_ == null) {
                ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber", new Object[0]);
            }
            else if (p_152330_2_) {
                ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber.self", new Object[0]);
            }
            else {
                ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber.other", new Object[] { p_152330_1_ });
            }
            ichatcomponent.getChatStyle().setColor(GuiTwitchUserMode.field_152331_a);
        }
        else if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_TURBO) {
            ichatcomponent = new ChatComponentTranslation("stream.user.subscription.turbo", new Object[0]);
            ichatcomponent.getChatStyle().setColor(GuiTwitchUserMode.field_152336_g);
        }
        return ichatcomponent;
    }
    
    public static IChatComponent func_152329_a(final ChatUserMode p_152329_0_, final String p_152329_1_, final boolean p_152329_2_) {
        IChatComponent ichatcomponent = null;
        if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.administrator", new Object[0]);
            ichatcomponent.getChatStyle().setColor(GuiTwitchUserMode.field_152336_g);
        }
        else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BANNED) {
            if (p_152329_1_ == null) {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned", new Object[0]);
            }
            else if (p_152329_2_) {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned.self", new Object[0]);
            }
            else {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned.other", new Object[] { p_152329_1_ });
            }
            ichatcomponent.getChatStyle().setColor(GuiTwitchUserMode.field_152335_f);
        }
        else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) {
            if (p_152329_1_ == null) {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster", new Object[0]);
            }
            else if (p_152329_2_) {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster.self", new Object[0]);
            }
            else {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster.other", new Object[0]);
            }
            ichatcomponent.getChatStyle().setColor(GuiTwitchUserMode.field_152331_a);
        }
        else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) {
            if (p_152329_1_ == null) {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator", new Object[0]);
            }
            else if (p_152329_2_) {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator.self", new Object[0]);
            }
            else {
                ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator.other", new Object[] { p_152329_1_ });
            }
            ichatcomponent.getChatStyle().setColor(GuiTwitchUserMode.field_152331_a);
        }
        else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_STAFF) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.staff", new Object[0]);
            ichatcomponent.getChatStyle().setColor(GuiTwitchUserMode.field_152336_g);
        }
        return ichatcomponent;
    }
    
    @Override
    public void initGui() {
        final int i = this.width / 3;
        final int j = i - 130;
        this.buttonList.add(new GuiButton(1, i * 0 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.timeout", new Object[0])));
        this.buttonList.add(new GuiButton(0, i * 1 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.ban", new Object[0])));
        this.buttonList.add(new GuiButton(2, i * 2 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.mod", new Object[0])));
        this.buttonList.add(new GuiButton(5, i * 0 + j / 2, this.height - 45, 130, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(3, i * 1 + j / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unban", new Object[0])));
        this.buttonList.add(new GuiButton(4, i * 2 + j / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unmod", new Object[0])));
        int k = 0;
        for (final IChatComponent ichatcomponent : this.field_152332_r) {
            k = Math.max(k, this.fontRendererObj.getStringWidth(ichatcomponent.getFormattedText()));
        }
        this.field_152334_t = this.width / 2 - k / 2;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.stream.func_152917_b("/ban " + this.field_152337_h.displayName);
            }
            else if (button.id == 3) {
                this.stream.func_152917_b("/unban " + this.field_152337_h.displayName);
            }
            else if (button.id == 2) {
                this.stream.func_152917_b("/mod " + this.field_152337_h.displayName);
            }
            else if (button.id == 4) {
                this.stream.func_152917_b("/unmod " + this.field_152337_h.displayName);
            }
            else if (button.id == 1) {
                this.stream.func_152917_b("/timeout " + this.field_152337_h.displayName);
            }
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_152338_i.getUnformattedText(), this.width / 2, 70, 16777215);
        int i = 80;
        for (final IChatComponent ichatcomponent : this.field_152332_r) {
            this.drawString(this.fontRendererObj, ichatcomponent.getFormattedText(), this.field_152334_t, i, 16777215);
            i += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
