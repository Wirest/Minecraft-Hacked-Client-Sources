// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

public class CommandThread extends SimpleThread
{
    protected SoundSystemLogger logger;
    private SoundSystem soundSystem;
    protected String className;
    
    public CommandThread(final SoundSystem s) {
        this.className = "CommandThread";
        this.logger = SoundSystemConfig.getLogger();
        this.soundSystem = s;
    }
    
    @Override
    protected void cleanup() {
        this.kill();
        this.logger = null;
        this.soundSystem = null;
        super.cleanup();
    }
    
    @Override
    public void run() {
        long currentTime;
        long previousTime = currentTime = System.currentTimeMillis();
        if (this.soundSystem == null) {
            this.errorMessage("SoundSystem was null in method run().", 0);
            this.cleanup();
            return;
        }
        this.snooze(3600000L);
        while (!this.dying()) {
            this.soundSystem.ManageSources();
            this.soundSystem.CommandQueue(null);
            currentTime = System.currentTimeMillis();
            if (!this.dying() && currentTime - previousTime > 10000L) {
                previousTime = currentTime;
                this.soundSystem.removeTemporarySources();
            }
            if (!this.dying()) {
                this.snooze(3600000L);
            }
        }
        this.cleanup();
    }
    
    protected void message(final String message, final int indent) {
        this.logger.message(message, indent);
    }
    
    protected void importantMessage(final String message, final int indent) {
        this.logger.importantMessage(message, indent);
    }
    
    protected boolean errorCheck(final boolean error, final String message) {
        return this.logger.errorCheck(error, this.className, message, 0);
    }
    
    protected void errorMessage(final String message, final int indent) {
        this.logger.errorMessage(this.className, message, indent);
    }
}
