package net.remgant.math;

public class ComplexDouble extends ComplexNumber
{
  public double real;
  public double imaginary;

  public ComplexDouble()
  {
      real = 0.0;
      imaginary = 0.0;
  }

  public ComplexDouble(double r, double i)
    {
      real = r;
      imaginary = i;
    }

  public ComplexDouble(double r)
    {
      real = r;
      imaginary = 0.0;
    }

  public ComplexDouble(int r)
    {
      real = (double)r;
      imaginary = 0.0;
    }

  public ComplexNumber add(ComplexNumber c)
    {
      ComplexDouble cd = (ComplexDouble)c;
      return new ComplexDouble(this.real+cd.real,this.imaginary+cd.imaginary);
    }

  public ComplexNumber subtract(ComplexNumber c)
    {
      ComplexDouble cd = (ComplexDouble)c;
      return new ComplexDouble(this.real-cd.real,this.imaginary-cd.imaginary);
    }

  public ComplexNumber multiply(ComplexNumber c)
    {
      ComplexDouble cd = (ComplexDouble)c;
      double r = (this.real * cd.real) - (this.imaginary * cd.imaginary);
      double i = (this.real * cd.imaginary) + (cd.real * this.imaginary);
      return new ComplexDouble(r,i);
    }

  public ComplexNumber divide(ComplexNumber c)
    {
      ComplexDouble cd = (ComplexDouble)c;
      double d = cd.real * cd.real + cd.imaginary * cd.imaginary;
      double r = ((this.real * cd.real) + (this.imaginary * cd.imaginary))/d;
      double i = ((this.imaginary * cd.real) - (this.real * cd.imaginary))/d;
      return new ComplexDouble(r,i);
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
      double d = Math.atan2(imaginary,real) / (2 * Math.PI) * 360.0;
      return d >= 0.0 ? d : d + 360.0;
    }

  public ComplexNumber pow(int n)
    {
      if (n == 0)
	return new ComplexDouble(1.0,0.0);
      if (n == 1)
	return this;

      double r = this.real;
      double i = this.imaginary;
      double rr,ii;
      switch(n)
	{
	case 0:
	  return new ComplexDouble(1.0,0.0);
	case 1:
	  return this;
	case 2:
	  rr = r*r - i*i;
	  ii = 2.0*r*i;
	  return new ComplexDouble(rr,ii);
	case 3:
	  rr = r*r*r - 3.0*r*i*i;
	  ii = 3.0*r*r*i - i*i*i;
	  return new ComplexDouble(rr,ii);
	case 4:
	  rr = Math.pow(r,4.0) - 6.0*r*r*i*i + Math.pow(i,4.0);
	  ii = 4.0*Math.pow(r,3.0)*i - 4.0*r*Math.pow(i,3.0);
	  return new ComplexDouble(rr,ii);
	case 5:
	  rr = Math.pow(r,5.0) - 10*Math.pow(r,3.0)*i*i + 5.0*r*Math.pow(i,4.0);
	  ii = 5.0*Math.pow(r,4.0)*i - 10.0*r*r*Math.pow(i,3.0) + 
	    Math.pow(i,5.0);
	  return new ComplexDouble(rr,ii);
	default:
	  ComplexDouble d = new ComplexDouble();
	  d.real = this.real;
	  d.imaginary = this.imaginary;
	  for (int j=1; j<n; j++)
	    d = (ComplexDouble)d.multiply((ComplexNumber)this);
	  return d;
	}
    }

  public String toString()
    {
      if (real == 0.0 && imaginary == 0.0)
	return "0.0";
      StringBuffer sb = new StringBuffer();
      if (real != 0.0)
	sb.append(real);
      if (imaginary > 0.0 && real != 0.0)
	sb.append("+");
      if (imaginary != 0.0)
	{
	  sb.append(imaginary);
	  sb.append("i");
	}
      return sb.toString();
    }

  public boolean equals(Object o)
    {
      if (!(o instanceof ComplexDouble))
	return false;
      ComplexDouble c = (ComplexDouble)o;
      return this.real == c.real && this.imaginary == c.imaginary;
    }
}
