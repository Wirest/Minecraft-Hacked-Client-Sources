/*     */ package net.minecraft.client.gui.stream;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiOptionButton;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ import net.minecraft.client.stream.IStream;
/*     */ 
/*     */ public class GuiStreamOptions extends net.minecraft.client.gui.GuiScreen
/*     */ {
/*  14 */   private static final GameSettings.Options[] field_152312_a = { GameSettings.Options.STREAM_BYTES_PER_PIXEL, GameSettings.Options.STREAM_FPS, GameSettings.Options.STREAM_KBPS, GameSettings.Options.STREAM_SEND_METADATA, GameSettings.Options.STREAM_VOLUME_MIC, GameSettings.Options.STREAM_VOLUME_SYSTEM, GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR, GameSettings.Options.STREAM_COMPRESSION };
/*  15 */   private static final GameSettings.Options[] field_152316_f = { GameSettings.Options.STREAM_CHAT_ENABLED, GameSettings.Options.STREAM_CHAT_USER_FILTER };
/*     */   private final net.minecraft.client.gui.GuiScreen parentScreen;
/*     */   private final GameSettings field_152318_h;
/*     */   private String field_152319_i;
/*     */   private String field_152313_r;
/*     */   private int field_152314_s;
/*  21 */   private boolean field_152315_t = false;
/*     */   
/*     */   public GuiStreamOptions(net.minecraft.client.gui.GuiScreen parentScreenIn, GameSettings p_i1073_2_)
/*     */   {
/*  25 */     this.parentScreen = parentScreenIn;
/*  26 */     this.field_152318_h = p_i1073_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  35 */     int i = 0;
/*  36 */     this.field_152319_i = I18n.format("options.stream.title", new Object[0]);
/*  37 */     this.field_152313_r = I18n.format("options.stream.chat.title", new Object[0]);
/*     */     GameSettings.Options[] arrayOfOptions;
/*  39 */     int j = (arrayOfOptions = field_152312_a).length; for (int i = 0; i < j; i++) { GameSettings.Options gamesettings$options = arrayOfOptions[i];
/*     */       
/*  41 */       if (gamesettings$options.getEnumFloat())
/*     */       {
/*  43 */         this.buttonList.add(new net.minecraft.client.gui.GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), gamesettings$options));
/*     */       }
/*     */       else
/*     */       {
/*  47 */         this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), gamesettings$options, this.field_152318_h.getKeyBinding(gamesettings$options)));
/*     */       }
/*     */       
/*  50 */       i++;
/*     */     }
/*     */     
/*  53 */     if (i % 2 == 1)
/*     */     {
/*  55 */       i++;
/*     */     }
/*     */     
/*  58 */     this.field_152314_s = (height / 6 + 24 * (i >> 1) + 6);
/*  59 */     i += 2;
/*     */     
/*  61 */     j = (arrayOfOptions = field_152316_f).length; for (i = 0; i < j; i++) { GameSettings.Options gamesettings$options1 = arrayOfOptions[i];
/*     */       
/*  63 */       if (gamesettings$options1.getEnumFloat())
/*     */       {
/*  65 */         this.buttonList.add(new net.minecraft.client.gui.GuiOptionSlider(gamesettings$options1.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), gamesettings$options1));
/*     */       }
/*     */       else
/*     */       {
/*  69 */         this.buttonList.add(new GuiOptionButton(gamesettings$options1.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), gamesettings$options1, this.field_152318_h.getKeyBinding(gamesettings$options1)));
/*     */       }
/*     */       
/*  72 */       i++;
/*     */     }
/*     */     
/*  75 */     this.buttonList.add(new GuiButton(200, width / 2 - 155, height / 6 + 168, 150, 20, I18n.format("gui.done", new Object[0])));
/*  76 */     GuiButton guibutton = new GuiButton(201, width / 2 + 5, height / 6 + 168, 150, 20, I18n.format("options.stream.ingestSelection", new Object[0]));
/*  77 */     guibutton.enabled = (((this.mc.getTwitchStream().isReadyToBroadcast()) && (this.mc.getTwitchStream().func_152925_v().length > 0)) || (this.mc.getTwitchStream().func_152908_z()));
/*  78 */     this.buttonList.add(guibutton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws java.io.IOException
/*     */   {
/*  86 */     if (button.enabled)
/*     */     {
/*  88 */       if ((button.id < 100) && ((button instanceof GuiOptionButton)))
/*     */       {
/*  90 */         GameSettings.Options gamesettings$options = ((GuiOptionButton)button).returnEnumOptions();
/*  91 */         this.field_152318_h.setOptionValue(gamesettings$options, 1);
/*  92 */         button.displayString = this.field_152318_h.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*     */         
/*  94 */         if ((this.mc.getTwitchStream().isBroadcasting()) && (gamesettings$options != GameSettings.Options.STREAM_CHAT_ENABLED) && (gamesettings$options != GameSettings.Options.STREAM_CHAT_USER_FILTER))
/*     */         {
/*  96 */           this.field_152315_t = true;
/*     */         }
/*     */       }
/*  99 */       else if ((button instanceof net.minecraft.client.gui.GuiOptionSlider))
/*     */       {
/* 101 */         if (button.id == GameSettings.Options.STREAM_VOLUME_MIC.returnEnumOrdinal())
/*     */         {
/* 103 */           this.mc.getTwitchStream().updateStreamVolume();
/*     */         }
/* 105 */         else if (button.id == GameSettings.Options.STREAM_VOLUME_SYSTEM.returnEnumOrdinal())
/*     */         {
/* 107 */           this.mc.getTwitchStream().updateStreamVolume();
/*     */         }
/* 109 */         else if (this.mc.getTwitchStream().isBroadcasting())
/*     */         {
/* 111 */           this.field_152315_t = true;
/*     */         }
/*     */       }
/*     */       
/* 115 */       if (button.id == 200)
/*     */       {
/* 117 */         this.mc.gameSettings.saveOptions();
/* 118 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/* 120 */       else if (button.id == 201)
/*     */       {
/* 122 */         this.mc.gameSettings.saveOptions();
/* 123 */         this.mc.displayGuiScreen(new GuiIngestServers(this));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 133 */     drawDefaultBackground();
/* 134 */     drawCenteredString(this.fontRendererObj, this.field_152319_i, width / 2, 20, 16777215);
/* 135 */     drawCenteredString(this.fontRendererObj, this.field_152313_r, width / 2, this.field_152314_s, 16777215);
/*     */     
/* 137 */     if (this.field_152315_t)
/*     */     {
/* 139 */       drawCenteredString(this.fontRendererObj, net.minecraft.util.EnumChatFormatting.RED + I18n.format("options.stream.changes", new Object[0]), width / 2, 20 + this.fontRendererObj.FONT_HEIGHT, 16777215);
/*     */     }
/*     */     
/* 142 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\stream\GuiStreamOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */