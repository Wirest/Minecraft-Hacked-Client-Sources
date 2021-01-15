package com.ihl.client.hook;

import com.ihl.client.event.*;
import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.world.World;

public class HookEntityPlayerSP extends EntityPlayerSP {

    public HookEntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatFileWriter p_i46278_4_) {
        super(mcIn, worldIn, p_i46278_3_, p_i46278_4_);
    }

    public void moveEntity(double x, double y, double z) {
        EventPlayerMove event = new EventPlayerMove(Event.Type.PRE, x, y, z);
        Module.event(event, false);

        if (event.cancelled) {
            return;
        }

        x = event.x;
        y = event.y;
        z = event.z;

        super.moveEntity(x, y, z);

        event.type = Event.Type.POST;
        Module.event(event, true);
    }

    public void onUpdate() {
        EventPlayerUpdate event = new EventPlayerUpdate(Event.Type.PRE);
        Module.event(event, false);

        if (event.cancelled) {
            return;
        }

        super.onUpdate();

        event.type = Event.Type.POST;
        Module.event(event, true);
    }

    public void onMotion() {
        EventPlayerMotion event = new EventPlayerMotion(Event.Type.PRE);
        Module.event(event, false);

        if (event.cancelled) {
            return;
        }

        super.onMotion();

        event.type = Event.Type.POST;
        Module.event(event, true);
    }

    public void sendChatMessage(String message) {
        EventChatSend event = new EventChatSend(Event.Type.PRE, message);
        Module.event(event, false);

        if (event.cancelled) {
            return;
        }

        message = event.message;

        super.sendChatMessage(message);
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        if (!Module.get("phase").active && (!Module.get("freecam").active || !Option.get(Module.get("freecam").options, "noclip").BOOLEAN())) {
            return super.pushOutOfBlocks(x, y, z);
        }
        return false;
    }

    public boolean isEntityInsideOpaqueBlock()
    {
        return super.isEntityInsideOpaqueBlock() && !Module.get("phase").active && !Module.get("noclip").active && !Module.get("freecam").active;
    }
}
