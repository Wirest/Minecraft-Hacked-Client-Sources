/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ 
/*     */ public class GuiControls extends GuiScreen
/*     */ {
/*  11 */   private static final GameSettings.Options[] optionsArr = { GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN };
/*     */   
/*     */ 
/*     */   private GuiScreen parentScreen;
/*     */   
/*     */ 
/*  17 */   protected String screenTitle = "Controls";
/*     */   
/*     */ 
/*     */   private GameSettings options;
/*     */   
/*     */ 
/*  23 */   public KeyBinding buttonId = null;
/*     */   public long time;
/*     */   private GuiKeyBindingList keyBindingList;
/*     */   private GuiButton buttonReset;
/*     */   
/*     */   public GuiControls(GuiScreen screen, GameSettings settings)
/*     */   {
/*  30 */     this.parentScreen = screen;
/*  31 */     this.options = settings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  40 */     this.keyBindingList = new GuiKeyBindingList(this, this.mc);
/*  41 */     this.buttonList.add(new GuiButton(200, width / 2 - 155, height - 29, 150, 20, net.minecraft.client.resources.I18n.format("gui.done", new Object[0])));
/*  42 */     this.buttonList.add(this.buttonReset = new GuiButton(201, width / 2 - 155 + 160, height - 29, 150, 20, net.minecraft.client.resources.I18n.format("controls.resetAll", new Object[0])));
/*  43 */     this.screenTitle = net.minecraft.client.resources.I18n.format("controls.title", new Object[0]);
/*  44 */     int i = 0;
/*     */     GameSettings.Options[] arrayOfOptions;
/*  46 */     int j = (arrayOfOptions = optionsArr).length; for (int i = 0; i < j; i++) { GameSettings.Options gamesettings$options = arrayOfOptions[i];
/*     */       
/*  48 */       if (gamesettings$options.getEnumFloat())
/*     */       {
/*  50 */         this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options));
/*     */       }
/*     */       else
/*     */       {
/*  54 */         this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.options.getKeyBinding(gamesettings$options)));
/*     */       }
/*     */       
/*  57 */       i++;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleMouseInput()
/*     */     throws IOException
/*     */   {
/*  66 */     super.handleMouseInput();
/*  67 */     this.keyBindingList.handleMouseInput();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws IOException
/*     */   {
/*  75 */     if (button.id == 200)
/*     */     {
/*  77 */       this.mc.displayGuiScreen(this.parentScreen);
/*     */     }
/*  79 */     else if (button.id == 201) {
/*     */       KeyBinding[] arrayOfKeyBinding;
/*  81 */       int j = (arrayOfKeyBinding = this.mc.gameSettings.keyBindings).length; for (int i = 0; i < j; i++) { KeyBinding keybinding = arrayOfKeyBinding[i];
/*     */         
/*  83 */         keybinding.setKeyCode(keybinding.getKeyCodeDefault());
/*     */       }
/*     */       
/*  86 */       KeyBinding.resetKeyBindingArrayAndHash();
/*     */     }
/*  88 */     else if ((button.id < 100) && ((button instanceof GuiOptionButton)))
/*     */     {
/*  90 */       this.options.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/*  91 */       button.displayString = this.options.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/* 100 */     if (this.buttonId != null)
/*     */     {
/* 102 */       this.options.setOptionKeyBinding(this.buttonId, -100 + mouseButton);
/* 103 */       this.buttonId = null;
/* 104 */       KeyBinding.resetKeyBindingArrayAndHash();
/*     */     }
/* 106 */     else if ((mouseButton != 0) || (!this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)))
/*     */     {
/* 108 */       super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state)
/*     */   {
/* 117 */     if ((state != 0) || (!this.keyBindingList.mouseReleased(mouseX, mouseY, state)))
/*     */     {
/* 119 */       super.mouseReleased(mouseX, mouseY, state);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/* 129 */     if (this.buttonId != null)
/*     */     {
/* 131 */       if (keyCode == 1)
/*     */       {
/* 133 */         this.options.setOptionKeyBinding(this.buttonId, 0);
/*     */       }
/* 135 */       else if (keyCode != 0)
/*     */       {
/* 137 */         this.options.setOptionKeyBinding(this.buttonId, keyCode);
/*     */       }
/* 139 */       else if (typedChar > 0)
/*     */       {
/* 141 */         this.options.setOptionKeyBinding(this.buttonId, typedChar + 'Ā');
/*     */       }
/*     */       
/* 144 */       this.buttonId = null;
/* 145 */       this.time = net.minecraft.client.Minecraft.getSystemTime();
/* 146 */       KeyBinding.resetKeyBindingArrayAndHash();
/*     */     }
/*     */     else
/*     */     {
/* 150 */       super.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 159 */     drawDefaultBackground();
/* 160 */     this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
/* 161 */     drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, 8, 16777215);
/* 162 */     boolean flag = true;
/*     */     KeyBinding[] arrayOfKeyBinding;
/* 164 */     int j = (arrayOfKeyBinding = this.options.keyBindings).length; for (int i = 0; i < j; i++) { KeyBinding keybinding = arrayOfKeyBinding[i];
/*     */       
/* 166 */       if (keybinding.getKeyCode() != keybinding.getKeyCodeDefault())
/*     */       {
/* 168 */         flag = false;
/* 169 */         break;
/*     */       }
/*     */     }
/*     */     
/* 173 */     this.buttonReset.enabled = (!flag);
/* 174 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiControls.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */