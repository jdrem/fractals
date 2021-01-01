package net.remgant.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class AnimatedPanel extends AbstractPanel implements ActionListener
{
  protected Timer timer;

  public AnimatedPanel(Dimension size)
    {
      this(size,250);
    }

  public AnimatedPanel(Dimension size,int interval)
    {
      super(size);
      timer = new Timer(interval,this);
      timer.start();
    }
  
  public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g.drawImage(image,0,0,this);
    }

  public void actionPerformed(ActionEvent e)
    {
      this.repaint();
    }
}
