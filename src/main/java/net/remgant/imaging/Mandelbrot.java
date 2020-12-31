package net.remgant.imaging;

import org.apache.commons.math3.complex.Complex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Mandelbrot extends AbstractFractalCreator {

    public static void main(String[] args) {
        new Mandelbrot();
    }

    public Mandelbrot() {
        super("Mandelbrot");

        double xc = -0.5;
        double yc = 0.0;
        double width = 2.0;
        double height = 2.0;
        try {
            Executors.newFixedThreadPool(4).invokeAll(Arrays.asList(
                    new ImageDrawer(0, 0, windowWidth / 2, windowHeight / 2, xc, yc, width, height, ((ImagePanel)panel).getImage()),
                    new ImageDrawer(0, 400, windowWidth / 2, windowHeight, xc, yc, width, height, ((ImagePanel)panel).getImage()),
                    new ImageDrawer(400, 0, windowWidth / 2, windowHeight / 2, xc, yc, width, height, ((ImagePanel)panel).getImage()),
                    new ImageDrawer(400, 400, windowWidth / 2, windowHeight / 2, xc, yc, width, height, ((ImagePanel)panel).getImage())
            ));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> panel.repaint(), 0, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    protected JPanel createPanel() {
        return new Mandelbrot.ImagePanel(new Dimension(windowWidth, windowHeight));
    }

    static class ImageDrawer implements Callable<Void> {
        int x;
        int y;
        int w;
        int h;
        double xc;
        double yc;
        double width;
        double height;
        BufferedImage image;

        public ImageDrawer(int x, int y, int w, int h, double xc, double yc, double width, double height, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.xc = xc - width/2.0;
            this.yc = yc - height/2.0;
            this.width = width;
            this.height = height;
            this.image = image;
        }

        public Void call() {
            final int iterationLimit = 256;
            double xSlice = width / (double) image.getWidth();
            double ySlice = height / (double) image.getHeight();
            Graphics2D g = image.createGraphics();
            for (int i = x; i < w + x; i++) {
                for (int j = y; j < h + y; j++) {
                    int iterations = 0;
                    double re = xc + (double) i * xSlice;
                    double im = yc + (double) j * ySlice;
                    Complex c = new Complex(re, im);
                    Complex c0 = c;
                    while (c.abs() < 2.0 && iterations < iterationLimit) {
                        c = c.multiply(c).add(c0);
                        iterations++;
                    }
                    Color color;
                    if (iterations >= iterationLimit)
                        color = Color.BLACK;
                    else {
                        float f = (float) iterations / (float) iterationLimit;
                        color = Color.getHSBColor(f, 1.0f, 1.0f);
                    }
                    g.setColor(color);
                    g.fill(new Rectangle2D.Double(i, j, 1.0, 1.0));
                }
            }
            return null;
        }
    }

    static class ImagePanel extends JPanel {
        private final BufferedImage image;

        ImagePanel(Dimension d) {
            super();
            image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, Color.gray, this);
        }

        public double getWidth2D() {
            return getWidth();
        }

        public double getHeight2D() {
            return getHeight();
        }

        @SuppressWarnings("unused")
        public Rectangle2D getBounds2D() {
            return new Rectangle2D.Double(0.0, 0.0, getWidth2D(), getHeight2D());
        }

        public BufferedImage getImage() {
            return image;
        }
    }
}
