// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import java.util.Date;
import net.minecraft.util.ChatComponentText;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.command.ICommandManager;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.CommandResultStats;
import net.minecraft.util.IChatComponent;
import java.text.SimpleDateFormat;
import net.minecraft.command.ICommandSender;

public abstract class CommandBlockLogic implements ICommandSender
{
    private static final SimpleDateFormat timestampFormat;
    private int successCount;
    private boolean trackOutput;
    private IChatComponent lastOutput;
    private String commandStored;
    private String customName;
    private final CommandResultStats resultStats;
    
    static {
        timestampFormat = new SimpleDateFormat("HH:mm:ss");
    }
    
    public CommandBlockLogic() {
        this.trackOutput = true;
        this.lastOutput = null;
        this.commandStored = "";
        this.customName = "@";
        this.resultStats = new CommandResultStats();
    }
    
    public int getSuccessCount() {
        return this.successCount;
    }
    
    public IChatComponent getLastOutput() {
        return this.lastOutput;
    }
    
    public void writeDataToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setString("Command", this.commandStored);
        tagCompound.setInteger("SuccessCount", this.successCount);
        tagCompound.setString("CustomName", this.customName);
        tagCompound.setBoolean("TrackOutput", this.trackOutput);
        if (this.lastOutput != null && this.trackOutput) {
            tagCompound.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
        }
        this.resultStats.writeStatsToNBT(tagCompound);
    }
    
    public void readDataFromNBT(final NBTTagCompound nbt) {
        this.commandStored = nbt.getString("Command");
        this.successCount = nbt.getInteger("SuccessCount");
        if (nbt.hasKey("CustomName", 8)) {
            this.customName = nbt.getString("CustomName");
        }
        if (nbt.hasKey("TrackOutput", 1)) {
            this.trackOutput = nbt.getBoolean("TrackOutput");
        }
        if (nbt.hasKey("LastOutput", 8) && this.trackOutput) {
            this.lastOutput = IChatComponent.Serializer.jsonToComponent(nbt.getString("LastOutput"));
        }
        this.resultStats.readStatsFromNBT(nbt);
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
        return permLevel <= 2;
    }
    
    public void setCommand(final String command) {
        this.commandStored = command;
        this.successCount = 0;
    }
    
    public String getCommand() {
        return this.commandStored;
    }
    
    public void trigger(final World worldIn) {
        if (worldIn.isRemote) {
            this.successCount = 0;
        }
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        if (minecraftserver != null && minecraftserver.isAnvilFileSet() && minecraftserver.isCommandBlockEnabled()) {
            final ICommandManager icommandmanager = minecraftserver.getCommandManager();
            try {
                this.lastOutput = null;
                this.successCount = icommandmanager.executeCommand(this, this.commandStored);
                return;
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing command block");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");
                crashreportcategory.addCrashSectionCallable("Command", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return CommandBlockLogic.this.getCommand();
                    }
                });
                crashreportcategory.addCrashSectionCallable("Name", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return CommandBlockLogic.this.getName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        this.successCount = 0;
    }
    
    @Override
    public String getName() {
        return this.customName;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }
    
    public void setName(final String p_145754_1_) {
        this.customName = p_145754_1_;
    }
    
    @Override
    public void addChatMessage(final IChatComponent component) {
        if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote) {
            this.lastOutput = new ChatComponentText("[" + CommandBlockLogic.timestampFormat.format(new Date()) + "] ").appendSibling(component);
            this.updateCommand();
        }
    }
    
    @Override
    public boolean sendCommandFeedback() {
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        return minecraftserver == null || !minecraftserver.isAnvilFileSet() || minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
    }
    
    @Override
    public void setCommandStat(final CommandResultStats.Type type, final int amount) {
        this.resultStats.func_179672_a(this, type, amount);
    }
    
    public abstract void updateCommand();
    
    public abstract int func_145751_f();
    
    public abstract void func_145757_a(final ByteBuf p0);
    
    public void setLastOutput(final IChatComponent lastOutputMessage) {
        this.lastOutput = lastOutputMessage;
    }
    
    public void setTrackOutput(final boolean shouldTrackOutput) {
        this.trackOutput = shouldTrackOutput;
    }
    
    public boolean shouldTrackOutput() {
        return this.trackOutput;
    }
    
    public boolean tryOpenEditCommandBlock(final EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            return false;
        }
        if (playerIn.getEntityWorld().isRemote) {
            playerIn.openEditCommandBlock(this);
        }
        return true;
    }
    
    public CommandResultStats getCommandResultStats() {
        return this.resultStats;
    }
}
