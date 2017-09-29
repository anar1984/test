/*
 * Created on Nov 20, 2003
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
public class Correlation {
	/**
	 * Computes the coefficient of correlation for sample or population data.
	 * @param x
	 * @param y
	 * @return the coeffecient of x and y
	 * @throws StatisticException
	 */
	public static double coefficient(double[] x, double[] y) throws StatisticException
	{
		return Variance.regressionSumSquares(x, y) / 
			Math.sqrt( Variance.sumDeviations(x) * Variance.totalSumSquares(y));
	}
	
	public static double fisherZTransformation(double[] x, double[] y) throws StatisticException
	{	
		return fisherZTransformation(coefficient(x, y));  
	}
	
	public static double fisherZTransformation(double coefficient) throws StatisticException
	{
		Probability.validateProbability(coefficient);
		return 0.5 * (Math.log( (1 + coefficient) / (1 - coefficient) ));
	}
	
	public static double normalCorrelation(double[] x, double[] y, double p) throws StatisticException
	{
		double coefficient = coefficient(x, y);
		double fisherZ     = fisherZTransformation(coefficient);
		double uz          = fisherZTransformation(p);
		
		return (fisherZ - uz) * Math.sqrt(x.length - 3);
	}
	
	public static double multipleCorrelationCoefficient()
	{ return 1.00; }
	
}
