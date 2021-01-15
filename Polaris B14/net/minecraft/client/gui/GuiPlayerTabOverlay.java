/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.scoreboard.IScoreObjectiveCriteria.EnumRenderType;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ 
/*     */ public class GuiPlayerTabOverlay extends Gui
/*     */ {
/*  25 */   private static final Ordering<NetworkPlayerInfo> field_175252_a = Ordering.from(new PlayerComparator(null));
/*     */   
/*     */   private final Minecraft mc;
/*     */   
/*     */   private final GuiIngame guiIngame;
/*     */   
/*     */   private IChatComponent footer;
/*     */   
/*     */   private IChatComponent header;
/*     */   
/*     */   private long lastTimeOpened;
/*     */   
/*     */   private boolean isBeingRendered;
/*     */   
/*     */   public GuiPlayerTabOverlay(Minecraft mcIn, GuiIngame guiIngameIn)
/*     */   {
/*  41 */     this.mc = mcIn;
/*  42 */     this.guiIngame = guiIngameIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
/*     */   {
/*  50 */     return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updatePlayerList(boolean willBeRendered)
/*     */   {
/*  59 */     if ((willBeRendered) && (!this.isBeingRendered))
/*     */     {
/*  61 */       this.lastTimeOpened = Minecraft.getSystemTime();
/*     */     }
/*     */     
/*  64 */     this.isBeingRendered = willBeRendered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn)
/*     */   {
/*  72 */     NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
/*  73 */     List<NetworkPlayerInfo> list = field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
/*  74 */     int i = 0;
/*  75 */     int j = 0;
/*     */     
/*  77 */     for (NetworkPlayerInfo networkplayerinfo : list)
/*     */     {
/*  79 */       int k = this.mc.fontRendererObj.getStringWidth(getPlayerName(networkplayerinfo));
/*  80 */       i = Math.max(i, k);
/*     */       
/*  82 */       if ((scoreObjectiveIn != null) && (scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS))
/*     */       {
/*  84 */         k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
/*  85 */         j = Math.max(j, k);
/*     */       }
/*     */     }
/*     */     
/*  89 */     list = list.subList(0, Math.min(list.size(), 80));
/*  90 */     int l3 = list.size();
/*  91 */     int i4 = l3;
/*     */     
/*     */ 
/*  94 */     for (int j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4)
/*     */     {
/*  96 */       j4++;
/*     */     }
/*     */     
/*  99 */     boolean flag = (this.mc.isIntegratedServerRunning()) || (this.mc.getNetHandler().getNetworkManager().getIsencrypted());
/*     */     int l;
/*     */     int l;
/* 102 */     if (scoreObjectiveIn != null) {
/*     */       int l;
/* 104 */       if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
/*     */       {
/* 106 */         l = 90;
/*     */       }
/*     */       else
/*     */       {
/* 110 */         l = j;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 115 */       l = 0;
/*     */     }
/*     */     
/* 118 */     int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
/* 119 */     int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
/* 120 */     int k1 = 10;
/* 121 */     int l1 = i1 * j4 + (j4 - 1) * 5;
/* 122 */     List<String> list1 = null;
/* 123 */     List<String> list2 = null;
/*     */     
/* 125 */     if (this.header != null)
/*     */     {
/* 127 */       list1 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);
/*     */       
/* 129 */       for (String s : list1)
/*     */       {
/* 131 */         l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s));
/*     */       }
/*     */     }
/*     */     
/* 135 */     if (this.footer != null)
/*     */     {
/* 137 */       list2 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);
/*     */       
/* 139 */       for (String s2 : list2)
/*     */       {
/* 141 */         l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s2));
/*     */       }
/*     */     }
/*     */     
/* 145 */     if (list1 != null)
/*     */     {
/* 147 */       drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
/*     */       
/* 149 */       for (String s3 : list1)
/*     */       {
/* 151 */         int i2 = this.mc.fontRendererObj.getStringWidth(s3);
/* 152 */         this.mc.fontRendererObj.drawStringWithShadow(s3, width / 2 - i2 / 2, k1, -1);
/* 153 */         k1 += this.mc.fontRendererObj.FONT_HEIGHT;
/*     */       }
/*     */       
/* 156 */       k1++;
/*     */     }
/*     */     
/* 159 */     drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);
/*     */     int l4;
/* 161 */     for (int k4 = 0; k4 < l3; k4++)
/*     */     {
/* 163 */       l4 = k4 / i4;
/* 164 */       int i5 = k4 % i4;
/* 165 */       int j2 = j1 + l4 * i1 + l4 * 5;
/* 166 */       int k2 = k1 + i5 * 9;
/* 167 */       drawRect(j2, k2, j2 + i1, k2 + 8, 553648127);
/* 168 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 169 */       GlStateManager.enableAlpha();
/* 170 */       GlStateManager.enableBlend();
/* 171 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */       
/* 173 */       if (k4 < list.size())
/*     */       {
/* 175 */         NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo)list.get(k4);
/* 176 */         String s1 = getPlayerName(networkplayerinfo1);
/* 177 */         GameProfile gameprofile = networkplayerinfo1.getGameProfile();
/*     */         
/* 179 */         if (flag)
/*     */         {
/* 181 */           EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
/* 182 */           boolean flag1 = (entityplayer != null) && (entityplayer.isWearing(net.minecraft.entity.player.EnumPlayerModelParts.CAPE)) && ((gameprofile.getName().equals("Dinnerbone")) || (gameprofile.getName().equals("Grumm")));
/* 183 */           this.mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
/* 184 */           int l2 = 8 + (flag1 ? 8 : 0);
/* 185 */           int i3 = 8 * (flag1 ? -1 : 1);
/* 186 */           Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, l2, 8, i3, 8, 8, 64.0F, 64.0F);
/*     */           
/* 188 */           if ((entityplayer != null) && (entityplayer.isWearing(net.minecraft.entity.player.EnumPlayerModelParts.HAT)))
/*     */           {
/* 190 */             int j3 = 8 + (flag1 ? 8 : 0);
/* 191 */             int k3 = 8 * (flag1 ? -1 : 1);
/* 192 */             Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, j3, 8, k3, 8, 8, 64.0F, 64.0F);
/*     */           }
/*     */           
/* 195 */           j2 += 9;
/*     */         }
/*     */         
/* 198 */         if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR)
/*     */         {
/* 200 */           s1 = EnumChatFormatting.ITALIC + s1;
/* 201 */           this.mc.fontRendererObj.drawStringWithShadow(s1, j2, k2, -1862270977);
/*     */         }
/*     */         else
/*     */         {
/* 205 */           this.mc.fontRendererObj.drawStringWithShadow(s1, j2, k2, -1);
/*     */         }
/*     */         
/* 208 */         if ((scoreObjectiveIn != null) && (networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR))
/*     */         {
/* 210 */           int k5 = j2 + i + 1;
/* 211 */           int l5 = k5 + l;
/*     */           
/* 213 */           if (l5 - k5 > 5)
/*     */           {
/* 215 */             drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
/*     */           }
/*     */         }
/*     */         
/* 219 */         drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
/*     */       }
/*     */     }
/*     */     
/* 223 */     if (list2 != null)
/*     */     {
/* 225 */       k1 = k1 + i4 * 9 + 1;
/* 226 */       drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
/*     */       
/* 228 */       for (String s4 : list2)
/*     */       {
/* 230 */         int j5 = this.mc.fontRendererObj.getStringWidth(s4);
/* 231 */         this.mc.fontRendererObj.drawStringWithShadow(s4, width / 2 - j5 / 2, k1, -1);
/* 232 */         k1 += this.mc.fontRendererObj.FONT_HEIGHT;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void drawPing(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn)
/*     */   {
/* 239 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 240 */     this.mc.getTextureManager().bindTexture(icons);
/* 241 */     int i = 0;
/* 242 */     int j = 0;
/*     */     
/* 244 */     if (networkPlayerInfoIn.getResponseTime() < 0)
/*     */     {
/* 246 */       j = 5;
/*     */     }
/* 248 */     else if (networkPlayerInfoIn.getResponseTime() < 150)
/*     */     {
/* 250 */       j = 0;
/*     */     }
/* 252 */     else if (networkPlayerInfoIn.getResponseTime() < 300)
/*     */     {
/* 254 */       j = 1;
/*     */     }
/* 256 */     else if (networkPlayerInfoIn.getResponseTime() < 600)
/*     */     {
/* 258 */       j = 2;
/*     */     }
/* 260 */     else if (networkPlayerInfoIn.getResponseTime() < 1000)
/*     */     {
/* 262 */       j = 3;
/*     */     }
/*     */     else
/*     */     {
/* 266 */       j = 4;
/*     */     }
/*     */     
/* 269 */     this.zLevel += 100.0F;
/* 270 */     drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + i * 10, 176 + j * 8, 10, 8);
/* 271 */     this.zLevel -= 100.0F;
/*     */   }
/*     */   
/*     */   private void drawScoreboardValues(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_)
/*     */   {
/* 276 */     int i = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();
/*     */     
/* 278 */     if (p_175247_1_.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
/*     */     {
/* 280 */       this.mc.getTextureManager().bindTexture(icons);
/*     */       
/* 282 */       if (this.lastTimeOpened == p_175247_6_.func_178855_p())
/*     */       {
/* 284 */         if (i < p_175247_6_.func_178835_l())
/*     */         {
/* 286 */           p_175247_6_.func_178846_a(Minecraft.getSystemTime());
/* 287 */           p_175247_6_.func_178844_b(this.guiIngame.getUpdateCounter() + 20);
/*     */         }
/* 289 */         else if (i > p_175247_6_.func_178835_l())
/*     */         {
/* 291 */           p_175247_6_.func_178846_a(Minecraft.getSystemTime());
/* 292 */           p_175247_6_.func_178844_b(this.guiIngame.getUpdateCounter() + 10);
/*     */         }
/*     */       }
/*     */       
/* 296 */       if ((Minecraft.getSystemTime() - p_175247_6_.func_178847_n() > 1000L) || (this.lastTimeOpened != p_175247_6_.func_178855_p()))
/*     */       {
/* 298 */         p_175247_6_.func_178836_b(i);
/* 299 */         p_175247_6_.func_178857_c(i);
/* 300 */         p_175247_6_.func_178846_a(Minecraft.getSystemTime());
/*     */       }
/*     */       
/* 303 */       p_175247_6_.func_178843_c(this.lastTimeOpened);
/* 304 */       p_175247_6_.func_178836_b(i);
/* 305 */       int j = MathHelper.ceiling_float_int(Math.max(i, p_175247_6_.func_178860_m()) / 2.0F);
/* 306 */       int k = Math.max(MathHelper.ceiling_float_int(i / 2), Math.max(MathHelper.ceiling_float_int(p_175247_6_.func_178860_m() / 2), 10));
/* 307 */       boolean flag = (p_175247_6_.func_178858_o() > this.guiIngame.getUpdateCounter()) && ((p_175247_6_.func_178858_o() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L);
/*     */       
/* 309 */       if (j > 0)
/*     */       {
/* 311 */         float f = Math.min((p_175247_5_ - p_175247_4_ - 4) / k, 9.0F);
/*     */         
/* 313 */         if (f > 3.0F)
/*     */         {
/* 315 */           for (int l = j; l < k; l++)
/*     */           {
/* 317 */             drawTexturedModalRect(p_175247_4_ + l * f, p_175247_2_, flag ? 25 : 16, 0, 9, 9);
/*     */           }
/*     */           
/* 320 */           for (int j1 = 0; j1 < j; j1++)
/*     */           {
/* 322 */             drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, flag ? 25 : 16, 0, 9, 9);
/*     */             
/* 324 */             if (flag)
/*     */             {
/* 326 */               if (j1 * 2 + 1 < p_175247_6_.func_178860_m())
/*     */               {
/* 328 */                 drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, 70, 0, 9, 9);
/*     */               }
/*     */               
/* 331 */               if (j1 * 2 + 1 == p_175247_6_.func_178860_m())
/*     */               {
/* 333 */                 drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, 79, 0, 9, 9);
/*     */               }
/*     */             }
/*     */             
/* 337 */             if (j1 * 2 + 1 < i)
/*     */             {
/* 339 */               drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, j1 >= 10 ? 160 : 52, 0, 9, 9);
/*     */             }
/*     */             
/* 342 */             if (j1 * 2 + 1 == i)
/*     */             {
/* 344 */               drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, j1 >= 10 ? 169 : 61, 0, 9, 9);
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 350 */           float f1 = MathHelper.clamp_float(i / 20.0F, 0.0F, 1.0F);
/* 351 */           int i1 = (int)((1.0F - f1) * 255.0F) << 16 | (int)(f1 * 255.0F) << 8;
/* 352 */           String s = i / 2.0F;
/*     */           
/* 354 */           if (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s + "hp") >= p_175247_4_)
/*     */           {
/* 356 */             s = s + "hp";
/*     */           }
/*     */           
/* 359 */           this.mc.fontRendererObj.drawStringWithShadow(s, (p_175247_5_ + p_175247_4_) / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, p_175247_2_, i1);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 365 */       String s1 = EnumChatFormatting.YELLOW + i;
/* 366 */       this.mc.fontRendererObj.drawStringWithShadow(s1, p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s1), p_175247_2_, 16777215);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setFooter(IChatComponent footerIn)
/*     */   {
/* 372 */     this.footer = footerIn;
/*     */   }
/*     */   
/*     */   public void setHeader(IChatComponent headerIn)
/*     */   {
/* 377 */     this.header = headerIn;
/*     */   }
/*     */   
/*     */   public void func_181030_a()
/*     */   {
/* 382 */     this.header = null;
/* 383 */     this.footer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class PlayerComparator
/*     */     implements java.util.Comparator<NetworkPlayerInfo>
/*     */   {
/*     */     public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_)
/*     */     {
/* 394 */       ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
/* 395 */       ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
/* 396 */       return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiPlayerTabOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */