package net.remgant.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import net.remgant.math.*;
import java.util.prefs.*;

public class Mandelbrot extends JFrame
{
  public static void main(String args[])
  {
    try
      {
	UIManager.setLookAndFeel
	  ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      }
    catch (Exception e)
      {
      }

    // System.out.println(System.getProperties().getProperty("os.name"));
    // System.out.println(System.getProperties().getProperty("os.arch"));
    // System.out.println(System.getProperties().getProperty("os.version"));

    Mandelbrot frame = new Mandelbrot(args);
    frame.pack();
    frame.setVisible(true);
  }


  int screenSizeX;
  int screenSizeY;
  LocalAnimatedPanel panel;
  double x,y,width,height;


  Mandelbrot(String args[])
  {
    super("Mandelbrot");
    screenSizeX = 400;
    screenSizeY = 400;

    Preferences packageRoot = Preferences.userNodeForPackage(Mandelbrot.class);
    x = packageRoot.getDouble("CenterX",-0.75);
    y = packageRoot.getDouble("CenterY",0.0);
    width = packageRoot.getDouble("Width",1.5);
    setBounds(0,0,screenSizeX,screenSizeY);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e)
	{ savePreferences();System.exit(0); }});

    JMenuBar myMenuBar = new JMenuBar();
    setJMenuBar(myMenuBar);
    JMenu FileMenu = new JMenu("File");
    myMenuBar.add(FileMenu);


    JMenuItem ExitMenuItem = new JMenuItem("Exit");
    ExitMenuItem.addActionListener(new ActionListener()
				   {public void actionPerformed(ActionEvent e)
				     {savePreferences();System.exit(1);}});
    FileMenu.add(ExitMenuItem);


    JMenuItem AboutMenuItem = new JMenuItem("About");

    JMenu HelpMenu = new JMenu("Help");
    myMenuBar.add(HelpMenu);

    AboutMenuItem.addActionListener(new ActionListener()
	{public void actionPerformed(ActionEvent e)
	    {JOptionPane.showMessageDialog(Mandelbrot.this,
					   "Mandelbrot");}});
    HelpMenu.add(AboutMenuItem);

    Dimension size = new Dimension(screenSizeX,screenSizeY);
    panel = new LocalAnimatedPanel(size);
    BufferedImage image = new BufferedImage(size.width,size.height,
				BufferedImage.TYPE_INT_RGB);
    panel.setLocalImage(image);
	 
    getContentPane().add("Center",panel);
    panel.setPreferredSize(size);
						 
    Drawer drawer = new Drawer(image.getGraphics(),x,y,width);
    panel.addMouseListener(drawer);
    Thread t = new Thread(drawer);
    t.start();
  }

  private void savePreferences()
    {
      Preferences packageRoot = 
	Preferences.userNodeForPackage(Mandelbrot.class);
      packageRoot.putDouble("CenterX",x);
      packageRoot.getDouble("CenterY",y);
      packageRoot.getDouble("Width",width);
    }

  class LocalAnimatedPanel extends AnimatedPanel
  {
    BufferedImage localImage;
    LocalAnimatedPanel(Dimension size)
      {
	super(size);
      }
    public void paintComponent(Graphics g)
      {
	super.paintComponent(g);
	if (localImage != null)
	  g.drawImage(localImage,0,0,this);
      }
    void setLocalImage(BufferedImage localImage)
      {
	this.localImage = localImage;
      }
  }

  class Drawer extends MouseAdapter implements Runnable
  {
    protected Graphics g;
    double microWidth;
    double microHeight;

    public Drawer(Graphics g,double x, double y,double width)
      {
	this.g = g;
	/*
	this.x = x;
	this.y = y;
	this.width = width;
	*/
      }

    public void mouseClicked(MouseEvent e)
      {
	System.out.print("("+e.getX()+","+e.getY()+") ");
	double dx = x + (e.getX()-screenSizeX/2)*microWidth;
	double dy = y - (e.getY()-screenSizeY/2)*microHeight;
	System.out.println("("+dx+","+dy+") ");
	restartNow = true;
	g.setColor(Color.black);
	g.fillRect(0,0,screenSizeX,screenSizeY);
	x = dx;
	y = dy;
	width /= 2.0;
      }

    boolean done;
    boolean restartNow;
    public void run()
    {
      for (;;)
	{
	  if (done)
	    {
	      if (restartNow)
		{
		  done = false;
		  restartNow = false;
		}
	      else
		{
		  try
		    {
		      Thread.sleep(250);
		    }
		  catch (InterruptedException ie)
		    {
		    }
		  continue;
		}
	    }
	  height = (double)screenSizeY/(double)screenSizeX * width;
	  ComplexNumber c;
	  ComplexNumber z;
	  microWidth = width / (double)screenSizeX;
	  microHeight = height / (double)screenSizeY;
	  int iterations=0;
	  for (int i=0; i<screenSizeX; i++)
	    {
	      for (int j=0; j<screenSizeY; j++)
		{
		  if (restartNow)
		    break;
		  c = ComplexNumber.getInstance(0,0,ComplexNumber.DOUBLE_TYPE);
		  double dx = x + (i-screenSizeX/2)*microWidth;
		  double dy = y - (j-screenSizeY/2)*microHeight;
		  z = ComplexNumber.getInstance(dx,dy,ComplexNumber.DOUBLE_TYPE);
		  boolean inSet = true;
		  for (int k=0; k<2000; k++)
		    {
		      c = c.square().add(z);
		      if (c.magnitude() > 2.0)
			{
			  inSet = false;
			  iterations = k;
			  break;
			}
		    }
		  if (!inSet)
		    {
		      Color color = chooseColor(iterations);
		      g.setColor(color);
		      g.drawOval(i,j,0,0);
		    }
		}
	      if (restartNow)
		break;
	    }
	  if (restartNow)
	    {
	      restartNow = false;
	      done = false;
	    }
	  else
	    {
	      done = true;
	    }
	}
    }
  }

  static Color chooseColor(int i)
  {
    float h = (float)i/200.0f;
    float s = 0.75f;
    float b = 0.5f;
    /*
    float h = 0.5f;
    float s = (float)i/200.0f;
    if (s > 1.0f)
      s = 1.0f;
    float b = 0.5f;
    */
    return Color.getHSBColor(h,s,b);
  }
}
