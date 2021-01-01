package net.remgant.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import net.remgant.math.*;
import java.util.prefs.*;

public class NewtonsMethod extends JFrame
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

    NewtonsMethod frame = new NewtonsMethod(args);
    frame.pack();
    frame.setVisible(true);
  }


  int screenSizeX;
  int screenSizeY;
  LocalAnimatedPanel panel;
  double x,y,width,height;
  int numberOfRoots;


  NewtonsMethod(String args[])
  {
    super("Newton\'s Method");
    screenSizeX = 400;
    screenSizeY = 400;

    Preferences packageRoot = Preferences.userNodeForPackage(NewtonsMethod.class);
    Preferences thisNode = packageRoot.node("NewtonsMethod");
    x = thisNode.getDouble("CenterX",0.0);
    y = thisNode.getDouble("CenterY",0.0);
    width = thisNode.getDouble("Width",3.0);
    numberOfRoots = thisNode.getInt("NumberOfRoots",8);
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
	    {JOptionPane.showMessageDialog(NewtonsMethod.this,
					   "NewtonsMethod");}});
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
	Preferences.userNodeForPackage(NewtonsMethod.class);
      Preferences thisNode = packageRoot.node("NewtonsMethod");
      thisNode.putDouble("CenterX",x);
      thisNode.putDouble("CenterY",y);
      thisNode.putDouble("Width",width);
      thisNode.putInt("NumberOfRoots",numberOfRoots);
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
	  ComplexNumber q;
	  ComplexNumber z;
	  microWidth = width / (double)screenSizeX;
	  microHeight = height / (double)screenSizeY;
	  int iterations=0;
	  final ComplexNumber ONE = 
	    ComplexNumber.getInstance(1.0,0.0,ComplexNumber.DOUBLE_TYPE);
	  for (int i=0; i<screenSizeX; i++)
	    {
	      for (int j=0; j<screenSizeY; j++)
		{
		  if (restartNow)
		    break;
		  double dx = x + (i-screenSizeX/2)*microWidth;
		  double dy = y - (j-screenSizeY/2)*microHeight;
		  z = ComplexNumber.getInstance
		    (dx,dy,ComplexNumber.DOUBLE_TYPE);
		  q = z;
		  boolean inSet = false;
		  int n = numberOfRoots;
		  iterations = 0;
		  for (int k=0; k<2000; k++)
		    {
		      ComplexNumber a = z.pow(n).subtract(ONE);
		      ComplexNumber b = 
			ComplexNumber.getInstance((double) n,0.0,
						  ComplexNumber.DOUBLE_TYPE);
		      ComplexNumber c = z.pow(n-1).multiply(b);
		      ComplexNumber d = a.divide(c);
		      ComplexNumber e = z.subtract(d);
		      z = e;
		      iterations++;
		      if (z.magnitude() == q.magnitude() &&
			  z.angle() == q.angle())
			{
			  inSet = true;
			  break;
			}
		      q = z;
		    }
		  
		  /*
		  Color colorSet[] = {Color.red,Color.blue,Color.yellow,
				      Color.green,Color.orange,Color.magenta,
				      Color.pink,Color.cyan};
		  */
		  Color colorSet[] = createColorSet(numberOfRoots);
		  
		  Color color = Color.black;
		  if (inSet)
		    {
		      double angle = z.angle();
		      double x = 360.0 / (double)n;
		      int y = (int)(angle / x);
		      color = colorSet[y];
		      if (iterations < 12)
			color = color.darker();
		      if (iterations < 10)
			color = color.darker();
		      if (iterations < 8)
			color = color.darker();
		      if (iterations < 4)
			color = color.darker();
		      if (iterations < 2)
			color = color.darker();
		      if (iterations < 1)
			color = color.darker();
		    }
		  g.setColor(color);
		  g.drawOval(i,j,0,0);
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
      return Color.getHSBColor(h,s,b);
    }
  
  static Color[] createColorSet(int n)
    {
      float h;
      float s = 0.75f;
      float b = 1.0f;
      Color initialSet[] = new Color[n];
      for (int i=0; i<n; i++)
	{
	  h = (float)i/(float)n;
	  initialSet[i] = Color.getHSBColor(h,s,b);
	}
      Color returnSet[] = new Color[n];
      for (int i=0; i<n/2; i++)
	{
	  returnSet[i] = initialSet[i*2];
	  returnSet[i+n/2] = initialSet[i*2+1];
	}
      if (n % 2 == 1)
	returnSet[n-1] = initialSet[n-1];
      return returnSet;
    }
}
