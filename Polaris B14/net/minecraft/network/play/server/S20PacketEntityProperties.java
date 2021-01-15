/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ 
/*     */ public class S20PacketEntityProperties implements net.minecraft.network.Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityId;
/*  17 */   private final List<Snapshot> field_149444_b = Lists.newArrayList();
/*     */   
/*     */ 
/*     */   public S20PacketEntityProperties() {}
/*     */   
/*     */ 
/*     */   public S20PacketEntityProperties(int entityIdIn, Collection<IAttributeInstance> p_i45236_2_)
/*     */   {
/*  25 */     this.entityId = entityIdIn;
/*     */     
/*  27 */     for (IAttributeInstance iattributeinstance : p_i45236_2_)
/*     */     {
/*  29 */       this.field_149444_b.add(new Snapshot(iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(), iattributeinstance.func_111122_c()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  38 */     this.entityId = buf.readVarIntFromBuffer();
/*  39 */     int i = buf.readInt();
/*     */     
/*  41 */     for (int j = 0; j < i; j++)
/*     */     {
/*  43 */       String s = buf.readStringFromBuffer(64);
/*  44 */       double d0 = buf.readDouble();
/*  45 */       List<AttributeModifier> list = Lists.newArrayList();
/*  46 */       int k = buf.readVarIntFromBuffer();
/*     */       
/*  48 */       for (int l = 0; l < k; l++)
/*     */       {
/*  50 */         java.util.UUID uuid = buf.readUuid();
/*  51 */         list.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
/*     */       }
/*     */       
/*  54 */       this.field_149444_b.add(new Snapshot(s, d0, list));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  63 */     buf.writeVarIntToBuffer(this.entityId);
/*  64 */     buf.writeInt(this.field_149444_b.size());
/*     */     Iterator localIterator2;
/*  66 */     for (Iterator localIterator1 = this.field_149444_b.iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */         localIterator2.hasNext())
/*     */     {
/*  66 */       Snapshot s20packetentityproperties$snapshot = (Snapshot)localIterator1.next();
/*     */       
/*  68 */       buf.writeString(s20packetentityproperties$snapshot.func_151409_a());
/*  69 */       buf.writeDouble(s20packetentityproperties$snapshot.func_151410_b());
/*  70 */       buf.writeVarIntToBuffer(s20packetentityproperties$snapshot.func_151408_c().size());
/*     */       
/*  72 */       localIterator2 = s20packetentityproperties$snapshot.func_151408_c().iterator(); continue;AttributeModifier attributemodifier = (AttributeModifier)localIterator2.next();
/*     */       
/*  74 */       buf.writeUuid(attributemodifier.getID());
/*  75 */       buf.writeDouble(attributemodifier.getAmount());
/*  76 */       buf.writeByte(attributemodifier.getOperation());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  86 */     handler.handleEntityProperties(this);
/*     */   }
/*     */   
/*     */   public int getEntityId()
/*     */   {
/*  91 */     return this.entityId;
/*     */   }
/*     */   
/*     */   public List<Snapshot> func_149441_d()
/*     */   {
/*  96 */     return this.field_149444_b;
/*     */   }
/*     */   
/*     */   public class Snapshot
/*     */   {
/*     */     private final String field_151412_b;
/*     */     private final double field_151413_c;
/*     */     private final Collection<AttributeModifier> field_151411_d;
/*     */     
/*     */     public Snapshot(double p_i45235_2_, Collection<AttributeModifier> arg4)
/*     */     {
/* 107 */       this.field_151412_b = p_i45235_2_;
/* 108 */       this.field_151413_c = p_i45235_3_;
/* 109 */       this.field_151411_d = p_i45235_5_;
/*     */     }
/*     */     
/*     */     public String func_151409_a()
/*     */     {
/* 114 */       return this.field_151412_b;
/*     */     }
/*     */     
/*     */     public double func_151410_b()
/*     */     {
/* 119 */       return this.field_151413_c;
/*     */     }
/*     */     
/*     */     public Collection<AttributeModifier> func_151408_c()
/*     */     {
/* 124 */       return this.field_151411_d;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S20PacketEntityProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */