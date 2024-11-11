/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author macbookairm1
 */
public class Pong extends JPanel implements ActionListener, KeyListener {
    Timer timer;
    Bola bola;
    Papan p1, p2;
    boolean isPausedAfterScore = false;
    boolean isStartingCountdown = true;
    boolean isPaused = false;
    long scorePauseStartTime;
    int gameDuration = 120;
    int timeRemaining;
    boolean isGameOver = false;
    int p1Score = 0;
    int p2Score = 0;
    long startTime;
    long lastScoreTime;
    final long speedIncreaseTime = 5 * 1000;
    final double speedIncrement = 0.8;
    int countdownValue = 3;
    String lastScorerMessage = "";

    public Pong() {
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        p1 = new Papan(1, Color.BLUE);
        p2 = new Papan(2, Color.RED);
        bola = new Bola(300, 200, p1, p2, this);
        timer = new Timer(1000, this);
        timer.start();

        timeRemaining = gameDuration;
        startTime = System.currentTimeMillis();
        lastScoreTime = System.currentTimeMillis();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPaused) {
            return;
        }

        if (isStartingCountdown) {
            countdownValue--;
            if (countdownValue <= 0) {
                isStartingCountdown = false;
                startTime = System.currentTimeMillis();
                lastScoreTime = System.currentTimeMillis();
                timer.setDelay(10);
            }
            repaint();
        } else if (isPausedAfterScore) {
            long pauseDuration = System.currentTimeMillis() - scorePauseStartTime;
            if (pauseDuration > 1000) {
                isPausedAfterScore = false;
                bola.resetPosition();
                lastScorerMessage = "";
            }
        } else if (!isGameOver) {
            bola.move();
            p1.move();
            p2.move();

            timeRemaining = gameDuration - (int) ((System.currentTimeMillis() - startTime) / 1000);

            if (timeRemaining <= 0) {
                isGameOver = true;
                showEndGameMenu();
            }

            if (System.currentTimeMillis() - lastScoreTime > speedIncreaseTime) {
                bola.xVelocity += (bola.xVelocity > 0) ? speedIncrement : -speedIncrement;
                bola.yVelocity += (bola.yVelocity > 0) ? speedIncrement : -speedIncrement;
                lastScoreTime = System.currentTimeMillis();
            }

            repaint();
        }
    }

    public void playerScores(int player) {
        if (player == 1) {
            p1Score++;
            lastScorerMessage = "Player 1 Goll!";
        } else {
            p2Score++;
            lastScorerMessage = "Player 2 Goll!";
        }
        isPausedAfterScore = true;
        scorePauseStartTime = System.currentTimeMillis();
        lastScoreTime = System.currentTimeMillis();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isStartingCountdown) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Dimulai dalam: " + countdownValue, getWidth() / 2 - 100, getHeight() / 2);
            return;
        }

        if (isPaused) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Pause", getWidth() / 2 - 50, getHeight() / 2);
            return;
        }

        bola.draw(g);
        p1.draw(g);
        p2.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Player 1: " + p1Score, 50, 20);
        g.drawString("Player 2: " + p2Score, getWidth() - 100, 20);

        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);

        g.drawString("Waktu: " + timeFormatted, getWidth() / 2 - 50, 20);

        if (!lastScorerMessage.isEmpty()) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(lastScorerMessage, getWidth() / 2 - 75, getHeight() / 2 - 50);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> p1.setUp(true);
            case KeyEvent.VK_S -> p1.setDown(true);
            case KeyEvent.VK_UP -> p2.setUp(true);
            case KeyEvent.VK_DOWN -> p2.setDown(true);
            case KeyEvent.VK_ESCAPE -> {
                isPaused = !isPaused;
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> p1.setUp(false);
            case KeyEvent.VK_S -> p1.setDown(false);
            case KeyEvent.VK_UP -> p2.setUp(false);
            case KeyEvent.VK_DOWN -> p2.setDown(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void showEndGameMenu() {
        String winner = p1Score > p2Score ? "Player 1" : p2Score > p1Score ? "Player 2" : "Seri bro";
        String message = "Yah selesai\n";
        message += "Skor: Player 1: " + p1Score + " - Player 2: " + p2Score + "\n";
        message += winner + " Keren!";

        int option = JOptionPane.showOptionDialog(this, message, "Yah selesai",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new Object[]{"Restart", "Exit"}, "Restart");

        if (option == 0) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        p1Score = 0;
        p2Score = 0;
        isGameOver = false;
        timeRemaining = gameDuration;
        isStartingCountdown = true;
        countdownValue = 3;
        startTime = System.currentTimeMillis();
        lastScoreTime = System.currentTimeMillis();
        bola.resetPosition();
        timer.setDelay(1000);
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ping Pong Cuy");
        Pong pongGame = new Pong();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pongGame);
        frame.pack();
        frame.setVisible(true);
    }
}