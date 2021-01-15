/*     */ package net.minecraft.client.renderer.entity.layers;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.ItemRenderer;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
/*     */ import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.tileentity.TileEntitySkull;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.StringUtils;
/*     */ 
/*     */ public class LayerCustomHead implements LayerRenderer<EntityLivingBase>
/*     */ {
/*     */   private final ModelRenderer field_177209_a;
/*     */   
/*     */   public LayerCustomHead(ModelRenderer p_i46120_1_)
/*     */   {
/*  29 */     this.field_177209_a = p_i46120_1_;
/*     */   }
/*     */   
/*     */   public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*     */   {
/*  34 */     ItemStack itemstack = entitylivingbaseIn.getCurrentArmor(3);
/*     */     
/*  36 */     if ((itemstack != null) && (itemstack.getItem() != null))
/*     */     {
/*  38 */       Item item = itemstack.getItem();
/*  39 */       Minecraft minecraft = Minecraft.getMinecraft();
/*  40 */       GlStateManager.pushMatrix();
/*     */       
/*  42 */       if (entitylivingbaseIn.isSneaking())
/*     */       {
/*  44 */         GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*     */       }
/*     */       
/*  47 */       boolean flag = ((entitylivingbaseIn instanceof EntityVillager)) || (((entitylivingbaseIn instanceof EntityZombie)) && (((EntityZombie)entitylivingbaseIn).isVillager()));
/*     */       
/*  49 */       if ((!flag) && (entitylivingbaseIn.isChild()))
/*     */       {
/*  51 */         float f = 2.0F;
/*  52 */         float f1 = 1.4F;
/*  53 */         GlStateManager.scale(f1 / f, f1 / f, f1 / f);
/*  54 */         GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
/*     */       }
/*     */       
/*  57 */       this.field_177209_a.postRender(0.0625F);
/*  58 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/*  60 */       if ((item instanceof ItemBlock))
/*     */       {
/*  62 */         float f2 = 0.625F;
/*  63 */         GlStateManager.translate(0.0F, -0.25F, 0.0F);
/*  64 */         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*  65 */         GlStateManager.scale(f2, -f2, -f2);
/*     */         
/*  67 */         if (flag)
/*     */         {
/*  69 */           GlStateManager.translate(0.0F, 0.1875F, 0.0F);
/*     */         }
/*     */         
/*  72 */         minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.HEAD);
/*     */       }
/*  74 */       else if (item == Items.skull)
/*     */       {
/*  76 */         float f3 = 1.1875F;
/*  77 */         GlStateManager.scale(f3, -f3, -f3);
/*     */         
/*  79 */         if (flag)
/*     */         {
/*  81 */           GlStateManager.translate(0.0F, 0.0625F, 0.0F);
/*     */         }
/*     */         
/*  84 */         GameProfile gameprofile = null;
/*     */         
/*  86 */         if (itemstack.hasTagCompound())
/*     */         {
/*  88 */           NBTTagCompound nbttagcompound = itemstack.getTagCompound();
/*     */           
/*  90 */           if (nbttagcompound.hasKey("SkullOwner", 10))
/*     */           {
/*  92 */             gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
/*     */           }
/*  94 */           else if (nbttagcompound.hasKey("SkullOwner", 8))
/*     */           {
/*  96 */             String s = nbttagcompound.getString("SkullOwner");
/*     */             
/*  98 */             if (!StringUtils.isNullOrEmpty(s))
/*     */             {
/* 100 */               gameprofile = TileEntitySkull.updateGameprofile(new GameProfile(null, s));
/* 101 */               nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 106 */         TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, itemstack.getMetadata(), gameprofile, -1);
/*     */       }
/*     */       
/* 109 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldCombineTextures()
/*     */   {
/* 115 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerCustomHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */