/*
 * Created on Nov 18, 2003
 *
 */
package jstats;


/**
 * Exception thrown by a Statistic calculation given invalid parameters.
 * 
 * @author Justin Scheiber, David Edelstein
 */
public class StatisticException extends ArithmeticException {

    /**
     * Default constructor
     */
	public StatisticException() { 
		super();
	}

    /**
     * Contructor with an error String describing what caused the Exception to be thrown.
     * @param errorString a descriptive String
     */
	public StatisticException(String errorString) { 
		super(errorString);
	}
}