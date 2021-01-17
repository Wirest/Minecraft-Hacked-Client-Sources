// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Iterator;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.base.Functions;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.primitives.Doubles;
import net.minecraft.util.BlockPos;
import java.util.Collections;
import java.util.List;

public abstract class CommandBase implements ICommand
{
    private static IAdminCommand theAdmin;
    
    public int getRequiredPermissionLevel() {
        return 4;
    }
    
    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return null;
    }
    
    public static int parseInt(final String input) throws NumberInvalidException {
        try {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException var2) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
        }
    }
    
    public static int parseInt(final String input, final int min) throws NumberInvalidException {
        return parseInt(input, min, Integer.MAX_VALUE);
    }
    
    public static int parseInt(final String input, final int min, final int max) throws NumberInvalidException {
        final int i = parseInt(input);
        if (i < min) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { i, min });
        }
        if (i > max) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { i, max });
        }
        return i;
    }
    
    public static long parseLong(final String input) throws NumberInvalidException {
        try {
            return Long.parseLong(input);
        }
        catch (NumberFormatException var2) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
        }
    }
    
    public static long parseLong(final String input, final long min, final long max) throws NumberInvalidException {
        final long i = parseLong(input);
        if (i < min) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { i, min });
        }
        if (i > max) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { i, max });
        }
        return i;
    }
    
    public static BlockPos parseBlockPos(final ICommandSender sender, final String[] args, final int startIndex, final boolean centerBlock) throws NumberInvalidException {
        final BlockPos blockpos = sender.getPosition();
        return new BlockPos(parseDouble(blockpos.getX(), args[startIndex], -30000000, 30000000, centerBlock), parseDouble(blockpos.getY(), args[startIndex + 1], 0, 256, false), parseDouble(blockpos.getZ(), args[startIndex + 2], -30000000, 30000000, centerBlock));
    }
    
    public static double parseDouble(final String input) throws NumberInvalidException {
        try {
            final double d0 = Double.parseDouble(input);
            if (!Doubles.isFinite(d0)) {
                throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
            }
            return d0;
        }
        catch (NumberFormatException var3) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
        }
    }
    
    public static double parseDouble(final String input, final double min) throws NumberInvalidException {
        return parseDouble(input, min, Double.MAX_VALUE);
    }
    
    public static double parseDouble(final String input, final double min, final double max) throws NumberInvalidException {
        final double d0 = parseDouble(input);
        if (d0 < min) {
            throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { d0, min });
        }
        if (d0 > max) {
            throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { d0, max });
        }
        return d0;
    }
    
    public static boolean parseBoolean(final String input) throws CommandException {
        if (input.equals("true") || input.equals("1")) {
            return true;
        }
        if (!input.equals("false") && !input.equals("0")) {
            throw new CommandException("commands.generic.boolean.invalid", new Object[] { input });
        }
        return false;
    }
    
    public static EntityPlayerMP getCommandSenderAsPlayer(final ICommandSender sender) throws PlayerNotFoundException {
        if (sender instanceof EntityPlayerMP) {
            return (EntityPlayerMP)sender;
        }
        throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
    }
    
    public static EntityPlayerMP getPlayer(final ICommandSender sender, final String username) throws PlayerNotFoundException {
        EntityPlayerMP entityplayermp = PlayerSelector.matchOnePlayer(sender, username);
        if (entityplayermp == null) {
            try {
                entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(UUID.fromString(username));
            }
            catch (IllegalArgumentException ex) {}
        }
        if (entityplayermp == null) {
            entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
        }
        if (entityplayermp == null) {
            throw new PlayerNotFoundException();
        }
        return entityplayermp;
    }
    
    public static Entity func_175768_b(final ICommandSender p_175768_0_, final String p_175768_1_) throws EntityNotFoundException {
        return getEntity(p_175768_0_, p_175768_1_, (Class<? extends Entity>)Entity.class);
    }
    
    public static <T extends Entity> T getEntity(final ICommandSender commandSender, final String p_175759_1_, final Class<? extends T> p_175759_2_) throws EntityNotFoundException {
        Entity entity = PlayerSelector.matchOneEntity(commandSender, p_175759_1_, (Class<? extends Entity>)p_175759_2_);
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        if (entity == null) {
            entity = minecraftserver.getConfigurationManager().getPlayerByUsername(p_175759_1_);
        }
        if (entity == null) {
            try {
                final UUID uuid = UUID.fromString(p_175759_1_);
                entity = minecraftserver.getEntityFromUuid(uuid);
                if (entity == null) {
                    entity = minecraftserver.getConfigurationManager().getPlayerByUUID(uuid);
                }
            }
            catch (IllegalArgumentException var6) {
                throw new EntityNotFoundException("commands.generic.entity.invalidUuid", new Object[0]);
            }
        }
        if (entity != null && p_175759_2_.isAssignableFrom(entity.getClass())) {
            return (T)entity;
        }
        throw new EntityNotFoundException();
    }
    
    public static List<Entity> func_175763_c(final ICommandSender p_175763_0_, final String p_175763_1_) throws EntityNotFoundException {
        return PlayerSelector.hasArguments(p_175763_1_) ? PlayerSelector.matchEntities(p_175763_0_, p_175763_1_, (Class<? extends Entity>)Entity.class) : Lists.newArrayList(func_175768_b(p_175763_0_, p_175763_1_));
    }
    
    public static String getPlayerName(final ICommandSender sender, final String query) throws PlayerNotFoundException {
        try {
            return getPlayer(sender, query).getName();
        }
        catch (PlayerNotFoundException playernotfoundexception) {
            if (PlayerSelector.hasArguments(query)) {
                throw playernotfoundexception;
            }
            return query;
        }
    }
    
    public static String getEntityName(final ICommandSender p_175758_0_, final String p_175758_1_) throws EntityNotFoundException {
        try {
            return getPlayer(p_175758_0_, p_175758_1_).getName();
        }
        catch (PlayerNotFoundException var5) {
            try {
                return func_175768_b(p_175758_0_, p_175758_1_).getUniqueID().toString();
            }
            catch (EntityNotFoundException entitynotfoundexception) {
                if (PlayerSelector.hasArguments(p_175758_1_)) {
                    throw entitynotfoundexception;
                }
                return p_175758_1_;
            }
        }
    }
    
    public static IChatComponent getChatComponentFromNthArg(final ICommandSender sender, final String[] args, final int p_147178_2_) throws CommandException, PlayerNotFoundException {
        return getChatComponentFromNthArg(sender, args, p_147178_2_, false);
    }
    
    public static IChatComponent getChatComponentFromNthArg(final ICommandSender sender, final String[] args, final int index, final boolean p_147176_3_) throws PlayerNotFoundException {
        final IChatComponent ichatcomponent = new ChatComponentText("");
        for (int i = index; i < args.length; ++i) {
            if (i > index) {
                ichatcomponent.appendText(" ");
            }
            IChatComponent ichatcomponent2 = new ChatComponentText(args[i]);
            if (p_147176_3_) {
                final IChatComponent ichatcomponent3 = PlayerSelector.matchEntitiesToChatComponent(sender, args[i]);
                if (ichatcomponent3 == null) {
                    if (PlayerSelector.hasArguments(args[i])) {
                        throw new PlayerNotFoundException();
                    }
                }
                else {
                    ichatcomponent2 = ichatcomponent3;
                }
            }
            ichatcomponent.appendSibling(ichatcomponent2);
        }
        return ichatcomponent;
    }
    
    public static String buildString(final String[] args, final int startPos) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (int i = startPos; i < args.length; ++i) {
            if (i > startPos) {
                stringbuilder.append(" ");
            }
            final String s = args[i];
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    
    public static CoordinateArg parseCoordinate(final double base, final String p_175770_2_, final boolean centerBlock) throws NumberInvalidException {
        return parseCoordinate(base, p_175770_2_, -30000000, 30000000, centerBlock);
    }
    
    public static CoordinateArg parseCoordinate(final double p_175767_0_, String p_175767_2_, final int min, final int max, final boolean centerBlock) throws NumberInvalidException {
        final boolean flag = p_175767_2_.startsWith("~");
        if (flag && Double.isNaN(p_175767_0_)) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { p_175767_0_ });
        }
        double d0 = 0.0;
        if (!flag || p_175767_2_.length() > 1) {
            final boolean flag2 = p_175767_2_.contains(".");
            if (flag) {
                p_175767_2_ = p_175767_2_.substring(1);
            }
            d0 += parseDouble(p_175767_2_);
            if (!flag2 && !flag && centerBlock) {
                d0 += 0.5;
            }
        }
        if (min != 0 || max != 0) {
            if (d0 < min) {
                throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { d0, min });
            }
            if (d0 > max) {
                throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { d0, max });
            }
        }
        return new CoordinateArg(d0 + (flag ? p_175767_0_ : 0.0), d0, flag);
    }
    
    public static double parseDouble(final double base, final String input, final boolean centerBlock) throws NumberInvalidException {
        return parseDouble(base, input, -30000000, 30000000, centerBlock);
    }
    
    public static double parseDouble(final double base, String input, final int min, final int max, final boolean centerBlock) throws NumberInvalidException {
        final boolean flag = input.startsWith("~");
        if (flag && Double.isNaN(base)) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { base });
        }
        double d0 = flag ? base : 0.0;
        if (!flag || input.length() > 1) {
            final boolean flag2 = input.contains(".");
            if (flag) {
                input = input.substring(1);
            }
            d0 += parseDouble(input);
            if (!flag2 && !flag && centerBlock) {
                d0 += 0.5;
            }
        }
        if (min != 0 || max != 0) {
            if (d0 < min) {
                throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] { d0, min });
            }
            if (d0 > max) {
                throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] { d0, max });
            }
        }
        return d0;
    }
    
    public static Item getItemByText(final ICommandSender sender, final String id) throws NumberInvalidException {
        final ResourceLocation resourcelocation = new ResourceLocation(id);
        final Item item = Item.itemRegistry.getObject(resourcelocation);
        if (item == null) {
            throw new NumberInvalidException("commands.give.item.notFound", new Object[] { resourcelocation });
        }
        return item;
    }
    
    public static Block getBlockByText(final ICommandSender sender, final String id) throws NumberInvalidException {
        final ResourceLocation resourcelocation = new ResourceLocation(id);
        if (!Block.blockRegistry.containsKey(resourcelocation)) {
            throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
        }
        final Block block = Block.blockRegistry.getObject(resourcelocation);
        if (block == null) {
            throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
        }
        return block;
    }
    
    public static String joinNiceString(final Object[] elements) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (int i = 0; i < elements.length; ++i) {
            final String s = elements[i].toString();
            if (i > 0) {
                if (i == elements.length - 1) {
                    stringbuilder.append(" and ");
                }
                else {
                    stringbuilder.append(", ");
                }
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    
    public static IChatComponent join(final List<IChatComponent> components) {
        final IChatComponent ichatcomponent = new ChatComponentText("");
        for (int i = 0; i < components.size(); ++i) {
            if (i > 0) {
                if (i == components.size() - 1) {
                    ichatcomponent.appendText(" and ");
                }
                else if (i > 0) {
                    ichatcomponent.appendText(", ");
                }
            }
            ichatcomponent.appendSibling(components.get(i));
        }
        return ichatcomponent;
    }
    
    public static String joinNiceStringFromCollection(final Collection<String> strings) {
        return joinNiceString(strings.toArray(new String[strings.size()]));
    }
    
    public static List<String> func_175771_a(final String[] p_175771_0_, final int p_175771_1_, final BlockPos p_175771_2_) {
        if (p_175771_2_ == null) {
            return null;
        }
        final int i = p_175771_0_.length - 1;
        String s;
        if (i == p_175771_1_) {
            s = Integer.toString(p_175771_2_.getX());
        }
        else if (i == p_175771_1_ + 1) {
            s = Integer.toString(p_175771_2_.getY());
        }
        else {
            if (i != p_175771_1_ + 2) {
                return null;
            }
            s = Integer.toString(p_175771_2_.getZ());
        }
        return Lists.newArrayList(s);
    }
    
    public static List<String> func_181043_b(final String[] p_181043_0_, final int p_181043_1_, final BlockPos p_181043_2_) {
        if (p_181043_2_ == null) {
            return null;
        }
        final int i = p_181043_0_.length - 1;
        String s;
        if (i == p_181043_1_) {
            s = Integer.toString(p_181043_2_.getX());
        }
        else {
            if (i != p_181043_1_ + 1) {
                return null;
            }
            s = Integer.toString(p_181043_2_.getZ());
        }
        return Lists.newArrayList(s);
    }
    
    public static boolean doesStringStartWith(final String original, final String region) {
        return region.regionMatches(true, 0, original, 0, original.length());
    }
    
    public static List<String> getListOfStringsMatchingLastWord(final String[] args, final String... possibilities) {
        return getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
    }
    
    public static List<String> getListOfStringsMatchingLastWord(final String[] p_175762_0_, final Collection<?> p_175762_1_) {
        final String s = p_175762_0_[p_175762_0_.length - 1];
        final List<String> list = (List<String>)Lists.newArrayList();
        if (!p_175762_1_.isEmpty()) {
            for (final String s2 : Iterables.transform((Iterable<?>)p_175762_1_, (Function<?, ? extends String>)Functions.toStringFunction())) {
                if (doesStringStartWith(s, s2)) {
                    list.add(s2);
                }
            }
            if (list.isEmpty()) {
                for (final Object object : p_175762_1_) {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation)object).getResourcePath())) {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }
        return list;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return false;
    }
    
    public static void notifyOperators(final ICommandSender sender, final ICommand command, final String msgFormat, final Object... msgParams) {
        notifyOperators(sender, command, 0, msgFormat, msgParams);
    }
    
    public static void notifyOperators(final ICommandSender sender, final ICommand command, final int p_152374_2_, final String msgFormat, final Object... msgParams) {
        if (CommandBase.theAdmin != null) {
            CommandBase.theAdmin.notifyOperators(sender, command, p_152374_2_, msgFormat, msgParams);
        }
    }
    
    public static void setAdminCommander(final IAdminCommand command) {
        CommandBase.theAdmin = command;
    }
    
    @Override
    public int compareTo(final ICommand p_compareTo_1_) {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }
    
    public static class CoordinateArg
    {
        private final double field_179633_a;
        private final double field_179631_b;
        private final boolean field_179632_c;
        
        protected CoordinateArg(final double p_i46051_1_, final double p_i46051_3_, final boolean p_i46051_5_) {
            this.field_179633_a = p_i46051_1_;
            this.field_179631_b = p_i46051_3_;
            this.field_179632_c = p_i46051_5_;
        }
        
        public double func_179628_a() {
            return this.field_179633_a;
        }
        
        public double func_179629_b() {
            return this.field_179631_b;
        }
        
        public boolean func_179630_c() {
            return this.field_179632_c;
        }
    }
}
