package net.remgant.math;

public class ComplexInteger extends ComplexNumber
{
  public int real;
  public int imaginary;

  public ComplexInteger(int r, int i)
    {
      real = r;
      imaginary = i;
    }

  public ComplexInteger()
    {
      real = 0;
      imaginary = 0;
    }

  public ComplexNumber add(ComplexNumber c)
    {
      ComplexInteger ci = (ComplexInteger)c;
      return new ComplexInteger(this.real+ci.real,this.imaginary+ci.imaginary);
    }

  public ComplexNumber subtract(ComplexNumber c)
    {
      ComplexInteger ci = (ComplexInteger)c;
      return new ComplexInteger(this.real-ci.real,this.imaginary-ci.imaginary);
    }

  public ComplexNumber multiply(ComplexNumber c)
    {
      ComplexInteger ci = (ComplexInteger)c;
      int r = (this.real * ci.real) - (this.imaginary * ci.imaginary);
      int i = (this.real * ci.imaginary) + (ci.real * this.imaginary);
      return new ComplexInteger(r,i);
    }

  public ComplexNumber divide(ComplexNumber c)
    {
      return null;
    }
  public ComplexNumber square()
    {
      return this.multiply(this);
    }

  public double magnitude()
  {
    return Math.sqrt(real * real + imaginary * imaginary);
  }

  public double angle()
    {
      return 0.0;
    }

  public ComplexNumber pow(int n)
    {
      if (n == 0)
	return new ComplexInteger(1,0);
      if (n == 1)
	return this;
      ComplexInteger d = new ComplexInteger();
      d.real = this.real;
      d.imaginary = this.imaginary;

      for (int i=0; i<n; i++)
	d = (ComplexInteger)d.multiply((ComplexNumber)d);
      return d;
    }

  public String toString()
    {
      StringBuffer sb = new StringBuffer();
      if (real != 0)
	sb.append(real);
      if (imaginary > 0 && real != 0)
	sb.append("+");
      if (imaginary != 0)
	{
	  sb.append(imaginary);
	  sb.append("i");
	}
      return sb.toString();
    }

  public boolean equals(Object o)
    {
      if (!(o instanceof ComplexInteger))
	return false;
      ComplexInteger c = (ComplexInteger)o;
      return this.real == c.real && this.imaginary == c.imaginary;
    }
}
