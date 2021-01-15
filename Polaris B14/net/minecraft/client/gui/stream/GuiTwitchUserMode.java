/*     */ package net.minecraft.client.gui.stream;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.stream.IStream;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import tv.twitch.chat.ChatUserInfo;
/*     */ import tv.twitch.chat.ChatUserMode;
/*     */ import tv.twitch.chat.ChatUserSubscription;
/*     */ 
/*     */ public class GuiTwitchUserMode extends net.minecraft.client.gui.GuiScreen
/*     */ {
/*  21 */   private static final EnumChatFormatting field_152331_a = EnumChatFormatting.DARK_GREEN;
/*  22 */   private static final EnumChatFormatting field_152335_f = EnumChatFormatting.RED;
/*  23 */   private static final EnumChatFormatting field_152336_g = EnumChatFormatting.DARK_PURPLE;
/*     */   private final ChatUserInfo field_152337_h;
/*     */   private final IChatComponent field_152338_i;
/*  26 */   private final List<IChatComponent> field_152332_r = Lists.newArrayList();
/*     */   private final IStream stream;
/*     */   private int field_152334_t;
/*     */   
/*     */   public GuiTwitchUserMode(IStream streamIn, ChatUserInfo p_i1064_2_)
/*     */   {
/*  32 */     this.stream = streamIn;
/*  33 */     this.field_152337_h = p_i1064_2_;
/*  34 */     this.field_152338_i = new ChatComponentText(p_i1064_2_.displayName);
/*  35 */     this.field_152332_r.addAll(func_152328_a(p_i1064_2_.modes, p_i1064_2_.subscriptions, streamIn));
/*     */   }
/*     */   
/*     */   public static List<IChatComponent> func_152328_a(Set<ChatUserMode> p_152328_0_, Set<ChatUserSubscription> p_152328_1_, IStream p_152328_2_)
/*     */   {
/*  40 */     String s = p_152328_2_ == null ? null : p_152328_2_.func_152921_C();
/*  41 */     boolean flag = (p_152328_2_ != null) && (p_152328_2_.func_152927_B());
/*  42 */     List<IChatComponent> list = Lists.newArrayList();
/*     */     
/*  44 */     for (ChatUserMode chatusermode : p_152328_0_)
/*     */     {
/*  46 */       IChatComponent ichatcomponent = func_152329_a(chatusermode, s, flag);
/*     */       
/*  48 */       if (ichatcomponent != null)
/*     */       {
/*  50 */         IChatComponent ichatcomponent1 = new ChatComponentText("- ");
/*  51 */         ichatcomponent1.appendSibling(ichatcomponent);
/*  52 */         list.add(ichatcomponent1);
/*     */       }
/*     */     }
/*     */     
/*  56 */     for (ChatUserSubscription chatusersubscription : p_152328_1_)
/*     */     {
/*  58 */       IChatComponent ichatcomponent2 = func_152330_a(chatusersubscription, s, flag);
/*     */       
/*  60 */       if (ichatcomponent2 != null)
/*     */       {
/*  62 */         IChatComponent ichatcomponent3 = new ChatComponentText("- ");
/*  63 */         ichatcomponent3.appendSibling(ichatcomponent2);
/*  64 */         list.add(ichatcomponent3);
/*     */       }
/*     */     }
/*     */     
/*  68 */     return list;
/*     */   }
/*     */   
/*     */   public static IChatComponent func_152330_a(ChatUserSubscription p_152330_0_, String p_152330_1_, boolean p_152330_2_)
/*     */   {
/*  73 */     IChatComponent ichatcomponent = null;
/*     */     
/*  75 */     if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER)
/*     */     {
/*  77 */       if (p_152330_1_ == null)
/*     */       {
/*  79 */         ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber", new Object[0]);
/*     */       }
/*  81 */       else if (p_152330_2_)
/*     */       {
/*  83 */         ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber.self", new Object[0]);
/*     */       }
/*     */       else
/*     */       {
/*  87 */         ichatcomponent = new ChatComponentTranslation("stream.user.subscription.subscriber.other", new Object[] { p_152330_1_ });
/*     */       }
/*     */       
/*  90 */       ichatcomponent.getChatStyle().setColor(field_152331_a);
/*     */     }
/*  92 */     else if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_TURBO)
/*     */     {
/*  94 */       ichatcomponent = new ChatComponentTranslation("stream.user.subscription.turbo", new Object[0]);
/*  95 */       ichatcomponent.getChatStyle().setColor(field_152336_g);
/*     */     }
/*     */     
/*  98 */     return ichatcomponent;
/*     */   }
/*     */   
/*     */   public static IChatComponent func_152329_a(ChatUserMode p_152329_0_, String p_152329_1_, boolean p_152329_2_)
/*     */   {
/* 103 */     IChatComponent ichatcomponent = null;
/*     */     
/* 105 */     if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR)
/*     */     {
/* 107 */       ichatcomponent = new ChatComponentTranslation("stream.user.mode.administrator", new Object[0]);
/* 108 */       ichatcomponent.getChatStyle().setColor(field_152336_g);
/*     */     }
/* 110 */     else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BANNED)
/*     */     {
/* 112 */       if (p_152329_1_ == null)
/*     */       {
/* 114 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned", new Object[0]);
/*     */       }
/* 116 */       else if (p_152329_2_)
/*     */       {
/* 118 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned.self", new Object[0]);
/*     */       }
/*     */       else
/*     */       {
/* 122 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.banned.other", new Object[] { p_152329_1_ });
/*     */       }
/*     */       
/* 125 */       ichatcomponent.getChatStyle().setColor(field_152335_f);
/*     */     }
/* 127 */     else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER)
/*     */     {
/* 129 */       if (p_152329_1_ == null)
/*     */       {
/* 131 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster", new Object[0]);
/*     */       }
/* 133 */       else if (p_152329_2_)
/*     */       {
/* 135 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster.self", new Object[0]);
/*     */       }
/*     */       else
/*     */       {
/* 139 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.broadcaster.other", new Object[0]);
/*     */       }
/*     */       
/* 142 */       ichatcomponent.getChatStyle().setColor(field_152331_a);
/*     */     }
/* 144 */     else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_MODERATOR)
/*     */     {
/* 146 */       if (p_152329_1_ == null)
/*     */       {
/* 148 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator", new Object[0]);
/*     */       }
/* 150 */       else if (p_152329_2_)
/*     */       {
/* 152 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator.self", new Object[0]);
/*     */       }
/*     */       else
/*     */       {
/* 156 */         ichatcomponent = new ChatComponentTranslation("stream.user.mode.moderator.other", new Object[] { p_152329_1_ });
/*     */       }
/*     */       
/* 159 */       ichatcomponent.getChatStyle().setColor(field_152331_a);
/*     */     }
/* 161 */     else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_STAFF)
/*     */     {
/* 163 */       ichatcomponent = new ChatComponentTranslation("stream.user.mode.staff", new Object[0]);
/* 164 */       ichatcomponent.getChatStyle().setColor(field_152336_g);
/*     */     }
/*     */     
/* 167 */     return ichatcomponent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/* 176 */     int i = width / 3;
/* 177 */     int j = i - 130;
/* 178 */     this.buttonList.add(new GuiButton(1, i * 0 + j / 2, height - 70, 130, 20, I18n.format("stream.userinfo.timeout", new Object[0])));
/* 179 */     this.buttonList.add(new GuiButton(0, i * 1 + j / 2, height - 70, 130, 20, I18n.format("stream.userinfo.ban", new Object[0])));
/* 180 */     this.buttonList.add(new GuiButton(2, i * 2 + j / 2, height - 70, 130, 20, I18n.format("stream.userinfo.mod", new Object[0])));
/* 181 */     this.buttonList.add(new GuiButton(5, i * 0 + j / 2, height - 45, 130, 20, I18n.format("gui.cancel", new Object[0])));
/* 182 */     this.buttonList.add(new GuiButton(3, i * 1 + j / 2, height - 45, 130, 20, I18n.format("stream.userinfo.unban", new Object[0])));
/* 183 */     this.buttonList.add(new GuiButton(4, i * 2 + j / 2, height - 45, 130, 20, I18n.format("stream.userinfo.unmod", new Object[0])));
/* 184 */     int k = 0;
/*     */     
/* 186 */     for (IChatComponent ichatcomponent : this.field_152332_r)
/*     */     {
/* 188 */       k = Math.max(k, this.fontRendererObj.getStringWidth(ichatcomponent.getFormattedText()));
/*     */     }
/*     */     
/* 191 */     this.field_152334_t = (width / 2 - k / 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws java.io.IOException
/*     */   {
/* 199 */     if (button.enabled)
/*     */     {
/* 201 */       if (button.id == 0)
/*     */       {
/* 203 */         this.stream.func_152917_b("/ban " + this.field_152337_h.displayName);
/*     */       }
/* 205 */       else if (button.id == 3)
/*     */       {
/* 207 */         this.stream.func_152917_b("/unban " + this.field_152337_h.displayName);
/*     */       }
/* 209 */       else if (button.id == 2)
/*     */       {
/* 211 */         this.stream.func_152917_b("/mod " + this.field_152337_h.displayName);
/*     */       }
/* 213 */       else if (button.id == 4)
/*     */       {
/* 215 */         this.stream.func_152917_b("/unmod " + this.field_152337_h.displayName);
/*     */       }
/* 217 */       else if (button.id == 1)
/*     */       {
/* 219 */         this.stream.func_152917_b("/timeout " + this.field_152337_h.displayName);
/*     */       }
/*     */       
/* 222 */       this.mc.displayGuiScreen(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 231 */     drawDefaultBackground();
/* 232 */     drawCenteredString(this.fontRendererObj, this.field_152338_i.getUnformattedText(), width / 2, 70, 16777215);
/* 233 */     int i = 80;
/*     */     
/* 235 */     for (IChatComponent ichatcomponent : this.field_152332_r)
/*     */     {
/* 237 */       drawString(this.fontRendererObj, ichatcomponent.getFormattedText(), this.field_152334_t, i, 16777215);
/* 238 */       i += this.fontRendererObj.FONT_HEIGHT;
/*     */     }
/*     */     
/* 241 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\stream\GuiTwitchUserMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */