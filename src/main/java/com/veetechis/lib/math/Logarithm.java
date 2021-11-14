package com.veetechis.lib.math;


/**
 * <p>
 * This class calculates logarithm of a number with a specified number base.
 * </p>
 *
 * @author		pendraconx@gmail.com
 */
public class Logarithm
{
	public static void main( String[] args )
	{
		if (args.length != 2) {
			System.out.println( "A value and logarithmic number base are required " +
				"e.g. (128 2) to calculate binary log of 128." );
		}
		else {
			if (args[1].equals("e")) {
				args[1] = "2.718";
			}
			double value = Double.parseDouble( args[0] );
			double base = Double.parseDouble( args[1] );
			System.out.println( logx(value, base) );
		}
	}


    /**
	 * <p>
	 * Returns the logarithm (exponential root) of the given value with the
	 * specified number base.
	 * </p>
	 *
	 * @param  value			the value to calculate logarithm.
	 * @param  base				the number base to calculate.
	 * @return					the logarithmic value.
     */
    public static double logx( double value, double base )
    {
		double logv = 0.0;

		if (base == 10) {
			logv = Math.log10( value );
		}
		else if (base == 2.718) {  // e
			logv = Math.log( value );
		}
		else {
			double numer = Math.log( value );
			double denom = Math.log( base );
			logv = numer / denom;
		}

		return logv;
    }
    
    /**
	 * <p>
	 * Returns the natural logarithm of the given value.
	 * </p>
     * 
	 * @param  value			the value to calculate logarithm base 'e' (2.718).
	 * @return					the logarithmic value.
     */
    public static double loge( double value )
    {
		return logx( value, 2.718 );
	}
	
    /**
	 * <p>
	 * Returns the logarithm of the given value for number base 10.
	 * </p>
     * 
	 * @param  value			the value to calculate logarithm base 10.
	 * @return					the logarithmic value.
     */
    public static double log10( double value )
    {
		return logx( value, 10 );
	}
	
    /**
	 * <p>
	 * Returns the binary logarithm of the given value.
	 * </p>
     * 
	 * @param  value			the value to calculate logarithm base 2.
	 * @return					the logarithmic value.
     */
    public static double log2( double value )
    {
		return logx( value, 2 );
	}
	
} // End of class: +com.veetechis.lib.math.Logarithm
