package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.MathHelper;

public class ModelHorse extends ModelBase {
    private ModelRenderer head;
    private ModelRenderer field_178711_b;
    private ModelRenderer field_178712_c;
    private ModelRenderer horseLeftEar;
    private ModelRenderer horseRightEar;

    /**
     * The left ear box for the mule model.
     */
    private ModelRenderer muleLeftEar;

    /**
     * The right ear box for the mule model.
     */
    private ModelRenderer muleRightEar;
    private ModelRenderer neck;

    /**
     * The box for the horse's ropes on its face.
     */
    private ModelRenderer horseFaceRopes;
    private ModelRenderer mane;
    private ModelRenderer body;
    private ModelRenderer tailBase;
    private ModelRenderer tailMiddle;
    private ModelRenderer tailTip;
    private ModelRenderer backLeftLeg;
    private ModelRenderer backLeftShin;
    private ModelRenderer backLeftHoof;
    private ModelRenderer backRightLeg;
    private ModelRenderer backRightShin;
    private ModelRenderer backRightHoof;
    private ModelRenderer frontLeftLeg;
    private ModelRenderer frontLeftShin;
    private ModelRenderer frontLeftHoof;
    private ModelRenderer frontRightLeg;
    private ModelRenderer frontRightShin;
    private ModelRenderer frontRightHoof;

    /**
     * The left chest box on the mule model.
     */
    private ModelRenderer muleLeftChest;

    /**
     * The right chest box on the mule model.
     */
    private ModelRenderer muleRightChest;
    private ModelRenderer horseSaddleBottom;
    private ModelRenderer horseSaddleFront;
    private ModelRenderer horseSaddleBack;
    private ModelRenderer horseLeftSaddleRope;
    private ModelRenderer horseLeftSaddleMetal;
    private ModelRenderer horseRightSaddleRope;
    private ModelRenderer horseRightSaddleMetal;

    /**
     * The left metal connected to the horse's face ropes.
     */
    private ModelRenderer horseLeftFaceMetal;

    /**
     * The right metal connected to the horse's face ropes.
     */
    private ModelRenderer horseRightFaceMetal;
    private ModelRenderer horseLeftRein;
    private ModelRenderer horseRightRein;
    private static final String __OBFID = "CL_00000846";

    public ModelHorse() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.body = new ModelRenderer(this, 0, 34);
        this.body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
        this.body.setRotationPoint(0.0F, 11.0F, 9.0F);
        this.tailBase = new ModelRenderer(this, 44, 0);
        this.tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3);
        this.tailBase.setRotationPoint(0.0F, 3.0F, 14.0F);
        this.setBoxRotation(this.tailBase, -1.134464F, 0.0F, 0.0F);
        this.tailMiddle = new ModelRenderer(this, 38, 7);
        this.tailMiddle.addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7);
        this.tailMiddle.setRotationPoint(0.0F, 3.0F, 14.0F);
        this.setBoxRotation(this.tailMiddle, -1.134464F, 0.0F, 0.0F);
        this.tailTip = new ModelRenderer(this, 24, 3);
        this.tailTip.addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7);
        this.tailTip.setRotationPoint(0.0F, 3.0F, 14.0F);
        this.setBoxRotation(this.tailTip, -1.40215F, 0.0F, 0.0F);
        this.backLeftLeg = new ModelRenderer(this, 78, 29);
        this.backLeftLeg.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5);
        this.backLeftLeg.setRotationPoint(4.0F, 9.0F, 11.0F);
        this.backLeftShin = new ModelRenderer(this, 78, 43);
        this.backLeftShin.addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3);
        this.backLeftShin.setRotationPoint(4.0F, 16.0F, 11.0F);
        this.backLeftHoof = new ModelRenderer(this, 78, 51);
        this.backLeftHoof.addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4);
        this.backLeftHoof.setRotationPoint(4.0F, 16.0F, 11.0F);
        this.backRightLeg = new ModelRenderer(this, 96, 29);
        this.backRightLeg.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5);
        this.backRightLeg.setRotationPoint(-4.0F, 9.0F, 11.0F);
        this.backRightShin = new ModelRenderer(this, 96, 43);
        this.backRightShin.addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3);
        this.backRightShin.setRotationPoint(-4.0F, 16.0F, 11.0F);
        this.backRightHoof = new ModelRenderer(this, 96, 51);
        this.backRightHoof.addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4);
        this.backRightHoof.setRotationPoint(-4.0F, 16.0F, 11.0F);
        this.frontLeftLeg = new ModelRenderer(this, 44, 29);
        this.frontLeftLeg.addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4);
        this.frontLeftLeg.setRotationPoint(4.0F, 9.0F, -8.0F);
        this.frontLeftShin = new ModelRenderer(this, 44, 41);
        this.frontLeftShin.addBox(-1.9F, 0.0F, -1.6F, 3, 5, 3);
        this.frontLeftShin.setRotationPoint(4.0F, 16.0F, -8.0F);
        this.frontLeftHoof = new ModelRenderer(this, 44, 51);
        this.frontLeftHoof.addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4);
        this.frontLeftHoof.setRotationPoint(4.0F, 16.0F, -8.0F);
        this.frontRightLeg = new ModelRenderer(this, 60, 29);
        this.frontRightLeg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4);
        this.frontRightLeg.setRotationPoint(-4.0F, 9.0F, -8.0F);
        this.frontRightShin = new ModelRenderer(this, 60, 41);
        this.frontRightShin.addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3);
        this.frontRightShin.setRotationPoint(-4.0F, 16.0F, -8.0F);
        this.frontRightHoof = new ModelRenderer(this, 60, 51);
        this.frontRightHoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4);
        this.frontRightHoof.setRotationPoint(-4.0F, 16.0F, -8.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7);
        this.head.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.head, 0.5235988F, 0.0F, 0.0F);
        this.field_178711_b = new ModelRenderer(this, 24, 18);
        this.field_178711_b.addBox(-2.0F, -10.0F, -7.0F, 4, 3, 6);
        this.field_178711_b.setRotationPoint(0.0F, 3.95F, -10.0F);
        this.setBoxRotation(this.field_178711_b, 0.5235988F, 0.0F, 0.0F);
        this.field_178712_c = new ModelRenderer(this, 24, 27);
        this.field_178712_c.addBox(-2.0F, -7.0F, -6.5F, 4, 2, 5);
        this.field_178712_c.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.field_178712_c, 0.5235988F, 0.0F, 0.0F);
        this.head.addChild(this.field_178711_b);
        this.head.addChild(this.field_178712_c);
        this.horseLeftEar = new ModelRenderer(this, 0, 0);
        this.horseLeftEar.addBox(0.45F, -12.0F, 4.0F, 2, 3, 1);
        this.horseLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.horseLeftEar, 0.5235988F, 0.0F, 0.0F);
        this.horseRightEar = new ModelRenderer(this, 0, 0);
        this.horseRightEar.addBox(-2.45F, -12.0F, 4.0F, 2, 3, 1);
        this.horseRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.horseRightEar, 0.5235988F, 0.0F, 0.0F);
        this.muleLeftEar = new ModelRenderer(this, 0, 12);
        this.muleLeftEar.addBox(-2.0F, -16.0F, 4.0F, 2, 7, 1);
        this.muleLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.muleLeftEar, 0.5235988F, 0.0F, 0.2617994F);
        this.muleRightEar = new ModelRenderer(this, 0, 12);
        this.muleRightEar.addBox(0.0F, -16.0F, 4.0F, 2, 7, 1);
        this.muleRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.muleRightEar, 0.5235988F, 0.0F, -0.2617994F);
        this.neck = new ModelRenderer(this, 0, 12);
        this.neck.addBox(-2.05F, -9.8F, -2.0F, 4, 14, 8);
        this.neck.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.neck, 0.5235988F, 0.0F, 0.0F);
        this.muleLeftChest = new ModelRenderer(this, 0, 34);
        this.muleLeftChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
        this.muleLeftChest.setRotationPoint(-7.5F, 3.0F, 10.0F);
        this.setBoxRotation(this.muleLeftChest, 0.0F, ((float) Math.PI / 2F), 0.0F);
        this.muleRightChest = new ModelRenderer(this, 0, 47);
        this.muleRightChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
        this.muleRightChest.setRotationPoint(4.5F, 3.0F, 10.0F);
        this.setBoxRotation(this.muleRightChest, 0.0F, ((float) Math.PI / 2F), 0.0F);
        this.horseSaddleBottom = new ModelRenderer(this, 80, 0);
        this.horseSaddleBottom.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8);
        this.horseSaddleBottom.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.horseSaddleFront = new ModelRenderer(this, 106, 9);
        this.horseSaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2);
        this.horseSaddleFront.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.horseSaddleBack = new ModelRenderer(this, 80, 9);
        this.horseSaddleBack.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2);
        this.horseSaddleBack.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.horseLeftSaddleMetal = new ModelRenderer(this, 74, 0);
        this.horseLeftSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
        this.horseLeftSaddleMetal.setRotationPoint(5.0F, 3.0F, 2.0F);
        this.horseLeftSaddleRope = new ModelRenderer(this, 70, 0);
        this.horseLeftSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
        this.horseLeftSaddleRope.setRotationPoint(5.0F, 3.0F, 2.0F);
        this.horseRightSaddleMetal = new ModelRenderer(this, 74, 4);
        this.horseRightSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
        this.horseRightSaddleMetal.setRotationPoint(-5.0F, 3.0F, 2.0F);
        this.horseRightSaddleRope = new ModelRenderer(this, 80, 0);
        this.horseRightSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
        this.horseRightSaddleRope.setRotationPoint(-5.0F, 3.0F, 2.0F);
        this.horseLeftFaceMetal = new ModelRenderer(this, 74, 13);
        this.horseLeftFaceMetal.addBox(1.5F, -8.0F, -4.0F, 1, 2, 2);
        this.horseLeftFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.horseLeftFaceMetal, 0.5235988F, 0.0F, 0.0F);
        this.horseRightFaceMetal = new ModelRenderer(this, 74, 13);
        this.horseRightFaceMetal.addBox(-2.5F, -8.0F, -4.0F, 1, 2, 2);
        this.horseRightFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.horseRightFaceMetal, 0.5235988F, 0.0F, 0.0F);
        this.horseLeftRein = new ModelRenderer(this, 44, 10);
        this.horseLeftRein.addBox(2.6F, -6.0F, -6.0F, 0, 3, 16);
        this.horseLeftRein.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.horseRightRein = new ModelRenderer(this, 44, 5);
        this.horseRightRein.addBox(-2.6F, -6.0F, -6.0F, 0, 3, 16);
        this.horseRightRein.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.mane = new ModelRenderer(this, 58, 0);
        this.mane.addBox(-1.0F, -11.5F, 5.0F, 2, 16, 4);
        this.mane.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.mane, 0.5235988F, 0.0F, 0.0F);
        this.horseFaceRopes = new ModelRenderer(this, 80, 12);
        this.horseFaceRopes.addBox(-2.5F, -10.1F, -7.0F, 5, 5, 12, 0.2F);
        this.horseFaceRopes.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.setBoxRotation(this.horseFaceRopes, 0.5235988F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        EntityHorse var8 = (EntityHorse) p_78088_1_;
        int var9 = var8.getHorseType();
        float var10 = var8.getGrassEatingAmount(0.0F);
        boolean var11 = var8.isAdultHorse();
        boolean var12 = var11 && var8.isHorseSaddled();
        boolean var13 = var11 && var8.isChested();
        boolean var14 = var9 == 1 || var9 == 2;
        float var15 = var8.getHorseSize();
        boolean var16 = var8.riddenByEntity != null;

        if (var12) {
            this.horseFaceRopes.render(p_78088_7_);
            this.horseSaddleBottom.render(p_78088_7_);
            this.horseSaddleFront.render(p_78088_7_);
            this.horseSaddleBack.render(p_78088_7_);
            this.horseLeftSaddleRope.render(p_78088_7_);
            this.horseLeftSaddleMetal.render(p_78088_7_);
            this.horseRightSaddleRope.render(p_78088_7_);
            this.horseRightSaddleMetal.render(p_78088_7_);
            this.horseLeftFaceMetal.render(p_78088_7_);
            this.horseRightFaceMetal.render(p_78088_7_);

            if (var16) {
                this.horseLeftRein.render(p_78088_7_);
                this.horseRightRein.render(p_78088_7_);
            }
        }

        if (!var11) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(var15, 0.5F + var15 * 0.5F, var15);
            GlStateManager.translate(0.0F, 0.95F * (1.0F - var15), 0.0F);
        }

        this.backLeftLeg.render(p_78088_7_);
        this.backLeftShin.render(p_78088_7_);
        this.backLeftHoof.render(p_78088_7_);
        this.backRightLeg.render(p_78088_7_);
        this.backRightShin.render(p_78088_7_);
        this.backRightHoof.render(p_78088_7_);
        this.frontLeftLeg.render(p_78088_7_);
        this.frontLeftShin.render(p_78088_7_);
        this.frontLeftHoof.render(p_78088_7_);
        this.frontRightLeg.render(p_78088_7_);
        this.frontRightShin.render(p_78088_7_);
        this.frontRightHoof.render(p_78088_7_);

        if (!var11) {
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(var15, var15, var15);
            GlStateManager.translate(0.0F, 1.35F * (1.0F - var15), 0.0F);
        }

        this.body.render(p_78088_7_);
        this.tailBase.render(p_78088_7_);
        this.tailMiddle.render(p_78088_7_);
        this.tailTip.render(p_78088_7_);
        this.neck.render(p_78088_7_);
        this.mane.render(p_78088_7_);

        if (!var11) {
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            float var17 = 0.5F + var15 * var15 * 0.5F;
            GlStateManager.scale(var17, var17, var17);

            if (var10 <= 0.0F) {
                GlStateManager.translate(0.0F, 1.35F * (1.0F - var15), 0.0F);
            } else {
                GlStateManager.translate(0.0F, 0.9F * (1.0F - var15) * var10 + 1.35F * (1.0F - var15) * (1.0F - var10), 0.15F * (1.0F - var15) * var10);
            }
        }

        if (var14) {
            this.muleLeftEar.render(p_78088_7_);
            this.muleRightEar.render(p_78088_7_);
        } else {
            this.horseLeftEar.render(p_78088_7_);
            this.horseRightEar.render(p_78088_7_);
        }

        this.head.render(p_78088_7_);

        if (!var11) {
            GlStateManager.popMatrix();
        }

        if (var13) {
            this.muleLeftChest.render(p_78088_7_);
            this.muleRightChest.render(p_78088_7_);
        }
    }

    /**
     * Sets the rotations for a ModelRenderer in the ModelHorse class.
     */
    private void setBoxRotation(ModelRenderer p_110682_1_, float p_110682_2_, float p_110682_3_, float p_110682_4_) {
        p_110682_1_.rotateAngleX = p_110682_2_;
        p_110682_1_.rotateAngleY = p_110682_3_;
        p_110682_1_.rotateAngleZ = p_110682_4_;
    }

    /**
     * Fixes and offsets a rotation in the ModelHorse class.
     */
    private float updateHorseRotation(float p_110683_1_, float p_110683_2_, float p_110683_3_) {
        float var4;

        for (var4 = p_110683_2_ - p_110683_1_; var4 < -180.0F; var4 += 360.0F) {
            ;
        }

        while (var4 >= 180.0F) {
            var4 -= 360.0F;
        }

        return p_110683_1_ + p_110683_3_ * var4;
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
        super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
        float var5 = this.updateHorseRotation(p_78086_1_.prevRenderYawOffset, p_78086_1_.renderYawOffset, p_78086_4_);
        float var6 = this.updateHorseRotation(p_78086_1_.prevRotationYawHead, p_78086_1_.rotationYawHead, p_78086_4_);
        float var7 = p_78086_1_.prevRotationPitch + (p_78086_1_.rotationPitch - p_78086_1_.prevRotationPitch) * p_78086_4_;
        float var8 = var6 - var5;
        float var9 = var7 / (180F / (float) Math.PI);

        if (var8 > 20.0F) {
            var8 = 20.0F;
        }

        if (var8 < -20.0F) {
            var8 = -20.0F;
        }

        if (p_78086_3_ > 0.2F) {
            var9 += MathHelper.cos(p_78086_2_ * 0.4F) * 0.15F * p_78086_3_;
        }

        EntityHorse var10 = (EntityHorse) p_78086_1_;
        float var11 = var10.getGrassEatingAmount(p_78086_4_);
        float var12 = var10.getRearingAmount(p_78086_4_);
        float var13 = 1.0F - var12;
        float var14 = var10.func_110201_q(p_78086_4_);
        boolean var15 = var10.field_110278_bp != 0;
        boolean var16 = var10.isHorseSaddled();
        boolean var17 = var10.riddenByEntity != null;
        float var18 = (float) p_78086_1_.ticksExisted + p_78086_4_;
        float var19 = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI);
        float var20 = var19 * 0.8F * p_78086_3_;
        this.head.rotationPointY = 4.0F;
        this.head.rotationPointZ = -10.0F;
        this.tailBase.rotationPointY = 3.0F;
        this.tailMiddle.rotationPointZ = 14.0F;
        this.muleRightChest.rotationPointY = 3.0F;
        this.muleRightChest.rotationPointZ = 10.0F;
        this.body.rotateAngleX = 0.0F;
        this.head.rotateAngleX = 0.5235988F + var9;
        this.head.rotateAngleY = var8 / (180F / (float) Math.PI);
        this.head.rotateAngleX = var12 * (0.2617994F + var9) + var11 * 2.18166F + (1.0F - Math.max(var12, var11)) * this.head.rotateAngleX;
        this.head.rotateAngleY = var12 * var8 / (180F / (float) Math.PI) + (1.0F - Math.max(var12, var11)) * this.head.rotateAngleY;
        this.head.rotationPointY = var12 * -6.0F + var11 * 11.0F + (1.0F - Math.max(var12, var11)) * this.head.rotationPointY;
        this.head.rotationPointZ = var12 * -1.0F + var11 * -10.0F + (1.0F - Math.max(var12, var11)) * this.head.rotationPointZ;
        this.tailBase.rotationPointY = var12 * 9.0F + var13 * this.tailBase.rotationPointY;
        this.tailMiddle.rotationPointZ = var12 * 18.0F + var13 * this.tailMiddle.rotationPointZ;
        this.muleRightChest.rotationPointY = var12 * 5.5F + var13 * this.muleRightChest.rotationPointY;
        this.muleRightChest.rotationPointZ = var12 * 15.0F + var13 * this.muleRightChest.rotationPointZ;
        this.body.rotateAngleX = var12 * -45.0F / (180F / (float) Math.PI) + var13 * this.body.rotateAngleX;
        this.horseLeftEar.rotationPointY = this.head.rotationPointY;
        this.horseRightEar.rotationPointY = this.head.rotationPointY;
        this.muleLeftEar.rotationPointY = this.head.rotationPointY;
        this.muleRightEar.rotationPointY = this.head.rotationPointY;
        this.neck.rotationPointY = this.head.rotationPointY;
        this.field_178711_b.rotationPointY = 0.02F;
        this.field_178712_c.rotationPointY = 0.0F;
        this.mane.rotationPointY = this.head.rotationPointY;
        this.horseLeftEar.rotationPointZ = this.head.rotationPointZ;
        this.horseRightEar.rotationPointZ = this.head.rotationPointZ;
        this.muleLeftEar.rotationPointZ = this.head.rotationPointZ;
        this.muleRightEar.rotationPointZ = this.head.rotationPointZ;
        this.neck.rotationPointZ = this.head.rotationPointZ;
        this.field_178711_b.rotationPointZ = 0.02F - var14 * 1.0F;
        this.field_178712_c.rotationPointZ = 0.0F + var14 * 1.0F;
        this.mane.rotationPointZ = this.head.rotationPointZ;
        this.horseLeftEar.rotateAngleX = this.head.rotateAngleX;
        this.horseRightEar.rotateAngleX = this.head.rotateAngleX;
        this.muleLeftEar.rotateAngleX = this.head.rotateAngleX;
        this.muleRightEar.rotateAngleX = this.head.rotateAngleX;
        this.neck.rotateAngleX = this.head.rotateAngleX;
        this.field_178711_b.rotateAngleX = 0.0F - 0.09424778F * var14;
        this.field_178712_c.rotateAngleX = 0.0F + 0.15707964F * var14;
        this.mane.rotateAngleX = this.head.rotateAngleX;
        this.horseLeftEar.rotateAngleY = this.head.rotateAngleY;
        this.horseRightEar.rotateAngleY = this.head.rotateAngleY;
        this.muleLeftEar.rotateAngleY = this.head.rotateAngleY;
        this.muleRightEar.rotateAngleY = this.head.rotateAngleY;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.field_178711_b.rotateAngleY = 0.0F;
        this.field_178712_c.rotateAngleY = 0.0F;
        this.mane.rotateAngleY = this.head.rotateAngleY;
        this.muleLeftChest.rotateAngleX = var20 / 5.0F;
        this.muleRightChest.rotateAngleX = -var20 / 5.0F;
        float var21 = ((float) Math.PI / 2F);
        float var22 = ((float) Math.PI * 3F / 2F);
        float var23 = -1.0471976F;
        float var24 = 0.2617994F * var12;
        float var25 = MathHelper.cos(var18 * 0.6F + (float) Math.PI);
        this.frontLeftLeg.rotationPointY = -2.0F * var12 + 9.0F * var13;
        this.frontLeftLeg.rotationPointZ = -2.0F * var12 + -8.0F * var13;
        this.frontRightLeg.rotationPointY = this.frontLeftLeg.rotationPointY;
        this.frontRightLeg.rotationPointZ = this.frontLeftLeg.rotationPointZ;
        this.backLeftShin.rotationPointY = this.backLeftLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + var24 + var13 * -var19 * 0.5F * p_78086_3_) * 7.0F;
        this.backLeftShin.rotationPointZ = this.backLeftLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + var24 + var13 * -var19 * 0.5F * p_78086_3_) * 7.0F;
        this.backRightShin.rotationPointY = this.backRightLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + var24 + var13 * var19 * 0.5F * p_78086_3_) * 7.0F;
        this.backRightShin.rotationPointZ = this.backRightLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + var24 + var13 * var19 * 0.5F * p_78086_3_) * 7.0F;
        float var26 = (-1.0471976F + var25) * var12 + var20 * var13;
        float var27 = (-1.0471976F + -var25) * var12 + -var20 * var13;
        this.frontLeftShin.rotationPointY = this.frontLeftLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + var26) * 7.0F;
        this.frontLeftShin.rotationPointZ = this.frontLeftLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + var26) * 7.0F;
        this.frontRightShin.rotationPointY = this.frontRightLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + var27) * 7.0F;
        this.frontRightShin.rotationPointZ = this.frontRightLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + var27) * 7.0F;
        this.backLeftLeg.rotateAngleX = var24 + -var19 * 0.5F * p_78086_3_ * var13;
        this.backLeftShin.rotateAngleX = -0.08726646F * var12 + (-var19 * 0.5F * p_78086_3_ - Math.max(0.0F, var19 * 0.5F * p_78086_3_)) * var13;
        this.backLeftHoof.rotateAngleX = this.backLeftShin.rotateAngleX;
        this.backRightLeg.rotateAngleX = var24 + var19 * 0.5F * p_78086_3_ * var13;
        this.backRightShin.rotateAngleX = -0.08726646F * var12 + (var19 * 0.5F * p_78086_3_ - Math.max(0.0F, -var19 * 0.5F * p_78086_3_)) * var13;
        this.backRightHoof.rotateAngleX = this.backRightShin.rotateAngleX;
        this.frontLeftLeg.rotateAngleX = var26;
        this.frontLeftShin.rotateAngleX = (this.frontLeftLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F + var25 * 0.2F)) * var12 + (var20 + Math.max(0.0F, var19 * 0.5F * p_78086_3_)) * var13;
        this.frontLeftHoof.rotateAngleX = this.frontLeftShin.rotateAngleX;
        this.frontRightLeg.rotateAngleX = var27;
        this.frontRightShin.rotateAngleX = (this.frontRightLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F - var25 * 0.2F)) * var12 + (-var20 + Math.max(0.0F, -var19 * 0.5F * p_78086_3_)) * var13;
        this.frontRightHoof.rotateAngleX = this.frontRightShin.rotateAngleX;
        this.backLeftHoof.rotationPointY = this.backLeftShin.rotationPointY;
        this.backLeftHoof.rotationPointZ = this.backLeftShin.rotationPointZ;
        this.backRightHoof.rotationPointY = this.backRightShin.rotationPointY;
        this.backRightHoof.rotationPointZ = this.backRightShin.rotationPointZ;
        this.frontLeftHoof.rotationPointY = this.frontLeftShin.rotationPointY;
        this.frontLeftHoof.rotationPointZ = this.frontLeftShin.rotationPointZ;
        this.frontRightHoof.rotationPointY = this.frontRightShin.rotationPointY;
        this.frontRightHoof.rotationPointZ = this.frontRightShin.rotationPointZ;

        if (var16) {
            this.horseSaddleBottom.rotationPointY = var12 * 0.5F + var13 * 2.0F;
            this.horseSaddleBottom.rotationPointZ = var12 * 11.0F + var13 * 2.0F;
            this.horseSaddleFront.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseSaddleBack.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseLeftSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseRightSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseLeftSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseRightSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.muleLeftChest.rotationPointY = this.muleRightChest.rotationPointY;
            this.horseSaddleFront.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseSaddleBack.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseLeftSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseRightSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseLeftSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseRightSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.muleLeftChest.rotationPointZ = this.muleRightChest.rotationPointZ;
            this.horseSaddleBottom.rotateAngleX = this.body.rotateAngleX;
            this.horseSaddleFront.rotateAngleX = this.body.rotateAngleX;
            this.horseSaddleBack.rotateAngleX = this.body.rotateAngleX;
            this.horseLeftRein.rotationPointY = this.head.rotationPointY;
            this.horseRightRein.rotationPointY = this.head.rotationPointY;
            this.horseFaceRopes.rotationPointY = this.head.rotationPointY;
            this.horseLeftFaceMetal.rotationPointY = this.head.rotationPointY;
            this.horseRightFaceMetal.rotationPointY = this.head.rotationPointY;
            this.horseLeftRein.rotationPointZ = this.head.rotationPointZ;
            this.horseRightRein.rotationPointZ = this.head.rotationPointZ;
            this.horseFaceRopes.rotationPointZ = this.head.rotationPointZ;
            this.horseLeftFaceMetal.rotationPointZ = this.head.rotationPointZ;
            this.horseRightFaceMetal.rotationPointZ = this.head.rotationPointZ;
            this.horseLeftRein.rotateAngleX = var9;
            this.horseRightRein.rotateAngleX = var9;
            this.horseFaceRopes.rotateAngleX = this.head.rotateAngleX;
            this.horseLeftFaceMetal.rotateAngleX = this.head.rotateAngleX;
            this.horseRightFaceMetal.rotateAngleX = this.head.rotateAngleX;
            this.horseFaceRopes.rotateAngleY = this.head.rotateAngleY;
            this.horseLeftFaceMetal.rotateAngleY = this.head.rotateAngleY;
            this.horseLeftRein.rotateAngleY = this.head.rotateAngleY;
            this.horseRightFaceMetal.rotateAngleY = this.head.rotateAngleY;
            this.horseRightRein.rotateAngleY = this.head.rotateAngleY;

            if (var17) {
                this.horseLeftSaddleRope.rotateAngleX = -1.0471976F;
                this.horseLeftSaddleMetal.rotateAngleX = -1.0471976F;
                this.horseRightSaddleRope.rotateAngleX = -1.0471976F;
                this.horseRightSaddleMetal.rotateAngleX = -1.0471976F;
                this.horseLeftSaddleRope.rotateAngleZ = 0.0F;
                this.horseLeftSaddleMetal.rotateAngleZ = 0.0F;
                this.horseRightSaddleRope.rotateAngleZ = 0.0F;
                this.horseRightSaddleMetal.rotateAngleZ = 0.0F;
            } else {
                this.horseLeftSaddleRope.rotateAngleX = var20 / 3.0F;
                this.horseLeftSaddleMetal.rotateAngleX = var20 / 3.0F;
                this.horseRightSaddleRope.rotateAngleX = var20 / 3.0F;
                this.horseRightSaddleMetal.rotateAngleX = var20 / 3.0F;
                this.horseLeftSaddleRope.rotateAngleZ = var20 / 5.0F;
                this.horseLeftSaddleMetal.rotateAngleZ = var20 / 5.0F;
                this.horseRightSaddleRope.rotateAngleZ = -var20 / 5.0F;
                this.horseRightSaddleMetal.rotateAngleZ = -var20 / 5.0F;
            }
        }

        var21 = -1.3089F + p_78086_3_ * 1.5F;

        if (var21 > 0.0F) {
            var21 = 0.0F;
        }

        if (var15) {
            this.tailBase.rotateAngleY = MathHelper.cos(var18 * 0.7F);
            var21 = 0.0F;
        } else {
            this.tailBase.rotateAngleY = 0.0F;
        }

        this.tailMiddle.rotateAngleY = this.tailBase.rotateAngleY;
        this.tailTip.rotateAngleY = this.tailBase.rotateAngleY;
        this.tailMiddle.rotationPointY = this.tailBase.rotationPointY;
        this.tailTip.rotationPointY = this.tailBase.rotationPointY;
        this.tailMiddle.rotationPointZ = this.tailBase.rotationPointZ;
        this.tailTip.rotationPointZ = this.tailBase.rotationPointZ;
        this.tailBase.rotateAngleX = var21;
        this.tailMiddle.rotateAngleX = var21;
        this.tailTip.rotateAngleX = -0.2618F + var21;
    }
}
