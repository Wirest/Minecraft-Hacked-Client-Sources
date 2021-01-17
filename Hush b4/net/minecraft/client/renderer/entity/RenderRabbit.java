// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityRabbit;

public class RenderRabbit extends RenderLiving<EntityRabbit>
{
    private static final ResourceLocation BROWN;
    private static final ResourceLocation WHITE;
    private static final ResourceLocation BLACK;
    private static final ResourceLocation GOLD;
    private static final ResourceLocation SALT;
    private static final ResourceLocation WHITE_SPLOTCHED;
    private static final ResourceLocation TOAST;
    private static final ResourceLocation CAERBANNOG;
    
    static {
        BROWN = new ResourceLocation("textures/entity/rabbit/brown.png");
        WHITE = new ResourceLocation("textures/entity/rabbit/white.png");
        BLACK = new ResourceLocation("textures/entity/rabbit/black.png");
        GOLD = new ResourceLocation("textures/entity/rabbit/gold.png");
        SALT = new ResourceLocation("textures/entity/rabbit/salt.png");
        WHITE_SPLOTCHED = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
        TOAST = new ResourceLocation("textures/entity/rabbit/toast.png");
        CAERBANNOG = new ResourceLocation("textures/entity/rabbit/caerbannog.png");
    }
    
    public RenderRabbit(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityRabbit entity) {
        final String s = EnumChatFormatting.getTextWithoutFormattingCodes(entity.getName());
        if (s != null && s.equals("Toast")) {
            return RenderRabbit.TOAST;
        }
        switch (entity.getRabbitType()) {
            default: {
                return RenderRabbit.BROWN;
            }
            case 1: {
                return RenderRabbit.WHITE;
            }
            case 2: {
                return RenderRabbit.BLACK;
            }
            case 3: {
                return RenderRabbit.WHITE_SPLOTCHED;
            }
            case 4: {
                return RenderRabbit.GOLD;
            }
            case 5: {
                return RenderRabbit.SALT;
            }
            case 99: {
                return RenderRabbit.CAERBANNOG;
            }
        }
    }
}
