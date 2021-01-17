package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import me.rigamortis.faurax.hooks.GuiIngameHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityOtherPlayerMP
extends AbstractClientPlayer {
    private boolean isItemInUse;
    private int otherPlayerMPPosRotationIncrements;
    private double otherPlayerMPX;
    private double otherPlayerMPY;
    private double otherPlayerMPZ;
    private double otherPlayerMPYaw;
    private double otherPlayerMPPitch;
    private static final String __OBFID = "CL_00000939";

    public EntityOtherPlayerMP(World worldIn, GameProfile p_i45075_2_) {
        super(worldIn, p_i45075_2_);
        this.stepHeight = 0.0f;
        this.noClip = true;
        this.field_71082_cx = 0.25f;
        this.renderDistanceWeight = 10.0;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return true;
    }

    @Override
    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        this.otherPlayerMPX = p_180426_1_;
        this.otherPlayerMPY = p_180426_3_;
        this.otherPlayerMPZ = p_180426_5_;
        this.otherPlayerMPYaw = p_180426_7_;
        this.otherPlayerMPPitch = p_180426_8_;
        this.otherPlayerMPPosRotationIncrements = p_180426_9_;
    }

    @Override
    public void onUpdate() {
        this.field_71082_cx = 0.0f;
        super.onUpdate();
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double var1 = this.posX - this.prevPosX;
        double var3 = this.posZ - this.prevPosZ;
        float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3) * 4.0f;
        if (var5 > 1.0f) {
            var5 = 1.0f;
        }
        this.limbSwingAmount += (var5 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
        if (!this.isItemInUse && this.isEating() && this.inventory.mainInventory[this.inventory.currentItem] != null) {
            ItemStack var6 = this.inventory.mainInventory[this.inventory.currentItem];
            this.setItemInUse(this.inventory.mainInventory[this.inventory.currentItem], var6.getItem().getMaxItemUseDuration(var6));
            this.isItemInUse = true;
        } else if (this.isItemInUse && !this.isEating()) {
            this.clearItemInUse();
            this.isItemInUse = false;
        }
    }

    @Override
    public void onLivingUpdate() {
        if (this.otherPlayerMPPosRotationIncrements > 0) {
            double var1 = this.posX + (this.otherPlayerMPX - this.posX) / (double)this.otherPlayerMPPosRotationIncrements;
            double var3 = this.posY + (this.otherPlayerMPY - this.posY) / (double)this.otherPlayerMPPosRotationIncrements;
            double var5 = this.posZ + (this.otherPlayerMPZ - this.posZ) / (double)this.otherPlayerMPPosRotationIncrements;
            double var7 = this.otherPlayerMPYaw - (double)this.rotationYaw;
            while (var7 < -180.0) {
                var7 += 360.0;
            }
            while (var7 >= 180.0) {
                var7 -= 360.0;
            }
            this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.otherPlayerMPPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.otherPlayerMPPitch - (double)this.rotationPitch) / (double)this.otherPlayerMPPosRotationIncrements);
            --this.otherPlayerMPPosRotationIncrements;
            this.setPosition(var1, var3, var5);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        this.prevCameraYaw = this.cameraYaw;
        this.updateArmSwingProgress();
        float var9 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float var2 = (float)Math.atan((- this.motionY) * 0.20000000298023224) * 15.0f;
        if (var9 > 0.1f) {
            var9 = 0.1f;
        }
        if (!this.onGround || this.getHealth() <= 0.0f) {
            var9 = 0.0f;
        }
        if (this.onGround || this.getHealth() <= 0.0f) {
            var2 = 0.0f;
        }
        this.cameraYaw += (var9 - this.cameraYaw) * 0.4f;
        this.cameraPitch += (var2 - this.cameraPitch) * 0.8f;
    }

    @Override
    public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn) {
        if (slotIn == 0) {
            this.inventory.mainInventory[this.inventory.currentItem] = itemStackIn;
        } else {
            this.inventory.armorInventory[slotIn - 1] = itemStackIn;
        }
    }

    @Override
    public void addChatMessage(IChatComponent message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
    }

    @Override
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        return false;
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }
}

