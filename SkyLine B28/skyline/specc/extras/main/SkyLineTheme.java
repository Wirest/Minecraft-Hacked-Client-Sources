package skyline.specc.extras.main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Mineman;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.RenderUtil;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.ScreenUtil;
import skyline.specc.SkyLine;
import skyline.specc.render.modules.HUD;
import skyline.specc.render.renderevnts.EventRenderScreen;
import skyline.specc.utils.FontUtil;

public class SkyLineTheme extends ModMode<HUD>
{

	public SkyLineTheme(HUD parent) {
		super(parent, "SkyLine");
	}
	
	private static Gui DeezNuts;
	
	@EventListener
	public void onEvent(EventRenderScreen event) {
		int[] counter = {1};
		FontUtil.bigarial.drawString(EnumChatFormatting.WHITE + "Sky" + EnumChatFormatting.AQUA + "Line", 2, 2, -1);

		List<Module> mods = new ArrayList<Module>();

		for (Module module : SkyLine.getManagers().getModuleManager().getContents())
			mods.add(module);
		
		Collections.sort(mods, new ModuleComparator());
		
		int y = 2;

        for (Module modules : mods) {
			int width = FontUtil.roboto.getStringWidth(modules.getDisplayName() + " ");
			if (modules.getState() && modules.getData().isVisible() && !modules.getName().contains("HUD")) {
				if (this.getParent().german.getValue()) {
					Gui.drawRect(ScaledResolution.getScaledWidth() - 2 - width - 0.5f, y - 2, ScaledResolution.getScaledWidth(), y + 8, Integer.MIN_VALUE);
					if (this.parent.left.getValue()) {
						Gui.drawRect(ScaledResolution.getScaledWidth() - width - 3.5f, y - 2, ScreenUtil.getWidth() - width - 2, y + 8, OverlayUtil.rainbow(counter[0] * 350, 0.4f, 1f));
					} else {
					    Gui.drawRect(ScaledResolution.getScaledWidth() - 1.7f, y - 2, ScaledResolution.getScaledWidth(), y + 8, OverlayUtil.rainbow(counter[0] * 350, 0.4f, 1f));
					}
					FontUtil.roboto.drawString(modules.getDisplayName(), ScreenUtil.getWidth() - width - 1f, y, OverlayUtil.rainbow(counter[0] * 350, 0.4f, 1f));
				}
				y += FontUtil.roboto.getHeight() + 3;
			}
		}

		final EnumFacing yaw = EnumFacing.getHorizontal(MathHelper.floor_double(p.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3);
		final String displayString = String.valueOf(yaw.getName().substring(0, 1).toUpperCase()) + yaw.getName().substring(1);

		RenderUtil.drawBorderedRect(0, ScaledResolution.getScaledHeight() - 21, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight() - 2, 1, new Color(41, 179, 193, 70), new Color(0, 0, 0));
		FontUtil.roboto.drawString(mc.getCurrentServerData().pingToServer + "Ms", 2, ScaledResolution.getScaledHeight() - 28, 3);
		FontUtil.roboto.drawString(displayString, 2, (ScaledResolution.getScaledHeight() - 18), 1);
		FontUtil.roboto.drawString(Math.round(mc.thePlayer.posX) + " " + Math.round(mc.thePlayer.posY) + " " + Math.round(mc.thePlayer.posZ), 2, (ScaledResolution.getScaledHeight() - 10), 1);
		String motdFirst = mc.getCurrentServerData().serverMOTD.split("\n")[0];		
		String motdSecond = mc.getCurrentServerData().serverMOTD.split("\n")[1];
        FontUtil.roboto.drawString(motdFirst, ScaledResolution.getScaledWidth() - FontUtil.roboto.getStringWidth(motdFirst) - 64, (ScaledResolution.getScaledHeight() - 18), -1);
        FontUtil.roboto.drawString(motdSecond, ScaledResolution.getScaledWidth() - FontUtil.roboto.getStringWidth(motdSecond) - 26, (ScaledResolution.getScaledHeight() - 10), -1);
       ++counter[0];
	}

    protected void func_178012_a(int p_178012_1_, int p_178012_2_, ResourceLocation p_178012_3_)
    {
        Mineman.getMinecraft().getTextureManager().bindTexture(p_178012_3_);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 18, 18, 18.0F, 18.0F);
        GlStateManager.disableBlend();
    }


    private static void func_175245_a(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo p_175245_4_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Mineman.getMinecraft().getTextureManager().bindTexture(Gui.icons);
        byte var5 = 0;
        boolean var6 = false;
        byte var7;

        if (p_175245_4_.getResponseTime() < 0) {
            var7 = 5;
        } else if (p_175245_4_.getResponseTime() < 150) {
            var7 = 0;
        } else if (p_175245_4_.getResponseTime() < 300) {
            var7 = 1;
        } else if (p_175245_4_.getResponseTime() < 600) {
            var7 = 2;
        } else if (p_175245_4_.getResponseTime() < 1000) {
            var7 = 3;
        } else {
            var7 = 4;
        }

        //zLevel += 100.0F;
        DeezNuts.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + var5 * 10, 176 + var7 * 8, 10, 8);
        //zLevel -= 100.0F;
    }
	
}