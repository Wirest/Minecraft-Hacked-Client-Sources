package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;

public class Event2D extends Event {
    double width, height;
    public Event2D(double width, double height){
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
