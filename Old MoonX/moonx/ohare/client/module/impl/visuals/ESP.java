package moonx.ohare.client.module.impl.visuals;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.combat.AntiBot;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.ColorValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * made by oHare for eclipse
 *
 * @since 8/30/2019
 **/
public class ESP extends Module {
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    private BooleanValue box = new BooleanValue("Box", true);
    private BooleanValue health = new BooleanValue("Health", true);
    private BooleanValue armor = new BooleanValue("Armor", true);
    private BooleanValue filled = new BooleanValue("Filled", false);
    private BooleanValue corner = new BooleanValue("Corner", false);
    private NumberValue<Float> thickness = new NumberValue<>("Thickness", 1.5f, 1.5f, 5.0f, 0.25f);
    private ColorValue color = new ColorValue("Color", new Color(240, 177, 175).getRGB());

    public ESP() {
        super("ESP", Category.VISUALS, new Color(175, 240, 238).getRGB());
    }

    @Handler
    public void onRender2D(Render2DEvent event) {
        getMc().theWorld.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if (isValid(ent) && RenderUtil.isInViewFrustrum(ent)) {
                    double posX = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks());
                    double posY = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks());
                    double posZ = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks());
                    double width = entity.width / 1.5;
                    double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                    AxisAlignedBB aabb = new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height + 0.05, posZ + width);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                    getMc().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - getMc().getRenderManager().viewerPosX, vector.y - getMc().getRenderManager().viewerPosY, vector.z - getMc().getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    getMc().entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        final Color clr = new Color(getColor(ent));
                        GL11.glPushMatrix();
                        final float x = (float) position.x;
                        final float w = (float) position.z - x;
                        final float y = (float) position.y + 3;
                        final float h = (float) position.w - y;
                        if (health.isEnabled()) {
                            RenderUtil.drawBar(x - 3f - thickness.getValue() / 2, y - 1, 1.5f, h + 2, ((int)ent.getMaxHealth()) / 2, ((int)ent.getHealth()) / 2, getHealthColor(ent));
                        }
                        if (armor.isEnabled() && entity instanceof EntityPlayer) {
                            double armorstrength = 0;
                            EntityPlayer player = (EntityPlayer) entity;
                            for (int index = 3; index >= 0; index--) {
                                final ItemStack stack = player.inventory.armorInventory[index];
                                if (stack != null) {
                                    armorstrength += getArmorStrength(stack);
                                }
                            }
                            if (armorstrength > 0.0f) {
                                RenderUtil.drawBar(x + w + 1.5f + thickness.getValue() / 2, y - 1, 1.5f, h + 2, 4, (int) (Math.min(armorstrength, 40) / 10),0xff5C7AFF);
                            }
                        }
                        if (filled.isEnabled())
                            RenderUtil.drawRect(x, y, w, h, new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), 120).getRGB());
                        if (box.isEnabled()) {
                            if (corner.isEnabled()) {
                                RenderUtil.drawCornerRect(x - 1.0 - thickness.getValue() / 2, y - 1.0 - thickness.getValue() / 2, w + 2 + thickness.getValue(), h + 2 + thickness.getValue(), thickness.getValue(), 0xff000000, true, 0.5f);
                                RenderUtil.drawCornerRect(x - 0.5f - thickness.getValue() / 2, y - 0.5f - thickness.getValue() / 2, w + 1 + thickness.getValue(), h + 1 + thickness.getValue(), thickness.getValue() - 1, clr.getRGB(), false, 0);
                            } else {
                                RenderUtil.drawBorderedRect(x - thickness.getValue() / 2, y - thickness.getValue() / 2, w + thickness.getValue(), h + thickness.getValue(), thickness.getValue() - 1, 0xff000000, 0);
                                RenderUtil.drawBorderedRect(x - 0.5 - thickness.getValue() / 2, y - 0.5 - thickness.getValue() / 2, w + 1 + thickness.getValue(), h + 1 + thickness.getValue(), thickness.getValue() - 1, clr.getRGB(), 0);
                                RenderUtil.drawBorderedRect(x - 1 - thickness.getValue() / 2, y - 1 - thickness.getValue() / 2, w + 2 + thickness.getValue(), h + 2 + thickness.getValue(), 0.5f, 0xff000000, 0);
                            }
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
        });
    }

    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.getBots().contains(entity) && getMc().thePlayer != entity && entity.getEntityId() != -1488 && isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || ((mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime)) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }

    private int getColor(EntityLivingBase ent) {
        if (Moonx.INSTANCE.getFriendManager().isFriend(ent.getName())) return new Color(122, 190, 255).getRGB();
        else if (ent.getName().equals(getMc().thePlayer.getName())) return new Color(0xFF99ff99).getRGB();
        return color.getValue();
    }

    private int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0F, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0F, 1.0F, 0.8f) | 0xFF000000;
    }


    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }
}
