/*    */ package net.minecraft.client.renderer.tileentity;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTUtil;
/*    */ import net.minecraft.tileentity.TileEntityBanner;
/*    */ import net.minecraft.tileentity.TileEntityChest;
/*    */ import net.minecraft.tileentity.TileEntityEnderChest;
/*    */ import net.minecraft.tileentity.TileEntitySkull;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ 
/*    */ public class TileEntityItemStackRenderer
/*    */ {
/* 20 */   public static TileEntityItemStackRenderer instance = new TileEntityItemStackRenderer();
/* 21 */   private TileEntityChest field_147717_b = new TileEntityChest(0);
/* 22 */   private TileEntityChest field_147718_c = new TileEntityChest(1);
/* 23 */   private TileEntityEnderChest enderChest = new TileEntityEnderChest();
/* 24 */   private TileEntityBanner banner = new TileEntityBanner();
/* 25 */   private TileEntitySkull skull = new TileEntitySkull();
/*    */   
/*    */   public void renderByItem(ItemStack itemStackIn)
/*    */   {
/* 29 */     if (itemStackIn.getItem() == Items.banner)
/*    */     {
/* 31 */       this.banner.setItemValues(itemStackIn);
/* 32 */       TileEntityRendererDispatcher.instance.renderTileEntityAt(this.banner, 0.0D, 0.0D, 0.0D, 0.0F);
/*    */     }
/* 34 */     else if (itemStackIn.getItem() == Items.skull)
/*    */     {
/* 36 */       GameProfile gameprofile = null;
/*    */       
/* 38 */       if (itemStackIn.hasTagCompound())
/*    */       {
/* 40 */         NBTTagCompound nbttagcompound = itemStackIn.getTagCompound();
/*    */         
/* 42 */         if (nbttagcompound.hasKey("SkullOwner", 10))
/*    */         {
/* 44 */           gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
/*    */         }
/* 46 */         else if ((nbttagcompound.hasKey("SkullOwner", 8)) && (nbttagcompound.getString("SkullOwner").length() > 0))
/*    */         {
/* 48 */           gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
/* 49 */           gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
/* 50 */           nbttagcompound.removeTag("SkullOwner");
/* 51 */           nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
/*    */         }
/*    */       }
/*    */       
/* 55 */       if (TileEntitySkullRenderer.instance != null)
/*    */       {
/* 57 */         GlStateManager.pushMatrix();
/* 58 */         GlStateManager.translate(-0.5F, 0.0F, -0.5F);
/* 59 */         GlStateManager.scale(2.0F, 2.0F, 2.0F);
/* 60 */         GlStateManager.disableCull();
/* 61 */         TileEntitySkullRenderer.instance.renderSkull(0.0F, 0.0F, 0.0F, EnumFacing.UP, 0.0F, itemStackIn.getMetadata(), gameprofile, -1);
/* 62 */         GlStateManager.enableCull();
/* 63 */         GlStateManager.popMatrix();
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 68 */       Block block = Block.getBlockFromItem(itemStackIn.getItem());
/*    */       
/* 70 */       if (block == Blocks.ender_chest)
/*    */       {
/* 72 */         TileEntityRendererDispatcher.instance.renderTileEntityAt(this.enderChest, 0.0D, 0.0D, 0.0D, 0.0F);
/*    */       }
/* 74 */       else if (block == Blocks.trapped_chest)
/*    */       {
/* 76 */         TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147718_c, 0.0D, 0.0D, 0.0D, 0.0F);
/*    */       }
/*    */       else
/*    */       {
/* 80 */         TileEntityRendererDispatcher.instance.renderTileEntityAt(this.field_147717_b, 0.0D, 0.0D, 0.0D, 0.0F);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntityItemStackRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */