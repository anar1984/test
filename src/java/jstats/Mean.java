/*
 * Created on Nov 18, 2003
 *
 */
package jstats;

import java.util.Arrays;

/**
 * Static methods for calculating means and other measures of center.
 * 
 * @author Justin Scheiber, David Edelstein
 */
public abstract class Mean {

    /**
	* Computes a median of an array of values.
	* @param values an array of doubles
	* @return the median of the values array
	* @throws StatisticException for an empty array
	*/
	public static double median(double[] values) throws StatisticException {
		if (values.length == 0) {
			throw new StatisticException("median cannot be computed from an empty values array");
		}
		double median = 0.0;
		if (values.length == 1) {
		    median = values[0];
		} else {
			int index = (values.length / 2);
			double[] newArray = new double[values.length];
			
			// make a new copy of the array //
			System.arraycopy(values, 0, newArray, 0, values.length);
			Arrays.sort(newArray);

			if (((newArray.length) % 2) == 0) {
				// handle special case for even length arrays
				median = (newArray[index-1] + newArray[index]) / 2;
			} else {
				median = newArray[index];
			}
		}
		return median;
	}

	/**
	 * Computes the midrange (the value exactly between the lowest and highest values) from an array of doubles.
	 * @param values an array of doubles
	 * @return the midrange of the values array
	 * @throws StatisticException for an empty array
	 */
	public static double midrange(double[] values) throws StatisticException {
		if (values.length == 0) {
			throw new StatisticException("midrange cannot be computed from an empty values array");
		}
		double midrange = 0.0;
		if (values.length == 1) {
		    midrange = values[0];
		} else {
		    double high = values[0];
		    double low = values[0];
		    for (int i = 1; i < values.length; i++) {
		        if (values[i] > high) {
		            high = values[i];
		        }
		        if (values[i] < low) {
		            low = values[i];
		        }
		    }
		    midrange = (high + low)/2;
		}
		return midrange;
	}

	/**
	* Calculate the arithmetic mean from an array of doubles.
	* @param values An array of doubles.
	* @return arithmetic mean
	* @throws StatisticException for empty array
	*/
	public static double arithmeticMean(double values[]) throws StatisticException {
		return Statistic.summation(values)/values.length;
	}

	/**
	 * Calculate a binomial mean.
	 * @param probability
	 * @param trials
	 * @return the binomial mean
	 * @throws StatisticException
	 */
	public static double binomialMean(double probability, long trials) throws StatisticException
	{
		Probability.validateProbability(probability);
		if(trials < 1)
			throw new StatisticException("The number of trials must be greater than one.");
			
		return probability * trials; 
	}
	
	/**
	* Calculates the geometic mean.  Used to average ratios.
	* @param values An array of doubles containing ratios.
	* @return geometic mean
	*/
	public static double geometricMean(double[] values) throws StatisticException 
	{
		int i;
		double product = 0.0;
	
		if(values.length == 0)
			throw new StatisticException("array is empty");
	
		for(i=0; i<values.length; i++)
			product *= values[i];
	
		return Math.pow(product, (1/values.length));
	}
	
	/**
	* Calculates the grand mean. Used to average multiple means with weights.
	* @param weights An array of doubles containing weights that correspond to each of the means.
	* @param means An array of doubles containing means from different observations. 
	* @return grand mean 
	*/
	public static double grandMean(double[] weights, double[] means) throws StatisticException 
	{ 
		int i;
		double[] tmpArray = new double[weights.length];
		
		if(weights.length != means.length)
			throw new StatisticException("Arrays do not have the same length");
	
		for(i=0; i<weights.length; i++) 
			tmpArray[i] = weights[i] * means[i];
	
		return Statistic.summation(tmpArray)/Statistic.summation(weights);
	}
	
	/** 
	* Calculates a harmonic mean.  Used to average frequencies. 
	* @param values An array of doubles containing frequencies.
	* @return harmonic mean
	*/
	public static double harmonicMean(double[] values) throws StatisticException
	{ 
		int i;
		double[] tmpArray = new double[values.length];
		
		if(values.length < 1) 
			throw new StatisticException("array is empty");
	
		for(i=0; i<values.length; i++) 
			tmpArray[i] = 1/values[i];
	
		return values.length/Statistic.summation(tmpArray);
	}
	public static double hypergeometricMean(double n, double a, double b)
	{
		return (n * a) / (a + b);
	}

	/**
	 * Calculates the mean of a probability distribution.
	 * @param probability
	 * @param values
	 * @return the mean of a probability distribution
	 * @throws StatisticException
	 */
	public static double probabilityMean(double probability, double[] values) throws StatisticException
	{
		double pm = 0.0;
		
		if(values.length == 0)
			throw new StatisticException();
		
		for(int i=0; i<values.length; i++)
			pm += (values[i] * probability);
		
		return pm;
	}

	/**
	* Caculate a weighted mean.  Used when the values in a set of 
	* data do not share equal importance.
	* @param weights Weights to use when calculating the mean.
	* @param values  Data on which to perform the weighted mean.
	* @return weighted mean
	*/
	public static double weightedMean(double[] weights, double[] values) throws StatisticException
	{
		int i;
		double[] tmpArray	= new double[weights.length];
	
		if(weights.length != values.length)
			throw new StatisticException("arrays do not have the same length");
	
		for(i=0; i<weights.length; i++)
			tmpArray[i] = values[i] * weights[i];
	
		return Statistic.summation(tmpArray)/Statistic.summation(weights);
	}
	
}