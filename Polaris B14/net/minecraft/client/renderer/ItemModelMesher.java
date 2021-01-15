/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.IBakedModel;
/*    */ import net.minecraft.client.resources.model.ModelManager;
/*    */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class ItemModelMesher
/*    */ {
/* 15 */   private final Map<Integer, ModelResourceLocation> simpleShapes = Maps.newHashMap();
/* 16 */   private final Map<Integer, IBakedModel> simpleShapesCache = Maps.newHashMap();
/* 17 */   private final Map<Item, ItemMeshDefinition> shapers = Maps.newHashMap();
/*    */   private final ModelManager modelManager;
/*    */   
/*    */   public ItemModelMesher(ModelManager modelManager)
/*    */   {
/* 22 */     this.modelManager = modelManager;
/*    */   }
/*    */   
/*    */   public TextureAtlasSprite getParticleIcon(Item item)
/*    */   {
/* 27 */     return getParticleIcon(item, 0);
/*    */   }
/*    */   
/*    */   public TextureAtlasSprite getParticleIcon(Item item, int meta)
/*    */   {
/* 32 */     return getItemModel(new ItemStack(item, 1, meta)).getParticleTexture();
/*    */   }
/*    */   
/*    */   public IBakedModel getItemModel(ItemStack stack)
/*    */   {
/* 37 */     Item item = stack.getItem();
/* 38 */     IBakedModel ibakedmodel = getItemModel(item, getMetadata(stack));
/*    */     
/* 40 */     if (ibakedmodel == null)
/*    */     {
/* 42 */       ItemMeshDefinition itemmeshdefinition = (ItemMeshDefinition)this.shapers.get(item);
/*    */       
/* 44 */       if (itemmeshdefinition != null)
/*    */       {
/* 46 */         ibakedmodel = this.modelManager.getModel(itemmeshdefinition.getModelLocation(stack));
/*    */       }
/*    */     }
/*    */     
/* 50 */     if (ibakedmodel == null)
/*    */     {
/* 52 */       ibakedmodel = this.modelManager.getMissingModel();
/*    */     }
/*    */     
/* 55 */     return ibakedmodel;
/*    */   }
/*    */   
/*    */   protected int getMetadata(ItemStack stack)
/*    */   {
/* 60 */     return stack.isItemStackDamageable() ? 0 : stack.getMetadata();
/*    */   }
/*    */   
/*    */   protected IBakedModel getItemModel(Item item, int meta)
/*    */   {
/* 65 */     return (IBakedModel)this.simpleShapesCache.get(Integer.valueOf(getIndex(item, meta)));
/*    */   }
/*    */   
/*    */   private int getIndex(Item item, int meta)
/*    */   {
/* 70 */     return Item.getIdFromItem(item) << 16 | meta;
/*    */   }
/*    */   
/*    */   public void register(Item item, int meta, ModelResourceLocation location)
/*    */   {
/* 75 */     this.simpleShapes.put(Integer.valueOf(getIndex(item, meta)), location);
/* 76 */     this.simpleShapesCache.put(Integer.valueOf(getIndex(item, meta)), this.modelManager.getModel(location));
/*    */   }
/*    */   
/*    */   public void register(Item item, ItemMeshDefinition definition)
/*    */   {
/* 81 */     this.shapers.put(item, definition);
/*    */   }
/*    */   
/*    */   public ModelManager getModelManager()
/*    */   {
/* 86 */     return this.modelManager;
/*    */   }
/*    */   
/*    */   public void rebuildCache()
/*    */   {
/* 91 */     this.simpleShapesCache.clear();
/*    */     
/* 93 */     for (Map.Entry<Integer, ModelResourceLocation> entry : this.simpleShapes.entrySet())
/*    */     {
/* 95 */       this.simpleShapesCache.put((Integer)entry.getKey(), this.modelManager.getModel((ModelResourceLocation)entry.getValue()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\ItemModelMesher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */