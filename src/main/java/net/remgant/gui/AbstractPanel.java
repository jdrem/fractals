package net.remgant.gui;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public abstract class AbstractPanel extends javax.swing.JPanel 
{
  protected Dimension size;
  protected BufferedImage image;
  protected Color bgColor;
  public AbstractPanel(Dimension size)
    {
      super();
      this.size = size;
      image = new BufferedImage(size.width,size.height,
				BufferedImage.TYPE_INT_RGB);
      bgColor = Color.white;
    }

  public BufferedImage getImage()
    {
      return image;
    }
}
