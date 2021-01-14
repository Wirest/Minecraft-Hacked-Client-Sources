package net.minecraft.command;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle extends CommandBase {
    private static final String __OBFID = "CL_00002341";

    public String getCommandName() {
        return "particle";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.particle.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        } else {
            boolean var3 = false;
            EnumParticleTypes var4 = null;
            EnumParticleTypes[] var5 = EnumParticleTypes.values();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                EnumParticleTypes var8 = var5[var7];

                if (var8.func_179343_f()) {
                    if (args[0].startsWith(var8.func_179346_b())) {
                        var3 = true;
                        var4 = var8;
                        break;
                    }
                } else if (args[0].equals(var8.func_179346_b())) {
                    var3 = true;
                    var4 = var8;
                    break;
                }
            }

            if (!var3) {
                throw new CommandException("commands.particle.notFound", new Object[]{args[0]});
            } else {
                String var30 = args[0];
                Vec3 var31 = sender.getPositionVector();
                double var32 = (double) ((float) func_175761_b(var31.xCoord, args[1], true));
                double var9 = (double) ((float) func_175761_b(var31.yCoord, args[2], true));
                double var11 = (double) ((float) func_175761_b(var31.zCoord, args[3], true));
                double var13 = (double) ((float) parseDouble(args[4]));
                double var15 = (double) ((float) parseDouble(args[5]));
                double var17 = (double) ((float) parseDouble(args[6]));
                double var19 = (double) ((float) parseDouble(args[7]));
                int var21 = 0;

                if (args.length > 8) {
                    var21 = parseInt(args[8], 0);
                }

                boolean var22 = false;

                if (args.length > 9 && "force".equals(args[9])) {
                    var22 = true;
                }

                World var23 = sender.getEntityWorld();

                if (var23 instanceof WorldServer) {
                    WorldServer var24 = (WorldServer) var23;
                    int[] var25 = new int[var4.func_179345_d()];

                    if (var4.func_179343_f()) {
                        String[] var26 = args[0].split("_", 3);

                        for (int var27 = 1; var27 < var26.length; ++var27) {
                            try {
                                var25[var27 - 1] = Integer.parseInt(var26[var27]);
                            } catch (NumberFormatException var29) {
                                throw new CommandException("commands.particle.notFound", new Object[]{args[0]});
                            }
                        }
                    }

                    var24.func_180505_a(var4, var22, var32, var9, var11, var21, var13, var15, var17, var19, var25);
                    notifyOperators(sender, this, "commands.particle.success", new Object[]{var30, Integer.valueOf(Math.max(var21, 1))});
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, EnumParticleTypes.func_179349_a()) : (args.length > 1 && args.length <= 4 ? func_175771_a(args, 1, pos) : (args.length == 9 ? getListOfStringsMatchingLastWord(args, new String[]{"normal", "force"}) : null));
    }
}
