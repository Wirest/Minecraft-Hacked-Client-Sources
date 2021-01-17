// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.WorldRenderer;
import optifine.ModelSprite;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class ModelRenderer
{
    public float textureWidth;
    public float textureHeight;
    private int textureOffsetX;
    private int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    private boolean compiled;
    private int displayList;
    public boolean mirror;
    public boolean showModel;
    public boolean isHidden;
    public List cubeList;
    public List childModels;
    public final String boxName;
    private ModelBase baseModel;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    private static final String __OBFID = "CL_00000874";
    public List spriteList;
    public boolean mirrorV;
    float savedScale;
    
    public ModelRenderer(final ModelBase model, final String boxNameIn) {
        this.spriteList = new ArrayList();
        this.mirrorV = false;
        this.textureWidth = 64.0f;
        this.textureHeight = 32.0f;
        this.showModel = true;
        this.cubeList = Lists.newArrayList();
        this.baseModel = model;
        model.boxList.add(this);
        this.boxName = boxNameIn;
        this.setTextureSize(model.textureWidth, model.textureHeight);
    }
    
    public ModelRenderer(final ModelBase model) {
        this(model, null);
    }
    
    public ModelRenderer(final ModelBase model, final int texOffX, final int texOffY) {
        this(model);
        this.setTextureOffset(texOffX, texOffY);
    }
    
    public void addChild(final ModelRenderer renderer) {
        if (this.childModels == null) {
            this.childModels = Lists.newArrayList();
        }
        this.childModels.add(renderer);
    }
    
    public ModelRenderer setTextureOffset(final int x, final int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }
    
    public ModelRenderer addBox(String partName, final float offX, final float offY, final float offZ, final int width, final int height, final int depth) {
        partName = String.valueOf(this.boxName) + "." + partName;
        final TextureOffset textureoffset = this.baseModel.getTextureOffset(partName);
        this.setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0f).setBoxName(partName));
        return this;
    }
    
    public ModelRenderer addBox(final float offX, final float offY, final float offZ, final int width, final int height, final int depth) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0f));
        return this;
    }
    
    public ModelRenderer addBox(final float p_178769_1_, final float p_178769_2_, final float p_178769_3_, final int p_178769_4_, final int p_178769_5_, final int p_178769_6_, final boolean p_178769_7_) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_178769_1_, p_178769_2_, p_178769_3_, p_178769_4_, p_178769_5_, p_178769_6_, 0.0f, p_178769_7_));
        return this;
    }
    
    public void addBox(final float p_78790_1_, final float p_78790_2_, final float p_78790_3_, final int width, final int height, final int depth, final float scaleFactor) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, width, height, depth, scaleFactor));
    }
    
    public void setRotationPoint(final float rotationPointXIn, final float rotationPointYIn, final float rotationPointZIn) {
        this.rotationPointX = rotationPointXIn;
        this.rotationPointY = rotationPointYIn;
        this.rotationPointZ = rotationPointZIn;
    }
    
    public void render(final float p_78785_1_) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(p_78785_1_);
            }
            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
            if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
                if (this.rotationPointX == 0.0f && this.rotationPointY == 0.0f && this.rotationPointZ == 0.0f) {
                    GlStateManager.callList(this.displayList);
                    if (this.childModels != null) {
                        for (int k = 0; k < this.childModels.size(); ++k) {
                            this.childModels.get(k).render(p_78785_1_);
                        }
                    }
                }
                else {
                    GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
                    GlStateManager.callList(this.displayList);
                    if (this.childModels != null) {
                        for (int j = 0; j < this.childModels.size(); ++j) {
                            this.childModels.get(j).render(p_78785_1_);
                        }
                    }
                    GlStateManager.translate(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
                }
            }
            else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
                if (this.rotateAngleZ != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                if (this.rotateAngleY != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (this.rotateAngleX != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
                GlStateManager.callList(this.displayList);
                if (this.childModels != null) {
                    for (int i = 0; i < this.childModels.size(); ++i) {
                        this.childModels.get(i).render(p_78785_1_);
                    }
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
        }
    }
    
    public void renderWithRotation(final float p_78791_1_) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(p_78791_1_);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * p_78791_1_, this.rotationPointY * p_78791_1_, this.rotationPointZ * p_78791_1_);
            if (this.rotateAngleY != 0.0f) {
                GlStateManager.rotate(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (this.rotateAngleX != 0.0f) {
                GlStateManager.rotate(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (this.rotateAngleZ != 0.0f) {
                GlStateManager.rotate(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GlStateManager.callList(this.displayList);
            GlStateManager.popMatrix();
        }
    }
    
    public void postRender(final float scale) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
            if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
                if (this.rotationPointX != 0.0f || this.rotationPointY != 0.0f || this.rotationPointZ != 0.0f) {
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                }
            }
            else {
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
                }
                if (this.rotateAngleY != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
                }
                if (this.rotateAngleX != 0.0f) {
                    GlStateManager.rotate(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
                }
            }
        }
    }
    
    private void compileDisplayList(final float scale) {
        if (this.displayList == 0) {
            this.savedScale = scale;
            this.displayList = GLAllocation.generateDisplayLists(1);
        }
        GL11.glNewList(this.displayList, 4864);
        final WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
        for (int i = 0; i < this.cubeList.size(); ++i) {
            this.cubeList.get(i).render(worldrenderer, scale);
        }
        for (int j = 0; j < this.spriteList.size(); ++j) {
            final ModelSprite modelsprite = this.spriteList.get(j);
            modelsprite.render(Tessellator.getInstance(), scale);
        }
        GL11.glEndList();
        this.compiled = true;
    }
    
    public ModelRenderer setTextureSize(final int textureWidthIn, final int textureHeightIn) {
        this.textureWidth = (float)textureWidthIn;
        this.textureHeight = (float)textureHeightIn;
        return this;
    }
    
    public void addSprite(final float p_addSprite_1_, final float p_addSprite_2_, final float p_addSprite_3_, final int p_addSprite_4_, final int p_addSprite_5_, final int p_addSprite_6_, final float p_addSprite_7_) {
        this.spriteList.add(new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, p_addSprite_1_, p_addSprite_2_, p_addSprite_3_, p_addSprite_4_, p_addSprite_5_, p_addSprite_6_, p_addSprite_7_));
    }
    
    public boolean getCompiled() {
        return this.compiled;
    }
    
    public int getDisplayList() {
        return this.displayList;
    }
    
    public void resetDisplayList() {
        if (this.compiled) {
            this.compiled = false;
            this.compileDisplayList(this.savedScale);
        }
    }
}
