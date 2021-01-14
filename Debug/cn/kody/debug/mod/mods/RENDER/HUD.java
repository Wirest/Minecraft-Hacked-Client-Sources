package cn.kody.debug.mod.mods.RENDER;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.client.renderer.OpenGlHelper;
import java.awt.Color;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

import java.util.Iterator;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Comparator;
import java.util.Collection;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.util.function.Predicate;
import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.Client;
import cn.kody.debug.events.EventBlock;
import cn.kody.debug.events.EventPacket;
import cn.kody.debug.events.EventRender;
import cn.kody.debug.events.EventRender2D;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.mod.mods.WORLD.Scaffold;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.ui.font.UnicodeFontRenderer;
import cn.kody.debug.utils.FluxColors;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.render.GLUtils;
import cn.kody.debug.utils.render.RenderUtil;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.utils.time.WaitTimer;
import cn.kody.debug.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;

public class HUD extends Mod
{
    private double lastTps;
    public Value<Boolean> potion;
    public TimeHelper timer;
    public boolean resetted;
    public boolean sorted;
    public static ArrayList<BlockPos> list;
    public float animationY;
    public float animationY2;
    public static boolean needSort;
    Map<Potion, Double> timerMap;
    private int x;
    private static ArrayList<Long> times;
    private static WaitTimer tpsTimer;
    
    public HUD() {
        super("HUD", "HUD", Category.RENDER);
        this.lastTps = 20.0;
        this.potion = new Value<Boolean>("HUD_PotionStatus", true);
        this.timer = new TimeHelper();
        this.resetted = false;
        this.sorted = false;
        this.timerMap = new HashMap<Potion, Double>();
    }
    

    public static void update() {
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
        HUD.list.clear();
    }
    
    @EventTarget
    public void onRenderBlock(EventBlock class1631) {
        final BlockPos blockPos = new BlockPos(class1631.x, class1631.y, class1631.z);
        if (!HUD.list.contains(blockPos) && class1631.block instanceof Block) {
            HUD.list.add(blockPos);
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        HUD.list.removeIf(HUD::lambda$onUpdate$0);
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D class112) {
        if (!this.resetted) {
            this.timer.reset();
            this.resetted = true;
        }
        if (this.resetted && !this.sorted) {
            HUD.needSort = true;
            if (this.timer.delay(2000.0)) {
                this.sorted = true;
            }
        }
        final float n = (float)class112.res.getScaledWidth();
        final float n2 = (float)class112.res.getScaledHeight();
        Client.instance.fontMgr.tahoma50.drawString(Client.CLIENT_NAME, 2.0f, -4.0f, Notification.reAlpha(Colors.WHITE.c, 0.75f));
        Client.instance.fontMgr.tahoma20.drawString("v" + Client.CLIENT_VERSION, 4.0f, 22.0f, Notification.reAlpha(Colors.WHITE.c, 0.75f));
        this.renderArray(Client.instance.fontMgr.tahoma18, class112.res);
        if (this.mc.thePlayer.getHealth() <= 6.0f) {
            final float animationY = this.animationY;
            float n3;
            if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
                n3 = 20.0f;
            }
            else {
                n3 = 2.0f;
            }
            final float n4 = 10.0f;
            final float animationY2 = this.animationY;
            int n5;
            if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
                n5 = 20;
            }
            else {
                n5 = 2;
            }
            this.animationY = (float) RenderUtil.getAnimationState(animationY, n3, (float)(Math.max(n4, Math.abs(animationY2 - n5) * 35.0f) * 0.3));
            final int reAlpha = Notification.reAlpha(Colors.YELLOW.c, 0.8f);
            RenderUtil.drawRoundedRect(n / 2.0f - 90.0f, this.animationY, n / 2.0f + 90.0f, this.animationY + 25.0f, reAlpha, reAlpha);
            Client.instance.fontMgr.tahoma20.drawCenteredString("======== WARNING ========", n / 2.0f - 1.0f, this.animationY + 2.0f, Colors.WHITE.c);
            Client.instance.fontMgr.tahoma16.drawCenteredString("Low Health!", n / 2.0f - 1.0f, this.animationY + 14.0f, Colors.WHITE.c);
        }
        else {
            this.animationY = (float) RenderUtil.getAnimationState(this.animationY, -25.0f, (float)Math.max(10.0, Math.abs(this.animationY + 25.0f) * 35.0f * 0.3));
            if (this.animationY > -25.0f) {
                final int reAlpha2 = Notification.reAlpha(Colors.YELLOW.c, 0.8f);
                RenderUtil.drawRoundedRect(n / 2.0f - 90.0f, this.animationY, n / 2.0f + 90.0f, this.animationY + 25.0f, reAlpha2, reAlpha2);
                Client.instance.fontMgr.tahoma20.drawCenteredString("======== WARNING ========", n / 2.0f - 1.0f, this.animationY + 2.0f, Colors.WHITE.c);
                Client.instance.fontMgr.tahoma16.drawCenteredString("Low Health!", n / 2.0f - 1.0f, this.animationY + 16.0f, Colors.WHITE.c);
            }
        }
        final StringBuilder append = new StringBuilder().append("\247b").append("X:§r ").append((int)this.mc.thePlayer.posX).append("\247b").append(" Y:§r ").append((int)this.mc.thePlayer.posY).append("\247b").append(" Z:§r ").append((int)this.mc.thePlayer.posZ).append("\247b").append(" FPS:§r ");
        final Minecraft mc = this.mc;
        final String string = append.append(Minecraft.getDebugFPS()).append("\247b").append(" TPS: §r").append((int)this.lastTps).toString();
        final FontRenderer fontRendererObj = this.mc.fontRendererObj;
        final String p_drawStringWithShadow_1_ = string;
        final float p_drawStringWithShadow_2_ = 2.0f;
        final float n6 = n2;
        int n7;
        if (this.mc.currentScreen instanceof GuiChat) {
            n7 = 24;
        }
        else {
            n7 = 12;
        }
        fontRendererObj.drawStringWithShadow(p_drawStringWithShadow_1_, p_drawStringWithShadow_2_, n6 - n7, Colors.WHITE.c);
        final ModManager modMgr = Client.instance.modMgr;
        if (ModManager.getMod(Scaffold.class).isEnabled()) {
            final int reAlpha3 = Notification.reAlpha(Colors.WHITE.c, 0.8f);
            if (BossStatus.bossName != null && BossStatus.statusBarTime > 0 && this.mc.thePlayer.getHealth() <= 6.0f) {
                this.animationY2 = (float) RenderUtil.getAnimationState(this.animationY2, 50.0f, (float)(Math.max(10.0f, Math.abs(this.animationY2 - 50.0f) * 35.0f) * 0.3));
            }
            else if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
                this.animationY2 = (float) RenderUtil.getAnimationState(this.animationY2, 20.0f, (float)(Math.max(10.0f, Math.abs(this.animationY2 - 20.0f) * 35.0f) * 0.3));
            }
            else if (this.mc.thePlayer.getHealth() <= 6.0f) {
                this.animationY2 = (float) RenderUtil.getAnimationState(this.animationY2, 30.0f, (float)(Math.max(10.0f, Math.abs(this.animationY2 - 30.0f) * 35.0f) * 0.3));
            }
            else {
                this.animationY2 = (float) RenderUtil.getAnimationState(this.animationY2, 2.0f, (float)(Math.max(10.0f, Math.abs(this.animationY2 - 2.0f) * 35.0f) * 0.3));
            }
            RenderUtil.drawRoundedRect(n / 2.0f - 90.0f, this.animationY2, n / 2.0f + 90.0f, this.animationY2 + 20.0f, reAlpha3, reAlpha3);
            final ModManager modMgr2 = Client.instance.modMgr;
            if (((Scaffold)ModManager.getMod(Scaffold.class)).currentlyHolding != null) {
                GL11.glPushMatrix();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                if (this.mc.theWorld != null) {
                    GLUtils.enableGUIStandardItemLighting();
                }
                GlStateManager.pushMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.clear(256);
                this.mc.getRenderItem().zLevel = -150.0f;
                final RenderItem renderItem = this.mc.getRenderItem();
                final ModManager modMgr3 = Client.instance.modMgr;
                renderItem.renderItemAndEffectIntoGUI(((Scaffold)ModManager.getMod(Scaffold.class)).currentlyHolding, (int)(n / 2.0f - 90.0f + 2.0f), (int)(this.animationY2 + 2.0f));
                this.mc.getRenderItem().zLevel = 0.0f;
                GlStateManager.disableBlend();
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GlStateManager.enableAlpha();
                GlStateManager.popMatrix();
                GL11.glPopMatrix();
            }
            else {
                Client.instance.fontMgr.icon30.drawString("c", n / 2.0f - 88.0f, this.animationY2 + 2.5f, Colors.GREY.c);
            }
            Client.instance.fontMgr.tahoma18.drawCenteredString(this.getBlockCount() + " blocks left", n / 2.0f - 2.0f, this.animationY2 + 5.5f, Colors.GREY.c);
        }else {
            final int reAlpha4 = Notification.reAlpha(Colors.WHITE.c, 0.8f);
            this.animationY2 = (float) RenderUtil.getAnimationState(this.animationY2, -25.0f, (float)(Math.max(10.0f, Math.abs(this.animationY2 + 25.0f) * 25.0f) * 0.3));
            if (this.animationY2 > -25.0f) {
                RenderUtil.drawRoundedRect(n / 2.0f - 90.0f, this.animationY2, n / 2.0f + 90.0f, this.animationY2, reAlpha4, reAlpha4);
                Client.instance.fontMgr.tahoma18.drawCenteredString("Scaffold Disabled", n / 2.0f - 2.0f, this.animationY2 + 5.5f, Colors.GREY.c);
            }
        }
        if (this.potion.getValueState()) {
            this.renderPotionStatus(n , n2);
        }
    }
    
    public int getBlockCount() {
        int n = 0;
        int i = 36;
        while (i < 45) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = stack.getItem();
                if (stack.getItem() instanceof ItemBlock && this.isValid(item)) {
                    n += stack.stackSize;
                }
            }
            ++i;
        }
        return n;
    }
    
    private boolean isValid(final Item item) {
        return item instanceof ItemBlock && !Scaffold.blacklistedBlocks.contains(((ItemBlock)item).getBlock());
    }
    
    @Override
    public void onFinalLoad() {
        final ModManager modMgr = Client.instance.modMgr;
        ArrayList<Mod> mods = new ArrayList<Mod>(ModManager.getEnabledModListHUD());
        HUD.needSort = true;
    }
    
    private void renderArray(final UnicodeFontRenderer unicodeFontRender, final ScaledResolution scaledResolution) {
        int n2;
        int n = n2 = 4;
        final int n3 = 6;
        if (this.mc.thePlayer != null && this.mc.theWorld != null && HUD.needSort) {
            final ModManager modMgr = Client.instance.modMgr;
            HUD.needSort = false;
        }
        ArrayList<Mod> mods = new ArrayList<Mod>(ModManager.getEnabledModListHUD());
        mods.sort(Comparator.comparingDouble(m -> - unicodeFontRender.getStringWidth(m.getRenderName())));
        for (final Mod mod : mods) {
            final String renderName = mod.getRenderName();
            mod.getDisplayName();
            if (mod.getDisplayName() != null) {
            }
            final float n4 = (float)unicodeFontRender.getStringWidth(renderName);
            GL11.glPushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("client/arraylistshadow.png"));
            GlStateManager.color(1.0f, 1.0f, 1.0f, 0.7f);
            Gui.drawModalRectWithCustomSizedTexture(scaledResolution.getScaledWidth() - n4 - n3 - 14.0f, n2 - 2.5f - 1.5f - 1.5f - 1.5f - 6.0f - 1.0f, 0.0f, 0.0f, unicodeFontRender.getStringWidth(renderName) * 1 + 20 + 10, 38.5, unicodeFontRender.getStringWidth(renderName) * 1 + 20 + 10, 38.5);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GL11.glPopMatrix();
            n2 += unicodeFontRender.FONT_HEIGHT + 1;
        }
        for (final Mod mod2 : mods) {
            final String renderName2 = mod2.getRenderName();
            mod2.getDisplayName();
            if (mod2.getDisplayName() != null) {
            }
            unicodeFontRender.drawStringWithShadowWithAlpha(renderName2, scaledResolution.getScaledWidth() - (float)unicodeFontRender.getStringWidth(renderName2) - n3, (float)(n + 1), Colors.WHITE.c, 0.85f);
            n += unicodeFontRender.FONT_HEIGHT + 1;
        }
    }
    
    public void renderPotionStatus(final float n, final float n2) {
        this.x = 5;
        for (final PotionEffect p_getDurationString_0_ : this.mc.thePlayer.getActivePotionEffects()) {
            final Potion potion = Potion.potionTypes[p_getDurationString_0_.getPotionID()];
            String s = I18n.format(potion.getName(), new Object[0]);
            int int1;
            int int2;
            try {
                int1 = Integer.parseInt(potion.getDurationString(p_getDurationString_0_).split(":")[0]);
                int2 = Integer.parseInt(potion.getDurationString(p_getDurationString_0_).split(":")[1]);
            }
            catch (Exception ex) {
                int1 = 0;
                int2 = 0;
            }
            final double n3 = int1 * 60 + int2;
            if (potion.maxtimer == 0 || n3 > potion.maxtimer) {
        	potion.maxtimer = (int)n3;
            }
            float n4 = 0.0f;
            if (n3 >= 0.0) {
                n4 = (float)(n3 / (float)potion.maxtimer * 100.0);
            }
            if (!this.timerMap.containsKey(potion)) {
                this.timerMap.put(potion, n3);
            }
            if (this.timerMap.get(potion) == 0.0 || n3 > this.timerMap.get(potion)) {
                this.timerMap.replace(potion, n3);
            }
            switch (p_getDurationString_0_.getAmplifier()) {
                case 1: {
                    s += " II";
                    break;
                }
                case 2: {
                    s += " III";
                    break;
                }
                case 3: {
                    s += " IV";
                    break;
                }
            }
            final int c = Colors.WHITE.c;
            if (p_getDurationString_0_.getDuration() < 600 && p_getDurationString_0_.getDuration() > 300) {
                final int c2 = Colors.YELLOW.c;
            }
            else if (p_getDurationString_0_.getDuration() < 300) {
                final int c3 = Colors.RED.c;
            }
            else if (p_getDurationString_0_.getDuration() > 600) {
                final int c4 = Colors.WHITE.c;
            }
            final int n5 = (int)((n - 6.0f) * 1.33f);
            final int n6 = (int)(n2 - 31.0f - this.mc.fontRendererObj.FONT_HEIGHT + this.x + 5.0f);
            if (n4 <= 1.0f) {
                n4 = 2.0f;
            }
            potion.animationX = (float) RenderUtil.getAnimationState(potion.animationX, 1.2f * n4, Math.max(10.0f, Math.abs(potion.animationX - 1.2f * n4) * 15.0f) * 0.3f);
            RenderUtil.drawRoundedRect(n - 130.0f, n2 - 40.0f + this.x, n - 10.0f, n2 - 10.0f + this.x, Notification.reAlpha(Colors.WHITE.c, 0.6f), Notification.reAlpha(Colors.WHITE.c, 0.6f));
            RenderUtil.drawRoundedRect(n - 130.0f, n2 - 40.0f + this.x, n - 130.0f + potion.animationX, n2 - 10.0f + this.x, Notification.reAlpha(Colors.WHITE.c, 0.2f), Notification.reAlpha(Colors.WHITE.c, 0.2f));
            final float n7 = n2 - this.mc.fontRendererObj.FONT_HEIGHT + this.x - 18.0f;
            
            Client.instance.fontMgr.wqy16.drawString(s, n - 101.0f, n7 - this.mc.fontRendererObj.FONT_HEIGHT, new Color(47, 116, 253).getRGB());
            Client.instance.fontMgr.tahoma16.drawString(potion.getDurationString(p_getDurationString_0_), n - 101.0f, n7 + 4.0f, Notification.reAlpha(new Color(Colors.GREY.c).darker().getRGB(), 0.8f));
            if (potion.hasStatusIcon()) {
                GlStateManager.pushMatrix();
                GL11.glDisable(2929);
                GL11.glEnable(3042);
                GL11.glDepthMask(false);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                final int statusIconIndex = potion.getStatusIconIndex();
                this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                this.mc.ingameGUI.drawTexturedModalRect(n - 124.0f, (float)(n6 + 1), 0 + statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glEnable(2929);
                GlStateManager.popMatrix();
            }
            this.x -= 35;
        }
    }
    
    @EventTarget
    public void onPacket(final EventPacket eventPacket) {
        if (!eventPacket.isOutgoing() && eventPacket.packet instanceof S03PacketTimeUpdate) {
            HUD.times.add(Math.max(0x8234E77283F29EA6L ^ 0x8234E77283F29D4EL, HUD.tpsTimer.getTime()));
            long n = 0xAC48C9D880040258L ^ 0xAC48C9D880040258L;
            if (HUD.times.size() > 5) {
                HUD.times.remove(0);
            }
            final Iterator<Long> iterator = HUD.times.iterator();
            while (iterator.hasNext()) {
                n += iterator.next();
            }
            this.lastTps = 20.0 / (n / HUD.times.size()) * 1000.0;
            HUD.tpsTimer.reset();
        }
    }
    
    private static int lambda$renderArray$2(final UnicodeFontRenderer unicodeFontRender, final Mod mod, final Mod mod2) {
        return -Float.compare((float)unicodeFontRender.getStringWidth(mod.getRenderName()), (float)unicodeFontRender.getStringWidth(mod2.getRenderName()));
    }
    
    private static int lambda$onFinalLoad$1(final Mod mod, final Mod mod2) {
        return -Float.compare((float)Client.instance.fontMgr.tahoma18.getStringWidth(mod.getRenderName()), (float)Client.instance.fontMgr.tahoma18.getStringWidth(mod2.getRenderName()));
    }
    
    private static boolean lambda$onUpdate$0(final BlockPos p_getBlockState_1_) {
        boolean b;
        if (!(Minecraft.getMinecraft().theWorld.getBlockState(p_getBlockState_1_).getBlock() instanceof BlockAir)) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    static {
        HUD.list = new ArrayList<BlockPos>();
        HUD.needSort = true;
        HUD.times = new ArrayList<Long>();
        HUD.tpsTimer = new WaitTimer();
    }
}
