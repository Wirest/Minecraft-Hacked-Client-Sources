package me.memewaredevs.client.module.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.ChatUtil;
import me.memewaredevs.client.util.movement.MovementUtils;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockPane;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.Iterator;
import java.util.function.Consumer;

public class HackerDetect extends Module {

    EntityLivingBase player;

    public HackerDetect() {
        super("Hacker Detect", 0, Category.MISC);
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer1 = (event) -> {
        for (Iterator<Entity> people = mc.theWorld.loadedEntityList.iterator(); people.hasNext();) { // simple way to get poop players
            Object gottem = people.next();
            if (gottem instanceof EntityPlayer) {
                EntityPlayer aPlayer = (EntityPlayer) gottem;
                if(aPlayer != mc.thePlayer) {
                    this.player = aPlayer;
                    if (this.player.ticksExisted > 50) {
                        if (this.player.rotationPitch < -90 || this.player.rotationPitch > 90) // poop headless invalid direction check for gay auras
                            ChatUtil.printChatFlag(this.player.getName() + " is rotated in an invalid direction!");

//                        if (this.player.posX > this.player.lastTickPosX + 1 && !(mc.theWorld.getBlock(this.player.posX, this.player.posY - 1, this.player.posZ) instanceof BlockIce) && !(mc.theWorld.getBlock(this.player.posX, this.player.posY - 1, this.player.posZ) instanceof BlockPackedIce) || this.player.posX < this.player.lastTickPosX - 1 && !(mc.theWorld.getBlock(this.player.posX, this.player.posY - 1, this.player.posZ) instanceof BlockIce) && !(mc.theWorld.getBlock(this.player.posX, this.player.posY - 1, this.player.posZ) instanceof BlockPackedIce) || this.player.posZ > this.player.lastTickPosZ + 1 && !(mc.theWorld.getBlock(this.player.posX, this.player.posY - 1, this.player.posZ) instanceof BlockIce) && !(mc.theWorld.getBlock(this.player.posX, this.player.posY - 1, this.player.posZ) instanceof BlockPackedIce) || this.player.posZ < this.player.lastTickPosZ - 1 && !(mc.theWorld.getBlock(this.player.posX, this.player.posY - 1, this.player.posZ) instanceof BlockIce) && !(mc.theWorld.getBlock(this.player.posX, this.player.posY - 1, this.player.posZ) instanceof BlockPackedIce))
//                            if (!this.player.getName().endsWith("s") && this.isMoving(this.player) && !this.player.isInvisible())
//                                ChatUtil.printChatFlag(this.player.getName() + "'s speed isn't legitimate!");
//                            else if (this.isMoving(this.player) && !this.player.isInvisible())
//                                ChatUtil.printChatFlag(this.player.getName() + "' speed isn't legitimate!");
                    } // fixing my cancer code here later, lazy rn
                }
            }
        }
    };

    private boolean isMoving(EntityLivingBase player) { // this for later
        if(player.prevPosX != player.posX)
            return true;
        if(player.prevPosY != player.posY)
            return true;
        if(player.prevPosZ != player.posZ)
            return true;
        return false;
    }
}
