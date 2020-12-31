package net.remgant.imaging;

import org.apache.commons.math3.complex.Complex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Julia extends AbstractFractalCreator {
    public static void main(String[] args) {
        new Julia();
    }

    public Julia() {
        super("Julia");
        double width = 4.0;
        double height = 4.0;
        int imageCount = 24;
        double interval = (2.0 * Math.PI) / imageCount;
        List<ImageDrawer> imageDrawerList = new ArrayList<>();
        for (int i=0; i<imageCount; i++ ) {
            Complex c0 = Complex.valueOf(0.7885).multiply(Complex.valueOf(0.0, (double)i * interval).exp());
            imageDrawerList.add(new ImageDrawer( windowWidth, windowHeight, c0, width, height));
        }
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<Future<BufferedImage>> futuresList;
        try {
            futuresList = executorService.invokeAll(imageDrawerList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<BufferedImage> bufferedImageList = futuresList.stream()
                .filter(Future::isDone)
                .map(f -> {
                    try {
                        return Optional.of(f.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return Optional.<BufferedImage>empty();
                    }})
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        ((ImagePanel)panel).setImageList(bufferedImageList);
    }

    static class ImageDrawer implements Callable<BufferedImage> {
        int w;
        int h;
        Complex c0;
        double xc;
        double yc;
        double width;
        double height;
        BufferedImage image;

        public ImageDrawer(int w, int h, Complex c0, double width, double height) {
            this.w = w;
            this.h = h;
            this.c0 = c0;
            this.xc = -width / 2.0;
            this.yc = -height / 2.0;
            this.width = width;
            this.height = height;
            this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }

        public BufferedImage call() {
            final int iterationLimit = 256;
            double xSlice = width / (double) image.getWidth();
            double ySlice = height / (double) image.getHeight();
            Graphics2D g = image.createGraphics();
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
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
            return image;
        }
    }

    @Override
    protected JPanel createPanel() {
        return new ImagePanel(new Dimension(windowWidth, windowHeight));
    }

    static protected class ImagePanel extends JPanel {
        private final BufferedImage defaultImage;
        java.util.List<BufferedImage> imageList;
        Iterator<BufferedImage> imageIterator;

        ImagePanel(Dimension d) {
            super();
            defaultImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = defaultImage.createGraphics();
            g.setColor(Color.BLACK);
            g.fill(new Rectangle2D.Double(0.0, 0.0, d.width, d.height));
        }

        public void setImageList(List<BufferedImage> imageList) {
            this.imageList = imageList;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imageList == null) {
                g.drawImage(defaultImage, 0, 0, Color.gray, this);
            } else {
                if (imageIterator == null || !imageIterator.hasNext())
                    imageIterator = imageList.iterator();
                g.drawImage(imageIterator.next(), 0, 0, Color.gray, this);
            }

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
    }

}
