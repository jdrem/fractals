package net.remgant.math;

public abstract class ComplexNumber
{
  public final static int DOUBLE_TYPE=1;
  public final static int INTEGER_TYPE=2;
  public final static int BIGDECIMAL_TYPE=3;
  
  public static ComplexNumber getInstance(double r, double i, int type)
  {
    switch (type)
      {
      case INTEGER_TYPE:
	return (ComplexNumber)new ComplexInteger((int)r,(int)i);
      case BIGDECIMAL_TYPE:
	return (ComplexNumber)new ComplexBigDecimal(r,i);
      default:
	return (ComplexNumber)new ComplexDouble(r,i);
      }
   }
 
  abstract public ComplexNumber add(ComplexNumber c);
  abstract public ComplexNumber subtract(ComplexNumber c);
  abstract public ComplexNumber multiply(ComplexNumber c);
  abstract public ComplexNumber divide(ComplexNumber c);
  abstract public ComplexNumber square();
  abstract public double magnitude();
  abstract public double angle();
  abstract public ComplexNumber pow(int n);
}
