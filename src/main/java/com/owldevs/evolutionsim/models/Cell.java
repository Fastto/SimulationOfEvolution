package com.owldevs.evolutionsim.models;

import com.owldevs.nn4j.NeuralNetwork;

import java.awt.*;
import java.util.AbstractMap;

public class Cell implements Drawable {

    //Types of cell
    enum Type {
        CELL
    }

    //Brainn
    public NeuralNetwork nn;

    //skills
    public Color color;
    public Type type;
    public Float maxSpeed;
    public Integer weightToDiv;
    public Float hungryMadness;

    //properties
    public Integer hungrySteps;
    public Integer totalEaten;
    public Float weight;
    public Float sizeToWeightK;
    public Integer borderWidth;
    public Float foodScore;
    public int x;
    public int y;
    public Boolean isDead;

    //todo: refactor
    public Food nearFood;


    public Cell(int x, int y) {
        this.x = x;
        this.y = y;

        color = Color.white;
        type = Type.CELL;

        maxSpeed = 7f;
        weightToDiv = 30;
        hungryMadness = .01f;
        sizeToWeightK = 1f;
        borderWidth = 3;
        foodScore = 1f;

        nn = new NeuralNetwork(0, 2, 2);
        hungrySteps = 0;
        totalEaten = 0;
        weight = 20f;
        isDead = false;
    }

    public void draw(Graphics graphics) {

        //draw border
        graphics.setColor(Color.black);
        Integer diameter = Math.round(weight * sizeToWeightK);
        Integer radius = Math.round(weight * sizeToWeightK / 2);

        graphics.fillOval(x - radius - borderWidth, y - radius - borderWidth,
                diameter + borderWidth * 2, diameter + borderWidth * 2);

        //draw body
        graphics.setColor(color);
        graphics.fillOval(x - radius, y - radius, diameter, diameter);

        //draw path to target
        if (nearFood != null) {
            AbstractMap.SimpleEntry<Double, Double> vectorToFood = getVectorToFood(nearFood);
            int dx = (int)(vectorToFood.getKey() * 30);
            int dy = (int) (vectorToFood.getValue() * 30);
            graphics.drawLine(x, y, x + dx, y + dy);
//            graphics.drawLine(x, y, (x + nearFood.x - x), (y + nearFood.y - y));
        }

        //draw stat
        graphics.drawString(String.valueOf(totalEaten), x - radius, y - radius - 10);

    }


    public void doStep() {
        if(isDead()) return;

        AbstractMap.SimpleEntry<Double, Double> vectorToFood = getVectorToFood(nearFood);
        double[] direction = nn.feedForward(new double[]{vectorToFood.getKey(), vectorToFood.getValue()});
//        double[] direction = nn.feedForward(new double[]{vectorToFood.getKey(), vectorToFood.getValue(), distanceToFood(nearFood)});

        if (Double.isNaN(direction[0]) || Double.isNaN(direction[1])) {
            direction[0] = Math.random() - 0.5;
            direction[1] = Math.random() - 0.5;
        }

        double ddx = Math.floor((direction[0] * getMaxSpeed()));
        double ddy =  Math.floor((direction[1] * getMaxSpeed()));

//        ddx += (Math.random() - 0.5) * hungryMadness * hungrySteps;
//        ddy += (Math.random() - 0.5) * hungryMadness * hungrySteps;

        int dx = (int) ddx;
        int dy = (int) ddy;

        x += dx;
        y += dy;

    }

    public Double distanceToFood(Food food) {
        float dx = food.x - x;
        float dy = food.y - y;
        return Math.abs(Math.sqrt(dx * dx + dy * dy));
    }

    public AbstractMap.SimpleEntry<Double, Double> getVectorToFood(Food food) {
        double dx = food.x - x;
        double dy = food.y - y;

        double ab = 1d * dx + 0d * dy;
        double moda = Math.sqrt(1d * 1d + 0d * 0d);
        double modb = Math.sqrt(dx * dx + dy * dy);
        double cos = ab / (moda * modb);
        double sin = Math.sqrt(1 - cos * cos);
        if(dy < 0) sin *= -1;

        return new AbstractMap.SimpleEntry<>(cos, sin);
    }

    public Integer getCount() {
        return totalEaten;
    }

    private void eat() {
        if(isDead()) return;

        hungrySteps = 0;
        totalEaten++;
        weight += 1 * foodScore;
        if (weight > 50) weight = 50f;
    }

    private void hungry() {
        if(isDead()) return;

        hungrySteps++;
        weight -= 0.04f * foodScore;
        if(weight <= 0){
            death();
        }
    }

    private void death(){
        isDead = true;
    }

    public Boolean isDead() {
        return isDead;
    }

    public Cell div(Cell topCell) {
        weight = 20f;
        Cell newCell = new Cell(x, y);

        newCell.nn = nn.clone();
        if(Math.random() > 0.5) {
            newCell.nn.mutate(0.1, 0.1);
        } else {
            newCell.nn.crossover(topCell.nn);
        }


        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        red += (Math.random()-.5) * 8;
        green += (Math.random()-.5) * 8;
        blue += (Math.random()-.5) * 8;

        if(red > 255) red = 255;
        if(red < 0) red = 0;
        if(green > 255) green = 255;
        if(green < 0) green = 0;
        if(blue > 255) blue = 255;
        if(blue < 0) blue = 0;

        newCell.color = new Color(red, green, blue);


        int dx = (int) ((Math.random() - 0.5) * 10);
        int dy = (int) ((Math.random() - 0.5) * 10);

        x += dx;
        y += dy;
        newCell.x += -dx * 5;
        newCell.y += -dy * 5;

        return newCell;
    }

    public Boolean canDiv() {
        return weight >= weightToDiv;
    }
//
//    public Cell mutate(int w, int h) {
//        Cell newCell = new Cell((int) (Math.random() * w), (int) (Math.random() * h));
//
//        newCell.nn.layers = nn.layers.clone();
//        newCell.nn.mutate(0.05, 1);
//
//        return newCell;
//    }


    public Boolean tryToEatNearFood() {
        if (nearFood != null && distanceToFood(nearFood) <= (getMaxSpeed() + 1)) {
            eat();
            nearFood = null;
            return true;
        }
        hungry();
        return false;
    }

    public void setNearFood(Food nearFood) {
        this.nearFood = nearFood;
    }

    public Float getMaxSpeed ( ) {
        return maxSpeed * (20/weight);
    }

}
