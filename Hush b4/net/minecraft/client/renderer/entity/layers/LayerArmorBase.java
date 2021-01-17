// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import shadersmod.client.ShadersRender;
import shadersmod.client.Shaders;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import optifine.CustomItems;
import optifine.Config;
import optifine.Reflector;
import net.minecraft.item.ItemArmor;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelBase;

public abstract class LayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase>
{
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES;
    protected ModelBase field_177189_c;
    protected ModelBase field_177186_d;
    private final RendererLivingEntity renderer;
    private float alpha;
    private float colorR;
    private float colorG;
    private float colorB;
    private boolean field_177193_i;
    private static final Map ARMOR_TEXTURE_RES_MAP;
    private static final String __OBFID = "CL_00002428";
    
    static {
        ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
        ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();
    }
    
    public LayerArmorBase(final RendererLivingEntity rendererIn) {
        this.alpha = 1.0f;
        this.colorR = 1.0f;
        this.colorG = 1.0f;
        this.colorB = 1.0f;
        this.renderer = rendererIn;
        this.initArmor();
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 4);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 3);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 2);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 1);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    private void renderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177182_2_, final float p_177182_3_, final float p_177182_4_, final float p_177182_5_, final float p_177182_6_, final float p_177182_7_, final float p_177182_8_, final int armorSlot) {
        final ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);
        if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
            final ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
            ModelBase modelbase = this.func_177175_a(armorSlot);
            modelbase.setModelAttributes(this.renderer.getMainModel());
            modelbase.setLivingAnimations(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_4_);
            if (Reflector.ForgeHooksClient.exists()) {
                modelbase = this.getArmorModelHook(entitylivingbaseIn, itemstack, armorSlot, modelbase);
            }
            this.func_177179_a(modelbase, armorSlot);
            final boolean flag = this.isSlotForLeggings(armorSlot);
            if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, flag ? 2 : 1, null)) {
                if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                    this.renderer.bindTexture(this.getArmorResource(entitylivingbaseIn, itemstack, flag ? 2 : 1, null));
                }
                else {
                    this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
                }
            }
            if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                final int j = itemarmor.getColor(itemstack);
                if (j != -1) {
                    final float f3 = (j >> 16 & 0xFF) / 255.0f;
                    final float f4 = (j >> 8 & 0xFF) / 255.0f;
                    final float f5 = (j & 0xFF) / 255.0f;
                    GlStateManager.color(this.colorR * f3, this.colorG * f4, this.colorB * f5, this.alpha);
                    modelbase.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, flag ? 2 : 1, "overlay")) {
                        this.renderer.bindTexture(this.getArmorResource(entitylivingbaseIn, itemstack, flag ? 2 : 1, "overlay"));
                    }
                }
                GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                modelbase.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                if (!this.field_177193_i && itemstack.isItemEnchanted() && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(entitylivingbaseIn, itemstack, modelbase, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_))) {
                    this.func_177183_a(entitylivingbaseIn, modelbase, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                }
                return;
            }
            switch (LayerArmorBase$1.field_178747_a[itemarmor.getArmorMaterial().ordinal()]) {
                case 1: {
                    final int i = itemarmor.getColor(itemstack);
                    final float f6 = (i >> 16 & 0xFF) / 255.0f;
                    final float f7 = (i >> 8 & 0xFF) / 255.0f;
                    final float f8 = (i & 0xFF) / 255.0f;
                    GlStateManager.color(this.colorR * f6, this.colorG * f7, this.colorB * f8, this.alpha);
                    modelbase.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, flag ? 2 : 1, "overlay")) {
                        this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
                    }
                }
                case 2:
                case 3:
                case 4:
                case 5: {
                    GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                    modelbase.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    break;
                }
            }
            if (!this.field_177193_i && itemstack.isItemEnchanted() && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(entitylivingbaseIn, itemstack, modelbase, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_))) {
                this.func_177183_a(entitylivingbaseIn, modelbase, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
            }
        }
    }
    
    public ItemStack getCurrentArmor(final EntityLivingBase entitylivingbaseIn, final int armorSlot) {
        return entitylivingbaseIn.getCurrentArmor(armorSlot - 1);
    }
    
    public ModelBase func_177175_a(final int p_177175_1_) {
        return this.isSlotForLeggings(p_177175_1_) ? this.field_177189_c : this.field_177186_d;
    }
    
    private boolean isSlotForLeggings(final int armorSlot) {
        return armorSlot == 2;
    }
    
    private void func_177183_a(final EntityLivingBase entitylivingbaseIn, final ModelBase modelbaseIn, final float p_177183_3_, final float p_177183_4_, final float p_177183_5_, final float p_177183_6_, final float p_177183_7_, final float p_177183_8_, final float p_177183_9_) {
        if ((!Config.isCustomItems() || CustomItems.isUseGlint()) && (!Config.isShaders() || !Shaders.isShadowPass)) {
            final float f = entitylivingbaseIn.ticksExisted + p_177183_5_;
            this.renderer.bindTexture(LayerArmorBase.ENCHANTED_ITEM_GLINT_RES);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            final float f2 = 0.5f;
            GlStateManager.color(f2, f2, f2, 1.0f);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(768, 1);
                final float f3 = 0.76f;
                GlStateManager.color(0.5f * f3, 0.25f * f3, 0.8f * f3, 1.0f);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                final float f4 = 0.33333334f;
                GlStateManager.scale(f4, f4, f4);
                GlStateManager.rotate(30.0f - i * 60.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.0f, f * (0.001f + i * 0.003f) * 20.0f, 0.0f);
                GlStateManager.matrixMode(5888);
                modelbaseIn.render(entitylivingbaseIn, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
            }
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }
    
    private ResourceLocation getArmorResource(final ItemArmor p_177181_1_, final boolean p_177181_2_) {
        return this.getArmorResource(p_177181_1_, p_177181_2_, null);
    }
    
    private ResourceLocation getArmorResource(final ItemArmor p_177178_1_, final boolean p_177178_2_, final String p_177178_3_) {
        final String s = String.format("textures/models/armor/%s_layer_%d%s.png", p_177178_1_.getArmorMaterial().getName(), p_177178_2_ ? 2 : 1, (p_177178_3_ == null) ? "" : String.format("_%s", p_177178_3_));
        ResourceLocation resourcelocation = LayerArmorBase.ARMOR_TEXTURE_RES_MAP.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            LayerArmorBase.ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
        }
        return resourcelocation;
    }
    
    protected abstract void initArmor();
    
    protected abstract void func_177179_a(final T p0, final int p1);
    
    protected ModelBase getArmorModelHook(final EntityLivingBase p_getArmorModelHook_1_, final ItemStack p_getArmorModelHook_2_, final int p_getArmorModelHook_3_, final ModelBase p_getArmorModelHook_4_) {
        return p_getArmorModelHook_4_;
    }
    
    public ResourceLocation getArmorResource(final Entity p_getArmorResource_1_, final ItemStack p_getArmorResource_2_, final int p_getArmorResource_3_, final String p_getArmorResource_4_) {
        final ItemArmor itemarmor = (ItemArmor)p_getArmorResource_2_.getItem();
        String s = itemarmor.getArmorMaterial().getName();
        String s2 = "minecraft";
        final int i = s.indexOf(58);
        if (i != -1) {
            s2 = s.substring(0, i);
            s = s.substring(i + 1);
        }
        String s3 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", s2, s, (p_getArmorResource_3_ == 2) ? 2 : 1, (p_getArmorResource_4_ == null) ? "" : String.format("_%s", p_getArmorResource_4_));
        s3 = Reflector.callString(Reflector.ForgeHooksClient_getArmorTexture, p_getArmorResource_1_, p_getArmorResource_2_, s3, p_getArmorResource_3_, p_getArmorResource_4_);
        ResourceLocation resourcelocation = LayerArmorBase.ARMOR_TEXTURE_RES_MAP.get(s3);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s3);
            LayerArmorBase.ARMOR_TEXTURE_RES_MAP.put(s3, resourcelocation);
        }
        return resourcelocation;
    }
    
    static final class LayerArmorBase$1
    {
        static final int[] field_178747_a;
        private static final String __OBFID = "CL_00002427";
        
        static {
            field_178747_a = new int[ItemArmor.ArmorMaterial.values().length];
            try {
                LayerArmorBase$1.field_178747_a[ItemArmor.ArmorMaterial.LEATHER.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                LayerArmorBase$1.field_178747_a[ItemArmor.ArmorMaterial.CHAIN.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                LayerArmorBase$1.field_178747_a[ItemArmor.ArmorMaterial.IRON.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                LayerArmorBase$1.field_178747_a[ItemArmor.ArmorMaterial.GOLD.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                LayerArmorBase$1.field_178747_a[ItemArmor.ArmorMaterial.DIAMOND.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
        }
    }
}
