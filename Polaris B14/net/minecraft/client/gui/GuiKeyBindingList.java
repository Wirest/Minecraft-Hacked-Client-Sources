/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class GuiKeyBindingList extends GuiListExtended
/*     */ {
/*     */   private final GuiControls field_148191_k;
/*     */   private final Minecraft mc;
/*     */   private final GuiListExtended.IGuiListEntry[] listEntries;
/*  16 */   private int maxListLabelWidth = 0;
/*     */   
/*     */   public GuiKeyBindingList(GuiControls controls, Minecraft mcIn)
/*     */   {
/*  20 */     super(mcIn, GuiControls.width, GuiControls.height, 63, GuiControls.height - 32, 20);
/*  21 */     this.field_148191_k = controls;
/*  22 */     this.mc = mcIn;
/*  23 */     KeyBinding[] akeybinding = (KeyBinding[])org.apache.commons.lang3.ArrayUtils.clone(mcIn.gameSettings.keyBindings);
/*  24 */     this.listEntries = new GuiListExtended.IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
/*  25 */     Arrays.sort(akeybinding);
/*  26 */     int i = 0;
/*  27 */     String s = null;
/*     */     KeyBinding[] arrayOfKeyBinding1;
/*  29 */     int j = (arrayOfKeyBinding1 = akeybinding).length; for (int i = 0; i < j; i++) { KeyBinding keybinding = arrayOfKeyBinding1[i];
/*     */       
/*  31 */       String s1 = keybinding.getKeyCategory();
/*     */       
/*  33 */       if (!s1.equals(s))
/*     */       {
/*  35 */         s = s1;
/*  36 */         this.listEntries[(i++)] = new CategoryEntry(s1);
/*     */       }
/*     */       
/*  39 */       int j = mcIn.fontRendererObj.getStringWidth(I18n.format(keybinding.getKeyDescription(), new Object[0]));
/*     */       
/*  41 */       if (j > this.maxListLabelWidth)
/*     */       {
/*  43 */         this.maxListLabelWidth = j;
/*     */       }
/*     */       
/*  46 */       this.listEntries[(i++)] = new KeyEntry(keybinding, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected int getSize()
/*     */   {
/*  52 */     return this.listEntries.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GuiListExtended.IGuiListEntry getListEntry(int index)
/*     */   {
/*  60 */     return this.listEntries[index];
/*     */   }
/*     */   
/*     */   protected int getScrollBarX()
/*     */   {
/*  65 */     return super.getScrollBarX() + 15;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getListWidth()
/*     */   {
/*  73 */     return super.getListWidth() + 32;
/*     */   }
/*     */   
/*     */   public class CategoryEntry implements GuiListExtended.IGuiListEntry
/*     */   {
/*     */     private final String labelText;
/*     */     private final int labelWidth;
/*     */     
/*     */     public CategoryEntry(String p_i45028_2_)
/*     */     {
/*  83 */       this.labelText = I18n.format(p_i45028_2_, new Object[0]);
/*  84 */       this.labelWidth = GuiKeyBindingList.this.mc.fontRendererObj.getStringWidth(this.labelText);
/*     */     }
/*     */     
/*     */     public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
/*     */     {
/*  89 */       GuiKeyBindingList.this.mc.fontRendererObj.drawString(this.labelText, GuiScreen.width / 2 - this.labelWidth / 2, y + slotHeight - GuiKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT - 1, 16777215);
/*     */     }
/*     */     
/*     */     public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
/*     */     {
/*  94 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
/*     */     
/*     */ 
/*     */     public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
/*     */   }
/*     */   
/*     */ 
/*     */   public class KeyEntry
/*     */     implements GuiListExtended.IGuiListEntry
/*     */   {
/*     */     private final KeyBinding keybinding;
/*     */     private final String keyDesc;
/*     */     private final GuiButton btnChangeKeyBinding;
/*     */     private final GuiButton btnReset;
/*     */     
/*     */     private KeyEntry(KeyBinding p_i45029_2_)
/*     */     {
/* 115 */       this.keybinding = p_i45029_2_;
/* 116 */       this.keyDesc = I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]);
/* 117 */       this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]));
/* 118 */       this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset", new Object[0]));
/*     */     }
/*     */     
/*     */     public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
/*     */     {
/* 123 */       boolean flag = GuiKeyBindingList.this.field_148191_k.buttonId == this.keybinding;
/* 124 */       GuiKeyBindingList.this.mc.fontRendererObj.drawString(this.keyDesc, x + 90 - GuiKeyBindingList.this.maxListLabelWidth, y + slotHeight / 2 - GuiKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT / 2, 16777215);
/* 125 */       this.btnReset.xPosition = (x + 190);
/* 126 */       this.btnReset.yPosition = y;
/* 127 */       this.btnReset.enabled = (this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault());
/* 128 */       this.btnReset.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
/* 129 */       this.btnChangeKeyBinding.xPosition = (x + 105);
/* 130 */       this.btnChangeKeyBinding.yPosition = y;
/* 131 */       this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());
/* 132 */       boolean flag1 = false;
/*     */       
/* 134 */       if (this.keybinding.getKeyCode() != 0) {
/*     */         KeyBinding[] arrayOfKeyBinding;
/* 136 */         int j = (arrayOfKeyBinding = GuiKeyBindingList.this.mc.gameSettings.keyBindings).length; for (int i = 0; i < j; i++) { KeyBinding keybinding = arrayOfKeyBinding[i];
/*     */           
/* 138 */           if ((keybinding != this.keybinding) && (keybinding.getKeyCode() == this.keybinding.getKeyCode()))
/*     */           {
/* 140 */             flag1 = true;
/* 141 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 146 */       if (flag)
/*     */       {
/* 148 */         this.btnChangeKeyBinding.displayString = (EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <");
/*     */       }
/* 150 */       else if (flag1)
/*     */       {
/* 152 */         this.btnChangeKeyBinding.displayString = (EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString);
/*     */       }
/*     */       
/* 155 */       this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
/*     */     }
/*     */     
/*     */     public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
/*     */     {
/* 160 */       if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_))
/*     */       {
/* 162 */         GuiKeyBindingList.this.field_148191_k.buttonId = this.keybinding;
/* 163 */         return true;
/*     */       }
/* 165 */       if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_))
/*     */       {
/* 167 */         GuiKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
/* 168 */         KeyBinding.resetKeyBindingArrayAndHash();
/* 169 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 173 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
/*     */     {
/* 179 */       this.btnChangeKeyBinding.mouseReleased(x, y);
/* 180 */       this.btnReset.mouseReleased(x, y);
/*     */     }
/*     */     
/*     */     public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiKeyBindingList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */