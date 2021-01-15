/*     */ package rip.jutting.polaris.command;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.command.commands.BanClearCommand;
/*     */ import rip.jutting.polaris.command.commands.BindCommand;
/*     */ import rip.jutting.polaris.command.commands.ClearCommand;
/*     */ import rip.jutting.polaris.command.commands.ConfigCommand;
/*     */ import rip.jutting.polaris.command.commands.DownCommand;
/*     */ import rip.jutting.polaris.command.commands.DumpCommand;
/*     */ import rip.jutting.polaris.command.commands.FriendCommand;
/*     */ import rip.jutting.polaris.command.commands.HelpCommand;
/*     */ import rip.jutting.polaris.command.commands.IPCommand;
/*     */ import rip.jutting.polaris.command.commands.MacroCommand;
/*     */ import rip.jutting.polaris.command.commands.NameCommand;
/*     */ import rip.jutting.polaris.command.commands.OldConfigCommand;
/*     */ import rip.jutting.polaris.command.commands.ProxyCommand;
/*     */ import rip.jutting.polaris.command.commands.SpammerCommand;
/*     */ import rip.jutting.polaris.command.commands.ToggleCommand;
/*     */ import rip.jutting.polaris.command.commands.TrollCommand;
/*     */ import rip.jutting.polaris.command.commands.UpCommand;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandManager
/*     */ {
/*     */   private HashMap<String[], Command> commands;
/*     */   private String prefix;
/*     */   
/*     */   public CommandManager()
/*     */   {
/*  39 */     this.commands = new HashMap();
/*  40 */     this.prefix = "-";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void loadCommands()
/*     */   {
/*  47 */     this.commands.put(new String[] { "help", "h" }, new HelpCommand());
/*  48 */     this.commands.put(new String[] { "friend", "f" }, new FriendCommand());
/*  49 */     this.commands.put(new String[] { "bind" }, new BindCommand());
/*  50 */     this.commands.put(new String[] { "toggle", "t" }, new ToggleCommand());
/*  51 */     this.commands.put(new String[] { "proxy" }, new ProxyCommand());
/*  52 */     this.commands.put(new String[] { "name", "watermark" }, new NameCommand());
/*  53 */     this.commands.put(new String[] { "config" }, new ConfigCommand());
/*  54 */     this.commands.put(new String[] { "up" }, new UpCommand());
/*  55 */     this.commands.put(new String[] { "down" }, new DownCommand());
/*  56 */     this.commands.put(new String[] { "dump" }, new DumpCommand());
/*  57 */     this.commands.put(new String[] { "clearbans" }, new BanClearCommand());
/*  58 */     this.commands.put(new String[] { "macro" }, new MacroCommand());
/*  59 */     this.commands.put(new String[] { "oldconfig" }, new OldConfigCommand());
/*  60 */     this.commands.put(new String[] { "spam" }, new SpammerCommand());
/*  61 */     this.commands.put(new String[] { "ip", "lookup" }, new IPCommand());
/*  62 */     this.commands.put(new String[] { "troll" }, new TrollCommand());
/*  63 */     this.commands.put(new String[] { "clear" }, new ClearCommand());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean processCommand(String rawMessage)
/*     */   {
/*  71 */     if (!rawMessage.startsWith(this.prefix)) {
/*  72 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  76 */     boolean safe = rawMessage.split(this.prefix).length > 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */     if (safe)
/*     */     {
/*  84 */       String beheaded = rawMessage.split(this.prefix)[1];
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  89 */       String[] args = beheaded.split(" ");
/*     */       
/*     */ 
/*  92 */       Command command = getCommand(args[0]);
/*     */       
/*     */ 
/*  95 */       if (command != null)
/*     */       {
/*  97 */         if (!command.run(args))
/*     */         {
/*  99 */           Polaris.sendMessage(command.usage());
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else {
/* 105 */         Polaris.sendMessage("Try -help.");
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 114 */       Polaris.sendMessage("Try -help.");
/*     */     }
/*     */     
/* 117 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private Command getCommand(String name)
/*     */   {
/*     */     int j;
/*     */     int i;
/* 125 */     for (Iterator localIterator = this.commands.entrySet().iterator(); localIterator.hasNext(); 
/*     */         
/* 127 */         i < j)
/*     */     {
/* 125 */       Map.Entry entry = (Map.Entry)localIterator.next();
/* 126 */       String[] key = (String[])entry.getKey();
/* 127 */       String[] arrayOfString1; j = (arrayOfString1 = key).length;i = 0; continue;String s = arrayOfString1[i];
/* 128 */       if (s.equalsIgnoreCase(name)) {
/* 129 */         return (Command)entry.getValue();
/*     */       }
/* 127 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HashMap<String[], Command> getCommands()
/*     */   {
/* 141 */     return this.commands;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */