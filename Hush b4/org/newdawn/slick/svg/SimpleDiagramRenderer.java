// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class SimpleDiagramRenderer
{
    protected static SGL GL;
    public Diagram diagram;
    public int list;
    
    static {
        SimpleDiagramRenderer.GL = Renderer.get();
    }
    
    public SimpleDiagramRenderer(final Diagram diagram) {
        this.list = -1;
        this.diagram = diagram;
    }
    
    public void render(final Graphics g) {
        if (this.list == -1) {
            this.list = SimpleDiagramRenderer.GL.glGenLists(1);
            SimpleDiagramRenderer.GL.glNewList(this.list, 4864);
            render(g, this.diagram);
            SimpleDiagramRenderer.GL.glEndList();
        }
        SimpleDiagramRenderer.GL.glCallList(this.list);
        TextureImpl.bindNone();
    }
    
    public static void render(final Graphics g, final Diagram diagram) {
        for (int i = 0; i < diagram.getFigureCount(); ++i) {
            final Figure figure = diagram.getFigure(i);
            if (figure.getData().isFilled()) {
                if (figure.getData().isColor("fill")) {
                    g.setColor(figure.getData().getAsColor("fill"));
                    g.fill(diagram.getFigure(i).getShape());
                    g.setAntiAlias(true);
                    g.draw(diagram.getFigure(i).getShape());
                    g.setAntiAlias(false);
                }
                final String fill = figure.getData().getAsReference("fill");
                if (diagram.getPatternDef(fill) != null) {
                    System.out.println("PATTERN");
                }
                if (diagram.getGradient(fill) != null) {
                    final Gradient gradient = diagram.getGradient(fill);
                    final Shape shape = diagram.getFigure(i).getShape();
                    TexCoordGenerator fg = null;
                    if (gradient.isRadial()) {
                        fg = new RadialGradientFill(shape, diagram.getFigure(i).getTransform(), gradient);
                    }
                    else {
                        fg = new LinearGradientFill(shape, diagram.getFigure(i).getTransform(), gradient);
                    }
                    Color.white.bind();
                    ShapeRenderer.texture(shape, gradient.getImage(), fg);
                }
            }
            if (figure.getData().isStroked() && figure.getData().isColor("stroke")) {
                g.setColor(figure.getData().getAsColor("stroke"));
                g.setLineWidth(figure.getData().getAsFloat("stroke-width"));
                g.setAntiAlias(true);
                g.draw(diagram.getFigure(i).getShape());
                g.setAntiAlias(false);
                g.resetLineWidth();
            }
        }
    }
}
