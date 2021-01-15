/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.command.CommandResultStats;
/*     */ import net.minecraft.command.CommandResultStats.Type;
/*     */ import net.minecraft.command.ICommandManager;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IChatComponent.Serializer;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public abstract class CommandBlockLogic implements net.minecraft.command.ICommandSender
/*     */ {
/*  23 */   private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");
/*     */   
/*     */   private int successCount;
/*     */   
/*  27 */   private boolean trackOutput = true;
/*     */   
/*     */ 
/*  30 */   private IChatComponent lastOutput = null;
/*     */   
/*     */ 
/*  33 */   private String commandStored = "";
/*     */   
/*     */ 
/*  36 */   private String customName = "@";
/*  37 */   private final CommandResultStats resultStats = new CommandResultStats();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSuccessCount()
/*     */   {
/*  44 */     return this.successCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getLastOutput()
/*     */   {
/*  52 */     return this.lastOutput;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeDataToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  60 */     tagCompound.setString("Command", this.commandStored);
/*  61 */     tagCompound.setInteger("SuccessCount", this.successCount);
/*  62 */     tagCompound.setString("CustomName", this.customName);
/*  63 */     tagCompound.setBoolean("TrackOutput", this.trackOutput);
/*     */     
/*  65 */     if ((this.lastOutput != null) && (this.trackOutput))
/*     */     {
/*  67 */       tagCompound.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
/*     */     }
/*     */     
/*  70 */     this.resultStats.writeStatsToNBT(tagCompound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readDataFromNBT(NBTTagCompound nbt)
/*     */   {
/*  78 */     this.commandStored = nbt.getString("Command");
/*  79 */     this.successCount = nbt.getInteger("SuccessCount");
/*     */     
/*  81 */     if (nbt.hasKey("CustomName", 8))
/*     */     {
/*  83 */       this.customName = nbt.getString("CustomName");
/*     */     }
/*     */     
/*  86 */     if (nbt.hasKey("TrackOutput", 1))
/*     */     {
/*  88 */       this.trackOutput = nbt.getBoolean("TrackOutput");
/*     */     }
/*     */     
/*  91 */     if ((nbt.hasKey("LastOutput", 8)) && (this.trackOutput))
/*     */     {
/*  93 */       this.lastOutput = IChatComponent.Serializer.jsonToComponent(nbt.getString("LastOutput"));
/*     */     }
/*     */     
/*  96 */     this.resultStats.readStatsFromNBT(nbt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*     */   {
/* 104 */     return permLevel <= 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCommand(String command)
/*     */   {
/* 112 */     this.commandStored = command;
/* 113 */     this.successCount = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommand()
/*     */   {
/* 121 */     return this.commandStored;
/*     */   }
/*     */   
/*     */   public void trigger(World worldIn)
/*     */   {
/* 126 */     if (worldIn.isRemote)
/*     */     {
/* 128 */       this.successCount = 0;
/*     */     }
/*     */     
/* 131 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/* 133 */     if ((minecraftserver != null) && (minecraftserver.isAnvilFileSet()) && (minecraftserver.isCommandBlockEnabled()))
/*     */     {
/* 135 */       ICommandManager icommandmanager = minecraftserver.getCommandManager();
/*     */       
/*     */       try
/*     */       {
/* 139 */         this.lastOutput = null;
/* 140 */         this.successCount = icommandmanager.executeCommand(this, this.commandStored);
/*     */       }
/*     */       catch (Throwable throwable)
/*     */       {
/* 144 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing command block");
/* 145 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");
/* 146 */         crashreportcategory.addCrashSectionCallable("Command", new Callable()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 150 */             return CommandBlockLogic.this.getCommand();
/*     */           }
/* 152 */         });
/* 153 */         crashreportcategory.addCrashSectionCallable("Name", new Callable()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 157 */             return CommandBlockLogic.this.getName();
/*     */           }
/* 159 */         });
/* 160 */         throw new net.minecraft.util.ReportedException(crashreport);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 165 */       this.successCount = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 174 */     return this.customName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getDisplayName()
/*     */   {
/* 182 */     return new ChatComponentText(getName());
/*     */   }
/*     */   
/*     */   public void setName(String p_145754_1_)
/*     */   {
/* 187 */     this.customName = p_145754_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addChatMessage(IChatComponent component)
/*     */   {
/* 195 */     if ((this.trackOutput) && (getEntityWorld() != null) && (!getEntityWorld().isRemote))
/*     */     {
/* 197 */       this.lastOutput = new ChatComponentText("[" + timestampFormat.format(new java.util.Date()) + "] ").appendSibling(component);
/* 198 */       updateCommand();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean sendCommandFeedback()
/*     */   {
/* 207 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 208 */     return (minecraftserver == null) || (!minecraftserver.isAnvilFileSet()) || (minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput"));
/*     */   }
/*     */   
/*     */   public void setCommandStat(CommandResultStats.Type type, int amount)
/*     */   {
/* 213 */     this.resultStats.func_179672_a(this, type, amount);
/*     */   }
/*     */   
/*     */   public abstract void updateCommand();
/*     */   
/*     */   public abstract int func_145751_f();
/*     */   
/*     */   public abstract void func_145757_a(ByteBuf paramByteBuf);
/*     */   
/*     */   public void setLastOutput(IChatComponent lastOutputMessage)
/*     */   {
/* 224 */     this.lastOutput = lastOutputMessage;
/*     */   }
/*     */   
/*     */   public void setTrackOutput(boolean shouldTrackOutput)
/*     */   {
/* 229 */     this.trackOutput = shouldTrackOutput;
/*     */   }
/*     */   
/*     */   public boolean shouldTrackOutput()
/*     */   {
/* 234 */     return this.trackOutput;
/*     */   }
/*     */   
/*     */   public boolean tryOpenEditCommandBlock(EntityPlayer playerIn)
/*     */   {
/* 239 */     if (!playerIn.capabilities.isCreativeMode)
/*     */     {
/* 241 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 245 */     if (playerIn.getEntityWorld().isRemote)
/*     */     {
/* 247 */       playerIn.openEditCommandBlock(this);
/*     */     }
/*     */     
/* 250 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public CommandResultStats getCommandResultStats()
/*     */   {
/* 256 */     return this.resultStats;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandBlockLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */