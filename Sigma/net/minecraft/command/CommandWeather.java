package net.minecraft.command;

import java.util.List;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather extends CommandBase {
    private static final String __OBFID = "CL_00001185";

    @Override
    public String getCommandName() {
        return "weather";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.weather.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1 && args.length <= 2) {
            int var3 = (300 + (new Random()).nextInt(600)) * 20;

            if (args.length >= 2) {
                var3 = CommandBase.parseInt(args[1], 1, 1000000) * 20;
            }

            WorldServer var4 = MinecraftServer.getServer().worldServers[0];
            WorldInfo var5 = var4.getWorldInfo();

            if ("clear".equalsIgnoreCase(args[0])) {
                var5.func_176142_i(var3);
                var5.setRainTime(0);
                var5.setThunderTime(0);
                var5.setRaining(false);
                var5.setThundering(false);
                CommandBase.notifyOperators(sender, this, "commands.weather.clear", new Object[0]);
            } else if ("rain".equalsIgnoreCase(args[0])) {
                var5.func_176142_i(0);
                var5.setRainTime(var3);
                var5.setThunderTime(var3);
                var5.setRaining(true);
                var5.setThundering(false);
                CommandBase.notifyOperators(sender, this, "commands.weather.rain", new Object[0]);
            } else {
                if (!"thunder".equalsIgnoreCase(args[0])) {
                    throw new WrongUsageException("commands.weather.usage", new Object[0]);
                }

                var5.func_176142_i(0);
                var5.setRainTime(var3);
                var5.setThunderTime(var3);
                var5.setRaining(true);
                var5.setThundering(true);
                CommandBase.notifyOperators(sender, this, "commands.weather.thunder", new Object[0]);
            }
        } else {
            throw new WrongUsageException("commands.weather.usage", new Object[0]);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, new String[]{"clear", "rain", "thunder"}) : null;
    }
}
