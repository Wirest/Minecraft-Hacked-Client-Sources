package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class CommandScoreboard extends CommandBase
{
    private static final String __OBFID = "CL_00000896";

    public String getCommandName()
    {
        return "scoreboard";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.scoreboard.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (!this.func_175780_b(sender, args))
        {
            if (args.length < 1)
            {
                throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
            }
            else
            {
                if (args[0].equalsIgnoreCase("objectives"))
                {
                    if (args.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }

                    if (args[1].equalsIgnoreCase("list"))
                    {
                        this.listObjectives(sender);
                    }
                    else if (args[1].equalsIgnoreCase("add"))
                    {
                        if (args.length < 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                        }

                        this.addObjective(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("remove"))
                    {
                        if (args.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                        }

                        this.removeObjective(sender, args[2]);
                    }
                    else
                    {
                        if (!args[1].equalsIgnoreCase("setdisplay"))
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                        }

                        if (args.length != 3 && args.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                        }

                        this.setObjectiveDisplay(sender, args, 2);
                    }
                }
                else if (args[0].equalsIgnoreCase("players"))
                {
                    if (args.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }

                    if (args[1].equalsIgnoreCase("list"))
                    {
                        if (args.length > 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                        }

                        this.listPlayers(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("add"))
                    {
                        if (args.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                        }

                        this.setPlayer(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("remove"))
                    {
                        if (args.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                        }

                        this.setPlayer(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("set"))
                    {
                        if (args.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                        }

                        this.setPlayer(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("reset"))
                    {
                        if (args.length != 3 && args.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                        }

                        this.resetPlayers(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("enable"))
                    {
                        if (args.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
                        }

                        this.func_175779_n(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("test"))
                    {
                        if (args.length != 5 && args.length != 6)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
                        }

                        this.func_175781_o(sender, args, 2);
                    }
                    else
                    {
                        if (!args[1].equalsIgnoreCase("operation"))
                        {
                            throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                        }

                        if (args.length != 7)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
                        }

                        this.func_175778_p(sender, args, 2);
                    }
                }
                else if (args[0].equalsIgnoreCase("teams"))
                {
                    if (args.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }

                    if (args[1].equalsIgnoreCase("list"))
                    {
                        if (args.length > 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                        }

                        this.listTeams(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("add"))
                    {
                        if (args.length < 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                        }

                        this.addTeam(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("remove"))
                    {
                        if (args.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                        }

                        this.removeTeam(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("empty"))
                    {
                        if (args.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                        }

                        this.emptyTeam(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("join"))
                    {
                        if (args.length < 4 && (args.length != 3 || !(sender instanceof EntityPlayer)))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                        }

                        this.joinTeam(sender, args, 2);
                    }
                    else if (args[1].equalsIgnoreCase("leave"))
                    {
                        if (args.length < 3 && !(sender instanceof EntityPlayer))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                        }

                        this.leaveTeam(sender, args, 2);
                    }
                    else
                    {
                        if (!args[1].equalsIgnoreCase("option"))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                        }

                        if (args.length != 4 && args.length != 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                        }

                        this.setTeamOption(sender, args, 2);
                    }
                }
            }
        }
    }

    private boolean func_175780_b(ICommandSender p_175780_1_, String[] p_175780_2_) throws CommandException
    {
        int var3 = -1;

        for (int var4 = 0; var4 < p_175780_2_.length; ++var4)
        {
            if (this.isUsernameIndex(p_175780_2_, var4) && "*".equals(p_175780_2_[var4]))
            {
                if (var3 >= 0)
                {
                    throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
                }

                var3 = var4;
            }
        }

        if (var3 < 0)
        {
            return false;
        }
        else
        {
            ArrayList var12 = Lists.newArrayList(this.getScoreboard().getObjectiveNames());
            String var5 = p_175780_2_[var3];
            ArrayList var6 = Lists.newArrayList();
            Iterator var7 = var12.iterator();

            while (var7.hasNext())
            {
                String var8 = (String)var7.next();
                p_175780_2_[var3] = var8;

                try
                {
                    this.processCommand(p_175780_1_, p_175780_2_);
                    var6.add(var8);
                }
                catch (CommandException var11)
                {
                    ChatComponentTranslation var10 = new ChatComponentTranslation(var11.getMessage(), var11.getErrorOjbects());
                    var10.getChatStyle().setColor(EnumChatFormatting.RED);
                    p_175780_1_.addChatMessage(var10);
                }
            }

            p_175780_2_[var3] = var5;
            p_175780_1_.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, var6.size());

            if (var6.size() == 0)
            {
                throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
            }
            else
            {
                return true;
            }
        }
    }

    protected Scoreboard getScoreboard()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
    }

    protected ScoreObjective func_147189_a(String name, boolean edit) throws CommandException
    {
        Scoreboard var3 = this.getScoreboard();
        ScoreObjective var4 = var3.getObjective(name);

        if (var4 == null)
        {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] {name});
        }
        else if (edit && var4.getCriteria().isReadOnly())
        {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] {name});
        }
        else
        {
            return var4;
        }
    }

    protected ScorePlayerTeam func_147183_a(String name) throws CommandException
    {
        Scoreboard var2 = this.getScoreboard();
        ScorePlayerTeam var3 = var2.getTeam(name);

        if (var3 == null)
        {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] {name});
        }
        else
        {
            return var3;
        }
    }

    protected void addObjective(ICommandSender sender, String[] args, int index) throws CommandException
    {
        String var4 = args[index++];
        String var5 = args[index++];
        Scoreboard var6 = this.getScoreboard();
        IScoreObjectiveCriteria var7 = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.INSTANCES.get(var5);

        if (var7 == null)
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] {var5});
        }
        else if (var6.getObjective(var4) != null)
        {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] {var4});
        }
        else if (var4.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] {var4, Integer.valueOf(16)});
        }
        else if (var4.length() == 0)
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        }
        else
        {
            if (args.length > index)
            {
                String var8 = getChatComponentFromNthArg(sender, args, index).getUnformattedText();

                if (var8.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] {var8, Integer.valueOf(32)});
                }

                if (var8.length() > 0)
                {
                    var6.addScoreObjective(var4, var7).setDisplayName(var8);
                }
                else
                {
                    var6.addScoreObjective(var4, var7);
                }
            }
            else
            {
                var6.addScoreObjective(var4, var7);
            }

            notifyOperators(sender, this, "commands.scoreboard.objectives.add.success", new Object[] {var4});
        }
    }

    protected void addTeam(ICommandSender p_147185_1_, String[] p_147185_2_, int p_147185_3_) throws CommandException
    {
        String var4 = p_147185_2_[p_147185_3_++];
        Scoreboard var5 = this.getScoreboard();

        if (var5.getTeam(var4) != null)
        {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] {var4});
        }
        else if (var4.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] {var4, Integer.valueOf(16)});
        }
        else if (var4.length() == 0)
        {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        }
        else
        {
            if (p_147185_2_.length > p_147185_3_)
            {
                String var6 = getChatComponentFromNthArg(p_147185_1_, p_147185_2_, p_147185_3_).getUnformattedText();

                if (var6.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] {var6, Integer.valueOf(32)});
                }

                if (var6.length() > 0)
                {
                    var5.createTeam(var4).setTeamName(var6);
                }
                else
                {
                    var5.createTeam(var4);
                }
            }
            else
            {
                var5.createTeam(var4);
            }

            notifyOperators(p_147185_1_, this, "commands.scoreboard.teams.add.success", new Object[] {var4});
        }
    }

    protected void setTeamOption(ICommandSender p_147200_1_, String[] p_147200_2_, int p_147200_3_) throws CommandException
    {
        ScorePlayerTeam var4 = this.func_147183_a(p_147200_2_[p_147200_3_++]);

        if (var4 != null)
        {
            String var5 = p_147200_2_[p_147200_3_++].toLowerCase();

            if (!var5.equalsIgnoreCase("color") && !var5.equalsIgnoreCase("friendlyfire") && !var5.equalsIgnoreCase("seeFriendlyInvisibles") && !var5.equalsIgnoreCase("nametagVisibility") && !var5.equalsIgnoreCase("deathMessageVisibility"))
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            else if (p_147200_2_.length == 4)
            {
                if (var5.equalsIgnoreCase("color"))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false))});
                }
                else if (!var5.equalsIgnoreCase("friendlyfire") && !var5.equalsIgnoreCase("seeFriendlyInvisibles"))
                {
                    if (!var5.equalsIgnoreCase("nametagVisibility") && !var5.equalsIgnoreCase("deathMessageVisibility"))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                    }
                    else
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceString(Team.EnumVisible.func_178825_a())});
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(Arrays.asList(new String[]{"true", "false"}))});
                }
            }
            else
            {
                String var6 = p_147200_2_[p_147200_3_];

                if (var5.equalsIgnoreCase("color"))
                {
                    EnumChatFormatting var7 = EnumChatFormatting.getValueByName(var6);

                    if (var7 == null || var7.isFancyStyling())
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false))});
                    }

                    var4.func_178774_a(var7);
                    var4.setNamePrefix(var7.toString());
                    var4.setNameSuffix(EnumChatFormatting.RESET.toString());
                }
                else if (var5.equalsIgnoreCase("friendlyfire"))
                {
                    if (!var6.equalsIgnoreCase("true") && !var6.equalsIgnoreCase("false"))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(Arrays.asList(new String[]{"true", "false"}))});
                    }

                    var4.setAllowFriendlyFire(var6.equalsIgnoreCase("true"));
                }
                else if (var5.equalsIgnoreCase("seeFriendlyInvisibles"))
                {
                    if (!var6.equalsIgnoreCase("true") && !var6.equalsIgnoreCase("false"))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(Arrays.asList(new String[]{"true", "false"}))});
                    }

                    var4.setSeeFriendlyInvisiblesEnabled(var6.equalsIgnoreCase("true"));
                }
                else
                {
                    Team.EnumVisible var8;

                    if (var5.equalsIgnoreCase("nametagVisibility"))
                    {
                        var8 = Team.EnumVisible.func_178824_a(var6);

                        if (var8 == null)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceString(Team.EnumVisible.func_178825_a())});
                        }

                        var4.func_178772_a(var8);
                    }
                    else if (var5.equalsIgnoreCase("deathMessageVisibility"))
                    {
                        var8 = Team.EnumVisible.func_178824_a(var6);

                        if (var8 == null)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceString(Team.EnumVisible.func_178825_a())});
                        }

                        var4.func_178773_b(var8);
                    }
                }

                notifyOperators(p_147200_1_, this, "commands.scoreboard.teams.option.success", new Object[] {var5, var4.getRegisteredName(), var6});
            }
        }
    }

    protected void removeTeam(ICommandSender p_147194_1_, String[] p_147194_2_, int p_147194_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        ScorePlayerTeam var5 = this.func_147183_a(p_147194_2_[p_147194_3_]);

        if (var5 != null)
        {
            var4.removeTeam(var5);
            notifyOperators(p_147194_1_, this, "commands.scoreboard.teams.remove.success", new Object[] {var5.getRegisteredName()});
        }
    }

    protected void listTeams(ICommandSender p_147186_1_, String[] p_147186_2_, int p_147186_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();

        if (p_147186_2_.length > p_147186_3_)
        {
            ScorePlayerTeam var5 = this.func_147183_a(p_147186_2_[p_147186_3_]);

            if (var5 == null)
            {
                return;
            }

            Collection var6 = var5.getMembershipCollection();
            p_147186_1_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var6.size());

            if (var6.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] {var5.getRegisteredName()});
            }

            ChatComponentTranslation var7 = new ChatComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] {Integer.valueOf(var6.size()), var5.getRegisteredName()});
            var7.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147186_1_.addChatMessage(var7);
            p_147186_1_.addChatMessage(new ChatComponentText(joinNiceString(var6.toArray())));
        }
        else
        {
            Collection var9 = var4.getTeams();
            p_147186_1_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var9.size());

            if (var9.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }

            ChatComponentTranslation var10 = new ChatComponentTranslation("commands.scoreboard.teams.list.count", new Object[] {Integer.valueOf(var9.size())});
            var10.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147186_1_.addChatMessage(var10);
            Iterator var11 = var9.iterator();

            while (var11.hasNext())
            {
                ScorePlayerTeam var8 = (ScorePlayerTeam)var11.next();
                p_147186_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] {var8.getRegisteredName(), var8.func_96669_c(), Integer.valueOf(var8.getMembershipCollection().size())}));
            }
        }
    }

    protected void joinTeam(ICommandSender p_147190_1_, String[] p_147190_2_, int p_147190_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = p_147190_2_[p_147190_3_++];
        HashSet var6 = Sets.newHashSet();
        HashSet var7 = Sets.newHashSet();
        String var8;

        if (p_147190_1_ instanceof EntityPlayer && p_147190_3_ == p_147190_2_.length)
        {
            var8 = getCommandSenderAsPlayer(p_147190_1_).getName();

            if (var4.func_151392_a(var8, var5))
            {
                var6.add(var8);
            }
            else
            {
                var7.add(var8);
            }
        }
        else
        {
            while (p_147190_3_ < p_147190_2_.length)
            {
                var8 = p_147190_2_[p_147190_3_++];

                if (var8.startsWith("@"))
                {
                    List var13 = func_175763_c(p_147190_1_, var8);
                    Iterator var10 = var13.iterator();

                    while (var10.hasNext())
                    {
                        Entity var11 = (Entity)var10.next();
                        String var12 = func_175758_e(p_147190_1_, var11.getUniqueID().toString());

                        if (var4.func_151392_a(var12, var5))
                        {
                            var6.add(var12);
                        }
                        else
                        {
                            var7.add(var12);
                        }
                    }
                }
                else
                {
                    String var9 = func_175758_e(p_147190_1_, var8);

                    if (var4.func_151392_a(var9, var5))
                    {
                        var6.add(var9);
                    }
                    else
                    {
                        var7.add(var9);
                    }
                }
            }
        }

        if (!var6.isEmpty())
        {
            p_147190_1_.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, var6.size());
            notifyOperators(p_147190_1_, this, "commands.scoreboard.teams.join.success", new Object[] {Integer.valueOf(var6.size()), var5, joinNiceString(var6.toArray(new String[0]))});
        }

        if (!var7.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] {Integer.valueOf(var7.size()), var5, joinNiceString(var7.toArray(new String[0]))});
        }
    }

    protected void leaveTeam(ICommandSender p_147199_1_, String[] p_147199_2_, int p_147199_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        HashSet var5 = Sets.newHashSet();
        HashSet var6 = Sets.newHashSet();
        String var7;

        if (p_147199_1_ instanceof EntityPlayer && p_147199_3_ == p_147199_2_.length)
        {
            var7 = getCommandSenderAsPlayer(p_147199_1_).getName();

            if (var4.removePlayerFromTeams(var7))
            {
                var5.add(var7);
            }
            else
            {
                var6.add(var7);
            }
        }
        else
        {
            while (p_147199_3_ < p_147199_2_.length)
            {
                var7 = p_147199_2_[p_147199_3_++];

                if (var7.startsWith("@"))
                {
                    List var12 = func_175763_c(p_147199_1_, var7);
                    Iterator var9 = var12.iterator();

                    while (var9.hasNext())
                    {
                        Entity var10 = (Entity)var9.next();
                        String var11 = func_175758_e(p_147199_1_, var10.getUniqueID().toString());

                        if (var4.removePlayerFromTeams(var11))
                        {
                            var5.add(var11);
                        }
                        else
                        {
                            var6.add(var11);
                        }
                    }
                }
                else
                {
                    String var8 = func_175758_e(p_147199_1_, var7);

                    if (var4.removePlayerFromTeams(var8))
                    {
                        var5.add(var8);
                    }
                    else
                    {
                        var6.add(var8);
                    }
                }
            }
        }

        if (!var5.isEmpty())
        {
            p_147199_1_.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, var5.size());
            notifyOperators(p_147199_1_, this, "commands.scoreboard.teams.leave.success", new Object[] {Integer.valueOf(var5.size()), joinNiceString(var5.toArray(new String[0]))});
        }

        if (!var6.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] {Integer.valueOf(var6.size()), joinNiceString(var6.toArray(new String[0]))});
        }
    }

    protected void emptyTeam(ICommandSender p_147188_1_, String[] p_147188_2_, int p_147188_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        ScorePlayerTeam var5 = this.func_147183_a(p_147188_2_[p_147188_3_]);

        if (var5 != null)
        {
            ArrayList var6 = Lists.newArrayList(var5.getMembershipCollection());
            p_147188_1_.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, var6.size());

            if (var6.isEmpty())
            {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] {var5.getRegisteredName()});
            }
            else
            {
                Iterator var7 = var6.iterator();

                while (var7.hasNext())
                {
                    String var8 = (String)var7.next();
                    var4.removePlayerFromTeam(var8, var5);
                }

                notifyOperators(p_147188_1_, this, "commands.scoreboard.teams.empty.success", new Object[] {Integer.valueOf(var6.size()), var5.getRegisteredName()});
            }
        }
    }

    protected void removeObjective(ICommandSender p_147191_1_, String p_147191_2_) throws CommandException
    {
        Scoreboard var3 = this.getScoreboard();
        ScoreObjective var4 = this.func_147189_a(p_147191_2_, false);
        var3.func_96519_k(var4);
        notifyOperators(p_147191_1_, this, "commands.scoreboard.objectives.remove.success", new Object[] {p_147191_2_});
    }

    protected void listObjectives(ICommandSender p_147196_1_) throws CommandException
    {
        Scoreboard var2 = this.getScoreboard();
        Collection var3 = var2.getScoreObjectives();

        if (var3.size() <= 0)
        {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        }
        else
        {
            ChatComponentTranslation var4 = new ChatComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] {Integer.valueOf(var3.size())});
            var4.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147196_1_.addChatMessage(var4);
            Iterator var5 = var3.iterator();

            while (var5.hasNext())
            {
                ScoreObjective var6 = (ScoreObjective)var5.next();
                p_147196_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] {var6.getName(), var6.getDisplayName(), var6.getCriteria().getName()}));
            }
        }
    }

    protected void setObjectiveDisplay(ICommandSender p_147198_1_, String[] p_147198_2_, int p_147198_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = p_147198_2_[p_147198_3_++];
        int var6 = Scoreboard.getObjectiveDisplaySlotNumber(var5);
        ScoreObjective var7 = null;

        if (p_147198_2_.length == 4)
        {
            var7 = this.func_147189_a(p_147198_2_[p_147198_3_], false);
        }

        if (var6 < 0)
        {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] {var5});
        }
        else
        {
            var4.setObjectiveInDisplaySlot(var6, var7);

            if (var7 != null)
            {
                notifyOperators(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] {Scoreboard.getObjectiveDisplaySlot(var6), var7.getName()});
            }
            else
            {
                notifyOperators(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] {Scoreboard.getObjectiveDisplaySlot(var6)});
            }
        }
    }

    protected void listPlayers(ICommandSender p_147195_1_, String[] p_147195_2_, int p_147195_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();

        if (p_147195_2_.length > p_147195_3_)
        {
            String var5 = func_175758_e(p_147195_1_, p_147195_2_[p_147195_3_]);
            Map var6 = var4.func_96510_d(var5);
            p_147195_1_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var6.size());

            if (var6.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] {var5});
            }

            ChatComponentTranslation var7 = new ChatComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] {Integer.valueOf(var6.size()), var5});
            var7.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147195_1_.addChatMessage(var7);
            Iterator var8 = var6.values().iterator();

            while (var8.hasNext())
            {
                Score var9 = (Score)var8.next();
                p_147195_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] {Integer.valueOf(var9.getScorePoints()), var9.getObjective().getDisplayName(), var9.getObjective().getName()}));
            }
        }
        else
        {
            Collection var10 = var4.getObjectiveNames();
            p_147195_1_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var10.size());

            if (var10.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }

            ChatComponentTranslation var11 = new ChatComponentTranslation("commands.scoreboard.players.list.count", new Object[] {Integer.valueOf(var10.size())});
            var11.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147195_1_.addChatMessage(var11);
            p_147195_1_.addChatMessage(new ChatComponentText(joinNiceString(var10.toArray())));
        }
    }

    protected void setPlayer(ICommandSender p_147197_1_, String[] p_147197_2_, int p_147197_3_) throws CommandException
    {
        String var4 = p_147197_2_[p_147197_3_ - 1];
        int var5 = p_147197_3_;
        String var6 = func_175758_e(p_147197_1_, p_147197_2_[p_147197_3_++]);
        ScoreObjective var7 = this.func_147189_a(p_147197_2_[p_147197_3_++], true);
        int var8 = var4.equalsIgnoreCase("set") ? parseInt(p_147197_2_[p_147197_3_++]) : parseInt(p_147197_2_[p_147197_3_++], 0);

        if (p_147197_2_.length > p_147197_3_)
        {
            Entity var9 = func_175768_b(p_147197_1_, p_147197_2_[var5]);

            try
            {
                NBTTagCompound var10 = JsonToNBT.func_180713_a(func_180529_a(p_147197_2_, p_147197_3_));
                NBTTagCompound var11 = new NBTTagCompound();
                var9.writeToNBT(var11);

                if (!CommandTestForBlock.func_175775_a(var10, var11, true))
                {
                    throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] {var6});
                }
            }
            catch (NBTException var12)
            {
                throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] {var12.getMessage()});
            }
        }

        Scoreboard var13 = this.getScoreboard();
        Score var14 = var13.getValueFromObjective(var6, var7);

        if (var4.equalsIgnoreCase("set"))
        {
            var14.setScorePoints(var8);
        }
        else if (var4.equalsIgnoreCase("add"))
        {
            var14.increseScore(var8);
        }
        else
        {
            var14.decreaseScore(var8);
        }

        notifyOperators(p_147197_1_, this, "commands.scoreboard.players.set.success", new Object[] {var7.getName(), var6, Integer.valueOf(var14.getScorePoints())});
    }

    protected void resetPlayers(ICommandSender p_147187_1_, String[] p_147187_2_, int p_147187_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = func_175758_e(p_147187_1_, p_147187_2_[p_147187_3_++]);

        if (p_147187_2_.length > p_147187_3_)
        {
            ScoreObjective var6 = this.func_147189_a(p_147187_2_[p_147187_3_++], false);
            var4.func_178822_d(var5, var6);
            notifyOperators(p_147187_1_, this, "commands.scoreboard.players.resetscore.success", new Object[] {var6.getName(), var5});
        }
        else
        {
            var4.func_178822_d(var5, (ScoreObjective)null);
            notifyOperators(p_147187_1_, this, "commands.scoreboard.players.reset.success", new Object[] {var5});
        }
    }

    protected void func_175779_n(ICommandSender p_175779_1_, String[] p_175779_2_, int p_175779_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = getPlayerName(p_175779_1_, p_175779_2_[p_175779_3_++]);
        ScoreObjective var6 = this.func_147189_a(p_175779_2_[p_175779_3_], false);

        if (var6.getCriteria() != IScoreObjectiveCriteria.field_178791_c)
        {
            throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] {var6.getName()});
        }
        else
        {
            Score var7 = var4.getValueFromObjective(var5, var6);
            var7.func_178815_a(false);
            notifyOperators(p_175779_1_, this, "commands.scoreboard.players.enable.success", new Object[] {var6.getName(), var5});
        }
    }

    protected void func_175781_o(ICommandSender p_175781_1_, String[] p_175781_2_, int p_175781_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = func_175758_e(p_175781_1_, p_175781_2_[p_175781_3_++]);
        ScoreObjective var6 = this.func_147189_a(p_175781_2_[p_175781_3_++], false);

        if (!var4.func_178819_b(var5, var6))
        {
            throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] {var6.getName(), var5});
        }
        else
        {
            int var7 = p_175781_2_[p_175781_3_].equals("*") ? Integer.MIN_VALUE : parseInt(p_175781_2_[p_175781_3_]);
            ++p_175781_3_;
            int var8 = p_175781_3_ < p_175781_2_.length && !p_175781_2_[p_175781_3_].equals("*") ? parseInt(p_175781_2_[p_175781_3_], var7) : Integer.MAX_VALUE;
            Score var9 = var4.getValueFromObjective(var5, var6);

            if (var9.getScorePoints() >= var7 && var9.getScorePoints() <= var8)
            {
                notifyOperators(p_175781_1_, this, "commands.scoreboard.players.test.success", new Object[] {Integer.valueOf(var9.getScorePoints()), Integer.valueOf(var7), Integer.valueOf(var8)});
            }
            else
            {
                throw new CommandException("commands.scoreboard.players.test.failed", new Object[] {Integer.valueOf(var9.getScorePoints()), Integer.valueOf(var7), Integer.valueOf(var8)});
            }
        }
    }

    protected void func_175778_p(ICommandSender p_175778_1_, String[] p_175778_2_, int p_175778_3_) throws CommandException
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = func_175758_e(p_175778_1_, p_175778_2_[p_175778_3_++]);
        ScoreObjective var6 = this.func_147189_a(p_175778_2_[p_175778_3_++], true);
        String var7 = p_175778_2_[p_175778_3_++];
        String var8 = func_175758_e(p_175778_1_, p_175778_2_[p_175778_3_++]);
        ScoreObjective var9 = this.func_147189_a(p_175778_2_[p_175778_3_], false);
        Score var10 = var4.getValueFromObjective(var5, var6);

        if (!var4.func_178819_b(var8, var9))
        {
            throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] {var9.getName(), var8});
        }
        else
        {
            Score var11 = var4.getValueFromObjective(var8, var9);

            if (var7.equals("+="))
            {
                var10.setScorePoints(var10.getScorePoints() + var11.getScorePoints());
            }
            else if (var7.equals("-="))
            {
                var10.setScorePoints(var10.getScorePoints() - var11.getScorePoints());
            }
            else if (var7.equals("*="))
            {
                var10.setScorePoints(var10.getScorePoints() * var11.getScorePoints());
            }
            else if (var7.equals("/="))
            {
                if (var11.getScorePoints() != 0)
                {
                    var10.setScorePoints(var10.getScorePoints() / var11.getScorePoints());
                }
            }
            else if (var7.equals("%="))
            {
                if (var11.getScorePoints() != 0)
                {
                    var10.setScorePoints(var10.getScorePoints() % var11.getScorePoints());
                }
            }
            else if (var7.equals("="))
            {
                var10.setScorePoints(var11.getScorePoints());
            }
            else if (var7.equals("<"))
            {
                var10.setScorePoints(Math.min(var10.getScorePoints(), var11.getScorePoints()));
            }
            else if (var7.equals(">"))
            {
                var10.setScorePoints(Math.max(var10.getScorePoints(), var11.getScorePoints()));
            }
            else
            {
                if (!var7.equals("><"))
                {
                    throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] {var7});
                }

                int var12 = var10.getScorePoints();
                var10.setScorePoints(var11.getScorePoints());
                var11.setScorePoints(var12);
            }

            notifyOperators(p_175778_1_, this, "commands.scoreboard.players.operation.success", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[] {"objectives", "players", "teams"});
        }
        else
        {
            if (args[0].equalsIgnoreCase("objectives"))
            {
                if (args.length == 2)
                {
                    return getListOfStringsMatchingLastWord(args, new String[] {"list", "add", "remove", "setdisplay"});
                }

                if (args[1].equalsIgnoreCase("add"))
                {
                    if (args.length == 4)
                    {
                        Set var4 = IScoreObjectiveCriteria.INSTANCES.keySet();
                        return func_175762_a(args, var4);
                    }
                }
                else if (args[1].equalsIgnoreCase("remove"))
                {
                    if (args.length == 3)
                    {
                        return func_175762_a(args, this.func_147184_a(false));
                    }
                }
                else if (args[1].equalsIgnoreCase("setdisplay"))
                {
                    if (args.length == 3)
                    {
                        return getListOfStringsMatchingLastWord(args, Scoreboard.func_178821_h());
                    }

                    if (args.length == 4)
                    {
                        return func_175762_a(args, this.func_147184_a(false));
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("players"))
            {
                if (args.length == 2)
                {
                    return getListOfStringsMatchingLastWord(args, new String[] {"set", "add", "remove", "reset", "list", "enable", "test", "operation"});
                }

                if (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove") && !args[1].equalsIgnoreCase("reset"))
                {
                    if (args[1].equalsIgnoreCase("enable"))
                    {
                        if (args.length == 3)
                        {
                            return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                        }

                        if (args.length == 4)
                        {
                            return func_175762_a(args, this.func_175782_e());
                        }
                    }
                    else if (!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("test"))
                    {
                        if (args[1].equalsIgnoreCase("operation"))
                        {
                            if (args.length == 3)
                            {
                                return func_175762_a(args, this.getScoreboard().getObjectiveNames());
                            }

                            if (args.length == 4)
                            {
                                return func_175762_a(args, this.func_147184_a(true));
                            }

                            if (args.length == 5)
                            {
                                return getListOfStringsMatchingLastWord(args, new String[] {"+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><"});
                            }

                            if (args.length == 6)
                            {
                                return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                            }

                            if (args.length == 7)
                            {
                                return func_175762_a(args, this.func_147184_a(false));
                            }
                        }
                    }
                    else
                    {
                        if (args.length == 3)
                        {
                            return func_175762_a(args, this.getScoreboard().getObjectiveNames());
                        }

                        if (args.length == 4 && args[1].equalsIgnoreCase("test"))
                        {
                            return func_175762_a(args, this.func_147184_a(false));
                        }
                    }
                }
                else
                {
                    if (args.length == 3)
                    {
                        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                    }

                    if (args.length == 4)
                    {
                        return func_175762_a(args, this.func_147184_a(true));
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("teams"))
            {
                if (args.length == 2)
                {
                    return getListOfStringsMatchingLastWord(args, new String[] {"add", "remove", "join", "leave", "empty", "list", "option"});
                }

                if (args[1].equalsIgnoreCase("join"))
                {
                    if (args.length == 3)
                    {
                        return func_175762_a(args, this.getScoreboard().getTeamNames());
                    }

                    if (args.length >= 4)
                    {
                        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                    }
                }
                else
                {
                    if (args[1].equalsIgnoreCase("leave"))
                    {
                        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                    }

                    if (!args[1].equalsIgnoreCase("empty") && !args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("remove"))
                    {
                        if (args[1].equalsIgnoreCase("option"))
                        {
                            if (args.length == 3)
                            {
                                return func_175762_a(args, this.getScoreboard().getTeamNames());
                            }

                            if (args.length == 4)
                            {
                                return getListOfStringsMatchingLastWord(args, new String[] {"color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility"});
                            }

                            if (args.length == 5)
                            {
                                if (args[3].equalsIgnoreCase("color"))
                                {
                                    return func_175762_a(args, EnumChatFormatting.getValidValues(true, false));
                                }

                                if (args[3].equalsIgnoreCase("nametagVisibility") || args[3].equalsIgnoreCase("deathMessageVisibility"))
                                {
                                    return getListOfStringsMatchingLastWord(args, Team.EnumVisible.func_178825_a());
                                }

                                if (args[3].equalsIgnoreCase("friendlyfire") || args[3].equalsIgnoreCase("seeFriendlyInvisibles"))
                                {
                                    return getListOfStringsMatchingLastWord(args, new String[] {"true", "false"});
                                }
                            }
                        }
                    }
                    else if (args.length == 3)
                    {
                        return func_175762_a(args, this.getScoreboard().getTeamNames());
                    }
                }
            }

            return null;
        }
    }

    protected List func_147184_a(boolean p_147184_1_)
    {
        Collection var2 = this.getScoreboard().getScoreObjectives();
        ArrayList var3 = Lists.newArrayList();
        Iterator var4 = var2.iterator();

        while (var4.hasNext())
        {
            ScoreObjective var5 = (ScoreObjective)var4.next();

            if (!p_147184_1_ || !var5.getCriteria().isReadOnly())
            {
                var3.add(var5.getName());
            }
        }

        return var3;
    }

    protected List func_175782_e()
    {
        Collection var1 = this.getScoreboard().getScoreObjectives();
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = var1.iterator();

        while (var3.hasNext())
        {
            ScoreObjective var4 = (ScoreObjective)var3.next();

            if (var4.getCriteria() == IScoreObjectiveCriteria.field_178791_c)
            {
                var2.add(var4.getName());
            }
        }

        return var2;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return !args[0].equalsIgnoreCase("players") ? (args[0].equalsIgnoreCase("teams") ? index == 2 : false) : (args.length > 1 && args[1].equalsIgnoreCase("operation") ? index == 2 || index == 5 : index == 2);
    }
}
