/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class ChatUtils
/*     */ {
/*     */   private final ChatComponentText message;
/*     */   
/*     */   private ChatUtils(ChatComponentText message)
/*     */   {
/*  14 */     this.message = message;
/*     */   }
/*     */   
/*     */   public static String addFormat(String message, String regex) {
/*  18 */     return message.replaceAll("(?i)" + regex + "([0-9a-fklmnor])", "§$1");
/*     */   }
/*     */   
/*     */   public void displayClientSided() {
/*  22 */     Minecraft.getMinecraft().thePlayer.addChatMessage(this.message);
/*     */   }
/*     */   
/*     */   private ChatComponentText getChatComponent() {
/*  26 */     return this.message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ChatMessageBuilder
/*     */   {
/*  38 */     private static final EnumChatFormatting defaultMessageColor = EnumChatFormatting.WHITE;
/*     */     private ChatComponentText theMessage;
/*     */     private boolean useDefaultMessageColor;
/*     */     
/*  42 */     public ChatMessageBuilder(boolean prependDefaultPrefix, boolean useDefaultMessageColor) { this.theMessage = new ChatComponentText("");
/*  43 */       this.useDefaultMessageColor = false;
/*  44 */       this.workingStyle = new ChatStyle();
/*  45 */       this.workerMessage = new ChatComponentText("");
/*  46 */       if (prependDefaultPrefix) {
/*  47 */         this.theMessage.appendSibling(new ChatMessageBuilder(false, false).appendText(String.valueOf(rip.jutting.polaris.Polaris.instance.prefix + " ")).setColor(EnumChatFormatting.RED).build().getChatComponent());
/*     */       }
/*  49 */       this.useDefaultMessageColor = useDefaultMessageColor; }
/*     */     
/*     */     private ChatStyle workingStyle;
/*     */     private ChatComponentText workerMessage;
/*  53 */     public ChatMessageBuilder() { this.theMessage = new ChatComponentText("");
/*  54 */       this.useDefaultMessageColor = false;
/*  55 */       this.workingStyle = new ChatStyle();
/*  56 */       this.workerMessage = new ChatComponentText("");
/*     */     }
/*     */     
/*     */     public ChatMessageBuilder appendText(String text) {
/*  60 */       appendSibling();
/*  61 */       this.workerMessage = new ChatComponentText(text);
/*  62 */       this.workingStyle = new ChatStyle();
/*  63 */       if (this.useDefaultMessageColor) {
/*  64 */         setColor(defaultMessageColor);
/*     */       }
/*  66 */       return this;
/*     */     }
/*     */     
/*     */     public ChatMessageBuilder setColor(EnumChatFormatting color) {
/*  70 */       this.workingStyle.setColor(color);
/*  71 */       return this;
/*     */     }
/*     */     
/*     */     public ChatMessageBuilder bold() {
/*  75 */       this.workingStyle.setBold(Boolean.valueOf(true));
/*  76 */       return this;
/*     */     }
/*     */     
/*     */     public ChatMessageBuilder italic() {
/*  80 */       this.workingStyle.setItalic(Boolean.valueOf(true));
/*  81 */       return this;
/*     */     }
/*     */     
/*     */     public ChatMessageBuilder strikethrough() {
/*  85 */       this.workingStyle.setStrikethrough(Boolean.valueOf(true));
/*  86 */       return this;
/*     */     }
/*     */     
/*     */     public ChatMessageBuilder underline() {
/*  90 */       this.workingStyle.setUnderlined(Boolean.valueOf(true));
/*  91 */       return this;
/*     */     }
/*     */     
/*     */     public ChatUtils build() {
/*  95 */       appendSibling();
/*  96 */       return new ChatUtils(this.theMessage, null);
/*     */     }
/*     */     
/*     */     private void appendSibling() {
/* 100 */       this.theMessage.appendSibling(this.workerMessage.setChatStyle(this.workingStyle));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\ChatUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */