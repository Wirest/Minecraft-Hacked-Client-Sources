package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import net.minecraft.util.EntitySelectors;
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
    private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet(new String[] {"x", "y", "z", "dx", "dy", "dz", "rm", "r"});

    /**
     * Returns the one player that matches the given at-token.  Returns null if more than one player matches.
     */
    public static EntityPlayerMP matchOnePlayer(ICommandSender sender, String token)
    {
        return (EntityPlayerMP)matchOneEntity(sender, token, EntityPlayerMP.class);
    }

    public static <T extends Entity> T matchOneEntity(ICommandSender sender, String token, Class <? extends T > targetClass)
    {
        List<T> list = matchEntities(sender, token, targetClass);
        return (T)(list.size() == 1 ? (Entity)list.get(0) : null);
    }

    public static IChatComponent matchEntitiesToChatComponent(ICommandSender sender, String token)
    {
        List<Entity> list = matchEntities(sender, token, Entity.class);

        if (list.isEmpty())
        {
            return null;
        }
        else
        {
            List<IChatComponent> list1 = Lists.<IChatComponent>newArrayList();

            for (Entity entity : list)
            {
                list1.add(entity.getDisplayName());
            }

            return CommandBase.join(list1);
        }
    }

    public static <T extends Entity> List<T> matchEntities(ICommandSender sender, String token, Class <? extends T > targetClass)
    {
        Matcher matcher = tokenPattern.matcher(token);

        if (matcher.matches() && sender.canCommandSenderUseCommand(1, "@"))
        {
            Map<String, String> map = getArgumentMap(matcher.group(2));

            if (!isEntityTypeValid(sender, map))
            {
                return Collections.<T>emptyList();
            }
            else
            {
                String s = matcher.group(1);
                BlockPos blockpos = func_179664_b(map, sender.getPosition());
                List<World> list = getWorlds(sender, map);
                List<T> list1 = Lists.<T>newArrayList();

                for (World world : list)
                {
                    if (world != null)
                    {
                        List<Predicate<Entity>> list2 = Lists.<Predicate<Entity>>newArrayList();
                        list2.addAll(func_179663_a(map, s));
                        list2.addAll(func_179648_b(map));
                        list2.addAll(func_179649_c(map));
                        list2.addAll(func_179659_d(map));
                        list2.addAll(func_179657_e(map));
                        list2.addAll(func_179647_f(map));
                        list2.addAll(func_180698_a(map, blockpos));
                        list2.addAll(func_179662_g(map));
                        list1.addAll(filterResults(map, targetClass, list2, s, world, blockpos));
                    }
                }

                return func_179658_a(list1, map, sender, targetClass, s, blockpos);
            }
        }
        else
        {
            return Collections.<T>emptyList();
        }
    }

    private static List<World> getWorlds(ICommandSender sender, Map<String, String> argumentMap)
    {
        List<World> list = Lists.<World>newArrayList();

        if (func_179665_h(argumentMap))
        {
            list.add(sender.getEntityWorld());
        }
        else
        {
            Collections.addAll(list, MinecraftServer.getServer().worldServers);
        }

        return list;
    }

    private static <T extends Entity> boolean isEntityTypeValid(ICommandSender commandSender, Map<String, String> params)
    {
        String s = func_179651_b(params, "type");
        s = s != null && s.startsWith("!") ? s.substring(1) : s;

        if (s != null && !EntityList.isStringValidEntityName(s))
        {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", new Object[] {s});
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            commandSender.addChatMessage(chatcomponenttranslation);
            return false;
        }
        else
        {
            return true;
        }
    }

    private static List<Predicate<Entity>> func_179663_a(Map<String, String> p_179663_0_, String p_179663_1_)
    {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        String s = func_179651_b(p_179663_0_, "type");
        final boolean flag = s != null && s.startsWith("!");

        if (flag)
        {
            s = s.substring(1);
        }

        boolean flag1 = !p_179663_1_.equals("e");
        boolean flag2 = p_179663_1_.equals("r") && s != null;

        if ((s == null || !p_179663_1_.equals("e")) && !flag2)
        {
            if (flag1)
            {
                list.add(new Predicate<Entity>()
                {
                    public boolean apply(Entity p_apply_1_)
                    {
                        return p_apply_1_ instanceof EntityPlayer;
                    }
                });
            }
        }
        else
        {
            final String s_f = s;
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    return EntityList.isStringEntityName(p_apply_1_, s_f) != flag;
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179648_b(Map<String, String> p_179648_0_)
    {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        final int i = parseIntWithDefault(p_179648_0_, "lm", -1);
        final int j = parseIntWithDefault(p_179648_0_, "l", -1);

        if (i > -1 || j > -1)
        {
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    if (!(p_apply_1_ instanceof EntityPlayerMP))
                    {
                        return false;
                    }
                    else
                    {
                        EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                        return (i <= -1 || entityplayermp.experienceLevel >= i) && (j <= -1 || entityplayermp.experienceLevel <= j);
                    }
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179649_c(Map<String, String> p_179649_0_)
    {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        final int i = parseIntWithDefault(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());

        if (i != WorldSettings.GameType.NOT_SET.getID())
        {
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    if (!(p_apply_1_ instanceof EntityPlayerMP))
                    {
                        return false;
                    }
                    else
                    {
                        EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                        return entityplayermp.theItemInWorldManager.getGameType().getID() == i;
                    }
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179659_d(Map<String, String> p_179659_0_)
    {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        String s = func_179651_b(p_179659_0_, "team");
        final boolean flag = s != null && s.startsWith("!");

        if (flag)
        {
            s = s.substring(1);
        }

        if (s != null)
        {
            final String s_f = s;
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    if (!(p_apply_1_ instanceof EntityLivingBase))
                    {
                        return false;
                    }
                    else
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
                        Team team = entitylivingbase.getTeam();
                        String s1 = team == null ? "" : team.getRegisteredName();
                        return s1.equals(s_f) != flag;
                    }
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179657_e(Map<String, String> p_179657_0_)
    {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        final Map<String, Integer> map = func_96560_a(p_179657_0_);

        if (map != null && map.size() > 0)
        {
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();

                    for (Entry<String, Integer> entry : map.entrySet())
                    {
                        String s = (String)entry.getKey();
                        boolean flag = false;

                        if (s.endsWith("_min") && s.length() > 4)
                        {
                            flag = true;
                            s = s.substring(0, s.length() - 4);
                        }

                        ScoreObjective scoreobjective = scoreboard.getObjective(s);

                        if (scoreobjective == null)
                        {
                            return false;
                        }

                        String s1 = p_apply_1_ instanceof EntityPlayerMP ? p_apply_1_.getName() : p_apply_1_.getUniqueID().toString();

                        if (!scoreboard.entityHasObjective(s1, scoreobjective))
                        {
                            return false;
                        }

                        Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
                        int i = score.getScorePoints();

                        if (i < ((Integer)entry.getValue()).intValue() && flag)
                        {
                            return false;
                        }

                        if (i > ((Integer)entry.getValue()).intValue() && !flag)
                        {
                            return false;
                        }
                    }

                    return true;
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179647_f(Map<String, String> p_179647_0_)
    {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        String s = func_179651_b(p_179647_0_, "name");
        final boolean flag = s != null && s.startsWith("!");

        if (flag)
        {
            s = s.substring(1);
        }

        if (s != null)
        {
            final String s_f = s;
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    return p_apply_1_.getName().equals(s_f) != flag;
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_180698_a(Map<String, String> p_180698_0_, final BlockPos p_180698_1_)
    {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        final int i = parseIntWithDefault(p_180698_0_, "rm", -1);
        final int j = parseIntWithDefault(p_180698_0_, "r", -1);

        if (p_180698_1_ != null && (i >= 0 || j >= 0))
        {
            final int k = i * i;
            final int l = j * j;
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    int i1 = (int)p_apply_1_.getDistanceSqToCenter(p_180698_1_);
                    return (i < 0 || i1 >= k) && (j < 0 || i1 <= l);
                }
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> func_179662_g(Map<String, String> p_179662_0_)
    {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();

        if (p_179662_0_.containsKey("rym") || p_179662_0_.containsKey("ry"))
        {
            final int i = func_179650_a(parseIntWithDefault(p_179662_0_, "rym", 0));
            final int j = func_179650_a(parseIntWithDefault(p_179662_0_, "ry", 359));
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    int i1 = PlayerSelector.func_179650_a((int)Math.floor((double)p_apply_1_.rotationYaw));
                    return i > j ? i1 >= i || i1 <= j : i1 >= i && i1 <= j;
                }
            });
        }

        if (p_179662_0_.containsKey("rxm") || p_179662_0_.containsKey("rx"))
        {
            final int k = func_179650_a(parseIntWithDefault(p_179662_0_, "rxm", 0));
            final int l = func_179650_a(parseIntWithDefault(p_179662_0_, "rx", 359));
            list.add(new Predicate<Entity>()
            {
                public boolean apply(Entity p_apply_1_)
                {
                    int i1 = PlayerSelector.func_179650_a((int)Math.floor((double)p_apply_1_.rotationPitch));
                    return k > l ? i1 >= k || i1 <= l : i1 >= k && i1 <= l;
                }
            });
        }

        return list;
    }

    private static List filterResults(Map p_179660_0_, Class p_179660_1_, List p_179660_2_, String p_179660_3_, World worldIn, BlockPos p_179660_5_)
    {
        ArrayList<Object> var6 = Lists.newArrayList();
        String var7 = func_179651_b(p_179660_0_, "type");
        var7 = var7 != null && var7.startsWith("!") ? var7.substring(1) : var7;
        boolean var8 = !p_179660_3_.equals("e");
        boolean var9 = p_179660_3_.equals("r") && var7 != null;
        int var10 = parseIntWithDefault(p_179660_0_, "dx", 0);
        int var11 = parseIntWithDefault(p_179660_0_, "dy", 0);
        int var12 = parseIntWithDefault(p_179660_0_, "dz", 0);
        int var13 = parseIntWithDefault(p_179660_0_, "r", -1);
        Predicate var14 = Predicates.and(p_179660_2_);
        Predicate var15 = Predicates.<Entity> and (EntitySelectors.selectAnything, var14);

        if (p_179660_5_ != null)
        {
            int var16 = worldIn.playerEntities.size();
            int var17 = worldIn.loadedEntityList.size();
            boolean var18 = var16 < var17 / 16;
            final AxisAlignedBB var19;

            if (!p_179660_0_.containsKey("dx") && !p_179660_0_.containsKey("dy") && !p_179660_0_.containsKey("dz"))
            {
                if (var13 >= 0)
                {
                    var19 = new AxisAlignedBB((double)(p_179660_5_.getX() - var13), (double)(p_179660_5_.getY() - var13), (double)(p_179660_5_.getZ() - var13), (double)(p_179660_5_.getX() + var13 + 1), (double)(p_179660_5_.getY() + var13 + 1), (double)(p_179660_5_.getZ() + var13 + 1));

                    if (var8 && var18 && !var9)
                    {
                        var6.addAll(worldIn.getPlayers(p_179660_1_, var15));
                    }
                    else
                    {
                        var6.addAll(worldIn.getEntitiesWithinAABB(p_179660_1_, var19, var15));
                    }
                }
                else if (p_179660_3_.equals("a"))
                {
                    var6.addAll(worldIn.getPlayers(p_179660_1_, var14));
                }
                else if (!p_179660_3_.equals("p") && (!p_179660_3_.equals("r") || var9))
                {
                    var6.addAll(worldIn.func_175644_a(p_179660_1_, var15));
                }
                else
                {
                    var6.addAll(worldIn.getPlayers(p_179660_1_, var15));
                }
            }
            else
            {
                var19 = func_179661_a(p_179660_5_, var10, var11, var12);

                if (var8 && var18 && !var9)
                {
                    Predicate var20 = new Predicate()
                    {
                        private static final String __OBFID = "CL_00002360";
                        public boolean func_179609_a(Entity p_179609_1_)
                        {
                            return p_179609_1_.posX >= var19.minX && p_179609_1_.posY >= var19.minY && p_179609_1_.posZ >= var19.minZ ? p_179609_1_.posX < var19.maxX && p_179609_1_.posY < var19.maxY && p_179609_1_.posZ < var19.maxZ : false;
                        }
                        public boolean apply(Object p_apply_1_)
                        {
                            return this.func_179609_a((Entity)p_apply_1_);
                        }
                    };
                    var6.addAll(worldIn.getPlayers(p_179660_1_, Predicates.and(var15, var20)));
                }
                else
                {
                    var6.addAll(worldIn.getEntitiesWithinAABB(p_179660_1_, var19, var15));
                }
            }
        }
        else if (p_179660_3_.equals("a"))
        {
            var6.addAll(worldIn.getPlayers(p_179660_1_, var14));
        }
        else if (!p_179660_3_.equals("p") && (!p_179660_3_.equals("r") || var9))
        {
            var6.addAll(worldIn.func_175644_a(p_179660_1_, var15));
        }
        else
        {
            var6.addAll(worldIn.getPlayers(p_179660_1_, var15));
        }

        return var6;
    }
    private static <T extends Entity> List<T> func_179658_a(List<T> p_179658_0_, Map<String, String> p_179658_1_, ICommandSender p_179658_2_, Class <? extends T > p_179658_3_, String p_179658_4_, final BlockPos p_179658_5_)
    {
        int i = parseIntWithDefault(p_179658_1_, "c", !p_179658_4_.equals("a") && !p_179658_4_.equals("e") ? 1 : 0);

        if (!p_179658_4_.equals("p") && !p_179658_4_.equals("a") && !p_179658_4_.equals("e"))
        {
            if (p_179658_4_.equals("r"))
            {
                Collections.shuffle((List<?>)p_179658_0_);
            }
        }
        else if (p_179658_5_ != null)
        {
            Collections.sort((List<T>)p_179658_0_, new Comparator<Entity>()
            {
                public int compare(Entity p_compare_1_, Entity p_compare_2_)
                {
                    return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(p_179658_5_), p_compare_2_.getDistanceSq(p_179658_5_)).result();
                }
            });
        }

        Entity entity = p_179658_2_.getCommandSenderEntity();

        if (entity != null && p_179658_3_.isAssignableFrom(entity.getClass()) && i == 1 && ((List)p_179658_0_).contains(entity) && !"r".equals(p_179658_4_))
        {
            p_179658_0_ = Lists.newArrayList((T)entity);
        }

        if (i != 0)
        {
            if (i < 0)
            {
                Collections.reverse((List<?>)p_179658_0_);
            }

            p_179658_0_ = ((List)p_179658_0_).subList(0, Math.min(Math.abs(i), ((List)p_179658_0_).size()));
        }

        return (List)p_179658_0_;
    }

    private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_)
    {
        boolean flag = p_179661_1_ < 0;
        boolean flag1 = p_179661_2_ < 0;
        boolean flag2 = p_179661_3_ < 0;
        int i = p_179661_0_.getX() + (flag ? p_179661_1_ : 0);
        int j = p_179661_0_.getY() + (flag1 ? p_179661_2_ : 0);
        int k = p_179661_0_.getZ() + (flag2 ? p_179661_3_ : 0);
        int l = p_179661_0_.getX() + (flag ? 0 : p_179661_1_) + 1;
        int i1 = p_179661_0_.getY() + (flag1 ? 0 : p_179661_2_) + 1;
        int j1 = p_179661_0_.getZ() + (flag2 ? 0 : p_179661_3_) + 1;
        return new AxisAlignedBB((double)i, (double)j, (double)k, (double)l, (double)i1, (double)j1);
    }

    public static int func_179650_a(int p_179650_0_)
    {
        p_179650_0_ = p_179650_0_ % 360;

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

    private static BlockPos func_179664_b(Map<String, String> p_179664_0_, BlockPos p_179664_1_)
    {
        return new BlockPos(parseIntWithDefault(p_179664_0_, "x", p_179664_1_.getX()), parseIntWithDefault(p_179664_0_, "y", p_179664_1_.getY()), parseIntWithDefault(p_179664_0_, "z", p_179664_1_.getZ()));
    }

    private static boolean func_179665_h(Map<String, String> p_179665_0_)
    {
        for (String s : WORLD_BINDING_ARGS)
        {
            if (p_179665_0_.containsKey(s))
            {
                return true;
            }
        }

        return false;
    }

    private static int parseIntWithDefault(Map<String, String> p_179653_0_, String p_179653_1_, int p_179653_2_)
    {
        return p_179653_0_.containsKey(p_179653_1_) ? MathHelper.parseIntWithDefault((String)p_179653_0_.get(p_179653_1_), p_179653_2_) : p_179653_2_;
    }

    private static String func_179651_b(Map<String, String> p_179651_0_, String p_179651_1_)
    {
        return (String)p_179651_0_.get(p_179651_1_);
    }

    public static Map<String, Integer> func_96560_a(Map<String, String> p_96560_0_)
    {
        Map<String, Integer> map = Maps.<String, Integer>newHashMap();

        for (String s : p_96560_0_.keySet())
        {
            if (s.startsWith("score_") && s.length() > "score_".length())
            {
                map.put(s.substring("score_".length()), Integer.valueOf(MathHelper.parseIntWithDefault((String)p_96560_0_.get(s), 1)));
            }
        }

        return map;
    }

    /**
     * Returns whether the given pattern can match more than one player.
     */
    public static boolean matchesMultiplePlayers(String p_82377_0_)
    {
        Matcher matcher = tokenPattern.matcher(p_82377_0_);

        if (!matcher.matches())
        {
            return false;
        }
        else
        {
            Map<String, String> map = getArgumentMap(matcher.group(2));
            String s = matcher.group(1);
            int i = !"a".equals(s) && !"e".equals(s) ? 1 : 0;
            return parseIntWithDefault(map, "c", i) != 1;
        }
    }

    /**
     * Returns whether the given token has any arguments set.
     */
    public static boolean hasArguments(String p_82378_0_)
    {
        return tokenPattern.matcher(p_82378_0_).matches();
    }

    private static Map<String, String> getArgumentMap(String argumentString)
    {
        Map<String, String> map = Maps.<String, String>newHashMap();

        if (argumentString == null)
        {
            return map;
        }
        else
        {
            int i = 0;
            int j = -1;

            for (Matcher matcher = intListPattern.matcher(argumentString); matcher.find(); j = matcher.end())
            {
                String s = null;

                switch (i++)
                {
                    case 0:
                        s = "x";
                        break;

                    case 1:
                        s = "y";
                        break;

                    case 2:
                        s = "z";
                        break;

                    case 3:
                        s = "r";
                }

                if (s != null && matcher.group(1).length() > 0)
                {
                    map.put(s, matcher.group(1));
                }
            }

            if (j < argumentString.length())
            {
                Matcher matcher1 = keyValueListPattern.matcher(j == -1 ? argumentString : argumentString.substring(j));

                while (matcher1.find())
                {
                    map.put(matcher1.group(1), matcher1.group(2));
                }
            }

            return map;
        }
    }
}
