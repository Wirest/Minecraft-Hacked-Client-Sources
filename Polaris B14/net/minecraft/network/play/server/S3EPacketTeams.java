/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Team.EnumVisible;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class S3EPacketTeams implements net.minecraft.network.Packet<INetHandlerPlayClient>
/*     */ {
/*  14 */   private String field_149320_a = "";
/*  15 */   private String field_149318_b = "";
/*  16 */   private String field_149319_c = "";
/*  17 */   private String field_149316_d = "";
/*     */   private String field_179816_e;
/*     */   private int field_179815_f;
/*     */   private Collection<String> field_149317_e;
/*     */   private int field_149314_f;
/*     */   private int field_149315_g;
/*     */   
/*     */   public S3EPacketTeams()
/*     */   {
/*  26 */     this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
/*  27 */     this.field_179815_f = -1;
/*  28 */     this.field_149317_e = Lists.newArrayList();
/*     */   }
/*     */   
/*     */   public S3EPacketTeams(ScorePlayerTeam p_i45225_1_, int p_i45225_2_)
/*     */   {
/*  33 */     this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
/*  34 */     this.field_179815_f = -1;
/*  35 */     this.field_149317_e = Lists.newArrayList();
/*  36 */     this.field_149320_a = p_i45225_1_.getRegisteredName();
/*  37 */     this.field_149314_f = p_i45225_2_;
/*     */     
/*  39 */     if ((p_i45225_2_ == 0) || (p_i45225_2_ == 2))
/*     */     {
/*  41 */       this.field_149318_b = p_i45225_1_.getTeamName();
/*  42 */       this.field_149319_c = p_i45225_1_.getColorPrefix();
/*  43 */       this.field_149316_d = p_i45225_1_.getColorSuffix();
/*  44 */       this.field_149315_g = p_i45225_1_.func_98299_i();
/*  45 */       this.field_179816_e = p_i45225_1_.getNameTagVisibility().field_178830_e;
/*  46 */       this.field_179815_f = p_i45225_1_.getChatFormat().getColorIndex();
/*     */     }
/*     */     
/*  49 */     if (p_i45225_2_ == 0)
/*     */     {
/*  51 */       this.field_149317_e.addAll(p_i45225_1_.getMembershipCollection());
/*     */     }
/*     */   }
/*     */   
/*     */   public S3EPacketTeams(ScorePlayerTeam p_i45226_1_, Collection<String> p_i45226_2_, int p_i45226_3_)
/*     */   {
/*  57 */     this.field_179816_e = Team.EnumVisible.ALWAYS.field_178830_e;
/*  58 */     this.field_179815_f = -1;
/*  59 */     this.field_149317_e = Lists.newArrayList();
/*     */     
/*  61 */     if ((p_i45226_3_ != 3) && (p_i45226_3_ != 4))
/*     */     {
/*  63 */       throw new IllegalArgumentException("Method must be join or leave for player constructor");
/*     */     }
/*  65 */     if ((p_i45226_2_ != null) && (!p_i45226_2_.isEmpty()))
/*     */     {
/*  67 */       this.field_149314_f = p_i45226_3_;
/*  68 */       this.field_149320_a = p_i45226_1_.getRegisteredName();
/*  69 */       this.field_149317_e.addAll(p_i45226_2_);
/*     */     }
/*     */     else
/*     */     {
/*  73 */       throw new IllegalArgumentException("Players cannot be null/empty");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  82 */     this.field_149320_a = buf.readStringFromBuffer(16);
/*  83 */     this.field_149314_f = buf.readByte();
/*     */     
/*  85 */     if ((this.field_149314_f == 0) || (this.field_149314_f == 2))
/*     */     {
/*  87 */       this.field_149318_b = buf.readStringFromBuffer(32);
/*  88 */       this.field_149319_c = buf.readStringFromBuffer(16);
/*  89 */       this.field_149316_d = buf.readStringFromBuffer(16);
/*  90 */       this.field_149315_g = buf.readByte();
/*  91 */       this.field_179816_e = buf.readStringFromBuffer(32);
/*  92 */       this.field_179815_f = buf.readByte();
/*     */     }
/*     */     
/*  95 */     if ((this.field_149314_f == 0) || (this.field_149314_f == 3) || (this.field_149314_f == 4))
/*     */     {
/*  97 */       int i = buf.readVarIntFromBuffer();
/*     */       
/*  99 */       for (int j = 0; j < i; j++)
/*     */       {
/* 101 */         this.field_149317_e.add(buf.readStringFromBuffer(40));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/* 111 */     buf.writeString(this.field_149320_a);
/* 112 */     buf.writeByte(this.field_149314_f);
/*     */     
/* 114 */     if ((this.field_149314_f == 0) || (this.field_149314_f == 2))
/*     */     {
/* 116 */       buf.writeString(this.field_149318_b);
/* 117 */       buf.writeString(this.field_149319_c);
/* 118 */       buf.writeString(this.field_149316_d);
/* 119 */       buf.writeByte(this.field_149315_g);
/* 120 */       buf.writeString(this.field_179816_e);
/* 121 */       buf.writeByte(this.field_179815_f);
/*     */     }
/*     */     
/* 124 */     if ((this.field_149314_f == 0) || (this.field_149314_f == 3) || (this.field_149314_f == 4))
/*     */     {
/* 126 */       buf.writeVarIntToBuffer(this.field_149317_e.size());
/*     */       
/* 128 */       for (String s : this.field_149317_e)
/*     */       {
/* 130 */         buf.writeString(s);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/* 140 */     handler.handleTeams(this);
/*     */   }
/*     */   
/*     */   public String func_149312_c()
/*     */   {
/* 145 */     return this.field_149320_a;
/*     */   }
/*     */   
/*     */   public String func_149306_d()
/*     */   {
/* 150 */     return this.field_149318_b;
/*     */   }
/*     */   
/*     */   public String func_149311_e()
/*     */   {
/* 155 */     return this.field_149319_c;
/*     */   }
/*     */   
/*     */   public String func_149309_f()
/*     */   {
/* 160 */     return this.field_149316_d;
/*     */   }
/*     */   
/*     */   public Collection<String> func_149310_g()
/*     */   {
/* 165 */     return this.field_149317_e;
/*     */   }
/*     */   
/*     */   public int func_149307_h()
/*     */   {
/* 170 */     return this.field_149314_f;
/*     */   }
/*     */   
/*     */   public int func_149308_i()
/*     */   {
/* 175 */     return this.field_149315_g;
/*     */   }
/*     */   
/*     */   public int func_179813_h()
/*     */   {
/* 180 */     return this.field_179815_f;
/*     */   }
/*     */   
/*     */   public String func_179814_i()
/*     */   {
/* 185 */     return this.field_179816_e;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S3EPacketTeams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */