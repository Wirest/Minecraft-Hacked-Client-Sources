package net.minecraft.client.model;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import optfine.ModelSprite;
import org.lwjgl.opengl.GL11;

public class ModelRenderer {
   private static final String __OBFID = "CL_00000874";
   public final String boxName;
   public float textureWidth;
   public float textureHeight;
   public float rotationPointX;
   public float rotationPointY;
   public float rotationPointZ;
   public float rotateAngleX;
   public float rotateAngleY;
   public float rotateAngleZ;
   public boolean mirror;
   public boolean showModel;
   public boolean isHidden;
   public List cubeList;
   public List childModels;
   public float offsetX;
   public float offsetY;
   public float offsetZ;
   public List spriteList;
   public boolean mirrorV;
   private int textureOffsetX;
   private int textureOffsetY;
   private boolean compiled;
   private int displayList;
   private final ModelBase baseModel;

   public ModelRenderer(ModelBase model, String boxNameIn) {
      this.spriteList = new ArrayList();
      this.mirrorV = false;
      this.textureWidth = 64.0F;
      this.textureHeight = 32.0F;
      this.showModel = true;
      this.cubeList = Lists.newArrayList();
      this.baseModel = model;
      model.boxList.add(this);
      this.boxName = boxNameIn;
      this.setTextureSize(model.textureWidth, model.textureHeight);
   }

   public ModelRenderer(ModelBase model) {
      this(model, (String)null);
   }

   public ModelRenderer(ModelBase model, int texOffX, int texOffY) {
      this(model);
      this.setTextureOffset(texOffX, texOffY);
   }

   public void addChild(ModelRenderer renderer) {
      if (this.childModels == null) {
         this.childModels = Lists.newArrayList();
      }

      this.childModels.add(renderer);
   }

   public ModelRenderer setTextureOffset(int x, int y) {
      this.textureOffsetX = x;
      this.textureOffsetY = y;
      return this;
   }

   public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth) {
      partName = this.boxName + "." + partName;
      TextureOffset textureoffset = this.baseModel.getTextureOffset(partName);
      this.setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
      this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F)).setBoxName(partName));
      return this;
   }

   public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F));
      return this;
   }

   public ModelRenderer addBox(float p_178769_1_, float p_178769_2_, float p_178769_3_, int p_178769_4_, int p_178769_5_, int p_178769_6_, boolean p_178769_7_) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_178769_1_, p_178769_2_, p_178769_3_, p_178769_4_, p_178769_5_, p_178769_6_, 0.0F, p_178769_7_));
      return this;
   }

   public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int width, int height, int depth, float scaleFactor) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, width, height, depth, scaleFactor));
   }

   public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
      this.rotationPointX = rotationPointXIn;
      this.rotationPointY = rotationPointYIn;
      this.rotationPointZ = rotationPointZIn;
   }

   public void render(float p_78785_1_) {
      if (!this.isHidden && this.showModel) {
         if (!this.compiled) {
            this.compileDisplayList(p_78785_1_);
         }

         GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
         int i;
         if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
               GlStateManager.callList(this.displayList);
               if (this.childModels != null) {
                  for(i = 0; i < this.childModels.size(); ++i) {
                     ((ModelRenderer)this.childModels.get(i)).render(p_78785_1_);
                  }
               }
            } else {
               GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
               GlStateManager.callList(this.displayList);
               if (this.childModels != null) {
                  for(i = 0; i < this.childModels.size(); ++i) {
                     ((ModelRenderer)this.childModels.get(i)).render(p_78785_1_);
                  }
               }

               GlStateManager.translate(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
            }
         } else {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
            if (this.rotateAngleZ != 0.0F) {
               GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F) {
               GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
               GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glCallList(this.displayList);
            if (this.childModels != null) {
               for(i = 0; i < this.childModels.size(); ++i) {
                  ((ModelRenderer)this.childModels.get(i)).render(p_78785_1_);
               }
            }

            GL11.glPopMatrix();
         }

         GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
      }

   }

   public void renderWithRotation(float p_78791_1_) {
      if (!this.isHidden && this.showModel) {
         if (!this.compiled) {
            this.compileDisplayList(p_78791_1_);
         }

         GL11.glPushMatrix();
         GL11.glTranslatef(this.rotationPointX * p_78791_1_, this.rotationPointY * p_78791_1_, this.rotationPointZ * p_78791_1_);
         if (this.rotateAngleY != 0.0F) {
            GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
         }

         if (this.rotateAngleX != 0.0F) {
            GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
         }

         if (this.rotateAngleZ != 0.0F) {
            GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
         }

         GL11.glCallList(this.displayList);
         GL11.glPopMatrix();
      }

   }

   public void postRender(float scale) {
      if (!this.isHidden && this.showModel) {
         if (!this.compiled) {
            this.compileDisplayList(scale);
         }

         if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
               GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
            }
         } else {
            GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
            if (this.rotateAngleZ != 0.0F) {
               GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F) {
               GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
               GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }
         }
      }

   }

   private void compileDisplayList(float scale) {
      this.displayList = GLAllocation.generateDisplayLists(1);
      GL11.glNewList(this.displayList, 4864);
      WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
      List list = this.cubeList;
      int i = 0;

      int i;
      for(i = list.size(); i < i; ++i) {
         Object value = list.get(i);
         ((ModelBox)value).render(worldrenderer, scale);
      }

      List list1 = this.spriteList;
      i = 0;

      for(int listSize = list1.size(); i < listSize; ++i) {
         Object o = list1.get(i);
         ModelSprite modelsprite = (ModelSprite)o;
         modelsprite.render(Tessellator.getInstance(), scale);
      }

      GL11.glEndList();
      this.compiled = true;
   }

   public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn) {
      this.textureWidth = (float)textureWidthIn;
      this.textureHeight = (float)textureHeightIn;
      return this;
   }

   public void addSprite(float p_addSprite_1_, float p_addSprite_2_, float p_addSprite_3_, int p_addSprite_4_, int p_addSprite_5_, int p_addSprite_6_, float p_addSprite_7_) {
      this.spriteList.add(new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, p_addSprite_1_, p_addSprite_2_, p_addSprite_3_, p_addSprite_4_, p_addSprite_5_, p_addSprite_6_, p_addSprite_7_));
   }
}
