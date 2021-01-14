package net.minecraft.command.server;

import io.netty.buffer.ByteBuf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

public abstract class CommandBlockLogic implements ICommandSender {
    /**
     * The formatting for the timestamp on commands run.
     */
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * The number of successful commands run. (used for redstone output)
     */
    private int successCount;
    private boolean trackOutput = true;

    /**
     * The previously run command.
     */
    private IChatComponent lastOutput = null;

    /**
     * The command stored in the command block.
     */
    private String commandStored = "";

    /**
     * The custom name of the command block. (defaults to "@")
     */
    private String customName = "@";
    private final CommandResultStats field_175575_g = new CommandResultStats();
    private static final String __OBFID = "CL_00000128";

    /**
     * returns the successCount int.
     */
    public int getSuccessCount() {
        return this.successCount;
    }

    /**
     * Returns the lastOutput.
     */
    public IChatComponent getLastOutput() {
        return this.lastOutput;
    }

    /**
     * Stores data to NBT format.
     */
    public void writeDataToNBT(NBTTagCompound p_145758_1_) {
        p_145758_1_.setString("Command", this.commandStored);
        p_145758_1_.setInteger("SuccessCount", this.successCount);
        p_145758_1_.setString("CustomName", this.customName);
        p_145758_1_.setBoolean("TrackOutput", this.trackOutput);

        if (this.lastOutput != null && this.trackOutput) {
            p_145758_1_.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
        }

        this.field_175575_g.func_179670_b(p_145758_1_);
    }

    /**
     * Reads NBT formatting and stored data into variables.
     */
    public void readDataFromNBT(NBTTagCompound p_145759_1_) {
        this.commandStored = p_145759_1_.getString("Command");
        this.successCount = p_145759_1_.getInteger("SuccessCount");

        if (p_145759_1_.hasKey("CustomName", 8)) {
            this.customName = p_145759_1_.getString("CustomName");
        }

        if (p_145759_1_.hasKey("TrackOutput", 1)) {
            this.trackOutput = p_145759_1_.getBoolean("TrackOutput");
        }

        if (p_145759_1_.hasKey("LastOutput", 8) && this.trackOutput) {
            this.lastOutput = IChatComponent.Serializer.jsonToComponent(p_145759_1_.getString("LastOutput"));
        }

        this.field_175575_g.func_179668_a(p_145759_1_);
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        return permissionLevel <= 2;
    }

    /**
     * Sets the command.
     */
    public void setCommand(String p_145752_1_) {
        this.commandStored = p_145752_1_;
        this.successCount = 0;
    }

    /**
     * Returns the customName of the command block.
     */
    public String getCustomName() {
        return this.commandStored;
    }

    public void trigger(World worldIn) {
        if (worldIn.isRemote) {
            this.successCount = 0;
        }

        MinecraftServer var2 = MinecraftServer.getServer();

        if (var2 != null && var2.func_175578_N() && var2.isCommandBlockEnabled()) {
            ICommandManager var3 = var2.getCommandManager();

            try {
                this.lastOutput = null;
                this.successCount = var3.executeCommand(this, this.commandStored);
            } catch (Throwable var7) {
                CrashReport var5 = CrashReport.makeCrashReport(var7, "Executing command block");
                CrashReportCategory var6 = var5.makeCategory("Command to be executed");
                var6.addCrashSectionCallable("Command", new Callable() {
                    private static final String __OBFID = "CL_00002154";

                    public String func_180324_a() {
                        return CommandBlockLogic.this.getCustomName();
                    }

                    public Object call() {
                        return this.func_180324_a();
                    }
                });
                var6.addCrashSectionCallable("Name", new Callable() {
                    private static final String __OBFID = "CL_00002153";

                    public String func_180326_a() {
                        return CommandBlockLogic.this.getName();
                    }

                    public Object call() {
                        return this.func_180326_a();
                    }
                });
                throw new ReportedException(var5);
            }
        } else {
            this.successCount = 0;
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return this.customName;
    }

    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }

    public void func_145754_b(String p_145754_1_) {
        this.customName = p_145754_1_;
    }

    /**
     * Notifies this sender of some sort of information.  This is for messages intended to display to the user.  Used
     * for typical output (like "you asked for whether or not this game rule is set, so here's your answer"), warnings
     * (like "I fetched this block for you by ID, but I'd like you to know that every time you do this, I die a little
     * inside"), and errors (like "it's not called iron_pixacke, silly").
     */
    public void addChatMessage(IChatComponent message) {
        if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote) {
            this.lastOutput = (new ChatComponentText("[" + timestampFormat.format(new Date()) + "] ")).appendSibling(message);
            this.func_145756_e();
        }
    }

    public boolean sendCommandFeedback() {
        MinecraftServer var1 = MinecraftServer.getServer();
        return var1 == null || !var1.func_175578_N() || var1.worldServers[0].getGameRules().getGameRuleBooleanValue("commandBlockOutput");
    }

    public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_) {
        this.field_175575_g.func_179672_a(this, p_174794_1_, p_174794_2_);
    }

    public abstract void func_145756_e();

    public abstract int func_145751_f();

    public abstract void func_145757_a(ByteBuf var1);

    public void func_145750_b(IChatComponent p_145750_1_) {
        this.lastOutput = p_145750_1_;
    }

    public void func_175573_a(boolean p_175573_1_) {
        this.trackOutput = p_175573_1_;
    }

    public boolean func_175571_m() {
        return this.trackOutput;
    }

    public boolean func_175574_a(EntityPlayer p_175574_1_) {
        if (!p_175574_1_.capabilities.isCreativeMode) {
            return false;
        } else {
            if (p_175574_1_.getEntityWorld().isRemote) {
                p_175574_1_.func_146095_a(this);
            }

            return true;
        }
    }

    public CommandResultStats func_175572_n() {
        return this.field_175575_g;
    }
}
