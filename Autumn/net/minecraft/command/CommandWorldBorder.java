package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder extends CommandBase {
   public String getCommandName() {
      return "worldborder";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.worldborder.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 1) {
         throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
      } else {
         WorldBorder worldborder = this.getWorldBorder();
         double d6;
         double d10;
         long i1;
         if (args[0].equals("set")) {
            if (args.length != 2 && args.length != 3) {
               throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
            }

            d6 = worldborder.getTargetSize();
            d10 = parseDouble(args[1], 1.0D, 6.0E7D);
            i1 = args.length > 2 ? parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L;
            if (i1 > 0L) {
               worldborder.setTransition(d6, d10, i1);
               if (d6 > d10) {
                  notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6), Long.toString(i1 / 1000L)});
               } else {
                  notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6), Long.toString(i1 / 1000L)});
               }
            } else {
               worldborder.setTransition(d10);
               notifyOperators(sender, this, "commands.worldborder.set.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6)});
            }
         } else if (args[0].equals("add")) {
            if (args.length != 2 && args.length != 3) {
               throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
            }

            d6 = worldborder.getDiameter();
            d10 = d6 + parseDouble(args[1], -d6, 6.0E7D - d6);
            i1 = worldborder.getTimeUntilTarget() + (args.length > 2 ? parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L);
            if (i1 > 0L) {
               worldborder.setTransition(d6, d10, i1);
               if (d6 > d10) {
                  notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6), Long.toString(i1 / 1000L)});
               } else {
                  notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6), Long.toString(i1 / 1000L)});
               }
            } else {
               worldborder.setTransition(d10);
               notifyOperators(sender, this, "commands.worldborder.set.success", new Object[]{String.format("%.1f", d10), String.format("%.1f", d6)});
            }
         } else if (args[0].equals("center")) {
            if (args.length != 3) {
               throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
            }

            BlockPos blockpos = sender.getPosition();
            double d1 = parseDouble((double)blockpos.getX() + 0.5D, args[1], true);
            double d3 = parseDouble((double)blockpos.getZ() + 0.5D, args[2], true);
            worldborder.setCenter(d1, d3);
            notifyOperators(sender, this, "commands.worldborder.center.success", new Object[]{d1, d3});
         } else if (args[0].equals("damage")) {
            if (args.length < 2) {
               throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
            }

            if (args[1].equals("buffer")) {
               if (args.length != 3) {
                  throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
               }

               d6 = parseDouble(args[2], 0.0D);
               d10 = worldborder.getDamageBuffer();
               worldborder.setDamageBuffer(d6);
               notifyOperators(sender, this, "commands.worldborder.damage.buffer.success", new Object[]{String.format("%.1f", d6), String.format("%.1f", d10)});
            } else if (args[1].equals("amount")) {
               if (args.length != 3) {
                  throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
               }

               d6 = parseDouble(args[2], 0.0D);
               d10 = worldborder.getDamageAmount();
               worldborder.setDamageAmount(d6);
               notifyOperators(sender, this, "commands.worldborder.damage.amount.success", new Object[]{String.format("%.2f", d6), String.format("%.2f", d10)});
            }
         } else if (args[0].equals("warning")) {
            if (args.length < 2) {
               throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
            }

            int j = parseInt(args[2], 0);
            int l;
            if (args[1].equals("time")) {
               if (args.length != 3) {
                  throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
               }

               l = worldborder.getWarningTime();
               worldborder.setWarningTime(j);
               notifyOperators(sender, this, "commands.worldborder.warning.time.success", new Object[]{j, l});
            } else if (args[1].equals("distance")) {
               if (args.length != 3) {
                  throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
               }

               l = worldborder.getWarningDistance();
               worldborder.setWarningDistance(j);
               notifyOperators(sender, this, "commands.worldborder.warning.distance.success", new Object[]{j, l});
            }
         } else {
            if (!args[0].equals("get")) {
               throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
            }

            d6 = worldborder.getDiameter();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double(d6 + 0.5D));
            sender.addChatMessage(new ChatComponentTranslation("commands.worldborder.get.success", new Object[]{String.format("%.0f", d6)}));
         }

      }
   }

   protected WorldBorder getWorldBorder() {
      return MinecraftServer.getServer().worldServers[0].getWorldBorder();
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[]{"set", "center", "damage", "warning", "add", "get"}) : (args.length == 2 && args[0].equals("damage") ? getListOfStringsMatchingLastWord(args, new String[]{"buffer", "amount"}) : (args.length >= 2 && args.length <= 3 && args[0].equals("center") ? func_181043_b(args, 1, pos) : (args.length == 2 && args[0].equals("warning") ? getListOfStringsMatchingLastWord(args, new String[]{"time", "distance"}) : null)));
   }
}
