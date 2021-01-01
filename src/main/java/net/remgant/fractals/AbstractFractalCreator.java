package net.remgant.fractals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AbstractFractalCreator extends JFrame {
    protected int windowWidth = 800;
    protected int windowHeight = 800;
    protected JPanel panel;

    protected AbstractFractalCreator(String name) {
        super(name);
        setBounds(0, 0, windowWidth, windowHeight);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Dimension size = new Dimension(windowWidth, windowHeight);
        panel = createPanel();
        getContentPane().add("Center", panel);
        panel.setPreferredSize(size);
        pack();
        setVisible(true);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> panel.repaint(), 0, 500, TimeUnit.MILLISECONDS);
    }

    protected abstract JPanel createPanel();
}
