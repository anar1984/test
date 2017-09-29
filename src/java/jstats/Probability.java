/*
 * Created on Nov 19, 2003
 *
 */
package jstats;


/**
 * The Probability class deals with probability calculations. A valid probability must always be a number >= 0.0 and <= 1.0.
 * Most distributions have requirements in order to calculate a valid distribution. A <code>StatisticException</code> will be thrown
 * when a distribution is given arguments that do not meet that distribution's requirements. 
 * 
 * @version 0.2.3
 * @author Justin Scheiber, David Edelstein
 *
 */
public abstract class Probability {

	/**
	 * <p>Calculates a binomial distribution probability.  Trials must be independent and have a fixed probability for
	 * each one.</p> 
	 * <p><code>P(x) = n!/(n-x)!x! * p^x * (1-p)^(n-x)</code></p>
	 * <p>(Note that for n = 0, x = 0, the probability will always be 1.00.)</p>
	 * <h4>Requirements:</h4>
	 * <p><code>probability</code> is a valid probability; <code>trials</code> <= <code>successes</code>
	 * @param probability the probability of any one trial succeeding (p)
	 * @param trials the number of trials to perform (n)
	 * @param successes the number of successes expected as an outcome (x)
	 * @return the probability of the given number of successes out of the given number of trials (P(x))
	 * @throws StatisticException if above requirements are not met
	 */
	public static double binomialDistribution(double probability, long trials, long successes) throws StatisticException {
		if (successes > trials)  {
			throw new StatisticException("The number of successes exceeds the number of trials."); 
		}
		Probability.validateProbability(probability);

		return Statistic.combination(trials, successes)* Math.pow(probability, successes) * 
			Math.pow(1.0 - probability, trials - successes);
	}

	/**
	 * <p>Computes a geometric probability distribution. The probability of getting the first success on the xth trial
	 * is:</p>
	 * <p><code>P(x) = p(1-p)^(x-1)</code></p>
	 * <h4>Requirements:</h4>
	 * <p><code>probability</code> is valid, <code>trial</code> > 0
	 * @param probability the probability of success on any one trial (p)
	 * @param trial the trial where the first success is expected (x)  
	 * @return the probability of the first success occurring on the given trial (P(x))
	 * @throws StatisticException if above requirements are not met
	 */
	public static double geometricDistribution(double probability, long trial) throws StatisticException {
		validateProbability(probability);
		if (trial < 1) {
		    throw new StatisticException("xth trial or higher must be >= 1");
		}
		double prob = 0.0;
		if (probability > 0.0) {
			prob = probability * Math.pow(1.0 - probability, trial - 1);
		}
		return prob;
	}
	
	/**
	 * <p>Calculates a hypergeometric distribution. When sampling n objects without replacement from a population with 
	 * A objects of one type and B objects of the other type, the probability of getting x objects of type A and n-x
	 * objects of type B is:
	 * <p><code>A!/(A-x)!x! * B!/(B-n+x)!(n-x)! / (A+B)!/(A+B-n)!n!</code>
	 * <p>or
	 * <p><code>Comb(a, x) * Comb(b, n-x)/Comb(a+b, n)</code>
	 * <p>Another way to express this is with a population of N, consisting of r elements of one type and N-r elements of the
	 * other type. In this case the formula is:
	 * <p>
	 * <code>Comb(r, x) * Comb(N-r, n-x)/Comb(N, n)</code>
	 * <p>Use n-r and r, respectively, for the arguments <code>population_a</code> and <code>population_b</code>.
	 * <h4>Requirements:</h4>
	 * <p>All arguments are whole numbers, <code>samples</code> <= <code>population_a</code> + <code>population_b</code>,
	 * <code>expected</code> <= <code>samples</code>
	 * @param population_a number of objects of type A
	 * @param population_b number of objects of type B
	 * @param samples number of samples taken
	 * @param expected desired number of type A objects
	 * @return the probability of getting x objects of type A
	 * @throws StatisticException if above requirements are not met
	 */
	public static double hyperGeometricDistribution(long population_a, long population_b, long samples, long expected) throws StatisticException {
	    long population = population_a + population_b;
	    double probability = 0.0;
	    // we have to handle a number of anomolous cases here so that the final formula doesn't throw an Exception
	    if (population_a < 0 || population_b < 0 || samples < 0 || expected < 0) {
	        throw new StatisticException("illegal negative parameter: " + population_a + ", " + population_b + ", " + samples + ", " + expected);
	    } else if (samples > population) {
	        throw new StatisticException("Sample size " + samples + " is greater than population size " + population);
	    } else if (expected > population_a) {
	        throw new StatisticException("Desired quantity " + expected + " exceeds population of a: " + population_a);
	    } else if (expected > samples) {
	        throw new StatisticException("Desired quantity " + expected + " exceeds sample size " + samples);
	    } else if (population_b == 0) {
	        if (samples == expected) {
	            probability = 1.0;
	        } // otherwise will automatically be 0
	    } else if ( samples > population_b && expected < (samples - population_b)) {
	        // this is impossible (if samples > b then minimum number of a's is n-b), so probability stays 0.0
	    } else {
			probability = (Statistic.combination(population_a, expected) * Statistic.combination(population_b, samples - expected)) /
			Statistic.combination(population, samples);
	    }
		return probability;
	}

	/**
	 * Calculates a negative hypergeometric distribution. Given a population consisting of A elements of one kind and B elements of another,
	 * what is the probability of selecting x samples before selecting n elements of type A? (I.e., when trying to get
	 * n elements of type A, what is the probability that the nth element of type A will be selected on the xth sample?) 
	 * <p><code>Comb(n+x-1, n-1) * Comb(N-n-x, a-n)/(N/a)</code>
	 * <h4>Requirements:</h4>
	 * <p>All arguments are whole numbers, <code>selections</code> > 0, <code>selections</code> >= <code>expected</code> 
	 * 
	 * <p>Note that if selections > population_a or expected > population_b, the probability will always be 0.0.
	 * @param population_a number of elements of type A
	 * @param population_b number of elements of type B
	 * @param selections number of elements of type A to be selected (n)
	 * @param expected number of samples before getting a number of A's equal to selections (x)
	 * @return the probability that the nth A will occur on the xth selection
	 * @throws StatisticException if above requirements are not met
	 */
	public static double negativeHypergeometricDistribution(long population_a, long population_b, long selections, long expected) throws StatisticException	{
	    long population = population_a + population_b;
	    if (population_a < 0 || population_b < 0 || expected < 0) {
	        throw new StatisticException("illegal negative parameter: " + population_a + ", " + population_b + ", " + selections + ", " + expected);
	    } else if (selections < 1) {
	        throw new StatisticException("selections must be > 0");
	    } else if (expected < selections) {
	        throw new StatisticException("number of samples " + selections + " cannot be less than expected seletions" + expected);
	    }
	    double probability = 0.0;
    	if (selections <= population_a && expected <= population_b) {
		    probability = Statistic.combination(selections + expected - 1, selections -1) * 
		    		Statistic.combination(population - selections - expected, population_a - selections) /
		    		Statistic.combination(population, population_a);
    	}
	    return probability;
	}

	/**
	 * <p>Calculates a multinomial probability distribution. Also known as a <i>multivariate binomial distribution</i>.
	 * Given mutually exclusive outcomes O1...Oz, with P(O1)....P(Oz), in n independent trials, 
	 * the probability of X1 outcomes of O1.. to Xz outcomes of Oz is:
	 * <p><code>P(x) = n!/(X1!)(X2!)....(Xz!) * P(O1)^X1 * ..... P(Oz)^Xz</code>.
	 * <p>By default, the sum of all probabilities must be <em>exactly</em> 1.00. You may use this method with a delta argument
	 * to allow for a degree of difference from 1.00 to account for rounding errors.
	 * @param trials number of independent trials (n)
	 * @param probabilities array of probabilities (P(O1) to P(Oz))
	 * @param outcomes array of expected outcomes (X1....Xz)
	 * @return probability of the expected outcomes (P(x))
	 * @throws StatisticException for invalid probabilities, if the sum all probabilities is not 1.00, if #probabilities != #outcomes, 
	 */
	public static double multinomialDistribution(long trials, double[] probabilities, long[] outcomes) throws StatisticException {
	    return multinomialDistribution(trials, probabilities, outcomes, 0.0);
	}

	/**
	 * This is the same function as the one without the delta argument, but allows specification of margin of error
	 * for the sum of all probabilities. The delta is the degree to which the sum of all probabilities can differ from 1.00.
	 * This allows use of probability values with high decimal places, where the sum, due to Java's rounding, might not be exactly 1.00. 
	 * Note that the higher the value for delta, the greater the likelihood of an inaccurate calculation. It is recommended delta not be
	 * greater than 0.01.
	 * <p>Delta tolerance is <strong>not</strong> guaranteed to be accurate beyond 6 decimal places (1E-6)
	 * @param trials number of independent trials (n)
	 * @param probabilities array of probabilities (P(O1) to P(Oz))
	 * @param outcomes array of expected outcomes (X1....Xz)
	 * @param delta the permissible delta between the sum of probabilities and 1.00
	 * @return probability of the expected outcomes (P(x))
	 * @throws StatisticException for invalid probabilities, if the sum of all probabilities is not 1.00, if trials < 1 or is not equal to the sum of outcomes,
	 * if #probabilities != #outcomes
	 */
	public static double multinomialDistribution(long trials, double[] probabilities, long[] outcomes, double delta) throws StatisticException {
	    if (trials < 1) {
	        throw new StatisticException("trials must be > 0");
	    }
		// make sure the sum of outcomes equals the trials
	    double sumtrials = Statistic.summation(outcomes);
	    if (trials != sumtrials) {
	        throw new StatisticException("sum of outcomes " + sumtrials + " is not equal to the number of trials: " + trials);
	    }
	    // make sure lengths match
		if(outcomes.length != probabilities.length) {
			throw new StatisticException("number of outcomes must equal the number of probabilities");
		}
		// make sure all probabilities are valid
		validateProbability(probabilities);
		// make sure probabilities add up to within delta of 1.00
		double probsum = Statistic.summation(probabilities);
	    double errorDelta = 1E-6;   // delta we need internally because Math.abs tends to create tiny rounding errors
		if (Math.abs(1.00 - probsum) > (delta + errorDelta)) {
		    throw new StatisticException("probabilities sum " + probsum + "; variance=" + Math.abs(1.00 - probsum) + " is greater than the allowable delta of " + delta);
		}
		
		double denominator  = 1.0;
		double multiplicand = 1.0;

		// X1!.....Xz!
		for (int i = 0; i < probabilities.length; i++) {
			denominator *= Statistic.factorial(outcomes[i]);
		}
		for (int j = 0; j < probabilities.length; j++) {
			multiplicand *= Math.pow(probabilities[j], outcomes[j]);
		}
		return (Statistic.factorial(trials) / denominator ) * multiplicand;
	}

	/**
	 * <p>Calculates a multivariate hypergeometric distribution. Given a set of N objects of <b>I</b> different types,
	 * with <b>R<i>i</i></b> elements for each i (i = 1...I), sampling <em>without replacement</em> <i>n</i> times,
	 * the probability of finding <b>X<i>i</i></b> objects for each i (i = 1...I) is:
	 * <p><code>Comb(R1, X1)/Comb(N, n) * ...... Comb(RI, XI)/Comb(N, n)</code>
	 * @param populations an array with length = number of different items, each value containing the number of elements of that type (Ri)
	 * @param expected an array of expected sample outcomes, each value containing the number of expected items of the corresponding population array element type (Xi) ("n" equals the sum of all outcomes)
	 * @return the probability that n samples from populations will result in a set corresponding to outcomes
	 * @throws StatisticException if the length of the populations and outcomes arrays are not the same, if any population value is 0, if samples <= 0, or
	 * if any outcome value is greater than the population of the corresponding item
	 */
	public static double multivariateHypergeometricDistribution(long[] populations, long[] expected) throws StatisticException	{
		if (populations.length != expected.length) {
			throw new StatisticException("population and outcomes must be arrays of equal length.");
		} else if (populations.length < 1) {
		    throw new StatisticException("Cannot calculate distribution from empty arrays");
		}
		// value N
		double population = Statistic.summation(populations);
		double samples = Statistic.summation(expected);
		if (population == 0) {
		    throw new StatisticException("Population size is 0");
		} else if (samples == 0) {
		    throw new StatisticException("Sample size is 0");
		} else if (population < samples) {
		    throw new StatisticException("Number of samples " + samples + " is greater than total population " + population);
		}

		double probability = 0.0;
		// shortcut for 1 type of item -- outcome must always be equal to the samples taken
		if (populations.length == 1) {
		    probability = 1.0;
		} else {
			double denominator = Statistic.combination(population, samples);

			double numerator   = 1.0;
			for(int i = 0; i < populations.length; i++) {
			    try {
					numerator *= Statistic.combination(populations[i], expected[i]);
			    } catch (StatisticException statex) {
			        throw new StatisticException("sample[" + i +"] " + expected[i] + " is greater than population " + populations[i]);
			    }
			}
			probability = numerator/denominator;
		}

		return probability;
	}

	/**
	 * <p>Approximates a binomial probability using a special form of the Poisson Distribution.
	 * Often used when the number of trials is large and the probability is small. The formula is:
	 * <p><code>P(x) = (np)^x * E^(-np)/x!</code>
	 * @param probability the probability of any one trial succeeding (p)
	 * @param trials the number of trials to perform (n)
	 * @param successes the number of successes expected as an outcome (x)
	 * @return the Poisson Approximation of the probability of the given number of successes out of the given number of trials (P(x))
	 * @throws StatisticException if probability is invalid, or successes > trials, or any parameter is negative
	 */
	public static double poissonApproximation(double probability, long trials, long successes) throws StatisticException {
		if (successes > trials)  {
			throw new StatisticException("The number of successes exceeds the number of trials."); 
		}
		validateProbability(probability);
		return ( Math.pow(trials * probability, successes) * Math.pow(Math.E, -(trials * probability) ) /
			Statistic.factorial(successes));
	}

	/**
	 * Computes the probability of a given number of successes given an average number of successes per unit.
	 * The formula for the Poisson Distribution is:
	 * <p><code>P(x) = u^x * e^-x/x!</code>
	 * <p>Where u is the mean number of successes in a given unit.
	 * @param mean the average number of successes (u)
	 * @param successes the expected number of successes (x) 
	 * @return the probability of a given unit resulting in the expected number of success (P(x))
	 * @throws StatisticException if mean <= 0 or successes < 0
	 */
	public static double poissonDistribution(double mean, long successes) throws StatisticException {
	    if (mean <= 0) {
	        throw new StatisticException("Mean must be > 0");
	    } else if (successes < 0) {
	        throw new StatisticException("Successes must be >= 0");
	    }

		return (Math.pow(mean, successes) * Math.exp(-mean)) / Statistic.factorial(successes); 
	}

	/**
	 * Validates that a double is a valid probability (>= 0.0 and <= 1.0).
	 * @param probability a double to test
	 * @return true if a valid probability
	 */
	public static boolean isValidProbability(double probability) {
	    boolean valid = true;
	    try {
	        validateProbability(probability);
	    } catch (StatisticException statex) {
	        valid = false;
	    }
	    return valid;
	}

	/**
	 * Validates that an array of doubles meets the criteria for a valid probability distribution (each value is a
	 * valid probability, and the distribution adds up to 1.00). A delta may be specified to account for rounding errors
	 * in Java when working with doubles; this is the degree to which the sum of the probabilities may vary from 1.0.
	 * The greater the delta, the greater the likelihood of an inaccurate calculation.
	 * @param distribution an array of doubles to test
	 * @param delta the allowable error tolerance
	 * @return true if distribution is a valid probability distribution
	 */
	public static boolean isValidProbabilityDistribution(double[] distribution, double delta) {
	    boolean valid = true;
	    try {
	        validateProbabilityDistribution(distribution, delta);
	    } catch (StatisticException statex) {
	        valid = false;
	    }
	    return valid;
	}

	/**
	 * Identical to the same method with a delta argument, but uses a default delta of 0.0 (i.e., allows no error tolerance).
	 * @param distribution an array of doubles to test
	 * @return true if distribution is a valid probability distribution
	 */
	public static boolean isValidProbabilityDistribution(double[] distribution) {
	    return isValidProbabilityDistribution(distribution, 0.0);
	}

	/**
	 * Confirms that the probability passed into the public functions is proper.
	 * @param probability a probability to validate
	 * @throws StatisticException if probability < 0.0 or > 1.0
	 */
	protected static void validateProbability(double probability) throws StatisticException {
		if(probability < 0.0 || probability > 1.0) {
			throw new StatisticException("Invalid probability: " + probability + "; validity requires 0.0 <= probability <= 1.0");
		}
	}

	/**
	 * Validate an array of probabilities
	 * @param probability an array of doubles
	 * @throws StatisticException if any one probability fails to validate
	 */
	protected static void validateProbability(double[] probability) throws StatisticException {
		for(int i = 0; i < probability.length; i++) {
		    validateProbability(probability[i]);
		}
	}

	/**
	 * Valid a probability distribution (an array of probabilities, each of which must be a valid probability,
	 * and the sum of which must add up to 1.00). A delta may be specified allowing a degree of variance from 1.00,
	 * since Java's math may introduce small rounding errors in doubles. It is recommended the delta not be greater than 0.01.
	 * Note that delta difference is not guaranteed to be accurate beyond 1E-6.
	 * @param distribution an array of doubles
	 * @param delta the degree to which the sum of the distribution can vary from 1.00.
	 * @throws StatisticException if the distribution does not meet the requirements of a probability distribution
	 */
	protected static void validateProbabilityDistribution(double[] distribution, double delta) throws StatisticException {
	    validateProbability(distribution);
	    double INTERNAL_DELTA = 1E-6;  // because in Java 1.01 - 1.00 = 0.010000000009!
	    double sum = Statistic.summation(distribution);
	    double diff = Math.abs(sum - 1.00);
		if (diff - delta > INTERNAL_DELTA) {
            throw new StatisticException("Probability distribution does not add up to 1.0");
	    }
	}

	/**
	 * Identical to the same method with a delta, but uses a default delta of 0 (i.e., has no error tolerance).
	 * @param distribution an array of doubles
	 * @throws StatisticException if the distribution does not meet the requirements of a probability distribution
	 */
	protected static void validateProbabilityDistribution(double[] distribution) throws StatisticException {
	    validateProbabilityDistribution(distribution, 0.0);
	}

}