package net.minecraft.client.gui.spectator.categories;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject
{
    private final List field_178672_a = Lists.newArrayList();

    public TeleportToTeam()
    {
        Minecraft var1 = Minecraft.getMinecraft();
        Iterator var2 = var1.theWorld.getScoreboard().getTeams().iterator();

        while (var2.hasNext())
        {
            ScorePlayerTeam var3 = (ScorePlayerTeam)var2.next();
            this.field_178672_a.add(new TeleportToTeam.TeamSelectionObject(var3));
        }
    }

    @Override
	public List func_178669_a()
    {
        return this.field_178672_a;
    }

    @Override
	public IChatComponent func_178670_b()
    {
        return new ChatComponentText("Select a team to teleport to");
    }

    @Override
	public void func_178661_a(SpectatorMenu menu)
    {
        menu.func_178647_a(this);
    }

    @Override
	public IChatComponent getSpectatorName()
    {
        return new ChatComponentText("Teleport to team member");
    }

    @Override
	public void func_178663_a(float p_178663_1_, int alpha)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 16.0F, 0.0F, 16, 16, 256.0F, 256.0F);
    }

    @Override
	public boolean func_178662_A_()
    {
        Iterator var1 = this.field_178672_a.iterator();
        ISpectatorMenuObject var2;

        do
        {
            if (!var1.hasNext())
            {
                return false;
            }

            var2 = (ISpectatorMenuObject)var1.next();
        }
        while (!var2.func_178662_A_());

        return true;
    }

    class TeamSelectionObject implements ISpectatorMenuObject
    {
        private final ScorePlayerTeam field_178676_b;
        private final ResourceLocation field_178677_c;
        private final List field_178675_d;

        public TeamSelectionObject(ScorePlayerTeam p_i45492_2_)
        {
            this.field_178676_b = p_i45492_2_;
            this.field_178675_d = Lists.newArrayList();
            Iterator var3 = p_i45492_2_.getMembershipCollection().iterator();

            while (var3.hasNext())
            {
                String var4 = (String)var3.next();
                NetworkPlayerInfo var5 = Minecraft.getMinecraft().getNetHandler().func_175104_a(var4);

                if (var5 != null)
                {
                    this.field_178675_d.add(var5);
                }
            }

            if (!this.field_178675_d.isEmpty())
            {
                String var6 = ((NetworkPlayerInfo)this.field_178675_d.get((new Random()).nextInt(this.field_178675_d.size()))).getGameProfile().getName();
                this.field_178677_c = AbstractClientPlayer.getLocationSkin(var6);
                AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, var6);
            }
            else
            {
                this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
        }

        @Override
		public void func_178661_a(SpectatorMenu menu)
        {
            menu.func_178647_a(new TeleportToPlayer(this.field_178675_d));
        }

        @Override
		public IChatComponent getSpectatorName()
        {
            return new ChatComponentText(this.field_178676_b.func_96669_c());
        }

        @Override
		public void func_178663_a(float p_178663_1_, int alpha)
        {
            int var3 = -1;
            String var4 = FontRenderer.getFormatFromString(this.field_178676_b.getColorPrefix());

            if (var4.length() >= 2)
            {
                var3 = Minecraft.getMinecraft().fontRendererObj.getColorCode(var4.charAt(1));
            }

            if (var3 >= 0)
            {
                float var5 = (var3 >> 16 & 255) / 255.0F;
                float var6 = (var3 >> 8 & 255) / 255.0F;
                float var7 = (var3 & 255) / 255.0F;
                Gui.drawRect(1, 1, 15, 15, MathHelper.func_180183_b(var5 * p_178663_1_, var6 * p_178663_1_, var7 * p_178663_1_) | alpha << 24);
            }

            Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178677_c);
            GlStateManager.color(p_178663_1_, p_178663_1_, p_178663_1_, alpha / 255.0F);
            Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
            Gui.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
        }

        @Override
		public boolean func_178662_A_()
        {
            return !this.field_178675_d.isEmpty();
        }
    }
}
