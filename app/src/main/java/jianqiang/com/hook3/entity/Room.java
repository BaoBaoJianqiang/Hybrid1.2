package jianqiang.com.hook3.entity;

import java.io.Serializable;

public class Room implements Serializable {
    private String roomType;
    private int price;

    public Room(String roomType, int price) {
        this.roomType = roomType;
        this.price = price;
    }
}