package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ICommandSender {
    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    String getName();

    IChatComponent getDisplayName();

    /**
     * Notifies this sender of some sort of information.  This is for messages intended to display to the user.  Used
     * for typical output (like "you asked for whether or not this game rule is set, so here's your answer"), warnings
     * (like "I fetched this block for you by ID, but I'd like you to know that every time you do this, I die a little
     * inside"), and errors (like "it's not called iron_pixacke, silly").
     */
    void addChatMessage(IChatComponent var1);

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    boolean canCommandSenderUseCommand(int var1, String var2);

    BlockPos getPosition();

    Vec3 getPositionVector();

    World getEntityWorld();

    Entity getCommandSenderEntity();

    boolean sendCommandFeedback();

    void func_174794_a(CommandResultStats.Type var1, int var2);
}
