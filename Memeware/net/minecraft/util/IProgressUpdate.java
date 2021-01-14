package net.minecraft.util;

public interface IProgressUpdate {
    /**
     * Shows the 'Saving level' string.
     */
    void displaySavingString(String var1);

    /**
     * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
     * and the WorkingString to "working...".
     */
    void resetProgressAndMessage(String var1);

    /**
     * Displays a string on the loading screen supposed to indicate what is being done currently.
     */
    void displayLoadingString(String var1);

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    void setLoadingProgress(int var1);

    void setDoneWorking();
}
