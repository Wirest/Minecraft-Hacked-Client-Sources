/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ 
/*     */ public class GuiSnooper extends GuiScreen
/*     */ {
/*     */   private final GuiScreen field_146608_a;
/*     */   private final GameSettings game_settings_2;
/*  16 */   private final List<String> field_146604_g = Lists.newArrayList();
/*  17 */   private final List<String> field_146609_h = Lists.newArrayList();
/*     */   private String field_146610_i;
/*     */   private String[] field_146607_r;
/*     */   private List field_146606_s;
/*     */   private GuiButton field_146605_t;
/*     */   
/*     */   public GuiSnooper(GuiScreen p_i1061_1_, GameSettings p_i1061_2_)
/*     */   {
/*  25 */     this.field_146608_a = p_i1061_1_;
/*  26 */     this.game_settings_2 = p_i1061_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  35 */     this.field_146610_i = I18n.format("options.snooper.title", new Object[0]);
/*  36 */     String s = I18n.format("options.snooper.desc", new Object[0]);
/*  37 */     List<String> list = Lists.newArrayList();
/*     */     
/*  39 */     for (Object s1 : this.fontRendererObj.listFormattedStringToWidth(s, width - 30))
/*     */     {
/*  41 */       list.add((String)s1);
/*     */     }
/*     */     
/*  44 */     this.field_146607_r = ((String[])list.toArray(new String[list.size()]));
/*  45 */     this.field_146604_g.clear();
/*  46 */     this.field_146609_h.clear();
/*  47 */     this.buttonList.add(this.field_146605_t = new GuiButton(1, width / 2 - 152, height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED)));
/*  48 */     this.buttonList.add(new GuiButton(2, width / 2 + 2, height - 30, 150, 20, I18n.format("gui.done", new Object[0])));
/*  49 */     boolean flag = (this.mc.getIntegratedServer() != null) && (this.mc.getIntegratedServer().getPlayerUsageSnooper() != null);
/*     */     
/*  51 */     for (Object entry : new TreeMap(this.mc.getPlayerUsageSnooper().getCurrentStats()).entrySet())
/*     */     {
/*  53 */       this.field_146604_g.add((flag ? "C " : "") + (String)((Map.Entry)entry).getKey());
/*  54 */       this.field_146609_h.add(this.fontRendererObj.trimStringToWidth((String)((Map.Entry)entry).getValue(), width - 220));
/*     */     }
/*     */     
/*  57 */     if (flag)
/*     */     {
/*  59 */       for (Object entry1 : new TreeMap(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats()).entrySet())
/*     */       {
/*  61 */         this.field_146604_g.add("S " + (String)((Map.Entry)entry1).getKey());
/*  62 */         this.field_146609_h.add(this.fontRendererObj.trimStringToWidth((String)((Map.Entry)entry1).getValue(), width - 220));
/*     */       }
/*     */     }
/*     */     
/*  66 */     this.field_146606_s = new List();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleMouseInput()
/*     */     throws java.io.IOException
/*     */   {
/*  74 */     super.handleMouseInput();
/*  75 */     this.field_146606_s.handleMouseInput();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws java.io.IOException
/*     */   {
/*  83 */     if (button.enabled)
/*     */     {
/*  85 */       if (button.id == 2)
/*     */       {
/*  87 */         this.game_settings_2.saveOptions();
/*  88 */         this.game_settings_2.saveOptions();
/*  89 */         this.mc.displayGuiScreen(this.field_146608_a);
/*     */       }
/*     */       
/*  92 */       if (button.id == 1)
/*     */       {
/*  94 */         this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
/*  95 */         this.field_146605_t.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 105 */     drawDefaultBackground();
/* 106 */     this.field_146606_s.drawScreen(mouseX, mouseY, partialTicks);
/* 107 */     drawCenteredString(this.fontRendererObj, this.field_146610_i, width / 2, 8, 16777215);
/* 108 */     int i = 22;
/*     */     String[] arrayOfString;
/* 110 */     int j = (arrayOfString = this.field_146607_r).length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
/*     */       
/* 112 */       drawCenteredString(this.fontRendererObj, s, width / 2, i, 8421504);
/* 113 */       i += this.fontRendererObj.FONT_HEIGHT;
/*     */     }
/*     */     
/* 116 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   class List extends GuiSlot
/*     */   {
/*     */     public List()
/*     */     {
/* 123 */       super(GuiSnooper.width, GuiSnooper.height, 80, GuiSnooper.height - 40, GuiSnooper.this.fontRendererObj.FONT_HEIGHT + 1);
/*     */     }
/*     */     
/*     */     protected int getSize()
/*     */     {
/* 128 */       return GuiSnooper.this.field_146604_g.size();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
/*     */     
/*     */ 
/*     */     protected boolean isSelected(int slotIndex)
/*     */     {
/* 137 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     protected void drawBackground() {}
/*     */     
/*     */ 
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */     {
/* 146 */       GuiSnooper.this.fontRendererObj.drawString((String)GuiSnooper.this.field_146604_g.get(entryID), 10.0D, p_180791_3_, 16777215);
/* 147 */       GuiSnooper.this.fontRendererObj.drawString((String)GuiSnooper.this.field_146609_h.get(entryID), 230.0D, p_180791_3_, 16777215);
/*     */     }
/*     */     
/*     */     protected int getScrollBarX()
/*     */     {
/* 152 */       return this.width - 10;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiSnooper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */