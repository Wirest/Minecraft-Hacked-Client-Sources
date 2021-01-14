
package me.memewaredevs.client.module.combat.aura;

import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.combat.Aura;
import me.memewaredevs.client.module.combat.AutoPotion;
import me.memewaredevs.client.util.combat.CombatUtil;
import me.memewaredevs.client.util.combat.RotationUtils;
import me.memewaredevs.client.util.misc.Timer;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class SingleAura {
    private static Timer timer = new Timer();
    private static boolean block;
    public static boolean unblock;
    private static boolean unblocked;
    public static void doUpdate(final Aura aura, final UpdateEvent e, final Minecraft mc) {
        boolean invisible = aura.getBool("Invisibles");
        double range = aura.getDouble("Range");
        boolean players = aura.getBool("Players");
        boolean monsters = aura.getBool("Monsters");
        boolean teams = false;
        final Entity entity = CombatUtil.getTarget(teams, monsters, players, range, invisible);
        final boolean autoBlock = aura.getBool("Auto Block");
        final long clickSpeed = aura.getDouble("Click Speed").longValue();
        Aura.isBlocking = mc.thePlayer.getHeldItem() != null
                && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock && CombatUtil.canBlock(
                teams, false, true, range + 4.0, true);
        BlockPos a = new BlockPos(-0.2223, -0.132, -0.312);
        if (Aura.isBlocking) {
            if (e.isPost() && block) {
                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(a, 255, mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
                unblock = true;
            }
        }
        block = true;
        if (AutoPotion.isPotting())
            return;
        Aura.currentEntity = (EntityLivingBase) entity;
        if (entity != null) {
            final float[] rots = RotationUtils.getRotations((EntityLivingBase) entity);
            e.setYaw(rots[0]);
            e.setPitch(rots[1]);
            if (unblocked) {
                mc.thePlayer.swingItem();
                PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                unblocked = false;
            }
            if (timer.delay(1000L / clickSpeed) && e.isPre()) {
                PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, a.offsetDown(), EnumFacing.UP));
                unblocked = true;
                block = false;
                timer.reset();
            }
        }
    }
}
