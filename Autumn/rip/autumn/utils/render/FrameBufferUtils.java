package rip.autumn.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;

public final class FrameBufferUtils {
   private static final Minecraft mc = Minecraft.getMinecraft();

   public static void checkSetupFBO() {
      Framebuffer fbo = mc.getFramebuffer();
      if (fbo != null && fbo.depthBuffer > -1) {
         setupFBO(fbo);
         fbo.depthBuffer = -1;
      }

   }

   public static void setupFBO(Framebuffer fbo) {
      EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
      int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
      EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
      EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
   }
}
