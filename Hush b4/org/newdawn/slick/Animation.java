// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.lwjgl.Sys;
import org.newdawn.slick.util.Log;
import java.util.ArrayList;

public class Animation implements Renderable
{
    private ArrayList frames;
    private int currentFrame;
    private long nextChange;
    private boolean stopped;
    private long timeLeft;
    private float speed;
    private int stopAt;
    private long lastUpdate;
    private boolean firstUpdate;
    private boolean autoUpdate;
    private int direction;
    private boolean pingPong;
    private boolean loop;
    private SpriteSheet spriteSheet;
    
    public Animation() {
        this(true);
    }
    
    public Animation(final Image[] frames, final int duration) {
        this(frames, duration, true);
    }
    
    public Animation(final Image[] frames, final int[] durations) {
        this(frames, durations, true);
    }
    
    public Animation(final boolean autoUpdate) {
        this.frames = new ArrayList();
        this.currentFrame = -1;
        this.nextChange = 0L;
        this.stopped = false;
        this.speed = 1.0f;
        this.stopAt = -2;
        this.firstUpdate = true;
        this.autoUpdate = true;
        this.direction = 1;
        this.loop = true;
        this.spriteSheet = null;
        this.currentFrame = 0;
        this.autoUpdate = autoUpdate;
    }
    
    public Animation(final Image[] frames, final int duration, final boolean autoUpdate) {
        this.frames = new ArrayList();
        this.currentFrame = -1;
        this.nextChange = 0L;
        this.stopped = false;
        this.speed = 1.0f;
        this.stopAt = -2;
        this.firstUpdate = true;
        this.autoUpdate = true;
        this.direction = 1;
        this.loop = true;
        this.spriteSheet = null;
        for (int i = 0; i < frames.length; ++i) {
            this.addFrame(frames[i], duration);
        }
        this.currentFrame = 0;
        this.autoUpdate = autoUpdate;
    }
    
    public Animation(final Image[] frames, final int[] durations, final boolean autoUpdate) {
        this.frames = new ArrayList();
        this.currentFrame = -1;
        this.nextChange = 0L;
        this.stopped = false;
        this.speed = 1.0f;
        this.stopAt = -2;
        this.firstUpdate = true;
        this.autoUpdate = true;
        this.direction = 1;
        this.loop = true;
        this.spriteSheet = null;
        this.autoUpdate = autoUpdate;
        if (frames.length != durations.length) {
            throw new RuntimeException("There must be one duration per frame");
        }
        for (int i = 0; i < frames.length; ++i) {
            this.addFrame(frames[i], durations[i]);
        }
        this.currentFrame = 0;
    }
    
    public Animation(final SpriteSheet frames, final int duration) {
        this(frames, 0, 0, frames.getHorizontalCount() - 1, frames.getVerticalCount() - 1, true, duration, true);
    }
    
    public Animation(final SpriteSheet frames, final int x1, final int y1, final int x2, final int y2, final boolean horizontalScan, final int duration, final boolean autoUpdate) {
        this.frames = new ArrayList();
        this.currentFrame = -1;
        this.nextChange = 0L;
        this.stopped = false;
        this.speed = 1.0f;
        this.stopAt = -2;
        this.firstUpdate = true;
        this.autoUpdate = true;
        this.direction = 1;
        this.loop = true;
        this.spriteSheet = null;
        this.autoUpdate = autoUpdate;
        if (!horizontalScan) {
            for (int x3 = x1; x3 <= x2; ++x3) {
                for (int y3 = y1; y3 <= y2; ++y3) {
                    this.addFrame(frames.getSprite(x3, y3), duration);
                }
            }
        }
        else {
            for (int y4 = y1; y4 <= y2; ++y4) {
                for (int x4 = x1; x4 <= x2; ++x4) {
                    this.addFrame(frames.getSprite(x4, y4), duration);
                }
            }
        }
    }
    
    public Animation(final SpriteSheet ss, final int[] frames, final int[] duration) {
        this.frames = new ArrayList();
        this.currentFrame = -1;
        this.nextChange = 0L;
        this.stopped = false;
        this.speed = 1.0f;
        this.stopAt = -2;
        this.firstUpdate = true;
        this.autoUpdate = true;
        this.direction = 1;
        this.loop = true;
        this.spriteSheet = null;
        this.spriteSheet = ss;
        int x = -1;
        int y = -1;
        for (int i = 0; i < frames.length / 2; ++i) {
            x = frames[i * 2];
            y = frames[i * 2 + 1];
            this.addFrame(duration[i], x, y);
        }
    }
    
    public void addFrame(final int duration, final int x, final int y) {
        if (duration == 0) {
            Log.error("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }
        if (this.frames.isEmpty()) {
            this.nextChange = (int)(duration / this.speed);
        }
        this.frames.add(new Frame(duration, x, y));
        this.currentFrame = 0;
    }
    
    public void setAutoUpdate(final boolean auto) {
        this.autoUpdate = auto;
    }
    
    public void setPingPong(final boolean pingPong) {
        this.pingPong = pingPong;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
    
    public void setSpeed(final float spd) {
        if (spd > 0.0f) {
            this.nextChange = (long)(this.nextChange * this.speed / spd);
            this.speed = spd;
        }
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public void stop() {
        if (this.frames.size() == 0) {
            return;
        }
        this.timeLeft = this.nextChange;
        this.stopped = true;
    }
    
    public void start() {
        if (!this.stopped) {
            return;
        }
        if (this.frames.size() == 0) {
            return;
        }
        this.stopped = false;
        this.nextChange = this.timeLeft;
    }
    
    public void restart() {
        if (this.frames.size() == 0) {
            return;
        }
        this.stopped = false;
        this.currentFrame = 0;
        this.nextChange = (int)(this.frames.get(0).duration / this.speed);
        this.firstUpdate = true;
        this.lastUpdate = 0L;
    }
    
    public void addFrame(final Image frame, final int duration) {
        if (duration == 0) {
            Log.error("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }
        if (this.frames.isEmpty()) {
            this.nextChange = (int)(duration / this.speed);
        }
        this.frames.add(new Frame(frame, duration));
        this.currentFrame = 0;
    }
    
    public void draw() {
        this.draw(0.0f, 0.0f);
    }
    
    @Override
    public void draw(final float x, final float y) {
        this.draw(x, y, (float)this.getWidth(), (float)this.getHeight());
    }
    
    public void draw(final float x, final float y, final Color filter) {
        this.draw(x, y, (float)this.getWidth(), (float)this.getHeight(), filter);
    }
    
    public void draw(final float x, final float y, final float width, final float height) {
        this.draw(x, y, width, height, Color.white);
    }
    
    public void draw(final float x, final float y, final float width, final float height, final Color col) {
        if (this.frames.size() == 0) {
            return;
        }
        if (this.autoUpdate) {
            final long now = this.getTime();
            long delta = now - this.lastUpdate;
            if (this.firstUpdate) {
                delta = 0L;
                this.firstUpdate = false;
            }
            this.lastUpdate = now;
            this.nextFrame(delta);
        }
        final Frame frame = this.frames.get(this.currentFrame);
        frame.image.draw(x, y, width, height, col);
    }
    
    public void renderInUse(final int x, final int y) {
        if (this.frames.size() == 0) {
            return;
        }
        if (this.autoUpdate) {
            final long now = this.getTime();
            long delta = now - this.lastUpdate;
            if (this.firstUpdate) {
                delta = 0L;
                this.firstUpdate = false;
            }
            this.lastUpdate = now;
            this.nextFrame(delta);
        }
        final Frame frame = this.frames.get(this.currentFrame);
        this.spriteSheet.renderInUse(x, y, frame.x, frame.y);
    }
    
    public int getWidth() {
        return this.frames.get(this.currentFrame).image.getWidth();
    }
    
    public int getHeight() {
        return this.frames.get(this.currentFrame).image.getHeight();
    }
    
    public void drawFlash(final float x, final float y, final float width, final float height) {
        this.drawFlash(x, y, width, height, Color.white);
    }
    
    public void drawFlash(final float x, final float y, final float width, final float height, final Color col) {
        if (this.frames.size() == 0) {
            return;
        }
        if (this.autoUpdate) {
            final long now = this.getTime();
            long delta = now - this.lastUpdate;
            if (this.firstUpdate) {
                delta = 0L;
                this.firstUpdate = false;
            }
            this.lastUpdate = now;
            this.nextFrame(delta);
        }
        final Frame frame = this.frames.get(this.currentFrame);
        frame.image.drawFlash(x, y, width, height, col);
    }
    
    @Deprecated
    public void updateNoDraw() {
        if (this.autoUpdate) {
            final long now = this.getTime();
            long delta = now - this.lastUpdate;
            if (this.firstUpdate) {
                delta = 0L;
                this.firstUpdate = false;
            }
            this.lastUpdate = now;
            this.nextFrame(delta);
        }
    }
    
    public void update(final long delta) {
        this.nextFrame(delta);
    }
    
    public int getFrame() {
        return this.currentFrame;
    }
    
    public void setCurrentFrame(final int index) {
        this.currentFrame = index;
    }
    
    public Image getImage(final int index) {
        final Frame frame = this.frames.get(index);
        return frame.image;
    }
    
    public int getFrameCount() {
        return this.frames.size();
    }
    
    public Image getCurrentFrame() {
        final Frame frame = this.frames.get(this.currentFrame);
        return frame.image;
    }
    
    private void nextFrame(final long delta) {
        if (this.stopped) {
            return;
        }
        if (this.frames.size() == 0) {
            return;
        }
        this.nextChange -= delta;
        while (this.nextChange < 0L && !this.stopped) {
            if (this.currentFrame == this.stopAt) {
                this.stopped = true;
                break;
            }
            if (this.currentFrame == this.frames.size() - 1 && !this.loop && !this.pingPong) {
                this.stopped = true;
                break;
            }
            this.currentFrame = (this.currentFrame + this.direction) % this.frames.size();
            if (this.pingPong) {
                if (this.currentFrame <= 0) {
                    this.currentFrame = 0;
                    this.direction = 1;
                    if (!this.loop) {
                        this.stopped = true;
                        break;
                    }
                }
                else if (this.currentFrame >= this.frames.size() - 1) {
                    this.currentFrame = this.frames.size() - 1;
                    this.direction = -1;
                }
            }
            final int realDuration = (int)(this.frames.get(this.currentFrame).duration / this.speed);
            this.nextChange += realDuration;
        }
    }
    
    public void setLooping(final boolean loop) {
        this.loop = loop;
    }
    
    private long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
    
    public void stopAt(final int frameIndex) {
        this.stopAt = frameIndex;
    }
    
    public int getDuration(final int index) {
        return this.frames.get(index).duration;
    }
    
    public void setDuration(final int index, final int duration) {
        this.frames.get(index).duration = duration;
    }
    
    public int[] getDurations() {
        final int[] durations = new int[this.frames.size()];
        for (int i = 0; i < this.frames.size(); ++i) {
            durations[i] = this.getDuration(i);
        }
        return durations;
    }
    
    @Override
    public String toString() {
        String res = "[Animation (" + this.frames.size() + ") ";
        for (int i = 0; i < this.frames.size(); ++i) {
            final Frame frame = this.frames.get(i);
            res = String.valueOf(res) + frame.duration + ",";
        }
        res = String.valueOf(res) + "]";
        return res;
    }
    
    public Animation copy() {
        final Animation copy = new Animation();
        copy.spriteSheet = this.spriteSheet;
        copy.frames = this.frames;
        copy.autoUpdate = this.autoUpdate;
        copy.direction = this.direction;
        copy.loop = this.loop;
        copy.pingPong = this.pingPong;
        copy.speed = this.speed;
        return copy;
    }
    
    private class Frame
    {
        public Image image;
        public int duration;
        public int x;
        public int y;
        
        public Frame(final Image image, final int duration) {
            this.x = -1;
            this.y = -1;
            this.image = image;
            this.duration = duration;
        }
        
        public Frame(final int duration, final int x, final int y) {
            this.x = -1;
            this.y = -1;
            this.image = Animation.this.spriteSheet.getSubImage(x, y);
            this.duration = duration;
            this.x = x;
            this.y = y;
        }
    }
}
