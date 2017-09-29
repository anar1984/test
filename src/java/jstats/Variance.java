/*
 * Created on Nov 18, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package jstats;

/**
 * @author justin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Variance {
	
	public static double chebyshev(double sd)
	{
		return 1 - (1/Math.pow(sd, 2));
	}
	
	/**
	* Computes the coefficient of variation.  Note that 'sd' and 'mean' must come
	* from the same data.
	* @param sd The standard deviation of a set of data.
	* @param mean The mean of a set of data.
	*/
	public static double coefficientOfVariation(double sd, double mean) throws StatisticException
	{
		return (sd / mean) * 100;
	}
	
	/**
	* Computes the coefficient of variation.  Used to express standard deviation 
	* independent of units of measure.
	* @param values An array of doubles containing a sample from a population.
	* @return coefficient of variation from a sample
	*/
	public static double coefficientOfVariation(double[] values) throws StatisticException
	{
		double mean = Mean.arithmeticMean(values);
		double sd   = sampleStandardDeviation(values);
		return (sd / mean) * 100;
	}
	
	/**
	* Determines relationship between the mean and the median.  This reflects
	* how the data differs from the normal bell shaped distribution.  Note that 
	* 'mean', 'median' and 'sd' all must be computed from the same sample.
	* @param mean  The mean of the sample.
	* @param median The median of the sample.
	* @param sd  The standard deviation of the sample.
	* @return the measure of skewness
	*/
	public static double pearsonianSkewness(double mean, double median, double sd) throws StatisticException
	{
		return (3 * (mean - median)) / sd;
	}
	/** Computes the skewness factor from a sample of data.
	* @param values A list of doubles containing a sample of data.
	* @return the measure of skewness
	*/
	public static double pearsonianSkewness(double[] values) throws StatisticException
	{
		double mean   = Mean.arithmeticMean(values);
		double median =  Mean.median(values);
		double standardDeviation = sampleStandardDeviation(values);
		return  (3 *( mean - median)) / standardDeviation;
	}
	
	public static double populationStandardDeviation(double[] values) throws StatisticException
	{
		return Math.sqrt(populationVarience(values));
	}
	
	public static double populationStandardUnit(double value, double mean, double sd)throws StatisticException
	{
		return sampleStandardUnit(value, mean, sd);
	}
	
	public static double populationStandardUnit(double[] values, int index) throws StatisticException
	{
		double mean = Mean.arithmeticMean(values);
		double sd   = populationStandardDeviation(values);
		return (values[index] - mean) / sd;
	}
	
	public static double populationVarience(double[] values) throws StatisticException
	{
		double sumOfDeviations = sumDeviations(values);
		
		return sumOfDeviations/values.length;
	}
	
	/**
	* Computes the sample standard deviation.  Used to have no idea */
	public static double sampleStandardDeviation(double[] values) throws StatisticException 
	{ 
		return Math.sqrt(sampleVariance(values));
	}
	
	/**
	* Computes the sample standard unit(aka sample z-score) from a list of doubles. 
	* Used to compute 'value' in terms of standard units.  Note that 'value', 'mean'
	* and 'sd' must be all from the same sample data.
	* @param value A double in the sample for which 
	* @param mean The mean of the sample.
	* @param sd   The standard deviation of the sample.
	* @return 'value' in terms of standard units
	*/
	public static double sampleStandardUnit(double value, double mean, double sd)
	{
		return (value - mean) / sd;
	}
	
	/**
	* Computes sample standard unit for the value at 'index' in an array of doubles.
	* @param values An array of doubles.
	* @param index An integer that indexes into array 'values', at which the standard
	* unit will be computed.
	*/
	public static double sampleStandardUnit(double[] values, int index) throws StatisticException
	{
		double mean = Mean.arithmeticMean(values);
		double sd   = sampleStandardDeviation(values);
		return (values[index] - mean) / sd;
	}
	
	/**
	* Computes sample variance. Do not use for population varience.
	* @param values A list of doubles that does not represent a full population -- but a sample instead.
	* @return sample varience
	*/
	public static double sampleVariance(double[] values) throws StatisticException 
	{ 
		if(values.length < 2)
			throw new StatisticException("Array is too small, must have at least 2 elements");
		
		return sumDeviations(values)/(values.length-1);
	}
	
	/**
	* Computes the sum of deviations (or Sxx).
	* @param values An array of doubles which constitute a sample from a larger population.
	* @return sum of squares(Sxx) aka sum of deviations
	*/
	public static double sumDeviations(double [] values) throws StatisticException 
	{ 
		double mean  = 0.0;
		double sigma = 0.0;
		int i;
		
		if(values.length == 0)
			throw new StatisticException("Array is empty");
	
		mean = Mean.arithmeticMean(values);
		for(i=0; i<values.length; i++)  
			sigma += Math.pow((values[i] - mean), 2); 
		
		return sigma;
	}
	
	// Sxy //
	public static double regressionSumSquares(double[] x, double[] y) throws StatisticException
	{ 
		int i;
		double sigma = 0.0;
		
		if(x.length == 0)
			throw new StatisticException("Array is empty.");
			
		if(x.length != y.length)
			throw new StatisticException("Arrays must be of equal size.");
		
		for(i=0; i<x.length; i++)
			sigma += x[i] * y[i];
		
		return sigma - ((Statistic.summation(x) * Statistic.summation(y)) / x.length);
	}
	
	// Syy
	public static double totalSumSquares(double[] y) throws StatisticException 
	{
		double sigma = 0.0;
		
		if(y.length == 0)
			throw new StatisticException("Array is empty.");
		
		for(int i=0; i<y.length; i++)
			sigma += Math.pow(y[i], 2);
		
		
		return sigma - ( Math.pow(Statistic.summation(y), 2) / y.length );
	}
	
	// Se
	public static double standardError(double[] x, double[] y) throws StatisticException
	{
		return Math.sqrt( (totalSumSquares(y) - (Math.pow(regressionSumSquares(x, y), 2) / sumDeviations(x) )) 
			/ (x.length - 2) );
	}

}
