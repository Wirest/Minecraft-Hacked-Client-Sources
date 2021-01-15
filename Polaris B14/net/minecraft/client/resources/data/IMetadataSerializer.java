/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.util.ChatStyle;
/*    */ import net.minecraft.util.EnumTypeAdapterFactory;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.IChatComponent.Serializer;
/*    */ import net.minecraft.util.IRegistry;
/*    */ import net.minecraft.util.RegistrySimple;
/*    */ 
/*    */ public class IMetadataSerializer
/*    */ {
/* 15 */   private final IRegistry<String, Registration<? extends IMetadataSection>> metadataSectionSerializerRegistry = new RegistrySimple();
/* 16 */   private final GsonBuilder gsonBuilder = new GsonBuilder();
/*    */   
/*    */ 
/*    */   private Gson gson;
/*    */   
/*    */ 
/*    */ 
/*    */   public IMetadataSerializer()
/*    */   {
/* 25 */     this.gsonBuilder.registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer());
/* 26 */     this.gsonBuilder.registerTypeHierarchyAdapter(ChatStyle.class, new net.minecraft.util.ChatStyle.Serializer());
/* 27 */     this.gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
/*    */   }
/*    */   
/*    */   public <T extends IMetadataSection> void registerMetadataSectionType(IMetadataSectionSerializer<T> p_110504_1_, Class<T> p_110504_2_)
/*    */   {
/* 32 */     this.metadataSectionSerializerRegistry.putObject(p_110504_1_.getSectionName(), new Registration(p_110504_1_, p_110504_2_, null));
/* 33 */     this.gsonBuilder.registerTypeAdapter(p_110504_2_, p_110504_1_);
/* 34 */     this.gson = null;
/*    */   }
/*    */   
/*    */   public <T extends IMetadataSection> T parseMetadataSection(String p_110503_1_, JsonObject p_110503_2_)
/*    */   {
/* 39 */     if (p_110503_1_ == null)
/*    */     {
/* 41 */       throw new IllegalArgumentException("Metadata section name cannot be null");
/*    */     }
/* 43 */     if (!p_110503_2_.has(p_110503_1_))
/*    */     {
/* 45 */       return null;
/*    */     }
/* 47 */     if (!p_110503_2_.get(p_110503_1_).isJsonObject())
/*    */     {
/* 49 */       throw new IllegalArgumentException("Invalid metadata for '" + p_110503_1_ + "' - expected object, found " + p_110503_2_.get(p_110503_1_));
/*    */     }
/*    */     
/*    */ 
/* 53 */     Registration<?> registration = (Registration)this.metadataSectionSerializerRegistry.getObject(p_110503_1_);
/*    */     
/* 55 */     if (registration == null)
/*    */     {
/* 57 */       throw new IllegalArgumentException("Don't know how to handle metadata section '" + p_110503_1_ + "'");
/*    */     }
/*    */     
/*    */ 
/* 61 */     return (IMetadataSection)getGson().fromJson(p_110503_2_.getAsJsonObject(p_110503_1_), registration.field_110500_b);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private Gson getGson()
/*    */   {
/* 71 */     if (this.gson == null)
/*    */     {
/* 73 */       this.gson = this.gsonBuilder.create();
/*    */     }
/*    */     
/* 76 */     return this.gson;
/*    */   }
/*    */   
/*    */   class Registration<T extends IMetadataSection>
/*    */   {
/*    */     final IMetadataSectionSerializer<T> field_110502_a;
/*    */     final Class<T> field_110500_b;
/*    */     
/*    */     private Registration(Class<T> p_i1305_2_)
/*    */     {
/* 86 */       this.field_110502_a = p_i1305_2_;
/* 87 */       this.field_110500_b = p_i1305_3_;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\IMetadataSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */