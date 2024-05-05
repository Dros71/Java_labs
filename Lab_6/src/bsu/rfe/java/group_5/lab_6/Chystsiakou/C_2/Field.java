package bsu.rfe.java.group_5.lab_6.Chystsiakou.C_2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.Timer;

import static java.lang.Math.pow;

@SuppressWarnings("serial")

public class Field extends JPanel {
    private boolean paused;
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    private Timer repaintTimer = new Timer(5, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            collisionCheck();
            repaint();
        }
    });
    private void collisionCheck(){
        for (int i = 0; i < balls.size() - 1; i++){
            for (int j = i + 1; j < balls.size(); j++){
                BouncingBall ball1 = balls.get(i);
                BouncingBall ball2 = balls.get(j);
                double distX = ball1.getX() - ball2.getX();
                double distY = ball1.getY() - ball2.getY();
                double r1Cube = pow(ball1.getRadius(), 3);
                double r2Cube = pow(ball2.getRadius(), 3);
                double sqrt = Math.sqrt(distX * distX + distY * distY);

                if ((ball1.getRadius() + ball2.getRadius()) >= sqrt){
                    double v1x = ball1.getSpeedX();
                    double v2x = ball2.getSpeedX();
                    double v1y = ball1.getSpeedY();
                    double v2y = ball2.getSpeedY();

                    ball1.setSpeedX(v1x * (r1Cube - r2Cube)/(r1Cube + r2Cube) + 2 * v2x * r2Cube/(r1Cube+r2Cube));
                    ball2.setSpeedX(v2x * (r2Cube - r1Cube)/(r1Cube + r2Cube) + 2 * v1x * r1Cube/(r1Cube+r2Cube));

                    ball1.setSpeedY(v1y * (r1Cube - r2Cube)/(r1Cube + r2Cube) + 2 * v2y * r2Cube/(r1Cube+r2Cube));
                    ball2.setSpeedY(v2y * (r2Cube - r1Cube)/(r1Cube + r2Cube) + 2 * v1y * r1Cube/(r1Cube+r2Cube));

                    int signX = 1;
                    int signY = 1;
                    //ArrayList<double> pos_dist = new ArrayList<double>(distX,distY);

                    double d = ball1.getRadius() + ball2.getRadius() - sqrt;
                    double alpha = Math.atan(distY/distX);

                    if (distX < 0){
                        signX = -signX;
                    }
                    if (distY < 0){
                        signY = -signY;
                    }

                    ball1.setX(ball1.getX() + signX * d/2 * Math.cos(alpha));
                    ball1.setY(ball1.getY() + signY * d/2 * Math.sin(alpha));

                    ball2.setX(ball2.getX() - signX * d/2 * Math.cos(alpha));
                    ball2.setY(ball2.getY() - signY * d/2 * Math.sin(alpha));

                    distX = ball1.getX() - ball2.getX();
                    distY = ball1.getY() - ball2.getY();
                    sqrt = Math.sqrt(distX * distX + distY * distY);
                    System.out.println(ball1.getRadius() + ball2.getRadius() - sqrt);

                }
            }
        }
    }

    public ArrayList<BouncingBall> getBalls() {
        return balls;
    }

    public Field() {
        setBackground(Color.WHITE);
        //balls.add(new BouncingBall(this, true));
        repaintTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball : balls) {
            ball.paint(canvas);
        }
    }
    public void addBall() {
        balls.add(new BouncingBall(this));
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void canMove(BouncingBall ball)
            throws InterruptedException {
        if (paused) {
            wait();
        }
    }
}
