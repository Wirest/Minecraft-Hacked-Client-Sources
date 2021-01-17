// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraft.util.EnumParticleTypes;

public class CommandParticle extends CommandBase
{
    @Override
    public String getCommandName() {
        return "particle";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.particle.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        }
        boolean flag = false;
        EnumParticleTypes enumparticletypes = null;
        EnumParticleTypes[] values;
        for (int length = (values = EnumParticleTypes.values()).length, k = 0; k < length; ++k) {
            final EnumParticleTypes enumparticletypes2 = values[k];
            if (enumparticletypes2.hasArguments()) {
                if (args[0].startsWith(enumparticletypes2.getParticleName())) {
                    flag = true;
                    enumparticletypes = enumparticletypes2;
                    break;
                }
            }
            else if (args[0].equals(enumparticletypes2.getParticleName())) {
                flag = true;
                enumparticletypes = enumparticletypes2;
                break;
            }
        }
        if (!flag) {
            throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
        }
        final String s = args[0];
        final Vec3 vec3 = sender.getPositionVector();
        final double d6 = (float)CommandBase.parseDouble(vec3.xCoord, args[1], true);
        final double d7 = (float)CommandBase.parseDouble(vec3.yCoord, args[2], true);
        final double d8 = (float)CommandBase.parseDouble(vec3.zCoord, args[3], true);
        final double d9 = (float)CommandBase.parseDouble(args[4]);
        final double d10 = (float)CommandBase.parseDouble(args[5]);
        final double d11 = (float)CommandBase.parseDouble(args[6]);
        final double d12 = (float)CommandBase.parseDouble(args[7]);
        int i = 0;
        if (args.length > 8) {
            i = CommandBase.parseInt(args[8], 0);
        }
        boolean flag2 = false;
        if (args.length > 9 && "force".equals(args[9])) {
            flag2 = true;
        }
        final World world = sender.getEntityWorld();
        if (world instanceof WorldServer) {
            final WorldServer worldserver = (WorldServer)world;
            final int[] aint = new int[enumparticletypes.getArgumentCount()];
            if (enumparticletypes.hasArguments()) {
                final String[] astring = args[0].split("_", 3);
                for (int j = 1; j < astring.length; ++j) {
                    try {
                        aint[j - 1] = Integer.parseInt(astring[j]);
                    }
                    catch (NumberFormatException var29) {
                        throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
                    }
                }
            }
            worldserver.spawnParticle(enumparticletypes, flag2, d6, d7, d8, i, d9, d10, d11, d12, aint);
            CommandBase.notifyOperators(sender, this, "commands.particle.success", s, Math.max(i, 1));
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, EnumParticleTypes.getParticleNames()) : ((args.length > 1 && args.length <= 4) ? CommandBase.func_175771_a(args, 1, pos) : ((args.length == 10) ? CommandBase.getListOfStringsMatchingLastWord(args, "normal", "force") : null));
    }
}
