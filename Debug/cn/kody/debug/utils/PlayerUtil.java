package cn.kody.debug.utils;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

public class PlayerUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public void portMove(float yaw, float multiplyer, float up) {
        double moveX = (- Math.sin(Math.toRadians(yaw))) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians(yaw)) * (double)multiplyer;
        double moveY = up;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.thePlayer.setPosition(moveX + Minecraft.thePlayer.posX, moveY + Minecraft.thePlayer.posY, moveZ + Minecraft.thePlayer.posZ);
    }

    public static void setMotion(double speed) {
        double forward = Minecraft.thePlayer.movementInput.moveForward;
        double strafe = Minecraft.thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Minecraft.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            Minecraft.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

//    public static boolean canBlock() {
//        var1 = Minecraft.getMinecraft().theWorld.loadedEntityList.iterator();
//        {
//            if (!var1.hasNext()) {
//                return false;
//            }
//            o = (Entity)var1.next();
//            if (!(o instanceof EntityPlayer) || o.isDead)
//            Minecraft.getMinecraft();
//            if (o == Minecraft.thePlayer)
//            Minecraft.getMinecraft();
//        } while (Minecraft.thePlayer.getDistanceToEntity(o) > 8.0f);
//        return true;
//    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static float getDirection() {
        Minecraft.getMinecraft();
        float yaw = Minecraft.thePlayer.rotationYaw;
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.moveForward > 0.0f) {
                forward = 0.5f;
            }
        }
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }

    public static boolean isInWater() {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ)).getBlock().getMaterial() == Material.water;
    }

    public static void toFwd(double speed) {
        Minecraft.getMinecraft();
        float yaw = Minecraft.thePlayer.rotationYaw * 0.017453292f;
        Minecraft.getMinecraft();
        EntityPlayerSP thePlayer = Minecraft.thePlayer;
        thePlayer.motionX -= (double)MathHelper.sin(yaw) * speed;
        Minecraft.getMinecraft();
        EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
        thePlayer2.motionZ += (double)MathHelper.cos(yaw) * speed;
    }

    public static void setSpeed(double speed) {
        Minecraft.getMinecraft();
        Minecraft.thePlayer.motionX = - Math.sin(PlayerUtil.getDirection()) * speed;
        Minecraft.getMinecraft();
        Minecraft.thePlayer.motionZ = Math.cos(PlayerUtil.getDirection()) * speed;
    }

    public static double getSpeed() {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double var10000 = Minecraft.thePlayer.motionX;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double var10001 = Minecraft.thePlayer.motionZ;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        return Math.sqrt((var10000 *= Minecraft.thePlayer.motionX) + var10001 * Minecraft.thePlayer.motionZ);
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
        return PlayerUtil.getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }

    public static Block getBlock(BlockPos pos) {
        Minecraft.getMinecraft();
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
        return PlayerUtil.getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static ArrayList vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        Minecraft mc = Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double posX = tpX - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double posY = tpY - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() + 1.1);
        Minecraft.getMinecraft();
        double posZ = tpZ - Minecraft.thePlayer.posZ;
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        float pitch = (float)((- Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ))) * 180.0 / 3.141592653589793);
        Minecraft.getMinecraft();
        double tmpX = Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double tmpY = Minecraft.thePlayer.posY;
        Minecraft.getMinecraft();
        double tmpZ = Minecraft.thePlayer.posZ;
        double steps = 1.0;
        double d = speed;
        do {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            if (d >= PlayerUtil.getDistance(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, tpX, tpY, tpZ)) break;
            steps += 1.0;
            d += speed;
        } while (true);
        d = speed;
        do {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            if (d >= PlayerUtil.getDistance(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, tpX, tpY, tpZ)) break;
            Minecraft.getMinecraft();
            tmpX = Minecraft.thePlayer.posX - Math.sin(PlayerUtil.getDirection(yaw)) * d;
            Minecraft.getMinecraft();
            tmpZ = Minecraft.thePlayer.posZ + Math.cos(PlayerUtil.getDirection(yaw)) * d;
            Minecraft.getMinecraft();
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (Minecraft.thePlayer.posY - tpY) / steps), (float)tmpZ));
            d += speed;
        } while (true);
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.moveForward > 0.0f) {
                forward = 0.5f;
            }
        }
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d2 = y1 - y2;
        double d3 = z1 - z2;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }

    public static boolean MovementInput() {
        return PlayerUtil.mc.gameSettings.keyBindForward.pressed || PlayerUtil.mc.gameSettings.keyBindLeft.pressed || PlayerUtil.mc.gameSettings.keyBindRight.pressed || PlayerUtil.mc.gameSettings.keyBindBack.pressed;
    }

    public static void blockHit(Entity en, boolean value) {
        Minecraft mc = Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        ItemStack stack = Minecraft.thePlayer.getCurrentEquippedItem();
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.getCurrentEquippedItem() != null && en != null && value && stack.getItem() instanceof ItemSword) {
            Minecraft.getMinecraft();
            if ((double)Minecraft.thePlayer.swingProgress > 0.2) {
                Minecraft.getMinecraft();
                Minecraft.getMinecraft();
                Minecraft.thePlayer.getCurrentEquippedItem().useItemRightClick(Minecraft.getMinecraft().theWorld, Minecraft.thePlayer);
            }
        }
    }

    public static float getItemAtkDamage(ItemStack itemStack) {
        Iterator iterator;
        Multimap<String, AttributeModifier> multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            double damage;
            Map.Entry entry = (Map.Entry)iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double d = damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            if (attributeModifier.getAmount() > 1.0) {
                return 1.0f + (float)damage;
            }
            return 1.0f;
        }
        return 1.0f;
    }

    public static int bestWeapon(Entity target) {
        Minecraft mc = Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.thePlayer.inventory.currentItem = 0;
        int firstSlot = 0;
        int bestWeapon = -1;
        int j = 1;
        for (int i = 0; i < 9; i = (int)((byte)(i + 1))) {
            Minecraft.getMinecraft();
            Minecraft.thePlayer.inventory.currentItem = i;
            Minecraft.getMinecraft();
            ItemStack itemStack = Minecraft.thePlayer.getHeldItem();
            if (itemStack == null) continue;
            int itemAtkDamage = (int)PlayerUtil.getItemAtkDamage(itemStack);
            if ((itemAtkDamage = (int)((float)itemAtkDamage + EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED))) <= j) continue;
            j = itemAtkDamage;
            bestWeapon = i;
        }
        if (bestWeapon != -1) {
            return bestWeapon;
        }
        return firstSlot;
    }

    public static void shiftClick(Item i) {
        for (int i1 = 9; i1 < 37; ++i1) {
            Minecraft.getMinecraft();
            ItemStack itemstack = Minecraft.thePlayer.inventoryContainer.getSlot(i1).getStack();
            if (itemstack == null || itemstack.getItem() != i) continue;
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            Minecraft.playerController.windowClick(0, i1, 0, 1, Minecraft.thePlayer);
            break;
        }
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            Minecraft.getMinecraft();
            ItemStack itemstack = Minecraft.thePlayer.inventory.getStackInSlot(i);
            if (itemstack != null) continue;
            return false;
        }
        return true;
    }

    public static void tellPlayer(String string) {
        Minecraft.getMinecraft();
        Minecraft.thePlayer.addChatMessage(new ChatComponentText(string));
    }

    public static void tellDebugPlayer(String string) {
        PlayerUtil.tellPlayer("\u00a7d[Debug]\u00a7f " + string);
    }
}

