package cedo.util;

import paulscode.sound.Library;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

// http://www.paulscode.com/tutorials/SoundSystem/SoundSystem.pdf

public class AudioUtil {
    private static SoundSystem mySoundSystem;

    public static void init() {
        boolean aLCompatible = SoundSystem.libraryCompatible(LibraryLWJGLOpenAL.class);
        boolean jSCompatible = SoundSystem.libraryCompatible(LibraryJavaSound.class);
        Class libraryType;
        try {
            SoundSystemConfig.addLibrary(LibraryJavaSound.class);
        } catch (SoundSystemException e) {
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }
        try {
            SoundSystemConfig.setCodec("wav", CodecWav.class);
        } catch (SoundSystemException e) {
            System.err.println("error linking with the CodecWav plug-in");
        }

        //if( aLCompatible )
        //    libraryType = LibraryLWJGLOpenAL.class; // OpenAL
        if (jSCompatible)
            libraryType = LibraryJavaSound.class; // Java Sound
        else
            libraryType = Library.class; // "No Sound, Silent Mode"
        try {
            mySoundSystem = new SoundSystem(libraryType);
        } catch (SoundSystemException sse) {
            // Shouldn’t happen, but it is best to prepare for anything
            sse.printStackTrace();
        }
    }

    public static void PlaySound(String filename) {
        boolean priority = false;
        String sourcename = "Source 1";
        boolean loop = false;
        float x = 0, y = 0, z = 0;
        int aModel = SoundSystemConfig.ATTENUATION_ROLLOFF;
        float rFactor = SoundSystemConfig.getDefaultRolloff();
        mySoundSystem.newSource(false, sourcename, filename, false, x, y, z, aModel, rFactor);
    }

    public static void PlaySound() {
        boolean priority = false;
        String sourcename = "Source 1";
        String filename = "explosion.wav";
        boolean loop = false;
        float x = 0, y = 0, z = 0;
        int aModel = SoundSystemConfig.ATTENUATION_ROLLOFF;
        float rFactor = SoundSystemConfig.getDefaultRolloff();
        mySoundSystem.newSource(false, sourcename, filename, false, x, y, z, aModel, rFactor);
    }
}
