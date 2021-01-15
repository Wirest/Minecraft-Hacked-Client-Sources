/*     */ package net.minecraft.client.gui.achievement;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.stats.Achievement;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatFileWriter;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiAchievements extends GuiScreen implements net.minecraft.client.gui.IProgressMeter
/*     */ {
/*  28 */   private static final int field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
/*  29 */   private static final int field_146571_z = AchievementList.minDisplayRow * 24 - 112;
/*  30 */   private static final int field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
/*  31 */   private static final int field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
/*  32 */   private static final ResourceLocation ACHIEVEMENT_BACKGROUND = new ResourceLocation("textures/gui/achievement/achievement_background.png");
/*     */   protected GuiScreen parentScreen;
/*  34 */   protected int field_146555_f = 256;
/*  35 */   protected int field_146557_g = 202;
/*     */   protected int field_146563_h;
/*     */   protected int field_146564_i;
/*  38 */   protected float field_146570_r = 1.0F;
/*     */   protected double field_146569_s;
/*     */   protected double field_146568_t;
/*     */   protected double field_146567_u;
/*     */   protected double field_146566_v;
/*     */   protected double field_146565_w;
/*     */   protected double field_146573_x;
/*     */   private int field_146554_D;
/*     */   private StatFileWriter statFileWriter;
/*  47 */   private boolean loadingAchievements = true;
/*     */   
/*     */   public GuiAchievements(GuiScreen parentScreenIn, StatFileWriter statFileWriterIn)
/*     */   {
/*  51 */     this.parentScreen = parentScreenIn;
/*  52 */     this.statFileWriter = statFileWriterIn;
/*  53 */     int i = 141;
/*  54 */     int j = 141;
/*  55 */     this.field_146569_s = (this.field_146567_u = this.field_146565_w = AchievementList.openInventory.displayColumn * 24 - i / 2 - 12);
/*  56 */     this.field_146568_t = (this.field_146566_v = this.field_146573_x = AchievementList.openInventory.displayRow * 24 - j / 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  65 */     this.mc.getNetHandler().addToSendQueue(new net.minecraft.network.play.client.C16PacketClientStatus(net.minecraft.network.play.client.C16PacketClientStatus.EnumState.REQUEST_STATS));
/*  66 */     this.buttonList.clear();
/*  67 */     this.buttonList.add(new net.minecraft.client.gui.GuiOptionButton(1, width / 2 + 24, height / 2 + 74, 80, 20, I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws IOException
/*     */   {
/*  75 */     if (!this.loadingAchievements)
/*     */     {
/*  77 */       if (button.id == 1)
/*     */       {
/*  79 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/*  90 */     if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
/*     */     {
/*  92 */       this.mc.displayGuiScreen(null);
/*  93 */       this.mc.setIngameFocus();
/*     */     }
/*     */     else
/*     */     {
/*  97 */       super.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 106 */     if (this.loadingAchievements)
/*     */     {
/* 108 */       drawDefaultBackground();
/* 109 */       drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), width / 2, height / 2, 16777215);
/* 110 */       drawCenteredString(this.fontRendererObj, lanSearchStates[((int)(Minecraft.getSystemTime() / 150L % lanSearchStates.length))], width / 2, height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
/*     */     }
/*     */     else
/*     */     {
/* 114 */       if (Mouse.isButtonDown(0))
/*     */       {
/* 116 */         int i = (width - this.field_146555_f) / 2;
/* 117 */         int j = (height - this.field_146557_g) / 2;
/* 118 */         int k = i + 8;
/* 119 */         int l = j + 17;
/*     */         
/* 121 */         if (((this.field_146554_D == 0) || (this.field_146554_D == 1)) && (mouseX >= k) && (mouseX < k + 224) && (mouseY >= l) && (mouseY < l + 155))
/*     */         {
/* 123 */           if (this.field_146554_D == 0)
/*     */           {
/* 125 */             this.field_146554_D = 1;
/*     */           }
/*     */           else
/*     */           {
/* 129 */             this.field_146567_u -= (mouseX - this.field_146563_h) * this.field_146570_r;
/* 130 */             this.field_146566_v -= (mouseY - this.field_146564_i) * this.field_146570_r;
/* 131 */             this.field_146565_w = (this.field_146569_s = this.field_146567_u);
/* 132 */             this.field_146573_x = (this.field_146568_t = this.field_146566_v);
/*     */           }
/*     */           
/* 135 */           this.field_146563_h = mouseX;
/* 136 */           this.field_146564_i = mouseY;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 141 */         this.field_146554_D = 0;
/*     */       }
/*     */       
/* 144 */       int i1 = Mouse.getDWheel();
/* 145 */       float f3 = this.field_146570_r;
/*     */       
/* 147 */       if (i1 < 0)
/*     */       {
/* 149 */         this.field_146570_r += 0.25F;
/*     */       }
/* 151 */       else if (i1 > 0)
/*     */       {
/* 153 */         this.field_146570_r -= 0.25F;
/*     */       }
/*     */       
/* 156 */       this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0F, 2.0F);
/*     */       
/* 158 */       if (this.field_146570_r != f3)
/*     */       {
/* 160 */         float f5 = f3 - this.field_146570_r;
/* 161 */         float f4 = f3 * this.field_146555_f;
/* 162 */         float f = f3 * this.field_146557_g;
/* 163 */         float f1 = this.field_146570_r * this.field_146555_f;
/* 164 */         float f2 = this.field_146570_r * this.field_146557_g;
/* 165 */         this.field_146567_u -= (f1 - f4) * 0.5F;
/* 166 */         this.field_146566_v -= (f2 - f) * 0.5F;
/* 167 */         this.field_146565_w = (this.field_146569_s = this.field_146567_u);
/* 168 */         this.field_146573_x = (this.field_146568_t = this.field_146566_v);
/*     */       }
/*     */       
/* 171 */       if (this.field_146565_w < field_146572_y)
/*     */       {
/* 173 */         this.field_146565_w = field_146572_y;
/*     */       }
/*     */       
/* 176 */       if (this.field_146573_x < field_146571_z)
/*     */       {
/* 178 */         this.field_146573_x = field_146571_z;
/*     */       }
/*     */       
/* 181 */       if (this.field_146565_w >= field_146559_A)
/*     */       {
/* 183 */         this.field_146565_w = (field_146559_A - 1);
/*     */       }
/*     */       
/* 186 */       if (this.field_146573_x >= field_146560_B)
/*     */       {
/* 188 */         this.field_146573_x = (field_146560_B - 1);
/*     */       }
/*     */       
/* 191 */       drawDefaultBackground();
/* 192 */       drawAchievementScreen(mouseX, mouseY, partialTicks);
/* 193 */       GlStateManager.disableLighting();
/* 194 */       GlStateManager.disableDepth();
/* 195 */       drawTitle();
/* 196 */       GlStateManager.enableLighting();
/* 197 */       GlStateManager.enableDepth();
/*     */     }
/*     */   }
/*     */   
/*     */   public void doneLoading()
/*     */   {
/* 203 */     if (this.loadingAchievements)
/*     */     {
/* 205 */       this.loadingAchievements = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/* 214 */     if (!this.loadingAchievements)
/*     */     {
/* 216 */       this.field_146569_s = this.field_146567_u;
/* 217 */       this.field_146568_t = this.field_146566_v;
/* 218 */       double d0 = this.field_146565_w - this.field_146567_u;
/* 219 */       double d1 = this.field_146573_x - this.field_146566_v;
/*     */       
/* 221 */       if (d0 * d0 + d1 * d1 < 4.0D)
/*     */       {
/* 223 */         this.field_146567_u += d0;
/* 224 */         this.field_146566_v += d1;
/*     */       }
/*     */       else
/*     */       {
/* 228 */         this.field_146567_u += d0 * 0.85D;
/* 229 */         this.field_146566_v += d1 * 0.85D;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void drawTitle()
/*     */   {
/* 236 */     int i = (width - this.field_146555_f) / 2;
/* 237 */     int j = (height - this.field_146557_g) / 2;
/* 238 */     this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), i + 15, j + 5, 4210752);
/*     */   }
/*     */   
/*     */   protected void drawAchievementScreen(int p_146552_1_, int p_146552_2_, float p_146552_3_)
/*     */   {
/* 243 */     int i = MathHelper.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * p_146552_3_);
/* 244 */     int j = MathHelper.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * p_146552_3_);
/*     */     
/* 246 */     if (i < field_146572_y)
/*     */     {
/* 248 */       i = field_146572_y;
/*     */     }
/*     */     
/* 251 */     if (j < field_146571_z)
/*     */     {
/* 253 */       j = field_146571_z;
/*     */     }
/*     */     
/* 256 */     if (i >= field_146559_A)
/*     */     {
/* 258 */       i = field_146559_A - 1;
/*     */     }
/*     */     
/* 261 */     if (j >= field_146560_B)
/*     */     {
/* 263 */       j = field_146560_B - 1;
/*     */     }
/*     */     
/* 266 */     int k = (width - this.field_146555_f) / 2;
/* 267 */     int l = (height - this.field_146557_g) / 2;
/* 268 */     int i1 = k + 16;
/* 269 */     int j1 = l + 17;
/* 270 */     this.zLevel = 0.0F;
/* 271 */     GlStateManager.depthFunc(518);
/* 272 */     GlStateManager.pushMatrix();
/* 273 */     GlStateManager.translate(i1, j1, -200.0F);
/* 274 */     GlStateManager.scale(1.0F / this.field_146570_r, 1.0F / this.field_146570_r, 0.0F);
/* 275 */     GlStateManager.enableTexture2D();
/* 276 */     GlStateManager.disableLighting();
/* 277 */     GlStateManager.enableRescaleNormal();
/* 278 */     GlStateManager.enableColorMaterial();
/* 279 */     int k1 = i + 288 >> 4;
/* 280 */     int l1 = j + 288 >> 4;
/* 281 */     int i2 = (i + 288) % 16;
/* 282 */     int j2 = (j + 288) % 16;
/* 283 */     int k2 = 4;
/* 284 */     int l2 = 8;
/* 285 */     int i3 = 10;
/* 286 */     int j3 = 22;
/* 287 */     int k3 = 37;
/* 288 */     Random random = new Random();
/* 289 */     float f = 16.0F / this.field_146570_r;
/* 290 */     float f1 = 16.0F / this.field_146570_r;
/*     */     
/* 292 */     for (int l3 = 0; l3 * f - j2 < 155.0F; l3++)
/*     */     {
/* 294 */       float f2 = 0.6F - (l1 + l3) / 25.0F * 0.3F;
/* 295 */       GlStateManager.color(f2, f2, f2, 1.0F);
/*     */       
/* 297 */       for (int i4 = 0; i4 * f1 - i2 < 224.0F; i4++)
/*     */       {
/* 299 */         random.setSeed(this.mc.getSession().getPlayerID().hashCode() + k1 + i4 + (l1 + l3) * 16);
/* 300 */         int j4 = random.nextInt(1 + l1 + l3) + (l1 + l3) / 2;
/* 301 */         TextureAtlasSprite textureatlassprite = func_175371_a(Blocks.sand);
/*     */         
/* 303 */         if ((j4 <= 37) && (l1 + l3 != 35))
/*     */         {
/* 305 */           if (j4 == 22)
/*     */           {
/* 307 */             if (random.nextInt(2) == 0)
/*     */             {
/* 309 */               textureatlassprite = func_175371_a(Blocks.diamond_ore);
/*     */             }
/*     */             else
/*     */             {
/* 313 */               textureatlassprite = func_175371_a(Blocks.redstone_ore);
/*     */             }
/*     */           }
/* 316 */           else if (j4 == 10)
/*     */           {
/* 318 */             textureatlassprite = func_175371_a(Blocks.iron_ore);
/*     */           }
/* 320 */           else if (j4 == 8)
/*     */           {
/* 322 */             textureatlassprite = func_175371_a(Blocks.coal_ore);
/*     */           }
/* 324 */           else if (j4 > 4)
/*     */           {
/* 326 */             textureatlassprite = func_175371_a(Blocks.stone);
/*     */           }
/* 328 */           else if (j4 > 0)
/*     */           {
/* 330 */             textureatlassprite = func_175371_a(Blocks.dirt);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 335 */           Block block = Blocks.bedrock;
/* 336 */           textureatlassprite = func_175371_a(block);
/*     */         }
/*     */         
/* 339 */         this.mc.getTextureManager().bindTexture(net.minecraft.client.renderer.texture.TextureMap.locationBlocksTexture);
/* 340 */         drawTexturedModalRect(i4 * 16 - i2, l3 * 16 - j2, textureatlassprite, 16, 16);
/*     */       }
/*     */     }
/*     */     
/* 344 */     GlStateManager.enableDepth();
/* 345 */     GlStateManager.depthFunc(515);
/* 346 */     this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
/*     */     
/* 348 */     for (int j5 = 0; j5 < AchievementList.achievementList.size(); j5++)
/*     */     {
/* 350 */       Achievement achievement1 = (Achievement)AchievementList.achievementList.get(j5);
/*     */       
/* 352 */       if (achievement1.parentAchievement != null)
/*     */       {
/* 354 */         int k5 = achievement1.displayColumn * 24 - i + 11;
/* 355 */         int l5 = achievement1.displayRow * 24 - j + 11;
/* 356 */         int j6 = achievement1.parentAchievement.displayColumn * 24 - i + 11;
/* 357 */         int k6 = achievement1.parentAchievement.displayRow * 24 - j + 11;
/* 358 */         boolean flag = this.statFileWriter.hasAchievementUnlocked(achievement1);
/* 359 */         boolean flag1 = this.statFileWriter.canUnlockAchievement(achievement1);
/* 360 */         int k4 = this.statFileWriter.func_150874_c(achievement1);
/*     */         
/* 362 */         if (k4 <= 4)
/*     */         {
/* 364 */           int l4 = -16777216;
/*     */           
/* 366 */           if (flag)
/*     */           {
/* 368 */             l4 = -6250336;
/*     */           }
/* 370 */           else if (flag1)
/*     */           {
/* 372 */             l4 = -16711936;
/*     */           }
/*     */           
/* 375 */           drawHorizontalLine(k5, j6, l5, l4);
/* 376 */           drawVerticalLine(j6, l5, k6, l4);
/*     */           
/* 378 */           if (k5 > j6)
/*     */           {
/* 380 */             drawTexturedModalRect(k5 - 11 - 7, l5 - 5, 114, 234, 7, 11);
/*     */           }
/* 382 */           else if (k5 < j6)
/*     */           {
/* 384 */             drawTexturedModalRect(k5 + 11, l5 - 5, 107, 234, 7, 11);
/*     */           }
/* 386 */           else if (l5 > k6)
/*     */           {
/* 388 */             drawTexturedModalRect(k5 - 5, l5 - 11 - 7, 96, 234, 11, 7);
/*     */           }
/* 390 */           else if (l5 < k6)
/*     */           {
/* 392 */             drawTexturedModalRect(k5 - 5, l5 + 11, 96, 241, 11, 7);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 398 */     Achievement achievement = null;
/* 399 */     float f3 = (p_146552_1_ - i1) * this.field_146570_r;
/* 400 */     float f4 = (p_146552_2_ - j1) * this.field_146570_r;
/* 401 */     RenderHelper.enableGUIStandardItemLighting();
/* 402 */     GlStateManager.disableLighting();
/* 403 */     GlStateManager.enableRescaleNormal();
/* 404 */     GlStateManager.enableColorMaterial();
/*     */     
/* 406 */     for (int i6 = 0; i6 < AchievementList.achievementList.size(); i6++)
/*     */     {
/* 408 */       Achievement achievement2 = (Achievement)AchievementList.achievementList.get(i6);
/* 409 */       int l6 = achievement2.displayColumn * 24 - i;
/* 410 */       int j7 = achievement2.displayRow * 24 - j;
/*     */       
/* 412 */       if ((l6 >= -24) && (j7 >= -24) && (l6 <= 224.0F * this.field_146570_r) && (j7 <= 155.0F * this.field_146570_r))
/*     */       {
/* 414 */         int l7 = this.statFileWriter.func_150874_c(achievement2);
/*     */         
/* 416 */         if (this.statFileWriter.hasAchievementUnlocked(achievement2))
/*     */         {
/* 418 */           float f5 = 0.75F;
/* 419 */           GlStateManager.color(f5, f5, f5, 1.0F);
/*     */         }
/* 421 */         else if (this.statFileWriter.canUnlockAchievement(achievement2))
/*     */         {
/* 423 */           float f6 = 1.0F;
/* 424 */           GlStateManager.color(f6, f6, f6, 1.0F);
/*     */         }
/* 426 */         else if (l7 < 3)
/*     */         {
/* 428 */           float f7 = 0.3F;
/* 429 */           GlStateManager.color(f7, f7, f7, 1.0F);
/*     */         }
/* 431 */         else if (l7 == 3)
/*     */         {
/* 433 */           float f8 = 0.2F;
/* 434 */           GlStateManager.color(f8, f8, f8, 1.0F);
/*     */         }
/*     */         else
/*     */         {
/* 438 */           if (l7 != 4) {
/*     */             continue;
/*     */           }
/*     */           
/*     */ 
/* 443 */           float f9 = 0.1F;
/* 444 */           GlStateManager.color(f9, f9, f9, 1.0F);
/*     */         }
/*     */         
/* 447 */         this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
/*     */         
/* 449 */         if (achievement2.getSpecial())
/*     */         {
/* 451 */           drawTexturedModalRect(l6 - 2, j7 - 2, 26, 202, 26, 26);
/*     */         }
/*     */         else
/*     */         {
/* 455 */           drawTexturedModalRect(l6 - 2, j7 - 2, 0, 202, 26, 26);
/*     */         }
/*     */         
/* 458 */         if (!this.statFileWriter.canUnlockAchievement(achievement2))
/*     */         {
/* 460 */           float f10 = 0.1F;
/* 461 */           GlStateManager.color(f10, f10, f10, 1.0F);
/* 462 */           this.itemRender.func_175039_a(false);
/*     */         }
/*     */         
/* 465 */         GlStateManager.enableLighting();
/* 466 */         GlStateManager.enableCull();
/* 467 */         this.itemRender.renderItemAndEffectIntoGUI(achievement2.theItemStack, l6 + 3, j7 + 3);
/* 468 */         GlStateManager.blendFunc(770, 771);
/* 469 */         GlStateManager.disableLighting();
/*     */         
/* 471 */         if (!this.statFileWriter.canUnlockAchievement(achievement2))
/*     */         {
/* 473 */           this.itemRender.func_175039_a(true);
/*     */         }
/*     */         
/* 476 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */         
/* 478 */         if ((f3 >= l6) && (f3 <= l6 + 22) && (f4 >= j7) && (f4 <= j7 + 22))
/*     */         {
/* 480 */           achievement = achievement2;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 485 */     GlStateManager.disableDepth();
/* 486 */     GlStateManager.enableBlend();
/* 487 */     GlStateManager.popMatrix();
/* 488 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 489 */     this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
/* 490 */     drawTexturedModalRect(k, l, 0, 0, this.field_146555_f, this.field_146557_g);
/* 491 */     this.zLevel = 0.0F;
/* 492 */     GlStateManager.depthFunc(515);
/* 493 */     GlStateManager.disableDepth();
/* 494 */     GlStateManager.enableTexture2D();
/* 495 */     super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);
/*     */     
/* 497 */     if (achievement != null)
/*     */     {
/* 499 */       String s = achievement.getStatName().getUnformattedText();
/* 500 */       String s1 = achievement.getDescription();
/* 501 */       int i7 = p_146552_1_ + 12;
/* 502 */       int k7 = p_146552_2_ - 4;
/* 503 */       int i8 = this.statFileWriter.func_150874_c(achievement);
/*     */       
/* 505 */       if (this.statFileWriter.canUnlockAchievement(achievement))
/*     */       {
/* 507 */         int j8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
/* 508 */         int i9 = this.fontRendererObj.splitStringWidth(s1, j8);
/*     */         
/* 510 */         if (this.statFileWriter.hasAchievementUnlocked(achievement))
/*     */         {
/* 512 */           i9 += 12;
/*     */         }
/*     */         
/* 515 */         drawGradientRect(i7 - 3, k7 - 3, i7 + j8 + 3, k7 + i9 + 3 + 12, -1073741824, -1073741824);
/* 516 */         this.fontRendererObj.drawSplitString(s1, i7, k7 + 12, j8, -6250336);
/*     */         
/* 518 */         if (this.statFileWriter.hasAchievementUnlocked(achievement))
/*     */         {
/* 520 */           this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken", new Object[0]), i7, k7 + i9 + 4, -7302913);
/*     */         }
/*     */       }
/* 523 */       else if (i8 == 3)
/*     */       {
/* 525 */         s = I18n.format("achievement.unknown", new Object[0]);
/* 526 */         int k8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
/* 527 */         String s2 = new ChatComponentTranslation("achievement.requires", new Object[] { achievement.parentAchievement.getStatName() }).getUnformattedText();
/* 528 */         int i5 = this.fontRendererObj.splitStringWidth(s2, k8);
/* 529 */         drawGradientRect(i7 - 3, k7 - 3, i7 + k8 + 3, k7 + i5 + 12 + 3, -1073741824, -1073741824);
/* 530 */         this.fontRendererObj.drawSplitString(s2, i7, k7 + 12, k8, -9416624);
/*     */       }
/* 532 */       else if (i8 < 3)
/*     */       {
/* 534 */         int l8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
/* 535 */         String s3 = new ChatComponentTranslation("achievement.requires", new Object[] { achievement.parentAchievement.getStatName() }).getUnformattedText();
/* 536 */         int j9 = this.fontRendererObj.splitStringWidth(s3, l8);
/* 537 */         drawGradientRect(i7 - 3, k7 - 3, i7 + l8 + 3, k7 + j9 + 12 + 3, -1073741824, -1073741824);
/* 538 */         this.fontRendererObj.drawSplitString(s3, i7, k7 + 12, l8, -9416624);
/*     */       }
/*     */       else
/*     */       {
/* 542 */         s = null;
/*     */       }
/*     */       
/* 545 */       if (s != null)
/*     */       {
/* 547 */         this.fontRendererObj.drawStringWithShadow(s, i7, k7, achievement.getSpecial() ? -8355776 : this.statFileWriter.canUnlockAchievement(achievement) ? -1 : achievement.getSpecial() ? -128 : -8355712);
/*     */       }
/*     */     }
/*     */     
/* 551 */     GlStateManager.enableDepth();
/* 552 */     GlStateManager.enableLighting();
/* 553 */     RenderHelper.disableStandardItemLighting();
/*     */   }
/*     */   
/*     */   private TextureAtlasSprite func_175371_a(Block p_175371_1_)
/*     */   {
/* 558 */     return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(p_175371_1_.getDefaultState());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean doesGuiPauseGame()
/*     */   {
/* 566 */     return !this.loadingAchievements;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\achievement\GuiAchievements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */