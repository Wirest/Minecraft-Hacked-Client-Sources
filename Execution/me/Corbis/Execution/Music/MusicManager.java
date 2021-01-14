package me.Corbis.Execution.Music;

import me.Corbis.Execution.Music.Songs.StiletoStateOfMind;

import java.util.ArrayList;

public class MusicManager {

    ArrayList<Song> songs = new ArrayList<>();


    public MusicManager(){
        songs.add(new StiletoStateOfMind());
    }

    public Song getSongByName(String name){
        for(Song song : songs){
            if(song.getName().equalsIgnoreCase(name)){
                return song;
            }
        }
        return null;
    }

}
