// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.block.material.Material;
import java.util.Map;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.scoreboard.Team;
import com.google.common.collect.Sets;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import net.minecraft.entity.Entity;
import com.google.common.collect.Lists;

public class CommandSpreadPlayers extends CommandBase
{
    @Override
    public String getCommandName() {
        return "spreadplayers";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.spreadplayers.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 6) {
            throw new WrongUsageException("commands.spreadplayers.usage", new Object[0]);
        }
        int i = 0;
        final BlockPos blockpos = sender.getPosition();
        final double d0 = CommandBase.parseDouble(blockpos.getX(), args[i++], true);
        final double d2 = CommandBase.parseDouble(blockpos.getZ(), args[i++], true);
        final double d3 = CommandBase.parseDouble(args[i++], 0.0);
        final double d4 = CommandBase.parseDouble(args[i++], d3 + 1.0);
        final boolean flag = CommandBase.parseBoolean(args[i++]);
        final List<Entity> list = (List<Entity>)Lists.newArrayList();
        while (i < args.length) {
            final String s = args[i++];
            if (PlayerSelector.hasArguments(s)) {
                final List<Entity> list2 = PlayerSelector.matchEntities(sender, s, (Class<? extends Entity>)Entity.class);
                if (list2.size() == 0) {
                    throw new EntityNotFoundException();
                }
                list.addAll(list2);
            }
            else {
                final EntityPlayer entityplayer = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(s);
                if (entityplayer == null) {
                    throw new PlayerNotFoundException();
                }
                list.add(entityplayer);
            }
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
        if (list.isEmpty()) {
            throw new EntityNotFoundException();
        }
        sender.addChatMessage(new ChatComponentTranslation("commands.spreadplayers.spreading." + (flag ? "teams" : "players"), new Object[] { list.size(), d4, d0, d2, d3 }));
        this.func_110669_a(sender, list, new Position(d0, d2), d3, d4, list.get(0).worldObj, flag);
    }
    
    private void func_110669_a(final ICommandSender p_110669_1_, final List<Entity> p_110669_2_, final Position p_110669_3_, final double p_110669_4_, final double p_110669_6_, final World worldIn, final boolean p_110669_9_) throws CommandException {
        final Random random = new Random();
        final double d0 = p_110669_3_.field_111101_a - p_110669_6_;
        final double d2 = p_110669_3_.field_111100_b - p_110669_6_;
        final double d3 = p_110669_3_.field_111101_a + p_110669_6_;
        final double d4 = p_110669_3_.field_111100_b + p_110669_6_;
        final Position[] acommandspreadplayers$position = this.func_110670_a(random, p_110669_9_ ? this.func_110667_a(p_110669_2_) : p_110669_2_.size(), d0, d2, d3, d4);
        final int i = this.func_110668_a(p_110669_3_, p_110669_4_, worldIn, random, d0, d2, d3, d4, acommandspreadplayers$position, p_110669_9_);
        final double d5 = this.func_110671_a(p_110669_2_, worldIn, acommandspreadplayers$position, p_110669_9_);
        CommandBase.notifyOperators(p_110669_1_, this, "commands.spreadplayers.success." + (p_110669_9_ ? "teams" : "players"), acommandspreadplayers$position.length, p_110669_3_.field_111101_a, p_110669_3_.field_111100_b);
        if (acommandspreadplayers$position.length > 1) {
            p_110669_1_.addChatMessage(new ChatComponentTranslation("commands.spreadplayers.info." + (p_110669_9_ ? "teams" : "players"), new Object[] { String.format("%.2f", d5), i }));
        }
    }
    
    private int func_110667_a(final List<Entity> p_110667_1_) {
        final Set<Team> set = (Set<Team>)Sets.newHashSet();
        for (final Entity entity : p_110667_1_) {
            if (entity instanceof EntityPlayer) {
                set.add(((EntityPlayer)entity).getTeam());
            }
            else {
                set.add(null);
            }
        }
        return set.size();
    }
    
    private int func_110668_a(final Position p_110668_1_, final double p_110668_2_, final World worldIn, final Random p_110668_5_, final double p_110668_6_, final double p_110668_8_, final double p_110668_10_, final double p_110668_12_, final Position[] p_110668_14_, final boolean p_110668_15_) throws CommandException {
        boolean flag = true;
        double d0 = 3.4028234663852886E38;
        int i;
        for (i = 0; i < 10000 && flag; ++i) {
            flag = false;
            d0 = 3.4028234663852886E38;
            for (int j = 0; j < p_110668_14_.length; ++j) {
                final Position commandspreadplayers$position = p_110668_14_[j];
                int k = 0;
                final Position commandspreadplayers$position2 = new Position();
                for (int l = 0; l < p_110668_14_.length; ++l) {
                    if (j != l) {
                        final Position commandspreadplayers$position3 = p_110668_14_[l];
                        final double d2 = commandspreadplayers$position.func_111099_a(commandspreadplayers$position3);
                        d0 = Math.min(d2, d0);
                        if (d2 < p_110668_2_) {
                            ++k;
                            final Position position = commandspreadplayers$position2;
                            position.field_111101_a += commandspreadplayers$position3.field_111101_a - commandspreadplayers$position.field_111101_a;
                            final Position position2 = commandspreadplayers$position2;
                            position2.field_111100_b += commandspreadplayers$position3.field_111100_b - commandspreadplayers$position.field_111100_b;
                        }
                    }
                }
                if (k > 0) {
                    final Position position3 = commandspreadplayers$position2;
                    position3.field_111101_a /= k;
                    final Position position4 = commandspreadplayers$position2;
                    position4.field_111100_b /= k;
                    final double d3 = commandspreadplayers$position2.func_111096_b();
                    if (d3 > 0.0) {
                        commandspreadplayers$position2.func_111095_a();
                        commandspreadplayers$position.func_111094_b(commandspreadplayers$position2);
                    }
                    else {
                        commandspreadplayers$position.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
                    }
                    flag = true;
                }
                if (commandspreadplayers$position.func_111093_a(p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_)) {
                    flag = true;
                }
            }
            if (!flag) {
                for (final Position commandspreadplayers$position4 : p_110668_14_) {
                    if (!commandspreadplayers$position4.func_111098_b(worldIn)) {
                        commandspreadplayers$position4.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
                        flag = true;
                    }
                }
            }
        }
        if (i >= 10000) {
            throw new CommandException("commands.spreadplayers.failure." + (p_110668_15_ ? "teams" : "players"), new Object[] { p_110668_14_.length, p_110668_1_.field_111101_a, p_110668_1_.field_111100_b, String.format("%.2f", d0) });
        }
        return i;
    }
    
    private double func_110671_a(final List<Entity> p_110671_1_, final World worldIn, final Position[] p_110671_3_, final boolean p_110671_4_) {
        double d0 = 0.0;
        int i = 0;
        final Map<Team, Position> map = (Map<Team, Position>)Maps.newHashMap();
        for (int j = 0; j < p_110671_1_.size(); ++j) {
            final Entity entity = p_110671_1_.get(j);
            Position commandspreadplayers$position;
            if (p_110671_4_) {
                final Team team = (entity instanceof EntityPlayer) ? ((EntityPlayer)entity).getTeam() : null;
                if (!map.containsKey(team)) {
                    map.put(team, p_110671_3_[i++]);
                }
                commandspreadplayers$position = map.get(team);
            }
            else {
                commandspreadplayers$position = p_110671_3_[i++];
            }
            entity.setPositionAndUpdate(MathHelper.floor_double(commandspreadplayers$position.field_111101_a) + 0.5f, commandspreadplayers$position.func_111092_a(worldIn), MathHelper.floor_double(commandspreadplayers$position.field_111100_b) + 0.5);
            double d2 = Double.MAX_VALUE;
            for (int k = 0; k < p_110671_3_.length; ++k) {
                if (commandspreadplayers$position != p_110671_3_[k]) {
                    final double d3 = commandspreadplayers$position.func_111099_a(p_110671_3_[k]);
                    d2 = Math.min(d3, d2);
                }
            }
            d0 += d2;
        }
        d0 /= p_110671_1_.size();
        return d0;
    }
    
    private Position[] func_110670_a(final Random p_110670_1_, final int p_110670_2_, final double p_110670_3_, final double p_110670_5_, final double p_110670_7_, final double p_110670_9_) {
        final Position[] acommandspreadplayers$position = new Position[p_110670_2_];
        for (int i = 0; i < acommandspreadplayers$position.length; ++i) {
            final Position commandspreadplayers$position = new Position();
            commandspreadplayers$position.func_111097_a(p_110670_1_, p_110670_3_, p_110670_5_, p_110670_7_, p_110670_9_);
            acommandspreadplayers$position[i] = commandspreadplayers$position;
        }
        return acommandspreadplayers$position;
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length >= 1 && args.length <= 2) ? CommandBase.func_181043_b(args, 0, pos) : null;
    }
    
    static class Position
    {
        double field_111101_a;
        double field_111100_b;
        
        Position() {
        }
        
        Position(final double p_i1358_1_, final double p_i1358_3_) {
            this.field_111101_a = p_i1358_1_;
            this.field_111100_b = p_i1358_3_;
        }
        
        double func_111099_a(final Position p_111099_1_) {
            final double d0 = this.field_111101_a - p_111099_1_.field_111101_a;
            final double d2 = this.field_111100_b - p_111099_1_.field_111100_b;
            return Math.sqrt(d0 * d0 + d2 * d2);
        }
        
        void func_111095_a() {
            final double d0 = this.func_111096_b();
            this.field_111101_a /= d0;
            this.field_111100_b /= d0;
        }
        
        float func_111096_b() {
            return MathHelper.sqrt_double(this.field_111101_a * this.field_111101_a + this.field_111100_b * this.field_111100_b);
        }
        
        public void func_111094_b(final Position p_111094_1_) {
            this.field_111101_a -= p_111094_1_.field_111101_a;
            this.field_111100_b -= p_111094_1_.field_111100_b;
        }
        
        public boolean func_111093_a(final double p_111093_1_, final double p_111093_3_, final double p_111093_5_, final double p_111093_7_) {
            boolean flag = false;
            if (this.field_111101_a < p_111093_1_) {
                this.field_111101_a = p_111093_1_;
                flag = true;
            }
            else if (this.field_111101_a > p_111093_5_) {
                this.field_111101_a = p_111093_5_;
                flag = true;
            }
            if (this.field_111100_b < p_111093_3_) {
                this.field_111100_b = p_111093_3_;
                flag = true;
            }
            else if (this.field_111100_b > p_111093_7_) {
                this.field_111100_b = p_111093_7_;
                flag = true;
            }
            return flag;
        }
        
        public int func_111092_a(final World worldIn) {
            BlockPos blockpos = new BlockPos(this.field_111101_a, 256.0, this.field_111100_b);
            while (blockpos.getY() > 0) {
                blockpos = blockpos.down();
                if (worldIn.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                    return blockpos.getY() + 1;
                }
            }
            return 257;
        }
        
        public boolean func_111098_b(final World worldIn) {
            BlockPos blockpos = new BlockPos(this.field_111101_a, 256.0, this.field_111100_b);
            while (blockpos.getY() > 0) {
                blockpos = blockpos.down();
                final Material material = worldIn.getBlockState(blockpos).getBlock().getMaterial();
                if (material != Material.air) {
                    return !material.isLiquid() && material != Material.fire;
                }
            }
            return false;
        }
        
        public void func_111097_a(final Random p_111097_1_, final double p_111097_2_, final double p_111097_4_, final double p_111097_6_, final double p_111097_8_) {
            this.field_111101_a = MathHelper.getRandomDoubleInRange(p_111097_1_, p_111097_2_, p_111097_6_);
            this.field_111100_b = MathHelper.getRandomDoubleInRange(p_111097_1_, p_111097_4_, p_111097_8_);
        }
    }
}
