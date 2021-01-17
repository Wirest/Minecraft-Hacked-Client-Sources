// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;

public class CommandPlaySound extends CommandBase
{
    @Override
    public String getCommandName() {
        return "playsound";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.playsound.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
        }
        int i = 0;
        final String s = args[i++];
        final EntityPlayerMP entityplayermp = CommandBase.getPlayer(sender, args[i++]);
        final Vec3 vec3 = sender.getPositionVector();
        double d0 = vec3.xCoord;
        if (args.length > i) {
            d0 = CommandBase.parseDouble(d0, args[i++], true);
        }
        double d2 = vec3.yCoord;
        if (args.length > i) {
            d2 = CommandBase.parseDouble(d2, args[i++], 0, 0, false);
        }
        double d3 = vec3.zCoord;
        if (args.length > i) {
            d3 = CommandBase.parseDouble(d3, args[i++], true);
        }
        double d4 = 1.0;
        if (args.length > i) {
            d4 = CommandBase.parseDouble(args[i++], 0.0, 3.4028234663852886E38);
        }
        double d5 = 1.0;
        if (args.length > i) {
            d5 = CommandBase.parseDouble(args[i++], 0.0, 2.0);
        }
        double d6 = 0.0;
        if (args.length > i) {
            d6 = CommandBase.parseDouble(args[i], 0.0, 1.0);
        }
        final double d7 = (d4 > 1.0) ? (d4 * 16.0) : 16.0;
        final double d8 = entityplayermp.getDistance(d0, d2, d3);
        if (d8 > d7) {
            if (d6 <= 0.0) {
                throw new CommandException("commands.playsound.playerTooFar", new Object[] { entityplayermp.getName() });
            }
            final double d9 = d0 - entityplayermp.posX;
            final double d10 = d2 - entityplayermp.posY;
            final double d11 = d3 - entityplayermp.posZ;
            final double d12 = Math.sqrt(d9 * d9 + d10 * d10 + d11 * d11);
            if (d12 > 0.0) {
                d0 = entityplayermp.posX + d9 / d12 * 2.0;
                d2 = entityplayermp.posY + d10 / d12 * 2.0;
                d3 = entityplayermp.posZ + d11 / d12 * 2.0;
            }
            d4 = d6;
        }
        entityplayermp.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(s, d0, d2, d3, (float)d4, (float)d5));
        CommandBase.notifyOperators(sender, this, "commands.playsound.success", s, entityplayermp.getName());
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length > 2 && args.length <= 5) ? CommandBase.func_175771_a(args, 2, pos) : null);
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 1;
    }
}
