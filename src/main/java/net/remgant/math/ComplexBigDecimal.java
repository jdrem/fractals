package net.remgant.math;
import java.math.BigDecimal;

public class ComplexBigDecimal extends ComplexNumber
{
  public BigDecimal real;
  public BigDecimal imaginary;
  protected final static BigDecimal ZERO = new BigDecimal(0.0);

  public ComplexBigDecimal()
  {
      real = new BigDecimal(0.0);
      imaginary = new BigDecimal(0.0);
  }

  public ComplexBigDecimal(BigDecimal r, BigDecimal i)
    {
      real = r;
      imaginary = i;
    }

  public ComplexBigDecimal(double r, double i)
    {
      real = new BigDecimal(r);
      imaginary = new BigDecimal(i);
    }

  public ComplexBigDecimal(double r)
    {
      real = new BigDecimal(r);
      imaginary = new BigDecimal(0.0);
    }

  public ComplexBigDecimal(int r)
    {
      real = new BigDecimal((double)r);
      imaginary = new BigDecimal(0.0);
    }

  public ComplexNumber add(ComplexNumber c)
    {
      ComplexBigDecimal cd = (ComplexBigDecimal)c;
      return new ComplexBigDecimal(this.real.add(cd.real),
				   this.imaginary.add(cd.imaginary));
    }

  public ComplexNumber subtract(ComplexNumber c)
    {
      ComplexBigDecimal cd = (ComplexBigDecimal)c;
      return new ComplexBigDecimal(this.real.subtract(cd.real),
				   this.imaginary.subtract(cd.imaginary));
    }

  public ComplexNumber multiply(ComplexNumber c)
    {
      ComplexBigDecimal cd = (ComplexBigDecimal)c;
      BigDecimal r = (this.real.multiply(cd.real)).subtract
	((this.imaginary.multiply(cd.imaginary)));
      BigDecimal i = (this.real.multiply(cd.imaginary)).add
	((cd.real.multiply(this.imaginary)));
      return new ComplexBigDecimal(r,i);
    }

  public ComplexNumber divide(ComplexNumber c)
    {
      return null;
    }

  public ComplexNumber square()
    {
      return this.multiply(this);
    }

  public ComplexNumber pow(int n)
    {
      if (n == 0)
	return new ComplexBigDecimal(1.0,0.0);
      if (n == 1)
	return this;
      ComplexBigDecimal d = new ComplexBigDecimal();
      d.real = this.real;
      d.imaginary = this.imaginary;

      for (int i=0; i<n; i++)
	d = (ComplexBigDecimal)d.multiply((ComplexNumber)d);
      return d;
    }

  public double magnitude()
  {
    return ComplexBigDecimal.sqrt(real.multiply(real).add(imaginary.multiply(imaginary))).doubleValue();
  }

  public double angle()
    {
      return 0.0;
    }

  public String toString()
    {
      if (real.equals(ZERO) && imaginary.equals(ZERO))
	return "0.0";
      StringBuffer sb = new StringBuffer();
      if (!real.equals(ZERO))
	sb.append(real);
      if ((imaginary.compareTo(ZERO) > 0) && !real.equals(ZERO))
	sb.append("+");
      if (!imaginary.equals(ZERO))
	{
	  sb.append(imaginary);
	  sb.append("i");
	}
      return sb.toString();
    }

  public boolean equals(Object o)
    {
      if (!(o instanceof ComplexBigDecimal))
	return false;
      ComplexBigDecimal c = (ComplexBigDecimal)o;
      return this.real.equals(c.real) && this.imaginary.equals(c.imaginary);
    }

  public static BigDecimal sqrt(BigDecimal val)
  {
    

    BigDecimal root = new BigDecimal(1.0);
    BigDecimal epsilon = new BigDecimal(0.0001);
    BigDecimal two = new BigDecimal(2.0);
    BigDecimal q;
    for (;;)
      {
	q = val.divide(root,BigDecimal.ROUND_HALF_DOWN);
	if (root.subtract(q).abs().compareTo(epsilon) <= 0)
	  break;
	root = root.add(q).divide(two,BigDecimal.ROUND_HALF_DOWN);
      }
    return root;
  }
}
