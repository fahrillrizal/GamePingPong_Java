/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author macbookairm1
 */
public class Papan {
    Rectangle papan;
    int id;
    boolean movingUp = false;
    boolean movingDown = false;
    Color color;

    public Papan(int id, Color color) {
        this.id = id;
        this.color = color;
        this.papan = new Rectangle(10, 70);
        if (id == 1) {
            papan.setLocation(20, 150);
        } else {
            papan.setLocation(570, 150);
        }
    }

    public void setUp(boolean move) {
        movingUp = move;
    }

    public void setDown(boolean move) {
        movingDown = move;
    }

    public void move() {
        if (movingUp && papan.y > 0) {
            papan.y -= 3;
        }
        if (movingDown && papan.y < 300) {
            papan.y += 3;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(papan.x, papan.y, papan.width, papan.height);
    }
}