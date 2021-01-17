// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import java.util.Random;

public class CommandWeather extends CommandBase
{
    @Override
    public String getCommandName() {
        return "weather";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.weather.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length >= 1 && args.length <= 2) {
            int i = (300 + new Random().nextInt(600)) * 20;
            if (args.length >= 2) {
                i = CommandBase.parseInt(args[1], 1, 1000000) * 20;
            }
            final World world = MinecraftServer.getServer().worldServers[0];
            final WorldInfo worldinfo = world.getWorldInfo();
            if ("clear".equalsIgnoreCase(args[0])) {
                worldinfo.setCleanWeatherTime(i);
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
                CommandBase.notifyOperators(sender, this, "commands.weather.clear", new Object[0]);
            }
            else if ("rain".equalsIgnoreCase(args[0])) {
                worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(i);
                worldinfo.setThunderTime(i);
                worldinfo.setRaining(true);
                worldinfo.setThundering(false);
                CommandBase.notifyOperators(sender, this, "commands.weather.rain", new Object[0]);
            }
            else {
                if (!"thunder".equalsIgnoreCase(args[0])) {
                    throw new WrongUsageException("commands.weather.usage", new Object[0]);
                }
                worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(i);
                worldinfo.setThunderTime(i);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
                CommandBase.notifyOperators(sender, this, "commands.weather.thunder", new Object[0]);
            }
            return;
        }
        throw new WrongUsageException("commands.weather.usage", new Object[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "clear", "rain", "thunder") : null;
    }
}
