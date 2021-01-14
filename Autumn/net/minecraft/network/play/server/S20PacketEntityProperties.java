package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S20PacketEntityProperties implements Packet {
   private int entityId;
   private final List field_149444_b = Lists.newArrayList();

   public S20PacketEntityProperties() {
   }

   public S20PacketEntityProperties(int entityIdIn, Collection p_i45236_2_) {
      this.entityId = entityIdIn;
      Iterator var3 = p_i45236_2_.iterator();

      while(var3.hasNext()) {
         IAttributeInstance iattributeinstance = (IAttributeInstance)var3.next();
         this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(), iattributeinstance.func_111122_c()));
      }

   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.entityId = buf.readVarIntFromBuffer();
      int i = buf.readInt();

      for(int j = 0; j < i; ++j) {
         String s = buf.readStringFromBuffer(64);
         double d0 = buf.readDouble();
         List list = Lists.newArrayList();
         int k = buf.readVarIntFromBuffer();

         for(int l = 0; l < k; ++l) {
            UUID uuid = buf.readUuid();
            list.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
         }

         this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(s, d0, list));
      }

   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarIntToBuffer(this.entityId);
      buf.writeInt(this.field_149444_b.size());
      Iterator var2 = this.field_149444_b.iterator();

      while(var2.hasNext()) {
         S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot = (S20PacketEntityProperties.Snapshot)var2.next();
         buf.writeString(s20packetentityproperties$snapshot.func_151409_a());
         buf.writeDouble(s20packetentityproperties$snapshot.func_151410_b());
         buf.writeVarIntToBuffer(s20packetentityproperties$snapshot.func_151408_c().size());
         Iterator var4 = s20packetentityproperties$snapshot.func_151408_c().iterator();

         while(var4.hasNext()) {
            AttributeModifier attributemodifier = (AttributeModifier)var4.next();
            buf.writeUuid(attributemodifier.getID());
            buf.writeDouble(attributemodifier.getAmount());
            buf.writeByte(attributemodifier.getOperation());
         }
      }

   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleEntityProperties(this);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public List func_149441_d() {
      return this.field_149444_b;
   }

   public class Snapshot {
      private final String field_151412_b;
      private final double field_151413_c;
      private final Collection field_151411_d;

      public Snapshot(String p_i45235_2_, double p_i45235_3_, Collection p_i45235_5_) {
         this.field_151412_b = p_i45235_2_;
         this.field_151413_c = p_i45235_3_;
         this.field_151411_d = p_i45235_5_;
      }

      public String func_151409_a() {
         return this.field_151412_b;
      }

      public double func_151410_b() {
         return this.field_151413_c;
      }

      public Collection func_151408_c() {
         return this.field_151411_d;
      }
   }
}
