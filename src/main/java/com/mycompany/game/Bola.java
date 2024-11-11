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
public class Bola {
    int x, y, xVelocity = 3, yVelocity = 3;
    int diameter = 20;
    Papan p1, p2;
    Pong pong;

    public Bola(int x, int y, Papan p1, Papan p2, Pong pong) {
        this.x = x;
        this.y = y;
        this.p1 = p1;
        this.p2 = p2;
        this.pong = pong;
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;

        if (y <= 0 || y >= 380) {
            yVelocity = -yVelocity;
        }

        if (checkCollision(p1) || checkCollision(p2)) {
            xVelocity = -xVelocity;
        }

        if (x <= 0) {
            pong.playerScores(2);
        } else if (x >= 580) {
            pong.playerScores(1);
        }
    }

    public boolean checkCollision(Papan papan) {
        return papan.papan.intersects(new Rectangle(x, y, diameter, diameter));
    }

    public void resetPosition() {
        x = 300;
        y = 200;
        xVelocity = (Math.random() > 0.5) ? 3 : -3;
        yVelocity = (Math.random() > 0.5) ? 3 : -3;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diameter, diameter);
    }
}