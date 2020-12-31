package net.remgant.imaging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AbstractFractalCreator extends JFrame {
    protected int windowWidth = 800;
    protected int windowHeight = 800;
    protected ImagePanel panel;

    protected AbstractFractalCreator(String name) {
        super(name);
        setBounds(0, 0, windowWidth, windowHeight);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Dimension size = new Dimension(windowWidth, windowHeight);
        panel = new ImagePanel(size);
        getContentPane().add("Center", panel);
        panel.setPreferredSize(size);
        pack();
        setVisible(true);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> panel.repaint(), 0, 500, TimeUnit.MILLISECONDS);
    }

    static protected class ImagePanel extends JPanel {
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
