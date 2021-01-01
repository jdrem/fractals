package net.remgant.imaging;

import org.apache.commons.math3.complex.Complex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class Newton extends AbstractFractalCreator {
    public static void main(String[] args) {
        new Newton();
    }

    public Newton() {
        super("Newton");
        double width = 4.0;
        double height = 4.0;
        int order = 4;
        try {
            Executors.newFixedThreadPool(4).invokeAll(Arrays.asList(
                    new ImageDrawer(0, 0, windowWidth / 2, windowHeight / 2,width, height, order, ((ImagePanel)panel).getImage()),
                    new ImageDrawer(0, 400, windowWidth / 2, windowHeight,width, height, order, ((ImagePanel)panel).getImage()),
                    new ImageDrawer(400, 0, windowWidth / 2, windowHeight / 2,width, height, order, ((ImagePanel)panel).getImage()),
                    new ImageDrawer(400, 400, windowWidth / 2, windowHeight / 2, width, height, order, ((ImagePanel)panel).getImage())
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
        double xc;
        double yc;
        double width;
        double height;
        int order;
        BufferedImage image;

        public ImageDrawer(int x, int y, int w, int h, double width, double height, int order, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.xc = -width / 2.0;
            this.yc = -height / 2.0;
            this.width = width;
            this.height = height;
            this.image = image;
            this.order = order;
        }

        public Void call() {
            final int iterationLimit = 256;
            double error = 0.1;
            double xSlice = width / (double) image.getWidth();
            double ySlice = height / (double) image.getHeight();
            Graphics2D g = image.createGraphics();
            for (int i = x; i < w + x; i++) {
                for (int j = y; j < h + y; j++) {
                    int iterations = 0;
                    double re = xc + (double) i * xSlice;
                    double im = yc + (double) j * ySlice;
                    Complex c = new Complex(re, im);
                    for (int k = 0; k < iterationLimit; k++) {
                        iterations++;
                        Complex z = Complex.valueOf(c.getReal(), c.getImaginary());
                        Complex a = c.pow(order).subtract(Complex.ONE);
                        Complex b = c.pow(order-1).multiply(Complex.valueOf(order, 0));
                        c = z.subtract(a.divide(b));
                        if (Math.abs(c.abs() - z.abs()) < error && Math.abs(angleOf(c) - angleOf(z)) < error) {
                            break;
                        }
                    }
                    float h = (float) (angleOf(c) / (2.0 * Math.PI));
                    Color color = Color.getHSBColor(h, 1.0f, 1.0f);
                    if (iterations < 16)
                        color = color.darker();
                    if (iterations < 8)
                        color = color.darker();
                    if (iterations < 4)
                        color.darker();
                    if (iterations < 2)
                        color.darker();
                    g.setColor(color);
                    g.fill(new Rectangle2D.Double(i, j, 1.0, 1.0));
                }
            }
            return null;
        }

        double angleOf(Complex c) {
            return Math.atan2(c.getImaginary(), c.getReal());
        }
    }


    @Override
    protected JPanel createPanel() {
        return new ImagePanel(new Dimension(windowWidth, windowHeight));
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
