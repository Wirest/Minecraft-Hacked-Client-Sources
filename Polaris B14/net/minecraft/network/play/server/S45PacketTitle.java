/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ 
/*     */ public class S45PacketTitle
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private Type type;
/*     */   private IChatComponent message;
/*     */   private int fadeInTime;
/*     */   private int displayTime;
/*     */   private int fadeOutTime;
/*     */   
/*     */   public S45PacketTitle() {}
/*     */   
/*     */   public S45PacketTitle(Type type, IChatComponent message)
/*     */   {
/*  23 */     this(type, message, -1, -1, -1);
/*     */   }
/*     */   
/*     */   public S45PacketTitle(int fadeInTime, int displayTime, int fadeOutTime)
/*     */   {
/*  28 */     this(Type.TIMES, null, fadeInTime, displayTime, fadeOutTime);
/*     */   }
/*     */   
/*     */   public S45PacketTitle(Type type, IChatComponent message, int fadeInTime, int displayTime, int fadeOutTime)
/*     */   {
/*  33 */     this.type = type;
/*  34 */     this.message = message;
/*  35 */     this.fadeInTime = fadeInTime;
/*  36 */     this.displayTime = displayTime;
/*  37 */     this.fadeOutTime = fadeOutTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  45 */     this.type = ((Type)buf.readEnumValue(Type.class));
/*     */     
/*  47 */     if ((this.type == Type.TITLE) || (this.type == Type.SUBTITLE))
/*     */     {
/*  49 */       this.message = buf.readChatComponent();
/*     */     }
/*     */     
/*  52 */     if (this.type == Type.TIMES)
/*     */     {
/*  54 */       this.fadeInTime = buf.readInt();
/*  55 */       this.displayTime = buf.readInt();
/*  56 */       this.fadeOutTime = buf.readInt();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  65 */     buf.writeEnumValue(this.type);
/*     */     
/*  67 */     if ((this.type == Type.TITLE) || (this.type == Type.SUBTITLE))
/*     */     {
/*  69 */       buf.writeChatComponent(this.message);
/*     */     }
/*     */     
/*  72 */     if (this.type == Type.TIMES)
/*     */     {
/*  74 */       buf.writeInt(this.fadeInTime);
/*  75 */       buf.writeInt(this.displayTime);
/*  76 */       buf.writeInt(this.fadeOutTime);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  85 */     handler.handleTitle(this);
/*     */   }
/*     */   
/*     */   public Type getType()
/*     */   {
/*  90 */     return this.type;
/*     */   }
/*     */   
/*     */   public IChatComponent getMessage()
/*     */   {
/*  95 */     return this.message;
/*     */   }
/*     */   
/*     */   public int getFadeInTime()
/*     */   {
/* 100 */     return this.fadeInTime;
/*     */   }
/*     */   
/*     */   public int getDisplayTime()
/*     */   {
/* 105 */     return this.displayTime;
/*     */   }
/*     */   
/*     */   public int getFadeOutTime()
/*     */   {
/* 110 */     return this.fadeOutTime;
/*     */   }
/*     */   
/*     */   public static enum Type
/*     */   {
/* 115 */     TITLE, 
/* 116 */     SUBTITLE, 
/* 117 */     TIMES, 
/* 118 */     CLEAR, 
/* 119 */     RESET;
/*     */     
/*     */     public static Type byName(String name) {
/*     */       Type[] arrayOfType;
/* 123 */       int j = (arrayOfType = values()).length; for (int i = 0; i < j; i++) { Type s45packettitle$type = arrayOfType[i];
/*     */         
/* 125 */         if (s45packettitle$type.name().equalsIgnoreCase(name))
/*     */         {
/* 127 */           return s45packettitle$type;
/*     */         }
/*     */       }
/*     */       
/* 131 */       return TITLE;
/*     */     }
/*     */     
/*     */     public static String[] getNames()
/*     */     {
/* 136 */       String[] astring = new String[values().length];
/* 137 */       int i = 0;
/*     */       Type[] arrayOfType;
/* 139 */       int j = (arrayOfType = values()).length; for (int i = 0; i < j; i++) { Type s45packettitle$type = arrayOfType[i];
/*     */         
/* 141 */         astring[(i++)] = s45packettitle$type.name().toLowerCase();
/*     */       }
/*     */       
/* 144 */       return astring;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S45PacketTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */