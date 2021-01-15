/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.gson.JsonParseException;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.CommandResultStats;
/*     */ import net.minecraft.command.CommandResultStats.Type;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.ClickEvent.Action;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IChatComponent.Serializer;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntitySign extends TileEntity
/*     */ {
/*  24 */   public final IChatComponent[] signText = { new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText("") };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  30 */   public int lineBeingEdited = -1;
/*  31 */   private boolean isEditable = true;
/*     */   private EntityPlayer player;
/*  33 */   private final CommandResultStats stats = new CommandResultStats();
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/*  37 */     super.writeToNBT(compound);
/*     */     
/*  39 */     for (int i = 0; i < 4; i++)
/*     */     {
/*  41 */       String s = IChatComponent.Serializer.componentToJson(this.signText[i]);
/*  42 */       compound.setString("Text" + (i + 1), s);
/*     */     }
/*     */     
/*  45 */     this.stats.writeStatsToNBT(compound);
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/*  50 */     this.isEditable = false;
/*  51 */     super.readFromNBT(compound);
/*  52 */     ICommandSender icommandsender = new ICommandSender()
/*     */     {
/*     */       public String getName()
/*     */       {
/*  56 */         return "Sign";
/*     */       }
/*     */       
/*     */       public IChatComponent getDisplayName() {
/*  60 */         return new ChatComponentText(getName());
/*     */       }
/*     */       
/*     */       public void addChatMessage(IChatComponent component) {}
/*     */       
/*     */       public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*     */       {
/*  67 */         return true;
/*     */       }
/*     */       
/*     */       public BlockPos getPosition() {
/*  71 */         return TileEntitySign.this.pos;
/*     */       }
/*     */       
/*     */       public Vec3 getPositionVector() {
/*  75 */         return new Vec3(TileEntitySign.this.pos.getX() + 0.5D, TileEntitySign.this.pos.getY() + 0.5D, TileEntitySign.this.pos.getZ() + 0.5D);
/*     */       }
/*     */       
/*     */       public World getEntityWorld() {
/*  79 */         return TileEntitySign.this.worldObj;
/*     */       }
/*     */       
/*     */       public Entity getCommandSenderEntity() {
/*  83 */         return null;
/*     */       }
/*     */       
/*     */       public boolean sendCommandFeedback() {
/*  87 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */       public void setCommandStat(CommandResultStats.Type type, int amount) {}
/*     */     };
/*     */     
/*  94 */     for (int i = 0; i < 4;)
/*     */     {
/*  96 */       String s = compound.getString("Text" + (i + 1));
/*     */       
/*     */       try
/*     */       {
/* 100 */         IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
/*     */         
/*     */         try
/*     */         {
/* 104 */           this.signText[i] = net.minecraft.util.ChatComponentProcessor.processComponent(icommandsender, ichatcomponent, null);
/*     */         }
/*     */         catch (CommandException var7)
/*     */         {
/* 108 */           this.signText[i] = ichatcomponent;
/*     */         }
/*  94 */         i++;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (JsonParseException var8)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */         this.signText[i] = new ChatComponentText(s);
/*     */       }
/*     */     }
/*     */     
/* 117 */     this.stats.readStatsFromNBT(compound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public net.minecraft.network.Packet getDescriptionPacket()
/*     */   {
/* 126 */     IChatComponent[] aichatcomponent = new IChatComponent[4];
/* 127 */     System.arraycopy(this.signText, 0, aichatcomponent, 0, 4);
/* 128 */     return new net.minecraft.network.play.server.S33PacketUpdateSign(this.worldObj, this.pos, aichatcomponent);
/*     */   }
/*     */   
/*     */   public boolean func_183000_F()
/*     */   {
/* 133 */     return true;
/*     */   }
/*     */   
/*     */   public boolean getIsEditable()
/*     */   {
/* 138 */     return this.isEditable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEditable(boolean isEditableIn)
/*     */   {
/* 146 */     this.isEditable = isEditableIn;
/*     */     
/* 148 */     if (!isEditableIn)
/*     */     {
/* 150 */       this.player = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPlayer(EntityPlayer playerIn)
/*     */   {
/* 156 */     this.player = playerIn;
/*     */   }
/*     */   
/*     */   public EntityPlayer getPlayer()
/*     */   {
/* 161 */     return this.player;
/*     */   }
/*     */   
/*     */   public boolean executeCommand(final EntityPlayer playerIn)
/*     */   {
/* 166 */     ICommandSender icommandsender = new ICommandSender()
/*     */     {
/*     */       public String getName()
/*     */       {
/* 170 */         return playerIn.getName();
/*     */       }
/*     */       
/*     */       public IChatComponent getDisplayName() {
/* 174 */         return playerIn.getDisplayName();
/*     */       }
/*     */       
/*     */       public void addChatMessage(IChatComponent component) {}
/*     */       
/*     */       public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*     */       {
/* 181 */         return permLevel <= 2;
/*     */       }
/*     */       
/*     */       public BlockPos getPosition() {
/* 185 */         return TileEntitySign.this.pos;
/*     */       }
/*     */       
/*     */       public Vec3 getPositionVector() {
/* 189 */         return new Vec3(TileEntitySign.this.pos.getX() + 0.5D, TileEntitySign.this.pos.getY() + 0.5D, TileEntitySign.this.pos.getZ() + 0.5D);
/*     */       }
/*     */       
/*     */       public World getEntityWorld() {
/* 193 */         return playerIn.getEntityWorld();
/*     */       }
/*     */       
/*     */       public Entity getCommandSenderEntity() {
/* 197 */         return playerIn;
/*     */       }
/*     */       
/*     */       public boolean sendCommandFeedback() {
/* 201 */         return false;
/*     */       }
/*     */       
/*     */       public void setCommandStat(CommandResultStats.Type type, int amount) {
/* 205 */         TileEntitySign.this.stats.func_179672_a(this, type, amount);
/*     */       }
/*     */     };
/*     */     
/* 209 */     for (int i = 0; i < this.signText.length; i++)
/*     */     {
/* 211 */       ChatStyle chatstyle = this.signText[i] == null ? null : this.signText[i].getChatStyle();
/*     */       
/* 213 */       if ((chatstyle != null) && (chatstyle.getChatClickEvent() != null))
/*     */       {
/* 215 */         ClickEvent clickevent = chatstyle.getChatClickEvent();
/*     */         
/* 217 */         if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
/*     */         {
/* 219 */           MinecraftServer.getServer().getCommandManager().executeCommand(icommandsender, clickevent.getValue());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 224 */     return true;
/*     */   }
/*     */   
/*     */   public CommandResultStats getStats()
/*     */   {
/* 229 */     return this.stats;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntitySign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */