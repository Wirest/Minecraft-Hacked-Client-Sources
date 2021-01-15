package net.minecraft.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerSelector
{
    /**
     * This matches the at-tokens introduced for command blocks, including their arguments, if any.
     */
    private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");

    /**
     * This matches things like "-1,,4", and is used for getting x,y,z,range from the token's argument list.
     */
    private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");

    /**
     * This matches things like "rm=4,c=2" and is used for handling named token arguments.
     */
    private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");

    /** A set of all valid arguments */
    private static final Set VALID_ARGUMENTS = Sets.newHashSet(new String[] {"x", "y", "z", "dx", "dy", "dz", "rm", "r"});

    /**
     * Returns the one player that matches the given at-token.  Returns null if more than one player matches.
     *  
     * @param sender The command sender to match the token for
     * @param token The selector token to match
     */
    public static EntityPlayerMP matchOnePlayer(ICommandSender sender, String token)
    {
        return (EntityPlayerMP)matchOneEntity(sender, token, EntityPlayerMP.class);
    }

    /**
     * Returns one entity of the given class that matches the given at-token. Returns null if more than one entity
     * matches.
     *  
     * @param sender The command sender to match the token for
     * @param token The selector token to match
     * @param targetClass The type of entity we are matching
     */
    public static Entity matchOneEntity(ICommandSender sender, String token, Class targetClass)
    {
        List var3 = matchEntities(sender, token, targetClass);
        return var3.size() == 1 ? (Entity)var3.get(0) : null;
    }

    public static IChatComponent matchEntitiesToChatComponent(ICommandSender sender, String token)
    {
        List var2 = matchEntities(sender, token, Entity.class);

        if (var2.isEmpty())
        {
            return null;
        }
        else
        {
            ArrayList var3 = Lists.newArrayList();
            Iterator var4 = var2.iterator();

            while (var4.hasNext())
            {
                Entity var5 = (Entity)var4.next();
                var3.add(var5.getDisplayName());
            }

            return CommandBase.join(var3);
        }
    }

    /**
     * Returns all entities of the given class that matches the given at-token in a list.
     *  
     * @param sender The command sender that executed the command
     * @param token The selector token to match
     * @param targetClass The type of entity we are matching
     */
    public static List matchEntities(ICommandSender sender, String token, Class targetClass)
    {
        Matcher var3 = tokenPattern.matcher(token);

        if (var3.matches() && sender.canCommandSenderUseCommand(1, "@"))
        {
            Map var4 = getArgumentMap(var3.group(2));

            if (!isEntityTypeValid(sender, var4))
            {
                return Collections.emptyList();
            }
            else
            {
                String var5 = var3.group(1);
                BlockPos var6 = func_179664_b(var4, sender.getPosition());
                List var7 = getWorlds(sender, var4);
                ArrayList var8 = Lists.newArrayList();
                Iterator var9 = var7.iterator();

                while (var9.hasNext())
                {
                    World var10 = (World)var9.next();

                    if (var10 != null)
                    {
                        ArrayList var11 = Lists.newArrayList();
                        var11.addAll(func_179663_a(var4, var5));
                        var11.addAll(func_179648_b(var4));
                        var11.addAll(func_179649_c(var4));
                        var11.addAll(func_179659_d(var4));
                        var11.addAll(func_179657_e(var4));
                        var11.addAll(func_179647_f(var4));
                        var11.addAll(func_180698_a(var4, var6));
                        var11.addAll(func_179662_g(var4));
                        var8.addAll(filterResults(var4, targetClass, var11, var5, var10, var6));
                    }
                }

                return func_179658_a(var8, var4, sender, targetClass, var5, var6);
            }
        }
        else
        {
            return Collections.emptyList();
        }
    }

    /**
     * Returns the worlds to match the entities in for the specified command sender and token. This returns the sender's
     * world if the selector specifies a location or all currently loaded worlds on the server if not.
     *  
     * @param sender The command sender to match the token for
     * @param argumentMap The parsed argument map of the token
     */
    private static List getWorlds(ICommandSender sender, Map argumentMap)
    {
        ArrayList var2 = Lists.newArrayList();

        if (func_179665_h(argumentMap))
        {
            var2.add(sender.getEntityWorld());
        }
        else
        {
            Collections.addAll(var2, MinecraftServer.getServer().worldServers);
        }

        return var2;
    }

    /**
     * Checks to make sure that the specified type is valid
     *  
     * @param commandSender The command sender
     * @param params The paramaters of the selector
     */
    private static boolean isEntityTypeValid(ICommandSender commandSender, Map params)
    {
        String var2 = func_179651_b(params, "type");
        var2 = var2 != null && var2.startsWith("!") ? var2.substring(1) : var2;

        if (var2 != null && !EntityList.isStringValidEntityName(var2))
        {
            ChatComponentTranslation var3 = new ChatComponentTranslation("commands.generic.entity.invalidType", new Object[] {var2});
            var3.getChatStyle().setColor(EnumChatFormatting.RED);
            commandSender.addChatMessage(var3);
            return false;
        }
        else
        {
            return true;
        }
    }

    private static List func_179663_a(Map p_179663_0_, String p_179663_1_)
    {
        ArrayList var2 = Lists.newArrayList();
        String var3 = func_179651_b(p_179663_0_, "type");
        final boolean var4 = var3 != null && var3.startsWith("!");

        if (var4)
        {
            var3 = var3.substring(1);
        }

        boolean var6 = !p_179663_1_.equals("e");
        boolean var7 = p_179663_1_.equals("r") && var3 != null;

        if ((var3 == null || !p_179663_1_.equals("e")) && !var7)
        {
            if (var6)
            {
                var2.add(new Predicate()
                {
                    public boolean func_179624_a(Entity p_179624_1_)
                    {
                        return p_179624_1_ instanceof EntityPlayer;
                    }
                    @Override
					public boolean apply(Object p_apply_1_)
                    {
                        return this.func_179624_a((Entity)p_apply_1_);
                    }
                });
            }
        }
        else
        {
            final String var3_f = var3;
            var2.add(new Predicate()
            {
                public boolean func_179613_a(Entity p_179613_1_)
                {
                    return EntityList.isStringEntityName(p_179613_1_, var3_f) != var4;
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179613_a((Entity)p_apply_1_);
                }
            });
        }

        return var2;
    }

    private static List func_179648_b(Map p_179648_0_)
    {
        ArrayList var1 = Lists.newArrayList();
        final int var2 = func_179653_a(p_179648_0_, "lm", -1);
        final int var3 = func_179653_a(p_179648_0_, "l", -1);

        if (var2 > -1 || var3 > -1)
        {
            var1.add(new Predicate()
            {
                public boolean func_179625_a(Entity p_179625_1_)
                {
                    if (!(p_179625_1_ instanceof EntityPlayerMP))
                    {
                        return false;
                    }
                    else
                    {
                        EntityPlayerMP var2x = (EntityPlayerMP)p_179625_1_;
                        return (var2 <= -1 || var2x.experienceLevel >= var2) && (var3 <= -1 || var2x.experienceLevel <= var3);
                    }
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179625_a((Entity)p_apply_1_);
                }
            });
        }

        return var1;
    }

    private static List func_179649_c(Map p_179649_0_)
    {
        ArrayList var1 = Lists.newArrayList();
        final int var2 = func_179653_a(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());

        if (var2 != WorldSettings.GameType.NOT_SET.getID())
        {
            var1.add(new Predicate()
            {
                public boolean func_179619_a(Entity p_179619_1_)
                {
                    if (!(p_179619_1_ instanceof EntityPlayerMP))
                    {
                        return false;
                    }
                    else
                    {
                        EntityPlayerMP var2x = (EntityPlayerMP)p_179619_1_;
                        return var2x.theItemInWorldManager.getGameType().getID() == var2;
                    }
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179619_a((Entity)p_apply_1_);
                }
            });
        }

        return var1;
    }

    private static List func_179659_d(Map p_179659_0_)
    {
        ArrayList var1 = Lists.newArrayList();
        String var2 = func_179651_b(p_179659_0_, "team");
        final boolean var3 = var2 != null && var2.startsWith("!");

        if (var3)
        {
            var2 = var2.substring(1);
        }

        if (var2 != null)
        {
            final String var2_f = var2;
            var1.add(new Predicate()
            {
                public boolean func_179621_a(Entity p_179621_1_)
                {
                    if (!(p_179621_1_ instanceof EntityLivingBase))
                    {
                        return false;
                    }
                    else
                    {
                        EntityLivingBase var2x = (EntityLivingBase)p_179621_1_;
                        Team var3x = var2x.getTeam();
                        String var4 = var3x == null ? "" : var3x.getRegisteredName();
                        return var4.equals(var2_f) != var3;
                    }
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179621_a((Entity)p_apply_1_);
                }
            });
        }

        return var1;
    }

    private static List func_179657_e(Map p_179657_0_)
    {
        ArrayList var1 = Lists.newArrayList();
        final Map var2 = func_96560_a(p_179657_0_);

        if (var2 != null && var2.size() > 0)
        {
            var1.add(new Predicate()
            {
                public boolean func_179603_a(Entity p_179603_1_)
                {
                    Scoreboard var2x = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
                    Iterator var3 = var2.entrySet().iterator();
                    Entry var4;
                    boolean var6;
                    int var10;

                    do
                    {
                        if (!var3.hasNext())
                        {
                            return true;
                        }

                        var4 = (Entry)var3.next();
                        String var5 = (String)var4.getKey();
                        var6 = false;

                        if (var5.endsWith("_min") && var5.length() > 4)
                        {
                            var6 = true;
                            var5 = var5.substring(0, var5.length() - 4);
                        }

                        ScoreObjective var7 = var2x.getObjective(var5);

                        if (var7 == null)
                        {
                            return false;
                        }

                        String var8 = p_179603_1_ instanceof EntityPlayerMP ? p_179603_1_.getCommandSenderName() : p_179603_1_.getUniqueID().toString();

                        if (!var2x.entityHasObjective(var8, var7))
                        {
                            return false;
                        }

                        Score var9 = var2x.getValueFromObjective(var8, var7);
                        var10 = var9.getScorePoints();

                        if (var10 < ((Integer)var4.getValue()).intValue() && var6)
                        {
                            return false;
                        }
                    }
                    while (var10 <= ((Integer)var4.getValue()).intValue() || var6);

                    return false;
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179603_a((Entity)p_apply_1_);
                }
            });
        }

        return var1;
    }

    private static List func_179647_f(Map p_179647_0_)
    {
        ArrayList var1 = Lists.newArrayList();
        String var2 = func_179651_b(p_179647_0_, "name");
        final boolean var3 = var2 != null && var2.startsWith("!");

        if (var3)
        {
            var2 = var2.substring(1);
        }

        if (var2 != null)
        {
            final String var2_f = var2;
            var1.add(new Predicate()
            {
                public boolean func_179600_a(Entity p_179600_1_)
                {
                    return p_179600_1_.getCommandSenderName().equals(var2_f) != var3;
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179600_a((Entity)p_apply_1_);
                }
            });
        }

        return var1;
    }

    private static List func_180698_a(Map p_180698_0_, final BlockPos p_180698_1_)
    {
        ArrayList var2 = Lists.newArrayList();
        final int var3 = func_179653_a(p_180698_0_, "rm", -1);
        final int var4 = func_179653_a(p_180698_0_, "r", -1);

        if (p_180698_1_ != null && (var3 >= 0 || var4 >= 0))
        {
            final int var5 = var3 * var3;
            final int var6 = var4 * var4;
            var2.add(new Predicate()
            {
                public boolean func_179594_a(Entity p_179594_1_)
                {
                    int var2 = (int)p_179594_1_.getDistanceSqToCenter(p_180698_1_);
                    return (var3 < 0 || var2 >= var5) && (var4 < 0 || var2 <= var6);
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179594_a((Entity)p_apply_1_);
                }
            });
        }

        return var2;
    }

    private static List func_179662_g(Map p_179662_0_)
    {
        ArrayList var1 = Lists.newArrayList();

        if (p_179662_0_.containsKey("rym") || p_179662_0_.containsKey("ry"))
        {
            final int var2 = func_179650_a(func_179653_a(p_179662_0_, "rym", 0));
            final int var3 = func_179650_a(func_179653_a(p_179662_0_, "ry", 359));
            var1.add(new Predicate()
            {
                public boolean func_179591_a(Entity p_179591_1_)
                {
                    int var2x = PlayerSelector.func_179650_a((int)Math.floor(p_179591_1_.rotationYaw));
                    return var2 > var3 ? var2x >= var2 || var2x <= var3 : var2x >= var2 && var2x <= var3;
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179591_a((Entity)p_apply_1_);
                }
            });
        }

        if (p_179662_0_.containsKey("rxm") || p_179662_0_.containsKey("rx"))
        {
            final int var2 = func_179650_a(func_179653_a(p_179662_0_, "rxm", 0));
            final int var3 = func_179650_a(func_179653_a(p_179662_0_, "rx", 359));
            var1.add(new Predicate()
            {
                public boolean func_179616_a(Entity p_179616_1_)
                {
                    int var2x = PlayerSelector.func_179650_a((int)Math.floor(p_179616_1_.rotationPitch));
                    return var2 > var3 ? var2x >= var2 || var2x <= var3 : var2x >= var2 && var2x <= var3;
                }
                @Override
				public boolean apply(Object p_apply_1_)
                {
                    return this.func_179616_a((Entity)p_apply_1_);
                }
            });
        }

        return var1;
    }

    /**
     * Filters the results based on the paramaters of the selector
     *  
     * @param params The paramaters of the selector
     * @param entityClass The class of the entity to filter
     * @param inputList The list that needs to be filtered
     * @param type The type of operation the selector is preforming
     * @param position The position of the command sender
     */
    private static List filterResults(Map params, Class entityClass, List inputList, String type, World worldIn, BlockPos position)
    {
        ArrayList var6 = Lists.newArrayList();
        String var7 = func_179651_b(params, "type");
        var7 = var7 != null && var7.startsWith("!") ? var7.substring(1) : var7;
        boolean var8 = !type.equals("e");
        boolean var9 = type.equals("r") && var7 != null;
        int var10 = func_179653_a(params, "dx", 0);
        int var11 = func_179653_a(params, "dy", 0);
        int var12 = func_179653_a(params, "dz", 0);
        int var13 = func_179653_a(params, "r", -1);
        Predicate var14 = Predicates.and(inputList);
        Predicate var15 = Predicates.and(IEntitySelector.selectAnything, var14);

        if (position != null)
        {
            int var16 = worldIn.playerEntities.size();
            int var17 = worldIn.loadedEntityList.size();
            boolean var18 = var16 < var17 / 16;
            final AxisAlignedBB var19;

            if (!params.containsKey("dx") && !params.containsKey("dy") && !params.containsKey("dz"))
            {
                if (var13 >= 0)
                {
                    var19 = new AxisAlignedBB(position.getX() - var13, position.getY() - var13, position.getZ() - var13, position.getX() + var13 + 1, position.getY() + var13 + 1, position.getZ() + var13 + 1);

                    if (var8 && var18 && !var9)
                    {
                        var6.addAll(worldIn.getPlayers(entityClass, var15));
                    }
                    else
                    {
                        var6.addAll(worldIn.getEntitiesWithinAABB(entityClass, var19, var15));
                    }
                }
                else if (type.equals("a"))
                {
                    var6.addAll(worldIn.getPlayers(entityClass, var14));
                }
                else if (!type.equals("p") && (!type.equals("r") || var9))
                {
                    var6.addAll(worldIn.getEntities(entityClass, var15));
                }
                else
                {
                    var6.addAll(worldIn.getPlayers(entityClass, var15));
                }
            }
            else
            {
                var19 = func_179661_a(position, var10, var11, var12);

                if (var8 && var18 && !var9)
                {
                    Predicate var20 = new Predicate()
                    {
                        public boolean func_179609_a(Entity p_179609_1_)
                        {
                            return p_179609_1_.posX >= var19.minX && p_179609_1_.posY >= var19.minY && p_179609_1_.posZ >= var19.minZ ? p_179609_1_.posX < var19.maxX && p_179609_1_.posY < var19.maxY && p_179609_1_.posZ < var19.maxZ : false;
                        }
                        @Override
						public boolean apply(Object p_apply_1_)
                        {
                            return this.func_179609_a((Entity)p_apply_1_);
                        }
                    };
                    var6.addAll(worldIn.getPlayers(entityClass, Predicates.and(var15, var20)));
                }
                else
                {
                    var6.addAll(worldIn.getEntitiesWithinAABB(entityClass, var19, var15));
                }
            }
        }
        else if (type.equals("a"))
        {
            var6.addAll(worldIn.getPlayers(entityClass, var14));
        }
        else if (!type.equals("p") && (!type.equals("r") || var9))
        {
            var6.addAll(worldIn.getEntities(entityClass, var15));
        }
        else
        {
            var6.addAll(worldIn.getPlayers(entityClass, var15));
        }

        return var6;
    }

    private static List func_179658_a(List p_179658_0_, Map p_179658_1_, ICommandSender p_179658_2_, Class p_179658_3_, String p_179658_4_, final BlockPos p_179658_5_)
    {
        int var6 = func_179653_a(p_179658_1_, "c", !p_179658_4_.equals("a") && !p_179658_4_.equals("e") ? 1 : 0);

        if (!p_179658_4_.equals("p") && !p_179658_4_.equals("a") && !p_179658_4_.equals("e"))
        {
            if (p_179658_4_.equals("r"))
            {
                Collections.shuffle(p_179658_0_);
            }
        }
        else if (p_179658_5_ != null)
        {
            Collections.sort(p_179658_0_, new Comparator()
            {
                public int func_179611_a(Entity p_179611_1_, Entity p_179611_2_)
                {
                    return ComparisonChain.start().compare(p_179611_1_.getDistanceSq(p_179658_5_), p_179611_2_.getDistanceSq(p_179658_5_)).result();
                }
                @Override
				public int compare(Object p_compare_1_, Object p_compare_2_)
                {
                    return this.func_179611_a((Entity)p_compare_1_, (Entity)p_compare_2_);
                }
            });
        }

        Entity var7 = p_179658_2_.getCommandSenderEntity();

        if (var7 != null && p_179658_3_.isAssignableFrom(var7.getClass()) && var6 == 1 && p_179658_0_.contains(var7) && !"r".equals(p_179658_4_))
        {
            p_179658_0_ = Lists.newArrayList(new Entity[] {var7});
        }

        if (var6 != 0)
        {
            if (var6 < 0)
            {
                Collections.reverse(p_179658_0_);
            }

            p_179658_0_ = p_179658_0_.subList(0, Math.min(Math.abs(var6), p_179658_0_.size()));
        }

        return p_179658_0_;
    }

    private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_)
    {
        boolean var4 = p_179661_1_ < 0;
        boolean var5 = p_179661_2_ < 0;
        boolean var6 = p_179661_3_ < 0;
        int var7 = p_179661_0_.getX() + (var4 ? p_179661_1_ : 0);
        int var8 = p_179661_0_.getY() + (var5 ? p_179661_2_ : 0);
        int var9 = p_179661_0_.getZ() + (var6 ? p_179661_3_ : 0);
        int var10 = p_179661_0_.getX() + (var4 ? 0 : p_179661_1_) + 1;
        int var11 = p_179661_0_.getY() + (var5 ? 0 : p_179661_2_) + 1;
        int var12 = p_179661_0_.getZ() + (var6 ? 0 : p_179661_3_) + 1;
        return new AxisAlignedBB(var7, var8, var9, var10, var11, var12);
    }

    public static int func_179650_a(int p_179650_0_)
    {
        p_179650_0_ %= 360;

        if (p_179650_0_ >= 160)
        {
            p_179650_0_ -= 360;
        }

        if (p_179650_0_ < 0)
        {
            p_179650_0_ += 360;
        }

        return p_179650_0_;
    }

    private static BlockPos func_179664_b(Map p_179664_0_, BlockPos p_179664_1_)
    {
        return new BlockPos(func_179653_a(p_179664_0_, "x", p_179664_1_.getX()), func_179653_a(p_179664_0_, "y", p_179664_1_.getY()), func_179653_a(p_179664_0_, "z", p_179664_1_.getZ()));
    }

    private static boolean func_179665_h(Map p_179665_0_)
    {
        Iterator var1 = VALID_ARGUMENTS.iterator();
        String var2;

        do
        {
            if (!var1.hasNext())
            {
                return false;
            }

            var2 = (String)var1.next();
        }
        while (!p_179665_0_.containsKey(var2));

        return true;
    }

    private static int func_179653_a(Map p_179653_0_, String p_179653_1_, int p_179653_2_)
    {
        return p_179653_0_.containsKey(p_179653_1_) ? MathHelper.parseIntWithDefault((String)p_179653_0_.get(p_179653_1_), p_179653_2_) : p_179653_2_;
    }

    private static String func_179651_b(Map p_179651_0_, String p_179651_1_)
    {
        return (String)p_179651_0_.get(p_179651_1_);
    }

    public static Map func_96560_a(Map p_96560_0_)
    {
        HashMap var1 = Maps.newHashMap();
        Iterator var2 = p_96560_0_.keySet().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();

            if (var3.startsWith("score_") && var3.length() > "score_".length())
            {
                var1.put(var3.substring("score_".length()), Integer.valueOf(MathHelper.parseIntWithDefault((String)p_96560_0_.get(var3), 1)));
            }
        }

        return var1;
    }

    /**
     * Returns whether the given pattern can match more than one player.
     */
    public static boolean matchesMultiplePlayers(String p_82377_0_)
    {
        Matcher var1 = tokenPattern.matcher(p_82377_0_);

        if (!var1.matches())
        {
            return false;
        }
        else
        {
            Map var2 = getArgumentMap(var1.group(2));
            String var3 = var1.group(1);
            int var4 = !"a".equals(var3) && !"e".equals(var3) ? 1 : 0;
            return func_179653_a(var2, "c", var4) != 1;
        }
    }

    /**
     * Returns whether the given token has any arguments set.
     */
    public static boolean hasArguments(String p_82378_0_)
    {
        return tokenPattern.matcher(p_82378_0_).matches();
    }

    /**
     * Parses the given argument string, turning it into a HashMap&lt;String, String&gt; of name-&gt;value.
     */
    private static Map getArgumentMap(String p_82381_0_)
    {
        HashMap var1 = Maps.newHashMap();

        if (p_82381_0_ == null)
        {
            return var1;
        }
        else
        {
            int var2 = 0;
            int var3 = -1;

            for (Matcher var4 = intListPattern.matcher(p_82381_0_); var4.find(); var3 = var4.end())
            {
                String var5 = null;

                switch (var2++)
                {
                    case 0:
                        var5 = "x";
                        break;

                    case 1:
                        var5 = "y";
                        break;

                    case 2:
                        var5 = "z";
                        break;

                    case 3:
                        var5 = "r";
                }

                if (var5 != null && var4.group(1).length() > 0)
                {
                    var1.put(var5, var4.group(1));
                }
            }

            if (var3 < p_82381_0_.length())
            {
                Matcher var6 = keyValueListPattern.matcher(var3 == -1 ? p_82381_0_ : p_82381_0_.substring(var3));

                while (var6.find())
                {
                    var1.put(var6.group(1), var6.group(2));
                }
            }

            return var1;
        }
    }
}
