package com.owldevs.evolutionsim.forms;

import com.owldevs.evolutionsim.models.Evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class PlainEarthForm extends JFrame implements Runnable, MouseListener, KeyListener {

    private final int w;
    private final int h;

    private boolean enableRendering;

    private BufferedImage spriteBufferedImage;
    private BufferedImage backgroundBufferedImage;

    private Evolution evolution;

    public PlainEarthForm() {
        w = 1500;
        h = 1000;

        //Business objects
        evolution = new Evolution(w, h);
        enableRendering = true; // press space to disable

        //form settings
        spriteBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        backgroundBufferedImage = new BufferedImage(w / 8, h / 8, BufferedImage.TYPE_INT_RGB);

        this.setSize(w + 16, h + 38);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(50, 50);
        this.add(new JLabel(new ImageIcon(spriteBufferedImage)));
        addMouseListener(this);
        addKeyListener(this);
    }

    @Override
    public void run() {
        while (true) {
            this.repaint();
        }
    }


    @Override
    public void paint(Graphics graphics) {

        if(evolution.isTimeToEvolute()) {
            evolution.evolute();
        }

        evolution.calcStep();

        if (enableRendering) {
            //отрисовка фона
            for (int i = 0; i < w / 8; i++) {
                for (int j = 0; j < h / 8; j++) {
                    int color = ((100) << 16) | ((100) << 8) | (100);
                    backgroundBufferedImage.setRGB(i, j, color);
                }
            }

            //отрисовка спрайтов
            Graphics spriteGraphic = spriteBufferedImage.getGraphics();
            spriteGraphic.drawImage(backgroundBufferedImage, 0, 0, w, h, this);

            evolution.getSprites().forEach(sprite -> sprite.draw(spriteGraphic));
            graphics.drawImage(spriteBufferedImage, 8, 30, w, h, this);
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == ' ') {
            enableRendering = !enableRendering;
        } else if (e.getKeyChar() == 'e') {
            evolution.evolute();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}