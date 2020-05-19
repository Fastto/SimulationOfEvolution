package com.owldevs.evolutionsim.models;

import java.awt.*;

public class Food implements Drawable {

    public int x;
    public int y;

    public Float weight;
    public Float sizeToWeightK;
    public Integer borderWidth;

    public Food(int x, int y) {

        this.x = x;
        this.y = y;

        weight = 10f;
        sizeToWeightK = 0.5f;
        borderWidth = 3;

    }

    public void draw(Graphics graphics) {

        //draw border
        graphics.setColor(Color.white);

        Integer diameter = Math.round(weight * sizeToWeightK);
        Integer radius = Math.round(weight * sizeToWeightK / 2);

        graphics.fillOval(x - radius - borderWidth, y - radius - borderWidth,
                diameter + borderWidth * 2, diameter + borderWidth * 2);

        //draw body
        graphics.setColor(Color.pink);
        graphics.fillOval(x - radius, y - radius, diameter, diameter);


    }

}
