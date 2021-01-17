// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.gui;

import org.lwjgl.Sys;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class TextField extends AbstractComponent
{
    private static final int INITIAL_KEY_REPEAT_INTERVAL = 400;
    private static final int KEY_REPEAT_INTERVAL = 50;
    private int width;
    private int height;
    protected int x;
    protected int y;
    private int maxCharacter;
    private String value;
    private Font font;
    private Color border;
    private Color text;
    private Color background;
    private int cursorPos;
    private boolean visibleCursor;
    private int lastKey;
    private char lastChar;
    private long repeatTimer;
    private String oldText;
    private int oldCursorPos;
    private boolean consume;
    
    public TextField(final GUIContext container, final Font font, final int x, final int y, final int width, final int height, final ComponentListener listener) {
        this(container, font, x, y, width, height);
        this.addListener(listener);
    }
    
    public TextField(final GUIContext container, final Font font, final int x, final int y, final int width, final int height) {
        super(container);
        this.maxCharacter = 10000;
        this.value = "";
        this.border = Color.white;
        this.text = Color.white;
        this.background = new Color(0.0f, 0.0f, 0.0f, 0.5f);
        this.visibleCursor = true;
        this.lastKey = -1;
        this.lastChar = '\0';
        this.consume = true;
        this.font = font;
        this.setLocation(x, y);
        this.width = width;
        this.height = height;
    }
    
    public void setConsumeEvents(final boolean consume) {
        this.consume = consume;
    }
    
    public void deactivate() {
        this.setFocus(false);
    }
    
    @Override
    public void setLocation(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    public void setBackgroundColor(final Color color) {
        this.background = color;
    }
    
    public void setBorderColor(final Color color) {
        this.border = color;
    }
    
    public void setTextColor(final Color color) {
        this.text = color;
    }
    
    @Override
    public void render(final GUIContext container, final Graphics g) {
        if (this.lastKey != -1) {
            if (this.input.isKeyDown(this.lastKey)) {
                if (this.repeatTimer < System.currentTimeMillis()) {
                    this.repeatTimer = System.currentTimeMillis() + 50L;
                    this.keyPressed(this.lastKey, this.lastChar);
                }
            }
            else {
                this.lastKey = -1;
            }
        }
        final Rectangle oldClip = g.getClip();
        g.setWorldClip((float)this.x, (float)this.y, (float)this.width, (float)this.height);
        final Color clr = g.getColor();
        if (this.background != null) {
            g.setColor(this.background.multiply(clr));
            g.fillRect((float)this.x, (float)this.y, (float)this.width, (float)this.height);
        }
        g.setColor(this.text.multiply(clr));
        final Font temp = g.getFont();
        final int cpos = this.font.getWidth(this.value.substring(0, this.cursorPos));
        int tx = 0;
        if (cpos > this.width) {
            tx = this.width - cpos - this.font.getWidth("_");
        }
        g.translate((float)(tx + 2), 0.0f);
        g.setFont(this.font);
        g.drawString(this.value, (float)(this.x + 1), (float)(this.y + 1));
        if (this.hasFocus() && this.visibleCursor) {
            g.drawString("_", (float)(this.x + 1 + cpos + 2), (float)(this.y + 1));
        }
        g.translate((float)(-tx - 2), 0.0f);
        if (this.border != null) {
            g.setColor(this.border.multiply(clr));
            g.drawRect((float)this.x, (float)this.y, (float)this.width, (float)this.height);
        }
        g.setColor(clr);
        g.setFont(temp);
        g.clearWorldClip();
        g.setClip(oldClip);
    }
    
    public String getText() {
        return this.value;
    }
    
    public void setText(final String value) {
        this.value = value;
        if (this.cursorPos > value.length()) {
            this.cursorPos = value.length();
        }
    }
    
    public void setCursorPos(final int pos) {
        this.cursorPos = pos;
        if (this.cursorPos > this.value.length()) {
            this.cursorPos = this.value.length();
        }
    }
    
    public void setCursorVisible(final boolean visibleCursor) {
        this.visibleCursor = visibleCursor;
    }
    
    public void setMaxLength(final int length) {
        this.maxCharacter = length;
        if (this.value.length() > this.maxCharacter) {
            this.value = this.value.substring(0, this.maxCharacter);
        }
    }
    
    protected void doPaste(final String text) {
        this.recordOldPosition();
        for (int i = 0; i < text.length(); ++i) {
            this.keyPressed(-1, text.charAt(i));
        }
    }
    
    protected void recordOldPosition() {
        this.oldText = this.getText();
        this.oldCursorPos = this.cursorPos;
    }
    
    protected void doUndo(final int oldCursorPos, final String oldText) {
        if (oldText != null) {
            this.setText(oldText);
            this.setCursorPos(oldCursorPos);
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (this.hasFocus()) {
            if (key != -1) {
                if (key == 47 && (this.input.isKeyDown(29) || this.input.isKeyDown(157))) {
                    final String text = Sys.getClipboard();
                    if (text != null) {
                        this.doPaste(text);
                    }
                    return;
                }
                if (key == 44 && (this.input.isKeyDown(29) || this.input.isKeyDown(157))) {
                    if (this.oldText != null) {
                        this.doUndo(this.oldCursorPos, this.oldText);
                    }
                    return;
                }
                if (this.input.isKeyDown(29) || this.input.isKeyDown(157)) {
                    return;
                }
                if (this.input.isKeyDown(56) || this.input.isKeyDown(184)) {
                    return;
                }
            }
            if (this.lastKey != key) {
                this.lastKey = key;
                this.repeatTimer = System.currentTimeMillis() + 400L;
            }
            else {
                this.repeatTimer = System.currentTimeMillis() + 50L;
            }
            this.lastChar = c;
            if (key == 203) {
                if (this.cursorPos > 0) {
                    --this.cursorPos;
                }
                if (this.consume) {
                    this.container.getInput().consumeEvent();
                }
            }
            else if (key == 205) {
                if (this.cursorPos < this.value.length()) {
                    ++this.cursorPos;
                }
                if (this.consume) {
                    this.container.getInput().consumeEvent();
                }
            }
            else if (key == 14) {
                if (this.cursorPos > 0 && this.value.length() > 0) {
                    if (this.cursorPos < this.value.length()) {
                        this.value = String.valueOf(this.value.substring(0, this.cursorPos - 1)) + this.value.substring(this.cursorPos);
                    }
                    else {
                        this.value = this.value.substring(0, this.cursorPos - 1);
                    }
                    --this.cursorPos;
                }
                if (this.consume) {
                    this.container.getInput().consumeEvent();
                }
            }
            else if (key == 211) {
                if (this.value.length() > this.cursorPos) {
                    this.value = String.valueOf(this.value.substring(0, this.cursorPos)) + this.value.substring(this.cursorPos + 1);
                }
                if (this.consume) {
                    this.container.getInput().consumeEvent();
                }
            }
            else if (c < '\u007f' && c > '\u001f' && this.value.length() < this.maxCharacter) {
                if (this.cursorPos < this.value.length()) {
                    this.value = String.valueOf(this.value.substring(0, this.cursorPos)) + c + this.value.substring(this.cursorPos);
                }
                else {
                    this.value = String.valueOf(this.value.substring(0, this.cursorPos)) + c;
                }
                ++this.cursorPos;
                if (this.consume) {
                    this.container.getInput().consumeEvent();
                }
            }
            else if (key == 28) {
                this.notifyListeners();
                if (this.consume) {
                    this.container.getInput().consumeEvent();
                }
            }
        }
    }
    
    @Override
    public void setFocus(final boolean focus) {
        this.lastKey = -1;
        super.setFocus(focus);
    }
}
