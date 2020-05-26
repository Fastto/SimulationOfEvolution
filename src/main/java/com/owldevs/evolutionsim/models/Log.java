package com.owldevs.evolutionsim.models;

import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;

public class Log implements Drawable {

    private Integer width;
    private Integer height;

    private ArrayList<String> data = new ArrayList<>();

    public void draw(Graphics graphics) {
        int y = 0;
        graphics.setColor(new Color(0xFF, 0xFF, 0xFF, 0x99));
        for (int i = data.size() - 1; i >= 0; i--) {
            graphics.drawString(data.get(i), 0, y + 10);
            y += 10;

            if (y > height) break;
        }
    }

    public Log(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public void addLn(String line) {
        data.add(line);
    }

    public void add(String line) {
        if(data.size() == 0 ) {
            data.add("");
        }

        data.set(data.size() - 1, data.get(data.size()-1) + line);
    }
}
