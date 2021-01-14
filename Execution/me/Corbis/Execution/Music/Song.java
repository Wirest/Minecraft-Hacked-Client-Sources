package me.Corbis.Execution.Music;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.utils.PlayMusic;

public class Song {

    String name;
    String location;

    public Song(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void play(){
        Execution.instance.soundHandler.playSound(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
