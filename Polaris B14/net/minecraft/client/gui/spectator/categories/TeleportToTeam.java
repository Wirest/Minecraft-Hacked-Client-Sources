/*     */ package net.minecraft.client.gui.spectator.categories;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenu;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class TeleportToTeam implements net.minecraft.client.gui.spectator.ISpectatorMenuView, ISpectatorMenuObject
/*     */ {
/*  25 */   private final List<ISpectatorMenuObject> field_178672_a = Lists.newArrayList();
/*     */   
/*     */   public TeleportToTeam()
/*     */   {
/*  29 */     Minecraft minecraft = Minecraft.getMinecraft();
/*     */     
/*  31 */     for (ScorePlayerTeam scoreplayerteam : minecraft.theWorld.getScoreboard().getTeams())
/*     */     {
/*  33 */       this.field_178672_a.add(new TeamSelectionObject(scoreplayerteam));
/*     */     }
/*     */   }
/*     */   
/*     */   public List<ISpectatorMenuObject> func_178669_a()
/*     */   {
/*  39 */     return this.field_178672_a;
/*     */   }
/*     */   
/*     */   public IChatComponent func_178670_b()
/*     */   {
/*  44 */     return new ChatComponentText("Select a team to teleport to");
/*     */   }
/*     */   
/*     */   public void func_178661_a(SpectatorMenu menu)
/*     */   {
/*  49 */     menu.func_178647_a(this);
/*     */   }
/*     */   
/*     */   public IChatComponent getSpectatorName()
/*     */   {
/*  54 */     return new ChatComponentText("Teleport to team member");
/*     */   }
/*     */   
/*     */   public void func_178663_a(float p_178663_1_, int alpha)
/*     */   {
/*  59 */     Minecraft.getMinecraft().getTextureManager().bindTexture(net.minecraft.client.gui.GuiSpectator.field_175269_a);
/*  60 */     Gui.drawModalRectWithCustomSizedTexture(0, 0, 16.0F, 0.0F, 16, 16, 256.0F, 256.0F);
/*     */   }
/*     */   
/*     */   public boolean func_178662_A_()
/*     */   {
/*  65 */     for (ISpectatorMenuObject ispectatormenuobject : this.field_178672_a)
/*     */     {
/*  67 */       if (ispectatormenuobject.func_178662_A_())
/*     */       {
/*  69 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   class TeamSelectionObject implements ISpectatorMenuObject
/*     */   {
/*     */     private final ScorePlayerTeam field_178676_b;
/*     */     private final ResourceLocation field_178677_c;
/*     */     private final List<NetworkPlayerInfo> field_178675_d;
/*     */     
/*     */     public TeamSelectionObject(ScorePlayerTeam p_i45492_2_)
/*     */     {
/*  84 */       this.field_178676_b = p_i45492_2_;
/*  85 */       this.field_178675_d = Lists.newArrayList();
/*     */       
/*  87 */       for (String s : p_i45492_2_.getMembershipCollection())
/*     */       {
/*  89 */         NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(s);
/*     */         
/*  91 */         if (networkplayerinfo != null)
/*     */         {
/*  93 */           this.field_178675_d.add(networkplayerinfo);
/*     */         }
/*     */       }
/*     */       
/*  97 */       if (!this.field_178675_d.isEmpty())
/*     */       {
/*  99 */         String s1 = ((NetworkPlayerInfo)this.field_178675_d.get(new Random().nextInt(this.field_178675_d.size()))).getGameProfile().getName();
/* 100 */         this.field_178677_c = AbstractClientPlayer.getLocationSkin(s1);
/* 101 */         AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, s1);
/*     */       }
/*     */       else
/*     */       {
/* 105 */         this.field_178677_c = net.minecraft.client.resources.DefaultPlayerSkin.getDefaultSkinLegacy();
/*     */       }
/*     */     }
/*     */     
/*     */     public void func_178661_a(SpectatorMenu menu)
/*     */     {
/* 111 */       menu.func_178647_a(new TeleportToPlayer(this.field_178675_d));
/*     */     }
/*     */     
/*     */     public IChatComponent getSpectatorName()
/*     */     {
/* 116 */       return new ChatComponentText(this.field_178676_b.getTeamName());
/*     */     }
/*     */     
/*     */     public void func_178663_a(float p_178663_1_, int alpha)
/*     */     {
/* 121 */       int i = -1;
/* 122 */       String s = FontRenderer.getFormatFromString(this.field_178676_b.getColorPrefix());
/*     */       
/* 124 */       if (s.length() >= 2)
/*     */       {
/* 126 */         i = Minecraft.getMinecraft().fontRendererObj.getColorCode(s.charAt(1));
/*     */       }
/*     */       
/* 129 */       if (i >= 0)
/*     */       {
/* 131 */         float f = (i >> 16 & 0xFF) / 255.0F;
/* 132 */         float f1 = (i >> 8 & 0xFF) / 255.0F;
/* 133 */         float f2 = (i & 0xFF) / 255.0F;
/* 134 */         Gui.drawRect(1.0D, 1.0D, 15.0D, 15.0D, MathHelper.func_180183_b(f * p_178663_1_, f1 * p_178663_1_, f2 * p_178663_1_) | alpha << 24);
/*     */       }
/*     */       
/* 137 */       Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178677_c);
/* 138 */       GlStateManager.color(p_178663_1_, p_178663_1_, p_178663_1_, alpha / 255.0F);
/* 139 */       Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
/* 140 */       Gui.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
/*     */     }
/*     */     
/*     */     public boolean func_178662_A_()
/*     */     {
/* 145 */       return !this.field_178675_d.isEmpty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\spectator\categories\TeleportToTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */