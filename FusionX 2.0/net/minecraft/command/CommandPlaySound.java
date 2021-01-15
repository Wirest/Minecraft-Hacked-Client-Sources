package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class CommandPlaySound extends CommandBase
{
    private static final String __OBFID = "CL_00000774";

    public String getCommandName()
    {
        return "playsound";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.playsound.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
        }
        else
        {
            byte var3 = 0;
            int var31 = var3 + 1;
            String var4 = args[var3];
            EntityPlayerMP var5 = getPlayer(sender, args[var31++]);
            Vec3 var6 = sender.getPositionVector();
            double var7 = var6.xCoord;

            if (args.length > var31)
            {
                var7 = func_175761_b(var7, args[var31++], true);
            }

            double var9 = var6.yCoord;

            if (args.length > var31)
            {
                var9 = func_175769_b(var9, args[var31++], 0, 0, false);
            }

            double var11 = var6.zCoord;

            if (args.length > var31)
            {
                var11 = func_175761_b(var11, args[var31++], true);
            }

            double var13 = 1.0D;

            if (args.length > var31)
            {
                var13 = parseDouble(args[var31++], 0.0D, 3.4028234663852886E38D);
            }

            double var15 = 1.0D;

            if (args.length > var31)
            {
                var15 = parseDouble(args[var31++], 0.0D, 2.0D);
            }

            double var17 = 0.0D;

            if (args.length > var31)
            {
                var17 = parseDouble(args[var31], 0.0D, 1.0D);
            }

            double var19 = var13 > 1.0D ? var13 * 16.0D : 16.0D;
            double var21 = var5.getDistance(var7, var9, var11);

            if (var21 > var19)
            {
                if (var17 <= 0.0D)
                {
                    throw new CommandException("commands.playsound.playerTooFar", new Object[] {var5.getName()});
                }

                double var23 = var7 - var5.posX;
                double var25 = var9 - var5.posY;
                double var27 = var11 - var5.posZ;
                double var29 = Math.sqrt(var23 * var23 + var25 * var25 + var27 * var27);

                if (var29 > 0.0D)
                {
                    var7 = var5.posX + var23 / var29 * 2.0D;
                    var9 = var5.posY + var25 / var29 * 2.0D;
                    var11 = var5.posZ + var27 / var29 * 2.0D;
                }

                var13 = var17;
            }

            var5.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(var4, var7, var9, var11, (float)var13, (float)var15));
            notifyOperators(sender, this, "commands.playsound.success", new Object[] {var4, var5.getName()});
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 2 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : (args.length > 2 && args.length <= 5 ? func_175771_a(args, 2, pos) : null);
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}
