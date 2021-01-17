// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.dnd;

import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.GraphicsConfiguration;
import java.awt.Cursor;
import java.awt.dnd.InvalidDnDOperationException;
import javax.swing.Icon;
import java.awt.dnd.DragGestureEvent;
import javax.swing.JFileChooser;
import javax.swing.JColorChooser;
import javax.swing.text.JTextComponent;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JTree;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.Point;
import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.Dimension;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DragSourceListener;

public abstract class DragHandler implements DragSourceListener, DragSourceMotionListener, DragGestureListener
{
    public static final Dimension MAX_GHOST_SIZE;
    public static final float DEFAULT_GHOST_ALPHA = 0.5f;
    public static final int UNKNOWN_MODIFIERS = -1;
    public static final Transferable UNKNOWN_TRANSFERABLE;
    protected static final int MOVE = 2;
    protected static final int COPY = 1;
    protected static final int LINK = 1073741824;
    protected static final int NONE = 0;
    static final int MOVE_MASK = 64;
    static final boolean OSX;
    static final int COPY_MASK;
    static final int LINK_MASK;
    static final int KEY_MASK = 9152;
    private static int modifiers;
    private static Transferable transferable;
    private int supportedActions;
    private boolean fixCursor;
    private Component dragSource;
    private GhostedDragImage ghost;
    private Point imageOffset;
    private Dimension maxGhostSize;
    private float ghostAlpha;
    private String lastAction;
    private boolean moved;
    
    static int getModifiers() {
        return DragHandler.modifiers;
    }
    
    public static Transferable getTransferable(final DropTargetEvent e) {
        if (e instanceof DropTargetDragEvent) {
            try {
                return (Transferable)e.getClass().getMethod("getTransferable", (Class<?>[])null).invoke(e, (Object[])null);
            }
            catch (Exception ex) {
                return DragHandler.transferable;
            }
        }
        if (e instanceof DropTargetDropEvent) {
            return ((DropTargetDropEvent)e).getTransferable();
        }
        return DragHandler.transferable;
    }
    
    protected DragHandler(final Component dragSource, final int actions) {
        this.fixCursor = true;
        this.maxGhostSize = DragHandler.MAX_GHOST_SIZE;
        this.ghostAlpha = 0.5f;
        this.dragSource = dragSource;
        this.supportedActions = actions;
        try {
            final String alpha = System.getProperty("DragHandler.alpha");
            if (alpha != null) {
                try {
                    this.ghostAlpha = Float.parseFloat(alpha);
                }
                catch (NumberFormatException ex) {}
            }
            final String max = System.getProperty("DragHandler.maxDragImageSize");
            if (max != null) {
                final String[] size = max.split("x");
                if (size.length == 2) {
                    try {
                        this.maxGhostSize = new Dimension(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                    }
                    catch (NumberFormatException ex2) {}
                }
            }
        }
        catch (SecurityException ex3) {}
        this.disableSwingDragSupport(dragSource);
        final DragSource src = DragSource.getDefaultDragSource();
        src.createDefaultDragGestureRecognizer(dragSource, this.supportedActions, this);
    }
    
    private void disableSwingDragSupport(final Component comp) {
        if (comp instanceof JTree) {
            ((JTree)comp).setDragEnabled(false);
        }
        else if (comp instanceof JList) {
            ((JList)comp).setDragEnabled(false);
        }
        else if (comp instanceof JTable) {
            ((JTable)comp).setDragEnabled(false);
        }
        else if (comp instanceof JTextComponent) {
            ((JTextComponent)comp).setDragEnabled(false);
        }
        else if (comp instanceof JColorChooser) {
            ((JColorChooser)comp).setDragEnabled(false);
        }
        else if (comp instanceof JFileChooser) {
            ((JFileChooser)comp).setDragEnabled(false);
        }
    }
    
    protected boolean canDrag(final DragGestureEvent e) {
        final int mods = e.getTriggerEvent().getModifiersEx() & 0x23C0;
        if (mods == 64) {
            return (this.supportedActions & 0x2) != 0x0;
        }
        if (mods == DragHandler.COPY_MASK) {
            return (this.supportedActions & 0x1) != 0x0;
        }
        return mods != DragHandler.LINK_MASK || (this.supportedActions & 0x40000000) != 0x0;
    }
    
    protected void setModifiers(final int mods) {
        DragHandler.modifiers = mods;
    }
    
    protected abstract Transferable getTransferable(final DragGestureEvent p0);
    
    protected Icon getDragIcon(final DragGestureEvent e, final Point srcOffset) {
        return null;
    }
    
    protected void dragStarted(final DragGestureEvent e) {
    }
    
    public void dragGestureRecognized(final DragGestureEvent e) {
        if ((e.getDragAction() & this.supportedActions) != 0x0 && this.canDrag(e)) {
            this.setModifiers(e.getTriggerEvent().getModifiersEx() & 0x23C0);
            final Transferable transferable = this.getTransferable(e);
            if (transferable == null) {
                return;
            }
            try {
                final Point srcOffset = new Point(0, 0);
                final Icon icon = this.getDragIcon(e, srcOffset);
                final Point origin = e.getDragOrigin();
                this.imageOffset = new Point(srcOffset.x - origin.x, srcOffset.y - origin.y);
                final Icon dragIcon = this.scaleDragIcon(icon, this.imageOffset);
                final Cursor cursor = null;
                if (dragIcon != null && DragSource.isDragImageSupported()) {
                    final GraphicsConfiguration gc = e.getComponent().getGraphicsConfiguration();
                    e.startDrag(cursor, this.createDragImage(gc, dragIcon), this.imageOffset, transferable, this);
                }
                else {
                    if (dragIcon != null) {
                        final Point screen = this.dragSource.getLocationOnScreen();
                        screen.translate(origin.x, origin.y);
                        final Point cursorOffset = new Point(-this.imageOffset.x, -this.imageOffset.y);
                        (this.ghost = new GhostedDragImage(this.dragSource, dragIcon, this.getImageLocation(screen), cursorOffset)).setAlpha(this.ghostAlpha);
                    }
                    e.startDrag(cursor, transferable, this);
                }
                this.dragStarted(e);
                this.moved = false;
                e.getDragSource().addDragSourceMotionListener(this);
                DragHandler.transferable = transferable;
            }
            catch (InvalidDnDOperationException ex) {
                if (this.ghost != null) {
                    this.ghost.dispose();
                    this.ghost = null;
                }
            }
        }
    }
    
    protected Icon scaleDragIcon(final Icon icon, final Point imageOffset) {
        return icon;
    }
    
    protected Image createDragImage(final GraphicsConfiguration gc, final Icon icon) {
        final int w = icon.getIconWidth();
        final int h = icon.getIconHeight();
        final BufferedImage image = gc.createCompatibleImage(w, h, 3);
        final Graphics2D g = (Graphics2D)image.getGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, w, h);
        g.setComposite(AlphaComposite.getInstance(2, this.ghostAlpha));
        icon.paintIcon(this.dragSource, g, 0, 0);
        g.dispose();
        return image;
    }
    
    private int reduce(final int actions) {
        if ((actions & 0x2) != 0x0 && actions != 2) {
            return 2;
        }
        if ((actions & 0x1) != 0x0 && actions != 1) {
            return 1;
        }
        return actions;
    }
    
    protected Cursor getCursorForAction(final int actualAction) {
        switch (actualAction) {
            case 2: {
                return DragSource.DefaultMoveDrop;
            }
            case 1: {
                return DragSource.DefaultCopyDrop;
            }
            case 1073741824: {
                return DragSource.DefaultLinkDrop;
            }
            default: {
                return DragSource.DefaultMoveNoDrop;
            }
        }
    }
    
    protected int getAcceptableDropAction(final int targetActions) {
        return this.reduce(this.supportedActions & targetActions);
    }
    
    protected int getDropAction(final DragSourceEvent ev) {
        if (ev instanceof DragSourceDragEvent) {
            final DragSourceDragEvent e = (DragSourceDragEvent)ev;
            return e.getDropAction();
        }
        if (ev instanceof DragSourceDropEvent) {
            return ((DragSourceDropEvent)ev).getDropAction();
        }
        return 0;
    }
    
    protected int adjustDropAction(final DragSourceEvent ev) {
        int action = this.getDropAction(ev);
        if (ev instanceof DragSourceDragEvent) {
            final DragSourceDragEvent e = (DragSourceDragEvent)ev;
            if (action == 0) {
                final int mods = e.getGestureModifiersEx() & 0x23C0;
                if (mods == 0) {
                    action = this.getAcceptableDropAction(e.getTargetActions());
                }
            }
        }
        return action;
    }
    
    protected void updateCursor(final DragSourceEvent ev) {
        if (!this.fixCursor) {
            return;
        }
        final Cursor cursor = this.getCursorForAction(this.adjustDropAction(ev));
        ev.getDragSourceContext().setCursor(cursor);
    }
    
    static String actionString(final int action) {
        switch (action) {
            case 2: {
                return "MOVE";
            }
            case 3: {
                return "MOVE|COPY";
            }
            case 1073741826: {
                return "MOVE|LINK";
            }
            case 1073741827: {
                return "MOVE|COPY|LINK";
            }
            case 1: {
                return "COPY";
            }
            case 1073741825: {
                return "COPY|LINK";
            }
            case 1073741824: {
                return "LINK";
            }
            default: {
                return "NONE";
            }
        }
    }
    
    private void describe(final String type, final DragSourceEvent e) {
    }
    
    public void dragDropEnd(final DragSourceDropEvent e) {
        this.describe("end", e);
        this.setModifiers(-1);
        DragHandler.transferable = DragHandler.UNKNOWN_TRANSFERABLE;
        if (this.ghost != null) {
            if (e.getDropSuccess()) {
                this.ghost.dispose();
            }
            else {
                this.ghost.returnToOrigin();
            }
            this.ghost = null;
        }
        final DragSource src = e.getDragSourceContext().getDragSource();
        src.removeDragSourceMotionListener(this);
        this.moved = false;
    }
    
    private Point getImageLocation(final Point where) {
        where.translate(this.imageOffset.x, this.imageOffset.y);
        return where;
    }
    
    public void dragEnter(final DragSourceDragEvent e) {
        this.describe("enter", e);
        if (this.ghost != null) {
            this.ghost.move(this.getImageLocation(e.getLocation()));
        }
        this.updateCursor(e);
    }
    
    public void dragMouseMoved(final DragSourceDragEvent e) {
        this.describe("move", e);
        if (this.ghost != null) {
            this.ghost.move(this.getImageLocation(e.getLocation()));
        }
        if (this.moved) {
            this.updateCursor(e);
        }
        this.moved = true;
    }
    
    public void dragOver(final DragSourceDragEvent e) {
        this.describe("over", e);
        if (this.ghost != null) {
            this.ghost.move(this.getImageLocation(e.getLocation()));
        }
        this.updateCursor(e);
    }
    
    public void dragExit(final DragSourceEvent e) {
        this.describe("exit", e);
    }
    
    public void dropActionChanged(final DragSourceDragEvent e) {
        this.describe("change", e);
        this.setModifiers(e.getGestureModifiersEx() & 0x23C0);
        if (this.ghost != null) {
            this.ghost.move(this.getImageLocation(e.getLocation()));
        }
        this.updateCursor(e);
    }
    
    static {
        MAX_GHOST_SIZE = new Dimension(250, 250);
        UNKNOWN_TRANSFERABLE = null;
        OSX = (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1);
        COPY_MASK = (DragHandler.OSX ? 512 : 128);
        LINK_MASK = (DragHandler.OSX ? 768 : 192);
        DragHandler.modifiers = -1;
        DragHandler.transferable = DragHandler.UNKNOWN_TRANSFERABLE;
    }
}
