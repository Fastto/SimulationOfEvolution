package com.owldevs.evolutionsim.models;

import java.awt.*;
import java.util.AbstractMap;
import java.util.LinkedList;

public class Graph implements Drawable {

    private Integer frqs = 1;
    private Integer current = 1;

    private Integer width;
    private Integer height;

    private LinkedList<AbstractMap.SimpleEntry<Integer, Integer>> data = new LinkedList<>();

    public void draw(Graphics graphics) {

        graphics.setColor(new Color(0xFF, 0xFF, 0xFF, 0x99));

        for (AbstractMap.SimpleEntry<Integer, Integer> dot : data) {
            graphics.drawLine(dot.getKey(), height - dot.getValue(), dot.getKey(), height);
        }

    }

    public Graph(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public void add(Integer y) {
        if (current >= frqs) {
            data.add(new AbstractMap.SimpleEntry<Integer, Integer>(data.size(), y));
            current = 0;
        }
        current++;

        if (data.size() > width) {
            frqs *= 2;
            LinkedList<AbstractMap.SimpleEntry<Integer, Integer>> newData = new LinkedList<>();
            Integer x = 0;
            for (int i = 0; i < data.size(); i++) {
                if(Math.ceil(i%2) == 0) {
                    newData.add(new AbstractMap.SimpleEntry<Integer, Integer>(x, data.get(i).getValue()));
                    x++;
                }
            }
            data = newData;
        }
    }
}
