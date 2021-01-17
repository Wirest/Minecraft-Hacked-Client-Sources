// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.JsonToNBT;
import java.util.Map;
import net.minecraft.scoreboard.Score;
import java.util.Set;
import net.minecraft.entity.Entity;
import com.google.common.collect.Sets;
import net.minecraft.util.ChatComponentText;
import net.minecraft.scoreboard.Team;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.command.ICommand;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.scoreboard.Scoreboard;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandResultStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandScoreboard extends CommandBase
{
    @Override
    public String getCommandName() {
        return "scoreboard";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.scoreboard.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (!this.func_175780_b(sender, args)) {
            if (args.length < 1) {
                throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
            }
            if (args[0].equalsIgnoreCase("objectives")) {
                if (args.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                }
                if (args[1].equalsIgnoreCase("list")) {
                    this.listObjectives(sender);
                }
                else if (args[1].equalsIgnoreCase("add")) {
                    if (args.length < 4) {
                        throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                    }
                    this.addObjective(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("remove")) {
                    if (args.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                    }
                    this.removeObjective(sender, args[2]);
                }
                else {
                    if (!args[1].equalsIgnoreCase("setdisplay")) {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }
                    if (args.length != 3 && args.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                    }
                    this.setObjectiveDisplay(sender, args, 2);
                }
            }
            else if (args[0].equalsIgnoreCase("players")) {
                if (args.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                }
                if (args[1].equalsIgnoreCase("list")) {
                    if (args.length > 3) {
                        throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                    }
                    this.listPlayers(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("add")) {
                    if (args.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                    }
                    this.setPlayer(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("remove")) {
                    if (args.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                    }
                    this.setPlayer(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("set")) {
                    if (args.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                    }
                    this.setPlayer(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("reset")) {
                    if (args.length != 3 && args.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                    }
                    this.resetPlayers(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("enable")) {
                    if (args.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
                    }
                    this.func_175779_n(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("test")) {
                    if (args.length != 5 && args.length != 6) {
                        throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
                    }
                    this.func_175781_o(sender, args, 2);
                }
                else {
                    if (!args[1].equalsIgnoreCase("operation")) {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }
                    if (args.length != 7) {
                        throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
                    }
                    this.func_175778_p(sender, args, 2);
                }
            }
            else {
                if (!args[0].equalsIgnoreCase("teams")) {
                    throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
                }
                if (args.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                }
                if (args[1].equalsIgnoreCase("list")) {
                    if (args.length > 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                    }
                    this.listTeams(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("add")) {
                    if (args.length < 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                    }
                    this.addTeam(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("remove")) {
                    if (args.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                    }
                    this.removeTeam(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("empty")) {
                    if (args.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                    }
                    this.emptyTeam(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("join")) {
                    if (args.length < 4 && (args.length != 3 || !(sender instanceof EntityPlayer))) {
                        throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                    }
                    this.joinTeam(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("leave")) {
                    if (args.length < 3 && !(sender instanceof EntityPlayer)) {
                        throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                    }
                    this.leaveTeam(sender, args, 2);
                }
                else {
                    if (!args[1].equalsIgnoreCase("option")) {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }
                    if (args.length != 4 && args.length != 5) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                    }
                    this.setTeamOption(sender, args, 2);
                }
            }
        }
    }
    
    private boolean func_175780_b(final ICommandSender p_175780_1_, final String[] p_175780_2_) throws CommandException {
        int i = -1;
        for (int j = 0; j < p_175780_2_.length; ++j) {
            if (this.isUsernameIndex(p_175780_2_, j) && "*".equals(p_175780_2_[j])) {
                if (i >= 0) {
                    throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
                }
                i = j;
            }
        }
        if (i < 0) {
            return false;
        }
        final List<String> list1 = (List<String>)Lists.newArrayList((Iterable<?>)this.getScoreboard().getObjectiveNames());
        final String s = p_175780_2_[i];
        final List<String> list2 = (List<String>)Lists.newArrayList();
        for (final String s2 : list1) {
            p_175780_2_[i] = s2;
            try {
                this.processCommand(p_175780_1_, p_175780_2_);
                list2.add(s2);
            }
            catch (CommandException commandexception) {
                final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
                chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
                p_175780_1_.addChatMessage(chatcomponenttranslation);
            }
        }
        p_175780_2_[i] = s;
        p_175780_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list2.size());
        if (list2.size() == 0) {
            throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
        }
        return true;
    }
    
    protected Scoreboard getScoreboard() {
        return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
    }
    
    protected ScoreObjective getObjective(final String name, final boolean edit) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final ScoreObjective scoreobjective = scoreboard.getObjective(name);
        if (scoreobjective == null) {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] { name });
        }
        if (edit && scoreobjective.getCriteria().isReadOnly()) {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] { name });
        }
        return scoreobjective;
    }
    
    protected ScorePlayerTeam getTeam(final String name) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final ScorePlayerTeam scoreplayerteam = scoreboard.getTeam(name);
        if (scoreplayerteam == null) {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] { name });
        }
        return scoreplayerteam;
    }
    
    protected void addObjective(final ICommandSender sender, final String[] args, int index) throws CommandException {
        final String s = args[index++];
        final String s2 = args[index++];
        final Scoreboard scoreboard = this.getScoreboard();
        final IScoreObjectiveCriteria iscoreobjectivecriteria = IScoreObjectiveCriteria.INSTANCES.get(s2);
        if (iscoreobjectivecriteria == null) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] { s2 });
        }
        if (scoreboard.getObjective(s) != null) {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] { s });
        }
        if (s.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] { s, 16 });
        }
        if (s.length() == 0) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        }
        if (args.length > index) {
            final String s3 = CommandBase.getChatComponentFromNthArg(sender, args, index).getUnformattedText();
            if (s3.length() > 32) {
                throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] { s3, 32 });
            }
            if (s3.length() > 0) {
                scoreboard.addScoreObjective(s, iscoreobjectivecriteria).setDisplayName(s3);
            }
            else {
                scoreboard.addScoreObjective(s, iscoreobjectivecriteria);
            }
        }
        else {
            scoreboard.addScoreObjective(s, iscoreobjectivecriteria);
        }
        CommandBase.notifyOperators(sender, this, "commands.scoreboard.objectives.add.success", s);
    }
    
    protected void addTeam(final ICommandSender sender, final String[] args, int index) throws CommandException {
        final String s = args[index++];
        final Scoreboard scoreboard = this.getScoreboard();
        if (scoreboard.getTeam(s) != null) {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] { s });
        }
        if (s.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] { s, 16 });
        }
        if (s.length() == 0) {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        }
        if (args.length > index) {
            final String s2 = CommandBase.getChatComponentFromNthArg(sender, args, index).getUnformattedText();
            if (s2.length() > 32) {
                throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] { s2, 32 });
            }
            if (s2.length() > 0) {
                scoreboard.createTeam(s).setTeamName(s2);
            }
            else {
                scoreboard.createTeam(s);
            }
        }
        else {
            scoreboard.createTeam(s);
        }
        CommandBase.notifyOperators(sender, this, "commands.scoreboard.teams.add.success", s);
    }
    
    protected void setTeamOption(final ICommandSender sender, final String[] args, int index) throws CommandException {
        final ScorePlayerTeam scoreplayerteam = this.getTeam(args[index++]);
        if (scoreplayerteam != null) {
            final String s = args[index++].toLowerCase();
            if (!s.equalsIgnoreCase("color") && !s.equalsIgnoreCase("friendlyfire") && !s.equalsIgnoreCase("seeFriendlyInvisibles") && !s.equalsIgnoreCase("nametagVisibility") && !s.equalsIgnoreCase("deathMessageVisibility")) {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            if (args.length == 4) {
                if (s.equalsIgnoreCase("color")) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)) });
                }
                if (s.equalsIgnoreCase("friendlyfire") || s.equalsIgnoreCase("seeFriendlyInvisibles")) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")) });
                }
                if (!s.equalsIgnoreCase("nametagVisibility") && !s.equalsIgnoreCase("deathMessageVisibility")) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceString(Team.EnumVisible.func_178825_a()) });
            }
            else {
                final String s2 = args[index];
                if (s.equalsIgnoreCase("color")) {
                    final EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s2);
                    if (enumchatformatting == null || enumchatformatting.isFancyStyling()) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)) });
                    }
                    scoreplayerteam.setChatFormat(enumchatformatting);
                    scoreplayerteam.setNamePrefix(enumchatformatting.toString());
                    scoreplayerteam.setNameSuffix(EnumChatFormatting.RESET.toString());
                }
                else if (s.equalsIgnoreCase("friendlyfire")) {
                    if (!s2.equalsIgnoreCase("true") && !s2.equalsIgnoreCase("false")) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")) });
                    }
                    scoreplayerteam.setAllowFriendlyFire(s2.equalsIgnoreCase("true"));
                }
                else if (s.equalsIgnoreCase("seeFriendlyInvisibles")) {
                    if (!s2.equalsIgnoreCase("true") && !s2.equalsIgnoreCase("false")) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")) });
                    }
                    scoreplayerteam.setSeeFriendlyInvisiblesEnabled(s2.equalsIgnoreCase("true"));
                }
                else if (s.equalsIgnoreCase("nametagVisibility")) {
                    final Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(s2);
                    if (team$enumvisible == null) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceString(Team.EnumVisible.func_178825_a()) });
                    }
                    scoreplayerteam.setNameTagVisibility(team$enumvisible);
                }
                else if (s.equalsIgnoreCase("deathMessageVisibility")) {
                    final Team.EnumVisible team$enumvisible2 = Team.EnumVisible.func_178824_a(s2);
                    if (team$enumvisible2 == null) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceString(Team.EnumVisible.func_178825_a()) });
                    }
                    scoreplayerteam.setDeathMessageVisibility(team$enumvisible2);
                }
                CommandBase.notifyOperators(sender, this, "commands.scoreboard.teams.option.success", s, scoreplayerteam.getRegisteredName(), s2);
            }
        }
    }
    
    protected void removeTeam(final ICommandSender p_147194_1_, final String[] p_147194_2_, final int p_147194_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final ScorePlayerTeam scoreplayerteam = this.getTeam(p_147194_2_[p_147194_3_]);
        if (scoreplayerteam != null) {
            scoreboard.removeTeam(scoreplayerteam);
            CommandBase.notifyOperators(p_147194_1_, this, "commands.scoreboard.teams.remove.success", scoreplayerteam.getRegisteredName());
        }
    }
    
    protected void listTeams(final ICommandSender p_147186_1_, final String[] p_147186_2_, final int p_147186_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        if (p_147186_2_.length > p_147186_3_) {
            final ScorePlayerTeam scoreplayerteam = this.getTeam(p_147186_2_[p_147186_3_]);
            if (scoreplayerteam == null) {
                return;
            }
            final Collection<String> collection = scoreplayerteam.getMembershipCollection();
            p_147186_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.size() <= 0) {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] { scoreplayerteam.getRegisteredName() });
            }
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] { collection.size(), scoreplayerteam.getRegisteredName() });
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147186_1_.addChatMessage(chatcomponenttranslation);
            p_147186_1_.addChatMessage(new ChatComponentText(CommandBase.joinNiceString(collection.toArray())));
        }
        else {
            final Collection<ScorePlayerTeam> collection2 = scoreboard.getTeams();
            p_147186_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection2.size());
            if (collection2.size() <= 0) {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }
            final ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.scoreboard.teams.list.count", new Object[] { collection2.size() });
            chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147186_1_.addChatMessage(chatcomponenttranslation2);
            for (final ScorePlayerTeam scoreplayerteam2 : collection2) {
                p_147186_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] { scoreplayerteam2.getRegisteredName(), scoreplayerteam2.getTeamName(), scoreplayerteam2.getMembershipCollection().size() }));
            }
        }
    }
    
    protected void joinTeam(final ICommandSender p_147190_1_, final String[] p_147190_2_, int p_147190_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final String s = p_147190_2_[p_147190_3_++];
        final Set<String> set = (Set<String>)Sets.newHashSet();
        final Set<String> set2 = (Set<String>)Sets.newHashSet();
        if (p_147190_1_ instanceof EntityPlayer && p_147190_3_ == p_147190_2_.length) {
            final String s2 = CommandBase.getCommandSenderAsPlayer(p_147190_1_).getName();
            if (scoreboard.addPlayerToTeam(s2, s)) {
                set.add(s2);
            }
            else {
                set2.add(s2);
            }
        }
        else {
            while (p_147190_3_ < p_147190_2_.length) {
                final String s3 = p_147190_2_[p_147190_3_++];
                if (s3.startsWith("@")) {
                    for (final Entity entity : CommandBase.func_175763_c(p_147190_1_, s3)) {
                        final String s4 = CommandBase.getEntityName(p_147190_1_, entity.getUniqueID().toString());
                        if (scoreboard.addPlayerToTeam(s4, s)) {
                            set.add(s4);
                        }
                        else {
                            set2.add(s4);
                        }
                    }
                }
                else {
                    final String s5 = CommandBase.getEntityName(p_147190_1_, s3);
                    if (scoreboard.addPlayerToTeam(s5, s)) {
                        set.add(s5);
                    }
                    else {
                        set2.add(s5);
                    }
                }
            }
        }
        if (!set.isEmpty()) {
            p_147190_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
            CommandBase.notifyOperators(p_147190_1_, this, "commands.scoreboard.teams.join.success", set.size(), s, CommandBase.joinNiceString(set.toArray(new String[set.size()])));
        }
        if (!set2.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] { set2.size(), s, CommandBase.joinNiceString(set2.toArray(new String[set2.size()])) });
        }
    }
    
    protected void leaveTeam(final ICommandSender p_147199_1_, final String[] p_147199_2_, int p_147199_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final Set<String> set = (Set<String>)Sets.newHashSet();
        final Set<String> set2 = (Set<String>)Sets.newHashSet();
        if (p_147199_1_ instanceof EntityPlayer && p_147199_3_ == p_147199_2_.length) {
            final String s3 = CommandBase.getCommandSenderAsPlayer(p_147199_1_).getName();
            if (scoreboard.removePlayerFromTeams(s3)) {
                set.add(s3);
            }
            else {
                set2.add(s3);
            }
        }
        else {
            while (p_147199_3_ < p_147199_2_.length) {
                final String s4 = p_147199_2_[p_147199_3_++];
                if (s4.startsWith("@")) {
                    for (final Entity entity : CommandBase.func_175763_c(p_147199_1_, s4)) {
                        final String s5 = CommandBase.getEntityName(p_147199_1_, entity.getUniqueID().toString());
                        if (scoreboard.removePlayerFromTeams(s5)) {
                            set.add(s5);
                        }
                        else {
                            set2.add(s5);
                        }
                    }
                }
                else {
                    final String s6 = CommandBase.getEntityName(p_147199_1_, s4);
                    if (scoreboard.removePlayerFromTeams(s6)) {
                        set.add(s6);
                    }
                    else {
                        set2.add(s6);
                    }
                }
            }
        }
        if (!set.isEmpty()) {
            p_147199_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
            CommandBase.notifyOperators(p_147199_1_, this, "commands.scoreboard.teams.leave.success", set.size(), CommandBase.joinNiceString(set.toArray(new String[set.size()])));
        }
        if (!set2.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] { set2.size(), CommandBase.joinNiceString(set2.toArray(new String[set2.size()])) });
        }
    }
    
    protected void emptyTeam(final ICommandSender p_147188_1_, final String[] p_147188_2_, final int p_147188_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final ScorePlayerTeam scoreplayerteam = this.getTeam(p_147188_2_[p_147188_3_]);
        if (scoreplayerteam != null) {
            final Collection<String> collection = (Collection<String>)Lists.newArrayList((Iterable<?>)scoreplayerteam.getMembershipCollection());
            p_147188_1_.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, collection.size());
            if (collection.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] { scoreplayerteam.getRegisteredName() });
            }
            for (final String s : collection) {
                scoreboard.removePlayerFromTeam(s, scoreplayerteam);
            }
            CommandBase.notifyOperators(p_147188_1_, this, "commands.scoreboard.teams.empty.success", collection.size(), scoreplayerteam.getRegisteredName());
        }
    }
    
    protected void removeObjective(final ICommandSender p_147191_1_, final String p_147191_2_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final ScoreObjective scoreobjective = this.getObjective(p_147191_2_, false);
        scoreboard.removeObjective(scoreobjective);
        CommandBase.notifyOperators(p_147191_1_, this, "commands.scoreboard.objectives.remove.success", p_147191_2_);
    }
    
    protected void listObjectives(final ICommandSender p_147196_1_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final Collection<ScoreObjective> collection = scoreboard.getScoreObjectives();
        if (collection.size() <= 0) {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        }
        final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] { collection.size() });
        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
        p_147196_1_.addChatMessage(chatcomponenttranslation);
        for (final ScoreObjective scoreobjective : collection) {
            p_147196_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] { scoreobjective.getName(), scoreobjective.getDisplayName(), scoreobjective.getCriteria().getName() }));
        }
    }
    
    protected void setObjectiveDisplay(final ICommandSender p_147198_1_, final String[] p_147198_2_, int p_147198_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final String s = p_147198_2_[p_147198_3_++];
        final int i = Scoreboard.getObjectiveDisplaySlotNumber(s);
        ScoreObjective scoreobjective = null;
        if (p_147198_2_.length == 4) {
            scoreobjective = this.getObjective(p_147198_2_[p_147198_3_], false);
        }
        if (i < 0) {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] { s });
        }
        scoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
        if (scoreobjective != null) {
            CommandBase.notifyOperators(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successSet", Scoreboard.getObjectiveDisplaySlot(i), scoreobjective.getName());
        }
        else {
            CommandBase.notifyOperators(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successCleared", Scoreboard.getObjectiveDisplaySlot(i));
        }
    }
    
    protected void listPlayers(final ICommandSender p_147195_1_, final String[] p_147195_2_, final int p_147195_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        if (p_147195_2_.length > p_147195_3_) {
            final String s = CommandBase.getEntityName(p_147195_1_, p_147195_2_[p_147195_3_]);
            final Map<ScoreObjective, Score> map = scoreboard.getObjectivesForEntity(s);
            p_147195_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, map.size());
            if (map.size() <= 0) {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] { s });
            }
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] { map.size(), s });
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147195_1_.addChatMessage(chatcomponenttranslation);
            for (final Score score : map.values()) {
                p_147195_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] { score.getScorePoints(), score.getObjective().getDisplayName(), score.getObjective().getName() }));
            }
        }
        else {
            final Collection<String> collection = scoreboard.getObjectiveNames();
            p_147195_1_.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.size() <= 0) {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }
            final ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation("commands.scoreboard.players.list.count", new Object[] { collection.size() });
            chatcomponenttranslation2.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147195_1_.addChatMessage(chatcomponenttranslation2);
            p_147195_1_.addChatMessage(new ChatComponentText(CommandBase.joinNiceString(collection.toArray())));
        }
    }
    
    protected void setPlayer(final ICommandSender p_147197_1_, final String[] p_147197_2_, int p_147197_3_) throws CommandException {
        final String s = p_147197_2_[p_147197_3_ - 1];
        final int i = p_147197_3_;
        final String s2 = CommandBase.getEntityName(p_147197_1_, p_147197_2_[p_147197_3_++]);
        if (s2.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s2, 40 });
        }
        final ScoreObjective scoreobjective = this.getObjective(p_147197_2_[p_147197_3_++], true);
        final int j = s.equalsIgnoreCase("set") ? CommandBase.parseInt(p_147197_2_[p_147197_3_++]) : CommandBase.parseInt(p_147197_2_[p_147197_3_++], 0);
        if (p_147197_2_.length > p_147197_3_) {
            final Entity entity = CommandBase.func_175768_b(p_147197_1_, p_147197_2_[i]);
            try {
                final NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(p_147197_2_, p_147197_3_));
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                entity.writeToNBT(nbttagcompound2);
                if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound2, true)) {
                    throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] { s2 });
                }
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        final Scoreboard scoreboard = this.getScoreboard();
        final Score score = scoreboard.getValueFromObjective(s2, scoreobjective);
        if (s.equalsIgnoreCase("set")) {
            score.setScorePoints(j);
        }
        else if (s.equalsIgnoreCase("add")) {
            score.increseScore(j);
        }
        else {
            score.decreaseScore(j);
        }
        CommandBase.notifyOperators(p_147197_1_, this, "commands.scoreboard.players.set.success", scoreobjective.getName(), s2, score.getScorePoints());
    }
    
    protected void resetPlayers(final ICommandSender p_147187_1_, final String[] p_147187_2_, int p_147187_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final String s = CommandBase.getEntityName(p_147187_1_, p_147187_2_[p_147187_3_++]);
        if (p_147187_2_.length > p_147187_3_) {
            final ScoreObjective scoreobjective = this.getObjective(p_147187_2_[p_147187_3_++], false);
            scoreboard.removeObjectiveFromEntity(s, scoreobjective);
            CommandBase.notifyOperators(p_147187_1_, this, "commands.scoreboard.players.resetscore.success", scoreobjective.getName(), s);
        }
        else {
            scoreboard.removeObjectiveFromEntity(s, null);
            CommandBase.notifyOperators(p_147187_1_, this, "commands.scoreboard.players.reset.success", s);
        }
    }
    
    protected void func_175779_n(final ICommandSender p_175779_1_, final String[] p_175779_2_, int p_175779_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final String s = CommandBase.getPlayerName(p_175779_1_, p_175779_2_[p_175779_3_++]);
        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, 40 });
        }
        final ScoreObjective scoreobjective = this.getObjective(p_175779_2_[p_175779_3_], false);
        if (scoreobjective.getCriteria() != IScoreObjectiveCriteria.TRIGGER) {
            throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] { scoreobjective.getName() });
        }
        final Score score = scoreboard.getValueFromObjective(s, scoreobjective);
        score.setLocked(false);
        CommandBase.notifyOperators(p_175779_1_, this, "commands.scoreboard.players.enable.success", scoreobjective.getName(), s);
    }
    
    protected void func_175781_o(final ICommandSender p_175781_1_, final String[] p_175781_2_, int p_175781_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final String s = CommandBase.getEntityName(p_175781_1_, p_175781_2_[p_175781_3_++]);
        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, 40 });
        }
        final ScoreObjective scoreobjective = this.getObjective(p_175781_2_[p_175781_3_++], false);
        if (!scoreboard.entityHasObjective(s, scoreobjective)) {
            throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] { scoreobjective.getName(), s });
        }
        final int i = p_175781_2_[p_175781_3_].equals("*") ? Integer.MIN_VALUE : CommandBase.parseInt(p_175781_2_[p_175781_3_]);
        final int j = (++p_175781_3_ < p_175781_2_.length && !p_175781_2_[p_175781_3_].equals("*")) ? CommandBase.parseInt(p_175781_2_[p_175781_3_], i) : Integer.MAX_VALUE;
        final Score score = scoreboard.getValueFromObjective(s, scoreobjective);
        if (score.getScorePoints() >= i && score.getScorePoints() <= j) {
            CommandBase.notifyOperators(p_175781_1_, this, "commands.scoreboard.players.test.success", score.getScorePoints(), i, j);
            return;
        }
        throw new CommandException("commands.scoreboard.players.test.failed", new Object[] { score.getScorePoints(), i, j });
    }
    
    protected void func_175778_p(final ICommandSender p_175778_1_, final String[] p_175778_2_, int p_175778_3_) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard();
        final String s = CommandBase.getEntityName(p_175778_1_, p_175778_2_[p_175778_3_++]);
        final ScoreObjective scoreobjective = this.getObjective(p_175778_2_[p_175778_3_++], true);
        final String s2 = p_175778_2_[p_175778_3_++];
        final String s3 = CommandBase.getEntityName(p_175778_1_, p_175778_2_[p_175778_3_++]);
        final ScoreObjective scoreobjective2 = this.getObjective(p_175778_2_[p_175778_3_], false);
        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, 40 });
        }
        if (s3.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s3, 40 });
        }
        final Score score = scoreboard.getValueFromObjective(s, scoreobjective);
        if (!scoreboard.entityHasObjective(s3, scoreobjective2)) {
            throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] { scoreobjective2.getName(), s3 });
        }
        final Score score2 = scoreboard.getValueFromObjective(s3, scoreobjective2);
        if (s2.equals("+=")) {
            score.setScorePoints(score.getScorePoints() + score2.getScorePoints());
        }
        else if (s2.equals("-=")) {
            score.setScorePoints(score.getScorePoints() - score2.getScorePoints());
        }
        else if (s2.equals("*=")) {
            score.setScorePoints(score.getScorePoints() * score2.getScorePoints());
        }
        else if (s2.equals("/=")) {
            if (score2.getScorePoints() != 0) {
                score.setScorePoints(score.getScorePoints() / score2.getScorePoints());
            }
        }
        else if (s2.equals("%=")) {
            if (score2.getScorePoints() != 0) {
                score.setScorePoints(score.getScorePoints() % score2.getScorePoints());
            }
        }
        else if (s2.equals("=")) {
            score.setScorePoints(score2.getScorePoints());
        }
        else if (s2.equals("<")) {
            score.setScorePoints(Math.min(score.getScorePoints(), score2.getScorePoints()));
        }
        else if (s2.equals(">")) {
            score.setScorePoints(Math.max(score.getScorePoints(), score2.getScorePoints()));
        }
        else {
            if (!s2.equals("><")) {
                throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] { s2 });
            }
            final int i = score.getScorePoints();
            score.setScorePoints(score2.getScorePoints());
            score2.setScorePoints(i);
        }
        CommandBase.notifyOperators(p_175778_1_, this, "commands.scoreboard.players.operation.success", new Object[0]);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "objectives", "players", "teams");
        }
        if (args[0].equalsIgnoreCase("objectives")) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "list", "add", "remove", "setdisplay");
            }
            if (args[1].equalsIgnoreCase("add")) {
                if (args.length == 4) {
                    final Set<String> set = IScoreObjectiveCriteria.INSTANCES.keySet();
                    return CommandBase.getListOfStringsMatchingLastWord(args, set);
                }
            }
            else if (args[1].equalsIgnoreCase("remove")) {
                if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.func_147184_a(false));
                }
            }
            else if (args[1].equalsIgnoreCase("setdisplay")) {
                if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, Scoreboard.getDisplaySlotStrings());
                }
                if (args.length == 4) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.func_147184_a(false));
                }
            }
        }
        else if (args[0].equalsIgnoreCase("players")) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "set", "add", "remove", "reset", "list", "enable", "test", "operation");
            }
            if (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove") && !args[1].equalsIgnoreCase("reset")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    if (args.length == 3) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                    }
                    if (args.length == 4) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, this.func_175782_e());
                    }
                }
                else if (!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("test")) {
                    if (args[1].equalsIgnoreCase("operation")) {
                        if (args.length == 3) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard().getObjectiveNames());
                        }
                        if (args.length == 4) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.func_147184_a(true));
                        }
                        if (args.length == 5) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><");
                        }
                        if (args.length == 6) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                        }
                        if (args.length == 7) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.func_147184_a(false));
                        }
                    }
                }
                else {
                    if (args.length == 3) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard().getObjectiveNames());
                    }
                    if (args.length == 4 && args[1].equalsIgnoreCase("test")) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, this.func_147184_a(false));
                    }
                }
            }
            else {
                if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                }
                if (args.length == 4) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.func_147184_a(true));
                }
            }
        }
        else if (args[0].equalsIgnoreCase("teams")) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "join", "leave", "empty", "list", "option");
            }
            if (args[1].equalsIgnoreCase("join")) {
                if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard().getTeamNames());
                }
                if (args.length >= 4) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                }
            }
            else {
                if (args[1].equalsIgnoreCase("leave")) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                }
                if (!args[1].equalsIgnoreCase("empty") && !args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("remove")) {
                    if (args[1].equalsIgnoreCase("option")) {
                        if (args.length == 3) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard().getTeamNames());
                        }
                        if (args.length == 4) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility");
                        }
                        if (args.length == 5) {
                            if (args[3].equalsIgnoreCase("color")) {
                                return CommandBase.getListOfStringsMatchingLastWord(args, EnumChatFormatting.getValidValues(true, false));
                            }
                            if (args[3].equalsIgnoreCase("nametagVisibility") || args[3].equalsIgnoreCase("deathMessageVisibility")) {
                                return CommandBase.getListOfStringsMatchingLastWord(args, Team.EnumVisible.func_178825_a());
                            }
                            if (args[3].equalsIgnoreCase("friendlyfire") || args[3].equalsIgnoreCase("seeFriendlyInvisibles")) {
                                return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
                            }
                        }
                    }
                }
                else if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard().getTeamNames());
                }
            }
        }
        return null;
    }
    
    protected List<String> func_147184_a(final boolean p_147184_1_) {
        final Collection<ScoreObjective> collection = this.getScoreboard().getScoreObjectives();
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final ScoreObjective scoreobjective : collection) {
            if (!p_147184_1_ || !scoreobjective.getCriteria().isReadOnly()) {
                list.add(scoreobjective.getName());
            }
        }
        return list;
    }
    
    protected List<String> func_175782_e() {
        final Collection<ScoreObjective> collection = this.getScoreboard().getScoreObjectives();
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final ScoreObjective scoreobjective : collection) {
            if (scoreobjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER) {
                list.add(scoreobjective.getName());
            }
        }
        return list;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args[0].equalsIgnoreCase("players") ? ((args.length > 1 && args[1].equalsIgnoreCase("operation")) ? (index == 2 || index == 5) : (index == 2)) : (args[0].equalsIgnoreCase("teams") && index == 2);
    }
}
