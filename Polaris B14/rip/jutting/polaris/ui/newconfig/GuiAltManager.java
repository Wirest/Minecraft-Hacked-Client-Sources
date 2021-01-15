/*     */ package rip.jutting.polaris.ui.newconfig;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.utils.RenderUtils.R2DUtils;
/*     */ 
/*     */ public class GuiAltManager
/*     */   extends GuiScreen
/*     */ {
/*     */   private GuiButton login;
/*     */   private AltLoginThread loginThread;
/*     */   private int offset;
/*     */   public Alt selectedAlt;
/*     */   private String status;
/*     */   private Scanner scanner;
/*     */   
/*     */   public GuiAltManager()
/*     */   {
/*  45 */     this.selectedAlt = null;
/*  46 */     this.status = (EnumChatFormatting.GRAY + "xd");
/*     */     try {
/*  48 */       AltManager.registry.clear();
/*  49 */       URL url = new URL("http://51.38.119.240/configs/");
/*  50 */       this.scanner = new Scanner(url.openStream());
/*  51 */       while (this.scanner.hasNextLine()) {
/*  52 */         String xd = this.scanner.nextLine();
/*  53 */         if ((xd.contains("by")) && 
/*  54 */           (!xd.contains("<tr><th colspan="))) {
/*  55 */           String uh = xd.substring(78, 150);
/*  56 */           String mhm = uh.replace("%20", " ");
/*  57 */           String[] parsed = mhm.split("\\.txt\"");
/*  58 */           String[] again = parsed[0].split("\\ ");
/*  59 */           AltManager.registry.add(new Alt(again[0], again[2]));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  64 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void actionPerformed(GuiButton button) throws IOException {
/*  69 */     switch (button.id) {
/*     */     case 0: 
/*  71 */       this.mc.displayGuiScreen(null);
/*  72 */       break;
/*     */     
/*     */     case 1: 
/*  75 */       String config = this.selectedAlt.getUsername();
/*  76 */       String name = this.selectedAlt.getPassword();
/*  77 */       String lolxd = config + name;
/*  78 */       String wow = config + " by " + name + ".txt";
/*  79 */       String oof = wow.replace(" ", "%20");
/*  80 */       URL url = verify("http://51.38.119.240/configs/" + oof);
/*  81 */       saveFile(url, System.getenv("APPDATA") + "/.minecraft/Polaris/" + lolxd + ".txt");
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   private static URL verify(String url)
/*     */   {
/*  88 */     if (!url.toLowerCase().startsWith("http://")) {
/*  89 */       return null;
/*     */     }
/*  91 */     URL verifyUrl = null;
/*     */     try
/*     */     {
/*  94 */       verifyUrl = new URL(url);
/*     */     } catch (Exception e) {
/*  96 */       e.printStackTrace();
/*     */     }
/*  98 */     return verifyUrl;
/*     */   }
/*     */   
/*     */   public static boolean saveFile(URL fileURL, String fileSavePath)
/*     */   {
/* 103 */     boolean isSucceed = true;
/*     */     
/* 105 */     CloseableHttpClient httpClient = HttpClients.createDefault();
/*     */     
/* 107 */     HttpGet httpGet = new HttpGet(fileURL.toString());
/* 108 */     httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
/* 109 */     httpGet.addHeader("Referer", "https://www.google.com");
/*     */     try
/*     */     {
/* 112 */       CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
/* 113 */       HttpEntity fileEntity = httpResponse.getEntity();
/*     */       
/* 115 */       if (fileEntity != null) {
/* 116 */         FileUtils.copyInputStreamToFile(fileEntity.getContent(), new File(fileSavePath));
/* 117 */         System.out.println("Success!");
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 121 */       isSucceed = false;
/* 122 */       System.out.println("Failed");
/*     */     }
/*     */     
/* 125 */     httpGet.releaseConnection();
/*     */     
/* 127 */     return isSucceed;
/*     */   }
/*     */   
/*     */   public void drawScreen(int par1, int par2, float par3)
/*     */   {
/* 132 */     drawDefaultBackground();
/* 133 */     RenderUtils.R2DUtils.drawBorderedRect(100.0F, 33.0F, width - 100, height - 50, 1.0F, 
/* 134 */       new Color(0, 0, 0, 30).getRGB(), new Color(0, 0, 0, 50).getRGB());
/* 135 */     CFontRenderer font = FontLoaders.vardana12;
/* 136 */     if (Mouse.hasWheel()) {
/* 137 */       int wheel = Mouse.getDWheel();
/* 138 */       if (wheel < 0) {
/* 139 */         this.offset += 26;
/* 140 */         if (this.offset < 0) {
/* 141 */           this.offset = 0;
/*     */         }
/* 143 */       } else if (wheel > 0) {
/* 144 */         this.offset -= 26;
/* 145 */         if (this.offset < 0) {
/* 146 */           this.offset = 0;
/*     */         }
/*     */       }
/*     */     }
/* 150 */     GL11.glPushMatrix();
/* 151 */     prepareScissorBox(0.0F, 33.0F, width, height - 50);
/* 152 */     GL11.glEnable(3089);
/* 153 */     int y = 38;
/* 154 */     for (Alt alt : AltManager.registry) {
/* 155 */       if (isAltInArea(y)) { String name;
/*     */         String name;
/* 157 */         if (alt.getMask().equals("")) {
/* 158 */           name = alt.getUsername();
/*     */         } else
/* 160 */           name = alt.getMask();
/*     */         String pass;
/*     */         String pass;
/* 163 */         if (alt.getPassword().equals("")) {
/* 164 */           pass = "§cCracked";
/*     */         } else {
/* 166 */           pass = alt.getPassword();
/*     */         }
/* 168 */         if (alt == this.selectedAlt) {
/* 169 */           if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
/* 170 */             RenderUtils.R2DUtils.drawBorderedRect(102.0F, y - this.offset - 4, width - 102, 
/* 171 */               y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 172 */               new Color(0, 0, 0, 60).getRGB());
/* 173 */           } else if (isMouseOverAlt(par1, par2, y - this.offset)) {
/* 174 */             RenderUtils.R2DUtils.drawBorderedRect(102.0F, y - this.offset - 4, width - 102, 
/* 175 */               y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 176 */               new Color(0, 0, 0, 60).getRGB());
/*     */           } else {
/* 178 */             RenderUtils.R2DUtils.drawBorderedRect(102.0F, y - this.offset - 4, width - 102, 
/* 179 */               y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 180 */               new Color(0, 0, 0, 60).getRGB());
/*     */           }
/* 182 */         } else if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
/* 183 */           RenderUtils.R2DUtils.drawBorderedRect(102.0F, y - this.offset - 4, width - 102, 
/* 184 */             y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 185 */             new Color(0, 0, 0, 60).getRGB());
/* 186 */         } else if (isMouseOverAlt(par1, par2, y - this.offset)) {
/* 187 */           RenderUtils.R2DUtils.drawBorderedRect(102.0F, y - this.offset - 4, width - 102, 
/* 188 */             y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 189 */             new Color(0, 0, 0, 60).getRGB());
/*     */         }
/* 191 */         font.drawCenteredString(name, width / 2, y - this.offset, 
/* 192 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 193 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 194 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/* 195 */           .getRGB());
/* 196 */         font.drawCenteredString(pass, width / 2, y - this.offset + 10, -1);
/* 197 */         y += 26;
/*     */       }
/*     */     }
/* 200 */     GL11.glDisable(3089);
/* 201 */     GL11.glPopMatrix();
/* 202 */     FontRenderer fontRendererObj = this.fontRendererObj;
/* 203 */     StringBuilder sb = new StringBuilder("Account Manager - ");
/* 204 */     font.drawCenteredString(String.format("Configs uploaded by other client users", new Object[] { Integer.valueOf(4) }), width / 2, 13.0F, -1);
/* 205 */     font.drawCenteredString(String.format("Configs: %s", new Object[] { Integer.valueOf(AltManager.registry.size()) }), width / 2, 23.0F, -1);
/* 206 */     super.drawScreen(par1, par2, par3);
/* 207 */     if (this.selectedAlt == null) {
/* 208 */       this.login.enabled = false;
/*     */     }
/*     */     else
/*     */     {
/* 212 */       this.login.enabled = true;
/*     */     }
/*     */     
/*     */ 
/* 216 */     if (Keyboard.isKeyDown(200)) {
/* 217 */       this.offset -= 26;
/* 218 */       if (this.offset < 0) {
/* 219 */         this.offset = 0;
/*     */       }
/* 221 */     } else if (Keyboard.isKeyDown(208)) {
/* 222 */       this.offset += 26;
/* 223 */       if (this.offset < 0) {
/* 224 */         this.offset = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void initGui()
/*     */   {
/* 231 */     this.buttonList.add(new GuiButton(0, width / 2 - 76, height - 26, 149, 20, "Back"));
/* 232 */     this.buttonList.add(this.login = new GuiButton(1, width / 2 - 76, height - 47, 149, 20, "Download"));
/* 233 */     this.login.enabled = false;
/*     */   }
/*     */   
/*     */   private boolean isAltInArea(int y) {
/* 237 */     return y - this.offset <= height - 50;
/*     */   }
/*     */   
/*     */   private boolean isMouseOverAlt(int x, int y, int y1) {
/* 241 */     return (x >= 52) && (y >= y1 - 4) && (x <= width - 52) && (y <= y1 + 20) && (x >= 0) && (y >= 33) && 
/* 242 */       (x <= width) && (y <= height - 50);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int par1, int par2, int par3) throws IOException
/*     */   {
/* 247 */     if (this.offset < 0) {
/* 248 */       this.offset = 0;
/*     */     }
/* 250 */     int y = 38 - this.offset;
/* 251 */     for (Alt alt : AltManager.registry) {
/* 252 */       if (isMouseOverAlt(par1, par2, y)) {
/* 253 */         if (alt == this.selectedAlt)
/*     */         {
/* 255 */           return;
/*     */         }
/* 257 */         this.selectedAlt = alt;
/*     */       }
/* 259 */       y += 26;
/*     */     }
/*     */     try {
/* 262 */       super.mouseClicked(par1, par2, par3);
/*     */     } catch (IOException e) {
/* 264 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void prepareScissorBox(float x, float y, float x2, float y2) {
/* 269 */     ScaledResolution scale = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/* 270 */     int factor = scale.getScaleFactor();
/* 271 */     GL11.glScissor((int)(x * factor), (int)((ScaledResolution.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), 
/* 272 */       (int)((y2 - y) * factor));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\newconfig\GuiAltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */