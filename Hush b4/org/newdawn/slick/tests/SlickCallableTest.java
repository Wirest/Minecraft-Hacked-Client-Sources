// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Image;
import org.newdawn.slick.BasicGame;

public class SlickCallableTest extends BasicGame
{
    private Image image;
    private Image back;
    private float rot;
    private AngelCodeFont font;
    private Animation homer;
    
    public SlickCallableTest() {
        super("Slick Callable Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.image = new Image("testdata/rocket.png");
        this.back = new Image("testdata/sky.jpg");
        this.font = new AngelCodeFont("testdata/hiero.fnt", "testdata/hiero.png");
        final SpriteSheet sheet = new SpriteSheet("testdata/homeranim.png", 36, 65);
        this.homer = new Animation(sheet, 0, 0, 7, 0, true, 150, true);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.scale(2.0f, 2.0f);
        g.fillRect(0.0f, 0.0f, 800.0f, 600.0f, this.back, 0.0f, 0.0f);
        g.resetTransform();
        g.drawImage(this.image, 100.0f, 100.0f);
        this.image.draw(100.0f, 200.0f, 80.0f, 200.0f);
        this.font.drawString(100.0f, 200.0f, "Text Drawn before the callable");
        final SlickCallable callable = new SlickCallable() {
            @Override
            protected void performGLOperations() throws SlickException {
                SlickCallableTest.this.renderGL();
            }
        };
        callable.call();
        this.homer.draw(450.0f, 250.0f, 80.0f, 200.0f);
        this.font.drawString(150.0f, 300.0f, "Text Drawn after the callable");
    }
    
    public void renderGL() {
        final FloatBuffer pos = BufferUtils.createFloatBuffer(4);
        pos.put(new float[] { 5.0f, 5.0f, 10.0f, 0.0f }).flip();
        final FloatBuffer red = BufferUtils.createFloatBuffer(4);
        red.put(new float[] { 0.8f, 0.1f, 0.0f, 1.0f }).flip();
        GL11.glLight(16384, 4611, pos);
        GL11.glEnable(16384);
        GL11.glEnable(2884);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        final float h = 0.75f;
        GL11.glFrustum(-1.0, 1.0, -h, h, 5.0, 60.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -40.0f);
        GL11.glRotatef(this.rot, 0.0f, 1.0f, 1.0f);
        GL11.glMaterial(1028, 5634, red);
        this.gear(0.5f, 2.0f, 2.0f, 10, 0.7f);
    }
    
    private void gear(final float inner_radius, final float outer_radius, final float width, final int teeth, final float tooth_depth) {
        final float r0 = inner_radius;
        final float r2 = outer_radius - tooth_depth / 2.0f;
        final float r3 = outer_radius + tooth_depth / 2.0f;
        final float da = 6.2831855f / teeth / 4.0f;
        GL11.glShadeModel(7424);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glBegin(8);
        for (int i = 0; i <= teeth; ++i) {
            final float angle = i * 2.0f * 3.1415927f / teeth;
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle), r2 * (float)Math.sin(angle), width * 0.5f);
            if (i < teeth) {
                GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
                GL11.glVertex3f(r2 * (float)Math.cos(angle + 3.0f * da), r2 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
            }
        }
        GL11.glEnd();
        GL11.glBegin(7);
        for (int i = 0; i < teeth; ++i) {
            final float angle = i * 2.0f * 3.1415927f / teeth;
            GL11.glVertex3f(r2 * (float)Math.cos(angle), r2 * (float)Math.sin(angle), width * 0.5f);
            GL11.glVertex3f(r3 * (float)Math.cos(angle + da), r3 * (float)Math.sin(angle + da), width * 0.5f);
            GL11.glVertex3f(r3 * (float)Math.cos(angle + 2.0f * da), r3 * (float)Math.sin(angle + 2.0f * da), width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 3.0f * da), r2 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
        }
        GL11.glEnd();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        GL11.glBegin(8);
        for (int i = 0; i <= teeth; ++i) {
            final float angle = i * 2.0f * 3.1415927f / teeth;
            GL11.glVertex3f(r2 * (float)Math.cos(angle), r2 * (float)Math.sin(angle), -width * 0.5f);
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 3.0f * da), r2 * (float)Math.sin(angle + 3.0f * da), -width * 0.5f);
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
        }
        GL11.glEnd();
        GL11.glBegin(7);
        for (int i = 0; i < teeth; ++i) {
            final float angle = i * 2.0f * 3.1415927f / teeth;
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 3.0f * da), r2 * (float)Math.sin(angle + 3.0f * da), -width * 0.5f);
            GL11.glVertex3f(r3 * (float)Math.cos(angle + 2.0f * da), r3 * (float)Math.sin(angle + 2.0f * da), -width * 0.5f);
            GL11.glVertex3f(r3 * (float)Math.cos(angle + da), r3 * (float)Math.sin(angle + da), -width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle), r2 * (float)Math.sin(angle), -width * 0.5f);
        }
        GL11.glEnd();
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glBegin(8);
        for (int i = 0; i < teeth; ++i) {
            final float angle = i * 2.0f * 3.1415927f / teeth;
            GL11.glVertex3f(r2 * (float)Math.cos(angle), r2 * (float)Math.sin(angle), width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle), r2 * (float)Math.sin(angle), -width * 0.5f);
            float u = r3 * (float)Math.cos(angle + da) - r2 * (float)Math.cos(angle);
            float v = r3 * (float)Math.sin(angle + da) - r2 * (float)Math.sin(angle);
            final float len = (float)Math.sqrt(u * u + v * v);
            u /= len;
            v /= len;
            GL11.glNormal3f(v, -u, 0.0f);
            GL11.glVertex3f(r3 * (float)Math.cos(angle + da), r3 * (float)Math.sin(angle + da), width * 0.5f);
            GL11.glVertex3f(r3 * (float)Math.cos(angle + da), r3 * (float)Math.sin(angle + da), -width * 0.5f);
            GL11.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
            GL11.glVertex3f(r3 * (float)Math.cos(angle + 2.0f * da), r3 * (float)Math.sin(angle + 2.0f * da), width * 0.5f);
            GL11.glVertex3f(r3 * (float)Math.cos(angle + 2.0f * da), r3 * (float)Math.sin(angle + 2.0f * da), -width * 0.5f);
            u = r2 * (float)Math.cos(angle + 3.0f * da) - r3 * (float)Math.cos(angle + 2.0f * da);
            v = r2 * (float)Math.sin(angle + 3.0f * da) - r3 * (float)Math.sin(angle + 2.0f * da);
            GL11.glNormal3f(v, -u, 0.0f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 3.0f * da), r2 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
            GL11.glVertex3f(r2 * (float)Math.cos(angle + 3.0f * da), r2 * (float)Math.sin(angle + 3.0f * da), -width * 0.5f);
            GL11.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
        }
        GL11.glVertex3f(r2 * (float)Math.cos(0.0), r2 * (float)Math.sin(0.0), width * 0.5f);
        GL11.glVertex3f(r2 * (float)Math.cos(0.0), r2 * (float)Math.sin(0.0), -width * 0.5f);
        GL11.glEnd();
        GL11.glShadeModel(7425);
        GL11.glBegin(8);
        for (int i = 0; i <= teeth; ++i) {
            final float angle = i * 2.0f * 3.1415927f / teeth;
            GL11.glNormal3f(-(float)Math.cos(angle), -(float)Math.sin(angle), 0.0f);
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
            GL11.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
        }
        GL11.glEnd();
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        this.rot += delta * 0.1f;
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new SlickCallableTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
