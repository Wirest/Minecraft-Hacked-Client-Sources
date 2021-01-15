/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.model.ModelBook;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.inventory.ContainerEnchantment;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnchantmentNameParts;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.IWorldNameable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class GuiEnchantment extends GuiContainer
/*     */ {
/*  28 */   private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  33 */   private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private static final ModelBook MODEL_BOOK = new ModelBook();
/*     */   
/*     */ 
/*     */   private final InventoryPlayer playerInventory;
/*     */   
/*     */ 
/*  44 */   private Random random = new Random();
/*     */   private ContainerEnchantment container;
/*     */   public int field_147073_u;
/*     */   public float field_147071_v;
/*     */   public float field_147069_w;
/*     */   public float field_147082_x;
/*     */   public float field_147081_y;
/*     */   public float field_147080_z;
/*     */   public float field_147076_A;
/*     */   ItemStack field_147077_B;
/*     */   private final IWorldNameable field_175380_I;
/*     */   
/*     */   public GuiEnchantment(InventoryPlayer inventory, World worldIn, IWorldNameable p_i45502_3_)
/*     */   {
/*  58 */     super(new ContainerEnchantment(inventory, worldIn));
/*  59 */     this.playerInventory = inventory;
/*  60 */     this.container = ((ContainerEnchantment)this.inventorySlots);
/*  61 */     this.field_175380_I = p_i45502_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*     */   {
/*  69 */     this.fontRendererObj.drawString(this.field_175380_I.getDisplayName().getUnformattedText(), 12.0D, 5.0D, 4210752);
/*  70 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, this.ySize - 96 + 2, 4210752);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/*  78 */     super.updateScreen();
/*  79 */     func_147068_g();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */     throws java.io.IOException
/*     */   {
/*  87 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  88 */     int i = (width - this.xSize) / 2;
/*  89 */     int j = (height - this.ySize) / 2;
/*     */     
/*  91 */     for (int k = 0; k < 3; k++)
/*     */     {
/*  93 */       int l = mouseX - (i + 60);
/*  94 */       int i1 = mouseY - (j + 14 + 19 * k);
/*     */       
/*  96 */       if ((l >= 0) && (i1 >= 0) && (l < 108) && (i1 < 19) && (this.container.enchantItem(this.mc.thePlayer, k)))
/*     */       {
/*  98 */         this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*     */   {
/* 108 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 109 */     this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
/* 110 */     int i = (width - this.xSize) / 2;
/* 111 */     int j = (height - this.ySize) / 2;
/* 112 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/* 113 */     GlStateManager.pushMatrix();
/* 114 */     GlStateManager.matrixMode(5889);
/* 115 */     GlStateManager.pushMatrix();
/* 116 */     GlStateManager.loadIdentity();
/* 117 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/* 118 */     GlStateManager.viewport((ScaledResolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (ScaledResolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
/* 119 */     GlStateManager.translate(-0.34F, 0.23F, 0.0F);
/* 120 */     org.lwjgl.util.glu.Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
/* 121 */     float f = 1.0F;
/* 122 */     GlStateManager.matrixMode(5888);
/* 123 */     GlStateManager.loadIdentity();
/* 124 */     RenderHelper.enableStandardItemLighting();
/* 125 */     GlStateManager.translate(0.0F, 3.3F, -16.0F);
/* 126 */     GlStateManager.scale(f, f, f);
/* 127 */     float f1 = 5.0F;
/* 128 */     GlStateManager.scale(f1, f1, f1);
/* 129 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 130 */     this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
/* 131 */     GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
/* 132 */     float f2 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * partialTicks;
/* 133 */     GlStateManager.translate((1.0F - f2) * 0.2F, (1.0F - f2) * 0.1F, (1.0F - f2) * 0.25F);
/* 134 */     GlStateManager.rotate(-(1.0F - f2) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
/* 135 */     GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
/* 136 */     float f3 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.25F;
/* 137 */     float f4 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.75F;
/* 138 */     f3 = (f3 - MathHelper.truncateDoubleToInt(f3)) * 1.6F - 0.3F;
/* 139 */     f4 = (f4 - MathHelper.truncateDoubleToInt(f4)) * 1.6F - 0.3F;
/*     */     
/* 141 */     if (f3 < 0.0F)
/*     */     {
/* 143 */       f3 = 0.0F;
/*     */     }
/*     */     
/* 146 */     if (f4 < 0.0F)
/*     */     {
/* 148 */       f4 = 0.0F;
/*     */     }
/*     */     
/* 151 */     if (f3 > 1.0F)
/*     */     {
/* 153 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 156 */     if (f4 > 1.0F)
/*     */     {
/* 158 */       f4 = 1.0F;
/*     */     }
/*     */     
/* 161 */     GlStateManager.enableRescaleNormal();
/* 162 */     MODEL_BOOK.render(null, 0.0F, f3, f4, f2, 0.0F, 0.0625F);
/* 163 */     GlStateManager.disableRescaleNormal();
/* 164 */     RenderHelper.disableStandardItemLighting();
/* 165 */     GlStateManager.matrixMode(5889);
/* 166 */     GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/* 167 */     GlStateManager.popMatrix();
/* 168 */     GlStateManager.matrixMode(5888);
/* 169 */     GlStateManager.popMatrix();
/* 170 */     RenderHelper.disableStandardItemLighting();
/* 171 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 172 */     EnchantmentNameParts.getInstance().reseedRandomGenerator(this.container.xpSeed);
/* 173 */     int k = this.container.getLapisAmount();
/*     */     
/* 175 */     for (int l = 0; l < 3; l++)
/*     */     {
/* 177 */       int i1 = i + 60;
/* 178 */       int j1 = i1 + 20;
/* 179 */       int k1 = 86;
/* 180 */       String s = EnchantmentNameParts.getInstance().generateNewRandomName();
/* 181 */       this.zLevel = 0.0F;
/* 182 */       this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
/* 183 */       int l1 = this.container.enchantLevels[l];
/* 184 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 186 */       if (l1 == 0)
/*     */       {
/* 188 */         drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
/*     */       }
/*     */       else
/*     */       {
/* 192 */         String s1 = l1;
/* 193 */         FontRenderer fontrenderer = this.mc.standardGalacticFontRenderer;
/* 194 */         int i2 = 6839882;
/*     */         
/* 196 */         if (((k < l + 1) || (this.mc.thePlayer.experienceLevel < l1)) && (!this.mc.thePlayer.capabilities.isCreativeMode))
/*     */         {
/* 198 */           drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
/* 199 */           drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
/* 200 */           fontrenderer.drawSplitString(s, j1, j + 16 + 19 * l, k1, (i2 & 0xFEFEFE) >> 1);
/* 201 */           i2 = 4226832;
/*     */         }
/*     */         else
/*     */         {
/* 205 */           int j2 = mouseX - (i + 60);
/* 206 */           int k2 = mouseY - (j + 14 + 19 * l);
/*     */           
/* 208 */           if ((j2 >= 0) && (k2 >= 0) && (j2 < 108) && (k2 < 19))
/*     */           {
/* 210 */             drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 204, 108, 19);
/* 211 */             i2 = 16777088;
/*     */           }
/*     */           else
/*     */           {
/* 215 */             drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 166, 108, 19);
/*     */           }
/*     */           
/* 218 */           drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
/* 219 */           fontrenderer.drawSplitString(s, j1, j + 16 + 19 * l, k1, i2);
/* 220 */           i2 = 8453920;
/*     */         }
/*     */         
/* 223 */         fontrenderer = this.mc.fontRendererObj;
/* 224 */         fontrenderer.drawStringWithShadow(s1, j1 + 86 - fontrenderer.getStringWidth(s1), j + 16 + 19 * l + 7, i2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 234 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 235 */     boolean flag = this.mc.thePlayer.capabilities.isCreativeMode;
/* 236 */     int i = this.container.getLapisAmount();
/*     */     
/* 238 */     for (int j = 0; j < 3; j++)
/*     */     {
/* 240 */       int k = this.container.enchantLevels[j];
/* 241 */       int l = this.container.field_178151_h[j];
/* 242 */       int i1 = j + 1;
/*     */       
/* 244 */       if ((isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY)) && (k > 0) && (l >= 0))
/*     */       {
/* 246 */         List<String> list = com.google.common.collect.Lists.newArrayList();
/*     */         
/* 248 */         if ((l >= 0) && (Enchantment.getEnchantmentById(l & 0xFF) != null))
/*     */         {
/* 250 */           String s = Enchantment.getEnchantmentById(l & 0xFF).getTranslatedName((l & 0xFF00) >> 8);
/* 251 */           list.add(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.ITALIC.toString() + I18n.format("container.enchant.clue", new Object[] { s }));
/*     */         }
/*     */         
/* 254 */         if (!flag)
/*     */         {
/* 256 */           if (l >= 0)
/*     */           {
/* 258 */             list.add("");
/*     */           }
/*     */           
/* 261 */           if (this.mc.thePlayer.experienceLevel < k)
/*     */           {
/* 263 */             list.add(EnumChatFormatting.RED.toString() + "Level Requirement: " + this.container.enchantLevels[j]);
/*     */           }
/*     */           else
/*     */           {
/* 267 */             String s1 = "";
/*     */             
/* 269 */             if (i1 == 1)
/*     */             {
/* 271 */               s1 = I18n.format("container.enchant.lapis.one", new Object[0]);
/*     */             }
/*     */             else
/*     */             {
/* 275 */               s1 = I18n.format("container.enchant.lapis.many", new Object[] { Integer.valueOf(i1) });
/*     */             }
/*     */             
/* 278 */             if (i >= i1)
/*     */             {
/* 280 */               list.add(EnumChatFormatting.GRAY.toString() + s1);
/*     */             }
/*     */             else
/*     */             {
/* 284 */               list.add(EnumChatFormatting.RED.toString() + s1);
/*     */             }
/*     */             
/* 287 */             if (i1 == 1)
/*     */             {
/* 289 */               s1 = I18n.format("container.enchant.level.one", new Object[0]);
/*     */             }
/*     */             else
/*     */             {
/* 293 */               s1 = I18n.format("container.enchant.level.many", new Object[] { Integer.valueOf(i1) });
/*     */             }
/*     */             
/* 296 */             list.add(EnumChatFormatting.GRAY.toString() + s1);
/*     */           }
/*     */         }
/*     */         
/* 300 */         drawHoveringText(list, mouseX, mouseY);
/* 301 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_147068_g()
/*     */   {
/* 308 */     ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();
/*     */     
/* 310 */     if (!ItemStack.areItemStacksEqual(itemstack, this.field_147077_B))
/*     */     {
/* 312 */       this.field_147077_B = itemstack;
/*     */       
/*     */       do
/*     */       {
/* 316 */         this.field_147082_x += this.random.nextInt(4) - this.random.nextInt(4);
/*     */       }
/* 318 */       while ((this.field_147071_v <= this.field_147082_x + 1.0F) && (this.field_147071_v >= this.field_147082_x - 1.0F));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 325 */     this.field_147073_u += 1;
/* 326 */     this.field_147069_w = this.field_147071_v;
/* 327 */     this.field_147076_A = this.field_147080_z;
/* 328 */     boolean flag = false;
/*     */     
/* 330 */     for (int i = 0; i < 3; i++)
/*     */     {
/* 332 */       if (this.container.enchantLevels[i] != 0)
/*     */       {
/* 334 */         flag = true;
/*     */       }
/*     */     }
/*     */     
/* 338 */     if (flag)
/*     */     {
/* 340 */       this.field_147080_z += 0.2F;
/*     */     }
/*     */     else
/*     */     {
/* 344 */       this.field_147080_z -= 0.2F;
/*     */     }
/*     */     
/* 347 */     this.field_147080_z = MathHelper.clamp_float(this.field_147080_z, 0.0F, 1.0F);
/* 348 */     float f1 = (this.field_147082_x - this.field_147071_v) * 0.4F;
/* 349 */     float f = 0.2F;
/* 350 */     f1 = MathHelper.clamp_float(f1, -f, f);
/* 351 */     this.field_147081_y += (f1 - this.field_147081_y) * 0.9F;
/* 352 */     this.field_147071_v += this.field_147081_y;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */