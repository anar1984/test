/*
 * Created on Nov 18, 2003
 *
 */

package jstats;

import java.util.Locale;

/**
 * Statistic is the base class of the jstats package, providing a number of static methods useful for collections of data.
 * 
 * @version 0.2.3
 * @author Justin Scheiber, David Edelstein
 */
public class Statistic {
    /** Numbers greater than this will have factorials computed using Stirling's Approximation */
    public final static int STIRLING_THRESHOLD = 50;
    
	/* class vars */
	public static Locale locale = null;

	/**
	 * <p>Performs factorial (n!) function. Uses <i>Stirling's Approximation</i> for n > 50.</p>
	 * <p>Note that n, while a <b>double</b>, must be an integer value; any fractional component of n is discarded before the factorial
	 * is computed; i.e., the factorial of 6.8 will be computed as a factorial of 6 (factorial(6.8) == 6!).</p>
	 * @param n number to factor
	 * @return the factorial of n
	 * @throws StatisticException for n < 0
	 */
	public static double factorial(double n) throws StatisticException {
		double result = 1;

		if (n < 0) {
			throw new StatisticException("Cannot compute factorial for negative number");
		} else if (n > STIRLING_THRESHOLD ) {
			result = StirlingApproximation(n);
		} else {
			for (int i = 2; i <= n; i++) {
				result *= i;
			}
		}
		return result;
	}

	/**
	 * Stirling's Approximation for a factorial: For large values of n, n! =~ n^n * e^-n * 2*PI*n.
	 * @param n number to factor
	 * @return Stirling's Approximation for n!
	 */
	public static double StirlingApproximation(double n) {
		return Math.pow(n, n) * Math.exp(-n) * Math.sqrt(2 * Math.PI * n);
	}

	/**
	 * <p>Permutation function:</p>
	 * <p><code>nPr = n!/(n-r)!</code></p> 
	 * <p>Note that n and r, while <b>doubles</b>, must actually be integer values and are calculated as such; any fractional
	 * component is discarded</p>
	 * @param n number of items from which to select
	 * @param r number of items in selection
	 * @return number of permutations P(n,r)
	 * @throws StatisticException if n < r or r < 0 or n < 0
	 */
	public static double permutation(double n, double r) throws StatisticException {
	    double perm;
		if (n < 0 || r < 0 || n < r) {
			throw new StatisticException("Illegal parameters (" + n + "," + r + "): Permutation function requires n >= r and n >= 0 and r >= 0");
		} else if (n == r) {
		    perm = factorial(n);  // shortcut -- n == 0 will return quickly from factorial function
		} else {
		    perm = (factorial(n) / factorial(n - r));
		}

		return perm;
	}


	/**
	 * <p>Combination function:</p>
	 * <p><code>nCr = n!/(n-r)!r!</code></p> 
	 * <p>Note that n and r, while <b>doubles</b>, must actually be integer values and are calculated as such; any fractional
	 * component is discarded</p>
	 * @param n number of items from which to select
	 * @param r length of combination sequence
	 * @return number of combinations C(n,r)
	 * @throws StatisticException if n < r or n < 0 or r < 0
	 */
	public static double combination(double n, double r) throws StatisticException {
	    double comb = 0.0;
		if (n < 0 || r < 0 || n < r) {
			throw new StatisticException("Illegal parameters (" + n + "," + r + "): Combination function requires n >= r and n >= 0 and r >= 0");
		} else if (n == r) {
	        // shortcut -- also covers the n == 0 case since r by above rule must also == 0
	        comb = 1;
	    } else {
			comb =(permutation(n, r) * (1.0/factorial(r)));
	    }
	    return comb;
	}

	/**
	* Sum of all the values in an array.
	* @param values an array of doubles.
	* @return sum of all elements in the array
	* @throws StatisticException for an empty array
	*/
	public static double summation(double values[]) throws StatisticException {
		int i;
		double sigma = 0.0;

		if (values.length < 1) {
			throw new StatisticException("Array is empty");
		}

		for (i = 0; i < values.length; i++) {
			sigma += values[i];
		}

		return sigma;
	}

	/**
	* Sum of all the values in an array.
	* @param values an array of longs.
	* @return sum of all elements in the array
	* @throws StatisticException for an empty array
	*/
	public static double summation(long values[]) throws StatisticException {
		int i;
		double sigma = 0.0;

		if (values.length < 1) {
			throw new StatisticException("Array is empty");
		}

		for (i = 0; i < values.length; i++) {
			sigma += values[i];
		}

		return sigma;
	}

}