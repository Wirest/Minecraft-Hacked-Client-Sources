/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.other.EventIsNormalBlock
 *  me.xtrm.delta.api.event.events.update.EventUpdate
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.MathHelper
 */
package delta.module.modules;

import delta.client.DeltaClient;
import delta.utils.BoundingBox;
import delta.utils.MovementUtils;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.other.EventIsNormalBlock;
import me.xtrm.delta.api.event.events.update.EventUpdate;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class NoClip
extends Module {
    public void onDisable() {
        this.mc.thePlayer.noClip = false;
        super.onDisable();
    }

    public NoClip() {
        super("NoClip", Category.Misc);
        this.setDescription("Traverse le sol si le joueur est dans un block (trapdoor, sable, etc...)");
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (DeltaClient.instance.managers.modulesManager.getModule("Freecam").isEnabled()) {
            return;
        }
        BoundingBox boundingBox = new BoundingBox(Math.floor(this.mc.thePlayer.posX), Math.floor(this.mc.thePlayer.posY), Math.floor(this.mc.thePlayer.posZ)).offset(0.0, -1.0, 0.0);
        Block block = this.mc.theWorld.getBlock((int)boundingBox._talented(), (int)boundingBox._adelaide(), (int)boundingBox._produce());
        this.mc.thePlayer.motionY = 0.0;
        if (block == Blocks.air || block.getMaterial().isReplaceable() || block == Blocks.tallgrass) {
            return;
        }
        this.mc.thePlayer.motionY = 1.0;
        this.mc.thePlayer.motionY = this.mc.gameSettings.keyBindJump.getIsKeyPressed() ? (this.mc.gameSettings.keyBindSneak.getIsKeyPressed() ? 0.0 : 0.4) : (this.mc.gameSettings.keyBindSneak.getIsKeyPressed() ? -0.4 : 0.0);
        this.mc.thePlayer.onGround = true;
        double d = MovementUtils.isMoving() ? 0.2 : 0.0;
        double d2 = 0.0;
        if (this.mc.thePlayer.isCollidedHorizontally || d2 != 0.0) {
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + (double)(-MathHelper.sin((float)MovementUtils.getDirection())) * d, this.mc.thePlayer.posY + d2, this.mc.thePlayer.posZ + (double)MathHelper.cos((float)MovementUtils.getDirection()) * d);
        }
    }

    @EventTarget
    public void onNormalBlock(EventIsNormalBlock eventIsNormalBlock) {
        eventIsNormalBlock.setCancelled(true);
    }
}

