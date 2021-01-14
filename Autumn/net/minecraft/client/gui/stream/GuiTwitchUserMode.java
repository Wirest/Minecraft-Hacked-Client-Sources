package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.FontRenderer;
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
   private static final EnumChatFormatting field_152331_a;
   private static final EnumChatFormatting field_152335_f;
   private static final EnumChatFormatting field_152336_g;
   private final ChatUserInfo field_152337_h;
   private final IChatComponent field_152338_i;
   private final List field_152332_r = Lists.newArrayList();
   private final IStream stream;
   private int field_152334_t;

   public GuiTwitchUserMode(IStream streamIn, ChatUserInfo p_i1064_2_) {
      this.stream = streamIn;
      this.field_152337_h = p_i1064_2_;
      this.field_152338_i = new ChatComponentText(p_i1064_2_.displayName);
      this.field_152332_r.addAll(func_152328_a(p_i1064_2_.modes, p_i1064_2_.subscriptions, streamIn));
   }

   public static List func_152328_a(Set p_152328_0_, Set p_152328_1_, IStream p_152328_2_) {
      String s = p_152328_2_ == null ? null : p_152328_2_.func_152921_C();
      boolean flag = p_152328_2_ != null && p_152328_2_.func_152927_B();
      List list = Lists.newArrayList();
      Iterator var6 = p_152328_0_.iterator();

      IChatComponent ichatcomponent2;
      ChatComponentText ichatcomponent3;
      while(var6.hasNext()) {
         ChatUserMode chatusermode = (ChatUserMode)var6.next();
         ichatcomponent2 = func_152329_a(chatusermode, s, flag);
         if (ichatcomponent2 != null) {
            ichatcomponent3 = new ChatComponentText("- ");
            ichatcomponent3.appendSibling(ichatcomponent2);
            list.add(ichatcomponent3);
         }
      }

      var6 = p_152328_1_.iterator();

      while(var6.hasNext()) {
         ChatUserSubscription chatusersubscription = (ChatUserSubscription)var6.next();
         ichatcomponent2 = func_152330_a(chatusersubscription, s, flag);
         if (ichatcomponent2 != null) {
            ichatcomponent3 = new ChatComponentText("- ");
            ichatcomponent3.appendSibling(ichatcomponent2);
            list.add(ichatcomponent3);
         }
      }

      return list;
   }

   public static IChatComponent func_152330_a(ChatUserSubscription p_152330_0_, String p_152330_1_, boolean p_152330_2_) {
      IChatComponent ichatcomponent = null;
      if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) {
         if (p_152330_1_ == null) {
            ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber", new Object[0]);
         } else if (p_152330_2_) {
            ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber.self", new Object[0]);
         } else {
            ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber.other", new Object[]{p_152330_1_});
         }

         ichatcomponent.getChatStyle().setColor(field_152331_a);
      } else if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_TURBO) {
         ichatcomponent = new ChatComponentTranslation("stream.user.subscription.turbo", new Object[0]);
         ichatcomponent.getChatStyle().setColor(field_152336_g);
      }

      return ichatcomponent;
   }

   public static IChatComponent func_152329_a(ChatUserMode p_152329_0_, String p_152329_1_, boolean p_152329_2_) {
      IChatComponent ichatcomponent = null;
      if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) {
         ichatcomponent = new ChatComponentTranslation("stream.user.mode.administrator", new Object[0]);
         ichatcomponent.getChatStyle().setColor(field_152336_g);
      } else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BANNED) {
         if (p_152329_1_ == null) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned", new Object[0]);
         } else if (p_152329_2_) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned.self", new Object[0]);
         } else {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned.other", new Object[]{p_152329_1_});
         }

         ichatcomponent.getChatStyle().setColor(field_152335_f);
      } else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) {
         if (p_152329_1_ == null) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster", new Object[0]);
         } else if (p_152329_2_) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster.self", new Object[0]);
         } else {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster.other", new Object[0]);
         }

         ichatcomponent.getChatStyle().setColor(field_152331_a);
      } else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) {
         if (p_152329_1_ == null) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator", new Object[0]);
         } else if (p_152329_2_) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator.self", new Object[0]);
         } else {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator.other", new Object[]{p_152329_1_});
         }

         ichatcomponent.getChatStyle().setColor(field_152331_a);
      } else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_STAFF) {
         ichatcomponent = new ChatComponentTranslation("stream.user.mode.staff", new Object[0]);
         ichatcomponent.getChatStyle().setColor(field_152336_g);
      }

      return ichatcomponent;
   }

   public void initGui() {
      int i = this.width / 3;
      int j = i - 130;
      this.buttonList.add(new GuiButton(1, i * 0 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.timeout")));
      this.buttonList.add(new GuiButton(0, i * 1 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.ban")));
      this.buttonList.add(new GuiButton(2, i * 2 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.mod")));
      this.buttonList.add(new GuiButton(5, i * 0 + j / 2, this.height - 45, 130, 20, I18n.format("gui.cancel")));
      this.buttonList.add(new GuiButton(3, i * 1 + j / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unban")));
      this.buttonList.add(new GuiButton(4, i * 2 + j / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unmod")));
      int k = 0;

      IChatComponent ichatcomponent;
      for(Iterator var4 = this.field_152332_r.iterator(); var4.hasNext(); k = Math.max(k, this.fontRendererObj.getStringWidth(ichatcomponent.getFormattedText()))) {
         ichatcomponent = (IChatComponent)var4.next();
      }

      this.field_152334_t = this.width / 2 - k / 2;
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.enabled) {
         if (button.id == 0) {
            this.stream.func_152917_b("/ban " + this.field_152337_h.displayName);
         } else if (button.id == 3) {
            this.stream.func_152917_b("/unban " + this.field_152337_h.displayName);
         } else if (button.id == 2) {
            this.stream.func_152917_b("/mod " + this.field_152337_h.displayName);
         } else if (button.id == 4) {
            this.stream.func_152917_b("/unmod " + this.field_152337_h.displayName);
         } else if (button.id == 1) {
            this.stream.func_152917_b("/timeout " + this.field_152337_h.displayName);
         }

         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.field_152338_i.getUnformattedText(), this.width / 2, 70, 16777215);
      int i = 80;

      for(Iterator var5 = this.field_152332_r.iterator(); var5.hasNext(); i += 9) {
         IChatComponent ichatcomponent = (IChatComponent)var5.next();
         this.drawString(this.fontRendererObj, ichatcomponent.getFormattedText(), this.field_152334_t, i, 16777215);
         FontRenderer var10001 = this.fontRendererObj;
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   static {
      field_152331_a = EnumChatFormatting.DARK_GREEN;
      field_152335_f = EnumChatFormatting.RED;
      field_152336_g = EnumChatFormatting.DARK_PURPLE;
   }
}
