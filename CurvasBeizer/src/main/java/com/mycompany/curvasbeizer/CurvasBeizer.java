/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.curvasbeizer;

/**
 *
 * @author melis
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CurvasBeizer extends JPanel{

    private ArrayList<Point> controlPoints = new ArrayList<>();
    private int degree = 2; 
    private Point selectedControlPoint = null;

    public CurvasBeizer() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controlPoints.add(e.getPoint());
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                for (Point p : controlPoints) {
                    if (p.distance(e.getPoint()) < 5) {
                        selectedControlPoint = p;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedControlPoint = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedControlPoint != null) {
                    selectedControlPoint.setLocation(e.getPoint());
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // draw control points
        g2d.setColor(Color.RED);
        for (Point p : controlPoints) {
            g2d.fillOval(p.x - 2, p.y - 2, 4, 4);
        }

        // draw Bezier curve
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        if (controlPoints.size() >= degree) {
            for (double t = 0; t <= 1; t += 0.01) {
                Point p = calculateBeizerPoint(t, controlPoints);
                g2d.drawOval(p.x - 1, p.y - 1, 2, 2);
            }
        }
    }

    private Point calculateBeizerPoint(double t, ArrayList<Point> controlPoints) {
        int n = controlPoints.size() - 1;
        double x = 0, y = 0;
        for (int i = 0; i <= n; i++) {
            double bernstein = bernsteinPolynomial(i, n, t);
            x += bernstein * controlPoints.get(i).x;
            y += bernstein * controlPoints.get(i).y;
        }
        return new Point((int) x, (int) y);
    }

    private double bernsteinPolynomial(int i, int n, double t) {
        double binomial = binomialCoefficient(n, i);
        return binomial * Math.pow(t, i) * Math.pow(1 - t, n - i);
    }

    private double binomialCoefficient(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    private double factorial(int n) {
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bezier Curve");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new CurvasBeizer());
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}