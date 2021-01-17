package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import javax.vecmath.*;
import me.rigamortis.faurax.module.*;
import net.minecraft.block.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import java.util.*;

public class Search extends Module implements RenderHelper
{
    public static ArrayList<Vector3f> blocks;
    public static final boolean[] ids;
    public static boolean enabled;
    
    static {
        Search.blocks = new ArrayList<Vector3f>();
        ids = new boolean[4096];
    }
    
    public Search() {
        this.setKey("");
        this.setName("Search");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        Search.mc.renderGlobal.loadRenderers();
        super.onEnabled();
        Search.enabled = true;
        add(56);
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Search.blocks.clear();
        Search.enabled = false;
    }
    
    @EventTarget
    public void preRenderBlock(final EventPreRenderBlock e) {
        if (this.isToggled()) {
            if (Search.blocks.size() >= 1000) {
                Search.blocks.clear();
            }
            final Vector3f vec3 = new Vector3f((float)e.getPos().getX(), (float)e.getPos().getY(), (float)e.getPos().getZ());
            if (!Search.blocks.contains(vec3) && shouldRender(Block.getIdFromBlock(e.getBlock()))) {
                final BlockPos pos = new BlockPos(vec3.getX(), vec3.getY(), vec3.getZ());
                final int id = Block.getIdFromBlock(Search.mc.theWorld.getBlockState(pos).getBlock());
                if (Search.mc.thePlayer.getDistance(vec3.getX(), vec3.getY(), vec3.getZ()) <= 128.0 && id != 0) {
                    Search.blocks.add(new Vector3f((float)e.getPos().getX(), (float)e.getPos().getY(), (float)e.getPos().getZ()));
                }
            }
        }
    }
    
    public void renderBlocks() {
        final int var0 = (int)Search.mc.thePlayer.posX;
        final int var = (int)Search.mc.thePlayer.posY;
        final int var2 = (int)Search.mc.thePlayer.posZ;
        Search.mc.renderGlobal.markBlockRangeForRenderUpdate(var0 - 200, var - 200, var2 - 200, var0 + 200, var + 200, var2 + 200);
    }
    
    public int getColorForBlock(final double posX, final double posY, final double posZ) {
        int color = -188;
        final BlockPos pos = new BlockPos(posX, posY, posZ);
        final int id = Block.getIdFromBlock(Search.mc.theWorld.getBlockState(pos).getBlock());
        if (id == 56) {
            color = 9480789;
        }
        if (id == 14) {
            color = -1869610923;
        }
        if (id == 15) {
            color = -2140123051;
        }
        if (id == 16) {
            color = 538976341;
        }
        if (id == 21) {
            color = 3170389;
        }
        if (id == 73) {
            color = 1610612821;
        }
        if (id == 74) {
            color = 1610612821;
        }
        if (id == 129) {
            color = 8396885;
        }
        return color;
    }
    
    @EventTarget
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            this.setModInfo(" §7" + Search.blocks.size());
            for (final Vector3f vec : Search.blocks) {
                final double n = vec.getX();
                Search.mc.getRenderManager();
                final double posX = n - RenderManager.renderPosX;
                final double n2 = vec.getY();
                Search.mc.getRenderManager();
                final double posY = n2 - RenderManager.renderPosY;
                final double n3 = vec.getZ();
                Search.mc.getRenderManager();
                final double posZ = n3 - RenderManager.renderPosZ;
                if (Search.mc.thePlayer.getDistance(vec.getX(), vec.getY(), vec.getZ()) <= 128.0 && shouldRender(Block.getIdFromBlock(Search.mc.theWorld.getBlockState(new BlockPos(vec.getX(), vec.getY(), vec.getZ())).getBlock()))) {
                    Search.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(posX, posY, posZ, posX + 1.0, posY + 1.0, posZ + 1.0), 1.5f, this.getColorForBlock(vec.getX(), vec.getY(), vec.getZ()));
                    Search.guiUtils.drawFilledBBESP(new AxisAlignedBB(posX, posY, posZ, posX + 1.0, posY + 1.0, posZ + 1.0), this.getColorForBlock(vec.getX(), vec.getY(), vec.getZ()));
                }
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    public static boolean contains(final int id) {
        return Search.ids[id];
    }
    
    public static void add(final int id) {
        Search.ids[id] = true;
    }
    
    public static void remove(final int id) {
        Search.ids[id] = false;
    }
    
    public static void clear() {
        for (int i = 0; i < Search.ids.length; ++i) {
            Search.ids[i] = false;
        }
    }
    
    public static boolean shouldRender(final int id) {
        return Search.ids[id];
    }
}
