/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventAttackEntity;
import me.slowly.client.events.EventBlockBB;
import me.slowly.client.events.EventPostMotion;
import me.slowly.client.events.EventPotionSaver;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventReceivePacket;
import me.slowly.client.events.EventSendPacket;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;

public class Test
extends Mod {
    private boolean flag1;
    private boolean flag2;
    private boolean flag3;
    private boolean flag4;
    private boolean flag5;
    private boolean flag6;
    private boolean flag7;
    private boolean flag8;
    private boolean flag9;
    private boolean flag10;
    TimeHelper time = new TimeHelper();

    public Test() {
        super("Test", Mod.Category.PLAYER, Colors.RED.c);
    }
    

    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Test Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Test Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
    }

    @EventTarget
    public void onReceive(EventReceivePacket event) {
    }

    @EventTarget
    public void BlockBoundingBox(EventBlockBB event) {
    }

    @EventTarget
    public void attack(EventAttackEntity event) {
    }

    @EventTarget
    public void onPotion(EventPotionSaver event) {
    }
}

