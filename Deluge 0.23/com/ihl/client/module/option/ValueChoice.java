package com.ihl.client.module.option;

public class ValueChoice extends Value {

    public String[] list;

    public ValueChoice(int value, String[] list) {
        super(list[value]);
        this.list = list;
    }

}
