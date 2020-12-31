package net.remgant.imaging;

import org.apache.commons.math3.complex.Complex;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class Julia extends AbstractFractalCreator {
    public static void main(String[] args) {
        new Julia();
    }

    public Julia() {
        super("Julia");
        double width = 4.0;
        double height = 4.0;
        Complex c0 = Complex.valueOf(0.7885).multiply(Complex.valueOf(0.0, Math.PI).exp());
        try {
            Executors.newFixedThreadPool(4).invokeAll(Arrays.asList(
                    new ImageDrawer(0, 0, windowWidth / 2, windowHeight / 2, c0, width, height, panel.getImage()),
                    new ImageDrawer(0, 400, windowWidth / 2, windowHeight / 2, c0, width, height, panel.getImage()),
                    new ImageDrawer(400, 0, windowWidth / 2, windowHeight / 2, c0, width, height, panel.getImage()),
                    new ImageDrawer(400, 400, windowWidth / 2, windowHeight / 2, c0, width, height, panel.getImage())
            ));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class ImageDrawer implements Callable<Void> {
        int x;
        int y;
        int w;
        int h;
        Complex c0;
        double xc;
        double yc;
        double width;
        double height;
        BufferedImage image;

        public ImageDrawer(int x, int y, int w, int h, Complex c0, double width, double height, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.c0 = c0;
            this.xc = -width / 2.0;
            this.yc = -height / 2.0;
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

}
