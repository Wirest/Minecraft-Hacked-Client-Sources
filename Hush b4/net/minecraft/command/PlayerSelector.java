// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import com.google.common.collect.Maps;
import net.minecraft.util.MathHelper;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import com.google.common.base.Predicates;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.entity.EntityList;
import net.minecraft.world.WorldServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import java.util.Map;
import java.util.regex.Matcher;
import com.google.common.base.Predicate;
import java.util.Collection;
import net.minecraft.world.World;
import java.util.Collections;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.IChatComponent;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.regex.Pattern;

public class PlayerSelector
{
    private static final Pattern tokenPattern;
    private static final Pattern intListPattern;
    private static final Pattern keyValueListPattern;
    private static final Set<String> WORLD_BINDING_ARGS;
    
    static {
        tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
        intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
        keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
        WORLD_BINDING_ARGS = Sets.newHashSet("x", "y", "z", "dx", "dy", "dz", "rm", "r");
    }
    
    public static EntityPlayerMP matchOnePlayer(final ICommandSender sender, final String token) {
        return matchOneEntity(sender, token, (Class<? extends EntityPlayerMP>)EntityPlayerMP.class);
    }
    
    public static <T extends Entity> T matchOneEntity(final ICommandSender sender, final String token, final Class<? extends T> targetClass) {
        final List<T> list = matchEntities(sender, token, targetClass);
        return (T)((list.size() == 1) ? ((T)list.get(0)) : null);
    }
    
    public static IChatComponent matchEntitiesToChatComponent(final ICommandSender sender, final String token) {
        final List<Entity> list = matchEntities(sender, token, (Class<? extends Entity>)Entity.class);
        if (list.isEmpty()) {
            return null;
        }
        final List<IChatComponent> list2 = (List<IChatComponent>)Lists.newArrayList();
        for (final Entity entity : list) {
            list2.add(entity.getDisplayName());
        }
        return CommandBase.join(list2);
    }
    
    public static <T extends Entity> List<T> matchEntities(final ICommandSender sender, final String token, final Class<? extends T> targetClass) {
        final Matcher matcher = PlayerSelector.tokenPattern.matcher(token);
        if (!matcher.matches() || !sender.canCommandSenderUseCommand(1, "@")) {
            return Collections.emptyList();
        }
        final Map<String, String> map = getArgumentMap(matcher.group(2));
        if (!isEntityTypeValid(sender, map)) {
            return Collections.emptyList();
        }
        final String s = matcher.group(1);
        final BlockPos blockpos = func_179664_b(map, sender.getPosition());
        final List<World> list = getWorlds(sender, map);
        final List<T> list2 = (List<T>)Lists.newArrayList();
        for (final World world : list) {
            if (world != null) {
                final List<Predicate<Entity>> list3 = (List<Predicate<Entity>>)Lists.newArrayList();
                list3.addAll(func_179663_a(map, s));
                list3.addAll(func_179648_b(map));
                list3.addAll(func_179649_c(map));
                list3.addAll(func_179659_d(map));
                list3.addAll(func_179657_e(map));
                list3.addAll(func_179647_f(map));
                list3.addAll(func_180698_a(map, blockpos));
                list3.addAll(func_179662_g(map));
                list2.addAll((Collection<? extends T>)filterResults(map, (Class<? extends Entity>)targetClass, list3, s, world, blockpos));
            }
        }
        return func_179658_a(list2, map, sender, targetClass, s, blockpos);
    }
    
    private static List<World> getWorlds(final ICommandSender sender, final Map<String, String> argumentMap) {
        final List<World> list = (List<World>)Lists.newArrayList();
        if (func_179665_h(argumentMap)) {
            list.add(sender.getEntityWorld());
        }
        else {
            Collections.addAll(list, MinecraftServer.getServer().worldServers);
        }
        return list;
    }
    
    private static <T extends Entity> boolean isEntityTypeValid(final ICommandSender commandSender, final Map<String, String> params) {
        String s = func_179651_b(params, "type");
        s = ((s != null && s.startsWith("!")) ? s.substring(1) : s);
        if (s != null && !EntityList.isStringValidEntityName(s)) {
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", new Object[] { s });
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            commandSender.addChatMessage(chatcomponenttranslation);
            return false;
        }
        return true;
    }
    
    private static List<Predicate<Entity>> func_179663_a(final Map<String, String> p_179663_0_, final String p_179663_1_) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = func_179651_b(p_179663_0_, "type");
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        final boolean flag2 = !p_179663_1_.equals("e");
        final boolean flag3 = p_179663_1_.equals("r") && s != null;
        if ((s == null || !p_179663_1_.equals("e")) && !flag3) {
            if (flag2) {
                list.add(new Predicate<Entity>() {
                    @Override
                    public boolean apply(final Entity p_apply_1_) {
                        return p_apply_1_ instanceof EntityPlayer;
                    }
                });
            }
        }
        else {
            final String s_f = s;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    return EntityList.isStringEntityName(p_apply_1_, s_f) ^ flag;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> func_179648_b(final Map<String, String> p_179648_0_) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        final int i = parseIntWithDefault(p_179648_0_, "lm", -1);
        final int j = parseIntWithDefault(p_179648_0_, "l", -1);
        if (i > -1 || j > -1) {
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    final EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                    return (i <= -1 || entityplayermp.experienceLevel >= i) && (j <= -1 || entityplayermp.experienceLevel <= j);
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> func_179649_c(final Map<String, String> p_179649_0_) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        final int i = parseIntWithDefault(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());
        if (i != WorldSettings.GameType.NOT_SET.getID()) {
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    final EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                    return entityplayermp.theItemInWorldManager.getGameType().getID() == i;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> func_179659_d(final Map<String, String> p_179659_0_) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = func_179651_b(p_179659_0_, "team");
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f = s;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityLivingBase)) {
                        return false;
                    }
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
                    final Team team = entitylivingbase.getTeam();
                    final String s1 = (team == null) ? "" : team.getRegisteredName();
                    return s1.equals(s_f) ^ flag;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> func_179657_e(final Map<String, String> p_179657_0_) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        final Map<String, Integer> map = func_96560_a(p_179657_0_);
        if (map != null && map.size() > 0) {
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    final Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
                    for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                        String s = entry.getKey();
                        boolean flag = false;
                        if (s.endsWith("_min") && s.length() > 4) {
                            flag = true;
                            s = s.substring(0, s.length() - 4);
                        }
                        final ScoreObjective scoreobjective = scoreboard.getObjective(s);
                        if (scoreobjective == null) {
                            return false;
                        }
                        final String s2 = (p_apply_1_ instanceof EntityPlayerMP) ? p_apply_1_.getName() : p_apply_1_.getUniqueID().toString();
                        if (!scoreboard.entityHasObjective(s2, scoreobjective)) {
                            return false;
                        }
                        final Score score = scoreboard.getValueFromObjective(s2, scoreobjective);
                        final int i = score.getScorePoints();
                        if (i < entry.getValue() && flag) {
                            return false;
                        }
                        if (i > entry.getValue() && !flag) {
                            return false;
                        }
                    }
                    return true;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> func_179647_f(final Map<String, String> p_179647_0_) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = func_179651_b(p_179647_0_, "name");
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f = s;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    return p_apply_1_.getName().equals(s_f) ^ flag;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> func_180698_a(final Map<String, String> p_180698_0_, final BlockPos p_180698_1_) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        final int i = parseIntWithDefault(p_180698_0_, "rm", -1);
        final int j = parseIntWithDefault(p_180698_0_, "r", -1);
        if (p_180698_1_ != null && (i >= 0 || j >= 0)) {
            final int k = i * i;
            final int l = j * j;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    final int i1 = (int)p_apply_1_.getDistanceSqToCenter(p_180698_1_);
                    return (i < 0 || i1 >= k) && (j < 0 || i1 <= l);
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> func_179662_g(final Map<String, String> p_179662_0_) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        if (p_179662_0_.containsKey("rym") || p_179662_0_.containsKey("ry")) {
            final int i = func_179650_a(parseIntWithDefault(p_179662_0_, "rym", 0));
            final int j = func_179650_a(parseIntWithDefault(p_179662_0_, "ry", 359));
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    final int i1 = PlayerSelector.func_179650_a((int)Math.floor(p_apply_1_.rotationYaw));
                    return (i > j) ? (i1 >= i || i1 <= j) : (i1 >= i && i1 <= j);
                }
            });
        }
        if (p_179662_0_.containsKey("rxm") || p_179662_0_.containsKey("rx")) {
            final int k = func_179650_a(parseIntWithDefault(p_179662_0_, "rxm", 0));
            final int l = func_179650_a(parseIntWithDefault(p_179662_0_, "rx", 359));
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(final Entity p_apply_1_) {
                    final int i1 = PlayerSelector.func_179650_a((int)Math.floor(p_apply_1_.rotationPitch));
                    return (k > l) ? (i1 >= k || i1 <= l) : (i1 >= k && i1 <= l);
                }
            });
        }
        return list;
    }
    
    private static <T extends Entity> List<T> filterResults(final Map<String, String> params, final Class<? extends T> entityClass, final List<Predicate<Entity>> inputList, final String type, final World worldIn, final BlockPos position) {
        final List<T> list = (List<T>)Lists.newArrayList();
        String s = func_179651_b(params, "type");
        s = ((s != null && s.startsWith("!")) ? s.substring(1) : s);
        final boolean flag = !type.equals("e");
        final boolean flag2 = type.equals("r") && s != null;
        final int i = parseIntWithDefault(params, "dx", 0);
        final int j = parseIntWithDefault(params, "dy", 0);
        final int k = parseIntWithDefault(params, "dz", 0);
        final int l = parseIntWithDefault(params, "r", -1);
        final Predicate<Entity> predicate = Predicates.and((Iterable<? extends Predicate<? super Entity>>)inputList);
        final Predicate<Entity> predicate2 = Predicates.and((Predicate<? super Entity>)EntitySelectors.selectAnything, (Predicate<? super Entity>)predicate);
        if (position != null) {
            final int i2 = worldIn.playerEntities.size();
            final int j2 = worldIn.loadedEntityList.size();
            final boolean flag3 = i2 < j2 / 16;
            if (!params.containsKey("dx") && !params.containsKey("dy") && !params.containsKey("dz")) {
                if (l >= 0) {
                    final AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(position.getX() - l, position.getY() - l, position.getZ() - l, position.getX() + l + 1, position.getY() + l + 1, position.getZ() + l + 1);
                    if (flag && flag3 && !flag2) {
                        list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate2));
                    }
                    else {
                        list.addAll((Collection<? extends T>)worldIn.getEntitiesWithinAABB((Class<? extends Entity>)entityClass, axisalignedbb1, (Predicate<? super Entity>)predicate2));
                    }
                }
                else if (type.equals("a")) {
                    list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate));
                }
                else if (!type.equals("p") && (!type.equals("r") || flag2)) {
                    list.addAll((Collection<? extends T>)worldIn.getEntities((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate2));
                }
                else {
                    list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate2));
                }
            }
            else {
                final AxisAlignedBB axisalignedbb2 = func_179661_a(position, i, j, k);
                if (flag && flag3 && !flag2) {
                    final Predicate<Entity> predicate3 = new Predicate<Entity>() {
                        @Override
                        public boolean apply(final Entity p_apply_1_) {
                            return p_apply_1_.posX >= axisalignedbb2.minX && p_apply_1_.posY >= axisalignedbb2.minY && p_apply_1_.posZ >= axisalignedbb2.minZ && (p_apply_1_.posX < axisalignedbb2.maxX && p_apply_1_.posY < axisalignedbb2.maxY && p_apply_1_.posZ < axisalignedbb2.maxZ);
                        }
                    };
                    list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, Predicates.and((Predicate<? super Entity>)predicate2, (Predicate<? super Entity>)predicate3)));
                }
                else {
                    list.addAll((Collection<? extends T>)worldIn.getEntitiesWithinAABB((Class<? extends Entity>)entityClass, axisalignedbb2, (Predicate<? super Entity>)predicate2));
                }
            }
        }
        else if (type.equals("a")) {
            list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate));
        }
        else if (!type.equals("p") && (!type.equals("r") || flag2)) {
            list.addAll((Collection<? extends T>)worldIn.getEntities((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate2));
        }
        else {
            list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (Predicate<? super Entity>)predicate2));
        }
        return list;
    }
    
    private static <T extends Entity> List<T> func_179658_a(List<T> p_179658_0_, final Map<String, String> p_179658_1_, final ICommandSender p_179658_2_, final Class<? extends T> p_179658_3_, final String p_179658_4_, final BlockPos p_179658_5_) {
        final int i = parseIntWithDefault(p_179658_1_, "c", (!p_179658_4_.equals("a") && !p_179658_4_.equals("e")) ? 1 : 0);
        if (!p_179658_4_.equals("p") && !p_179658_4_.equals("a") && !p_179658_4_.equals("e")) {
            if (p_179658_4_.equals("r")) {
                Collections.shuffle(p_179658_0_);
            }
        }
        else if (p_179658_5_ != null) {
            Collections.sort(p_179658_0_, new Comparator<Entity>() {
                @Override
                public int compare(final Entity p_compare_1_, final Entity p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(p_179658_5_), p_compare_2_.getDistanceSq(p_179658_5_)).result();
                }
            });
        }
        final Entity entity = p_179658_2_.getCommandSenderEntity();
        if (entity != null && p_179658_3_.isAssignableFrom(entity.getClass()) && i == 1 && p_179658_0_.contains(entity) && !"r".equals(p_179658_4_)) {
            p_179658_0_ = (List<T>)Lists.newArrayList(entity);
        }
        if (i != 0) {
            if (i < 0) {
                Collections.reverse(p_179658_0_);
            }
            p_179658_0_ = p_179658_0_.subList(0, Math.min(Math.abs(i), p_179658_0_.size()));
        }
        return p_179658_0_;
    }
    
    private static AxisAlignedBB func_179661_a(final BlockPos p_179661_0_, final int p_179661_1_, final int p_179661_2_, final int p_179661_3_) {
        final boolean flag = p_179661_1_ < 0;
        final boolean flag2 = p_179661_2_ < 0;
        final boolean flag3 = p_179661_3_ < 0;
        final int i = p_179661_0_.getX() + (flag ? p_179661_1_ : 0);
        final int j = p_179661_0_.getY() + (flag2 ? p_179661_2_ : 0);
        final int k = p_179661_0_.getZ() + (flag3 ? p_179661_3_ : 0);
        final int l = p_179661_0_.getX() + (flag ? 0 : p_179661_1_) + 1;
        final int i2 = p_179661_0_.getY() + (flag2 ? 0 : p_179661_2_) + 1;
        final int j2 = p_179661_0_.getZ() + (flag3 ? 0 : p_179661_3_) + 1;
        return new AxisAlignedBB(i, j, k, l, i2, j2);
    }
    
    public static int func_179650_a(int p_179650_0_) {
        p_179650_0_ %= 360;
        if (p_179650_0_ >= 160) {
            p_179650_0_ -= 360;
        }
        if (p_179650_0_ < 0) {
            p_179650_0_ += 360;
        }
        return p_179650_0_;
    }
    
    private static BlockPos func_179664_b(final Map<String, String> p_179664_0_, final BlockPos p_179664_1_) {
        return new BlockPos(parseIntWithDefault(p_179664_0_, "x", p_179664_1_.getX()), parseIntWithDefault(p_179664_0_, "y", p_179664_1_.getY()), parseIntWithDefault(p_179664_0_, "z", p_179664_1_.getZ()));
    }
    
    private static boolean func_179665_h(final Map<String, String> p_179665_0_) {
        for (final String s : PlayerSelector.WORLD_BINDING_ARGS) {
            if (p_179665_0_.containsKey(s)) {
                return true;
            }
        }
        return false;
    }
    
    private static int parseIntWithDefault(final Map<String, String> p_179653_0_, final String p_179653_1_, final int p_179653_2_) {
        return p_179653_0_.containsKey(p_179653_1_) ? MathHelper.parseIntWithDefault(p_179653_0_.get(p_179653_1_), p_179653_2_) : p_179653_2_;
    }
    
    private static String func_179651_b(final Map<String, String> p_179651_0_, final String p_179651_1_) {
        return p_179651_0_.get(p_179651_1_);
    }
    
    public static Map<String, Integer> func_96560_a(final Map<String, String> p_96560_0_) {
        final Map<String, Integer> map = (Map<String, Integer>)Maps.newHashMap();
        for (final String s : p_96560_0_.keySet()) {
            if (s.startsWith("score_") && s.length() > "score_".length()) {
                map.put(s.substring("score_".length()), MathHelper.parseIntWithDefault(p_96560_0_.get(s), 1));
            }
        }
        return map;
    }
    
    public static boolean matchesMultiplePlayers(final String p_82377_0_) {
        final Matcher matcher = PlayerSelector.tokenPattern.matcher(p_82377_0_);
        if (!matcher.matches()) {
            return false;
        }
        final Map<String, String> map = getArgumentMap(matcher.group(2));
        final String s = matcher.group(1);
        final int i = (!"a".equals(s) && !"e".equals(s)) ? 1 : 0;
        return parseIntWithDefault(map, "c", i) != 1;
    }
    
    public static boolean hasArguments(final String p_82378_0_) {
        return PlayerSelector.tokenPattern.matcher(p_82378_0_).matches();
    }
    
    private static Map<String, String> getArgumentMap(final String argumentString) {
        final Map<String, String> map = (Map<String, String>)Maps.newHashMap();
        if (argumentString == null) {
            return map;
        }
        int i = 0;
        int j = -1;
        final Matcher matcher = PlayerSelector.intListPattern.matcher(argumentString);
        while (matcher.find()) {
            String s = null;
            switch (i++) {
                case 0: {
                    s = "x";
                    break;
                }
                case 1: {
                    s = "y";
                    break;
                }
                case 2: {
                    s = "z";
                    break;
                }
                case 3: {
                    s = "r";
                    break;
                }
            }
            if (s != null && matcher.group(1).length() > 0) {
                map.put(s, matcher.group(1));
            }
            j = matcher.end();
        }
        if (j < argumentString.length()) {
            final Matcher matcher2 = PlayerSelector.keyValueListPattern.matcher((j == -1) ? argumentString : argumentString.substring(j));
            while (matcher2.find()) {
                map.put(matcher2.group(1), matcher2.group(2));
            }
        }
        return map;
    }
}
