// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public interface ICommandSender
{
    String getName();
    
    IChatComponent getDisplayName();
    
    void addChatMessage(final IChatComponent p0);
    
    boolean canCommandSenderUseCommand(final int p0, final String p1);
    
    BlockPos getPosition();
    
    Vec3 getPositionVector();
    
    World getEntityWorld();
    
    Entity getCommandSenderEntity();
    
    boolean sendCommandFeedback();
    
    void setCommandStat(final CommandResultStats.Type p0, final int p1);
}
