/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package me.memewaredevs.client.module.combat;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.combat.CombatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AntiBot extends Module {

    public AntiBot(final int key, final Module.Category category) {
        super("Anti Bot", key, category);
        this.addModes("ViperMC", "Hypixel", "Mineplex");
    }
    public static ArrayList<Entity> bad = new ArrayList<>();

    @Override
    public void onEnable() {
        bad.clear();
        CombatUtil.botList.clear();
    }

    @Override
    public void onDisable() {
        CombatUtil.botList.clear();
        bad.clear();
    }

    @Handler
    public Consumer<UpdateEvent> fard = (event) -> {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                if (!Float.isNaN(entityPlayer.getHealth())) {
                    if (!bad.contains(entityPlayer)) {
                        bad.add(entityPlayer);
                        CombatUtil.botList.add(entityPlayer);
                    }
                }
                if (entityPlayer.onGround && CombatUtil.botList.contains(entityPlayer)) {
                    CombatUtil.botList.remove(entityPlayer);
                }
            }
        }
    };

}
