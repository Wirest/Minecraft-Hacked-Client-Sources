/*     */ package net.minecraft.client.gui.achievement;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiSlot;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityList.EntityEggInfo;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatCrafting;
/*     */ import net.minecraft.stats.StatFileWriter;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class GuiStats extends GuiScreen implements net.minecraft.client.gui.IProgressMeter
/*     */ {
/*     */   protected GuiScreen parentScreen;
/*  34 */   protected String screenTitle = "Select world";
/*     */   
/*     */   private StatsGeneral generalStats;
/*     */   
/*     */   private StatsItem itemStats;
/*     */   private StatsBlock blockStats;
/*     */   private StatsMobsList mobStats;
/*     */   private StatFileWriter field_146546_t;
/*     */   private GuiSlot displaySlot;
/*  43 */   private boolean doesGuiPauseGame = true;
/*     */   
/*     */   public GuiStats(GuiScreen p_i1071_1_, StatFileWriter p_i1071_2_)
/*     */   {
/*  47 */     this.parentScreen = p_i1071_1_;
/*  48 */     this.field_146546_t = p_i1071_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  57 */     this.screenTitle = I18n.format("gui.stats", new Object[0]);
/*  58 */     this.doesGuiPauseGame = true;
/*  59 */     this.mc.getNetHandler().addToSendQueue(new net.minecraft.network.play.client.C16PacketClientStatus(net.minecraft.network.play.client.C16PacketClientStatus.EnumState.REQUEST_STATS));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleMouseInput()
/*     */     throws IOException
/*     */   {
/*  67 */     super.handleMouseInput();
/*     */     
/*  69 */     if (this.displaySlot != null)
/*     */     {
/*  71 */       this.displaySlot.handleMouseInput();
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_175366_f()
/*     */   {
/*  77 */     this.generalStats = new StatsGeneral(this.mc);
/*  78 */     this.generalStats.registerScrollButtons(1, 1);
/*  79 */     this.itemStats = new StatsItem(this.mc);
/*  80 */     this.itemStats.registerScrollButtons(1, 1);
/*  81 */     this.blockStats = new StatsBlock(this.mc);
/*  82 */     this.blockStats.registerScrollButtons(1, 1);
/*  83 */     this.mobStats = new StatsMobsList(this.mc);
/*  84 */     this.mobStats.registerScrollButtons(1, 1);
/*     */   }
/*     */   
/*     */   public void createButtons()
/*     */   {
/*  89 */     this.buttonList.add(new GuiButton(0, width / 2 + 4, height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
/*  90 */     this.buttonList.add(new GuiButton(1, width / 2 - 160, height - 52, 80, 20, I18n.format("stat.generalButton", new Object[0])));
/*     */     GuiButton guibutton;
/*  92 */     this.buttonList.add(guibutton = new GuiButton(2, width / 2 - 80, height - 52, 80, 20, I18n.format("stat.blocksButton", new Object[0])));
/*     */     GuiButton guibutton1;
/*  94 */     this.buttonList.add(guibutton1 = new GuiButton(3, width / 2, height - 52, 80, 20, I18n.format("stat.itemsButton", new Object[0])));
/*     */     GuiButton guibutton2;
/*  96 */     this.buttonList.add(guibutton2 = new GuiButton(4, width / 2 + 80, height - 52, 80, 20, I18n.format("stat.mobsButton", new Object[0])));
/*     */     
/*  98 */     if (this.blockStats.getSize() == 0)
/*     */     {
/* 100 */       guibutton.enabled = false;
/*     */     }
/*     */     
/* 103 */     if (this.itemStats.getSize() == 0)
/*     */     {
/* 105 */       guibutton1.enabled = false;
/*     */     }
/*     */     
/* 108 */     if (this.mobStats.getSize() == 0)
/*     */     {
/* 110 */       guibutton2.enabled = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws IOException
/*     */   {
/* 119 */     if (button.enabled)
/*     */     {
/* 121 */       if (button.id == 0)
/*     */       {
/* 123 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/* 125 */       else if (button.id == 1)
/*     */       {
/* 127 */         this.displaySlot = this.generalStats;
/*     */       }
/* 129 */       else if (button.id == 3)
/*     */       {
/* 131 */         this.displaySlot = this.itemStats;
/*     */       }
/* 133 */       else if (button.id == 2)
/*     */       {
/* 135 */         this.displaySlot = this.blockStats;
/*     */       }
/* 137 */       else if (button.id == 4)
/*     */       {
/* 139 */         this.displaySlot = this.mobStats;
/*     */       }
/*     */       else
/*     */       {
/* 143 */         this.displaySlot.actionPerformed(button);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 153 */     if (this.doesGuiPauseGame)
/*     */     {
/* 155 */       drawDefaultBackground();
/* 156 */       drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), width / 2, height / 2, 16777215);
/* 157 */       drawCenteredString(this.fontRendererObj, lanSearchStates[((int)(Minecraft.getSystemTime() / 150L % lanSearchStates.length))], width / 2, height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
/*     */     }
/*     */     else
/*     */     {
/* 161 */       this.displaySlot.drawScreen(mouseX, mouseY, partialTicks);
/* 162 */       drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, 20, 16777215);
/* 163 */       super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     }
/*     */   }
/*     */   
/*     */   public void doneLoading()
/*     */   {
/* 169 */     if (this.doesGuiPauseGame)
/*     */     {
/* 171 */       func_175366_f();
/* 172 */       createButtons();
/* 173 */       this.displaySlot = this.generalStats;
/* 174 */       this.doesGuiPauseGame = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean doesGuiPauseGame()
/*     */   {
/* 183 */     return !this.doesGuiPauseGame;
/*     */   }
/*     */   
/*     */   private void drawStatsScreen(int p_146521_1_, int p_146521_2_, Item p_146521_3_)
/*     */   {
/* 188 */     drawButtonBackground(p_146521_1_ + 1, p_146521_2_ + 1);
/* 189 */     GlStateManager.enableRescaleNormal();
/* 190 */     RenderHelper.enableGUIStandardItemLighting();
/* 191 */     this.itemRender.renderItemIntoGUI(new ItemStack(p_146521_3_, 1, 0), p_146521_1_ + 2, p_146521_2_ + 2);
/* 192 */     RenderHelper.disableStandardItemLighting();
/* 193 */     GlStateManager.disableRescaleNormal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void drawButtonBackground(int p_146531_1_, int p_146531_2_)
/*     */   {
/* 201 */     drawSprite(p_146531_1_, p_146531_2_, 0, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void drawSprite(int p_146527_1_, int p_146527_2_, int p_146527_3_, int p_146527_4_)
/*     */   {
/* 209 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 210 */     this.mc.getTextureManager().bindTexture(statIcons);
/* 211 */     float f = 0.0078125F;
/* 212 */     float f1 = 0.0078125F;
/* 213 */     int i = 18;
/* 214 */     int j = 18;
/* 215 */     Tessellator tessellator = Tessellator.getInstance();
/* 216 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 217 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 218 */     worldrenderer.pos(p_146527_1_ + 0, p_146527_2_ + 18, this.zLevel).tex((p_146527_3_ + 0) * 0.0078125F, (p_146527_4_ + 18) * 0.0078125F).endVertex();
/* 219 */     worldrenderer.pos(p_146527_1_ + 18, p_146527_2_ + 18, this.zLevel).tex((p_146527_3_ + 18) * 0.0078125F, (p_146527_4_ + 18) * 0.0078125F).endVertex();
/* 220 */     worldrenderer.pos(p_146527_1_ + 18, p_146527_2_ + 0, this.zLevel).tex((p_146527_3_ + 18) * 0.0078125F, (p_146527_4_ + 0) * 0.0078125F).endVertex();
/* 221 */     worldrenderer.pos(p_146527_1_ + 0, p_146527_2_ + 0, this.zLevel).tex((p_146527_3_ + 0) * 0.0078125F, (p_146527_4_ + 0) * 0.0078125F).endVertex();
/* 222 */     tessellator.draw();
/*     */   }
/*     */   
/*     */   abstract class Stats extends GuiSlot
/*     */   {
/* 227 */     protected int field_148218_l = -1;
/*     */     protected List<StatCrafting> statsHolder;
/*     */     protected Comparator<StatCrafting> statSorter;
/* 230 */     protected int field_148217_o = -1;
/*     */     protected int field_148215_p;
/*     */     
/*     */     protected Stats(Minecraft mcIn)
/*     */     {
/* 235 */       super(GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, 20);
/* 236 */       setShowSelectionBox(false);
/* 237 */       setHasListHeader(true, 20);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
/*     */     
/*     */ 
/*     */     protected boolean isSelected(int slotIndex)
/*     */     {
/* 246 */       return false;
/*     */     }
/*     */     
/*     */     protected void drawBackground()
/*     */     {
/* 251 */       GuiStats.this.drawDefaultBackground();
/*     */     }
/*     */     
/*     */     protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_)
/*     */     {
/* 256 */       if (!org.lwjgl.input.Mouse.isButtonDown(0))
/*     */       {
/* 258 */         this.field_148218_l = -1;
/*     */       }
/*     */       
/* 261 */       if (this.field_148218_l == 0)
/*     */       {
/* 263 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 0);
/*     */       }
/*     */       else
/*     */       {
/* 267 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 18);
/*     */       }
/*     */       
/* 270 */       if (this.field_148218_l == 1)
/*     */       {
/* 272 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 0);
/*     */       }
/*     */       else
/*     */       {
/* 276 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 18);
/*     */       }
/*     */       
/* 279 */       if (this.field_148218_l == 2)
/*     */       {
/* 281 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 0);
/*     */       }
/*     */       else
/*     */       {
/* 285 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 18);
/*     */       }
/*     */       
/* 288 */       if (this.field_148217_o != -1)
/*     */       {
/* 290 */         int i = 79;
/* 291 */         int j = 18;
/*     */         
/* 293 */         if (this.field_148217_o == 1)
/*     */         {
/* 295 */           i = 129;
/*     */         }
/* 297 */         else if (this.field_148217_o == 2)
/*     */         {
/* 299 */           i = 179;
/*     */         }
/*     */         
/* 302 */         if (this.field_148215_p == 1)
/*     */         {
/* 304 */           j = 36;
/*     */         }
/*     */         
/* 307 */         GuiStats.this.drawSprite(p_148129_1_ + i, p_148129_2_ + 1, j, 0);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_148132_a(int p_148132_1_, int p_148132_2_)
/*     */     {
/* 313 */       this.field_148218_l = -1;
/*     */       
/* 315 */       if ((p_148132_1_ >= 79) && (p_148132_1_ < 115))
/*     */       {
/* 317 */         this.field_148218_l = 0;
/*     */       }
/* 319 */       else if ((p_148132_1_ >= 129) && (p_148132_1_ < 165))
/*     */       {
/* 321 */         this.field_148218_l = 1;
/*     */       }
/* 323 */       else if ((p_148132_1_ >= 179) && (p_148132_1_ < 215))
/*     */       {
/* 325 */         this.field_148218_l = 2;
/*     */       }
/*     */       
/* 328 */       if (this.field_148218_l >= 0)
/*     */       {
/* 330 */         func_148212_h(this.field_148218_l);
/* 331 */         this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new net.minecraft.util.ResourceLocation("gui.button.press"), 1.0F));
/*     */       }
/*     */     }
/*     */     
/*     */     protected final int getSize()
/*     */     {
/* 337 */       return this.statsHolder.size();
/*     */     }
/*     */     
/*     */     protected final StatCrafting func_148211_c(int p_148211_1_)
/*     */     {
/* 342 */       return (StatCrafting)this.statsHolder.get(p_148211_1_);
/*     */     }
/*     */     
/*     */     protected abstract String func_148210_b(int paramInt);
/*     */     
/*     */     protected void func_148209_a(StatBase p_148209_1_, int p_148209_2_, int p_148209_3_, boolean p_148209_4_)
/*     */     {
/* 349 */       if (p_148209_1_ != null)
/*     */       {
/* 351 */         String s = p_148209_1_.format(GuiStats.this.field_146546_t.readStat(p_148209_1_));
/* 352 */         GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_148209_2_ - GuiStats.this.fontRendererObj.getStringWidth(s), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
/*     */       }
/*     */       else
/*     */       {
/* 356 */         String s1 = "-";
/* 357 */         GuiStats.this.drawString(GuiStats.this.fontRendererObj, s1, p_148209_2_ - GuiStats.this.fontRendererObj.getStringWidth(s1), p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_148142_b(int p_148142_1_, int p_148142_2_)
/*     */     {
/* 363 */       if ((p_148142_2_ >= this.top) && (p_148142_2_ <= this.bottom))
/*     */       {
/* 365 */         int i = getSlotIndexFromScreenCoords(p_148142_1_, p_148142_2_);
/* 366 */         int j = this.width / 2 - 92 - 16;
/*     */         
/* 368 */         if (i >= 0)
/*     */         {
/* 370 */           if ((p_148142_1_ < j + 40) || (p_148142_1_ > j + 40 + 20))
/*     */           {
/* 372 */             return;
/*     */           }
/*     */           
/* 375 */           StatCrafting statcrafting = func_148211_c(i);
/* 376 */           func_148213_a(statcrafting, p_148142_1_, p_148142_2_);
/*     */         }
/*     */         else
/*     */         {
/* 380 */           String s = "";
/*     */           
/* 382 */           if ((p_148142_1_ >= j + 115 - 18) && (p_148142_1_ <= j + 115))
/*     */           {
/* 384 */             s = func_148210_b(0);
/*     */           }
/* 386 */           else if ((p_148142_1_ >= j + 165 - 18) && (p_148142_1_ <= j + 165))
/*     */           {
/* 388 */             s = func_148210_b(1);
/*     */           }
/*     */           else
/*     */           {
/* 392 */             if ((p_148142_1_ < j + 215 - 18) || (p_148142_1_ > j + 215))
/*     */             {
/* 394 */               return;
/*     */             }
/*     */             
/* 397 */             s = func_148210_b(2);
/*     */           }
/*     */           
/* 400 */           s = I18n.format(s, new Object[0]).trim();
/*     */           
/* 402 */           if (s.length() > 0)
/*     */           {
/* 404 */             int k = p_148142_1_ + 12;
/* 405 */             int l = p_148142_2_ - 12;
/* 406 */             int i1 = GuiStats.this.fontRendererObj.getStringWidth(s);
/* 407 */             GuiStats.this.drawGradientRect(k - 3, l - 3, k + i1 + 3, l + 8 + 3, -1073741824, -1073741824);
/* 408 */             GuiStats.this.fontRendererObj.drawStringWithShadow(s, k, l, -1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_148213_a(StatCrafting p_148213_1_, int p_148213_2_, int p_148213_3_)
/*     */     {
/* 416 */       if (p_148213_1_ != null)
/*     */       {
/* 418 */         Item item = p_148213_1_.func_150959_a();
/* 419 */         ItemStack itemstack = new ItemStack(item);
/* 420 */         String s = itemstack.getUnlocalizedName();
/* 421 */         String s1 = I18n.format(new StringBuilder(String.valueOf(s)).append(".name").toString(), new Object[0]).trim();
/*     */         
/* 423 */         if (s1.length() > 0)
/*     */         {
/* 425 */           int i = p_148213_2_ + 12;
/* 426 */           int j = p_148213_3_ - 12;
/* 427 */           int k = GuiStats.this.fontRendererObj.getStringWidth(s1);
/* 428 */           GuiStats.this.drawGradientRect(i - 3, j - 3, i + k + 3, j + 8 + 3, -1073741824, -1073741824);
/* 429 */           GuiStats.this.fontRendererObj.drawStringWithShadow(s1, i, j, -1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_148212_h(int p_148212_1_)
/*     */     {
/* 436 */       if (p_148212_1_ != this.field_148217_o)
/*     */       {
/* 438 */         this.field_148217_o = p_148212_1_;
/* 439 */         this.field_148215_p = -1;
/*     */       }
/* 441 */       else if (this.field_148215_p == -1)
/*     */       {
/* 443 */         this.field_148215_p = 1;
/*     */       }
/*     */       else
/*     */       {
/* 447 */         this.field_148217_o = -1;
/* 448 */         this.field_148215_p = 0;
/*     */       }
/*     */       
/* 451 */       java.util.Collections.sort(this.statsHolder, this.statSorter);
/*     */     }
/*     */   }
/*     */   
/*     */   class StatsBlock extends GuiStats.Stats
/*     */   {
/*     */     public StatsBlock(Minecraft mcIn)
/*     */     {
/* 459 */       super(mcIn);
/* 460 */       this.statsHolder = Lists.newArrayList();
/*     */       
/* 462 */       for (StatCrafting statcrafting : StatList.objectMineStats)
/*     */       {
/* 464 */         boolean flag = false;
/* 465 */         int i = Item.getIdFromItem(statcrafting.func_150959_a());
/*     */         
/* 467 */         if (GuiStats.this.field_146546_t.readStat(statcrafting) > 0)
/*     */         {
/* 469 */           flag = true;
/*     */         }
/* 471 */         else if ((StatList.objectUseStats[i] != null) && (GuiStats.this.field_146546_t.readStat(StatList.objectUseStats[i]) > 0))
/*     */         {
/* 473 */           flag = true;
/*     */         }
/* 475 */         else if ((StatList.objectCraftStats[i] != null) && (GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[i]) > 0))
/*     */         {
/* 477 */           flag = true;
/*     */         }
/*     */         
/* 480 */         if (flag)
/*     */         {
/* 482 */           this.statsHolder.add(statcrafting);
/*     */         }
/*     */       }
/*     */       
/* 486 */       this.statSorter = new Comparator()
/*     */       {
/*     */         public int compare(StatCrafting p_compare_1_, StatCrafting p_compare_2_)
/*     */         {
/* 490 */           int j = Item.getIdFromItem(p_compare_1_.func_150959_a());
/* 491 */           int k = Item.getIdFromItem(p_compare_2_.func_150959_a());
/* 492 */           StatBase statbase = null;
/* 493 */           StatBase statbase1 = null;
/*     */           
/* 495 */           if (GuiStats.StatsBlock.this.field_148217_o == 2)
/*     */           {
/* 497 */             statbase = StatList.mineBlockStatArray[j];
/* 498 */             statbase1 = StatList.mineBlockStatArray[k];
/*     */           }
/* 500 */           else if (GuiStats.StatsBlock.this.field_148217_o == 0)
/*     */           {
/* 502 */             statbase = StatList.objectCraftStats[j];
/* 503 */             statbase1 = StatList.objectCraftStats[k];
/*     */           }
/* 505 */           else if (GuiStats.StatsBlock.this.field_148217_o == 1)
/*     */           {
/* 507 */             statbase = StatList.objectUseStats[j];
/* 508 */             statbase1 = StatList.objectUseStats[k];
/*     */           }
/*     */           
/* 511 */           if ((statbase != null) || (statbase1 != null))
/*     */           {
/* 513 */             if (statbase == null)
/*     */             {
/* 515 */               return 1;
/*     */             }
/*     */             
/* 518 */             if (statbase1 == null)
/*     */             {
/* 520 */               return -1;
/*     */             }
/*     */             
/* 523 */             int l = GuiStats.this.field_146546_t.readStat(statbase);
/* 524 */             int i1 = GuiStats.this.field_146546_t.readStat(statbase1);
/*     */             
/* 526 */             if (l != i1)
/*     */             {
/* 528 */               return (l - i1) * GuiStats.StatsBlock.this.field_148215_p;
/*     */             }
/*     */           }
/*     */           
/* 532 */           return j - k;
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_)
/*     */     {
/* 539 */       super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
/*     */       
/* 541 */       if (this.field_148218_l == 0)
/*     */       {
/* 543 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1, 18, 18);
/*     */       }
/*     */       else
/*     */       {
/* 547 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 18, 18);
/*     */       }
/*     */       
/* 550 */       if (this.field_148218_l == 1)
/*     */       {
/* 552 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1, 36, 18);
/*     */       }
/*     */       else
/*     */       {
/* 556 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 36, 18);
/*     */       }
/*     */       
/* 559 */       if (this.field_148218_l == 2)
/*     */       {
/* 561 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1, 54, 18);
/*     */       }
/*     */       else
/*     */       {
/* 565 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 54, 18);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */     {
/* 571 */       StatCrafting statcrafting = func_148211_c(entryID);
/* 572 */       Item item = statcrafting.func_150959_a();
/* 573 */       GuiStats.this.drawStatsScreen(p_180791_2_ + 40, p_180791_3_, item);
/* 574 */       int i = Item.getIdFromItem(item);
/* 575 */       func_148209_a(StatList.objectCraftStats[i], p_180791_2_ + 115, p_180791_3_, entryID % 2 == 0);
/* 576 */       func_148209_a(StatList.objectUseStats[i], p_180791_2_ + 165, p_180791_3_, entryID % 2 == 0);
/* 577 */       func_148209_a(statcrafting, p_180791_2_ + 215, p_180791_3_, entryID % 2 == 0);
/*     */     }
/*     */     
/*     */     protected String func_148210_b(int p_148210_1_)
/*     */     {
/* 582 */       return p_148210_1_ == 1 ? "stat.used" : p_148210_1_ == 0 ? "stat.crafted" : "stat.mined";
/*     */     }
/*     */   }
/*     */   
/*     */   class StatsGeneral extends GuiSlot
/*     */   {
/*     */     public StatsGeneral(Minecraft mcIn)
/*     */     {
/* 590 */       super(GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, 10);
/* 591 */       setShowSelectionBox(false);
/*     */     }
/*     */     
/*     */     protected int getSize()
/*     */     {
/* 596 */       return StatList.generalStats.size();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
/*     */     
/*     */ 
/*     */     protected boolean isSelected(int slotIndex)
/*     */     {
/* 605 */       return false;
/*     */     }
/*     */     
/*     */     protected int getContentHeight()
/*     */     {
/* 610 */       return getSize() * 10;
/*     */     }
/*     */     
/*     */     protected void drawBackground()
/*     */     {
/* 615 */       GuiStats.this.drawDefaultBackground();
/*     */     }
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */     {
/* 620 */       StatBase statbase = (StatBase)StatList.generalStats.get(entryID);
/* 621 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, statbase.getStatName().getUnformattedText(), p_180791_2_ + 2, p_180791_3_ + 1, entryID % 2 == 0 ? 16777215 : 9474192);
/* 622 */       String s = statbase.format(GuiStats.this.field_146546_t.readStat(statbase));
/* 623 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_180791_2_ + 2 + 213 - GuiStats.this.fontRendererObj.getStringWidth(s), p_180791_3_ + 1, entryID % 2 == 0 ? 16777215 : 9474192);
/*     */     }
/*     */   }
/*     */   
/*     */   class StatsItem extends GuiStats.Stats
/*     */   {
/*     */     public StatsItem(Minecraft mcIn)
/*     */     {
/* 631 */       super(mcIn);
/* 632 */       this.statsHolder = Lists.newArrayList();
/*     */       
/* 634 */       for (StatCrafting statcrafting : StatList.itemStats)
/*     */       {
/* 636 */         boolean flag = false;
/* 637 */         int i = Item.getIdFromItem(statcrafting.func_150959_a());
/*     */         
/* 639 */         if (GuiStats.this.field_146546_t.readStat(statcrafting) > 0)
/*     */         {
/* 641 */           flag = true;
/*     */         }
/* 643 */         else if ((StatList.objectBreakStats[i] != null) && (GuiStats.this.field_146546_t.readStat(StatList.objectBreakStats[i]) > 0))
/*     */         {
/* 645 */           flag = true;
/*     */         }
/* 647 */         else if ((StatList.objectCraftStats[i] != null) && (GuiStats.this.field_146546_t.readStat(StatList.objectCraftStats[i]) > 0))
/*     */         {
/* 649 */           flag = true;
/*     */         }
/*     */         
/* 652 */         if (flag)
/*     */         {
/* 654 */           this.statsHolder.add(statcrafting);
/*     */         }
/*     */       }
/*     */       
/* 658 */       this.statSorter = new Comparator()
/*     */       {
/*     */         public int compare(StatCrafting p_compare_1_, StatCrafting p_compare_2_)
/*     */         {
/* 662 */           int j = Item.getIdFromItem(p_compare_1_.func_150959_a());
/* 663 */           int k = Item.getIdFromItem(p_compare_2_.func_150959_a());
/* 664 */           StatBase statbase = null;
/* 665 */           StatBase statbase1 = null;
/*     */           
/* 667 */           if (GuiStats.StatsItem.this.field_148217_o == 0)
/*     */           {
/* 669 */             statbase = StatList.objectBreakStats[j];
/* 670 */             statbase1 = StatList.objectBreakStats[k];
/*     */           }
/* 672 */           else if (GuiStats.StatsItem.this.field_148217_o == 1)
/*     */           {
/* 674 */             statbase = StatList.objectCraftStats[j];
/* 675 */             statbase1 = StatList.objectCraftStats[k];
/*     */           }
/* 677 */           else if (GuiStats.StatsItem.this.field_148217_o == 2)
/*     */           {
/* 679 */             statbase = StatList.objectUseStats[j];
/* 680 */             statbase1 = StatList.objectUseStats[k];
/*     */           }
/*     */           
/* 683 */           if ((statbase != null) || (statbase1 != null))
/*     */           {
/* 685 */             if (statbase == null)
/*     */             {
/* 687 */               return 1;
/*     */             }
/*     */             
/* 690 */             if (statbase1 == null)
/*     */             {
/* 692 */               return -1;
/*     */             }
/*     */             
/* 695 */             int l = GuiStats.this.field_146546_t.readStat(statbase);
/* 696 */             int i1 = GuiStats.this.field_146546_t.readStat(statbase1);
/*     */             
/* 698 */             if (l != i1)
/*     */             {
/* 700 */               return (l - i1) * GuiStats.StatsItem.this.field_148215_p;
/*     */             }
/*     */           }
/*     */           
/* 704 */           return j - k;
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_)
/*     */     {
/* 711 */       super.drawListHeader(p_148129_1_, p_148129_2_, p_148129_3_);
/*     */       
/* 713 */       if (this.field_148218_l == 0)
/*     */       {
/* 715 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1, 72, 18);
/*     */       }
/*     */       else
/*     */       {
/* 719 */         GuiStats.this.drawSprite(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 72, 18);
/*     */       }
/*     */       
/* 722 */       if (this.field_148218_l == 1)
/*     */       {
/* 724 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1, 18, 18);
/*     */       }
/*     */       else
/*     */       {
/* 728 */         GuiStats.this.drawSprite(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 18, 18);
/*     */       }
/*     */       
/* 731 */       if (this.field_148218_l == 2)
/*     */       {
/* 733 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1, 36, 18);
/*     */       }
/*     */       else
/*     */       {
/* 737 */         GuiStats.this.drawSprite(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 36, 18);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */     {
/* 743 */       StatCrafting statcrafting = func_148211_c(entryID);
/* 744 */       Item item = statcrafting.func_150959_a();
/* 745 */       GuiStats.this.drawStatsScreen(p_180791_2_ + 40, p_180791_3_, item);
/* 746 */       int i = Item.getIdFromItem(item);
/* 747 */       func_148209_a(StatList.objectBreakStats[i], p_180791_2_ + 115, p_180791_3_, entryID % 2 == 0);
/* 748 */       func_148209_a(StatList.objectCraftStats[i], p_180791_2_ + 165, p_180791_3_, entryID % 2 == 0);
/* 749 */       func_148209_a(statcrafting, p_180791_2_ + 215, p_180791_3_, entryID % 2 == 0);
/*     */     }
/*     */     
/*     */     protected String func_148210_b(int p_148210_1_)
/*     */     {
/* 754 */       return p_148210_1_ == 2 ? "stat.used" : p_148210_1_ == 1 ? "stat.crafted" : "stat.depleted";
/*     */     }
/*     */   }
/*     */   
/*     */   class StatsMobsList extends GuiSlot
/*     */   {
/* 760 */     private final List<EntityList.EntityEggInfo> field_148222_l = Lists.newArrayList();
/*     */     
/*     */     public StatsMobsList(Minecraft mcIn)
/*     */     {
/* 764 */       super(GuiStats.width, GuiStats.height, 32, GuiStats.height - 64, GuiStats.this.fontRendererObj.FONT_HEIGHT * 4);
/* 765 */       setShowSelectionBox(false);
/*     */       
/* 767 */       for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.entityEggs.values())
/*     */       {
/* 769 */         if ((GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151512_d) > 0) || (GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151513_e) > 0))
/*     */         {
/* 771 */           this.field_148222_l.add(entitylist$entityegginfo);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected int getSize()
/*     */     {
/* 778 */       return this.field_148222_l.size();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
/*     */     
/*     */ 
/*     */     protected boolean isSelected(int slotIndex)
/*     */     {
/* 787 */       return false;
/*     */     }
/*     */     
/*     */     protected int getContentHeight()
/*     */     {
/* 792 */       return getSize() * GuiStats.this.fontRendererObj.FONT_HEIGHT * 4;
/*     */     }
/*     */     
/*     */     protected void drawBackground()
/*     */     {
/* 797 */       GuiStats.this.drawDefaultBackground();
/*     */     }
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */     {
/* 802 */       EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)this.field_148222_l.get(entryID);
/* 803 */       String s = I18n.format("entity." + EntityList.getStringFromID(entitylist$entityegginfo.spawnedID) + ".name", new Object[0]);
/* 804 */       int i = GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151512_d);
/* 805 */       int j = GuiStats.this.field_146546_t.readStat(entitylist$entityegginfo.field_151513_e);
/* 806 */       String s1 = I18n.format("stat.entityKills", new Object[] { Integer.valueOf(i), s });
/* 807 */       String s2 = I18n.format("stat.entityKilledBy", new Object[] { s, Integer.valueOf(j) });
/*     */       
/* 809 */       if (i == 0)
/*     */       {
/* 811 */         s1 = I18n.format("stat.entityKills.none", new Object[] { s });
/*     */       }
/*     */       
/* 814 */       if (j == 0)
/*     */       {
/* 816 */         s2 = I18n.format("stat.entityKilledBy.none", new Object[] { s });
/*     */       }
/*     */       
/* 819 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, s, p_180791_2_ + 2 - 10, p_180791_3_ + 1, 16777215);
/* 820 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, s1, p_180791_2_ + 2, p_180791_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT, i == 0 ? 6316128 : 9474192);
/* 821 */       GuiStats.this.drawString(GuiStats.this.fontRendererObj, s2, p_180791_2_ + 2, p_180791_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT * 2, j == 0 ? 6316128 : 9474192);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\achievement\GuiStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */