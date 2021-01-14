package net.minecraft.client.renderer.entity;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomItems;
import optifine.Reflector;

public class RenderItem implements IResourceManagerReloadListener {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private boolean field_175058_l = true;

    /**
     * Defines the zLevel of rendering of item on GUI.
     */
    public float zLevel;
    private final ItemModelMesher itemModelMesher;
    private final TextureManager field_175057_n;
    public static float field_175055_b = 0.0F;
    public static float field_175056_c = 0.0F;
    public static float field_175053_d = 0.0F;
    public static float field_175054_e = 0.0F;
    public static float field_175051_f = 0.0F;
    public static float field_175052_g = 0.0F;
    public static float field_175061_h = 0.0F;
    public static float field_175062_i = 0.0F;
    public static float field_175060_j = 0.0F;
    private static final String __OBFID = "CL_00001003";
    private ModelResourceLocation modelLocation = null;

    public RenderItem(TextureManager p_i46165_1_, ModelManager p_i46165_2_) {
        this.field_175057_n = p_i46165_1_;
        Config.setModelManager(p_i46165_2_);

        if (Reflector.ItemModelMesherForge_Constructor.exists()) {
            this.itemModelMesher = (ItemModelMesher) Reflector.newInstance(Reflector.ItemModelMesherForge_Constructor, new Object[]{p_i46165_2_});
        } else {
            this.itemModelMesher = new ItemModelMesher(p_i46165_2_);
        }

        this.registerItems();
    }

    public void func_175039_a(boolean p_175039_1_) {
        this.field_175058_l = p_175039_1_;
    }

    public ItemModelMesher getItemModelMesher() {
        return this.itemModelMesher;
    }

    protected void registerItem(Item p_175048_1_, int p_175048_2_, String p_175048_3_) {
        this.itemModelMesher.register(p_175048_1_, p_175048_2_, new ModelResourceLocation(p_175048_3_, "inventory"));
    }

    protected void registerBlock(Block p_175029_1_, int p_175029_2_, String p_175029_3_) {
        this.registerItem(Item.getItemFromBlock(p_175029_1_), p_175029_2_, p_175029_3_);
    }

    private void registerBlock(Block p_175031_1_, String p_175031_2_) {
        this.registerBlock(p_175031_1_, 0, p_175031_2_);
    }

    private void registerItem(Item p_175047_1_, String p_175047_2_) {
        this.registerItem(p_175047_1_, 0, p_175047_2_);
    }

    private void func_175036_a(IBakedModel p_175036_1_, ItemStack p_175036_2_) {
        this.func_175045_a(p_175036_1_, -1, p_175036_2_);
    }

    public void func_175035_a(IBakedModel p_175035_1_, int p_175035_2_) {
        this.func_175045_a(p_175035_1_, p_175035_2_, (ItemStack) null);
    }

    private void func_175045_a(IBakedModel p_175045_1_, int p_175045_2_, ItemStack p_175045_3_) {
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        boolean renderTextureMap = Minecraft.getMinecraft().getTextureMapBlocks().isTextureBound();
        boolean multiTexture = Config.isMultiTexture() && renderTextureMap;

        if (multiTexture) {
            var5.setBlockLayer(EnumWorldBlockLayer.SOLID);
        }

        var5.startDrawingQuads();
        var5.setVertexFormat(DefaultVertexFormats.field_176599_b);
        EnumFacing[] var6 = EnumFacing.VALUES;
        int var7 = var6.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            EnumFacing var9 = var6[var8];
            this.func_175032_a(var5, p_175045_1_.func_177551_a(var9), p_175045_2_, p_175045_3_);
        }

        this.func_175032_a(var5, p_175045_1_.func_177550_a(), p_175045_2_, p_175045_3_);
        var4.draw();

        if (multiTexture) {
            var5.setBlockLayer((EnumWorldBlockLayer) null);
            GlStateManager.bindCurrentTexture();
        }
    }

    public void func_180454_a(ItemStack p_180454_1_, IBakedModel p_180454_2_) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);

        if (p_180454_2_.isBuiltInRenderer()) {
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            TileEntityRendererChestHelper.instance.renderByItem(p_180454_1_);
        } else {
            if (Config.isCustomItems()) {
                p_180454_2_ = CustomItems.getCustomItemModel(p_180454_1_, p_180454_2_, this.modelLocation);
            }

            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            this.func_175036_a(p_180454_2_, p_180454_1_);

            if (p_180454_1_.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomEffect(this, p_180454_1_, p_180454_2_))) {
                this.renderEffect(p_180454_2_);
            }
        }

        GlStateManager.popMatrix();
    }

    private void renderEffect(IBakedModel p_180451_1_) {
        if (!Config.isCustomItems() || CustomItems.isUseGlint()) {
            GlStateManager.depthMask(false);
            GlStateManager.depthFunc(514);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            this.field_175057_n.bindTexture(RES_ITEM_GLINT);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(8.0F, 8.0F, 8.0F);
            float var2 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
            GlStateManager.translate(var2, 0.0F, 0.0F);
            GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
            this.func_175035_a(p_180451_1_, -8372020);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(8.0F, 8.0F, 8.0F);
            float var3 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
            GlStateManager.translate(-var3, 0.0F, 0.0F);
            GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
            this.func_175035_a(p_180451_1_, -8372020);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            this.field_175057_n.bindTexture(TextureMap.locationBlocksTexture);
        }
    }

    private void func_175038_a(WorldRenderer p_175038_1_, BakedQuad p_175038_2_) {
        Vec3i var3 = p_175038_2_.getFace().getDirectionVec();
        p_175038_1_.func_178975_e((float) var3.getX(), (float) var3.getY(), (float) var3.getZ());
    }

    private void func_175033_a(WorldRenderer p_175033_1_, BakedQuad p_175033_2_, int p_175033_3_) {
        if (p_175033_1_.isMultiTexture()) {
            p_175033_1_.func_178981_a(p_175033_2_.getVertexDataSingle());
            p_175033_1_.putSprite(p_175033_2_.getSprite());
        } else {
            p_175033_1_.func_178981_a(p_175033_2_.func_178209_a());
        }

        if (Reflector.IColoredBakedQuad.exists() && Reflector.IColoredBakedQuad.isInstance(p_175033_2_)) {
            forgeHooksClient_putQuadColor(p_175033_1_, p_175033_2_, p_175033_3_);
        } else {
            p_175033_1_.func_178968_d(p_175033_3_);
        }

        this.func_175038_a(p_175033_1_, p_175033_2_);
    }

    private void func_175032_a(WorldRenderer p_175032_1_, List p_175032_2_, int p_175032_3_, ItemStack p_175032_4_) {
        boolean var5 = p_175032_3_ == -1 && p_175032_4_ != null;
        BakedQuad var7;
        int var8;

        for (Iterator var6 = p_175032_2_.iterator(); var6.hasNext(); this.func_175033_a(p_175032_1_, var7, var8)) {
            var7 = (BakedQuad) var6.next();
            var8 = p_175032_3_;

            if (var5 && var7.func_178212_b()) {
                var8 = p_175032_4_.getItem().getColorFromItemStack(p_175032_4_, var7.func_178211_c());

                if (Config.isCustomColors()) {
                    var8 = CustomColors.getColorFromItemStack(p_175032_4_, var7.func_178211_c(), var8);
                }

                if (EntityRenderer.anaglyphEnable) {
                    var8 = TextureUtil.func_177054_c(var8);
                }

                var8 |= -16777216;
            }
        }
    }

    public boolean func_175050_a(ItemStack p_175050_1_) {
        IBakedModel var2 = this.itemModelMesher.getItemModel(p_175050_1_);
        return var2 == null ? false : var2.isAmbientOcclusionEnabled();
    }

    private void func_175046_c(ItemStack p_175046_1_) {
        IBakedModel var2 = this.itemModelMesher.getItemModel(p_175046_1_);
        Item var3 = p_175046_1_.getItem();

        if (var3 != null) {
            boolean var4 = var2.isAmbientOcclusionEnabled();

            if (!var4) {
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void func_175043_b(ItemStack p_175043_1_) {
        IBakedModel var2 = this.itemModelMesher.getItemModel(p_175043_1_);
        this.func_175040_a(p_175043_1_, var2, ItemCameraTransforms.TransformType.NONE);
    }

    public void func_175049_a(ItemStack p_175049_1_, EntityLivingBase p_175049_2_, ItemCameraTransforms.TransformType p_175049_3_) {
        IBakedModel var4 = this.itemModelMesher.getItemModel(p_175049_1_);

        if (p_175049_2_ instanceof EntityPlayer) {
            EntityPlayer var5 = (EntityPlayer) p_175049_2_;
            Item var6 = p_175049_1_.getItem();
            ModelResourceLocation var7 = null;

            if (var6 == Items.fishing_rod && var5.fishEntity != null) {
                var7 = new ModelResourceLocation("fishing_rod_cast", "inventory");
            } else if (var6 == Items.bow && var5.getItemInUse() != null) {
                int var8 = p_175049_1_.getMaxItemUseDuration() - var5.getItemInUseCount();

                if (var8 >= 18) {
                    var7 = new ModelResourceLocation("bow_pulling_2", "inventory");
                } else if (var8 > 13) {
                    var7 = new ModelResourceLocation("bow_pulling_1", "inventory");
                } else if (var8 > 0) {
                    var7 = new ModelResourceLocation("bow_pulling_0", "inventory");
                }
            } else if (Reflector.ForgeItem_getModel.exists()) {
                var7 = (ModelResourceLocation) Reflector.call(var6, Reflector.ForgeItem_getModel, new Object[]{p_175049_1_, var5, Integer.valueOf(var5.getItemInUseCount())});
            }

            this.modelLocation = var7;

            if (var7 != null) {
                var4 = this.itemModelMesher.getModelManager().getModel(var7);
            }
        }

        this.func_175040_a(p_175049_1_, var4, p_175049_3_);
        this.modelLocation = null;
    }

    protected void func_175034_a(ItemTransformVec3f p_175034_1_) {
        applyVanillaTransform(p_175034_1_);
    }

    public static void applyVanillaTransform(ItemTransformVec3f p_175034_1_) {
        if (p_175034_1_ != ItemTransformVec3f.field_178366_a) {
            GlStateManager.translate(p_175034_1_.field_178365_c.x + field_175055_b, p_175034_1_.field_178365_c.y + field_175056_c, p_175034_1_.field_178365_c.z + field_175053_d);
            GlStateManager.rotate(p_175034_1_.field_178364_b.y + field_175051_f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(p_175034_1_.field_178364_b.x + field_175054_e, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(p_175034_1_.field_178364_b.z + field_175052_g, 0.0F, 0.0F, 1.0F);
            GlStateManager.scale(p_175034_1_.field_178363_d.x + field_175061_h, p_175034_1_.field_178363_d.y + field_175062_i, p_175034_1_.field_178363_d.z + field_175060_j);
        }
    }

    protected void func_175040_a(ItemStack p_175040_1_, IBakedModel p_175040_2_, ItemCameraTransforms.TransformType p_175040_3_) {
        this.field_175057_n.bindTexture(TextureMap.locationBlocksTexture);
        this.field_175057_n.getTexture(TextureMap.locationBlocksTexture).func_174936_b(false, false);
        this.func_175046_c(p_175040_1_);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();

        if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
            p_175040_2_ = (IBakedModel) Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[]{p_175040_2_, p_175040_3_});
        } else {
            switch (RenderItem.SwitchTransformType.field_178640_a[p_175040_3_.ordinal()]) {
                case 1:
                default:
                    break;

                case 2:
                    this.func_175034_a(p_175040_2_.getItemCameraTransforms().field_178355_b);
                    break;

                case 3:
                    this.func_175034_a(p_175040_2_.getItemCameraTransforms().field_178356_c);
                    break;

                case 4:
                    this.func_175034_a(p_175040_2_.getItemCameraTransforms().field_178353_d);
                    break;

                case 5:
                    this.func_175034_a(p_175040_2_.getItemCameraTransforms().field_178354_e);
            }
        }

        this.func_180454_a(p_175040_1_, p_175040_2_);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.field_175057_n.bindTexture(TextureMap.locationBlocksTexture);
        this.field_175057_n.getTexture(TextureMap.locationBlocksTexture).func_174935_a();
    }

    public void func_175042_a(ItemStack p_175042_1_, int p_175042_2_, int p_175042_3_) {
        IBakedModel var4 = this.itemModelMesher.getItemModel(p_175042_1_);
        GlStateManager.pushMatrix();
        this.field_175057_n.bindTexture(TextureMap.locationBlocksTexture);
        this.field_175057_n.getTexture(TextureMap.locationBlocksTexture).func_174936_b(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.func_180452_a(p_175042_2_, p_175042_3_, var4.isAmbientOcclusionEnabled());

        if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
            var4 = (IBakedModel) Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[]{var4, ItemCameraTransforms.TransformType.GUI});
        } else {
            this.func_175034_a(var4.getItemCameraTransforms().field_178354_e);
        }

        this.func_180454_a(p_175042_1_, var4);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        this.field_175057_n.bindTexture(TextureMap.locationBlocksTexture);
        this.field_175057_n.getTexture(TextureMap.locationBlocksTexture).func_174935_a();
    }

    private void func_180452_a(int p_180452_1_, int p_180452_2_, boolean p_180452_3_) {
        GlStateManager.translate((float) p_180452_1_, (float) p_180452_2_, 100.0F + this.zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, -1.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);

        if (p_180452_3_) {
            GlStateManager.scale(40.0F, 40.0F, 40.0F);
            GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.enableLighting();
        } else {
            GlStateManager.scale(64.0F, 64.0F, 64.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.disableLighting();
        }
    }

    public void func_180450_b(final ItemStack p_180450_1_, int p_180450_2_, int p_180450_3_) {
        if (p_180450_1_ != null) {
            this.zLevel += 50.0F;

            try {
                this.func_175042_a(p_180450_1_, p_180450_2_, p_180450_3_);
            } catch (Throwable var7) {
                CrashReport var5 = CrashReport.makeCrashReport(var7, "Rendering item");
                CrashReportCategory var6 = var5.makeCategory("Item being rendered");
                var6.addCrashSectionCallable("Item Type", new Callable() {
                    private static final String __OBFID = "CL_00001004";

                    public String call() {
                        return String.valueOf(p_180450_1_.getItem());
                    }
                });
                var6.addCrashSectionCallable("Item Aux", new Callable() {
                    private static final String __OBFID = "CL_00001005";

                    public String call() {
                        return String.valueOf(p_180450_1_.getMetadata());
                    }
                });
                var6.addCrashSectionCallable("Item NBT", new Callable() {
                    private static final String __OBFID = "CL_00001006";

                    public String call() {
                        return String.valueOf(p_180450_1_.getTagCompound());
                    }
                });
                var6.addCrashSectionCallable("Item Foil", new Callable() {
                    public String call() {
                        return String.valueOf(p_180450_1_.hasEffect());
                    }
                });
                throw new ReportedException(var5);
            }

            this.zLevel -= 50.0F;
        }
    }

    public void func_175030_a(FontRenderer p_175030_1_, ItemStack p_175030_2_, int p_175030_3_, int p_175030_4_) {
        this.func_180453_a(p_175030_1_, p_175030_2_, p_175030_3_, p_175030_4_, (String) null);
    }

    public void func_180453_a(FontRenderer p_180453_1_, ItemStack p_180453_2_, int p_180453_3_, int p_180453_4_, String p_180453_5_) {
        if (p_180453_2_ != null) {
            if (p_180453_2_.stackSize != 1 || p_180453_5_ != null) {
                String itemDamaged = p_180453_5_ == null ? String.valueOf(p_180453_2_.stackSize) : p_180453_5_;

                if (p_180453_5_ == null && p_180453_2_.stackSize < 1) {
                    itemDamaged = EnumChatFormatting.RED + String.valueOf(p_180453_2_.stackSize);
                }

                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                p_180453_1_.func_175063_a(itemDamaged, (float) (p_180453_3_ + 19 - 2 - p_180453_1_.getStringWidth(itemDamaged)), (float) (p_180453_4_ + 6 + 3), 16777215);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }

            boolean itemDamaged1 = p_180453_2_.isItemDamaged();

            if (Reflector.ForgeItem_showDurabilityBar.exists()) {
                itemDamaged1 = Reflector.callBoolean(p_180453_2_.getItem(), Reflector.ForgeItem_showDurabilityBar, new Object[]{p_180453_2_});
            }

            if (itemDamaged1) {
                int var12 = (int) Math.round(13.0D - (double) p_180453_2_.getItemDamage() * 13.0D / (double) p_180453_2_.getMaxDamage());
                int var7 = (int) Math.round(255.0D - (double) p_180453_2_.getItemDamage() * 255.0D / (double) p_180453_2_.getMaxDamage());

                if (Reflector.ForgeItem_getDurabilityForDisplay.exists()) {
                    double var8 = Reflector.callDouble(p_180453_2_.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, new Object[]{p_180453_2_});
                    var12 = (int) Math.round(13.0D - var8 * 13.0D);
                    var7 = (int) Math.round(255.0D - var8 * 255.0D);
                }

                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.func_179090_x();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator var81 = Tessellator.getInstance();
                WorldRenderer var9 = var81.getWorldRenderer();
                int var10 = 255 - var7 << 16 | var7 << 8;
                int var11 = (255 - var7) / 4 << 16 | 16128;
                this.func_175044_a(var9, p_180453_3_ + 2, p_180453_4_ + 13, 13, 2, 0);
                this.func_175044_a(var9, p_180453_3_ + 2, p_180453_4_ + 13, 12, 1, var11);
                this.func_175044_a(var9, p_180453_3_ + 2, p_180453_4_ + 13, var12, 1, var10);

                if (!Reflector.ForgeHooksClient.exists()) {
                    GlStateManager.enableBlend();
                }

                GlStateManager.enableAlpha();
                GlStateManager.func_179098_w();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }

    private void func_175044_a(WorldRenderer p_175044_1_, int p_175044_2_, int p_175044_3_, int p_175044_4_, int p_175044_5_, int p_175044_6_) {
        p_175044_1_.startDrawingQuads();
        p_175044_1_.func_178991_c(p_175044_6_);
        p_175044_1_.addVertex((double) (p_175044_2_ + 0), (double) (p_175044_3_ + 0), 0.0D);
        p_175044_1_.addVertex((double) (p_175044_2_ + 0), (double) (p_175044_3_ + p_175044_5_), 0.0D);
        p_175044_1_.addVertex((double) (p_175044_2_ + p_175044_4_), (double) (p_175044_3_ + p_175044_5_), 0.0D);
        p_175044_1_.addVertex((double) (p_175044_2_ + p_175044_4_), (double) (p_175044_3_ + 0), 0.0D);
        Tessellator.getInstance().draw();
    }

    private void registerItems() {
        this.registerBlock(Blocks.anvil, "anvil_intact");
        this.registerBlock(Blocks.anvil, 1, "anvil_slightly_damaged");
        this.registerBlock(Blocks.anvil, 2, "anvil_very_damaged");
        this.registerBlock(Blocks.carpet, EnumDyeColor.BLACK.func_176765_a(), "black_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.BLUE.func_176765_a(), "blue_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.BROWN.func_176765_a(), "brown_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.CYAN.func_176765_a(), "cyan_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.GRAY.func_176765_a(), "gray_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.GREEN.func_176765_a(), "green_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.LIME.func_176765_a(), "lime_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.ORANGE.func_176765_a(), "orange_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.PINK.func_176765_a(), "pink_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.PURPLE.func_176765_a(), "purple_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.RED.func_176765_a(), "red_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.SILVER.func_176765_a(), "silver_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.WHITE.func_176765_a(), "white_carpet");
        this.registerBlock(Blocks.carpet, EnumDyeColor.YELLOW.func_176765_a(), "yellow_carpet");
        this.registerBlock(Blocks.cobblestone_wall, BlockWall.EnumType.MOSSY.func_176657_a(), "mossy_cobblestone_wall");
        this.registerBlock(Blocks.cobblestone_wall, BlockWall.EnumType.NORMAL.func_176657_a(), "cobblestone_wall");
        this.registerBlock(Blocks.dirt, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
        this.registerBlock(Blocks.dirt, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
        this.registerBlock(Blocks.dirt, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
        this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.FERN.func_176936_a(), "double_fern");
        this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.GRASS.func_176936_a(), "double_grass");
        this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.PAEONIA.func_176936_a(), "paeonia");
        this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.ROSE.func_176936_a(), "double_rose");
        this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.SUNFLOWER.func_176936_a(), "sunflower");
        this.registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.SYRINGA.func_176936_a(), "syringa");
        this.registerBlock(Blocks.leaves, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_leaves");
        this.registerBlock(Blocks.leaves, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_leaves");
        this.registerBlock(Blocks.leaves, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_leaves");
        this.registerBlock(Blocks.leaves, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_leaves");
        this.registerBlock(Blocks.leaves2, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4, "acacia_leaves");
        this.registerBlock(Blocks.leaves2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4, "dark_oak_leaves");
        this.registerBlock(Blocks.log, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_log");
        this.registerBlock(Blocks.log, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_log");
        this.registerBlock(Blocks.log, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_log");
        this.registerBlock(Blocks.log, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_log");
        this.registerBlock(Blocks.log2, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4, "acacia_log");
        this.registerBlock(Blocks.log2, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4, "dark_oak_log");
        this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.CHISELED_STONEBRICK.func_176881_a(), "chiseled_brick_monster_egg");
        this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.COBBLESTONE.func_176881_a(), "cobblestone_monster_egg");
        this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.CRACKED_STONEBRICK.func_176881_a(), "cracked_brick_monster_egg");
        this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.MOSSY_STONEBRICK.func_176881_a(), "mossy_brick_monster_egg");
        this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.STONE.func_176881_a(), "stone_monster_egg");
        this.registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.STONEBRICK.func_176881_a(), "stone_brick_monster_egg");
        this.registerBlock(Blocks.planks, BlockPlanks.EnumType.ACACIA.func_176839_a(), "acacia_planks");
        this.registerBlock(Blocks.planks, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_planks");
        this.registerBlock(Blocks.planks, BlockPlanks.EnumType.DARK_OAK.func_176839_a(), "dark_oak_planks");
        this.registerBlock(Blocks.planks, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_planks");
        this.registerBlock(Blocks.planks, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_planks");
        this.registerBlock(Blocks.planks, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_planks");
        this.registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
        this.registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
        this.registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
        this.registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.CHISELED.getMetaFromState(), "chiseled_quartz_block");
        this.registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.DEFAULT.getMetaFromState(), "quartz_block");
        this.registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.LINES_Y.getMetaFromState(), "quartz_column");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.ALLIUM.func_176968_b(), "allium");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.BLUE_ORCHID.func_176968_b(), "blue_orchid");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.HOUSTONIA.func_176968_b(), "houstonia");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.ORANGE_TULIP.func_176968_b(), "orange_tulip");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.OXEYE_DAISY.func_176968_b(), "oxeye_daisy");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.PINK_TULIP.func_176968_b(), "pink_tulip");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.POPPY.func_176968_b(), "poppy");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.RED_TULIP.func_176968_b(), "red_tulip");
        this.registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.WHITE_TULIP.func_176968_b(), "white_tulip");
        this.registerBlock(Blocks.sand, BlockSand.EnumType.RED_SAND.func_176688_a(), "red_sand");
        this.registerBlock(Blocks.sand, BlockSand.EnumType.SAND.func_176688_a(), "sand");
        this.registerBlock(Blocks.sandstone, BlockSandStone.EnumType.CHISELED.func_176675_a(), "chiseled_sandstone");
        this.registerBlock(Blocks.sandstone, BlockSandStone.EnumType.DEFAULT.func_176675_a(), "sandstone");
        this.registerBlock(Blocks.sandstone, BlockSandStone.EnumType.SMOOTH.func_176675_a(), "smooth_sandstone");
        this.registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.CHISELED.getMetaFromState(), "chiseled_red_sandstone");
        this.registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.DEFAULT.getMetaFromState(), "red_sandstone");
        this.registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.SMOOTH.getMetaFromState(), "smooth_red_sandstone");
        this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.ACACIA.func_176839_a(), "acacia_sapling");
        this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_sapling");
        this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.DARK_OAK.func_176839_a(), "dark_oak_sapling");
        this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_sapling");
        this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_sapling");
        this.registerBlock(Blocks.sapling, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_sapling");
        this.registerBlock(Blocks.sponge, 0, "sponge");
        this.registerBlock(Blocks.sponge, 1, "sponge_wet");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.BLACK.func_176765_a(), "black_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.BLUE.func_176765_a(), "blue_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.BROWN.func_176765_a(), "brown_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.CYAN.func_176765_a(), "cyan_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.GRAY.func_176765_a(), "gray_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.GREEN.func_176765_a(), "green_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.LIME.func_176765_a(), "lime_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.ORANGE.func_176765_a(), "orange_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.PINK.func_176765_a(), "pink_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.PURPLE.func_176765_a(), "purple_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.RED.func_176765_a(), "red_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.SILVER.func_176765_a(), "silver_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.WHITE.func_176765_a(), "white_stained_glass");
        this.registerBlock(Blocks.stained_glass, EnumDyeColor.YELLOW.func_176765_a(), "yellow_stained_glass");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BLACK.func_176765_a(), "black_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BLUE.func_176765_a(), "blue_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BROWN.func_176765_a(), "brown_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.CYAN.func_176765_a(), "cyan_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.GRAY.func_176765_a(), "gray_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.GREEN.func_176765_a(), "green_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.LIME.func_176765_a(), "lime_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.ORANGE.func_176765_a(), "orange_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.PINK.func_176765_a(), "pink_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.PURPLE.func_176765_a(), "purple_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.RED.func_176765_a(), "red_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.SILVER.func_176765_a(), "silver_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.WHITE.func_176765_a(), "white_stained_glass_pane");
        this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.YELLOW.func_176765_a(), "yellow_stained_glass_pane");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLACK.func_176765_a(), "black_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLUE.func_176765_a(), "blue_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BROWN.func_176765_a(), "brown_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.CYAN.func_176765_a(), "cyan_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GRAY.func_176765_a(), "gray_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GREEN.func_176765_a(), "green_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIME.func_176765_a(), "lime_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.ORANGE.func_176765_a(), "orange_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PINK.func_176765_a(), "pink_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PURPLE.func_176765_a(), "purple_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.RED.func_176765_a(), "red_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.SILVER.func_176765_a(), "silver_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.WHITE.func_176765_a(), "white_stained_hardened_clay");
        this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.YELLOW.func_176765_a(), "yellow_stained_hardened_clay");
        this.registerBlock(Blocks.stone, BlockStone.EnumType.ANDESITE.getMetaFromState(), "andesite");
        this.registerBlock(Blocks.stone, BlockStone.EnumType.ANDESITE_SMOOTH.getMetaFromState(), "andesite_smooth");
        this.registerBlock(Blocks.stone, BlockStone.EnumType.DIORITE.getMetaFromState(), "diorite");
        this.registerBlock(Blocks.stone, BlockStone.EnumType.DIORITE_SMOOTH.getMetaFromState(), "diorite_smooth");
        this.registerBlock(Blocks.stone, BlockStone.EnumType.GRANITE.getMetaFromState(), "granite");
        this.registerBlock(Blocks.stone, BlockStone.EnumType.GRANITE_SMOOTH.getMetaFromState(), "granite_smooth");
        this.registerBlock(Blocks.stone, BlockStone.EnumType.STONE.getMetaFromState(), "stone");
        this.registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.CRACKED.getMetaFromState(), "cracked_stonebrick");
        this.registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.DEFAULT.getMetaFromState(), "stonebrick");
        this.registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.CHISELED.getMetaFromState(), "chiseled_stonebrick");
        this.registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.MOSSY.getMetaFromState(), "mossy_stonebrick");
        this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.BRICK.func_176624_a(), "brick_slab");
        this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.COBBLESTONE.func_176624_a(), "cobblestone_slab");
        this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.WOOD.func_176624_a(), "old_wood_slab");
        this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.NETHERBRICK.func_176624_a(), "nether_brick_slab");
        this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.QUARTZ.func_176624_a(), "quartz_slab");
        this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.SAND.func_176624_a(), "sandstone_slab");
        this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a(), "stone_brick_slab");
        this.registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.STONE.func_176624_a(), "stone_slab");
        this.registerBlock(Blocks.stone_slab2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.func_176915_a(), "red_sandstone_slab");
        this.registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.DEAD_BUSH.func_177044_a(), "dead_bush");
        this.registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.FERN.func_177044_a(), "fern");
        this.registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.GRASS.func_177044_a(), "tall_grass");
        this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.ACACIA.func_176839_a(), "acacia_slab");
        this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_slab");
        this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.DARK_OAK.func_176839_a(), "dark_oak_slab");
        this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_slab");
        this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_slab");
        this.registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_slab");
        this.registerBlock(Blocks.wool, EnumDyeColor.BLACK.func_176765_a(), "black_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.BLUE.func_176765_a(), "blue_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.BROWN.func_176765_a(), "brown_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.CYAN.func_176765_a(), "cyan_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.GRAY.func_176765_a(), "gray_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.GREEN.func_176765_a(), "green_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.LIME.func_176765_a(), "lime_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.ORANGE.func_176765_a(), "orange_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.PINK.func_176765_a(), "pink_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.PURPLE.func_176765_a(), "purple_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.RED.func_176765_a(), "red_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.SILVER.func_176765_a(), "silver_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.WHITE.func_176765_a(), "white_wool");
        this.registerBlock(Blocks.wool, EnumDyeColor.YELLOW.func_176765_a(), "yellow_wool");
        this.registerBlock(Blocks.acacia_stairs, "acacia_stairs");
        this.registerBlock(Blocks.activator_rail, "activator_rail");
        this.registerBlock(Blocks.beacon, "beacon");
        this.registerBlock(Blocks.bedrock, "bedrock");
        this.registerBlock(Blocks.birch_stairs, "birch_stairs");
        this.registerBlock(Blocks.bookshelf, "bookshelf");
        this.registerBlock(Blocks.brick_block, "brick_block");
        this.registerBlock(Blocks.brick_block, "brick_block");
        this.registerBlock(Blocks.brick_stairs, "brick_stairs");
        this.registerBlock(Blocks.brown_mushroom, "brown_mushroom");
        this.registerBlock(Blocks.cactus, "cactus");
        this.registerBlock(Blocks.clay, "clay");
        this.registerBlock(Blocks.coal_block, "coal_block");
        this.registerBlock(Blocks.coal_ore, "coal_ore");
        this.registerBlock(Blocks.cobblestone, "cobblestone");
        this.registerBlock(Blocks.crafting_table, "crafting_table");
        this.registerBlock(Blocks.dark_oak_stairs, "dark_oak_stairs");
        this.registerBlock(Blocks.daylight_detector, "daylight_detector");
        this.registerBlock(Blocks.deadbush, "dead_bush");
        this.registerBlock(Blocks.detector_rail, "detector_rail");
        this.registerBlock(Blocks.diamond_block, "diamond_block");
        this.registerBlock(Blocks.diamond_ore, "diamond_ore");
        this.registerBlock(Blocks.dispenser, "dispenser");
        this.registerBlock(Blocks.dropper, "dropper");
        this.registerBlock(Blocks.emerald_block, "emerald_block");
        this.registerBlock(Blocks.emerald_ore, "emerald_ore");
        this.registerBlock(Blocks.enchanting_table, "enchanting_table");
        this.registerBlock(Blocks.end_portal_frame, "end_portal_frame");
        this.registerBlock(Blocks.end_stone, "end_stone");
        this.registerBlock(Blocks.oak_fence, "oak_fence");
        this.registerBlock(Blocks.spruce_fence, "spruce_fence");
        this.registerBlock(Blocks.birch_fence, "birch_fence");
        this.registerBlock(Blocks.jungle_fence, "jungle_fence");
        this.registerBlock(Blocks.dark_oak_fence, "dark_oak_fence");
        this.registerBlock(Blocks.acacia_fence, "acacia_fence");
        this.registerBlock(Blocks.oak_fence_gate, "oak_fence_gate");
        this.registerBlock(Blocks.spruce_fence_gate, "spruce_fence_gate");
        this.registerBlock(Blocks.birch_fence_gate, "birch_fence_gate");
        this.registerBlock(Blocks.jungle_fence_gate, "jungle_fence_gate");
        this.registerBlock(Blocks.dark_oak_fence_gate, "dark_oak_fence_gate");
        this.registerBlock(Blocks.acacia_fence_gate, "acacia_fence_gate");
        this.registerBlock(Blocks.furnace, "furnace");
        this.registerBlock(Blocks.glass, "glass");
        this.registerBlock(Blocks.glass_pane, "glass_pane");
        this.registerBlock(Blocks.glowstone, "glowstone");
        this.registerBlock(Blocks.golden_rail, "golden_rail");
        this.registerBlock(Blocks.gold_block, "gold_block");
        this.registerBlock(Blocks.gold_ore, "gold_ore");
        this.registerBlock(Blocks.grass, "grass");
        this.registerBlock(Blocks.gravel, "gravel");
        this.registerBlock(Blocks.hardened_clay, "hardened_clay");
        this.registerBlock(Blocks.hay_block, "hay_block");
        this.registerBlock(Blocks.heavy_weighted_pressure_plate, "heavy_weighted_pressure_plate");
        this.registerBlock(Blocks.hopper, "hopper");
        this.registerBlock(Blocks.ice, "ice");
        this.registerBlock(Blocks.iron_bars, "iron_bars");
        this.registerBlock(Blocks.iron_block, "iron_block");
        this.registerBlock(Blocks.iron_ore, "iron_ore");
        this.registerBlock(Blocks.iron_trapdoor, "iron_trapdoor");
        this.registerBlock(Blocks.jukebox, "jukebox");
        this.registerBlock(Blocks.jungle_stairs, "jungle_stairs");
        this.registerBlock(Blocks.ladder, "ladder");
        this.registerBlock(Blocks.lapis_block, "lapis_block");
        this.registerBlock(Blocks.lapis_ore, "lapis_ore");
        this.registerBlock(Blocks.lever, "lever");
        this.registerBlock(Blocks.light_weighted_pressure_plate, "light_weighted_pressure_plate");
        this.registerBlock(Blocks.lit_pumpkin, "lit_pumpkin");
        this.registerBlock(Blocks.melon_block, "melon_block");
        this.registerBlock(Blocks.mossy_cobblestone, "mossy_cobblestone");
        this.registerBlock(Blocks.mycelium, "mycelium");
        this.registerBlock(Blocks.netherrack, "netherrack");
        this.registerBlock(Blocks.nether_brick, "nether_brick");
        this.registerBlock(Blocks.nether_brick_fence, "nether_brick_fence");
        this.registerBlock(Blocks.nether_brick_stairs, "nether_brick_stairs");
        this.registerBlock(Blocks.noteblock, "noteblock");
        this.registerBlock(Blocks.oak_stairs, "oak_stairs");
        this.registerBlock(Blocks.obsidian, "obsidian");
        this.registerBlock(Blocks.packed_ice, "packed_ice");
        this.registerBlock(Blocks.piston, "piston");
        this.registerBlock(Blocks.pumpkin, "pumpkin");
        this.registerBlock(Blocks.quartz_ore, "quartz_ore");
        this.registerBlock(Blocks.quartz_stairs, "quartz_stairs");
        this.registerBlock(Blocks.rail, "rail");
        this.registerBlock(Blocks.redstone_block, "redstone_block");
        this.registerBlock(Blocks.redstone_lamp, "redstone_lamp");
        this.registerBlock(Blocks.redstone_ore, "redstone_ore");
        this.registerBlock(Blocks.redstone_torch, "redstone_torch");
        this.registerBlock(Blocks.red_mushroom, "red_mushroom");
        this.registerBlock(Blocks.sandstone_stairs, "sandstone_stairs");
        this.registerBlock(Blocks.red_sandstone_stairs, "red_sandstone_stairs");
        this.registerBlock(Blocks.sea_lantern, "sea_lantern");
        this.registerBlock(Blocks.slime_block, "slime");
        this.registerBlock(Blocks.snow, "snow");
        this.registerBlock(Blocks.snow_layer, "snow_layer");
        this.registerBlock(Blocks.soul_sand, "soul_sand");
        this.registerBlock(Blocks.spruce_stairs, "spruce_stairs");
        this.registerBlock(Blocks.sticky_piston, "sticky_piston");
        this.registerBlock(Blocks.stone_brick_stairs, "stone_brick_stairs");
        this.registerBlock(Blocks.stone_button, "stone_button");
        this.registerBlock(Blocks.stone_pressure_plate, "stone_pressure_plate");
        this.registerBlock(Blocks.stone_stairs, "stone_stairs");
        this.registerBlock(Blocks.tnt, "tnt");
        this.registerBlock(Blocks.torch, "torch");
        this.registerBlock(Blocks.trapdoor, "trapdoor");
        this.registerBlock(Blocks.tripwire_hook, "tripwire_hook");
        this.registerBlock(Blocks.vine, "vine");
        this.registerBlock(Blocks.waterlily, "waterlily");
        this.registerBlock(Blocks.web, "web");
        this.registerBlock(Blocks.wooden_button, "wooden_button");
        this.registerBlock(Blocks.wooden_pressure_plate, "wooden_pressure_plate");
        this.registerBlock(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION.func_176968_b(), "dandelion");
        this.registerBlock(Blocks.chest, "chest");
        this.registerBlock(Blocks.trapped_chest, "trapped_chest");
        this.registerBlock(Blocks.ender_chest, "ender_chest");
        this.registerItem(Items.iron_shovel, "iron_shovel");
        this.registerItem(Items.iron_pickaxe, "iron_pickaxe");
        this.registerItem(Items.iron_axe, "iron_axe");
        this.registerItem(Items.flint_and_steel, "flint_and_steel");
        this.registerItem(Items.apple, "apple");
        this.registerItem(Items.bow, 0, "bow");
        this.registerItem(Items.bow, 1, "bow_pulling_0");
        this.registerItem(Items.bow, 2, "bow_pulling_1");
        this.registerItem(Items.bow, 3, "bow_pulling_2");
        this.registerItem(Items.arrow, "arrow");
        this.registerItem(Items.coal, 0, "coal");
        this.registerItem(Items.coal, 1, "charcoal");
        this.registerItem(Items.diamond, "diamond");
        this.registerItem(Items.iron_ingot, "iron_ingot");
        this.registerItem(Items.gold_ingot, "gold_ingot");
        this.registerItem(Items.iron_sword, "iron_sword");
        this.registerItem(Items.wooden_sword, "wooden_sword");
        this.registerItem(Items.wooden_shovel, "wooden_shovel");
        this.registerItem(Items.wooden_pickaxe, "wooden_pickaxe");
        this.registerItem(Items.wooden_axe, "wooden_axe");
        this.registerItem(Items.stone_sword, "stone_sword");
        this.registerItem(Items.stone_shovel, "stone_shovel");
        this.registerItem(Items.stone_pickaxe, "stone_pickaxe");
        this.registerItem(Items.stone_axe, "stone_axe");
        this.registerItem(Items.diamond_sword, "diamond_sword");
        this.registerItem(Items.diamond_shovel, "diamond_shovel");
        this.registerItem(Items.diamond_pickaxe, "diamond_pickaxe");
        this.registerItem(Items.diamond_axe, "diamond_axe");
        this.registerItem(Items.stick, "stick");
        this.registerItem(Items.bowl, "bowl");
        this.registerItem(Items.mushroom_stew, "mushroom_stew");
        this.registerItem(Items.golden_sword, "golden_sword");
        this.registerItem(Items.golden_shovel, "golden_shovel");
        this.registerItem(Items.golden_pickaxe, "golden_pickaxe");
        this.registerItem(Items.golden_axe, "golden_axe");
        this.registerItem(Items.string, "string");
        this.registerItem(Items.feather, "feather");
        this.registerItem(Items.gunpowder, "gunpowder");
        this.registerItem(Items.wooden_hoe, "wooden_hoe");
        this.registerItem(Items.stone_hoe, "stone_hoe");
        this.registerItem(Items.iron_hoe, "iron_hoe");
        this.registerItem(Items.diamond_hoe, "diamond_hoe");
        this.registerItem(Items.golden_hoe, "golden_hoe");
        this.registerItem(Items.wheat_seeds, "wheat_seeds");
        this.registerItem(Items.wheat, "wheat");
        this.registerItem(Items.bread, "bread");
        this.registerItem(Items.leather_helmet, "leather_helmet");
        this.registerItem(Items.leather_chestplate, "leather_chestplate");
        this.registerItem(Items.leather_leggings, "leather_leggings");
        this.registerItem(Items.leather_boots, "leather_boots");
        this.registerItem(Items.chainmail_helmet, "chainmail_helmet");
        this.registerItem(Items.chainmail_chestplate, "chainmail_chestplate");
        this.registerItem(Items.chainmail_leggings, "chainmail_leggings");
        this.registerItem(Items.chainmail_boots, "chainmail_boots");
        this.registerItem(Items.iron_helmet, "iron_helmet");
        this.registerItem(Items.iron_chestplate, "iron_chestplate");
        this.registerItem(Items.iron_leggings, "iron_leggings");
        this.registerItem(Items.iron_boots, "iron_boots");
        this.registerItem(Items.diamond_helmet, "diamond_helmet");
        this.registerItem(Items.diamond_chestplate, "diamond_chestplate");
        this.registerItem(Items.diamond_leggings, "diamond_leggings");
        this.registerItem(Items.diamond_boots, "diamond_boots");
        this.registerItem(Items.golden_helmet, "golden_helmet");
        this.registerItem(Items.golden_chestplate, "golden_chestplate");
        this.registerItem(Items.golden_leggings, "golden_leggings");
        this.registerItem(Items.golden_boots, "golden_boots");
        this.registerItem(Items.flint, "flint");
        this.registerItem(Items.porkchop, "porkchop");
        this.registerItem(Items.cooked_porkchop, "cooked_porkchop");
        this.registerItem(Items.painting, "painting");
        this.registerItem(Items.golden_apple, "golden_apple");
        this.registerItem(Items.golden_apple, 1, "golden_apple");
        this.registerItem(Items.sign, "sign");
        this.registerItem(Items.oak_door, "oak_door");
        this.registerItem(Items.spruce_door, "spruce_door");
        this.registerItem(Items.birch_door, "birch_door");
        this.registerItem(Items.jungle_door, "jungle_door");
        this.registerItem(Items.acacia_door, "acacia_door");
        this.registerItem(Items.dark_oak_door, "dark_oak_door");
        this.registerItem(Items.bucket, "bucket");
        this.registerItem(Items.water_bucket, "water_bucket");
        this.registerItem(Items.lava_bucket, "lava_bucket");
        this.registerItem(Items.minecart, "minecart");
        this.registerItem(Items.saddle, "saddle");
        this.registerItem(Items.iron_door, "iron_door");
        this.registerItem(Items.redstone, "redstone");
        this.registerItem(Items.snowball, "snowball");
        this.registerItem(Items.boat, "boat");
        this.registerItem(Items.leather, "leather");
        this.registerItem(Items.milk_bucket, "milk_bucket");
        this.registerItem(Items.brick, "brick");
        this.registerItem(Items.clay_ball, "clay_ball");
        this.registerItem(Items.reeds, "reeds");
        this.registerItem(Items.paper, "paper");
        this.registerItem(Items.book, "book");
        this.registerItem(Items.slime_ball, "slime_ball");
        this.registerItem(Items.chest_minecart, "chest_minecart");
        this.registerItem(Items.furnace_minecart, "furnace_minecart");
        this.registerItem(Items.egg, "egg");
        this.registerItem(Items.compass, "compass");
        this.registerItem(Items.fishing_rod, "fishing_rod");
        this.registerItem(Items.fishing_rod, 1, "fishing_rod_cast");
        this.registerItem(Items.clock, "clock");
        this.registerItem(Items.glowstone_dust, "glowstone_dust");
        this.registerItem(Items.fish, ItemFishFood.FishType.COD.getItemDamage(), "cod");
        this.registerItem(Items.fish, ItemFishFood.FishType.SALMON.getItemDamage(), "salmon");
        this.registerItem(Items.fish, ItemFishFood.FishType.CLOWNFISH.getItemDamage(), "clownfish");
        this.registerItem(Items.fish, ItemFishFood.FishType.PUFFERFISH.getItemDamage(), "pufferfish");
        this.registerItem(Items.cooked_fish, ItemFishFood.FishType.COD.getItemDamage(), "cooked_cod");
        this.registerItem(Items.cooked_fish, ItemFishFood.FishType.SALMON.getItemDamage(), "cooked_salmon");
        this.registerItem(Items.dye, EnumDyeColor.BLACK.getDyeColorDamage(), "dye_black");
        this.registerItem(Items.dye, EnumDyeColor.RED.getDyeColorDamage(), "dye_red");
        this.registerItem(Items.dye, EnumDyeColor.GREEN.getDyeColorDamage(), "dye_green");
        this.registerItem(Items.dye, EnumDyeColor.BROWN.getDyeColorDamage(), "dye_brown");
        this.registerItem(Items.dye, EnumDyeColor.BLUE.getDyeColorDamage(), "dye_blue");
        this.registerItem(Items.dye, EnumDyeColor.PURPLE.getDyeColorDamage(), "dye_purple");
        this.registerItem(Items.dye, EnumDyeColor.CYAN.getDyeColorDamage(), "dye_cyan");
        this.registerItem(Items.dye, EnumDyeColor.SILVER.getDyeColorDamage(), "dye_silver");
        this.registerItem(Items.dye, EnumDyeColor.GRAY.getDyeColorDamage(), "dye_gray");
        this.registerItem(Items.dye, EnumDyeColor.PINK.getDyeColorDamage(), "dye_pink");
        this.registerItem(Items.dye, EnumDyeColor.LIME.getDyeColorDamage(), "dye_lime");
        this.registerItem(Items.dye, EnumDyeColor.YELLOW.getDyeColorDamage(), "dye_yellow");
        this.registerItem(Items.dye, EnumDyeColor.LIGHT_BLUE.getDyeColorDamage(), "dye_light_blue");
        this.registerItem(Items.dye, EnumDyeColor.MAGENTA.getDyeColorDamage(), "dye_magenta");
        this.registerItem(Items.dye, EnumDyeColor.ORANGE.getDyeColorDamage(), "dye_orange");
        this.registerItem(Items.dye, EnumDyeColor.WHITE.getDyeColorDamage(), "dye_white");
        this.registerItem(Items.bone, "bone");
        this.registerItem(Items.sugar, "sugar");
        this.registerItem(Items.cake, "cake");
        this.registerItem(Items.bed, "bed");
        this.registerItem(Items.repeater, "repeater");
        this.registerItem(Items.cookie, "cookie");
        this.registerItem(Items.shears, "shears");
        this.registerItem(Items.melon, "melon");
        this.registerItem(Items.pumpkin_seeds, "pumpkin_seeds");
        this.registerItem(Items.melon_seeds, "melon_seeds");
        this.registerItem(Items.beef, "beef");
        this.registerItem(Items.cooked_beef, "cooked_beef");
        this.registerItem(Items.chicken, "chicken");
        this.registerItem(Items.cooked_chicken, "cooked_chicken");
        this.registerItem(Items.rabbit, "rabbit");
        this.registerItem(Items.cooked_rabbit, "cooked_rabbit");
        this.registerItem(Items.mutton, "mutton");
        this.registerItem(Items.cooked_mutton, "cooked_mutton");
        this.registerItem(Items.rabbit_foot, "rabbit_foot");
        this.registerItem(Items.rabbit_hide, "rabbit_hide");
        this.registerItem(Items.rabbit_stew, "rabbit_stew");
        this.registerItem(Items.rotten_flesh, "rotten_flesh");
        this.registerItem(Items.ender_pearl, "ender_pearl");
        this.registerItem(Items.blaze_rod, "blaze_rod");
        this.registerItem(Items.ghast_tear, "ghast_tear");
        this.registerItem(Items.gold_nugget, "gold_nugget");
        this.registerItem(Items.nether_wart, "nether_wart");
        this.itemModelMesher.register(Items.potionitem, new ItemMeshDefinition() {
            private static final String __OBFID = "CL_00002440";

            public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
                return ItemPotion.isSplash(p_178113_1_.getMetadata()) ? new ModelResourceLocation("bottle_splash", "inventory") : new ModelResourceLocation("bottle_drinkable", "inventory");
            }
        });
        this.registerItem(Items.glass_bottle, "glass_bottle");
        this.registerItem(Items.spider_eye, "spider_eye");
        this.registerItem(Items.fermented_spider_eye, "fermented_spider_eye");
        this.registerItem(Items.blaze_powder, "blaze_powder");
        this.registerItem(Items.magma_cream, "magma_cream");
        this.registerItem(Items.brewing_stand, "brewing_stand");
        this.registerItem(Items.cauldron, "cauldron");
        this.registerItem(Items.ender_eye, "ender_eye");
        this.registerItem(Items.speckled_melon, "speckled_melon");
        this.itemModelMesher.register(Items.spawn_egg, new ItemMeshDefinition() {
            private static final String __OBFID = "CL_00002439";

            public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
                return new ModelResourceLocation("spawn_egg", "inventory");
            }
        });
        this.registerItem(Items.experience_bottle, "experience_bottle");
        this.registerItem(Items.fire_charge, "fire_charge");
        this.registerItem(Items.writable_book, "writable_book");
        this.registerItem(Items.emerald, "emerald");
        this.registerItem(Items.item_frame, "item_frame");
        this.registerItem(Items.flower_pot, "flower_pot");
        this.registerItem(Items.carrot, "carrot");
        this.registerItem(Items.potato, "potato");
        this.registerItem(Items.baked_potato, "baked_potato");
        this.registerItem(Items.poisonous_potato, "poisonous_potato");
        this.registerItem(Items.map, "map");
        this.registerItem(Items.golden_carrot, "golden_carrot");
        this.registerItem(Items.skull, 0, "skull_skeleton");
        this.registerItem(Items.skull, 1, "skull_wither");
        this.registerItem(Items.skull, 2, "skull_zombie");
        this.registerItem(Items.skull, 3, "skull_char");
        this.registerItem(Items.skull, 4, "skull_creeper");
        this.registerItem(Items.carrot_on_a_stick, "carrot_on_a_stick");
        this.registerItem(Items.nether_star, "nether_star");
        this.registerItem(Items.pumpkin_pie, "pumpkin_pie");
        this.registerItem(Items.firework_charge, "firework_charge");
        this.registerItem(Items.comparator, "comparator");
        this.registerItem(Items.netherbrick, "netherbrick");
        this.registerItem(Items.quartz, "quartz");
        this.registerItem(Items.tnt_minecart, "tnt_minecart");
        this.registerItem(Items.hopper_minecart, "hopper_minecart");
        this.registerItem(Items.armor_stand, "armor_stand");
        this.registerItem(Items.iron_horse_armor, "iron_horse_armor");
        this.registerItem(Items.golden_horse_armor, "golden_horse_armor");
        this.registerItem(Items.diamond_horse_armor, "diamond_horse_armor");
        this.registerItem(Items.lead, "lead");
        this.registerItem(Items.name_tag, "name_tag");
        this.itemModelMesher.register(Items.banner, new ItemMeshDefinition() {
            private static final String __OBFID = "CL_00002438";

            public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
                return new ModelResourceLocation("banner", "inventory");
            }
        });
        this.registerItem(Items.record_13, "record_13");
        this.registerItem(Items.record_cat, "record_cat");
        this.registerItem(Items.record_blocks, "record_blocks");
        this.registerItem(Items.record_chirp, "record_chirp");
        this.registerItem(Items.record_far, "record_far");
        this.registerItem(Items.record_mall, "record_mall");
        this.registerItem(Items.record_mellohi, "record_mellohi");
        this.registerItem(Items.record_stal, "record_stal");
        this.registerItem(Items.record_strad, "record_strad");
        this.registerItem(Items.record_ward, "record_ward");
        this.registerItem(Items.record_11, "record_11");
        this.registerItem(Items.record_wait, "record_wait");
        this.registerItem(Items.prismarine_shard, "prismarine_shard");
        this.registerItem(Items.prismarine_crystals, "prismarine_crystals");
        this.itemModelMesher.register(Items.enchanted_book, new ItemMeshDefinition() {
            private static final String __OBFID = "CL_00002437";

            public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
                return new ModelResourceLocation("enchanted_book", "inventory");
            }
        });
        this.itemModelMesher.register(Items.filled_map, new ItemMeshDefinition() {
            private static final String __OBFID = "CL_00002436";

            public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
                return new ModelResourceLocation("filled_map", "inventory");
            }
        });
        this.registerBlock(Blocks.command_block, "command_block");
        this.registerItem(Items.fireworks, "fireworks");
        this.registerItem(Items.command_block_minecart, "command_block_minecart");
        this.registerBlock(Blocks.barrier, "barrier");
        this.registerBlock(Blocks.mob_spawner, "mob_spawner");
        this.registerItem(Items.written_book, "written_book");
        this.registerBlock(Blocks.brown_mushroom_block, BlockHugeMushroom.EnumType.ALL_INSIDE.func_176896_a(), "brown_mushroom_block");
        this.registerBlock(Blocks.red_mushroom_block, BlockHugeMushroom.EnumType.ALL_INSIDE.func_176896_a(), "red_mushroom_block");
        this.registerBlock(Blocks.dragon_egg, "dragon_egg");

        if (Reflector.ModelLoader_onRegisterItems.exists()) {
            Reflector.call(Reflector.ModelLoader_onRegisterItems, new Object[]{this.itemModelMesher});
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.itemModelMesher.rebuildCache();
    }

    public static void forgeHooksClient_putQuadColor(WorldRenderer renderer, BakedQuad quad, int color) {
        float cr = (float) (color & 255);
        float cg = (float) (color >>> 8 & 255);
        float cb = (float) (color >>> 16 & 255);
        float ca = (float) (color >>> 24 & 255);
        int[] vd = quad.func_178209_a();
        int step = vd.length / 4;

        for (int i = 0; i < 4; ++i) {
            int vc = vd[3 + step * i];
            float vcr = (float) (vc & 255);
            float vcg = (float) (vc >>> 8 & 255);
            float vcb = (float) (vc >>> 16 & 255);
            float vca = (float) (vc >>> 24 & 255);
            int ncr = Math.min(255, (int) (cr * vcr / 255.0F));
            int ncg = Math.min(255, (int) (cg * vcg / 255.0F));
            int ncb = Math.min(255, (int) (cb * vcb / 255.0F));
            int nca = Math.min(255, (int) (ca * vca / 255.0F));
            renderer.func_178972_a(renderer.getGLCallListForPass(4 - i), ncr, ncg, ncb, nca);
        }
    }

    static final class SwitchTransformType {
        static final int[] field_178640_a = new int[ItemCameraTransforms.TransformType.values().length];
        private static final String __OBFID = "CL_00002441";

        static {
            try {
                field_178640_a[ItemCameraTransforms.TransformType.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                field_178640_a[ItemCameraTransforms.TransformType.THIRD_PERSON.ordinal()] = 2;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_178640_a[ItemCameraTransforms.TransformType.FIRST_PERSON.ordinal()] = 3;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_178640_a[ItemCameraTransforms.TransformType.HEAD.ordinal()] = 4;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_178640_a[ItemCameraTransforms.TransformType.GUI.ordinal()] = 5;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
