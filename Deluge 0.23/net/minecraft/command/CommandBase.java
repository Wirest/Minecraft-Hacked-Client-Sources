package net.minecraft.command;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public abstract class CommandBase implements ICommand
{
    private static IAdminCommand theAdmin;
    private static final String __OBFID = "CL_00001739";

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    public List getCommandAliases()
    {
        return Collections.emptyList();
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return null;
    }

    public static int parseInt(String input) throws NumberInvalidException
    {
        try
        {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException var2)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
        }
    }

    public static int parseInt(String input, int min) throws NumberInvalidException
    {
        return parseInt(input, min, Integer.MAX_VALUE);
    }

    public static int parseInt(String input, int min, int max) throws NumberInvalidException
    {
        int var3 = parseInt(input);

        if (var3 < min)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Integer.valueOf(var3), Integer.valueOf(min)});
        }
        else if (var3 > max)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Integer.valueOf(var3), Integer.valueOf(max)});
        }
        else
        {
            return var3;
        }
    }

    public static long parseLong(String input) throws NumberInvalidException
    {
        try
        {
            return Long.parseLong(input);
        }
        catch (NumberFormatException var2)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
        }
    }

    public static long parseLong(String input, long min, long max) throws NumberInvalidException
    {
        long var5 = parseLong(input);

        if (var5 < min)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Long.valueOf(var5), Long.valueOf(min)});
        }
        else if (var5 > max)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Long.valueOf(var5), Long.valueOf(max)});
        }
        else
        {
            return var5;
        }
    }

    public static BlockPos func_175757_a(ICommandSender sender, String[] args, int p_175757_2_, boolean p_175757_3_) throws NumberInvalidException
    {
        BlockPos var4 = sender.getPosition();
        return new BlockPos(func_175769_b((double)var4.getX(), args[p_175757_2_], -30000000, 30000000, p_175757_3_), func_175769_b((double)var4.getY(), args[p_175757_2_ + 1], 0, 256, false), func_175769_b((double)var4.getZ(), args[p_175757_2_ + 2], -30000000, 30000000, p_175757_3_));
    }

    public static double parseDouble(String input) throws NumberInvalidException
    {
        try
        {
            double var1 = Double.parseDouble(input);

            if (!Doubles.isFinite(var1))
            {
                throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
            }
            else
            {
                return var1;
            }
        }
        catch (NumberFormatException var3)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
        }
    }

    public static double parseDouble(String input, double min) throws NumberInvalidException
    {
        return parseDouble(input, min, Double.MAX_VALUE);
    }

    public static double parseDouble(String input, double min, double max) throws NumberInvalidException
    {
        double var5 = parseDouble(input);

        if (var5 < min)
        {
            throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(var5), Double.valueOf(min)});
        }
        else if (var5 > max)
        {
            throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(var5), Double.valueOf(max)});
        }
        else
        {
            return var5;
        }
    }

    public static boolean parseBoolean(String input) throws CommandException
    {
        if (!input.equals("true") && !input.equals("1"))
        {
            if (!input.equals("false") && !input.equals("0"))
            {
                throw new CommandException("commands.generic.boolean.invalid", new Object[] {input});
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender sender) throws PlayerNotFoundException
    {
        if (sender instanceof EntityPlayerMP)
        {
            return (EntityPlayerMP)sender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }

    public static EntityPlayerMP getPlayer(ICommandSender sender, String username) throws PlayerNotFoundException
    {
        EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(sender, username);

        if (var2 == null)
        {
            try
            {
                var2 = MinecraftServer.getServer().getConfigurationManager().func_177451_a(UUID.fromString(username));
            }
            catch (IllegalArgumentException var4)
            {
                ;
            }
        }

        if (var2 == null)
        {
            var2 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
        }

        if (var2 == null)
        {
            throw new PlayerNotFoundException();
        }
        else
        {
            return var2;
        }
    }

    public static Entity func_175768_b(ICommandSender p_175768_0_, String p_175768_1_) throws EntityNotFoundException
    {
        return func_175759_a(p_175768_0_, p_175768_1_, Entity.class);
    }

    public static Entity func_175759_a(ICommandSender p_175759_0_, String p_175759_1_, Class p_175759_2_) throws EntityNotFoundException
    {
        Object var3 = PlayerSelector.func_179652_a(p_175759_0_, p_175759_1_, p_175759_2_);
        MinecraftServer var4 = MinecraftServer.getServer();

        if (var3 == null)
        {
            var3 = var4.getConfigurationManager().getPlayerByUsername(p_175759_1_);
        }

        if (var3 == null)
        {
            try
            {
                UUID var5 = UUID.fromString(p_175759_1_);
                var3 = var4.getEntityFromUuid(var5);

                if (var3 == null)
                {
                    var3 = var4.getConfigurationManager().func_177451_a(var5);
                }
            }
            catch (IllegalArgumentException var6)
            {
                throw new EntityNotFoundException("commands.generic.entity.invalidUuid", new Object[0]);
            }
        }

        if (var3 != null && p_175759_2_.isAssignableFrom(var3.getClass()))
        {
            return (Entity)var3;
        }
        else
        {
            throw new EntityNotFoundException();
        }
    }

    public static List func_175763_c(ICommandSender p_175763_0_, String p_175763_1_) throws EntityNotFoundException
    {
        return (List)(PlayerSelector.hasArguments(p_175763_1_) ? PlayerSelector.func_179656_b(p_175763_0_, p_175763_1_, Entity.class) : Lists.newArrayList(new Entity[] {func_175768_b(p_175763_0_, p_175763_1_)}));
    }

    public static String getPlayerName(ICommandSender sender, String query) throws PlayerNotFoundException
    {
        try
        {
            return getPlayer(sender, query).getName();
        }
        catch (PlayerNotFoundException var3)
        {
            if (PlayerSelector.hasArguments(query))
            {
                throw var3;
            }
            else
            {
                return query;
            }
        }
    }

    public static String func_175758_e(ICommandSender p_175758_0_, String p_175758_1_) throws EntityNotFoundException
    {
        try
        {
            return getPlayer(p_175758_0_, p_175758_1_).getName();
        }
        catch (PlayerNotFoundException var5)
        {
            try
            {
                return func_175768_b(p_175758_0_, p_175758_1_).getUniqueID().toString();
            }
            catch (EntityNotFoundException var4)
            {
                if (PlayerSelector.hasArguments(p_175758_1_))
                {
                    throw var4;
                }
                else
                {
                    return p_175758_1_;
                }
            }
        }
    }

    public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int p_147178_2_) throws CommandException
    {
        return getChatComponentFromNthArg(sender, args, p_147178_2_, false);
    }

    public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int index, boolean p_147176_3_) throws PlayerNotFoundException
    {
        ChatComponentText var4 = new ChatComponentText("");

        for (int var5 = index; var5 < args.length; ++var5)
        {
            if (var5 > index)
            {
                var4.appendText(" ");
            }

            Object var6 = new ChatComponentText(args[var5]);

            if (p_147176_3_)
            {
                IChatComponent var7 = PlayerSelector.func_150869_b(sender, args[var5]);

                if (var7 == null)
                {
                    if (PlayerSelector.hasArguments(args[var5]))
                    {
                        throw new PlayerNotFoundException();
                    }
                }
                else
                {
                    var6 = var7;
                }
            }

            var4.appendSibling((IChatComponent)var6);
        }

        return var4;
    }

    public static String func_180529_a(String[] p_180529_0_, int p_180529_1_)
    {
        StringBuilder var2 = new StringBuilder();

        for (int var3 = p_180529_1_; var3 < p_180529_0_.length; ++var3)
        {
            if (var3 > p_180529_1_)
            {
                var2.append(" ");
            }

            String var4 = p_180529_0_[var3];
            var2.append(var4);
        }

        return var2.toString();
    }

    public static CommandBase.CoordinateArg func_175770_a(double p_175770_0_, String p_175770_2_, boolean p_175770_3_) throws NumberInvalidException
    {
        return func_175767_a(p_175770_0_, p_175770_2_, -30000000, 30000000, p_175770_3_);
    }

    public static CommandBase.CoordinateArg func_175767_a(double p_175767_0_, String p_175767_2_, int p_175767_3_, int p_175767_4_, boolean p_175767_5_) throws NumberInvalidException
    {
        boolean var6 = p_175767_2_.startsWith("~");

        if (var6 && Double.isNaN(p_175767_0_))
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {Double.valueOf(p_175767_0_)});
        }
        else
        {
            double var7 = 0.0D;

            if (!var6 || p_175767_2_.length() > 1)
            {
                boolean var9 = p_175767_2_.contains(".");

                if (var6)
                {
                    p_175767_2_ = p_175767_2_.substring(1);
                }

                var7 += parseDouble(p_175767_2_);

                if (!var9 && !var6 && p_175767_5_)
                {
                    var7 += 0.5D;
                }
            }

            if (p_175767_3_ != 0 || p_175767_4_ != 0)
            {
                if (var7 < (double)p_175767_3_)
                {
                    throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(var7), Integer.valueOf(p_175767_3_)});
                }

                if (var7 > (double)p_175767_4_)
                {
                    throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(var7), Integer.valueOf(p_175767_4_)});
                }
            }

            return new CommandBase.CoordinateArg(var7 + (var6 ? p_175767_0_ : 0.0D), var7, var6);
        }
    }

    public static double func_175761_b(double p_175761_0_, String p_175761_2_, boolean p_175761_3_) throws NumberInvalidException
    {
        return func_175769_b(p_175761_0_, p_175761_2_, -30000000, 30000000, p_175761_3_);
    }

    public static double func_175769_b(double base, String input, int min, int max, boolean centerBlock) throws NumberInvalidException
    {
        boolean var6 = input.startsWith("~");

        if (var6 && Double.isNaN(base))
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {Double.valueOf(base)});
        }
        else
        {
            double var7 = var6 ? base : 0.0D;

            if (!var6 || input.length() > 1)
            {
                boolean var9 = input.contains(".");

                if (var6)
                {
                    input = input.substring(1);
                }

                var7 += parseDouble(input);

                if (!var9 && !var6 && centerBlock)
                {
                    var7 += 0.5D;
                }
            }

            if (min != 0 || max != 0)
            {
                if (var7 < (double)min)
                {
                    throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(var7), Integer.valueOf(min)});
                }

                if (var7 > (double)max)
                {
                    throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(var7), Integer.valueOf(max)});
                }
            }

            return var7;
        }
    }

    /**
     * Gets the Item specified by the given text string.  First checks the item registry, then tries by parsing the
     * string as an integer ID (deprecated).  Warns the sender if we matched by parsing the ID.  Throws if the item
     * wasn't found.  Returns the item if it was found.
     */
    public static Item getItemByText(ICommandSender sender, String id) throws NumberInvalidException
    {
        ResourceLocation var2 = new ResourceLocation(id);
        Item var3 = (Item)Item.itemRegistry.getObject(var2);

        if (var3 == null)
        {
            throw new NumberInvalidException("commands.give.notFound", new Object[] {var2});
        }
        else
        {
            return var3;
        }
    }

    /**
     * Gets the Block specified by the given text string.  First checks the block registry, then tries by parsing the
     * string as an integer ID (deprecated).  Warns the sender if we matched by parsing the ID.  Throws if the block
     * wasn't found.  Returns the block if it was found.
     */
    public static Block getBlockByText(ICommandSender sender, String id) throws NumberInvalidException
    {
        ResourceLocation var2 = new ResourceLocation(id);

        if (!Block.blockRegistry.containsKey(var2))
        {
            throw new NumberInvalidException("commands.give.notFound", new Object[] {var2});
        }
        else
        {
            Block var3 = (Block)Block.blockRegistry.getObject(var2);

            if (var3 == null)
            {
                throw new NumberInvalidException("commands.give.notFound", new Object[] {var2});
            }
            else
            {
                return var3;
            }
        }
    }

    /**
     * Creates a linguistic series joining the input objects together.  Examples: 1) {} --> "",  2) {"Steve"} -->
     * "Steve",  3) {"Steve", "Phil"} --> "Steve and Phil",  4) {"Steve", "Phil", "Mark"} --> "Steve, Phil and Mark"
     */
    public static String joinNiceString(Object[] elements)
    {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = 0; var2 < elements.length; ++var2)
        {
            String var3 = elements[var2].toString();

            if (var2 > 0)
            {
                if (var2 == elements.length - 1)
                {
                    var1.append(" and ");
                }
                else
                {
                    var1.append(", ");
                }
            }

            var1.append(var3);
        }

        return var1.toString();
    }

    public static IChatComponent join(List components)
    {
        ChatComponentText var1 = new ChatComponentText("");

        for (int var2 = 0; var2 < components.size(); ++var2)
        {
            if (var2 > 0)
            {
                if (var2 == components.size() - 1)
                {
                    var1.appendText(" and ");
                }
                else if (var2 > 0)
                {
                    var1.appendText(", ");
                }
            }

            var1.appendSibling((IChatComponent)components.get(var2));
        }

        return var1;
    }

    /**
     * Creates a linguistic series joining together the elements of the given collection.  Examples: 1) {} --> "",  2)
     * {"Steve"} --> "Steve",  3) {"Steve", "Phil"} --> "Steve and Phil",  4) {"Steve", "Phil", "Mark"} --> "Steve, Phil
     * and Mark"
     */
    public static String joinNiceStringFromCollection(Collection strings)
    {
        return joinNiceString(strings.toArray(new String[strings.size()]));
    }

    public static List func_175771_a(String[] p_175771_0_, int p_175771_1_, BlockPos p_175771_2_)
    {
        if (p_175771_2_ == null)
        {
            return null;
        }
        else
        {
            String var3;

            if (p_175771_0_.length - 1 == p_175771_1_)
            {
                var3 = Integer.toString(p_175771_2_.getX());
            }
            else if (p_175771_0_.length - 1 == p_175771_1_ + 1)
            {
                var3 = Integer.toString(p_175771_2_.getY());
            }
            else
            {
                if (p_175771_0_.length - 1 != p_175771_1_ + 2)
                {
                    return null;
                }

                var3 = Integer.toString(p_175771_2_.getZ());
            }

            return Lists.newArrayList(new String[] {var3});
        }
    }

    /**
     * Returns true if the given substring is exactly equal to the start of the given string (case insensitive).
     */
    public static boolean doesStringStartWith(String original, String region)
    {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    /**
     * Returns a List of strings (chosen from the given strings) which the last word in the given string array is a
     * beginning-match for. (Tab completion).
     */
    public static List getListOfStringsMatchingLastWord(String[] args, String ... possibilities)
    {
        return func_175762_a(args, Arrays.asList(possibilities));
    }

    public static List func_175762_a(String[] p_175762_0_, Collection p_175762_1_)
    {
        String var2 = p_175762_0_[p_175762_0_.length - 1];
        ArrayList var3 = Lists.newArrayList();

        if (!p_175762_1_.isEmpty())
        {
            Iterator var4 = Iterables.transform(p_175762_1_, Functions.toStringFunction()).iterator();

            while (var4.hasNext())
            {
                String var5 = (String)var4.next();

                if (doesStringStartWith(var2, var5))
                {
                    var3.add(var5);
                }
            }

            if (var3.isEmpty())
            {
                var4 = p_175762_1_.iterator();

                while (var4.hasNext())
                {
                    Object var6 = var4.next();

                    if (var6 instanceof ResourceLocation && doesStringStartWith(var2, ((ResourceLocation)var6).getResourcePath()))
                    {
                        var3.add(String.valueOf(var6));
                    }
                }
            }
        }

        return var3;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

    public static void notifyOperators(ICommandSender sender, ICommand command, String msgFormat, Object ... msgParams)
    {
        notifyOperators(sender, command, 0, msgFormat, msgParams);
    }

    public static void notifyOperators(ICommandSender sender, ICommand command, int p_152374_2_, String msgFormat, Object ... msgParams)
    {
        if (theAdmin != null)
        {
            theAdmin.notifyOperators(sender, command, p_152374_2_, msgFormat, msgParams);
        }
    }

    /**
     * Sets the static IAdminCommander.
     */
    public static void setAdminCommander(IAdminCommand command)
    {
        theAdmin = command;
    }

    public int compareTo(ICommand p_compareTo_1_)
    {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((ICommand)p_compareTo_1_);
    }

    public static class CoordinateArg
    {
        private final double field_179633_a;
        private final double field_179631_b;
        private final boolean field_179632_c;
        private static final String __OBFID = "CL_00002365";

        protected CoordinateArg(double p_i46051_1_, double p_i46051_3_, boolean p_i46051_5_)
        {
            this.field_179633_a = p_i46051_1_;
            this.field_179631_b = p_i46051_3_;
            this.field_179632_c = p_i46051_5_;
        }

        public double func_179628_a()
        {
            return this.field_179633_a;
        }

        public double func_179629_b()
        {
            return this.field_179631_b;
        }

        public boolean func_179630_c()
        {
            return this.field_179632_c;
        }
    }
}
