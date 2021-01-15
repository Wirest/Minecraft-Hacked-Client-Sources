/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ 
/*     */ public abstract class InventoryEffectRenderer extends GuiContainer
/*     */ {
/*     */   private boolean hasActivePotionEffects;
/*     */   
/*     */   public InventoryEffectRenderer(net.minecraft.inventory.Container inventorySlotsIn)
/*     */   {
/*  17 */     super(inventorySlotsIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  26 */     super.initGui();
/*  27 */     updateActivePotionEffects();
/*     */   }
/*     */   
/*     */   protected void updateActivePotionEffects()
/*     */   {
/*  32 */     if (!this.mc.thePlayer.getActivePotionEffects().isEmpty())
/*     */     {
/*  34 */       this.guiLeft = (160 + (width - this.xSize - 200) / 2);
/*  35 */       this.hasActivePotionEffects = true;
/*     */     }
/*     */     else
/*     */     {
/*  39 */       this.guiLeft = ((width - this.xSize) / 2);
/*  40 */       this.hasActivePotionEffects = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  49 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/*  51 */     if (this.hasActivePotionEffects)
/*     */     {
/*  53 */       drawActivePotionEffects();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void drawActivePotionEffects()
/*     */   {
/*  62 */     int i = this.guiLeft - 124;
/*  63 */     int j = this.guiTop;
/*  64 */     int k = 166;
/*  65 */     Collection<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffects();
/*     */     
/*  67 */     if (!collection.isEmpty())
/*     */     {
/*  69 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  70 */       GlStateManager.disableLighting();
/*  71 */       int l = 33;
/*     */       
/*  73 */       if (collection.size() > 5)
/*     */       {
/*  75 */         l = 132 / (collection.size() - 1);
/*     */       }
/*     */       
/*  78 */       for (PotionEffect potioneffect : this.mc.thePlayer.getActivePotionEffects())
/*     */       {
/*  80 */         Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
/*  81 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  82 */         this.mc.getTextureManager().bindTexture(inventoryBackground);
/*  83 */         drawTexturedModalRect(i, j, 0, 166, 140, 32);
/*     */         
/*  85 */         if (potion.hasStatusIcon())
/*     */         {
/*  87 */           int i1 = potion.getStatusIconIndex();
/*  88 */           drawTexturedModalRect(i + 6, j + 7, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
/*     */         }
/*     */         
/*  91 */         String s1 = I18n.format(potion.getName(), new Object[0]);
/*     */         
/*  93 */         if (potioneffect.getAmplifier() == 1)
/*     */         {
/*  95 */           s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
/*     */         }
/*  97 */         else if (potioneffect.getAmplifier() == 2)
/*     */         {
/*  99 */           s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
/*     */         }
/* 101 */         else if (potioneffect.getAmplifier() == 3)
/*     */         {
/* 103 */           s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
/*     */         }
/*     */         
/* 106 */         this.fontRendererObj.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
/* 107 */         String s = Potion.getDurationString(potioneffect);
/* 108 */         this.fontRendererObj.drawStringWithShadow(s, i + 10 + 18, j + 6 + 10, 8355711);
/* 109 */         j += l;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\InventoryEffectRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */