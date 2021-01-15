/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.audio.SoundCategory;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class GuiScreenOptionsSounds extends GuiScreen
/*     */ {
/*     */   private final GuiScreen field_146505_f;
/*     */   private final GameSettings game_settings_4;
/*  17 */   protected String field_146507_a = "Options";
/*     */   private String field_146508_h;
/*     */   
/*     */   public GuiScreenOptionsSounds(GuiScreen p_i45025_1_, GameSettings p_i45025_2_) {
/*  21 */     this.field_146505_f = p_i45025_1_;
/*  22 */     this.game_settings_4 = p_i45025_2_;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  26 */     int i = 0;
/*  27 */     this.field_146507_a = I18n.format("options.sounds.title", new Object[0]);
/*  28 */     this.field_146508_h = I18n.format("options.off", new Object[0]);
/*  29 */     this.buttonList.add(new Button(SoundCategory.MASTER.getCategoryId(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), SoundCategory.MASTER, true));
/*  30 */     i += 2;
/*     */     SoundCategory[] arrayOfSoundCategory;
/*  32 */     int j = (arrayOfSoundCategory = SoundCategory.values()).length; for (int i = 0; i < j; i++) { SoundCategory soundcategory = arrayOfSoundCategory[i];
/*  33 */       if (soundcategory != SoundCategory.MASTER) {
/*  34 */         this.buttonList.add(new Button(soundcategory.getCategoryId(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), soundcategory, false));
/*  35 */         i++;
/*     */       }
/*     */     }
/*     */     
/*  39 */     this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/*  43 */     if ((button.enabled) && 
/*  44 */       (button.id == 200)) {
/*  45 */       this.mc.gameSettings.saveOptions();
/*  46 */       this.mc.displayGuiScreen(this.field_146505_f);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  52 */     drawDefaultBackground();
/*  53 */     drawCenteredString(this.fontRendererObj, this.field_146507_a, width / 2, 15, 16777215);
/*  54 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   protected String getSoundVolume(SoundCategory p_146504_1_) {
/*  58 */     float f = this.game_settings_4.getSoundLevel(p_146504_1_);
/*  59 */     return (int)(f * 100.0F) + "%";
/*     */   }
/*     */   
/*     */   class Button extends GuiButton {
/*     */     private final SoundCategory field_146153_r;
/*     */     private final String field_146152_s;
/*  65 */     public float field_146156_o = 1.0F;
/*     */     public boolean field_146155_p;
/*     */     
/*     */     public Button(int p_i45024_2_, int p_i45024_3_, int p_i45024_4_, SoundCategory p_i45024_5_, boolean p_i45024_6_) {
/*  69 */       super(p_i45024_3_, p_i45024_4_, p_i45024_6_ ? 310 : 150, 20, "");
/*  70 */       this.field_146153_r = p_i45024_5_;
/*  71 */       this.field_146152_s = I18n.format("soundCategory." + p_i45024_5_.getCategoryName(), new Object[0]);
/*  72 */       this.displayString = (this.field_146152_s + ": " + GuiScreenOptionsSounds.this.getSoundVolume(p_i45024_5_));
/*  73 */       this.field_146156_o = GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(p_i45024_5_);
/*     */     }
/*     */     
/*     */     protected int getHoverState(boolean mouseOver) {
/*  77 */       return 0;
/*     */     }
/*     */     
/*     */     protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
/*  81 */       if (this.visible) {
/*  82 */         if (this.field_146155_p) {
/*  83 */           this.field_146156_o = ((mouseX - (this.xPosition + 4)) / (this.width - 8));
/*  84 */           this.field_146156_o = MathHelper.clamp_float(this.field_146156_o, 0.0F, 1.0F);
/*  85 */           mc.gameSettings.setSoundLevel(this.field_146153_r, this.field_146156_o);
/*  86 */           mc.gameSettings.saveOptions();
/*  87 */           this.displayString = (this.field_146152_s + ": " + GuiScreenOptionsSounds.this.getSoundVolume(this.field_146153_r));
/*     */         }
/*     */         
/*  90 */         net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  91 */         drawTexturedModalRect(this.xPosition + (int)(this.field_146156_o * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
/*  92 */         drawTexturedModalRect(this.xPosition + (int)(this.field_146156_o * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/*  97 */       if (super.mousePressed(mc, mouseX, mouseY)) {
/*  98 */         this.field_146156_o = ((mouseX - (this.xPosition + 4)) / (this.width - 8));
/*  99 */         this.field_146156_o = MathHelper.clamp_float(this.field_146156_o, 0.0F, 1.0F);
/* 100 */         mc.gameSettings.setSoundLevel(this.field_146153_r, this.field_146156_o);
/* 101 */         mc.gameSettings.saveOptions();
/* 102 */         this.displayString = (this.field_146152_s + ": " + GuiScreenOptionsSounds.this.getSoundVolume(this.field_146153_r));
/* 103 */         this.field_146155_p = true;
/* 104 */         return true;
/*     */       }
/* 106 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public void playPressSound(SoundHandler soundHandlerIn) {}
/*     */     
/*     */     public void mouseReleased(int mouseX, int mouseY)
/*     */     {
/* 114 */       if (this.field_146155_p) {
/* 115 */         if (this.field_146153_r == SoundCategory.MASTER) {
/* 116 */           float f = 1.0F;
/*     */         } else {
/* 118 */           GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(this.field_146153_r);
/*     */         }
/*     */         
/* 121 */         GuiScreenOptionsSounds.this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
/*     */       }
/*     */       
/* 124 */       this.field_146155_p = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreenOptionsSounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */