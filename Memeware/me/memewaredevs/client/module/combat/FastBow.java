
package me.memewaredevs.client.module.combat;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.combat.RotationUtils;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FastBow extends Module {

    public FastBow() {
        super("Fast Bow", 0, Module.Category.COMBAT);
    }

    @Handler
    public Consumer<UpdateEvent> update = event -> {
        if (event.isPre()) {
            this.getTarg().forEach(target -> {
                if (doCheck()) {
                    mc.thePlayer.inventory.getCurrentItem().setItemDamage(-1);
                    duraHack(9, this.mc.thePlayer.inventory.currentItem);
                    mc.playerController.sendUseItem(mc.thePlayer,
                            mc.theWorld,
                            mc.thePlayer.inventory.getCurrentItem());
                    mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(
                            mc.thePlayer.inventory.getCurrentItem(),
                            mc.theWorld, mc.thePlayer);
                    final float[] rotations = RotationUtils.getBowAngles(target);
                    for (int i2 = 0; i2 < 20; ++i2) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
                                this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, rotations[0],
                                rotations[1], true));
                    }
                    ;
                    duraHack(9, this.mc.thePlayer.inventory.currentItem);
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
                    mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(
                            mc.thePlayer.inventory.getCurrentItem(),
                            mc.theWorld, mc.thePlayer, 10);
                    PacketUtil.sendPacket(new C03PacketPlayer());
                }
            });
        }
    };

    @Override
    public void onEnable() {
    }

    private boolean doCheck() {
        boolean b = mc.thePlayer.getHealth() <= 0.0f
                || !mc.thePlayer.isCollidedVertically
                && !mc.thePlayer.capabilities.isCreativeMode
                || mc.thePlayer.inventory.getCurrentItem() == null
                || !(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow)
                || !mc.gameSettings.keyBindUseItem.getIsKeyPressed();
        return !b;
    }

    @SuppressWarnings("unused")
    private void duraHack(int slot, int hotbarNum) {
        for (int unused : new int[2]) {
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
        }
    }

    @Override
    public void onDisable() {


    }

    private List<EntityLivingBase> getTarg() {
        return mc.theWorld.playerEntities.stream()
                .filter(ent -> mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) <= 25
                        && mc.thePlayer != ent)
                .limit(10).collect(Collectors.toCollection(ArrayList::new));
    }
}
