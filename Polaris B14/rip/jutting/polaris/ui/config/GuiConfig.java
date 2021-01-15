/*     */ package rip.jutting.polaris.ui.config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiIngameMenu;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ 
/*     */ public final class GuiConfig extends GuiScreen
/*     */ {
/*     */   private final List<Config> config;
/*     */   private ConfigSlot configlot;
/*     */   private Scanner scanner;
/*     */   private String uh;
/*     */   
/*     */   public GuiConfig()
/*     */   {
/*  31 */     this.config = new ArrayList();
/*     */     try {
/*  33 */       URL url = new URL("http://51.38.119.240/configs/");
/*  34 */       this.scanner = new Scanner(url.openStream());
/*  35 */       while (this.scanner.hasNextLine()) {
/*  36 */         String xd = this.scanner.nextLine();
/*  37 */         if ((xd.contains("by")) && 
/*  38 */           (!xd.contains("<tr><th colspan="))) {
/*  39 */           String uh = xd.substring(78, 150);
/*  40 */           String mhm = uh.replace("%20", " ");
/*  41 */           String[] parsed = mhm.split("\\.txt\"");
/*  42 */           String[] again = parsed[0].split("\\ ");
/*  43 */           this.config.add(new Config(again[0], again[1] + " " + again[2]));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  48 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws IOException
/*     */   {
/*  54 */     super.handleMouseInput();
/*  55 */     this.configlot.handleMouseInput();
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  60 */     CFontRenderer font = FontLoaders.vardana12;
/*  61 */     this.configlot.drawScreen(mouseX, mouseY, partialTicks);
/*  62 */     font.drawCenteredString(String.format("Configs uploaded by other client users", new Object[] { Integer.valueOf(4) }), width / 2, 13.0F, -1);
/*  63 */     font.drawStringWithShadow(String.format("Configs: %s", new Object[] { Integer.valueOf(this.config.size()) }), 2.0D, 2.0D, -1);
/*  64 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public void initGui()
/*     */   {
/*  69 */     this.buttonList.clear();
/*  70 */     this.buttonList.add(new GuiButton(0, width / 2 - 76, height - 26, 149, 20, "Back"));
/*  71 */     this.buttonList.add(new GuiButton(1, width / 2 - 76, height - 50, 149, 20, "Download"));
/*  72 */     this.configlot = new ConfigSlot(this);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */   {
/*  77 */     if (keyCode == 200) {
/*  78 */       if (this.configlot.selected == 0) {
/*  79 */         this.configlot.selected = this.config.size();
/*     */       }
/*  81 */       ConfigSlot configlot = this.configlot;
/*  82 */       configlot.selected -= 1;
/*     */     }
/*  84 */     if (keyCode == 208) {
/*  85 */       if (this.configlot.selected == this.config.size()) {
/*  86 */         this.configlot.selected = 0;
/*     */       }
/*  88 */       ConfigSlot configlot2 = this.configlot;
/*  89 */       configlot2.selected += 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public void actionPerformed(GuiButton button) {
/*     */     try {
/*  95 */       super.actionPerformed(button);
/*     */     } catch (Exception e) {
/*  97 */       e.printStackTrace();
/*     */     }
/*  99 */     if (button.id == 0) {
/* 100 */       this.mc.displayGuiScreen(new GuiIngameMenu());
/*     */     }
/* 102 */     if (button.id == 1) {
/*     */       try {
/* 104 */         URL url = new URL("http://51.38.119.240/configs/");
/* 105 */         this.scanner = new Scanner(url.openStream());
/* 106 */         while (this.scanner.hasNextLine()) {
/* 107 */           String xd = this.scanner.nextLine();
/* 108 */           if ((xd.contains("by")) && 
/* 109 */             (!xd.contains("<tr><th colspan="))) {
/* 110 */             String uh = xd.substring(78, 150);
/* 111 */             String[] arrayOfString = uh.split("\\\"");
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 117 */         e.printStackTrace();
/*     */       }
/* 119 */       System.out.println(this.configlot.getSelected());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static URL verify(String url)
/*     */   {
/* 127 */     if (!url.toLowerCase().startsWith("https://")) {
/* 128 */       return null;
/*     */     }
/* 130 */     URL verifyUrl = null;
/*     */     try
/*     */     {
/* 133 */       verifyUrl = new URL(url);
/*     */     } catch (Exception e) {
/* 135 */       e.printStackTrace();
/*     */     }
/* 137 */     return verifyUrl;
/*     */   }
/*     */   
/*     */   public static boolean saveFile(URL fileURL, String fileSavePath)
/*     */   {
/* 142 */     boolean isSucceed = true;
/*     */     
/* 144 */     CloseableHttpClient httpClient = HttpClients.createDefault();
/*     */     
/* 146 */     HttpGet httpGet = new HttpGet(fileURL.toString());
/* 147 */     httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
/* 148 */     httpGet.addHeader("Referer", "https://www.google.com");
/*     */     try
/*     */     {
/* 151 */       CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
/* 152 */       HttpEntity fileEntity = httpResponse.getEntity();
/*     */       
/* 154 */       if (fileEntity != null) {
/* 155 */         FileUtils.copyInputStreamToFile(fileEntity.getContent(), new File(fileSavePath));
/* 156 */         System.out.println("Success!");
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 160 */       isSucceed = false;
/* 161 */       System.out.println("Failed");
/*     */     }
/*     */     
/* 164 */     httpGet.releaseConnection();
/*     */     
/* 166 */     return isSucceed;
/*     */   }
/*     */   
/*     */   public final List<Config> getConfigs() {
/* 170 */     return this.config;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\config\GuiConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */